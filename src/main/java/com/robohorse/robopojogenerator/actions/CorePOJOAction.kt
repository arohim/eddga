package com.robohorse.robopojogenerator.actions

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class CorePOJOAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {
        val notification = NotificationGroup("Not Implemented", NotificationDisplayType.STICKY_BALLOON, true)
        notification.createNotification("Not Implemented",
                "Will be implemented soon",
                NotificationType.INFORMATION,
                null
        ).notify(e?.project)
    }
}