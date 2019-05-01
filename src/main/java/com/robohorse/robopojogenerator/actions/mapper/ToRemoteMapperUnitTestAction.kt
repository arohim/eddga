package com.robohorse.robopojogenerator.actions.mapper

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.MapperTestGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.MapperTestGeneratorModel
import javax.inject.Inject

class ToRemoteMapperUnitTestAction : AnAction() {

    @Inject
    lateinit var controller: MapperTestGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val mapperTestGeneratorModel = MapperTestGeneratorModel(
                from = "entity",
                to = "model",
                fileNameSuffix = "ModelMapperTest",
                templateName = "ToRemoteMapperTest",
                classNameSuffix = "EntityMapper",
                isNullable = false
        )
        controller.onActionHandled(e, mapperTestGeneratorModel)
    }
}
