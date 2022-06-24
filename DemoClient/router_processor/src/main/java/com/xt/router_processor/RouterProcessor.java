package com.xt.router_processor;

import com.google.auto.service.AutoService;
import com.xt.router_api.Route;

import java.io.IOException;
import java.io.OutputStream;
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
        System.out.println(TAG + ":process");
        if (annotations.isEmpty()) {
            return false;
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "TAG");
        //所有带Route注解的类
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Route.class);
        for (Element e : elementsAnnotatedWith) {
            Route annotation = e.getAnnotation(Route.class);
            System.out.println(TAG + ",moduleName:" + annotation.moduleName());
            generatedClass(annotation.moduleName());
        }
        return true;
    }

    //针对注解中的内容生成类
    private void generatedClass(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.xt.client;");
        builder.append("public class " + name + " {");
        builder.append("}");

        System.out.println(TAG + ":createJAVAFile:" + name);

        try {
            JavaFileObject sourceFile = filer.createSourceFile(name);
            OutputStream outputStream = null;
            outputStream = sourceFile.openOutputStream();

            outputStream.write(builder.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}