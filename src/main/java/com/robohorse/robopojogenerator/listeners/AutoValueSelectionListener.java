package com.robohorse.robopojogenerator.listeners;

import com.robohorse.robopojogenerator.view.ui.CorePOJOGeneratorVew;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by vadim on 28.02.17.
 */
public class AutoValueSelectionListener implements ItemListener {
    private CorePOJOGeneratorVew generatorVew;

    public AutoValueSelectionListener(CorePOJOGeneratorVew generatorVew) {
        this.generatorVew = generatorVew;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        final boolean autoValueEnabled = itemEvent.getStateChange() == ItemEvent.SELECTED;
        enableCheckBoxes(generatorVew, autoValueEnabled);
    }

    private void enableCheckBoxes(CorePOJOGeneratorVew generatorVew, boolean autoValueEnabled) {
    }
}
