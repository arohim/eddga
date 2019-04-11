package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.MapperGeneratorDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.delegates.POJOGenerationDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import com.robohorse.robopojogenerator.view.binders.CorePOJOGeneratorViewBinder
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import javax.inject.Inject

class MapperGeneratorActionControllerTest {

    @InjectMocks
    lateinit var controller: MapperGeneratorActionController

    @Mock
    lateinit var environmentDelegate: EnvironmentDelegate

    @Mock
    lateinit var messageDelegate: MessageDelegate

    @Mock
    lateinit var coreGeneratorViewBinder: CorePOJOGeneratorViewBinder

    @Mock
    lateinit var generationDelegate: MapperGeneratorDelegate

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun onActionHandled() {
        val projectModel = ProjectModel.Builder().build()
        val event = Mockito.mock(AnActionEvent::class.java)
        val mapperGeneratorModel = MapperGeneratorModel(fileNameSuffix = "templateName", templateName = "templateName")

        `when`(environmentDelegate.obtainProjectModel(event)).thenReturn(projectModel)
        controller.onActionHandled(event, mapperGeneratorModel)
        verify(coreGeneratorViewBinder).bindView(any(), any(), any(), any())
    }

    @Test
    @Throws(Exception::class)
    fun onActionHandled_withError() {
        val exception = RoboPluginException("", "")
        val event = Mockito.mock(AnActionEvent::class.java)
        val mapperGeneratorModel = MapperGeneratorModel(fileNameSuffix = "templateName", templateName = "templateName")

        `when`(environmentDelegate.obtainProjectModel(event)).thenThrow(exception)
        controller.onActionHandled(event, mapperGeneratorModel)
        verify(messageDelegate).onPluginExceptionHandled(exception)
    }
}