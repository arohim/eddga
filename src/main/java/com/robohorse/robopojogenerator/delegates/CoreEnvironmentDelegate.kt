package com.robohorse.robopojogenerator.delegates

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.models.ProjectModel

import javax.inject.Inject

class CoreEnvironmentDelegate @Inject
constructor() {

    @Throws(RoboPluginException::class)
    fun obtainProjectModel(event: AnActionEvent): ProjectModel {
        val directory = checkPath(event)
        val project = event.project
        val virtualFolder = event.getData(LangDataKeys.VIRTUAL_FILE)

        val packageName = ProjectRootManager
                .getInstance(project!!)
                .fileIndex
                .getPackageNameByDirectory(virtualFolder!!)
        return ProjectModel.Builder()
                .setDirectory(directory)
                .setPackageName(packageName)
                .setProject(project)
                .setVirtualFolder(virtualFolder)
                .build()
    }

    fun refreshProject(projectModel: ProjectModel) {
        ProjectView.getInstance(projectModel.project).refresh()
        projectModel.virtualFolder.refresh(false, true)
    }

    @Throws(RoboPluginException::class)
    private fun checkPath(event: AnActionEvent): PsiDirectory {
        val component = event.project?.let {
            ProjectConfigurationComponent.getInstance(it)
        } ?: throw PathException()

        if (component.domainPath.isEmpty() ||
                component.dataPath.isEmpty() ||
                component.cachePath.isEmpty() ||
                component.roguePath.isEmpty()
        ) {
            throw PathException()
        }

        val pathItem = event.getData(CommonDataKeys.NAVIGATABLE)
        if (pathItem != null) {
            if (pathItem is PsiDirectory) {
                return pathItem
            }
        }
        throw PathException()
    }
}
