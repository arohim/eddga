package com.robohorse.robopojogenerator.listeners

import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
class CoreGenerateActionListener(private val generatorVew: CoreGeneratorVew,
                                 private val eventListener: GuiFormEventListener) : ActionListener {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var classGenerateHelper: ClassGenerateHelper

    init {
        Injector.getAppComponent().inject(this)
    }

    override fun actionPerformed(e: ActionEvent) {
        //        final JTextArea textArea = generatorVew.getTextArea();
        val annotationEnum = resolveAnnotationItem()
    }

    private fun resolveAnnotationItem(): AnnotationEnum {
        val buttonGroup = generatorVew.typeButtonGroup
        val buttons = buttonGroup!!.elements
        while (buttons.hasMoreElements()) {
            val button = buttons.nextElement()
            if (button.isSelected) {
                for (annotationEnum in AnnotationEnum.values()) {
                    if (annotationEnum.text == button.text) {
                        return annotationEnum
                    }
                }
            }
        }
        return AnnotationEnum.NONE
    }
}