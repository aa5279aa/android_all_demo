package com.xt.router_processor;

import com.google.auto.service.AutoService;
import com.xt.router_api.Route;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
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
    private List<String> list = new ArrayList<>();
    Writer writer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        try {
            JavaFileObject sourceFile = filer.createSourceFile("RegisterRouter");
            writer = sourceFile.openWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        System.out.println(TAG + ":process");
        if (annotations.isEmpty()) {
            return false;
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "TAG");
        //所有带Route注解的类
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Route.class);
        Map<String, String> map = new HashMap<>();
        for (Element e : elementsAnnotatedWith) {
            Route annotation = e.getAnnotation(Route.class);
            Element enclosingElement = e.getEnclosingElement();
            String aPackage = ProcessorUtils.getPackage(enclosingElement);
            System.out.println(TAG + ",moduleName:" + annotation.moduleName() + ",SimpleName:" + e.getSimpleName());
            map.put(annotation.moduleName(), aPackage + "." + e.getSimpleName().toString());
        }
        generatedClass("RegisterRouter", map);
        return true;
    }

    /**
     * 针对注解中的内容生成类
     * 注册key，类名
     */
    private void generatedClass(String name, Map<String, String> moduleMap) {
        System.out.println(TAG + ":createJAVAFile:" + name);
        StringBuilder builder = new StringBuilder();
        addLine(builder, "package com.xt.client;");

        addLine(builder, "import java.util.HashMap;");
        addLine(builder, "import java.util.Map;");
        addLine(builder, "import java.util.Map;");
        addLine(builder, "import com.xt.client.function.route.RouterBase;");
        addLine(builder, " import com.xt.client.function.route.RegisterRouterInter;");

        for (String value : moduleMap.values()) {
            addLine(builder, "import " + value + ";");
        }


        addLine(builder, "public class " + name + " extends RegisterRouterInter {");
        addLine(builder, "  @Override");
        addLine(builder, "  public void init() {");

        for (String key : moduleMap.keySet()) {
            addLine(builder, "  map.put(\"" + key + "\", new " + moduleMap.get(key) + "());");
        }

        addLine(builder, "  }");
        builder.append("}");

        try {
            writer.append(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(TAG + ":createJAVAFile.end:" + name);
    }

    private void addLine(StringBuilder builder, String line) {
        builder.append(line);
        builder.append("\n");
    }

}