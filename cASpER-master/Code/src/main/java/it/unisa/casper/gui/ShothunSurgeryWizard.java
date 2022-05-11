package it.unisa.casper.gui;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import it.unisa.casper.gui.radarMap.RadarMapUtils;
import it.unisa.casper.gui.radarMap.RadarMapUtilsAdapter;
import it.unisa.casper.refactor.manipulator.FeatureEnvyRefactoringStrategy;
import it.unisa.casper.refactor.manipulator.ShotgunSurgeryRefactoringStrategy;
import it.unisa.casper.refactor.strategy.RefactoringManager;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShothunSurgeryWizard extends DialogWrapper {


    private Project project;
    private ClassBean classeAffetta;
    private JPanel mainPanel;
    private boolean errorOccurred;
    private RadarMapUtils radars;
    private JPanel radarmaps;

    protected ShothunSurgeryWizard(ClassBean classeAffetta, Project project) {
        super(true);
        this.classeAffetta = classeAffetta;
        this.project = project;
        this.errorOccurred = false;
        setResizable(true);
        init();
        setTitle("SHOTGUN SURGERY WIZARD");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mainPanel = new JPanel();
        radarmaps = new JPanel();
        radarmaps.setLayout(new GridLayout(0, 2));

        radars = new RadarMapUtilsAdapter();
        JPanel oldClassRadarMap = radars.createRadarMapFromClassBean(classeAffetta, "Old Class");
        JPanel newClassRadarMap = radars.createRadarMapFromClassBean(new ClassBean.Builder(classeAffetta.getFullQualifiedName(), generaTextContent()).build(), "New Class");
        oldClassRadarMap.setSize(200,200);
        newClassRadarMap.setSize(200,200);
        radarmaps.add(oldClassRadarMap);
        radarmaps.add(newClassRadarMap);


        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JPanel sx = new JPanel();
        JPanel dx = new JPanel();
        sx.setLayout(new BoxLayout(sx, BoxLayout.Y_AXIS));
        sx.setBorder(new TitledBorder("OLD CLASS"));
        dx.setLayout(new BoxLayout(dx, BoxLayout.Y_AXIS));
        dx.setBorder(new TitledBorder("NEW CLASS"));

        src.main.java.it.unisa.casper.gui.StyleText generator = new src.main.java.it.unisa.casper.gui.StyleText();

        JTextPane classeVecchia = new JTextPane();
        String textContentOld = classeAffetta.getTextContent();
        classeVecchia.setStyledDocument(generator.createDocument(textContentOld));
        sx.add(classeVecchia);

        JTextPane classeNuova = new JTextPane();
        classeNuova.setStyledDocument(generator.createDocument(generaTextContent()));
        dx.add(classeNuova);

        mainPanel.add(sx);
        mainPanel.add(dx);

        JScrollPane scroll = new JScrollPane(mainPanel);

        JPanel temp = new JPanel(new GridLayout(2,0));
        temp.add(radarmaps);
        temp.add(scroll);

        return temp;
    }

    private String generaTextContent(){

        StringBuilder stringBuilder = new StringBuilder(classeAffetta.getTextContent().substring(0,classeAffetta.getTextContent().lastIndexOf("}") - 1));

        for (ClassBean classeColpita : classeAffetta.getShotgunSurgeryHittedClasses()){
            for(MethodBean metodoColpito : classeColpita.getShotgunSurgeryHittedMethods()){
                stringBuilder.append("\n" + metodoColpito.getTextContent());
            }
        }
        stringBuilder.append("\n}");

        return stringBuilder.toString();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = new DialogWrapperAction("REFACTOR") {

            String message;
            Icon icon;

            @Override
            protected void doAction(ActionEvent actionEvent) {

                ShotgunSurgeryRefactoringStrategy shotgunSurgeryRefactoringStrategy = new ShotgunSurgeryRefactoringStrategy(classeAffetta, project);
                RefactoringManager refactoringManager = new RefactoringManager(shotgunSurgeryRefactoringStrategy);

                WriteCommandAction.runWriteCommandAction(project, () -> {
                    try {
                        refactoringManager.executeRefactor();
                    } catch (Exception e) {
                        errorOccurred = true;
                        message = e.getMessage();
                    }
                });

                if (errorOccurred) {
                    //message = "Something went wrong during refactoring. Press Ctrl+Z to fix";
                    icon = Messages.getErrorIcon();
                } else {
                    message = "Move method refactoring correctly executed.\nCheck the imports in the manipulated classes.";
                    icon = Messages.getInformationIcon();
                }

                Messages.showMessageDialog(message, "Outcome of refactoring", icon);
                close(0);
            }
        };

        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }
}
