package com.robohorse.robopojogenerator.delegates

import com.intellij.ide.util.DirectoryUtil
import com.intellij.ide.util.PackageUtil
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject


open class DirectoryCreatorDelegate @Inject constructor() {

    @Inject
    lateinit var environmentDelegate: ProjectEnvironmentDelegate

    open fun createDirectory(projectModel: ProjectModel, parent: PsiDirectory, packageName: String): PsiDirectory? {
        var directory: PsiDirectory? = null
        val file = LocalFileSystem.getInstance().findFileByPath(packageName)

        if (file != null) {
            directory = PsiManager.getInstance(projectModel.project).findDirectory(file)
            return directory
        }

        val runnable = Runnable {
            directory = DirectoryUtil.mkdirs(PsiManager.getInstance(projectModel.project), packageName)
        }
        WriteCommandAction.runWriteCommandAction(projectModel.project, runnable)
        environmentDelegate.refreshProject(projectModel)
        return directory
    }

}