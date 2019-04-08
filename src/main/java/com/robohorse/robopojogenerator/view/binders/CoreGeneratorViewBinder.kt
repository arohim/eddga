package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.listeners.*
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew

import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
open class CoreGeneratorViewBinder @Inject constructor() {

    fun bindView(builder: DialogBuilder, event: AnActionEvent, eventListener: GuiFormEventListener) {
        val generatorView = CoreGeneratorVew()
        val basePath = event.project?.basePath

        val actionListener = MultiPOJOGenerateActionListener(generatorView, event, GenerationModel.Builder().build(), eventListener)
        generatorView.generateButton.addActionListener(actionListener)
        generatorView.domainPathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.domainPath))
        generatorView.roguePathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.roguePath))
        generatorView.cachePathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.cachePath))
        generatorView.dataPathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.dataPath))

        event.project?.let {
            val component = ProjectConfigurationComponent.getInstance(it)
            generatorView.domainPath.text = component.domainPath
            generatorView.roguePath.text = component.roguePath
            generatorView.cachePath.text = component.cachePath
            generatorView.dataPath.text = component.dataPath
        }

        builder.setCenterPanel(generatorView.rootView)
//        builder.setTitle(generationModel.dialogTitle)
        builder.removeAllActions()
        builder.show()
    }
}
