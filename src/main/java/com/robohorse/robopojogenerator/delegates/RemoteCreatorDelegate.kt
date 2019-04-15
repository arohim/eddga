package com.robohorse.robopojogenerator.delegates

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.controllers.MultiPOJOGeneratorActionController
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import java.io.File
import javax.inject.Inject

open class RemoteCreatorDelegate @Inject constructor() {

    @Inject
    lateinit var directoryCreatorDelegate: DirectoryCreatorDelegate

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var mapperGenerationDelegate: MapperGeneratorDelegate

    fun runGenerationTask(project: Project, projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
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
        val projectDir = PsiManager.getInstance(projectModel.project).findDirectory(projectModel.project.baseDir)
                ?: throw PathException()
        val path = projectModel.project.basePath + File.separator + coreGeneratorModel.remotePath + MultiPOJOGeneratorActionController.MAPPER_PATH
        val directory = directoryCreatorDelegate.createDirectory(projectModel, projectDir, path)
                ?: throw PathException()

        val regenProjectModel = ProjectModel.Builder()
                .setDirectory(directory)
                .setDirectoryPath(directory.virtualFile.path)
                .setPackageName(projectModel.packageName)
                .setProject(projectModel.project)
                .setVirtualFolder(projectModel.virtualFolder)
                .build()

        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()

        val fromMapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "EntityMapper",
                templateName = "FromRemoteMapper"
        )

        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, fromMapperGeneratorModel)

        val toMapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "ModelMapper",
                templateName = "ToRemoteMapper"
        )
        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, toMapperGeneratorModel)
    }
}