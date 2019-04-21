package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.*
import javax.inject.Inject

open class DomainTestCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Inject
    lateinit var factoryGenerationDelegate: FactoryGeneratorDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generateUseCaseTest(projectModel, coreGeneratorModel)
        generateFactory(projectModel, coreGeneratorModel)
    }

    private fun generateFactory(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.domainTestPath + CoreGeneratorActionController.FACTORY_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val factoryGeneratorModel = FactoryGeneratorModel(
                fileNameSuffix = "Factory",
                templateName = "DomainFactory",
                remote = false,
                cache = false,
                data = false,
                domain = true
        )

        factoryGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, factoryGeneratorModel)
    }

    private fun generateUseCaseTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.domainTestPath ?: throw PathException()
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "UseCaseTest"
        val fileNameSuffix = "UseCaseTest"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

}