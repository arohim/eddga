package com.robohorse.robopojogenerator.delegates

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.common.ClassCreator
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel

import javax.inject.Inject

open class CoreGenerationDelegate @Inject constructor() {
    @Inject
    lateinit var classCreator: ClassCreator
    @Inject
    lateinit var environmentDelegate: CoreEnvironmentDelegate
    @Inject
    lateinit var messageDelegate: MessageDelegate

    fun runGenerationTask(generationModel: GenerationModel,
                          projectModel: ProjectModel) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(projectModel.project,
                "RoboPOJO Generation", false) {

            override fun run(indicator: ProgressIndicator) {
                try {
                    classCreator.generateFiles(generationModel, projectModel)
                    messageDelegate.showSuccessMessage()

                } catch (e: RoboPluginException) {
                    messageDelegate.onPluginExceptionHandled(e)
                } finally {
                    indicator.stop()
                    environmentDelegate.refreshProject(projectModel)
                }
            }
        })
    }
}
