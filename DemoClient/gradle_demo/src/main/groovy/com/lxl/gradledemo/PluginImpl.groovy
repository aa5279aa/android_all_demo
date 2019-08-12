package com.lxl.gradledemo

import org.gradle.api.Plugin
import org.gradle.api.Project


public class PluginImpl implements Plugin<Project> {


    @Override
    void apply(Project project) {

        def isPlugin = project.plugins.hasPlugin("plugin.test")
        project.gradle.addListener(new TimeListener())

    }
}