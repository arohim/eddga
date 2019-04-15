package com.robohorse.robopojogenerator.actions.pojo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.MultiPOJOGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import javax.inject.Inject

class MultiPOJOAction : AnAction() {

    @Inject
    lateinit var controller: MultiPOJOGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        controller.onActionHandled(e)
    }
}
