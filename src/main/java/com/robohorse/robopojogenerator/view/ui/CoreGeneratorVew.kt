package com.robohorse.robopojogenerator.view.ui

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.Theme

import javax.swing.*
import java.io.IOException

class CoreGeneratorVew {
    val rootView: JPanel? = null
    val generateButton: JButton? = null
    private var textArea: RSyntaxTextArea? = null
    private var scrollView: JScrollPane? = null
    val genRogueCheckBox: JCheckBox? = null
    val genDataCheckBox: JCheckBox? = null
    val genDomainCheckBox: JCheckBox? = null
    val genCacheCheckBox: JCheckBox? = null
    val domainPath: JTextField? = null
    val dataPath: JTextField? = null
    val cachePath: JTextField? = null
    val roguePath: JTextField? = null

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
