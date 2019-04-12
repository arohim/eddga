package com.robohorse.robopojogenerator.generator

import com.robohorse.robopojogenerator.generator.common.ClassField
import com.robohorse.robopojogenerator.generator.common.ClassItem
import com.robohorse.robopojogenerator.generator.common.FactoryCreator
import com.robohorse.robopojogenerator.generator.consts.ClassEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM
import com.robohorse.robopojogenerator.generator.consts.templates.ImportsTemplate
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
    fun `generate non list methods`() {
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
                "\treturn ClassNameModel(\n" +
                "\t\tvalue = randomInt(),\n" +
                "\t\ttype = randomUuid(),\n" +
                "\t\tavailable = randomBoolean(),\n" +
                "\t\tcost = randomLong()\n" +
                "\t)\n" +
                "}\n\n"

        assertEquals(expected, actual)
    }

    @Test
    fun `generate list methods`() {
        // GIVEN
        val dataItemClass = ClassItem("DataItem")
        dataItemClass.addClassField("value", ClassField(ClassEnum.INTEGER).also { it.listFormat = NON_NULL_LIST_OF_ITEM })

        val className = "ClassName"
        val classItem = ClassItem(className)
        classItem.addClassImport(ImportsTemplate.LIST)

        val dataItemField = ClassField("DataItem")
        dataItemField.setClassField(ClassField("Data").also { it.listFormat = NON_NULL_LIST_OF_ITEM })
        dataItemField.listFormat = NON_NULL_LIST_OF_ITEM
        classItem.addClassField("data", dataItemField)

        val classItems = mutableListOf(classItem, dataItemClass)

        // WHEN
        val actual = factory.generateMethods(classItems)

        // THEN
        val expected = "fun makeClassNameModel(repeat: Int): ClassNameModel {\n" +
                "\treturn ClassNameModel(\n" +
                "\t\tdata = makeDataItemModels(repeat)\n" +
                "\t)\n" +
                "}\n" +
                "\n" +
                "private fun makeDataItemModels(repeat: Int): List<DataItemModel> {\n" +
                "\tval contents = mutableListOf<DataItemModel>()\n" +
                "\tkotlin.repeat(repeat) {\n" +
                "\t\tcontents.add(makeDataItemModel())\n" +
                "\t}\n" +
                "\treturn contents\n" +
                "}\n" +
                "\n" +
                "private fun makeDataItemModel(): DataItemModel {\n" +
                "\treturn DataItemModel(\n" +
                "\t\tvalue = randomInt()\n" +
                "\t)\n" +
                "}\n\n"

        assertEquals(expected, actual)
    }

    @Test
    fun `mixed non and list methods`() {
        // GIVEN
        val dataItemClass1 = ClassItem("DataItem")
        dataItemClass1.addClassField("value", ClassField(ClassEnum.INTEGER).also { it.listFormat = NON_NULL_LIST_OF_ITEM })
        val dataItemClass2 = ClassItem("DataItem2")
        dataItemClass2.addClassField("value", ClassField(ClassEnum.INTEGER).also { it.listFormat = NON_NULL_LIST_OF_ITEM })

        val className = "ClassName"
        val classItem = ClassItem(className)
        classItem.addClassImport(ImportsTemplate.LIST)

        val dataItemField = ClassField("DataItem")
        dataItemField.setClassField(ClassField("Data").also { it.listFormat = NON_NULL_LIST_OF_ITEM })
        dataItemField.listFormat = NON_NULL_LIST_OF_ITEM

        val dataItemField2 = ClassField("DataItem2")
        dataItemField2.listFormat = NON_NULL_LIST_OF_ITEM

        classItem.addClassField("data", dataItemField)
        classItem.addClassField("data2", dataItemField2)

        val classItems = mutableListOf(classItem, dataItemClass1, dataItemClass2)

        // WHEN
        val actual = factory.generateMethods(classItems)

        // THEN
        val expected = "fun makeClassNameModel(repeat: Int): ClassNameModel {\n" +
                "\treturn ClassNameModel(\n" +
                "\t\tdata = makeDataItemModels(repeat)\n" +
                "\t)\n" +
                "}\n" +
                "\n" +
                "private fun makeDataItemModels(repeat: Int): List<DataItemModel> {\n" +
                "\tval contents = mutableListOf<DataItemModel>()\n" +
                "\tkotlin.repeat(repeat) {\n" +
                "\t\tcontents.add(makeDataItemModel())\n" +
                "\t}\n" +
                "\treturn contents\n" +
                "}\n" +
                "\n" +
                "private fun makeDataItemModel(): DataItemModel {\n" +
                "\treturn DataItemModel(\n" +
                "\t\tvalue = randomInt()\n" +
                "\t)\n" +
                "}\n\n"

        assertEquals(expected, actual)
    }
}