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
        templateProperties["CLASS_NAME"] = generationModel.rootClassName
        templateProperties["METHODS"] = generateMethods(classItemSet, factoryGeneratorModel)
        val fileName = generationModel.rootClassName + factoryGeneratorModel.fileNameSuffix
        fileTemplateWriterDelegate.writeTemplate(
                projectModel.directory,
                fileName,
                factoryGeneratorModel.templateName,
                templateProperties
        )
    }

    fun generateMethods(classItems: MutableList<ClassItem>, factoryGeneratorModel: FactoryGeneratorModel): String {
        var methods = ""

        if (factoryGeneratorModel.domain)
            methods += generateDomainMethods(classItems, "", "")

        if (factoryGeneratorModel.remote)
            methods += generateDomainMethods(classItems, "", "Model")

        if (factoryGeneratorModel.data)
            methods += generateDomainMethods(classItems, "", "Entity")

        if (factoryGeneratorModel.cache)
            methods += generateDomainMethods(classItems, "Cached", "")

        return methods
    }

    private fun generateDomainMethods(classItems: MutableList<ClassItem>, prefix: String, suffix: String): String {
        var result = ""
        if (isContainListClass(classItems)) {
            val classListType = getClassListType(classItems)
            var isFirstClass = true
            classItems.forEach { classItem: ClassItem ->
                if (classListType.contains(classItem.className)) {
                    result += generateMultipleAndSingleMethods(classItem, prefix, suffix)
                } else {
                    result += generateListMethod(classItem, isFirstClass, prefix, suffix)
                    isFirstClass = false
                }
            }
        } else {
            classItems.forEach { classItem: ClassItem ->
                result += generateNonListMethod(classItem, prefix, suffix)
            }
        }
        return result
    }

    private fun generateMultipleAndSingleMethods(classItem: ClassItem, prefix: String, suffix: String): String {
        var result = ""

        val className = getClassName(prefix, classItem.className, suffix)
        result += "private fun make${className}s(repeat: Int): List<${className}> {\n"
        result += "\tval contents = mutableListOf<${className}>()\n" +
                "\tkotlin.repeat(repeat) {\n" +
                "\t\tcontents.add(make${className}())\n" +
                "\t}\n" +
                "\treturn contents\n"
        result += "}\n\n"

        result += "private fun make${className}(): ${className} {\n"
        result += generateFields(classItem, prefix, suffix)
        result += "}\n\n"
        return result
    }

    private fun getClassListType(classItems: MutableList<ClassItem>): List<String> {
        return classItems
                .flatMap { it.classFields.values }
                .filter { it.isListField }
                .map { it.className }
    }

    private fun isContainListClass(classItems: MutableList<ClassItem>) =
            classItems.flatMap { it.classImports }.contains(ImportsTemplate.LIST)

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

        result += "fun make${className}(): ${className} {\n"
        result += generateFields(classItem, prefix, suffix)
        result += "}\n\n"
        return result
    }

    private fun generateFields(classItem: ClassItem, prefix: String, suffix: String): String {
        var result = ""
        var counter = 0
        classItem.classFields.forEach { classField: Map.Entry<String, ClassField> ->
            val classType: String? = generateValue(classField.value, prefix, suffix)
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

    private fun generateValue(classField: ClassField, prefix: String, suffix: String): String {
        return when (classField.classEnum) {
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
                val className = getClassName(prefix, classField.className, suffix)
                if (classField.isListField) {
                    "make${className}s(repeat)"
                } else {
                    "make$className()"
                }
            }
        }
    }
}
