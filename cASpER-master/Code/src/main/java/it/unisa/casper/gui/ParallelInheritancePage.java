package it.unisa.casper.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import it.unisa.casper.gui.radarMap.RadarMapUtils;
import it.unisa.casper.gui.radarMap.RadarMapUtilsAdapter;
import it.unisa.casper.refactor.manipulator.FieldMover;
import it.unisa.casper.refactor.manipulator.ParallelInheritanceStrategy;
import it.unisa.casper.refactor.manipulator.UpdateClassUtility;
import it.unisa.casper.refactor.strategy.RefactoringManager;
import it.unisa.casper.storage.beans.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParallelInheritancePage  extends DialogWrapper {

    private ClassBean parallelIheritanceClass, superClass1, superClass2;
    private Project project;
    private JPanel mainPanel;
    private RadarMapUtils radars;
    private JPanel radarmaps;
    private List<PackageBean> packageBeans;

    protected ParallelInheritancePage(ClassBean PI, @Nullable Project project,List<PackageBean> systemPackages) {
        super(project);
        this.packageBeans = systemPackages;
        this.parallelIheritanceClass = PI;
        this.project = project;
        this.packageBeans = systemPackages;
        this.superClass1 = getSuperClassBean(PI.getSuperclass());
        this.superClass2 = getSuperClassBean(PI.getParallelInheritanceClass().getSuperclass());
        setResizable(true);
        init();
        setTitle("PARALLEL INHERITANCE PAGE");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        radarmaps = new JPanel();
        radarmaps.setLayout(new GridLayout(0, 2));

        radars = new RadarMapUtilsAdapter();
        JPanel firstClassMap = radars.createRadarMapFromClassBean(superClass1, superClass1.getFullQualifiedName());
        JPanel secondClassMap = radars.createRadarMapFromClassBean(superClass2, superClass2.getFullQualifiedName());
        firstClassMap.setSize(200, 200);
        secondClassMap.setSize(200,200);
        radarmaps.add(firstClassMap);
        radarmaps.add(secondClassMap);


        JPanel sx = new JPanel();
        JPanel dx = new JPanel();
        sx.setBorder(new TitledBorder("CLASSE: "+getClassName(superClass1)));


        sx.setLayout(new BoxLayout(sx, BoxLayout.Y_AXIS));
        dx.setLayout(new BoxLayout(dx, BoxLayout.Y_AXIS));


        ClassBean c = parallelIheritanceClass.getParallelInheritanceClass();
        dx.setBorder(new TitledBorder("CLASSE: "+getClassName(superClass2)));

        JPanel pannelloClasse1 = new JPanel();
        pannelloClasse1.setLayout(new BorderLayout());
        JTextPane contenutoClasse1 = new JTextPane();
        contenutoClasse1.setLayout(new BorderLayout());
        src.main.java.it.unisa.casper.gui.StyleText generator1 = new src.main.java.it.unisa.casper.gui.StyleText();
        contenutoClasse1.setStyledDocument(generator1.createDocument(superClass1.getTextContent()));
        sx.add(contenutoClasse1);

        JPanel pannelloClasse2 = new JPanel();
        pannelloClasse2.setLayout(new BorderLayout());
        JTextPane contenutoClasse2 = new JTextPane();
        contenutoClasse2.setLayout(new BorderLayout());
        src.main.java.it.unisa.casper.gui.StyleText generator2 = new src.main.java.it.unisa.casper.gui.StyleText();
        contenutoClasse2.setStyledDocument(generator2.createDocument(superClass2.getTextContent()));
        dx.add(contenutoClasse2);

        mainPanel.add(sx);
        mainPanel.add(dx);
        JScrollPane scroll = new JScrollPane(mainPanel);

        JPanel temp = new JPanel(new GridLayout(2,0));
        temp.add(radarmaps);
        temp.add(scroll);

        return temp;
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = new DialogWrapperAction("FIND SOLUTION") {

            String message;

            @Override
            protected void doAction(ActionEvent actionEvent) {




                ParallelInheritanceWizard parallelInheritanceWizard = new ParallelInheritanceWizard(superClass1, superClass2, project, packageBeans);
                parallelInheritanceWizard.show();
                close(0);
            }
        };
        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }

    private ClassBean getSuperClassBean(String name){
        for(PackageBean p : packageBeans){
            for(ClassBean c : p.getClassList()){
                if(c.getFullQualifiedName().equalsIgnoreCase(name)){
                   return c;
                }
            }
        }
        return null;
    }


    private String getClassName(ClassBean classBean){
        String[] tmpArray = classBean.getFullQualifiedName().split("\\.");
        String tmp = tmpArray[tmpArray.length-1] + ".java";
        return tmp;
    }
}
