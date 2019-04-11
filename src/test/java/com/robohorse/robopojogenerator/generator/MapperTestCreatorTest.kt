package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.MapperCreator
import com.robohorse.robopojogenerator.generator.common.MapperTestCreator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.processing.ClassProcessor
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
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

    @InjectMocks
    lateinit var mapperTestCreator: MapperTestCreator

    @Test
    fun generateAssertions() {
        // GIVEN
        val classFields: MutableMap<String, ClassField> = LinkedHashMap()
        classFields["propA"] = ClassField(ClassEnum.STRING)
        classFields["propB"] = ClassField(ClassEnum.STRING)
        classFields["propC"] = ClassField(ClassEnum.STRING)
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
}