package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
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
            templateProperties["MAP_TO_ENTITIES"] = generateMappingFieldString(classItem.classFields,
                    mapperGeneratorModel.fileNameSuffix, mapperGeneratorModel.mapToMethodName, false)
            templateProperties["MAP_FROM_ENTITIES"] = generateMappingFieldString(classItem.classFields,
                    mapperGeneratorModel.fileNameSuffix, mapperGeneratorModel.mapFromMethodName, false)
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
        val injectors = mutableListOf<String>()
        val classFieldClasses = classFields.filter { isClassField(it.value) || it.value.isListField }
        classFieldClasses.forEach {
            val fieldName = generateHelper.formatClassField("${it.key}$suffix")
            val className = generateHelper.formatClassName("${it.key}$suffix")
            injectors.add("private val $fieldName: $className")
        }
        return injectors.joinToString(",\n")
    }

    fun generateMappingFieldString(classFields: MutableMap<String, ClassField>, suffix: String, mapperMethod: String, isNullable: Boolean): String {
        val nullSafety = if (isNullable) "?" else ""

        val asserts = mutableListOf<String>()
        classFields.forEach {
            val fieldName = generateHelper.formatClassField(it.key)
            val defaultValue = if (isNullable) getDefaultValue(it.value) else ""

            val assert = when {
                isClassField(it.value) -> {
                    val mapperName = generateHelper.formatClassField("${it.key}$suffix")
                    "$fieldName = $mapperName.$mapperMethod(type$nullSafety.$fieldName)$defaultValue"
                }
                it.value.isListField -> {
                    val mapperName = generateHelper.formatClassField("${it.key}$suffix")
                    "$fieldName = type.$fieldName$nullSafety.map { $mapperName.$mapperMethod(it) }$defaultValue"
                }
                else -> {
                    "$fieldName = type$nullSafety.$fieldName$defaultValue"
                }
            }
            asserts.add(assert)
        }
        return asserts.joinToString(",\n")
    }

    private fun getDefaultValue(classField: ClassField): String {
        return when (classField.classEnum) {
            ClassEnum.STRING -> {
                " ?: defaultString"
            }
            ClassEnum.INTEGER -> {
                " ?: defaultInt"
            }
            ClassEnum.BOOLEAN -> {
                " ?: defaultBoolean"
            }
            ClassEnum.LONG -> {
                " ?: defaultLong"
            }
            ClassEnum.FLOAT -> {
                " ?: defaultFloat"
            }
            ClassEnum.DOUBLE -> {
                " ?: defaultDouble"
            }
            else -> {
                if (classField.isListField) {
                    return " ?: listOf()"
                }
                ""
            }
        }
    }

    private fun isClassField(value: ClassField): Boolean {
        return value.className != null
    }
}
