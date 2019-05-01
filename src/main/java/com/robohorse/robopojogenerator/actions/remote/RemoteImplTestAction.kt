package com.robohorse.robopojogenerator.actions.remote

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.ClassNameTemplateActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.ClassNameTemplateModel
import javax.inject.Inject

class RemoteImplTestAction : AnAction() {

    @Inject
    lateinit var controller: ClassNameTemplateActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val classNameTemplateModel = ClassNameTemplateModel(
                dialogTitle = "Remote impl unit test",
                templateName = "RemoteImplTest",
                fileNameSuffix = "RemoteImplTest"
        )
        controller.onActionHandled(e, classNameTemplateModel)
    }
}