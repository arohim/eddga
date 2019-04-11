package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.MapperGeneratorDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.listeners.GuiFormEventListener
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.view.binders.CorePOJOGeneratorViewBinder
import javax.inject.Inject

open class MapperGeneratorActionController @Inject constructor() {

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var coreGeneratorViewBinder: CorePOJOGeneratorViewBinder

    @Inject
    lateinit var generationDelegate: MapperGeneratorDelegate

    fun onActionHandled(event: AnActionEvent, mapperGeneratorModels: MapperGeneratorModel) {
        try {
            proceed(event, mapperGeneratorModels)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent, mapperGeneratorModel: MapperGeneratorModel) {
        val projectModel = environmentDelegate.obtainProjectModel(event)
        val dialogBuilder = DialogBuilder()
        val window = dialogBuilder.window

        val generationModel = GenerationModel()
        coreGeneratorViewBinder.bindView(dialogBuilder, event, generationModel, GuiFormEventListener { generationModel ->
            window.dispose()
            generationDelegate.runGenerationTask(generationModel, projectModel, mapperGeneratorModel)
        })
    }
}