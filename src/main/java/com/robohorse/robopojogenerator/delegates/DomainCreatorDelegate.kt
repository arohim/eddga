package com.robohorse.robopojogenerator.delegates

import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.controllers.CoreGeneratorActionController.Companion.MODEL_PATH
import com.robohorse.robopojogenerator.errors.custom.PathException
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel
import java.io.File
import javax.inject.Inject

open class DomainCreatorDelegate @Inject constructor() : CoreCreatorDelegate() {

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
        val path = coreGeneratorModel.domainPath + MODEL_PATH
        val regenProjectModel = regenProjectModel(projectModel, path)
        val domainGenerationModel = GenerationModel.Builder()
                .useKotlin(true)
                .setAnnotationItem(AnnotationEnum.NONE)
                .setSettersAvailable(false)
                .setGettersAvailable(false)
                .setToStringAvailable(false)
                .setRewriteClasses(true)
                .setPrefix("")
                .setSuffix("")
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .setFieldDTOFormat(ClassTemplate.NON_NULL_FIELD_KOTLIN_DTO)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .build()

        pOJOGenerationDelegate.runGenerationTask(domainGenerationModel, regenProjectModel)
    }

}