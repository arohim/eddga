package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class MapperCreator @Inject constructor() {

    @Inject
    lateinit var roboPOJOGenerator: RoboPOJOGenerator

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Inject
    lateinit var generateHelper: ClassGenerateHelper

    @Throws(RoboPluginException::class)
    fun generateFiles(generationModel: GenerationModel,
                      projectModel: ProjectModel,
                      mapperGeneratorModel: MapperGeneratorModel) {
        val classItemSet = roboPOJOGenerator.generate(generationModel).toMutableList()
        for (classItem in classItemSet) {
            val fileTemplateManager = fileTemplateWriterDelegate.getInstance(projectModel.project)
            val templateProperties = fileTemplateManager.defaultProperties
            templateProperties["CLASS_NAME"] = classItem.className
            templateProperties["MAP_TO_ENTITIES"] = generateMappingFieldString(classItem.classFields, mapperGeneratorModel.fileNameSuffix, mapperGeneratorModel.mapToMethodName)
            templateProperties["MAP_FROM_ENTITIES"] = generateMappingFieldString(classItem.classFields, mapperGeneratorModel.fileNameSuffix, mapperGeneratorModel.mapFromMethodName)
            templateProperties["INJECTORS"] = generateInjectors(classItem.classFields, mapperGeneratorModel.fileNameSuffix)
            val fileName = classItem.className + mapperGeneratorModel.fileNameSuffix
            fileTemplateWriterDelegate.writeTemplate(
                    projectModel.directory,
                    fileName,
                    mapperGeneratorModel.templateName,
                    templateProperties
            )
        }
    }

    fun generateInjectors(classFields: Map<String, ClassField>, suffix: String): String {
        var injectors = ""
        val classFieldClasses = classFields.filter { isClassField(it.value) }
        var counter = classFieldClasses.size
        classFieldClasses.forEach {
            val fieldName = generateHelper.formatClassField("${it.key}$suffix")
            val className = generateHelper.formatClassName("${it.key}$suffix")
            injectors += "private val $fieldName: $className"

            if (counter > 1) {
                injectors += ",\n"
            }
            counter--
        }
        return injectors
    }

    fun generateMappingFieldString(classFields: MutableMap<String, ClassField>, suffix: String, mapperMethod: String): String {
        var asserts = ""
        var counter = 0
        classFields.forEach {
            val fieldName = generateHelper.formatClassField(it.key)

            asserts += if (isClassField(it.value)) {
                val mapperName = generateHelper.formatClassField("${it.key}$suffix")
                "$fieldName = $mapperName.$mapperMethod(type.$fieldName)"
            } else
                "$fieldName = type.$fieldName"
            if (counter < classFields.size - 1) {
                asserts += ",\n"
            }
            counter++
        }
        return asserts
    }

    private fun isClassField(value: ClassField): Boolean {
        return value.className != null
    }
}
