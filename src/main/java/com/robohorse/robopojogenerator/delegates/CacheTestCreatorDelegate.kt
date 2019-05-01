package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.models.*
import javax.inject.Inject

open class CacheTestCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

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
        val path = coreGeneratorModel.cacheTestPath + CoreGeneratorActionController.FACTORY_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val factoryGeneratorModel = FactoryGeneratorModel(
                fileNameSuffix = "Factory",
                templateName = "CacheFactory",
                remote = false,
                cache = true,
                data = true,
                domain = false
        )

        factoryGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, factoryGeneratorModel)
    }

    private fun generateMapperTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.cacheTestPath + CoreGeneratorActionController.MAPPER_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val mapperTestGeneratorModel = MapperTestGeneratorModel(
                from = "cached",
                to = "entity",
                fileNameSuffix = "EntityMapperTest",
                templateName = "CacheMapperTest",
                classNameSuffix = "EntityMapper",
                isNullable = false
        )

        mapperTestGeneratorDelegate.runGenerationTask(generationModel, regenProjectModel, mapperTestGeneratorModel)
    }

}