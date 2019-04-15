package com.robohorse.robopojogenerator.controllers

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.robohorse.robopojogenerator.delegates.DirectoryCreatorDelegate
import com.robohorse.robopojogenerator.delegates.MessageDelegate
import com.robohorse.robopojogenerator.delegates.POJOGenerationDelegate
import com.robohorse.robopojogenerator.delegates.ProjectEnvironmentDelegate
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.errors.custom.PathException
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
    lateinit var generationDelegate: POJOGenerationDelegate

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
                        generateFolders(it, projectModel, coreGeneratorModel)
                        generatePOJOs(it, projectModel, coreGeneratorModel)
                    }
                    window.dispose()
                }
            })
        }
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
        val projectModel = ProjectModel.Builder()
                .setDirectory(directory)
                .setDirectoryPath(directory?.virtualFile?.path)
                .setPackageName(projectModel.packageName)
                .setProject(project)
                .setVirtualFolder(projectModel.virtualFolder)
                .build()

        generationDelegate.runGenerationTask(generationModel, projectModel)
    }

    private fun generateFolders(project: Project, projectModel: ProjectModel, model: CoreGeneratorModel) {
        val projectDir = PsiManager.getInstance(project).findDirectory(projectModel.project.baseDir)
                ?: throw PathException()

        // create domain directories
        val domainPath = projectModel.project.basePath + model.domainPath
        directoryCreatorDelegate.createDirectory(project, projectDir, domainPath)

        // create data directories
        val dataPath = projectModel.project.basePath + model.dataPath
        directoryCreatorDelegate.createDirectory(project, projectDir, dataPath)

        // create cache directories
        val cachePath = projectModel.project.basePath + model.cachePath
        directoryCreatorDelegate.createDirectory(project, projectDir, cachePath)

        // create rogue directories
        val roguePath = projectModel.project.basePath + model.roguePath
        directoryCreatorDelegate.createDirectory(project, projectDir, roguePath)

        // create remote directories
        val remotePath = projectModel.project.basePath + model.remotePath
        directoryCreatorDelegate.createDirectory(project, projectDir, remotePath)

        environmentDelegate.refreshProject(projectModel)
    }

}
