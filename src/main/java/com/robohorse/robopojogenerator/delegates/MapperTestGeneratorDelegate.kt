package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.common.MapperTestCreator
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperTestGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class MapperTestGeneratorDelegate @Inject constructor() {

    @Inject
    lateinit var mapperTestCreator: MapperTestCreator

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var messageDelegate: MessageDelegate

    fun runGenerationTask(generationModel: GenerationModel,
                          projectModel: ProjectModel,
                          mapperTestGeneratorModel: MapperTestGeneratorModel) {
        try {
            mapperTestCreator.generateFiles(generationModel, projectModel, mapperTestGeneratorModel)
            messageDelegate.showSuccessMessage()
        } catch (e: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(e)
        } finally {
            environmentDelegate.refreshProject(projectModel)
        }
    }
}