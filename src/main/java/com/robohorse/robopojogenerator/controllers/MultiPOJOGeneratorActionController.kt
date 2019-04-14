package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.delegates.DirectoryCreatorDelegate
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.listeners.CoreGeneratorFormEventListener
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.view.binders.CoreGeneratorViewBinder
import javax.inject.Inject

open class MultiPOJOGeneratorActionController @Inject constructor() {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var environmentDelegate: EnvironmentDelegate

    @Inject
    lateinit var directoryCreatorDelegate: DirectoryCreatorDelegate

    @Inject
    lateinit var viewBinder: CoreGeneratorViewBinder

    fun onActionHandled(event: AnActionEvent) {
        try {
            proceed(event)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent) {
        val projectModel = environmentDelegate.obtainProjectModel(event)
        val dialogBuilder = DialogBuilder()
        val window = dialogBuilder.window

        with(viewBinder) {
            bindView(dialogBuilder, event, projectModel, object : CoreGeneratorFormEventListener {
                override fun onJsonDataObtained(model: CoreGeneratorModel) {
                    window.dispose()
                    event.project?.let {
                        // create domain directories
                        val domainPath = projectModel.projectDirectoryPath + model.domainPath
                        directoryCreatorDelegate.createDirectory(it, projectModel.projectDirectory, domainPath)

                        // create data directories
                        val dataPath = projectModel.projectDirectoryPath + model.dataPath
                        directoryCreatorDelegate.createDirectory(it, projectModel.projectDirectory, dataPath)

                        // create cache directories
                        val cachePath = projectModel.projectDirectoryPath + model.cachePath
                        directoryCreatorDelegate.createDirectory(it, projectModel.projectDirectory, cachePath)

                        // create rogue directories
                        val roguePath = projectModel.projectDirectoryPath + model.roguePath
                        directoryCreatorDelegate.createDirectory(it, projectModel.projectDirectory, roguePath)

                        // create remote directories
                        val remotePath = projectModel.projectDirectoryPath + model.remotePath
                        directoryCreatorDelegate.createDirectory(it, projectModel.projectDirectory, remotePath)

                        environmentDelegate.refreshRootProject(projectModel)
                    }
                }
            })
        }
    }

}
