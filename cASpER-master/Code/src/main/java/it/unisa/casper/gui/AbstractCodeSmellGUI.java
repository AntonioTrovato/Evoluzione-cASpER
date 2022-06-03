package it.unisa.casper.gui;

import javax.swing.*;

public interface AbstractCodeSmellGUI {

    public JComponent createCenterPanel();
    public Action[] createActions();
}
