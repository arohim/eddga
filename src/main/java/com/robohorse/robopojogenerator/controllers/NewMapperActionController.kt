package com.robohorse.robopojogenerator.controllers

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import javax.inject.Inject

open class NewMapperActionController @Inject constructor() {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    fun onActionHandled(event: AnActionEvent) {
        try {
            proceed(event)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent) {
        val projectModel = environmentDelegate.obtainProjectModel(event)
        val className = Messages.showInputDialog(event.project,
                "Enter Class Name",
                "From remote Mapper",
                null)

        FileTemplateManager.getInstance(projectModel.project).getInternalTemplate("FromRemoteMapper")?.let { fileTemplate ->
            val templateProperties = FileTemplateManager.getInstance(projectModel.project).defaultProperties
            templateProperties["CLASS_NAME"] = className
            FileTemplateUtil.createFromTemplate(fileTemplate, className, templateProperties, projectModel.directory)
        }
    }
}