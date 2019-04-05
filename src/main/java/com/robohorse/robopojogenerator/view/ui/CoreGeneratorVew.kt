package com.robohorse.robopojogenerator.view.ui

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.Theme

import javax.swing.*
import java.io.IOException

class CoreGeneratorVew {
    var rootView: JPanel? = null
    var generateButton: JButton? = null
    var textArea: RSyntaxTextArea? = null
    var scrollView: JScrollPane? = null
    var genRogueCheckBox: JCheckBox? = null
    var genDataCheckBox: JCheckBox? = null
    var genDomainCheckBox: JCheckBox? = null
    var genCacheCheckBox: JCheckBox? = null
    var domainPath: JTextField? = null
    var dataPath: JTextField? = null
    var cachePath: JTextField? = null
    var roguePath: JTextField? = null
    var languageGroup: ButtonGroup? = null
    var typeButtonGroup: ButtonGroup? = null

    fun getTextArea(): JTextArea? {
        return textArea
    }

    private fun createUIComponents() {
        textArea = RSyntaxTextArea()
        textArea?.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
        textArea?.isCodeFoldingEnabled = true
        scrollView = JScrollPane(textArea)
        try {
            val theme = Theme.load(javaClass.getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"))
            theme.apply(textArea!!)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }

    }
}
