package com.robohorse.robopojogenerator.listeners

import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.inject.Inject

open class MultiPOJOGenerateActionListener @Inject constructor(private val generatorView: CoreGeneratorVew,
                                                               private val event: AnActionEvent,
                                                               private val eventListener: CoreGeneratorFormEventListener) : ActionListener {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var classGenerateHelper: ClassGenerateHelper

    init {
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: ActionEvent) {
        val textArea = generatorView.textArea
        val textField = generatorView.rootClassNameTextField

        val coreGeneratorModel = CoreGeneratorModel(isGenerateDomainTest = false, isGenerateCacheTest = false, isGenerateDataTest = false, isGenerateRemoteTest = false, isGenerateDomain = false, isGenerateRogue = false, isGenerateCache = false, isGenerateData = false, isGenerateRemote = false)
        saveConfiguration()
        var content = textArea.text
        val className = textField.text
        try {
            content = classGenerateHelper.validateJsonContent(content)
            coreGeneratorModel.content = content
            coreGeneratorModel.rootClassName = className
            coreGeneratorModel.domainPath = generatorView.domainPath.text
            coreGeneratorModel.roguePath = generatorView.roguePath.text
            coreGeneratorModel.cachePath = generatorView.cachePath.text
            coreGeneratorModel.dataPath = generatorView.dataPath.text
            coreGeneratorModel.remotePath = generatorView.remotePath.text

            coreGeneratorModel.domainTestPath = generatorView.domainTestPath.text
            coreGeneratorModel.cacheTestPath = generatorView.cacheTestPath.text
            coreGeneratorModel.dataTestPath = generatorView.dataTestPath.text
            coreGeneratorModel.remoteTestPath = generatorView.remoteTestPath.text

            coreGeneratorModel.isGenerateDomain = generatorView.genDomainCheckBox.isBorderPaintedFlat
            coreGeneratorModel.isGenerateRogue = generatorView.genRogueCheckBox.isBorderPaintedFlat
            coreGeneratorModel.isGenerateCache = generatorView.genCacheCheckBox.isBorderPaintedFlat
            coreGeneratorModel.isGenerateData = generatorView.genDataCheckBox.isBorderPaintedFlat
            coreGeneratorModel.isGenerateRemote = generatorView.genRemoteCheckBox.isBorderPaintedFlat

            coreGeneratorModel.isGenerateDomainTest = generatorView.genDomainTestCheckBox.isBorderPaintedFlat
            coreGeneratorModel.isGenerateCacheTest = generatorView.genCacheTestCheckBox.isBorderPaintedFlat
            coreGeneratorModel.isGenerateDataTest = generatorView.genDataTestCheckBox.isBorderPaintedFlat
            coreGeneratorModel.isGenerateRemoteTest = generatorView.genRemoteTestCheckBox.isBorderPaintedFlat
            eventListener.onJsonDataObtained(coreGeneratorModel)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun saveConfiguration() {
        event.project?.let {
            val component = ProjectConfigurationComponent.getInstance(it)
            component.domainPath = generatorView.domainPath.text
            component.roguePath = generatorView.roguePath.text
            component.cachePath = generatorView.cachePath.text
            component.dataPath = generatorView.dataPath.text
            component.remotePath = generatorView.remotePath.text

            component.domainTestPath = generatorView.domainTestPath.text
            component.cacheTestPath = generatorView.cacheTestPath.text
            component.dataTestPath = generatorView.dataTestPath.text
            component.remoteTestPath = generatorView.remoteTestPath.text

            component.rootClassNameTextField = generatorView.rootClassNameTextField.text

            component.domainCheckBox = generatorView.genDomainCheckBox.isBorderPaintedFlat
            component.rogueCheckBox = generatorView.genRogueCheckBox.isBorderPaintedFlat
            component.cacheCheckBox = generatorView.genCacheCheckBox.isBorderPaintedFlat
            component.dataCheckBox = generatorView.genDataCheckBox.isBorderPaintedFlat
            component.remoteCheckBox = generatorView.genRemoteCheckBox.isBorderPaintedFlat

            component.domainTestCheckBox = generatorView.genDomainTestCheckBox.isBorderPaintedFlat
            component.cacheTestCheckBox = generatorView.genCacheTestCheckBox.isBorderPaintedFlat
            component.dataTestCheckBox = generatorView.genDataTestCheckBox.isBorderPaintedFlat
            component.remoteTestCheckBox = generatorView.genRemoteTestCheckBox.isBorderPaintedFlat
        }
    }
}
