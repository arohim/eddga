package com.robohorse.robopojogenerator.listeners;

import com.robohorse.robopojogenerator.view.ui.POJOGeneratorVew;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by vadim on 28.02.17.
 */
public class AutoValueSelectionListener implements ItemListener {
    private POJOGeneratorVew generatorVew;

    public AutoValueSelectionListener(POJOGeneratorVew generatorVew) {
        this.generatorVew = generatorVew;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        final boolean autoValueEnabled = itemEvent.getStateChange() == ItemEvent.SELECTED;
        enableCheckBoxes(generatorVew, autoValueEnabled);
    }

    private void enableCheckBoxes(POJOGeneratorVew generatorVew, boolean autoValueEnabled) {
    }
}
