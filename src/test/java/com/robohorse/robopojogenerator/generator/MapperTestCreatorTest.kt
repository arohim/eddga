package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.MapperTestCreator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.processing.ClassProcessor
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.LinkedHashMap

@RunWith(MockitoJUnitRunner::class)
class MapperTestCreatorTest {

    @Mock
    lateinit var classGenerateHelper: ClassGenerateHelper

    @Mock
    lateinit var processor: ClassProcessor

    @Mock
    lateinit var roboPOJOGenerator: RoboPOJOGenerator

    @Mock
    lateinit var fileTemplateWriterDelegate: FileTemplateWriterDelegate

    @Mock
    lateinit var generateHelper: ClassGenerateHelper

    @InjectMocks
    lateinit var mapperTestCreator: MapperTestCreator

    @Test
    fun generateAssertions() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["propA"] = ClassField(ClassEnum.STRING)
        classFields["propB"] = ClassField(ClassEnum.STRING)
        classFields["propC"] = ClassField(ClassEnum.STRING)
        Mockito.`when`(generateHelper.formatClassField("propA")).thenReturn("propA")
        Mockito.`when`(generateHelper.formatClassField("propB")).thenReturn("propB")
        Mockito.`when`(generateHelper.formatClassField("propC")).thenReturn("propC")

        val from = "cached"
        val to = "entity"

        // WHEN
        val actual = mapperTestCreator.generateAssertions(classFields, from, to)

        // THEN
        val expected = "assertEquals(cached.propA, entity.propA)\n" +
                "assertEquals(cached.propB, entity.propB)\n" +
                "assertEquals(cached.propC, entity.propC)"
        assertEquals(expected, actual)
    }

    @Test
    fun generateProperties() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["ClassA"] = ClassField("ClassA")
        classFields["ClassB"] = ClassField("ClassB")
        classFields["propC"] = ClassField(ClassEnum.STRING)
        Mockito.`when`(generateHelper.formatClassField("ClassA")).thenReturn("classA")
        Mockito.`when`(generateHelper.formatClassField("ClassB")).thenReturn("classB")
        val suffix = "EntityMapper"

        // WHEN
        val actual = mapperTestCreator.generateProperties(classFields, suffix)

        // THEN
        val expected = "private lateinit var classAEntityMapper: ClassAEntityMapper\n\n" +
                "private lateinit var classBEntityMapper: ClassBEntityMapper\n\n"

        assertEquals(expected, actual)
    }

    @Test
    fun generatePropertyParameters() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["ClassA"] = ClassField("ClassA")
        classFields["ClassB"] = ClassField("ClassB")
        classFields["propC"] = ClassField(ClassEnum.STRING)
        Mockito.`when`(generateHelper.formatClassField("ClassA")).thenReturn("classA")
        Mockito.`when`(generateHelper.formatClassField("ClassB")).thenReturn("classB")
        val suffix = "EntityMapper"

        // WHEN
        val actual = mapperTestCreator.generatePropertyParameters(classFields, suffix)

        // THEN
        val expected = "classAEntityMapper, classBEntityMapper"

        assertEquals(expected, actual)
    }

    @Test
    fun generatePropertiesInitialization() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["ClassA"] = ClassField("ClassA")
        classFields["ClassB"] = ClassField("ClassB")
        classFields["propC"] = ClassField(ClassEnum.STRING)
        val classItem = ClassItem("ClassName")
        classFields.forEach { field ->
            classItem.addClassField(field.key, field.value)
        }

        Mockito.`when`(generateHelper.formatClassField("ClassA")).thenReturn("classA")
        Mockito.`when`(generateHelper.formatClassField("ClassB")).thenReturn("classB")
        val suffix = "EntityMapper"

        // WHEN
        val actual = mapperTestCreator.generatePropertiesInitialization(classItem, suffix)

        // THEN
        val expected = "classAEntityMapper = ClassAEntityMapper()\n" +
                "classBEntityMapper = ClassBEntityMapper()"

        assertEquals(expected, actual)
    }
}