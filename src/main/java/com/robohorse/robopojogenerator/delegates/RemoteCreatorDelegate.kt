package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
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

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generateMapper(projectModel, coreGeneratorModel)
//        generateFactory(generationModel, projectModel)
//        generateMapperUnitTest(generationModel, projectModel)
//        generateUseCase(generationModel, projectModel)
    }

    private fun generateUseCase(generationModel: GenerationModel, projectModel: ProjectModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generateMapperUnitTest(generationModel: GenerationModel, projectModel: ProjectModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generateFactory(generationModel: GenerationModel, projectModel: ProjectModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                mapFromMethodName = "mapFromRemote"
        )

        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, fromMapperGeneratorModel)

        val toMapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "ModelMapper",
                templateName = "ToRemoteMapper",
                mapToMethodName = "mapToRemote",
                mapFromMethodName = "mapFromRemote"
        )
        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, toMapperGeneratorModel)
    }
}