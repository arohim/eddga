package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.view.binders.CoreGeneratorViewBinder
import com.robohorse.robopojogenerator.view.binders.GeneratorViewBinder
import javax.inject.Inject

open class CoreGeneratorActionController @Inject constructor() {

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var coreGeneratorViewBinder: CoreGeneratorViewBinder

    fun onActionHandled(event: AnActionEvent) {
        try {
            proceed(event)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent) {
        messageDelegate.showMessage("Not Implemented", "Will be implemented soon")

//        val projectModel = environmentDelegate.obtainProjectModel(event)
        val dialogBuilder = DialogBuilder()
        val window = dialogBuilder.window

        coreGeneratorViewBinder.bindView(dialogBuilder) {
            window.dispose()

        }
    }
}