package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController.Companion.MODEL_PATH
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate
import com.robohorse.robopojogenerator.models.ClassNameTemplateModel
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class DomainCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generatePOJO(projectModel, coreGeneratorModel)
        generateUseCase(projectModel, coreGeneratorModel)
        generateRepositoryInterface(projectModel, coreGeneratorModel)
    }

    private fun generateRepositoryInterface(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.domainPath ?: throw PathException()
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "RepositoryInterface"
        val fileNameSuffix = "Repository"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateUseCase(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.domainPath ?: throw PathException()
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val classNameTemplateModel = ClassNameTemplateModel(
                dialogTitle = "Use Case",
                templateName = "UseCase",
                fileNameSuffix = ""
        )

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + classNameTemplateModel.fileNameSuffix,
                classNameTemplateModel.templateName, templateProperties)
    }

    private fun generatePOJO(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.domainPath + MODEL_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val domainGenerationModel = GenerationModel.Builder()
                .useKotlin(true)
                .setAnnotationItem(AnnotationEnum.NONE)
                .setSettersAvailable(false)
                .setGettersAvailable(false)
                .setToStringAvailable(false)
                .setRewriteClasses(true)
                .setPrefix("")
                .setSuffix("")
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .setFieldDTOFormat(ClassTemplate.NON_NULL_FIELD_KOTLIN_DTO)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .build()

        pOJOGenerationDelegate.runGenerationTask(domainGenerationModel, regenProjectModel)
    }

}