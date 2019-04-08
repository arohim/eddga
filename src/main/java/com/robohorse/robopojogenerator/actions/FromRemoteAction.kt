package com.robohorse.robopojogenerator.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.NewMapperActionController
import com.robohorse.robopojogenerator.injections.Injector
import javax.inject.Inject

class FromRemoteAction : AnAction() {

    @Inject
    lateinit var newMapperActionController: NewMapperActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        newMapperActionController.onActionHandled(e)
    }
}
