package com.robohorse.robopojogenerator.models

data class MapperTestGeneratorModel(
        val from: String,
        val to: String,
        val fileNameSuffix: String,
        val templateName: String,
        val classNameSuffix: String,
        val isNullable: Boolean
)