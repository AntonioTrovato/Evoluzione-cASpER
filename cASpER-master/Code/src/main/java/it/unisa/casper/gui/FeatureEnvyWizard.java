package it.unisa.casper.gui;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import it.unisa.casper.gui.radarMap.RadarMapUtils;
import it.unisa.casper.gui.radarMap.RadarMapUtilsAdapter;
import it.unisa.casper.refactor.manipulator.FeatureEnvyRefactoringStrategy;
import it.unisa.casper.refactor.manipulator.UpdateClassUtility;
import it.unisa.casper.refactor.strategy.RefactoringManager;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;
import it.unisa.casper.topic.TopicExtracter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import src.main.java.it.unisa.casper.gui.StyleText;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FeatureEnvyWizard extends DialogWrapper {

    private Project project;
    private List<PackageBean> packageBeans;
    private MethodBean smellMethod;
    private JPanel radarmaps;
    private JPanel contentPanel;
    private JPanel livelli;
    private JPanel codici;
    private JPanel oldcurrentclass;
    private JPanel oldenviedclass;
    private JPanel newenviedclass;
    private JPanel newcurrentclass;
    private RadarMapUtils radars;
    private static StringBuilder textAreaContent;
    private boolean errorOccurred;

    public FeatureEnvyWizard(MethodBean smellMethod, Project project, List<PackageBean> systemPackages) {
        super(true);
        setResizable(false);
        this.smellMethod = smellMethod;
        this.project = project;
        this.errorOccurred = false;
        this.packageBeans = systemPackages;
        init();
        setTitle("FEATURE ENVY REFACTORING");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        contentPanel = new JPanel(); // pannello principale
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setPreferredSize(new Dimension(1250, 900));
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        livelli = new JPanel();
        radarmaps = new JPanel(); // pannello per visualizzare le radarMaps
        codici = new JPanel();
        radars = new RadarMapUtilsAdapter();

        livelli.setLayout(new GridLayout(2, 0));
        radarmaps.setLayout(new GridLayout(0, 4));
        codici.setLayout(new GridLayout(0, 2));

        oldcurrentclass = radars.createRadarMapFromClassBean(smellMethod.getBelongingClass(), "Old Current Class Topics");
        oldenviedclass = radars.createRadarMapFromClassBean(smellMethod.getEnviedClass(), "Old Envied Class Topics");
        newcurrentclass = getRadarMapFromNewCurrentClass(smellMethod, new ClassBean.Builder(smellMethod.getBelongingClass().getFullQualifiedName(), smellMethod.getBelongingClass().getTextContent()).build());
        newenviedclass = getRadarMapFromNewEnviedClass(smellMethod, smellMethod.getEnviedClass());

        oldcurrentclass.setSize(200, 200);
        oldenviedclass.setSize(200, 200);
        newcurrentclass.setSize(200, 200);
        newenviedclass.setSize(200, 200);
        radarmaps.add(oldcurrentclass);
        radarmaps.add(oldenviedclass);
        radarmaps.add(newcurrentclass);
        radarmaps.add(newenviedclass);

        createTextArea("Old Text Content", smellMethod.getTextContent());
        String newText = smellMethod.getEnviedClass().getTextContent();
        newText = newText.substring(0, newText.lastIndexOf("}") - 1) + "\n\n   " + smellMethod.getTextContent() + "\n}";
        createTextArea("New Text Content", newText);

        livelli.add(radarmaps);
        livelli.add(codici);
        centralPanel.add(livelli);

        contentPanel.add(centralPanel, BorderLayout.CENTER);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textAreaContent = new StringBuilder();
        textAreaContent.append("By clicking \"REFACTOR\" button, method ");
        textAreaContent.append(smellMethod.getFullQualifiedName());
        textAreaContent.append(" will be moved from ");
        textAreaContent.append(smellMethod.getBelongingClass().getFullQualifiedName());
        textAreaContent.append(" to ");
        textAreaContent.append(smellMethod.getEnviedClass().getFullQualifiedName());

        textArea.setText(textAreaContent.toString());

        contentPanel.add(new JBScrollPane(textArea), BorderLayout.SOUTH);

        return contentPanel;
    }

    private void createTextArea(String titolo, String message) {
        JTextPane textContentArea = new JTextPane();
        textContentArea.setEditable(false);
        JPanel nuovo = new JPanel();
        nuovo.setBorder(new TitledBorder(titolo));
        JScrollPane scroll = new JScrollPane(textContentArea);
        nuovo.setLayout(new BorderLayout(0, 0));
        nuovo.add(scroll, BorderLayout.CENTER);
        StyleText generator = new StyleText();
        textContentArea.setStyledDocument(generator.createDocument(message));
        codici.add(nuovo);
    }

    private JPanel getRadarMapFromNewCurrentClass(MethodBean smellMethod, ClassBean oldBelongingClass) {
        TreeMap<String, Integer> belongingClassTopicsFinali = new TreeMap<>(); // treemap in cui inserisco i topic definitivi della new belonging class
        TopicExtracter extracter1 = new TopicExtracter();
        TreeMap<String, Integer> oldBelongingClassTopics = extracter1.extractTopicFromClassBean(oldBelongingClass);
        Set<Map.Entry<String, Integer>> oldBelongingClassTopicSet = oldBelongingClassTopics.entrySet(); //set dei topic della belonging class PRIMA del refactoring

        //istanzia il new belonging class senza il metodo smell
        TopicExtracter extracter2 = new TopicExtracter();
        ClassBean newBelongingClass = createNewBelongingClass(smellMethod, oldBelongingClass);
        TreeMap<String, Integer> newBelongingClassTopics = extracter2.extractTopicFromClassBean(newBelongingClass);
        Set<Map.Entry<String, Integer>> newBelongingClassTopicSet = newBelongingClassTopics.entrySet(); //set di topic della belonging class DOPO il refactoring

        belongingClassTopicsFinali = getStringIntegerTreeMap(oldBelongingClassTopicSet, newBelongingClassTopicSet);
        return radars.getRadarMapPanel(belongingClassTopicsFinali, "New Current Class Topic");
    }

    @NotNull
    static TreeMap<String, Integer> getStringIntegerTreeMap(Set<Map.Entry<String, Integer>> oldBelongingClassTopicSet, Set<Map.Entry<String, Integer>> newBelongingClassTopicSet) {
        TreeMap<String, Integer> belongingClassTopicsFinali = new TreeMap<>(); // treemap in cui inserisco i topic definitivi della new belonging class
        for (Map.Entry<String, Integer> anOldTopic : oldBelongingClassTopicSet) {  //confronto ogni topic dei set old e new
            for (Map.Entry<String, Integer> aNewTopic : newBelongingClassTopicSet) {
                if (anOldTopic.getKey().equalsIgnoreCase(aNewTopic.getKey())) { //se i topic hanno la stessa chiave, cioè il nome, allora lo aggiungo alla treemap dei topic finali
                    belongingClassTopicsFinali.put(anOldTopic.getKey(), aNewTopic.getValue()); //il valore numerico relativo al topic aggiunto è quello presente nei topic della nuova belonging class

                }
            }
        }
        return belongingClassTopicsFinali;
    }

    private ClassBean createNewBelongingClass(MethodBean smellMethod, ClassBean oldBelongingClass) {
        String newBelongingClassTextContent = oldBelongingClass.getTextContent().replace(smellMethod.getTextContent(), "");
        return new ClassBean.Builder(oldBelongingClass.getFullQualifiedName(), newBelongingClassTextContent).build();
    }


    private JPanel getRadarMapFromNewEnviedClass(MethodBean smellMethod, ClassBean enviedClass) {
        String newTextContent = enviedClass.getTextContent() + smellMethod.getTextContent();
        ClassBean newEnviedClassBean = new ClassBean.Builder(enviedClass.getFullQualifiedName(), newTextContent).build();
        return radars.createRadarMapFromClassBean(newEnviedClassBean, "New Envied Class Topic");
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = new DialogWrapperAction("REFACTOR") {

            String message;
            Icon icon;

            @Override
            protected void doAction(ActionEvent actionEvent) {
                FeatureEnvyRefactoringStrategy featureEnvyRefactoringStrategy = new FeatureEnvyRefactoringStrategy(smellMethod, project);
                RefactoringManager refactoringManager = new RefactoringManager(featureEnvyRefactoringStrategy);

                WriteCommandAction.runWriteCommandAction(project, () -> {
                    try {
                        refactoringManager.executeRefactor();

                        ClassBean target = null;

                        for(PackageBean p : packageBeans){
                            for(ClassBean c : p.getClassList()){
                                if(c.getFullQualifiedName().equalsIgnoreCase(smellMethod.getBelongingClass().getFullQualifiedName())){
                                    target = c;
                                }
                            }
                        }

                        UpdateClassUtility.addImport(target, smellMethod.getEnviedClass());


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
                    try {
                        FileWriter f = new FileWriter(System.getProperty("user.home") + File.separator + ".casper" + File.separator + "refactoring.txt");
                        BufferedWriter out = new BufferedWriter(f);
                        out.write(textAreaContent.toString());
                    } catch (IOException e) {
                    }
                }

                Messages.showMessageDialog(message, "Outcome of refactoring", icon);
                close(0);
            }
        };

        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }
}
