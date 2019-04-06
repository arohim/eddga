package com.robohorse.robopojogenerator.generator.consts.annotations;

/**
 * Created by vadim on 25.09.16.
 */
public enum CoreLayerEnum {
    DOMAIN("Domain"),
    Data("Data"),
    Remote("Remote"),
    Cache("Cache");

    private String text;

    CoreLayerEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
