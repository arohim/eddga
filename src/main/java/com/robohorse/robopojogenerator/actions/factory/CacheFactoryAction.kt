package com.robohorse.robopojogenerator.actions.factory

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.FactoryGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.FactoryGeneratorModel
import javax.inject.Inject

class CacheFactoryAction : AnAction() {

    @Inject
    lateinit var controller: FactoryGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val factoryGeneratorModel = FactoryGeneratorModel(
                fileNameSuffix = "Factory",
                templateName = "CacheFactory",
                remote = false,
                cache = true,
                data = true,
                domain = false
        )

        controller.onActionHandled(e, factoryGeneratorModel)
    }
}
