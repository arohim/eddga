package com.robohorse.robopojogenerator.controllers;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.robohorse.robopojogenerator.delegates.EnvironmentDelegate;
import com.robohorse.robopojogenerator.delegates.MessageDelegate;
import com.robohorse.robopojogenerator.delegates.POJOGenerationDelegate;
import com.robohorse.robopojogenerator.errors.RoboPluginException;
import com.robohorse.robopojogenerator.models.GenerationModel;
import com.robohorse.robopojogenerator.models.ProjectModel;
import com.robohorse.robopojogenerator.view.binders.CoreGeneratorViewBinder;
import com.robohorse.robopojogenerator.view.binders.CorePOJOGeneratorViewBinder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vadim on 22.10.16.
 */
public class GeneratePOJOActionControllerTest {

    @InjectMocks
    POJOGeneratorActionController generatePOJOActionController;
    @Mock
    EnvironmentDelegate environmentDelegate;
    @Mock
    MessageDelegate messageDelegate;
    @Mock
    CorePOJOGeneratorViewBinder corePOJOGeneratorViewBinder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onActionHandled() throws Exception {
        final ProjectModel projectModel = new ProjectModel
                .Builder()
                .build();
        AnActionEvent event = Mockito.mock(AnActionEvent.class);
        GenerationModel generationModel = new GenerationModel.Builder().build();

        when(environmentDelegate.obtainProjectModel(event)).thenReturn(projectModel);
        generatePOJOActionController.onActionHandled(event, generationModel);
        verify(corePOJOGeneratorViewBinder).bindView(any(), any(), any(), any());
    }

    @Test
    public void onActionHandled_withError() throws Exception {
        final RoboPluginException exception = new RoboPluginException("", "");
        AnActionEvent event = Mockito.mock(AnActionEvent.class);
        GenerationModel generationModel = new GenerationModel.Builder().build();

        when(environmentDelegate.obtainProjectModel(event)).thenThrow(exception);
        generatePOJOActionController.onActionHandled(event, generationModel);
        verify(messageDelegate).onPluginExceptionHandled(exception);
    }
}