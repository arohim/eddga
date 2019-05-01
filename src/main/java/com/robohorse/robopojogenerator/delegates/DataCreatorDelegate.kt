package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController.Companion.MAPPER_PATH
import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController.Companion.MODEL_PATH
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class DataCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var mapperGenerationDelegate: MapperGeneratorDelegate

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generatePOJO(projectModel, coreGeneratorModel)
        generateMapper(projectModel, coreGeneratorModel)
        generateDataRepository(projectModel, coreGeneratorModel)
        generateCacheDataStore(projectModel, coreGeneratorModel)
        generateDataStoreFactory(projectModel, coreGeneratorModel)
        generateRemoteDataStore(projectModel, coreGeneratorModel)
        generateDataStoreInterface(projectModel, coreGeneratorModel)
        generateRemoteInterface(projectModel, coreGeneratorModel)
        generateCacheInterface(projectModel, coreGeneratorModel)
    }

    private fun generateDataStoreInterface(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + CoreGeneratorActionController.REPOSITORY_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "DataStoreInterface"
        val fileNameSuffix = "DataStore"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateRemoteInterface(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + CoreGeneratorActionController.REPOSITORY_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "RemoteInterface"
        val fileNameSuffix = "Remote"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateCacheInterface(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + CoreGeneratorActionController.REPOSITORY_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "CacheInterface"
        val fileNameSuffix = "Cache"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateRemoteDataStore(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + CoreGeneratorActionController.SOURCE_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "RemoteDataStore"
        val fileNameSuffix = "RemoteDataStore"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateDataStoreFactory(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + CoreGeneratorActionController.SOURCE_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "DataStoreFactory"
        val fileNameSuffix = "DataStoreFactory"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateCacheDataStore(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + CoreGeneratorActionController.SOURCE_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "CacheDataStore"
        val fileNameSuffix = "CacheDataStore"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateDataRepository(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath ?: throw PathException()
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "DataRepository"
        val fileNameSuffix = "DataRepository"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateMapper(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + MAPPER_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()
        val mapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "Mapper",
                templateName = "DataMapper",
                mapToMethodName = "mapToEntity",
                mapFromMethodName = "mapFromEntity",
                isNullable = false
        )

        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, mapperGeneratorModel)
    }

    private fun generatePOJO(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + MODEL_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val generationModel = GenerationModel.Builder()
                .useKotlin(true)
                .setAnnotationItem(AnnotationEnum.NONE)
                .setSettersAvailable(false)
                .setGettersAvailable(false)
                .setToStringAvailable(false)
                .setRewriteClasses(true)
                .setPrefix("")
                .setSuffix("Entity")
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .setFieldDTOFormat(ClassTemplate.NON_NULL_FIELD_KOTLIN_DTO)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .setDialogTitle("Data Layer POJO Generator")
                .build()

        pOJOGenerationDelegate.runGenerationTask(generationModel, regenProjectModel)
    }

}