package com.robohorse.robopojogenerator.components

import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import java.io.Serializable

@State(name = "ProjectConfiguration",
        storages = [Storage(value = "projectConfiguration.xml")])
class ProjectConfigurationComponent(project: Project? = null) :
        AbstractProjectComponent(project),
        Serializable,
        PersistentStateComponent<ProjectConfigurationComponent> {

    var cachePath: String? = null
    var dataPath: String? = null
    var domainPath: String? = null
    var roguePath: String? = null
    var remotePath: String? = null
    var cacheTestPath: String? = null
    var dataTestPath: String? = null
    var domainTestPath: String? = null
    var remoteTestPath: String? = null
    var rootClassNameTextField: String? = null

    var cacheCheckBox: Boolean = false
    var dataCheckBox: Boolean = false
    var domainCheckBox: Boolean = false
    var rogueCheckBox: Boolean = false
    var remoteCheckBox: Boolean = false
    var cacheTestCheckBox: Boolean = false
    var dataTestCheckBox: Boolean = false
    var domainTestCheckBox: Boolean = false
    var remoteTestCheckBox: Boolean = false

    override fun getState(): ProjectConfigurationComponent? = this

    override fun loadState(state: ProjectConfigurationComponent) =
            XmlSerializerUtil.copyBean(state, this)

    companion object {
        fun getInstance(project: Project): ProjectConfigurationComponent = project.getComponent(ProjectConfigurationComponent::class.java)
    }
}