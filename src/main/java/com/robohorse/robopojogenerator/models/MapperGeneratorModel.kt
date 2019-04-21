package com.robohorse.robopojogenerator.models

data class MapperGeneratorModel(
        val fileNameSuffix: String,
        val templateName: String,
        val mapToMethodName: String,
        val mapFromMethodName: String
)