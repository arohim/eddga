package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.MapperCreator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.processing.ClassProcessor
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.LinkedHashMap
import javax.inject.Inject
import kotlin.collections.HashSet
import kotlin.collections.MutableMap
import kotlin.collections.set

@RunWith(MockitoJUnitRunner::class)
class MapperCreatorTest {

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
    lateinit var mapperCreator: MapperCreator

    @Test
    fun `Generate mapping field string`() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["propA"] = ClassField(ClassEnum.STRING)
        classFields["propB"] = ClassField(ClassEnum.STRING)
        classFields["propC"] = ClassField(ClassEnum.STRING)

        // WHEN
        val actual = mapperCreator.generateMappingFieldString(classFields)

        // THEN
        val expected = "propA = type.propA,\n" +
                "propB = type.propB,\n" +
                "propC = type.propC"
        assertEquals(expected, actual)
    }

    @Test
    fun `Generate file`() {
        // GIVEN
        val generationModel = GenerationModel.Builder().build()
        val projectModel = ProjectModel.Builder().build()
        val mapperGeneratorModel = MapperGeneratorModel(fileNameSuffix = "", templateName = "")
        val classItems = HashSet<ClassItem>()
        `when`(roboPOJOGenerator.generate(generationModel)).thenReturn(classItems)

        // WHEN
        mapperCreator.generateFiles(generationModel, projectModel, mapperGeneratorModel)

        // THEN

    }

    @Test
    fun `Generate Injectors`() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["class_a"] = ClassField("ClassA")
        classFields["class_b"] = ClassField("ClassB")
        classFields["class_c"] = ClassField("ClassC")
        classFields["propB"] = ClassField(ClassEnum.STRING)
        classFields["propC"] = ClassField(ClassEnum.STRING)
        val suffix = "EntityMapper"
        `when`(generateHelper.formatClassName("class_a$suffix")).thenReturn("ClassAEntityMapper")
        `when`(generateHelper.formatClassField("class_a$suffix")).thenReturn("classAEntityMapper")
        `when`(generateHelper.formatClassName("class_b$suffix")).thenReturn("ClassBEntityMapper")
        `when`(generateHelper.formatClassField("class_b$suffix")).thenReturn("classBEntityMapper")
        `when`(generateHelper.formatClassName("class_c$suffix")).thenReturn("ClassCEntityMapper")
        `when`(generateHelper.formatClassField("class_c$suffix")).thenReturn("classCEntityMapper")

        // WHEN
        val actual = mapperCreator.generateInjectors(classFields, suffix)

        // THEN
        val expected = "private val classAEntityMapper: ClassAEntityMapper,\n" +
                "private val classBEntityMapper: ClassBEntityMapper,\n" +
                "private val classCEntityMapper: ClassCEntityMapper"
        assertEquals(expected, actual)
    }
}