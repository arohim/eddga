package com.robohorse.robopojogenerator.generator.processing

import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.JsonItem
import com.robohorse.robopojogenerator.generator.common.JsonItemArray
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ImportsTemplate
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import org.json.JSONArray
import org.json.JSONObject

import javax.inject.Inject
import java.util.HashMap

/**
 * Created by vadim on 23.09.16.
 */
open class ClassProcessor @Inject constructor() {

    @Inject
    lateinit var classGenerateHelper: ClassGenerateHelper

    fun proceed(jsonItem: JsonItem, itemMap: MutableMap<String, ClassItem>, prefix: String?, suffix: String?) {

        val formatClassName = prefix + classGenerateHelper.formatClassName(jsonItem.key) + suffix
        val classItem = ClassItem(formatClassName)

        for (jsonObjectKey in jsonItem.jsonObject.keySet()) {
            val jsonObject = jsonItem.jsonObject.get(jsonObjectKey)
            val innerObjectResolver = object : InnerObjectResolver() {

                override fun onInnerObjectIdentified(classType: ClassEnum) {
                    classItem.addClassField(jsonObjectKey, ClassField(classType))
                }

                override fun onJsonObjectIdentified() {
                    val fieldClassName = prefix + jsonObjectKey + suffix
                    val className = classGenerateHelper.formatClassName(fieldClassName)
                    val classField = ClassField(className)
                    val item = JsonItem(jsonObject as JSONObject, jsonObjectKey)

                    classItem.addClassField(jsonObjectKey, classField)
                    proceed(item, itemMap, prefix, suffix)
                }

                override fun onJsonArrayIdentified() {
                    val jsonArray = jsonObject as JSONArray
                    classItem.addClassImport(ImportsTemplate.LIST)

                    val classField = ClassField()
                    if (jsonArray.length() == 0) {
                        classField.setClassField(ClassField(ClassEnum.OBJECT))
                        classItem.addClassField(jsonObjectKey, classField)

                    } else {
                        val jsonItemArray = JsonItemArray(jsonObject, jsonObjectKey)
                        proceedArray(jsonItemArray, classField, itemMap, prefix, suffix)
                        classItem.addClassField(jsonObjectKey, classField)
                    }
                }
            }
            innerObjectResolver.resolveClassType(jsonObject)
        }
        appendItemsMap(itemMap, classItem)
    }

    private fun appendItemsMap(itemMap: MutableMap<String, ClassItem>, classItem: ClassItem) {
        val className = classItem.className
        if (itemMap.containsKey(className)) {
            val storedClassItem = itemMap[className]
            storedClassItem?.classFields?.let { classItem.classFields.putAll(it) }
        }
        itemMap[className] = classItem
    }

    private fun proceedArray(jsonItemArray: JsonItemArray,
                             classField: ClassField,
                             itemMap: MutableMap<String, ClassItem>, prefix: String?, suffix: String?) {
        val itemName = classGenerateHelper.getClassNameWithItemPostfix(jsonItemArray.key, prefix, suffix)
        if (jsonItemArray.jsonArray.length() != 0) {
            val `object` = jsonItemArray.jsonArray.get(0)
            val innerObjectResolver = object : InnerObjectResolver() {

                override fun onInnerObjectIdentified(classType: ClassEnum) {
                    classField.setClassField(ClassField(classType))
                }

                override fun onJsonObjectIdentified() {
                    val size = jsonItemArray.jsonArray.length()
                    val innerItemsMap = HashMap<String, ClassItem>()
                    for (index in 0 until size) {
                        val jsonObject = jsonItemArray.jsonArray.get(index) as JSONObject
                        val jsonItem = JsonItem(jsonObject, itemName)
                        proceed(jsonItem, innerItemsMap, "", "")
                    }
                    classField.setClassField(ClassField(itemName))
                    itemMap.putAll(innerItemsMap)
                }

                override fun onJsonArrayIdentified() {
                    classField.setClassField(ClassField())
                    val jsonItemArray = JsonItemArray(`object` as JSONArray, itemName)
                    proceedArray(jsonItemArray, classField, itemMap, prefix, suffix)
                }
            }
            innerObjectResolver.resolveClassType(`object`)

        } else {
            classField.setClassField(ClassField(ClassEnum.OBJECT))
        }
    }
}
