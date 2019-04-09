package com.robohorse.robopojogenerator.delegates

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import javax.inject.Inject

open class DirectoryCreatorDelegate @Inject constructor() {
    open fun createDirectory(project: Project, parent: PsiDirectory, packageName: String): PsiDirectory? {
        var directoryCreated: PsiDirectory? = null

        for (dir in parent.subdirectories) {
            if (dir.name.equals(packageName, ignoreCase = true)) {
                directoryCreated = dir
                break
            }
        }

        if (directoryCreated == null) {
            val runnable = Runnable { directoryCreated = parent.createSubdirectory(packageName) }
            WriteCommandAction.runWriteCommandAction(project, runnable)
        }
        return directoryCreated
    }
}