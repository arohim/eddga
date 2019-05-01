package com.robohorse.robopojogenerator.actions.mapper

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.MapperTestGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.MapperTestGeneratorModel
import javax.inject.Inject

class DataMapperUnitTestAction : AnAction() {

    @Inject
    lateinit var controller: MapperTestGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val mapperTestGeneratorModel = MapperTestGeneratorModel(
                from = "domain",
                to = "entity",
                fileNameSuffix = "MapperTest",
                templateName = "DataMapperTest",
                classNameSuffix = "Mapper",
                isNullable = false
        )
        controller.onActionHandled(e, mapperTestGeneratorModel)
    }
}
