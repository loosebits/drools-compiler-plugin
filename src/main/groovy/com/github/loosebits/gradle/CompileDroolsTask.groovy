package com.github.loosebits.gradle


import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.io.ResourceFactory;
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException


class CompileDroolsTask extends DefaultTask {
    ClassLoader cl
    DroolsPluginExtension ext
    
    private void init() {
        ext = project.extensions.getByName("drools");
        cl = new GroovyClassLoader(Thread.currentThread().contextClassLoader);
        ext.classpath.files.each {
            logger.debug "Adding classpath element: $it.absolutePath" 
            cl.addClasspath(it.absolutePath); 
        }
    }
    
    
    @TaskAction
    public void compile() {
        init()
        PackageBuilderConfiguration conf = new PackageBuilderConfiguration(cl)
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder(conf)
        ext.srcDir.eachFileRecurse { File f ->
            ResourceType type;
            if ((type = ResourceType.determineResourceType(f.name))) {
                builder.add(ResourceFactory.newFileResource(f), type)
                logger.info "Adding $f.name"
            }
        }
        if (builder.hasErrors()) {
            throw new TaskExecutionException(builder.errors)
        }
        ext.outputFile.parentFile.mkdirs()
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(ext.outputFile.absolutePath))
        oo.writeObject(builder.knowledgePackages);
        oo.close()
        
    }
    
}
