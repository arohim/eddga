package com.robohorse.robopojogenerator.view.ui

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.Theme
import java.io.IOException
import javax.swing.*


class CoreGeneratorVew {
    lateinit var rootView: JPanel
    lateinit var generateButton: JButton
    lateinit var textArea: RSyntaxTextArea
    lateinit var scrollView: JScrollPane
    lateinit var genRogueCheckBox: JCheckBox
    lateinit var genDataCheckBox: JCheckBox
    lateinit var genDomainCheckBox: JCheckBox
    lateinit var genCacheCheckBox: JCheckBox
    lateinit var domainPath: JTextField
    lateinit var domainPathButton: JButton
    lateinit var dataPath: JTextField
    lateinit var dataPathButton: JButton
    lateinit var cachePath: JTextField
    lateinit var cachePathButton: JButton
    lateinit var roguePath: JTextField
    lateinit var roguePathButton: JButton
    lateinit var languageGroup: ButtonGroup
    lateinit var typeButtonGroup: ButtonGroup

    fun getTextArea(): JTextArea? {
        return textArea
    }

    private fun createUIComponents() {
        textArea = RSyntaxTextArea()
        textArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
        textArea.isCodeFoldingEnabled = true
        scrollView = JScrollPane(textArea)
        try {
            val theme = Theme.load(javaClass.getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"))
            theme.apply(textArea)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }
}
