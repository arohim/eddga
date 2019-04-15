package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.delegates.DirectoryCreatorDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.delegates.ProjectEnvironmentDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.listeners.CoreGeneratorFormEventListener
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import com.robohorse.robopojogenerator.view.binders.CoreGeneratorViewBinder
import javax.inject.Inject

open class MultiPOJOGeneratorActionController @Inject constructor() {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var environmentDelegate: ProjectEnvironmentDelegate

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
                override fun onJsonDataObtained(coreGeneratorModel: CoreGeneratorModel) {
                    event.project?.let {
                        generateFolders(it, projectModel, coreGeneratorModel)
                        generatePOJO(it, projectModel, coreGeneratorModel)
                    }
                    window.dispose()
                }
            })
        }
    }

    private fun generatePOJO(project: Project, projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {

    }

    private fun generateFolders(project: Project, projectModel: ProjectModel, model: CoreGeneratorModel) {
        val projectDir = PsiManager.getInstance(project).findDirectory(projectModel.project.baseDir)
                ?: throw PathException()

        // create domain directories
        val domainPath = projectModel.project.basePath + model.domainPath
        directoryCreatorDelegate.createDirectory(project, projectDir, domainPath)

        // create data directories
        val dataPath = projectModel.project.basePath + model.dataPath
        directoryCreatorDelegate.createDirectory(project, projectDir, dataPath)

        // create cache directories
        val cachePath = projectModel.project.basePath + model.cachePath
        directoryCreatorDelegate.createDirectory(project, projectDir, cachePath)

        // create rogue directories
        val roguePath = projectModel.project.basePath + model.roguePath
        directoryCreatorDelegate.createDirectory(project, projectDir, roguePath)

        // create remote directories
        val remotePath = projectModel.project.basePath + model.remotePath
        directoryCreatorDelegate.createDirectory(project, projectDir, remotePath)

        environmentDelegate.refreshProject(projectModel)
    }

}
