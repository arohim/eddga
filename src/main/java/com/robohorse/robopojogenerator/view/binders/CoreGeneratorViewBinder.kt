package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.listeners.*
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew

import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
open class CoreGeneratorViewBinder @Inject constructor() {

    fun bindView(builder: DialogBuilder, event: AnActionEvent, projectModel: ProjectModel, eventListener: CoreGeneratorFormEventListener) {
        val generatorView = CoreGeneratorVew()

        val actionListener = MultiPOJOGenerateActionListener(generatorView, event, eventListener)
        generatorView.generateButton.addActionListener(actionListener)
//        generatorView.domainPathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.domainPath))
//        generatorView.roguePathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.roguePath))
//        generatorView.cachePathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.cachePath))
//        generatorView.dataPathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.dataPath))

        event.project?.let {
            generatorView.basePath.text = it.basePath
            val component = ProjectConfigurationComponent.getInstance(it)
            generatorView.domainPath.text = component.domainPath
            generatorView.roguePath.text = component.roguePath
            generatorView.cachePath.text = component.cachePath
            generatorView.dataPath.text = component.dataPath
            generatorView.remotePath.text = component.remotePath
        }

        builder.setCenterPanel(generatorView.rootView)
//        builder.setTitle(generationModel.dialogTitle)
        builder.removeAllActions()
        builder.show()
    }
}