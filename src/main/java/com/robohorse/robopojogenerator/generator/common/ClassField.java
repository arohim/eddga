package com.robohorse.robopojogenerator.generator.common;

import com.robohorse.robopojogenerator.generator.consts.ClassEnum;

import static com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM;

/**
 * Created by vadim on 29.10.16.
 */
public class ClassField {
    private ClassEnum classEnum;
    private String className;
    private ClassField classField;
    private String listFormat = NON_NULL_LIST_OF_ITEM;

    public ClassField() {
    }

    public ClassField(ClassEnum classEnum) {
        this.classEnum = classEnum;
    }

    public ClassField(String className) {
        this.className = className;
    }

    public void setClassField(ClassField decorator) {
        if (classField == null) {
            classField = decorator;

        } else {
            classField.setClassField(decorator);
        }
    }

    public String getListFormat() {
        return listFormat;
    }

    public void setListFormat(String listFormat) {
        this.listFormat = listFormat;
    }

    public String getJavaItem() {
        return null != classField ? wrapListJava() :
                (null != className ? className : classEnum.getPrimitive());
    }

    public String getKotlinItem() {
        return null != classField ? wrapListKotlin() :
                (null != className ? className : classEnum.getKotlin());
    }

    private String wrapListJava() {
        return String.format(listFormat, classField.getJavaBoxed());
    }

    private String wrapListKotlin() {
        return String.format(listFormat, classField.getKotlinItem());
    }

    private String getJavaBoxed() {
        return null != classField ? wrapListJava() :
                (null != className ? className : classEnum.getBoxed());
    }
}
