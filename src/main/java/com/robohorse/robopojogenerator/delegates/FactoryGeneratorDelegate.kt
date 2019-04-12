package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.common.FactoryCreator
import com.robohorse.robopojogenerator.generator.common.MapperCreator
import com.robohorse.robopojogenerator.models.FactoryGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class FactoryGeneratorDelegate @Inject constructor() {

    @Inject
    lateinit var factoryCreator: FactoryCreator

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var messageDelegate: MessageDelegate

    fun runGenerationTask(generationModel: GenerationModel,
                          projectModel: ProjectModel,
                          factoryGeneratorModel: FactoryGeneratorModel) {
        try {
            factoryCreator.generateFiles(generationModel, projectModel, factoryGeneratorModel)
            messageDelegate.showSuccessMessage()
        } catch (e: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(e)
        } finally {
            environmentDelegate.refreshProject(projectModel)
        }
    }
}