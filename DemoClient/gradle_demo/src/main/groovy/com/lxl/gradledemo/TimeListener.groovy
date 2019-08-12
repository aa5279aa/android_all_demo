package com.lxl.gradledemo;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskState


public class TimeListener implements TaskExecutionListener, BuildListener {

    private long lastTime
    private times = []

    @Override
    public void buildStarted(Gradle gradle) {

    }

    @Override
    public void settingsEvaluated(Settings settings) {

    }

    @Override
    public void projectsLoaded(Gradle gradle) {

    }

    @Override
    public void projectsEvaluated(Gradle gradle) {

    }

    @Override
    public void buildFinished(BuildResult buildResult) {
        println 'Task spend time:'
        for (time in times) {
            if (time[0] >= 10) {
                printf '%7sms %s\n', time
            }
        }
    }

    @Override
    public void beforeExecute(Task task) {
        lastTime = System.currentTimeMillis()
    }

    @Override
    public void afterExecute(Task task, TaskState taskState) {
        def ms = System.currentTimeMillis() - lastTime
        lastTime = System.currentTimeMillis()
        times.add([ms, task.path])
        task.project.logger.warn "${task.path} spend ${ms}ms"


    }
}
