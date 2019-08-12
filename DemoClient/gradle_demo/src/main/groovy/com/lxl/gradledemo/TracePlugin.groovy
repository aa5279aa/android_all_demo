package com.lxl.gradledemo

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.lxl.gradledemo.TraceTransform
class TracePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new TraceTransform())
    }

}