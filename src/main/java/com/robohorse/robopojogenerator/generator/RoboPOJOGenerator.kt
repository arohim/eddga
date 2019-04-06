package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.JsonItem
import com.robohorse.robopojogenerator.generator.processing.ClassProcessor
import com.robohorse.robopojogenerator.models.GenerationModel
import org.json.JSONObject

import javax.inject.Inject
import java.util.HashMap
import java.util.HashSet

/**
 * Created by vadim on 22.09.16.
 */
open class RoboPOJOGenerator @Inject constructor() {

    @Inject
    lateinit var processor: ClassProcessor

    fun generate(model: GenerationModel): Set<ClassItem> {
        val jsonObject = JSONObject(model.content)
        val map = HashMap<String, ClassItem>()
        val jsonItem = JsonItem(jsonObject, model.rootClassName)
        processor.proceed(jsonItem, map, model.prefix, model.suffix)
        return HashSet(map.values)
    }

}
