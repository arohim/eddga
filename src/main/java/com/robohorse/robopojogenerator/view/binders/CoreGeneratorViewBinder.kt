package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.listeners.*
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew

import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
class CoreGeneratorViewBinder @Inject
constructor() {

    fun bindView(builder: DialogBuilder, event: AnActionEvent, eventListener: GuiFormEventListener) {
        val generatorVew = CoreGeneratorVew()

        val actionListener = CoreGenerateActionListener(generatorVew, event, eventListener)
        generatorVew.generateButton.addActionListener(actionListener)
        generatorVew.domainPathButton.addActionListener(object : ChooseFileActionListener(event.project?.basePath, generatorVew.domainPath) {})
        generatorVew.cachePathButton.addActionListener(object : ChooseFileActionListener(event.project?.basePath, generatorVew.cachePath) {})
        generatorVew.dataPathButton.addActionListener(object : ChooseFileActionListener(event.project?.basePath, generatorVew.dataPath) {})
        generatorVew.roguePathButton.addActionListener(object : ChooseFileActionListener(event.project?.basePath, generatorVew.roguePath) {})

        event.project?.let {
            val component = ProjectConfigurationComponent.getInstance(it)
            generatorVew.domainPath.text = component.domainPath
            generatorVew.dataPath.text = component.dataPath
            generatorVew.cachePath.text = component.cachePath
            generatorVew.roguePath.text = component.roguePath
        }

        builder.setCenterPanel(generatorVew.rootView)
        builder.setTitle("Core POJO & Mapper Generator")
        builder.removeAllActions()
        builder.show()

    }
}
