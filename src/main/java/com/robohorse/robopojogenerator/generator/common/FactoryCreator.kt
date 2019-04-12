package com.robohorse.robopojogenerator.generator.common

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.RoboPOJOGenerator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
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
        classItems.forEach { classItem: ClassItem ->
            methods += "fun make${classItem.className}Model(): ${classItem.className}Model {\n"
            methods = generateFields(classItem, methods)
            methods += "}\n\n"
        }
        return methods
    }

    private fun generateFields(classItem: ClassItem, methods: String): String {
        var result = methods
        var counter = 0
        classItem.classFields.forEach { classField: Map.Entry<String, ClassField> ->
            result += ""
            val classType = generateValue(classField.value.kotlinItem)
            when (counter) {
                classItem.classFields.size - 1 -> {
                    // end the return command
                    result += "\t\t${classField.key} = $classType\n" +
                            "\t)\n"
                }
                0 -> {
                    // first
                    result += "\treturn ${classItem.className}(\n" +
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

    private fun generateValue(classType: String): String {
        return when (classType) {
            ClassEnum.STRING.kotlin -> {
                "randomUuid()"
            }
            ClassEnum.INTEGER.kotlin -> {
                "randomInt()"
            }
            ClassEnum.BOOLEAN.kotlin -> {
                "randomBoolean()"
            }
            ClassEnum.LONG.kotlin, ClassEnum.FLOAT.kotlin, ClassEnum.DOUBLE.kotlin -> {
                "randomLong()"
            }
            else -> ""
        }
    }
}
