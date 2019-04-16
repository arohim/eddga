package com.robohorse.robopojogenerator.delegates

import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController.Companion.MODEL_PATH
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel
import javax.inject.Inject

open class RogueCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    fun runGenerationTask(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        generatePOJO(projectModel, coreGeneratorModel)
//        generateMapper(generationModel, projectModel)
//        generateFactory(generationModel, projectModel)
//        generateMapperUnitTest(generationModel, projectModel)
//        generateUseCase(generationModel, projectModel)
    }

    private fun generateUseCase(generationModel: GenerationModel, projectModel: ProjectModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generateMapperUnitTest(generationModel: GenerationModel, projectModel: ProjectModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generateFactory(generationModel: GenerationModel, projectModel: ProjectModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generateMapper(generationModel: GenerationModel, projectModel: ProjectModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generatePOJO(projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val path = coreGeneratorModel.roguePath + MODEL_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val generationModel = GenerationModel.Builder()
                .useKotlin(true)
                .setAnnotationItem(AnnotationEnum.GSON)
                .setSettersAvailable(false)
                .setGettersAvailable(false)
                .setToStringAvailable(false)
                .setRewriteClasses(true)
                .setPrefix("")
                .setSuffix("Model")
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .setFieldDTOFormat(ClassTemplate.NULLABLE_FIELD_KOTLIN_DTO)
                .setListFormat(ArrayItemsTemplate.NULLABLE_LIST_OF_ITEM)
                .setDialogTitle("Rogue POJO Generator")
                .build()

        pOJOGenerationDelegate.runGenerationTask(generationModel, regenProjectModel)
    }

}