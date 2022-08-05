package com.xt.router_processor;

import com.google.auto.service.AutoService;
import com.xt.router_api.ApplicationInit;
import com.xt.router_api.Route;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xt.router_api.ApplicationInit")
@SupportedOptions("moduleName")
public class ApplicationInitProcessor extends AbstractProcessor {
    Filer filer;
    String TAG = "ApplicationInitProcessor";

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
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(ApplicationInit.class);
        for (Element e : elementsAnnotatedWith) {
            String name = e.getSimpleName().toString();
            Element enclosingElement = e.getEnclosingElement();
            String packageName;
            if (enclosingElement instanceof PackageElement) {
                packageName = ((PackageElement) enclosingElement).getQualifiedName().toString();
                System.out.println(TAG + ":PackageElement,enclosingQualifiedname:" + packageName);
            } else {
                packageName = ((TypeElement) enclosingElement).getQualifiedName().toString();
                System.out.println(TAG + ":ELSE,enclosingQualifiedname:" + packageName);
            }
            addToList(packageName + "." + name);
        }
        return true;
    }

    //针对注解中的内容生成类
    private void addToList(String name) {


        System.out.println(TAG + ":addToList:" + name);


    }
}