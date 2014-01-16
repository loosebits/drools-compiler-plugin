package com.github.loosebits.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class DroolsPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Task compileDroolsTask = project.tasks.create('compileDrools',CompileDroolsTask)
        def t = project.getTasksByName("compileGroovy",false) 
        if (!t) {
            t = project.getTasksByName("compileJava",false)
        }
        if (t) {
            compileDroolsTask.dependsOn(t)
        }
        t = project.getTasksByName("classes",false)
        t*.dependsOn(compileDroolsTask)
        project.extensions.create("drools", DroolsPluginExtension, compileDroolsTask) 
    }

}
