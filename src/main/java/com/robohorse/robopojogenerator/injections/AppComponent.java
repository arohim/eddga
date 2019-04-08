package com.robohorse.robopojogenerator.injections;

import com.robohorse.robopojogenerator.actions.*;
import com.robohorse.robopojogenerator.controllers.NewMapperActionController;
import com.robohorse.robopojogenerator.generator.postprocessing.common.AutoValueClassPostProcessor;
import com.robohorse.robopojogenerator.generator.postprocessing.common.CommonJavaPostProcessor;
import com.robohorse.robopojogenerator.generator.postprocessing.common.KotlinDataClassPostProcessor;
import com.robohorse.robopojogenerator.listeners.POJOGenerateActionListener;
import com.robohorse.robopojogenerator.listeners.GenerateActionListener;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by vadim on 28.09.16.
 */
@Singleton
@Component(modules = {
        AppModule.class
})
public interface AppComponent {

    void inject(CoreGeneratorAction item);

    void inject(GenerateActionListener item);

    void inject(POJOGenerateActionListener item);

    void inject(DataLayerPOJOAction item);

    void inject(RemotePOJOAction item);

    void inject(DomainLayerPOJOAction item);

    void inject(CacheLayerPOJOAction item);

    void inject(FromRemoteAction item);

    CommonJavaPostProcessor newCommonJavaPostProcessor();

    AutoValueClassPostProcessor newAutoValueClassPostProcessor();

    KotlinDataClassPostProcessor newKotlinDataClassPostProcessor();
}
