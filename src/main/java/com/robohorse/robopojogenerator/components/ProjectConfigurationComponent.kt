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

    var cachePath: String = ""
    var dataPath: String = ""
    var domainPath: String = ""
    var roguePath: String = ""
    var lastPath: String = ""

    override fun getState(): ProjectConfigurationComponent? = this

    override fun loadState(state: ProjectConfigurationComponent) =
            XmlSerializerUtil.copyBean(state, this)

    companion object {
        fun getInstance(project: Project): ProjectConfigurationComponent = project.getComponent(ProjectConfigurationComponent::class.java)
    }
}