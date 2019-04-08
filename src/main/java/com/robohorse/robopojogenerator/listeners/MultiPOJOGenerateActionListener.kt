package com.robohorse.robopojogenerator.listeners

import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.view.ui.POJOGeneratorVew
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.inject.Inject

open class MultiPOJOGenerateActionListener @Inject constructor(private val generatorVew: POJOGeneratorVew,
                                                               private val event: AnActionEvent,
                                                               private val generationModel: GenerationModel,
                                                               private val eventListener: GuiFormEventListener) : ActionListener {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var classGenerateHelper: ClassGenerateHelper

    init {
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: ActionEvent) {
        val textArea = generatorVew.textArea
        val textField = generatorVew.classNameTextField

        saveConfiguration()
        var content = textArea?.text
        val className = textField.text
        try {
            content = classGenerateHelper.validateJsonContent(content)
            generationModel.content = content
            generationModel.rootClassName = className
            eventListener.onJsonDataObtained(generationModel)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun saveConfiguration() {
        event.project?.let {
            val component = ProjectConfigurationComponent.getInstance(it)
        }
    }
}
