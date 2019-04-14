package com.robohorse.robopojogenerator.models

data class CoreGeneratorModel(
        var cachePath: String? = null,
        var dataPath: String? = null,
        var domainPath: String? = null,
        var roguePath: String? = null,
        var content: String? = null,
        var rootClassName: String? = null
)