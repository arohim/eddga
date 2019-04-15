package com.robohorse.robopojogenerator.delegates

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class ProjectEnvironmentDelegate @Inject constructor() {

    @Throws(RoboPluginException::class)
    fun obtainProjectModel(event: AnActionEvent): ProjectModel {
        val project = event.project ?: throw Exception("Unknown exception")
        val directory = PsiManager.getInstance(project).findDirectory(getProjectPath(event).baseDir)
        val virtualFolder = directory?.virtualFile

        val packageName = ProjectRootManager
                .getInstance(project)
                .fileIndex
                .getPackageNameByDirectory(virtualFolder!!)
        return ProjectModel.Builder()
                .setDirectory(directory)
                .setDirectoryPath(directory.virtualFile.path)
                .setVirtualFolder(virtualFolder)
                .setPackageName(packageName)
                .setProject(project)
                .build()
    }

    fun refreshProject(projectModel: ProjectModel) {
        ProjectView.getInstance(projectModel.project).refresh()
        projectModel.virtualFolder.refresh(false, true)
    }

    @Throws(RoboPluginException::class)
    private fun getProjectPath(event: AnActionEvent): Project {
        val pathItem = event.getData(CommonDataKeys.PROJECT)
        if (pathItem != null) {
            return pathItem
        }
        throw PathException()
    }
}
