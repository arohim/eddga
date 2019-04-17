package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.*
import javax.inject.Inject

open class RemoteTestCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var mapperTestGeneratorDelegate: MapperTestGeneratorDelegate

    @Inject
    lateinit var factoryGenerationDelegate: FactoryGeneratorDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generateMapperTest(projectModel, coreGeneratorModel)
        generateFactory(projectModel, coreGeneratorModel)
    }

    private fun generateFactory(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.remoteTestPath ?: throw PathException()
        val regenProjectModel = regenProjectModel(projectModel, path)

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val factoryGeneratorModel = FactoryGeneratorModel(
                fileNameSuffix = "Factory",
                templateName = "RemoteFactory",
                remote = true,
                cache = false,
                data = true,
                domain = false
        )

        factoryGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, factoryGeneratorModel)
    }

    private fun generateMapperTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.remoteTestPath + CoreGeneratorActionController.MAPPER_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val fromMapperTestGeneratorModel = MapperTestGeneratorModel(
                from = "model",
                to = "entity",
                fileNameSuffix = "EntityMapperTest",
                templateName = "FromRemoteMapperTest"
        )

        mapperTestGeneratorDelegate.runGenerationTask(generationModel, regenProjectModel, fromMapperTestGeneratorModel)

        val toMapperTestGeneratorModel = MapperTestGeneratorModel(
                from = "entity",
                to = "model",
                fileNameSuffix = "ModelMapperTest",
                templateName = "ToRemoteMapperTest"
        )

        mapperTestGeneratorDelegate.runGenerationTask(generationModel, regenProjectModel, toMapperTestGeneratorModel)
    }
}