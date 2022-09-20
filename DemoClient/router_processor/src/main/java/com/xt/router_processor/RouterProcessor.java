package com.xt.router_processor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Symbol;
import com.xt.router_api.Route;
import com.xt.router_api.RouteMethod;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * 动态生成class，把所有带Router注解的类加入到生成类中。
 * 但是这个有些问题，就是每个注解都会执行一次process方法，如何避免重复生成的问题。
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xt.router_api.Route")
@SupportedOptions("moduleName")
public class RouterProcessor extends AbstractProcessor {
    Filer filer;
    String TAG = "RouterProcessor";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        System.out.println(TAG + ":process,annotations:" + annotations.size());
        if (annotations.isEmpty()) {
            return false;
        }
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "TAG");
        //所有带Route注解的类
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Route.class);
        Map<String, String> map = new HashMap<>();
        for (Element e : elementsAnnotatedWith) {
            Route annotation = e.getAnnotation(Route.class);
            Element enclosingElement = e.getEnclosingElement();
            String packageName = ProcessorUtils.getPackage(enclosingElement);
            generatedProxyClass(packageName, e.getSimpleName().toString(), e);

            map.put(annotation.moduleName(), packageName + "." + e.getSimpleName().toString());
        }
        generatedClass("RegisterRouter", map);
        return true;
    }

    private void generatedProxyClass(String packageName, String className, Element classElement) {
        //获取所有方法
        List<? extends Element> enclosedElements = classElement.getEnclosedElements();
        StringBuilder builder = new StringBuilder();
        String localName = "m" + className;
        String proxyName = className + "Proxy";

        addLine(builder, "package " + packageName + ";");
        addLine(builder, "public class " + proxyName + " implements RouterProxyInter {");
        addLine(builder, "    " + className + " " + localName + ";");
        addLine(builder, "    public " + proxyName + "(" + className + " " + localName + "){");
        addLine(builder, "        " + "this." + localName + " = " + localName + ";");
        addLine(builder, "    }");
        addLine(builder, "");
        addLine(builder, "    public void handleAction(String action, Object args) {");
        boolean isElse = false;
        for (Element e : enclosedElements) {
            RouteMethod annotation = e.getAnnotation(RouteMethod.class);
            if (annotation == null) {
                continue;
            }
            /**
             * 参数类型转换
             * 首先，获取注解标记的方法类型
             *
             */
            String paramsType = getType(e);

            String typeTransforLine = "";
            if (paramsType.equals("java.lang.String")) {
                typeTransforLine = paramsType + " local=String.valueOf(args);";
            } else if (paramsType.equals("java.lang.Integer")) {
                typeTransforLine = paramsType + " local=Integer.parseInt(args);";
            } else {
                typeTransforLine = "Object local=args;";
            }
            String methodName = e.getSimpleName().toString();
            String methodKey = annotation.methodKey();
            //方法中如果带RouteMethod注解，则生成
            if (!isElse) {
                isElse = true;
                builder.append("        if");
            } else {
                builder.append("        else if");
            }
            addLine(builder, "(\"" + methodKey + "\".equals(action)) {");
            addLine(builder, "            " + typeTransforLine);
            addLine(builder, "            m" + className + "." + methodName + "(local);");
            addLine(builder, "        }");
        }
        addLine(builder, "    }");
        addLine(builder, "}");
//        System.out.println(TAG + ",builder:\n" + builder);
        JavaFileObject sourceFile = null;
        try {
            sourceFile = filer.createSourceFile(proxyName);
            Writer writer = sourceFile.openWriter();
            writer.append(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对注解中的内容生成类
     * 注册key，类名
     */
    private void generatedClass(String fileName, Map<String, String> moduleMap) {
        StringBuilder builder = new StringBuilder();
        addLine(builder, "package com.xt.client;");

        addLine(builder, "import java.util.HashMap;");
        addLine(builder, "import java.util.Map;");
        addLine(builder, "import java.util.Map;");
        addLine(builder, "import com.xt.client.function.route.RouterBase;");
        addLine(builder, "import com.xt.client.function.route.RegisterRouterInter;");

        for (String value : moduleMap.values()) {
            addLine(builder, "import " + value + ";");
        }


        addLine(builder, "public class " + fileName + " extends RegisterRouterInter {");
        addLine(builder, "  @Override");
        addLine(builder, "  public void init() {");

        for (String key : moduleMap.keySet()) {
            String name = moduleMap.get(key);
            String className = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
            String createLine = "      " + name + "Proxy " + className + " = new " + name + "Proxy(new " + name + "());";
            String addLine = "      map.put(\"" + key + "\", " + className + ");";
            addLine(builder, createLine);
            addLine(builder, addLine);
        }

        addLine(builder, "  }");
        builder.append("}");
//        System.out.println(TAG + ":RouterProcessor:" + builder.toString());
        try {
            JavaFileObject sourceFile = filer.createSourceFile("RegisterRouter");
            Writer writer = sourceFile.openWriter();
            writer.append(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLine(StringBuilder builder, String line) {
        builder.append(line);
        builder.append("\n");
    }

    private String getType(Element methodElement) {
        Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) methodElement;
        com.sun.tools.javac.util.List<Symbol.VarSymbol> params = methodSymbol.params;
        for (Symbol.VarSymbol varSymbol : params) {
            return varSymbol.type.toString();
        }
        return Object.class.toString();
    }
}