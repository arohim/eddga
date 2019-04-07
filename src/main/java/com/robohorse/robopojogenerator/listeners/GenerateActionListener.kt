package com.robohorse.robopojogenerator.listeners

import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.annotations.CoreLayerEnum
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.injections.Injector
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate.FIELD_KOTLIN_DTO
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate.NON_NULL_FIELD_KOTLIN_DTO
import com.robohorse.robopojogenerator.view.ui.GeneratorVew

import javax.inject.Inject
import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.Enumeration

/**
 * Created by vadim on 24.09.16.
 */
open class GenerateActionListener(private val generatorVew: GeneratorVew,
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

        val coreLayerEnum = resolveCoreLayerItem()
        val annotation = resolveAnnotationEnum(coreLayerEnum)
        val fieldDTOFormat = resolveFieldDTOFormat(coreLayerEnum)
        val useKotlin = true
        val rewriteClasses = true
        val useSetters = false
        val useGetters = false
        val useStrings = false

        var content = textArea.text
        val className = textField.text
        try {
            classGenerateHelper.validateClassName(className)
            content = classGenerateHelper.validateJsonContent(content)
            eventListener.onJsonDataObtained(GenerationModel.Builder()
                    .useKotlin(useKotlin)
                    .setAnnotationItem(annotation)
                    .setContent(content)
                    .setSettersAvailable(useSetters)
                    .setGettersAvailable(useGetters)
                    .setToStringAvailable(useStrings)
                    .setRootClassName(className)
                    .setRewriteClasses(rewriteClasses)
                    .setPrefix(getPrefixName(coreLayerEnum))
                    .setSuffix(getSuffixName(coreLayerEnum))
                    .setFieldDTOFormat(fieldDTOFormat)
                    .build())

        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }

    }

    private fun resolveAnnotationEnum(coreLayerEnum: CoreLayerEnum): AnnotationEnum {
        return when (coreLayerEnum) {
            CoreLayerEnum.Remote -> {
                AnnotationEnum.GSON
            }
            else -> {
                AnnotationEnum.NONE
            }
        }
    }

    private fun resolveFieldDTOFormat(coreLayerEnum: CoreLayerEnum): String {
        return when (coreLayerEnum) {
            CoreLayerEnum.Remote -> {
                FIELD_KOTLIN_DTO
            }
            else -> {
                NON_NULL_FIELD_KOTLIN_DTO
            }
        }
    }

    private fun getSuffixName(coreLayerEnum: CoreLayerEnum): String {
        return when (coreLayerEnum) {
            CoreLayerEnum.DOMAIN -> {
                ""
            }
            CoreLayerEnum.Data -> {
                "Entity"
            }
            CoreLayerEnum.Remote -> {
                "Model"
            }
            CoreLayerEnum.Cache -> {
                ""
            }
            else -> {
                ""
            }
        }
    }

    private fun getPrefixName(coreLayerEnum: CoreLayerEnum): String {
        return when (coreLayerEnum) {
            CoreLayerEnum.DOMAIN -> {
                ""
            }
            CoreLayerEnum.Data -> {
                ""
            }
            CoreLayerEnum.Remote -> {
                ""
            }
            CoreLayerEnum.Cache -> {
                "Cached"
            }
            else -> {
                ""
            }
        }
    }

    private fun resolveCoreLayerItem(): CoreLayerEnum {
        val buttonGroup = generatorVew.typeButtonGroup
        val buttons = buttonGroup.elements
        while (buttons
                        .hasMoreElements()) {
            val button = buttons.nextElement()
            if (button.isSelected) {
                for (layerEnum in CoreLayerEnum.values()) {
                    if (layerEnum.text == button.text) {
                        return layerEnum
                    }
                }
            }
        }
        return CoreLayerEnum.DOMAIN
    }
}
