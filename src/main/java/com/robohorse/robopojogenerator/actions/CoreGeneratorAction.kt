package com.robohorse.robopojogenerator.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import javax.inject.Inject

class CoreGeneratorAction : AnAction() {

    @Inject
    lateinit var coreGeneratorActionController: CoreGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        coreGeneratorActionController.onActionHandled(e)
    }
}