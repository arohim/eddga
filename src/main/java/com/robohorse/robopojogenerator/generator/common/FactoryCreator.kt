package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ImportsTemplate
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperTestGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class FactoryCreator @Inject constructor() {

    @Inject
    lateinit var roboPOJOGenerator: RoboPOJOGenerator

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Throws(RoboPluginException::class)
    fun generateFiles(generationModel: GenerationModel,
                      projectModel: ProjectModel,
                      mapperTestGeneratorModel: MapperTestGeneratorModel) {
        val classItemSet = roboPOJOGenerator.generate(generationModel).toMutableList()
        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(projectModel.project)
        val templateProperties = fileTemplateManager.defaultProperties
        templateProperties["CLASS_NAME"] = generationModel.rootClassName
        templateProperties["METHODS"] = generateMethods(classItemSet)
        val fileName = generationModel.rootClassName + mapperTestGeneratorModel.fileNameSuffix
        fileTemplateWriterDelegate.writeTemplate(
                projectModel.directory,
                fileName,
                mapperTestGeneratorModel.templateName,
                templateProperties
        )
    }

    fun generateMethods(classItems: MutableList<ClassItem>): String {
        var methods = ""

        if (isContainListClass(classItems)) {
            val classListType = getClassListType(classItems)
            classItems.forEach { classItem: ClassItem ->
                methods += if (classListType.contains(classItem.className)) {
                    generateMultipleAndSingleMethods(classItem)
                } else {
                    generateListMethod(classItem)
                }
            }
        } else {
            classItems.forEach { classItem: ClassItem ->
                methods += generateNonListMethod(classItem)
            }
        }

        return methods
    }

    private fun generateMultipleAndSingleMethods(classItem: ClassItem): String {
        var result = ""

        result += "private fun make${classItem.className}Models(repeat: Int): List<${classItem.className}Model> {\n"
        result += "\tval contents = mutableListOf<${classItem.className}Model>()\n" +
                "\tkotlin.repeat(repeat) {\n" +
                "\t\tcontents.add(make${classItem.className}Model())\n" +
                "\t}\n" +
                "\treturn contents\n"
        result += "}\n\n"

        result += "private fun make${classItem.className}Model(): ${classItem.className}Model {\n"
        result += generateFields(classItem)
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

    private fun generateListMethod(classItem: ClassItem): String {
        var result = ""
        result += "fun make${classItem.className}Model(repeat: Int): ${classItem.className}Model {\n"
        result += generateFields(classItem)
        result += "}\n\n"
        return result
    }

    private fun generateNonListMethod(classItem: ClassItem): String {
        var result = ""
        result += "fun make${classItem.className}Model(): ${classItem.className}Model {\n"
        result += generateFields(classItem)
        result += "}\n\n"
        return result
    }

    private fun generateFields(classItem: ClassItem): String {
        var result = ""
        var counter = 0
        classItem.classFields.forEach { classField: Map.Entry<String, ClassField> ->
            val classType: String? = generateValue(classField.value)
            when {
                counter == classItem.classFields.size - 1 && classItem.classFields.size > 1 -> {
                    // end the return command
                    result += "\t\t${classField.key} = $classType\n" +
                            "\t)\n"
                }
                counter == 0 && classItem.classFields.size == 1 -> {
                    result += "\treturn ${classItem.className}Model(\n" +
                            "\t\t${classField.key} = $classType\n" +
                            "\t)\n"
                }
                counter == 0 -> {
                    // first
                    result += "\treturn ${classItem.className}Model(\n" +
                            "\t\t${classField.key} = $classType,\n"
                }
                else -> {
                    // middle
                    result += "\t\t${classField.key} = $classType,\n"
                }
            }
            counter++
        }
        return result
    }

    private fun generateValue(classField: ClassField): String {
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
            ClassEnum.LONG, ClassEnum.FLOAT, ClassEnum.DOUBLE -> {
                "randomLong()"
            }
            else -> {
                "make${classField.className}Models(repeat)"
            }
        }
    }
}
