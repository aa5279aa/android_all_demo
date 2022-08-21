package com.xt.router_processor;

import com.google.auto.service.AutoService;
import com.xt.router_api.ApplicationInit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * 注解处理类，处理com.xt.router_api.ApplicationInit类型注解。
 * 凡是被@ApplicationInit注解声明的类，加入到services/com.xt.client.inter.ServiceProviderInterface中，启动自动实现初始化。
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xt.router_api.ApplicationInit")
@SupportedOptions("moduleName")
public class ApplicationInitProcessor extends AbstractProcessor {
    Filer filer;
    String TAG = "ApplicationInitProcessor";
    boolean isAdd = false;
    String servicesPath;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations.isEmpty()) {
            return false;
        }

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "TAG");
        //所有带ApplicationInit注解的类
        Set<? extends Element> set = roundEnvironment.getElementsAnnotatedWith(ApplicationInit.class);
        for (Element e : set) {
            String name = e.getSimpleName().toString();
            Element enclosingElement = e.getEnclosingElement();
            String packageName = ProcessorUtils.getPackage(enclosingElement);
            addToList(packageName + "." + name);
        }
        return true;
    }

    //针对注解中的内容生成类
    private void addToList(String name) {

        /**
         * 临时文件目录
         * DemoClient/app/build/intermediates/java_res/debug/out/META-INF/services/com.xt.client.inter.ServiceProviderInterface
         */

        try {
            if (servicesPath == null) {
                FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, "a", "a");
                String path = resource.toUri().getPath();
                servicesPath = path.substring(0, path.indexOf("app") + 3);
                servicesPath += "/build/intermediates/java_res/debug/out/META-INF/services";
            }
            File folder = new File(servicesPath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder.getAbsolutePath() + File.separator + "com.xt.client.inter.ServiceProviderInterface");

            StringBuilder builder = new StringBuilder();
            if (isAdd) {
                builder.append("\n");
            }
            //第一次的的时候覆盖，后面追加
            FileWriter fileWriter = new FileWriter(file, isAdd);
            isAdd = true;
            builder.append(name);
            System.out.println(TAG + ":append:" + builder);
            fileWriter.append(builder.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}