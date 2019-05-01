package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.*
import javax.inject.Inject

open class DataTestCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var mapperTestGeneratorDelegate: MapperTestGeneratorDelegate

    @Inject
    lateinit var factoryGenerationDelegate: FactoryGeneratorDelegate

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generateMapperTest(projectModel, coreGeneratorModel)
        generateFactory(projectModel, coreGeneratorModel)
        generateDataRepositoryTest(projectModel, coreGeneratorModel)
        generateCacheDataStoreTest(projectModel, coreGeneratorModel)
        generateDataStoreFactoryTest(projectModel, coreGeneratorModel)
        generateRemoteDataStoreTest(projectModel, coreGeneratorModel)
    }

    private fun generateRemoteDataStoreTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataTestPath + CoreGeneratorActionController.SOURCE_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "RemoteDataStoreTest"
        val fileNameSuffix = "RemoteDataStoreTest"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateDataStoreFactoryTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataTestPath + CoreGeneratorActionController.SOURCE_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "DataStoreFactoryTest"
        val fileNameSuffix = "DataStoreFactoryTest"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateCacheDataStoreTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataTestPath + CoreGeneratorActionController.SOURCE_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "CacheDataStoreTest"
        val fileNameSuffix = "CacheDataStoreTest"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateDataRepositoryTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataTestPath ?: throw PathException()
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "DataRepositoryTest"
        val fileNameSuffix = "DataRepositoryTest"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateFactory(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataTestPath + CoreGeneratorActionController.FACTORY_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val factoryGeneratorModel = FactoryGeneratorModel(
                fileNameSuffix = "Factory",
                templateName = "DataFactory",
                remote = false,
                cache = false,
                data = true,
                domain = true
        )

        factoryGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, factoryGeneratorModel)
    }

    private fun generateMapperTest(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataTestPath + CoreGeneratorActionController.MAPPER_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val mapperTestGeneratorModel = MapperTestGeneratorModel(
                from = "domain",
                to = "entity",
                fileNameSuffix = "MapperTest",
                templateName = "DataMapperTest",
                classNameSuffix = "Mapper",
                isNullable = false
        )

        mapperTestGeneratorDelegate.runGenerationTask(generationModel, regenProjectModel, mapperTestGeneratorModel)
    }

}