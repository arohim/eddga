package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.MapperTestCreator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.processing.ClassProcessor
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.MapperTestGeneratorModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.internal.Classes
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
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
        classFields["ClassD"] = ClassField("ClassD")
        `when`(generateHelper.formatClassField("propA")).thenReturn("propA")
        `when`(generateHelper.formatClassField("propB")).thenReturn("propB")
        `when`(generateHelper.formatClassField("propC")).thenReturn("propC")
        `when`(generateHelper.formatClassField("ClassD")).thenReturn("classD")

        val from = "cached"
        val to = "entity"
        val mapperTestGeneratorModel = MapperTestGeneratorModel(
                from = from,
                to = to,
                fileNameSuffix = "EntityMapperTest",
                templateName = "FromRemoteMapperTest",
                classNameSuffix = "EntityMapper",
                isNullable = false
        )

        // WHEN
        val actual = mapperTestCreator.generateAssertions(classFields, mapperTestGeneratorModel)

        // THEN
        val expected = "assertEquals(cached.propA, entity.propA)\n" +
                "assertEquals(cached.propB, entity.propB)\n" +
                "assertEquals(cached.propC, entity.propC)\n" +
                "assertNotNull(cached.classD)\n" +
                "assertNotNull(entity.classD)"
        assertEquals(expected, actual)
    }

    @Test
    fun generateNullAbleAssertions() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["propA"] = ClassField(ClassEnum.STRING)
        classFields["propB"] = ClassField(ClassEnum.STRING)
        classFields["propC"] = ClassField(ClassEnum.STRING)
        classFields["ClassD"] = ClassField("ClassD")
        `when`(generateHelper.formatClassField("propA")).thenReturn("propA")
        `when`(generateHelper.formatClassField("propB")).thenReturn("propB")
        `when`(generateHelper.formatClassField("propC")).thenReturn("propC")
        `when`(generateHelper.formatClassField("ClassD")).thenReturn("classD")

        val from = "cached"
        val to = "entity"
        val mapperTestGeneratorModel = MapperTestGeneratorModel(
                from = from,
                to = to,
                fileNameSuffix = "EntityMapperTest",
                templateName = "FromRemoteMapperTest",
                classNameSuffix = "EntityMapper",
                isNullable = true
        )

        // WHEN
        val actual = mapperTestCreator.generateAssertions(classFields, mapperTestGeneratorModel)

        // THEN
        val expected = "assertEquals(cached?.propA, entity.propA)\n" +
                "assertEquals(cached?.propB, entity.propB)\n" +
                "assertEquals(cached?.propC, entity.propC)\n" +
                "assertNotNull(cached.classD)\n" +
                "assertNotNull(entity.classD)"
        assertEquals(expected, actual)
    }

    @Test
    fun generateProperties() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["ClassA"] = ClassField("ClassA")
        classFields["ClassB"] = ClassField("ClassB")
        classFields["propC"] = ClassField(ClassEnum.STRING)
        `when`(generateHelper.formatClassField("ClassA")).thenReturn("classA")
        `when`(generateHelper.formatClassField("ClassB")).thenReturn("classB")
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
        classFields["propD"] = ClassField()
        classFields["propD"]?.classField = ClassField(ClassEnum.OBJECT)
        `when`(generateHelper.formatClassField("ClassA")).thenReturn("classA")
        `when`(generateHelper.formatClassField("ClassB")).thenReturn("classB")
        `when`(generateHelper.formatClassField("propD")).thenReturn("propD")
        val suffix = "EntityMapper"

        // WHEN
        val actual = mapperTestCreator.generatePropertyParameters(classFields, suffix)

        // THEN
        val expected = "classAEntityMapper, classBEntityMapper, propDEntityMapper"

        assertEquals(expected, actual)
    }

    @Test
    fun generatePropertiesInitialization() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["ClassA"] = ClassField("ClassA")
        classFields["ClassB"] = ClassField("ClassB")
        classFields["propC"] = ClassField(ClassEnum.STRING)
        classFields["propD"] = ClassField()
        classFields["propD"]?.classField = ClassField(ClassEnum.OBJECT)
        val classItem = ClassItem("ClassName")
        classFields.forEach { field ->
            classItem.addClassField(field.key, field.value)
        }

        `when`(generateHelper.formatClassField("ClassA")).thenReturn("classA")
        `when`(generateHelper.formatClassField("ClassB")).thenReturn("classB")
        `when`(generateHelper.formatClassField("propD")).thenReturn("propD")
        `when`(generateHelper.formatClassName("propD")).thenReturn("PropD")
        val suffix = "EntityMapper"

        // WHEN
        val actual = mapperTestCreator.generatePropertiesInitialization(classItem, suffix)

        // THEN
        val expected = "classAEntityMapper = ClassAEntityMapper()\n" +
                "classBEntityMapper = ClassBEntityMapper()\n" +
                "propDEntityMapper = PropDEntityMapper()"

        assertEquals(expected, actual)
    }
}