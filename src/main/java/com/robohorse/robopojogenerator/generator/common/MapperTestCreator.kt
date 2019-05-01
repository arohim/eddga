package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
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
            templateProperties["ASSERTIONS"] = generateAssertions(classItem.classFields, mapperTestGeneratorModel)
            templateProperties["PROPERTIES"] = generateProperties(classItem.classFields, mapperTestGeneratorModel.classNameSuffix)
            templateProperties["PROPERTY_PARAMETERS"] = generatePropertyParameters(classItem.classFields, mapperTestGeneratorModel.classNameSuffix)
            templateProperties["PROPERTIES_INITIALIZATION"] = generatePropertiesInitialization(classItem, mapperTestGeneratorModel.classNameSuffix)
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

    fun generateAssertions(classFields: MutableMap<String, ClassField>, mapperTestGeneratorModel: MapperTestGeneratorModel): String {
        val from = mapperTestGeneratorModel.from
        val to = mapperTestGeneratorModel.to
        val isNullable = mapperTestGeneratorModel.isNullable
        val nullSafety = if (isNullable) "?" else ""

        val asserts = mutableListOf<String>()
        classFields.forEach {
            val fileName = generateHelper.formatClassField(it.key)
            when {
                isClassField(it.value) -> {
                    asserts.add("assertNotNull($from.$fileName)")
                    asserts.add("assertNotNull($to.$fileName)")
                }
                it.value.isListField -> {
                    asserts.add("assertEquals($from$nullSafety.$fileName$nullSafety.size, $to.$fileName.size)")
                }
                else -> {
                    asserts.add("assertEquals($from$nullSafety.$fileName, $to.$fileName)")
                }
            }
        }
        return asserts.joinToString("\n")
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
