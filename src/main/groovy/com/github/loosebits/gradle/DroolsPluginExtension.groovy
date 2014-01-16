package com.github.loosebits.gradle

import org.gradle.api.file.FileCollection

class DroolsPluginExtension {
    private CompileDroolsTask compileDroolsTask
    File srcDir
    FileCollection classpath
    File outputFile
    
    public DroolsPluginExtension(CompileDroolsTask t) {
        compileDroolsTask = t
    }
    


    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
        compileDroolsTask.inputs.dir(srcDir.absolutePath)
        
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
        compileDroolsTask.outputs.file(outputFile)
    }
}
