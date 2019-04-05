package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.listeners.*
import com.robohorse.robopojogenerator.view.ui.CoreGeneratorVew

import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
class CoreGeneratorViewBinder @Inject
constructor() {

    fun bindView(builder: DialogBuilder, eventListener: GuiFormEventListener) {
        val generatorVew = CoreGeneratorVew()

        val actionListener = CoreGenerateActionListener(generatorVew, eventListener)
        generatorVew.generateButton?.addActionListener(actionListener)

        generatorVew.domainPath?.let {
            it.addMouseListener(object : MouseListenerClicked(it) {})
        }

        generatorVew.cachePath?.let {
            it.addMouseListener(object : MouseListenerClicked(it) {})
        }

        generatorVew.dataPath?.let {
            it.addMouseListener(object : MouseListenerClicked(it) {})
        }

        generatorVew.roguePath?.let {
            it.addMouseListener(object : MouseListenerClicked(it) {})
        }

        builder.setCenterPanel(generatorVew.rootView)
        builder.setTitle("Core Generator")
        builder.removeAllActions()
        builder.show()

    }
}
