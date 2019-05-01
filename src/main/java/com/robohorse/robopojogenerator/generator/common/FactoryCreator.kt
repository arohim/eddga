package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ImportsTemplate
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.FactoryGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class FactoryCreator @Inject constructor() {

    @Inject
    lateinit var roboPOJOGenerator: RoboPOJOGenerator

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Inject
    lateinit var generateHelper: ClassGenerateHelper

    @Throws(RoboPluginException::class)
    fun generateFiles(generationModel: GenerationModel,
                      projectModel: ProjectModel,
                      factoryGeneratorModel: FactoryGeneratorModel) {
        val classItemSet = roboPOJOGenerator.generate(generationModel).toMutableList()
        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(projectModel.project)
        val templateProperties = fileTemplateManager.defaultProperties
        classItemSet.forEach { classItem: ClassItem ->
            val className = classItem.className
            templateProperties["CLASS_NAME"] = className
            templateProperties["METHODS"] = generateMethods(classItem, factoryGeneratorModel)
            val fileName = className + factoryGeneratorModel.fileNameSuffix
            fileTemplateWriterDelegate.writeTemplate(
                    projectModel.directory,
                    fileName,
                    factoryGeneratorModel.templateName,
                    templateProperties
            )
        }
    }


    fun generateMethods(classItem: ClassItem, factoryGeneratorModel: FactoryGeneratorModel): String {
        var methods = ""

        if (factoryGeneratorModel.domain)
            methods += generateDomainMethods(classItem, "", "")

        if (factoryGeneratorModel.remote)
            methods += generateDomainMethods(classItem, "", "Model")

        if (factoryGeneratorModel.data)
            methods += generateDomainMethods(classItem, "", "Entity")

        if (factoryGeneratorModel.cache)
            methods += generateDomainMethods(classItem, "Cached", "")

        return methods
    }

    private fun generateDomainMethods(classItem: ClassItem, prefix: String, suffix: String): String {
        var result = ""
        result += if (isContainListClass(classItem)) {
            if (isListType(classItem)) {
                generateMultipleAndSingleMethods(classItem, prefix, suffix)
            } else {
                generateListMethod(classItem, true, prefix, suffix)
            }
        } else {
            generateNonListMethod(classItem, prefix, suffix)
        }
        return result
    }

    private fun generateMultipleAndSingleMethods(classItem: ClassItem, prefix: String, suffix: String): String {
        var result = ""

        val className = getClassName(prefix, classItem.className, suffix)

        result += "fun make$className(repeat: Int): $className {\n"
        result += generateFields(classItem, prefix, suffix)
        result += "}\n\n"

        val classFields = getListType(classItem)

        classFields.forEach { (key, _) ->
            val methodName = generateHelper.formatClassName(key)
            val classFieldName = getClassName(prefix, methodName, suffix)
            result += "private fun make${classFieldName}s(repeat: Int): List<$classFieldName> {\n"
            result += "\tval contents = mutableListOf<$classFieldName>()\n" +
                    "\tkotlin.repeat(repeat) {\n" +
                    "\t\tcontents.add(make$classFieldName())\n" +
                    "\t}\n" +
                    "\treturn contents\n"
            result += "}\n"
        }

        return result
    }

    private fun isListType(classItems: ClassItem): Boolean {
        return getListType(classItems)
                .isNotEmpty()
    }

    private fun getListType(classItems: ClassItem): Map<String, ClassField> {
        return classItems
                .classFields
                .filter { it.value.isListField }
    }

    private fun isContainListClass(classItems: ClassItem) =
            classItems.classImports.contains(ImportsTemplate.LIST)

    private fun generateListMethod(classItem: ClassItem, isFirstClass: Boolean, prefix: String, suffix: String): String {
        var result = ""
        val param = if (isFirstClass) "repeat: Int" else ""
        val className = getClassName(prefix, classItem.className, suffix)

        result += "fun make${className}($param): ${className} {\n"
        result += generateFields(classItem, prefix, suffix)
        result += "}\n\n"
        return result
    }

    private fun generateNonListMethod(classItem: ClassItem, prefix: String, suffix: String): String {
        var result = ""
        val className = prefix + classItem.className + suffix

        result += "fun make$className(): $className {\n"
        result += generateFields(classItem, prefix, suffix)
        result += "}\n\n"
        return result
    }

    private fun generateFields(classItem: ClassItem, prefix: String, suffix: String): String {
        var result = ""
        var counter = 0
        classItem.classFields.forEach { classField: Map.Entry<String, ClassField> ->
            val classType: String? = generateValue(classField, prefix, suffix)
            val className = getClassName(prefix, classItem.className, suffix)
            val fileName = generateHelper.formatClassField(classField.key)
            when {
                counter == classItem.classFields.size - 1 && classItem.classFields.size > 1 -> {
                    // end the return command
                    result += "\t\t$fileName = $classType\n" +
                            "\t)\n"
                }
                counter == 0 && classItem.classFields.size == 1 -> {
                    result += "\treturn ${className}(\n" +
                            "\t\t$fileName = $classType\n" +
                            "\t)\n"
                }
                counter == 0 -> {
                    // first
                    result += "\treturn ${className}(\n" +
                            "\t\t$fileName = $classType,\n"
                }
                else -> {
                    // middle
                    result += "\t\t$fileName = $classType,\n"
                }
            }
            counter++
        }
        return result
    }

    private fun getClassName(prefix: String, className: String, suffix: String) = prefix + className + suffix

    private fun generateValue(classField: Map.Entry<String, ClassField>, prefix: String, suffix: String): String {
        return when (classField.value.classEnum) {
            ClassEnum.STRING -> {
                "randomUuid()"
            }
            ClassEnum.INTEGER -> {
                "randomInt()"
            }
            ClassEnum.BOOLEAN -> {
                "randomBoolean()"
            }
            ClassEnum.LONG -> {
                "randomLong()"
            }
            ClassEnum.FLOAT -> {
                "randomFloat()"
            }
            ClassEnum.DOUBLE -> {
                "randomDouble()"
            }
            else -> {
                val rawClassName = generateHelper.formatClassName(classField.key)
                val className = getClassName(prefix, rawClassName, suffix)
                if (classField.value.isListField) {
                    "make${className}s(repeat)"
                } else {
                    "make$className()"
                }
            }
        }
    }
}
