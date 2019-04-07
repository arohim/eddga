package com.robohorse.robopojogenerator.view.binders;

import com.intellij.openapi.ui.DialogBuilder;
import com.robohorse.robopojogenerator.generator.consts.annotations.CoreLayerEnum;
import com.robohorse.robopojogenerator.listeners.GenerateActionListener;
import com.robohorse.robopojogenerator.listeners.GuiFormEventListener;
import com.robohorse.robopojogenerator.view.ui.POJOGeneratorVew;

import javax.inject.Inject;
import javax.swing.*;
import java.util.Enumeration;

/**
 * Created by vadim on 24.09.16.
 */
public class GeneratorViewBinder {
    @Inject
    public GeneratorViewBinder() {
    }

    public void bindView(DialogBuilder builder, GuiFormEventListener eventListener) {
        final POJOGeneratorVew generatorVew = new POJOGeneratorVew();
        final GenerateActionListener actionListener = new GenerateActionListener(generatorVew, eventListener);
        generatorVew.getGenerateButton().addActionListener(actionListener);

        bindGroupViews(generatorVew.getTypeButtonGroup(), generatorVew);

        builder.setCenterPanel(generatorVew.getRootView());
        builder.setTitle("RoboPOJOGenerator");
        builder.removeAllActions();
        builder.show();
    }

    private void bindGroupViews(ButtonGroup buttonGroup, POJOGeneratorVew generatorVew) {
        final Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        for (CoreLayerEnum annotationItems : CoreLayerEnum.values()) {
            if (buttons.hasMoreElements()) {
                final AbstractButton button = buttons.nextElement();
                button.setText(annotationItems.getText());
            } else {
                break;
            }
        }
    }
}
