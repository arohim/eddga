package com.robohorse.robopojogenerator.listeners

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JFileChooser
import javax.swing.JTextField

open class MouseListenerClicked(private val destinationTextField: JTextField) : MouseListener {

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        chooser.showSaveDialog(null)
        chooser.currentDirectory = java.io.File(".")

        System.out.println(chooser.currentDirectory)
        System.out.println(chooser.selectedFile)

        destinationTextField.text = chooser.selectedFile.path
    }

}
