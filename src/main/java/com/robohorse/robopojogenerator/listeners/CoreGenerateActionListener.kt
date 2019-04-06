package com.robohorse.robopojogenerator.listeners

import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
class CoreGenerateActionListener(private val generatorVew: CoreGeneratorVew,
                                 private val event: AnActionEvent,
                                 private val eventListener: GuiFormEventListener) : ActionListener {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var classGenerateHelper: ClassGenerateHelper

    init {
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: ActionEvent) {
        val textArea = generatorVew.getTextArea()

        val annotationEnum = AnnotationEnum.GSON
        saveConfiguration()

        val useKotlin = true
        val rewriteClasses = true
        val useSetters = false
        val useGetters = false
        val useStrings = false

        var content = textArea?.text
        val className = "Response"

        try {
            content = classGenerateHelper.validateJsonContent(content)
            eventListener.onJsonDataObtained(GenerationModel.Builder()
                    .useKotlin(useKotlin)
                    .setAnnotationItem(annotationEnum)
                    .setContent(content)
                    .setSettersAvailable(useSetters)
                    .setGettersAvailable(useGetters)
                    .setToStringAvailable(useStrings)
                    .setRootClassName(className)
                    .setRewriteClasses(rewriteClasses)
                    .build())

        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun saveConfiguration() {
        event.project?.let {
            val component = ProjectConfigurationComponent.getInstance(it)
            component.domainPath = generatorVew.domainPath.text
            component.dataPath = generatorVew.dataPath.text
            component.cachePath = generatorVew.cachePath.text
            component.roguePath = generatorVew.roguePath.text
        }
    }
}
