package com.robohorse.robopojogenerator.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.ClassNameTemplateActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.ClassNameTemplateModel
import javax.inject.Inject

class ToRemoteMapperUnitTestAction : AnAction() {

    @Inject
    lateinit var newMapperActionController: ClassNameTemplateActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val classNameTemplateModel = ClassNameTemplateModel(
                "To remote mapper Unit Test",
                "ToRemoteMapperUnitTest",
                "ModelMapperTest"
        )
        newMapperActionController.onActionHandled(e, classNameTemplateModel)
    }
}
