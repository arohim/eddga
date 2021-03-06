package com.robohorse.robopojogenerator.actions.mapper

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.MapperTestGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.MapperTestGeneratorModel
import javax.inject.Inject

class CacheMapperUnitTestAction : AnAction() {

    @Inject
    lateinit var controller: MapperTestGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val mapperTestGeneratorModel = MapperTestGeneratorModel(
                from = "cached",
                to = "entity",
                fileNameSuffix = "EntityMapperTest",
                templateName = "CacheMapperTest",
                classNameSuffix = "EntityMapper",
                isNullable = false
        )
        controller.onActionHandled(e, mapperTestGeneratorModel)
    }
}
