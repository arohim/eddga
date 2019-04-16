package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController.Companion.MAPPER_PATH
import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController.Companion.MODEL_PATH
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.MapperGeneratorModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class DataCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var mapperGenerationDelegate: MapperGeneratorDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generatePOJO(projectModel, coreGeneratorModel)
        generateMapper(projectModel, coreGeneratorModel)
    }

    private fun generateMapper(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + MAPPER_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val generationModel = GenerationModel.Builder()
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .build()
        val mapperGeneratorModel = MapperGeneratorModel(
                fileNameSuffix = "Mapper",
                templateName = "DataMapper"
        )

        mapperGenerationDelegate.runGenerationTask(generationModel, regenProjectModel, mapperGeneratorModel)
    }

    private fun generatePOJO(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.dataPath + MODEL_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val generationModel = GenerationModel.Builder()
                .useKotlin(true)
                .setAnnotationItem(AnnotationEnum.NONE)
                .setSettersAvailable(false)
                .setGettersAvailable(false)
                .setToStringAvailable(false)
                .setRewriteClasses(true)
                .setPrefix("")
                .setSuffix("Entity")
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .setFieldDTOFormat(ClassTemplate.NON_NULL_FIELD_KOTLIN_DTO)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .setDialogTitle("Data Layer POJO Generator")
                .build()

        pOJOGenerationDelegate.runGenerationTask(generationModel, regenProjectModel)
    }

}