package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class RemoteCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var mapperGenerationDelegate: MapperGeneratorDelegate

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generateMapper(projectModel, coreGeneratorModel)
        generateRemoteImpl(projectModel, coreGeneratorModel)
    }

    private fun generateRemoteImpl(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.remotePath ?: throw PathException()
        val regenProjectModel = regenProjectModel(projectModel, path)

        val fileTemplateManager = fileTemplateWriterDelegate.getInstance(regenProjectModel.project)
        val className = coreGeneratorModel.rootClassName

        val templateName = "ApiRemoteImpl"
        val fileNameSuffix = "ARemoteImpl"

        val templateProperties = fileTemplateManager.defaultProperties.also {
            it["CLASS_NAME"] = className
        }

        fileTemplateWriterDelegate.writeTemplate(regenProjectModel.directory,
                className + fileNameSuffix,
                templateName, templateProperties)
    }

    private fun generateMapper(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.remotePath + CoreGeneratorActionController.MAPPER_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val fromMapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "EntityMapper",
                templateName = "FromRemoteMapper",
                mapToMethodName = "mapToRemote",
                mapFromMethodName = "mapFromRemote",
                isNullable = true
        )

        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, fromMapperGeneratorModel)

        val toMapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "ModelMapper",
                templateName = "ToRemoteMapper",
                mapToMethodName = "mapToRemote",
                mapFromMethodName = "mapFromRemote",
                isNullable = false
        )
        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, toMapperGeneratorModel)
    }
}