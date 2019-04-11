package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.MapperCreator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import java.util.LinkedHashMap

class MapperCreatorTest {

    @InjectMocks
    lateinit var mapperCreator: MapperCreator

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

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
}