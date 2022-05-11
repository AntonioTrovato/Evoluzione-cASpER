package it.unisa.casper.gui;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import it.unisa.casper.gui.radarMap.RadarMapUtils;
import it.unisa.casper.gui.radarMap.RadarMapUtilsAdapter;
import it.unisa.casper.refactor.manipulator.ParallelInheritanceStrategy;
import it.unisa.casper.refactor.manipulator.ShotgunSurgeryRefactoringStrategy;
import it.unisa.casper.refactor.manipulator.UpdateClassUtility;
import it.unisa.casper.refactor.strategy.RefactoringManager;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.InstanceVariableBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ParallelInheritanceWizard  extends DialogWrapper {

    private Project project;
    private ClassBean super1, super2;
    private JPanel mainPanel;
    private boolean errorOccurred;
    private RadarMapUtils radars;
    private JPanel radarmaps;
    private List<PackageBean> packageBeans;

    protected ParallelInheritanceWizard(ClassBean super1, ClassBean super2, @Nullable Project project,List<PackageBean> systemPackages) {
        super(true);
        this.project = project;
        this.packageBeans = systemPackages;
        this.super1 = super1;
        this.super2 = super2;
        this.errorOccurred = false;
        setResizable(true);
        init();
        setTitle("PARALLEL INHERITANCE WIZARD");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mainPanel = new JPanel();
        radarmaps = new JPanel();
        radarmaps.setLayout(new GridLayout(0, 2));

        radars = new RadarMapUtilsAdapter();
        JPanel oldClassRadarMap = radars.createRadarMapFromClassBean(super1, "Old SuperClass");
        JPanel newClassRadarMap = radars.createRadarMapFromClassBean(new ClassBean.Builder(super1.getFullQualifiedName(), generaTextContent()).build(), "New SuperClass");
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
        String textContentOld = super1.getTextContent();
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

        StringBuilder stringBuilder = new StringBuilder(super1.getTextContent().substring(0,super1.getTextContent().indexOf("{")));

        for(InstanceVariableBean i : super1.getInstanceVariablesList()){
            stringBuilder.append("\n" + i.getVisibility() + " " + i.getType() + " " + getInstanceVariableName(i) + ";");
        }

        for(InstanceVariableBean i : super2.getInstanceVariablesList()){
            Boolean test = false;
            for(InstanceVariableBean i2 : super1.getInstanceVariablesList()){
                if(getInstanceVariableName(i2).equalsIgnoreCase(getInstanceVariableName(i))){
                    test = test || true;
                 }else{
                    test = test || false;
                }
            }
            if(test){

                stringBuilder.append("\n" + i.getVisibility() + " " + i.getType() + " " + getInstanceVariableName(i)+"_refactor" + ";");
            }else{
                stringBuilder.append("\n" + i.getVisibility() + " " + i.getType() + " " + getInstanceVariableName(i) + ";");
            }


        }

        for(MethodBean methodBean : super1.getMethodList()){
            stringBuilder.append("\n"+methodBean.getTextContent());
        }

        for(MethodBean method : super2.getMethodList()){
            stringBuilder.append("\n" + method.getTextContent());
        }
        stringBuilder.append("\n}");

        return stringBuilder.toString();
    }

    private String getInstanceVariableName(InstanceVariableBean c){
        String[] tmpArray = c.getFullQualifiedName().split("\\.");
        String tmp = tmpArray[tmpArray.length-1];
        return tmp;
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = new DialogWrapperAction("REFACTOR") {

            String message;
            Icon icon;

            @Override
            protected void doAction(ActionEvent actionEvent) {

                ParallelInheritanceStrategy parallelInheritanceStrategy = new ParallelInheritanceStrategy(super1, super2, project, packageBeans);
                RefactoringManager refactoringManager = new RefactoringManager(parallelInheritanceStrategy);

                //WriteCommandAction.runWriteCommandAction(project, () -> {
                    try {
                        refactoringManager.executeRefactor();
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorOccurred = true;
                        message = e.getMessage();
                    }
               // });

                for (PackageBean p : packageBeans) {
                    System.out.println("PACKAGE: " + p.getFullQualifiedName());
                    for (ClassBean c : p.getClassList()) {
                        System.out.println("CLASSE: " + c.getFullQualifiedName());
                        if (c.getSuperclass().equalsIgnoreCase(super2.getFullQualifiedName())) {
                            System.out.println("IF TRUE");
                            UpdateClassUtility.modifyExtend(c, super1);
                        }
                    }
                }

                UpdateClassUtility.deleteClassFile(super2);

                System.out.println("BOH");

                if (errorOccurred) {
                    icon = Messages.getErrorIcon();
                } else {
                    message = "Parallel inheritance refactoring correctly executed.\nCheck the manipulated classes.";
                    icon = Messages.getInformationIcon();
                }

                Messages.showMessageDialog(message, "Outcome of refactoring", icon);
                close(0);
            }
        };

        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }
}
