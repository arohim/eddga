package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.delegates.*
import com.robohorse.robopojogenerator.errors.RoboPluginException
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

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var domainCreatorDelegate: DomainCreatorDelegate

    @Inject
    lateinit var dataCreatorDelegate: DataCreatorDelegate

    @Inject
    lateinit var cacheCreatorDelegate: CacheCreatorDelegate

    @Inject
    lateinit var rogueCreatorDelegate: RogueCreatorDelegate

    @Inject
    lateinit var remoteCreatorDelegate: RemoteCreatorDelegate

    @Inject
    lateinit var remoteTestCreatorDelegate: RemoteTestCreatorDelegate

    @Inject
    lateinit var cacheCreatorTestDelegate: CacheTestCreatorDelegate

    @Inject
    lateinit var dataCreatorTestDelegate: DataTestCreatorDelegate

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
            bindView(dialogBuilder, event, object : CoreGeneratorFormEventListener {
                override fun onJsonDataObtained(coreGeneratorModel: CoreGeneratorModel) {
                    generate(coreGeneratorModel, projectModel)
                    window.dispose()
                }
            })
        }
    }

    private fun generate(coreGeneratorModel: CoreGeneratorModel, projectModel: ProjectModel) {
        if (coreGeneratorModel.isGenerateDomain)
            domainCreatorDelegate.runGenerationTask(projectModel, coreGeneratorModel)
        if (coreGeneratorModel.isGenerateData)
            dataCreatorDelegate.runGenerationTask(projectModel, coreGeneratorModel)
        if (coreGeneratorModel.isGenerateCache)
            cacheCreatorDelegate.runGenerationTask(projectModel, coreGeneratorModel)
        if (coreGeneratorModel.isGenerateRogue)
            rogueCreatorDelegate.runGenerationTask(projectModel, coreGeneratorModel)
        if (coreGeneratorModel.isGenerateRemote)
            remoteCreatorDelegate.runGenerationTask(projectModel, coreGeneratorModel)
        if (coreGeneratorModel.isGenerateRemoteTest)
            remoteTestCreatorDelegate.runGenerationTask(projectModel, coreGeneratorModel)
        if (coreGeneratorModel.isGenerateCacheTest)
            cacheCreatorTestDelegate.runGenerationTask(projectModel, coreGeneratorModel)
        if (coreGeneratorModel.isGenerateDataTest)
            dataCreatorTestDelegate.runGenerationTask(projectModel, coreGeneratorModel)
    }

    companion object {
        const val MODEL_PATH = "/model"
        const val MAPPER_PATH = "/mapper"
    }
}
