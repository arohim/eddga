package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.delegates.*
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.listeners.CoreGeneratorFormEventListener
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
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
                        domainCreatorDelegate.runGenerationTask(it, projectModel, coreGeneratorModel)
                        dataCreatorDelegate.runGenerationTask(it, projectModel, coreGeneratorModel)
                        cacheCreatorDelegate.runGenerationTask(it, projectModel, coreGeneratorModel)
                        rogueCreatorDelegate.runGenerationTask(it, projectModel, coreGeneratorModel)
                        remoteCreatorDelegate.runGenerationTask(it, projectModel, coreGeneratorModel)
                    }
                    window.dispose()
                }
            })
        }
    }

    companion object {
        const val MODEL_PATH = "/model"
        const val MAPPER_PATH = "/mapper"
    }
}
