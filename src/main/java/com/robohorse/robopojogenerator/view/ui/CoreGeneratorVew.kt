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
    lateinit var dataPath: JTextField
    lateinit var cachePath: JTextField
    lateinit var roguePath: JTextField
    lateinit var classNameTextField: JTextField
    lateinit var languageGroup: ButtonGroup
    lateinit var typeButtonGroup: ButtonGroup
    lateinit var basePath: JLabel

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
