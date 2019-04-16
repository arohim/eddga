package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.FactoryGeneratorDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.listeners.GuiFormEventListener
import com.robohorse.robopojogenerator.models.FactoryGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.view.binders.CorePOJOGeneratorViewBinder
import javax.inject.Inject

open class FactoryGeneratorActionController @Inject constructor() {

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var coreGeneratorViewBinder: CorePOJOGeneratorViewBinder

    @Inject
    lateinit var generationDelegate: FactoryGeneratorDelegate

    fun onActionHandled(event: AnActionEvent, factoryGeneratorModel: FactoryGeneratorModel) {
        try {
            proceed(event, factoryGeneratorModel)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent, factoryGeneratorModel: FactoryGeneratorModel) {
        val projectModel = environmentDelegate.obtainProjectModel(event)
        val dialogBuilder = DialogBuilder()
        val window = dialogBuilder.window

        val generationModel = GenerationModel()
        with(coreGeneratorViewBinder) {
            bindView(dialogBuilder, event, generationModel, GuiFormEventListener { generationModel ->
                window.dispose()
                generationDelegate.runGenerationTask(generationModel, projectModel, factoryGeneratorModel)
            })
        }
    }
}