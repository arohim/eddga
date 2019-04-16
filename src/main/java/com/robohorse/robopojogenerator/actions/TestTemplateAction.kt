package com.robohorse.robopojogenerator.actions

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDirectory

class TestTemplateAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val dir = event.getData(CommonDataKeys.NAVIGATABLE) as PsiDirectory

        val className = "TestTemplate"

        event.project?.let {
            FileTemplateManager.getInstance(it).getInternalTemplate("Test")
        }?.let { fileTemplate ->
            val templateProperties = FileTemplateManager.getInstance(event.project!!).defaultProperties
            templateProperties["CLASS_NAME"] = "Hello"
            FileTemplateUtil.createFromTemplate(fileTemplate, className, templateProperties, dir)
        }

//        val runnable = Runnable { JavaDirectoryService.getInstance().createClass(dir, className, "Test", false, varTemplate) }
//        WriteCommandAction.runWriteCommandAction(event.project, runnable)
    }
}
