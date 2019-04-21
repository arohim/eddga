package com.robohorse.robopojogenerator.injections;

import com.robohorse.robopojogenerator.actions.api.*;
import com.robohorse.robopojogenerator.actions.data.*;
import com.robohorse.robopojogenerator.actions.domain.UseCaseAction;
import com.robohorse.robopojogenerator.actions.domain.UseCaseTestAction;
import com.robohorse.robopojogenerator.actions.factory.CacheFactoryAction;
import com.robohorse.robopojogenerator.actions.factory.DataFactoryAction;
import com.robohorse.robopojogenerator.actions.factory.DomainFactoryAction;
import com.robohorse.robopojogenerator.actions.factory.RemoteFactoryAction;
import com.robohorse.robopojogenerator.actions.mapper.*;
import com.robohorse.robopojogenerator.actions.pojo.*;
import com.robohorse.robopojogenerator.actions.cache.CacheImplAction;
import com.robohorse.robopojogenerator.actions.presentation.*;
import com.robohorse.robopojogenerator.actions.remote.RemoteImplTestAction;
import com.robohorse.robopojogenerator.actions.tracking.TrackerImplAction;
import com.robohorse.robopojogenerator.actions.tracking.TrackerInterfaceAction;
import com.robohorse.robopojogenerator.generator.postprocessing.common.AutoValueClassPostProcessor;
import com.robohorse.robopojogenerator.generator.postprocessing.common.CommonJavaPostProcessor;
import com.robohorse.robopojogenerator.generator.postprocessing.common.KotlinDataClassPostProcessor;
import com.robohorse.robopojogenerator.listeners.MultiPOJOGenerateActionListener;
import com.robohorse.robopojogenerator.listeners.POJOGenerateActionListener;
import com.robohorse.robopojogenerator.listeners.GenerateActionListener;
import dagger.Component;
import org.jetbrains.annotations.NotNull;

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

    void inject(FromRemoteMapperAction item);

    void inject(ToRemoteMapperAction item);

    void inject(FromRemoteMapperUnitTestAction item);

    void inject(ToRemoteMapperUnitTestAction item);

    void inject(CacheMapperAction item);

    void inject(CacheMapperUnitTestAction item);

    void inject(DataMapperAction item);

    void inject(DataMapperUnitTestAction item);

    void inject(MultiPOJOGenerateActionListener item);

    CommonJavaPostProcessor newCommonJavaPostProcessor();

    AutoValueClassPostProcessor newAutoValueClassPostProcessor();

    KotlinDataClassPostProcessor newKotlinDataClassPostProcessor();

    void inject(@NotNull RemoteFactoryAction cacheFactoryAction);

    void inject(@NotNull CacheFactoryAction cacheFactoryAction);

    void inject(@NotNull DataFactoryAction dataFactoryAction);

    void inject(@NotNull DomainFactoryAction domainFactoryAction);

    void inject(@NotNull ApiServiceImplAction apiServiceImplAction);

    void inject(@NotNull ApiGateWayAction apiGateWayAction);

    void inject(@NotNull ApiServiceInterfaceAction apiServiceInterfaceAction);

    void inject(@NotNull ApiRemoteImplAction apiRemoteImplAction);

    void inject(@NotNull CacheImplAction cacheImplAction);

    void inject(@NotNull DataRepositoryAction dataRepositoryAction);

    void inject(@NotNull RemoteDataStoreAction remoteDataStoreAction);

    void inject(@NotNull DataStoreFactoryAction dataStoreFactoryAction);

    void inject(@NotNull CacheDataStoreAction cacheDataStoreAction);

    void inject(@NotNull CacheInterfaceAction cacheInterfaceAction);

    void inject(@NotNull RemoteInterfaceAction remoteInterfaceAction);

    void inject(@NotNull DataStoreInterfaceAction dataStoreInterfaceAction);

    void inject(@NotNull UseCaseAction useCaseAction);

    void inject(@NotNull ContractInterfaceAction contractInterfaceAction);

    void inject(@NotNull PresenterAction presenterAction);

    void inject(@NotNull FragmentAction fragmentAction);

    void inject(@NotNull DaggerModuleAction daggerModuleAction);

    void inject(@NotNull ActivityAction activityAction);

    void inject(@NotNull TrackerInterfaceAction trackerInterfaceAction);

    void inject(@NotNull TrackerImplAction trackerImplAction);

    void inject(@NotNull UseCaseTestAction useCaseTestAction);

    void inject(@NotNull CacheDataStoreTestAction cacheDataStoreTestAction);

    void inject(@NotNull DataRepositoryTestAction dataRepositoryTestAction);

    void inject(@NotNull DataStoreFactoryTestAction dataStoreTestAction);

    void inject(@NotNull RemoteStoreTestAction remoteStoreTestAction);

    void inject(@NotNull RemoteImplTestAction remoteImplTestAction);
}
