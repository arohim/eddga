package com.robohorse.robopojogenerator.delegates

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.models.ProjectModel
import java.io.File
import javax.inject.Inject


open class DirectoryCreatorDelegate @Inject constructor() {

    @Inject
    lateinit var environmentDelegate: ProjectEnvironmentDelegate

    open fun createDirectory(projectModel: ProjectModel, parent: PsiDirectory, packageName: String): PsiDirectory? {
        var directoryCreated: PsiDirectory? = null

        LocalFileSystem.getInstance().findFileByPath(packageName)?.let {
            directoryCreated = PsiManager.getInstance(projectModel.project).findDirectory(it)
            return directoryCreated
        }

        val runnable = Runnable {
            val file = File(packageName)
            val result = file.mkdirs()
            System.out.print("created : $result")
            environmentDelegate.refreshProject(projectModel)
            Thread.sleep(1000)
        }
        WriteCommandAction.runWriteCommandAction(projectModel.project, runnable)
        LocalFileSystem.getInstance().findFileByPath(packageName)?.let {
            directoryCreated = PsiManager.getInstance(projectModel.project).findDirectory(it)
        }
        return directoryCreated
    }

}