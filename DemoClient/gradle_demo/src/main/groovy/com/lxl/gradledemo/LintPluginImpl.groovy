package com.lxl.gradledemo

import com.android.annotations.NonNull
import com.android.build.gradle.LintPlugin
import com.android.builder.model.Variant
import com.android.sdklib.BuildToolInfo
import com.android.tools.lint.LintCliFlags
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.LintRequest
import com.android.tools.lint.gradle.LintGradleClient
import com.android.tools.lint.gradle.LintGradleProject
import com.android.tools.lint.gradle.api.VariantInputs
import org.gradle.api.Plugin
import org.gradle.api.Project


public class LintPluginImpl extends LintGradleClient {


    LintPluginImpl(String version, IssueRegistry registry, LintCliFlags flags, Project gradleProject, File sdkHome, Variant variant, VariantInputs variantInputs, BuildToolInfo buildToolInfo, boolean isAndroid, String baselineVariantName) {
        super(version, registry, flags, gradleProject, sdkHome, variant, variantInputs, buildToolInfo, isAndroid, baselineVariantName)
    }

    @Override
    @NonNull
    protected LintRequest createLintRequest(@NonNull List<File> files) {
        def request = super.createLintRequest(files);
        //这里注入
        for(com.android.tools.lint.detector.api.Project project:request.getProjects()){
            project.addFile(new File())
        }

        return lintRequest;
    }

}