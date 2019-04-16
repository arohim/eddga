package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.listeners.*
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew
import java.io.File

import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
open class CoreGeneratorViewBinder @Inject constructor() {

    fun bindView(builder: DialogBuilder, event: AnActionEvent, eventListener: CoreGeneratorFormEventListener) {
        val generatorView = CoreGeneratorVew()

        val actionListener = MultiPOJOGenerateActionListener(generatorView, event, eventListener)
        generatorView.generateButton.addActionListener(actionListener)
//        generatorView.domainPathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.domainPath))
//        generatorView.roguePathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.roguePath))
//        generatorView.cachePathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.cachePath))
//        generatorView.dataPathButton.addActionListener(ChooseFileActionListener(basePath, generatorView.dataPath))

        event.project?.let {
            generatorView.basePath.text = it.basePath + File.separator
            val component = ProjectConfigurationComponent.getInstance(it)
            bindPath(component, generatorView)
            bindCheckBox(component, generatorView)
        }

        builder.setCenterPanel(generatorView.rootView)
//        builder.setTitle(generationModel.dialogTitle)
        builder.removeAllActions()
        builder.show()
    }

    private fun bindPath(component: ProjectConfigurationComponent, generatorView: CoreGeneratorVew) {
        component.domainPath?.let {
            generatorView.domainPath.text = it
        }
        component.roguePath?.let {
            generatorView.roguePath.text = it
        }
        component.cachePath?.let {
            generatorView.cachePath.text = it
        }
        component.dataPath?.let {
            generatorView.dataPath.text = it
        }
        component.remotePath?.let {
            generatorView.remotePath.text = it
        }
        component.domainTestPath?.let {
            generatorView.domainTestPath.text = it
        }
        component.cacheTestPath?.let {
            generatorView.cacheTestPath.text = it
        }
        component.dataTestPath?.let {
            generatorView.dataTestPath.text = it
        }
        component.remoteTestPath?.let {
            generatorView.remoteTestPath.text = it
        }
        component.rootClassNameTextField?.let {
            generatorView.rootClassNameTextField.text = it
        }
    }

    private fun bindCheckBox(component: ProjectConfigurationComponent, generatorView: CoreGeneratorVew) {
        component.domainCheckBox.let {
            generatorView.genDomainCheckBox.isBorderPaintedFlat = it
        }
        component.rogueCheckBox.let {
            generatorView.genRogueCheckBox.isBorderPaintedFlat = it
        }
        component.cacheCheckBox.let {
            generatorView.genCacheCheckBox.isBorderPaintedFlat = it
        }
        component.dataCheckBox.let {
            generatorView.genDataCheckBox.isBorderPaintedFlat = it
        }
        component.remoteCheckBox.let {
            generatorView.genRemoteCheckBox.isBorderPaintedFlat = it
        }
        component.domainTestCheckBox.let {
            generatorView.genDomainTestCheckBox.isBorderPaintedFlat = it
        }
        component.dataTestCheckBox.let {
            generatorView.genDataTestCheckBox.isBorderPaintedFlat = it
        }
        component.cacheTestCheckBox.let {
            generatorView.genCacheTestCheckBox.isBorderPaintedFlat = it
        }
        component.remoteTestCheckBox.let {
            generatorView.genRemoteTestCheckBox.isBorderPaintedFlat = it
        }
    }
}
