package com.xt.router_processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

public class ProcessorUtils {
    public static String getPackage(Element enclosingElement) {
        String packageName;
        if (enclosingElement instanceof PackageElement) {
            packageName = ((PackageElement) enclosingElement).getQualifiedName().toString();
        } else {
            packageName = ((TypeElement) enclosingElement).getQualifiedName().toString();
        }
        return packageName;
    }

}
