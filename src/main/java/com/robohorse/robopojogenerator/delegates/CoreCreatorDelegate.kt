package com.robohorse.robopojogenerator.delegates

import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.ProjectModel
import java.io.File
import javax.inject.Inject

open class CoreCreatorDelegate @Inject constructor() {

    @Inject
    lateinit var directoryCreatorDelegate: DirectoryCreatorDelegate

    protected fun regenProjectModel(projectModel: ProjectModel, folderPath: String): ProjectModel {
        val projectDir = PsiManager.getInstance(projectModel.project)
                .findDirectory(projectModel.project.baseDir)
                ?: throw PathException()
        val path = projectModel.project.basePath + File.separator + folderPath
        val directory = directoryCreatorDelegate.createDirectory(projectModel, projectDir, path)
                ?: throw PathException()
        return ProjectModel.Builder()
                .setDirectory(directory)
                .setDirectoryPath(directory.virtualFile.path)
                .setPackageName(projectModel.packageName)
                .setProject(projectModel.project)
                .setVirtualFolder(projectModel.virtualFolder)
                .build()
    }
}