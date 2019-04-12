package com.robohorse.robopojogenerator.models

data class FactoryGeneratorModel(
        val fileNameSuffix: String,
        val templateName: String,
        val remote: Boolean = false,
        val cache: Boolean = false,
        val data: Boolean = false,
        val domain: Boolean = false
)