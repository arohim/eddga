package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.common.MapperCreator
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class MapperGeneratorDelegate @Inject constructor() {

    @Inject
    lateinit var mapperCreator: MapperCreator

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var messageDelegate: MessageDelegate

    fun runGenerationTask(generationModel: GenerationModel,
                          projectModel: ProjectModel,
                          mapperGeneratorModel: MapperGeneratorModel) {
        try {
            mapperCreator.generateFiles(generationModel, projectModel, mapperGeneratorModel)
            messageDelegate.showSuccessMessage()
        } catch (e: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(e)
        } finally {
            environmentDelegate.refreshProject(projectModel)
        }
    }
}