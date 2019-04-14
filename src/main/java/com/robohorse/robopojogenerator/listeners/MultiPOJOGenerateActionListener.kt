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
        val textField = generatorView.classNameTextField

        val coreGeneratorModel = CoreGeneratorModel()
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
        }
    }
}
