package com.robohorse.robopojogenerator.listeners

import com.robohorse.robopojogenerator.models.CoreGeneratorModel

interface CoreGeneratorFormEventListener {

    fun onJsonDataObtained(coreGeneratorModel: CoreGeneratorModel)
    
}
