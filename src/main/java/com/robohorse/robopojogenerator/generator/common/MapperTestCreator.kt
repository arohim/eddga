package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.MapperTestGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class MapperTestCreator @Inject constructor() {

    @Inject
    lateinit var roboPOJOGenerator: RoboPOJOGenerator

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Inject
    lateinit var generateHelper: ClassGenerateHelper

    @Throws(RoboPluginException::class)
    fun generateFiles(generationModel: GenerationModel,
                      projectModel: ProjectModel,
                      mapperTestGeneratorModel: MapperTestGeneratorModel) {
        val classItemSet = roboPOJOGenerator.generate(generationModel).toMutableList()
        for (classItem in classItemSet) {
            val fileTemplateManager = fileTemplateWriterDelegate.getInstance(projectModel.project)
            val templateProperties = fileTemplateManager.defaultProperties
            templateProperties["CLASS_NAME"] = classItem.className
            templateProperties["ASSERTIONS"] = generateAssertions(classItem.classFields, mapperTestGeneratorModel.from, mapperTestGeneratorModel.to)
            val fileName = classItem.className + mapperTestGeneratorModel.fileNameSuffix
            fileTemplateWriterDelegate.writeTemplate(
                    projectModel.directory,
                    fileName,
                    mapperTestGeneratorModel.templateName,
                    templateProperties
            )
        }
    }

    fun generateAssertions(classFields: MutableMap<String, ClassField>, from: String, to: String): String {
        var asserts = ""
        var counter = 0
        classFields.forEach {
            val fileName = generateHelper.formatClassField(it.key)
            asserts += "assertEquals($from.$fileName, $to.$fileName)"
            if (counter < classFields.size - 1) {
                asserts += "\n"
            }
            counter++
        }
        return asserts
    }
}
