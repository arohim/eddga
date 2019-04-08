package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.robohorse.robopojogenerator.models.ClassNameTemplateModel

import javax.inject.Inject

open class ClassNameTemplateGeneratorViewBinder @Inject constructor() {

    fun bindView(event: AnActionEvent, classNameTemplateModel: ClassNameTemplateModel, success: (className: String?) -> Unit) {
        val className = Messages.showInputDialog(event.project,
                "Enter Class Name",
                classNameTemplateModel.dialogTitle,
                null)

        success(className)
    }
}
