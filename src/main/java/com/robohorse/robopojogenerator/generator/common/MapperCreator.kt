package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class MapperCreator @Inject constructor() {

    @Inject
    lateinit var roboPOJOGenerator: RoboPOJOGenerator

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Throws(RoboPluginException::class)
    fun generateFiles(generationModel: GenerationModel,
                      projectModel: ProjectModel,
                      mapperGeneratorModel: MapperGeneratorModel) {
        val classItemSet = roboPOJOGenerator.generate(generationModel).toMutableList()
        for (classItem in classItemSet) {
            val fileTemplateManager = fileTemplateWriterDelegate.getInstance(projectModel.project)
            val templateProperties = fileTemplateManager.defaultProperties
            templateProperties["CLASS_NAME"] = classItem.className
            templateProperties["ENTITIES"] = generateMappingFieldString(classItem.classFields)
            val fileName = classItem.className + mapperGeneratorModel.fileNameSuffix
            fileTemplateWriterDelegate.writeTemplate(
                    projectModel.directory,
                    fileName,
                    mapperGeneratorModel.templateName,
                    templateProperties
            )
        }
    }

    fun generateMappingFieldString(classFields: MutableMap<String, ClassField>): String {
        var asserts = ""
        var counter = 0
        classFields.forEach {
            asserts += "${it.key} = type.${it.key}"
            if (counter < classFields.size - 1) {
                asserts += ",\n"
            }
            counter++
        }
        return asserts
    }
}
