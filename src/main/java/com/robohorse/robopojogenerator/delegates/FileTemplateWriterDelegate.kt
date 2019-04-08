package com.robohorse.robopojogenerator.delegates

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import java.util.*
import javax.inject.Inject

open class FileTemplateWriterDelegate @Inject constructor() {

    private var instance: FileTemplateManager? = null

    open val defaultProperties = this.instance?.defaultProperties

    open fun getInstance(project: Project): FileTemplateManager {
        if (instance == null)
            instance = FileTemplateManager.getInstance(project)
        return this.instance!!
    }

    open fun writeTemplate(directory: PsiDirectory, fileName: String?, templateName: String, templateProperties: Properties?) {
        instance?.getInternalTemplate(templateName)?.let {
            FileTemplateUtil.createFromTemplate(it, fileName, templateProperties, directory)
        }
    }

}