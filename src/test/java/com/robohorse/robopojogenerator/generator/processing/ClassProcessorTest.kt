package com.robohorse.robopojogenerator.generator.processing

import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.JsonItem
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.GenerationModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import testutils.JsonReader
import java.util.*

/**
 * Created by vadim on 02.10.16.
 */

class ClassProcessorTest {

    private val jsonReader = JsonReader()

    @InjectMocks
    lateinit var classProcessor: ClassProcessor

    @Mock
    lateinit var classGenerateHelper: ClassGenerateHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun testSingleObjectGeneration_isCorrect() {
        val jsonObject = jsonReader.read("single_object.json")
        val name = "Response"
        val prefix = "Prefix"
        val suffix = "Suffix"
        val generationModel = GenerationModel.Builder()
                .setContent("")
                .setRootClassName(name)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .build()

        `when`(classGenerateHelper.formatClassName(name)).thenReturn(name)

        val classItemMap = HashMap<String, ClassItem>()
        val jsonItem = JsonItem(jsonObject, name)
        classProcessor.proceed(generationModel, jsonItem, classItemMap, prefix, suffix)
        assertTrue(classItemMap.size == 1)

        val iterator = classItemMap.values.iterator()
        val classItem = iterator.next()
        val className = classItem.className
        val actualName = prefix + name + suffix
        assertEquals(className, actualName)

        val fields = classItem.classFields
        assertNotNull(fields)

        for (key in jsonObject.keySet()) {
            assertTrue(fields.containsKey(key))
        }
    }

    @Test
    @Throws(Exception::class)
    fun testInnerObjectGeneration_isCorrect() {
        // GIVEN
        val jsonObject = jsonReader.read("inner_json_object.json")
        val innerJsonObject = jsonObject.optJSONObject("data")
        val name = "Response"
        val generationModel = GenerationModel.Builder()
                .setContent("")
                .setRootClassName(name)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .build()

        `when`(classGenerateHelper.formatClassName(name)).thenReturn(name)
        `when`(classGenerateHelper.formatClassName("data")).thenReturn("Data")

        val classItemMap = HashMap<String, ClassItem>()
        val jsonItem = JsonItem(jsonObject, name)

        // WHEN
        classProcessor.proceed(generationModel, jsonItem, classItemMap, "", "")

        // THEN
        assertTrue(classItemMap.size == 2)
        assertTrue(classItemMap.containsKey("Response"))
        assertTrue(classItemMap.containsKey("Data"))
        for (classItem in classItemMap.values) {
            val fields = classItem.classFields
            assertNotNull(fields)

            if (name.equals(classItem.className, ignoreCase = true)) {
                for (key in jsonObject.keySet()) {
                    assertTrue(fields.containsKey(key))
                }
            } else {
                for (key in innerJsonObject.keySet()) {
                    assertTrue(fields.containsKey(key))
                }
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyArrayGeneration_isCorrect() {
        val jsonObject = jsonReader.read("empty_array.json")
        val name = "Response"
        val generationModel = GenerationModel.Builder()
                .setContent("")
                .setRootClassName(name)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .build()

        `when`(classGenerateHelper.formatClassName(name)).thenReturn(name)
        val classItemMap = HashMap<String, ClassItem>()
        val jsonItem = JsonItem(jsonObject, name)

        classProcessor.proceed(generationModel, jsonItem, classItemMap, "", "")
        assertTrue(classItemMap.size == 1)

        val iterator = classItemMap.values.iterator()
        val classItem = iterator.next()
        assertEquals(classItem.className, name)

        val fields = classItem.classFields
        assertNotNull(fields)

        for (key in jsonObject.keySet()) {
            assertTrue(fields.containsKey(key))
        }

        val targetObjectType = classItem.classFields["data"]

        assertEquals("List<Object>", targetObjectType?.javaItem)
    }

    @Test
    @Throws(Exception::class)
    fun testIntegerArrayGeneration_isCorrect() {
        val jsonObject = jsonReader.read("array_with_primitive.json")
        val name = "Response"
        val targetType = "List<Integer>"
        val generationModel = GenerationModel.Builder()
                .setContent("")
                .setRootClassName(name)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .build()

        `when`(classGenerateHelper.formatClassName(name)).thenReturn(name)

        val classItemMap = HashMap<String, ClassItem>()
        val jsonItem = JsonItem(jsonObject, name)
        classProcessor.proceed(generationModel, jsonItem, classItemMap, "", "")

        assertTrue(classItemMap.size == 1)

        val iterator = classItemMap.values.iterator()
        val classItem = iterator.next()
        assertEquals(classItem.className, name)

        val fields = classItem.classFields
        assertNotNull(fields)

        for (key in jsonObject.keySet()) {
            assertTrue(fields.containsKey(key))
        }

        val actualType = classItem.classFields["data"]

        assertEquals(targetType, actualType?.javaItem)
    }

    @Test
    @Throws(Exception::class)
    fun testInnerArrayObjectGeneration_isCorrect() {
        val jsonObject = jsonReader.read("array_with_jsonobject.json")
        val innerJsonArray = jsonObject.optJSONArray("data")
        val innerJsonObject = innerJsonArray.getJSONObject(0)
        val name = "Response"
        val targetType = "List<DataItem>"
        val generationModel = GenerationModel.Builder()
                .setContent("")
                .setRootClassName(name)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .build()

        `when`(classGenerateHelper.formatClassName(name))
                .thenReturn(name)
        `when`(classGenerateHelper.getClassNameWithItemPostfix(Mockito.anyString())).thenReturn("DataItem")

        val classItemMap = HashMap<String, ClassItem>()
        val jsonItem = JsonItem(jsonObject, name)

        classProcessor.proceed(generationModel, jsonItem, classItemMap, "", "")

        assertTrue(classItemMap.size == 2)

        for (classItem in classItemMap.values) {
            val fields = classItem.classFields
            assertNotNull(fields)

            if (name.equals(classItem.className, ignoreCase = true)) {
                for (key in jsonObject.keySet()) {
                    assertTrue(fields.containsKey(key))
                    val actualType = classItem.classFields["data"]
                    val javaItem = actualType?.javaItem
                    assertEquals(targetType, javaItem)
                }
            } else {
                for (key in innerJsonObject.keySet()) {
                    assertTrue(fields.containsKey(key))
                }
            }
        }
    }
}
