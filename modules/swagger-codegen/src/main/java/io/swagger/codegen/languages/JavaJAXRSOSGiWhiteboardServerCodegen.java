package io.swagger.codegen.languages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.features.BeanValidationFeatures;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

public class JavaJAXRSOSGiWhiteboardServerCodegen extends JavaJAXRSSpecServerCodegen
{

    public JavaJAXRSOSGiWhiteboardServerCodegen()
    {
        super();

        artifactId = "swagger-jaxrs-osgi-whiteboard-server";
        outputFolder = "generated-code/JavaJaxRS-OSGi-Whiteboard";
        invokerPackage = "io.swagger.provider";
        sourceFolder = "src" + File.separator + "gen" + File.separator + "java";

        // put the implementation files in the same folder
        implFolder = sourceFolder;

        // API templates to support OSGi service provider
        apiTemplateFiles.put("apiImpl.mustache", ".java");

        // Use standard types
        typeMapping.put("DateTime", "java.util.Date");

        // Updated template directory
        embeddedTemplateDir = templateDir = JAXRS_TEMPLATE_DIRECTORY_NAME + File.separator + "osgi-whiteboard";
    }

    @Override
    public String getName()
    {
        return "jaxrs-osgi-whiteboard";
    }


    @Override
    public void processOpts()
    {
        super.processOpts();

        supportingFiles.clear(); // Don't need extra files provided by AbstractJAX-RS & Java Codegen

        // RestApplication into src/main/java
        writeOptional(outputFolder, new SupportingFile("RestApplication.mustache",
                (sourceFolder + '/' + invokerPackage).replace(".", "/"), "RestApplication.java"));
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);

        // Reinstate JsonProperty
        model.imports.add("JsonProperty");
    }

    @Override
    public String getHelp() {
        return "Generates a Java JAXRS Server according to JAX-RS Services whiteboard OSGi RFC-217.";
    }

    @Override
    public String apiFilename(String templateName, String tag) {
        String result;

        if ( templateName.endsWith("Impl.mustache") ) {
            result = implFileFolder() + "/" +  toApiName(tag)  + "Impl.java";
        } else {
            result= super.apiFilename(templateName, tag);
        }
        return result;
    }

    private String implFileFolder() {
        return outputFolder + "/" + implFolder + "/" + invokerPackage.replace('.', '/');
    }
}
