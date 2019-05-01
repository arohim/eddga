package com.robohorse.robopojogenerator.actions.mapper

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.controllers.MapperGeneratorActionController
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import javax.inject.Inject

class DataMapperAction : AnAction() {

    @Inject
    lateinit var mapperGeneratorActionController: MapperGeneratorActionController

    init {
        Injector.initModules()
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val mapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "Mapper",
                templateName = "DataMapper",
                mapToMethodName = "mapToEntity",
                mapFromMethodName = "mapFromEntity",
                isNullable = false
        )

        mapperGeneratorActionController.onActionHandled(e, mapperGeneratorModel)
    }
}
