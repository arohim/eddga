package com.robohorse.robopojogenerator.generator

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.whenever
import com.robohorse.robopojogenerator.generator.common.JsonItem
import com.robohorse.robopojogenerator.generator.processing.ClassProcessor
import com.robohorse.robopojogenerator.generator.utils.ClassGenerateHelper
import com.robohorse.robopojogenerator.models.GenerationModel
import junit.framework.Assert.assertNotNull
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by vadim on 22.10.16.
 */
class RoboPOJOGeneratorTest {

    @InjectMocks
    lateinit var roboPOJOGenerator: RoboPOJOGenerator

    @Mock
    lateinit var classProcessor: ClassProcessor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun generate() {
        val json = JSONObject().toString()
        val generationModel = GenerationModel.Builder()
                .setContent(json)
                .setRootClassName("Response")
                .build()

        val jsonItem = JsonItem(JSONObject(), "Response")
        whenever(classProcessor.proceed(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())).then { }
//        `when`(classGenerateHelper.formatClassName(anyString())).thenReturn("name")

        //        when(classProcessor.proceed(ArgumentMatchers.anyObject(), ArgumentMatchers.anyObject(),
        //                ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null);
        val classItemSet = roboPOJOGenerator.generate(generationModel)
        assertNotNull(classItemSet)
    }
}