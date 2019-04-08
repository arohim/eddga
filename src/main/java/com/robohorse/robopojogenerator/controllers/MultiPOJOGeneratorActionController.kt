package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.listeners.GuiFormEventListener
import com.robohorse.robopojogenerator.view.binders.CoreGeneratorViewBinder
import javax.inject.Inject

open class MultiPOJOGeneratorActionController @Inject constructor() {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var viewBinder: CoreGeneratorViewBinder

    fun onActionHandled(event: AnActionEvent) {
        try {
            proceed(event)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent) {
        val projectModel = environmentDelegate.obtainProjectModel(event)
        val dialogBuilder = DialogBuilder()
        val window = dialogBuilder.window

        viewBinder.bindView(dialogBuilder, event, GuiFormEventListener { generationModel ->
            window.dispose()
            // TODO()
        })
    }
}
