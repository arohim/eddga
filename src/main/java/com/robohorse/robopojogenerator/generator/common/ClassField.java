package com.robohorse.robopojogenerator.generator.common;

import com.robohorse.robopojogenerator.generator.consts.ClassEnum;

/**
 * Created by vadim on 29.10.16.
 */
public class ClassField {
    private ClassEnum classEnum;
    private String className;
    private ClassField classField;
    private String listFormat;

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
        return isListField() ? wrapListJava() :
                (null != className ? className : classEnum.getPrimitive());
    }

    public String getKotlinItem() {
        return isListField() ? wrapListKotlin() :
                (null != className ? className : classEnum.getKotlin());
    }

    private String wrapListJava() {
        return String.format(listFormat, classField.getJavaBoxed());
    }

    private String wrapListKotlin() {
        return String.format(listFormat, classField.getKotlinItem());
    }

    private String getJavaBoxed() {
        return isListField() ? wrapListJava() :
                (null != className ? className : classEnum.getBoxed());
    }

    boolean isListField() {
        return null != classField;
    }

    public ClassEnum getClassEnum() {
        return classEnum;
    }

    public String getClassName() {
        return className;
    }

    public ClassField getClassField() {
        return classField;
    }
}
