package com.robohorse.robopojogenerator.actions.data

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.ClassNameTemplateActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.ClassNameTemplateModel
import javax.inject.Inject

class DataStoreFactoryTestAction : AnAction() {

    @Inject
    lateinit var controller: ClassNameTemplateActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val classNameTemplateModel = ClassNameTemplateModel(
                dialogTitle = "Data Store Factory Unit Test",
                templateName = "DataStoreFactoryTest",
                fileNameSuffix = "DataStoreFactoryTest"
        )
        controller.onActionHandled(e, classNameTemplateModel)
    }
}