package it.unisa.casper.gui;

import it.unisa.casper.gui.radarMap.RadarMapUtils;
import it.unisa.casper.gui.radarMap.RadarMapUtilsAdapter;
import org.jetbrains.annotations.NotNull;
import src.main.java.it.unisa.casper.gui.StyleText;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShotgunSurgeryPage  extends DialogWrapper {

    private ClassBean shotgunSurgeryClass;
    private Project project;
    private JPanel mainPanel;
    private RadarMapUtils radars;
    private JPanel radarmaps;

    protected ShotgunSurgeryPage(ClassBean shotgunSurgeryClass, @Nullable Project project) {
        super(project, true);
        this.shotgunSurgeryClass = shotgunSurgeryClass;
        this.project = project;
       // setSize(10000, 10000);
        setResizable(true);
        init();
        setTitle("SHOTGUN SURGERY PAGE");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        radarmaps = new JPanel();
        radarmaps.setLayout(new GridLayout(0, 1));

        radars = new RadarMapUtilsAdapter();
        JPanel radarMap = radars.createRadarMapFromClassBean(shotgunSurgeryClass, "Shotgun Surgery Class Topics");
        radarMap.setSize(300,300);
        radarmaps.add(radarMap);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));


        JPanel sx = new JPanel();
        JPanel dx = new JPanel();
        sx.setLayout(new BoxLayout(sx, BoxLayout.Y_AXIS));
        dx.setLayout(new BoxLayout(dx, BoxLayout.Y_AXIS));


        List<ClassBean> listaClassiColpite =  this.shotgunSurgeryClass.getShotgunSurgeryHittedClasses();

        int indice = 0;
        for (ClassBean c : listaClassiColpite) {

            List<MethodBean> listaMetodiColpiti = c.getShotgunSurgeryHittedMethods();

            indice++;

            JPanel pannelloMetodi = new JPanel();
            pannelloMetodi.setLayout(new BorderLayout());
            String[] tmpArray = c.getFullQualifiedName().split("\\.");
            String tmp = tmpArray[tmpArray.length-1] + ".java";
            pannelloMetodi.setBorder(new TitledBorder("CLASSE: "+tmp));
            JTextPane contenutoMetodi = new JTextPane();
            contenutoMetodi.setLayout(new BorderLayout());

            String textContent = "";
            for (MethodBean m : listaMetodiColpiti) {
                //TEST
                System.out.println(m.getFullQualifiedName());
                //TEST

                textContent = textContent + m.getTextContent() + "\n";
            }


            StyleText generator = new StyleText();
            contenutoMetodi.setStyledDocument(generator.createDocument(textContent));
            pannelloMetodi.add(contenutoMetodi);
            if((indice/2) == 0){
                sx.add(pannelloMetodi);
            }else{
                dx.add(pannelloMetodi);
            }

        }

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
                ShothunSurgeryWizard shothunSurgeryWizard = new ShothunSurgeryWizard(shotgunSurgeryClass, project);
                shothunSurgeryWizard.show();
                close(0);
            }
        };
        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }
}
