package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.models.ClassNameTemplateModel
import com.robohorse.robopojogenerator.view.binders.ClassNameTemplateGeneratorViewBinder
import javax.inject.Inject

open class ClassNameTemplateActionController @Inject constructor() {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Inject
    lateinit var viewBinder: ClassNameTemplateGeneratorViewBinder

    fun onActionHandled(event: AnActionEvent, classNameTemplateModel: ClassNameTemplateModel) {
        try {
            proceed(event, classNameTemplateModel)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent, classNameTemplateModel: ClassNameTemplateModel) {
        val projectModel = environmentDelegate.obtainProjectModel(event)

        viewBinder.bindView(event, classNameTemplateModel) { className ->
            if (className == null) return@bindView

            val fileTemplateManager = fileTemplateWriterDelegate.getInstance(projectModel.project)
            val templateProperties = fileTemplateManager.defaultProperties.also {
                it["CLASS_NAME"] = className
            }
            fileTemplateWriterDelegate.writeTemplate(projectModel.directory,
                    className + classNameTemplateModel.fileNameSuffix,
                    classNameTemplateModel.templateName, templateProperties)
        }
    }
}