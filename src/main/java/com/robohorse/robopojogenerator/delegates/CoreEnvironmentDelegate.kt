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
import com.intellij.openapi.vfs.VfsUtil.findFileByIoFile
import java.util.concurrent.atomic.AtomicReference
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File


class CoreEnvironmentDelegate @Inject
constructor() {

    @Throws(RoboPluginException::class)
    fun obtainProjectModel(event: AnActionEvent): ProjectModel {
        val directoryPath = checkPath(event)
        val project = event.project
        val virtualFolder: VirtualFile? = event.project?.baseDir

        val packageName = virtualFolder?.let {
            ProjectRootManager
                    .getInstance(project!!)
                    .fileIndex
                    .getPackageNameByDirectory(it)
        }

        return ProjectModel.Builder()
                .setDirectoryPath(directoryPath)
                .setPackageName(packageName)
                .setProject(project)
                .setVirtualFolder(virtualFolder)
                .build()
    }

    fun refreshProject(projectModel: ProjectModel) {
        ProjectView.getInstance(projectModel.project).refresh()
        projectModel.virtualFolder?.refresh(false, true)
    }

    @Throws(RoboPluginException::class)
    private fun checkPath(event: AnActionEvent): String {
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

        return component.domainPath
    }

    private fun makeVirtualFile(file: String): VirtualFile? {
        val localFileSystem = LocalFileSystem.getInstance()
        return localFileSystem.findFileByIoFile(File(file))
    }

    private fun findProject(file: String): Project {
        val localFileSystem = LocalFileSystem.getInstance()
        val projectLocator = ProjectLocator.getInstance()
        val ret = AtomicReference<Project>()
        FileUtil.processFilesRecursively(
                File(file)
        ) FileUtil@{ f ->
            val vf = localFileSystem.findFileByIoFile(f)
            if (vf != null) {
                ret.set(projectLocator.guessProjectForFile(vf))
                return@FileUtil false
            }
            true
        }
        return ret.get()
    }
}
