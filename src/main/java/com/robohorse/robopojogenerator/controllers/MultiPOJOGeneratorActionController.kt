package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.delegates.*
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.consts.annotations.AnnotationEnum
import com.robohorse.robopojogenerator.generator.consts.templates.ArrayItemsTemplate
import com.robohorse.robopojogenerator.generator.consts.templates.ClassTemplate
import com.robohorse.robopojogenerator.listeners.CoreGeneratorFormEventListener
import com.robohorse.robopojogenerator.models.CoreGeneratorModel
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel
import com.robohorse.robopojogenerator.view.binders.CoreGeneratorViewBinder
import javax.inject.Inject

open class MultiPOJOGeneratorActionController @Inject constructor() {

    @Inject
    lateinit var messageDelegate: MessageDelegate

    @Inject
    lateinit var environmentDelegate: ProjectEnvironmentDelegate

    @Inject
    lateinit var directoryCreatorDelegate: DirectoryCreatorDelegate

    @Inject
    lateinit var viewBinder: CoreGeneratorViewBinder

    @Inject
    lateinit var pOJOGenerationDelegate: POJOGenerationDelegate

    @Inject
    lateinit var mapperGenerationDelegate: MapperGeneratorDelegate

    @Inject
    lateinit var domainCreatorDelegate: DomainCreatorDelegate

    @Inject
    lateinit var dataCreatorDelegate: DataCreatorDelegate

    fun onActionHandled(event: AnActionEvent) {
        try {
            proceed(event)
        } catch (exception: RoboPluginException) {
            messageDelegate.onPluginExceptionHandled(exception)
        }
    }

    private fun proceed(event: AnActionEvent) {
        val projectModel = environmentDelegate.obtainProjectModel(event)
        val dialogBuilder = DialogBuilder()
        val window = dialogBuilder.window

        with(viewBinder) {
            bindView(dialogBuilder, event, projectModel, object : CoreGeneratorFormEventListener {
                override fun onJsonDataObtained(coreGeneratorModel: CoreGeneratorModel) {
                    event.project?.let {
                        domainCreatorDelegate.runGenerationTask(it, projectModel, coreGeneratorModel)
                        dataCreatorDelegate.runGenerationTask(it, projectModel, coreGeneratorModel)
//                        generateFolders(it, projectModel, coreGeneratorModel)
//                        generatePOJOs(it, projectModel, coreGeneratorModel)
//                        generateMappers(it, projectModel, coreGeneratorModel)
                    }
                    window.dispose()
                }
            })
        }
    }

    private fun generateMappers(project: Project, projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
//        val cachePath = projectModel.project.basePath + coreGeneratorModel.cachePath
//        val cacheGenerationModel = GenerationModel.Builder()
//                .setContent(coreGeneratorModel.content)
//                .setRootClassName(coreGeneratorModel.rootClassName)
//                .build()
//        val cacheMapperGeneratorModel = MapperGeneratorModel(
//                fileNameSuffix = "EntityMapper",
//                templateName = "CacheMapper"
//        )
//
//        mapperGenerationDelegate.runGenerationTask(cacheGenerationModel, projectModel, cacheMapperGeneratorModel)
    }

    private fun generatePOJOs(project: Project, projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
        val domainPath = projectModel.project.basePath + coreGeneratorModel.domainPath
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

        generatePOJO(domainPath, domainGenerationModel, project, projectModel)

        val cachePath = projectModel.project.basePath + coreGeneratorModel.cachePath
        val cacheGenerationModel = GenerationModel.Builder()
                .useKotlin(true)
                .setAnnotationItem(AnnotationEnum.NONE)
                .setSettersAvailable(false)
                .setGettersAvailable(false)
                .setToStringAvailable(false)
                .setRewriteClasses(true)
                .setPrefix("Cached")
                .setSuffix("")
                .setContent(coreGeneratorModel.content)
                .setRootClassName(coreGeneratorModel.rootClassName)
                .setFieldDTOFormat(ClassTemplate.NON_NULL_FIELD_KOTLIN_DTO)
                .setListFormat(ArrayItemsTemplate.NON_NULL_LIST_OF_ITEM)
                .setDialogTitle("Cache POJO Generator")
                .build()

        generatePOJO(cachePath, cacheGenerationModel, project, projectModel)

        val dataPath = projectModel.project.basePath + coreGeneratorModel.dataPath
        val dataGenerationModel = GenerationModel.Builder()
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

        generatePOJO(dataPath, dataGenerationModel, project, projectModel)

        val roguePath = projectModel.project.basePath + coreGeneratorModel.roguePath
        val rogueGenerationModel = GenerationModel.Builder()
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

        generatePOJO(roguePath, rogueGenerationModel, project, projectModel)

    }

    private fun generatePOJO(path: String, generationModel: GenerationModel, project: Project, projectModel: ProjectModel) {
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(path)!!
        val directory = PsiManager.getInstance(project).findDirectory(virtualFile)
        val regenProjectModel = ProjectModel.Builder()
                .setDirectory(directory)
                .setDirectoryPath(directory?.virtualFile?.path)
                .setPackageName(projectModel.packageName)
                .setProject(project)
                .setVirtualFolder(projectModel.virtualFolder)
                .build()

        pOJOGenerationDelegate.runGenerationTask(generationModel, regenProjectModel)
    }

    private fun generateFolders(project: Project, projectModel: ProjectModel, coreGeneratorModel: CoreGeneratorModel) {
//        val projectDir = PsiManager.getInstance(project).findDirectory(projectModel.project.baseDir)
//                ?: throw PathException()
//
//        // create domain directories
//        val domainPath = projectModel.project.basePath + coreGeneratorModel.domainPath
//        directoryCreatorDelegate.createDirectory(, projectDir, domainPath)
//
//        // create data directories
//        val dataPath = projectModel.project.basePath + coreGeneratorModel.dataPath
//        directoryCreatorDelegate.createDirectory(, projectDir, dataPath)
//
//        // create cache directories
//        val cachePath = projectModel.project.basePath + coreGeneratorModel.cachePath
//        directoryCreatorDelegate.createDirectory(, projectDir, cachePath)
//
//        // create rogue directories
//        val roguePath = projectModel.project.basePath + coreGeneratorModel.roguePath
//        directoryCreatorDelegate.createDirectory(, projectDir, roguePath)
//
//        // create remote directories
//        val remotePath = projectModel.project.basePath + coreGeneratorModel.remotePath
//        directoryCreatorDelegate.createDirectory(, projectDir, remotePath)
//
//        environmentDelegate.refreshProject(projectModel)
    }

    companion object {
        const val MODEL_PATH = "/model"
        const val MAPPER_PATH = "/mapper"
    }
}
