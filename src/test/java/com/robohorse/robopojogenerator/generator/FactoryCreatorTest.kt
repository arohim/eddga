package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.FactoryCreator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FactoryCreatorTest {

    @InjectMocks
    lateinit var factory: FactoryCreator

    @Test
    fun generateMethods() {
        // GIVEN
        val className = "ClassName"
        val classItem = ClassItem(className)
        classItem.addClassField("value", ClassField(ClassEnum.INTEGER))
        classItem.addClassField("type", ClassField(ClassEnum.STRING))
        classItem.addClassField("available", ClassField(ClassEnum.BOOLEAN))
        classItem.addClassField("cost", ClassField(ClassEnum.DOUBLE))

        val classItems = mutableListOf(classItem)

        // WHEN
        val actual = factory.generateMethods(classItems)

        // THEN
        val expected = "fun makeClassNameModel(): ClassNameModel {\n" +
                "\treturn ClassName(\n" +
                "\t\tvalue = randomInt(),\n" +
                "\t\ttype = randomUuid(),\n" +
                "\t\tavailable = randomBoolean(),\n" +
                "\t\tcost = randomLong()\n" +
                "\t)\n" +
                "}\n\n"

        assertEquals(expected, actual)
    }
}