package com.robohorse.robopojogenerator.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.CorePOJOGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import javax.inject.Inject

class CoreGeneratorAction : AnAction() {

    @Inject
    lateinit var corePOJOGeneratorActionController: CorePOJOGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
//        corePOJOGeneratorActionController.onActionHandled(e, generationModel)
    }
}