package com.robohorse.robopojogenerator.view.binders

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.robohorse.robopojogenerator.components.ProjectConfigurationComponent
import com.robohorse.robopojogenerator.listeners.*
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.view.ui.POJOGeneratorVew

import javax.inject.Inject

/**
 * Created by vadim on 24.09.16.
 */
open class CorePOJOGeneratorViewBinder @Inject constructor() {

    fun bindView(builder: DialogBuilder, event: AnActionEvent, generationModel: GenerationModel, eventListener: GuiFormEventListener) {
        val generatorVew = POJOGeneratorVew()
        val basePath = event.project?.basePath

        val actionListener = POJOGenerateActionListener(generatorVew, event, generationModel, eventListener)
        generatorVew.generateButton.addActionListener(actionListener)

        event.project?.let {
            val component = ProjectConfigurationComponent.getInstance(it)
        }

        builder.setCenterPanel(generatorVew.rootView)
        builder.setTitle(generationModel.dialogTitle)
        builder.removeAllActions()
        builder.show()
    }
}
