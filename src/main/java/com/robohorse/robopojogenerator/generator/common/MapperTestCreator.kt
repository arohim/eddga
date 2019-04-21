package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.GenerationModel
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
            templateProperties["PROPERTIES"] = generateProperties(classItem.classFields, mapperTestGeneratorModel.fileNameSuffix)
            templateProperties["PROPERTY_PARAMETERS"] = generatePropertyParameters(classItem.classFields, mapperTestGeneratorModel.fileNameSuffix)
            templateProperties["PROPERTIES_INITIALIZATION"] = generatePropertiesInitialization(classItem, mapperTestGeneratorModel.fileNameSuffix)
            val fileName = classItem.className + mapperTestGeneratorModel.fileNameSuffix
            fileTemplateWriterDelegate.writeTemplate(
                    projectModel.directory,
                    fileName,
                    mapperTestGeneratorModel.templateName,
                    templateProperties
            )
        }
    }

    fun generateProperties(classFields: Map<String, ClassField>, suffix: String): String {
        var properties = ""
        val classItems = classFields.filter { isClassField(it.value) }
        classItems.forEach { classItem ->
            val fieldName = generateHelper.formatClassField(classItem.key) + suffix
            val className = classItem.value.className + suffix
            properties += "private lateinit var $fieldName: $className\n\n"
        }
        return properties
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

    private fun isClassField(value: ClassField): Boolean {
        return value.className != null
    }

    fun generatePropertyParameters(classFields: MutableMap<String, ClassField>, suffix: String): String {
        val parameters = mutableListOf<String>()
        val classItems = classFields.filter { isClassField(it.value) }
        classItems.forEach { classItem ->
            val fieldName = generateHelper.formatClassField(classItem.key) + suffix
            parameters.add(fieldName)
        }
        return parameters.joinToString(", ")
    }

    fun generatePropertiesInitialization(classItem: ClassItem, suffix: String): String {
        val initialization = mutableListOf<String>()
        val fields = classItem.classFields.filter { isClassField(it.value) }
        fields.forEach { field ->
            val fieldName = generateHelper.formatClassField(field.key) + suffix
            val className = field.value.className + suffix
            initialization.add("$fieldName = $className()")
        }
        return initialization.joinToString("\n")
    }
}
