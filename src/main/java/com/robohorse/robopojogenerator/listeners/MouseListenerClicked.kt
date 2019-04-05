package com.robohorse.robopojogenerator.listeners

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFileChooser
import javax.swing.JTextField

open class MouseListenerClicked(private val destinationTextField: JTextField) : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        chooser.showSaveDialog(null)
        chooser.currentDirectory = java.io.File(".")

        System.out.println(chooser.currentDirectory)
        System.out.println(chooser.selectedFile)

        destinationTextField.text = chooser.selectedFile.path
    }
}
