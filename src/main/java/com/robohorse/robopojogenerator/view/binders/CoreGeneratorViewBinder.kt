package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.listeners.*
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew
import com.robohorse.robopojogenerator.view.ui.GeneratorVew

import javax.inject.Inject
import javax.swing.*
import java.util.Enumeration

/**
 * Created by vadim on 24.09.16.
 */
class CoreGeneratorViewBinder @Inject
constructor() {

    fun bindView(builder: DialogBuilder, eventListener: GuiFormEventListener) {
        val generatorVew = CoreGeneratorVew()
        val actionListener = CoreGenerateActionListener(generatorVew, eventListener)
        generatorVew.generateButton?.addActionListener(actionListener)

        builder.setCenterPanel(generatorVew.rootView)
        builder.setTitle("Core Generator")
        builder.removeAllActions()
        builder.show()
    }
}
