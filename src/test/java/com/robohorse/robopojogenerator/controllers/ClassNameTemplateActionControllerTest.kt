package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.whenever
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate
import com.robohorse.robopojogenerator.delegates.FileTemplateWriterDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.models.ClassNameTemplateModel
import com.robohorse.robopojogenerator.models.ProjectModel
import com.robohorse.robopojogenerator.view.binders.ClassNameTemplateGeneratorViewBinder
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ClassNameTemplateActionControllerTest {

    @InjectMocks
    lateinit var classNameTemplateActionController: ClassNameTemplateActionController

    @Mock
    lateinit var messageDelegate: MessageDelegate
    @Mock
    lateinit var environmentDelegate: EnvironmentDelegate
    @Mock
    lateinit var viewBinder: ClassNameTemplateGeneratorViewBinder

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onActionHandled() {
        // GIVEN
        val projectModel = ProjectModel.Builder().build()
        val event = Mockito.mock(AnActionEvent::class.java)
        val classNameTemplateModel = ClassNameTemplateModel("dialogTitle", "templateName", "")
        whenever(environmentDelegate.obtainProjectModel(any())).thenReturn(projectModel)

        // WHEN
        classNameTemplateActionController.onActionHandled(event, classNameTemplateModel)

        // THEN
        verify(viewBinder).bindView(any(), any(), anyOrNull())
    }

    @Test
    @Throws(Exception::class)
    fun onActionHandled_withError() {
        val exception = RoboPluginException("", "")
        val event = Mockito.mock(AnActionEvent::class.java)
        val classNameTemplateModel = ClassNameTemplateModel("dialogTitle", "templateName", "")

        whenever(environmentDelegate.obtainProjectModel(event)).thenThrow(exception)
        classNameTemplateActionController.onActionHandled(event, classNameTemplateModel)
        verify(messageDelegate).onPluginExceptionHandled(exception)
    }
}