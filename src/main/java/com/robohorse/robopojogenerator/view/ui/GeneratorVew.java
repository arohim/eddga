package com.robohorse.robopojogenerator.view.ui;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by vadim on 24.09.16.
 */
public class GeneratorVew {
    private JPanel rootView;
    private JButton generateButton;
    private RSyntaxTextArea textArea;
    private JRadioButton DomainRadioButton;
    private JRadioButton RemoteRadioButton;
    private JRadioButton CacheRadioButton;
    private JRadioButton DataRadioButton;
    private JTextField className;
    private JScrollPane scrollView;
    private ButtonGroup languageGroup;
    private ButtonGroup typeButtonGroup;

    public JPanel getRootView() {
        return rootView;
    }

    public ButtonGroup getTypeButtonGroup() {
        return typeButtonGroup;
    }

    public JTextField getClassNameTextField() {
        return className;
    }

    public JButton getGenerateButton() {
        return generateButton;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JRadioButton getDomainRadioButton() {
        return DomainRadioButton;
    }

    public JRadioButton getRemoteRadioButton() {
        return RemoteRadioButton;
    }

    public JRadioButton getCacheRadioButton() {
        return CacheRadioButton;
    }

    public JRadioButton getDataRadioButton() {
        return DataRadioButton;
    }

    private void createUIComponents() {
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setCodeFoldingEnabled(true);
        scrollView = new JScrollPane(textArea);
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"));
            theme.apply(textArea);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public ButtonGroup getLanguageGroup() {

        return languageGroup;
    }
}
