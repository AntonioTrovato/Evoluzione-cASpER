package it.unisa.casper.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.table.JBTable;
import it.unisa.casper.analysis.code_smell.CodeSmell;
import it.unisa.casper.analysis.history_analysis_utility.HistoryAnalysisStartup;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;
import it.unisa.casper.structuralMetrics.CKMetrics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import src.main.java.it.unisa.casper.gui.StyleText;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class CheckProjectPage extends DialogWrapper {

    private DefaultTableModel model;

    private Project currentProject;
    private List<PackageBean> packages;
    private List<PackageBean> promiscuousPackageList;
    private List<MethodBean> featureEnvyList;
    private List<ClassBean> misplacedClassList;
    private List<ClassBean> blobList;
    private List<ClassBean> divergentChangeList;
    private List<ClassBean> parallelInheritanceList;
    private List<ClassBean> shotgunSurgeryList;
    private JPanel pannello;
    private JTextPane codeVisual;
    private JTable table;
    private DecimalFormat df = new DecimalFormat("0.000");
    private DecimalFormat df2 = new DecimalFormat("0");
    private DecimalFormat df3 = new DecimalFormat("0.0000");
    private JPanel contentPanel;
    private JPanel smellVisual;
    private JPanel panel;
    private JPanel textual;
    private JPanel structural;
    private JTextField valCoseno;
    private ArrayList<JTextField> valDipendenza;
    private JLabel soglia2;

    private JPanel centerPanel;
    private JPanel nuovo;
    private JPanel slider;
    private JSlider dipendenze;
    private JSlider coseno;
    private JPanel smell;

    private ArrayList<JPanel> smellPanel;
    private HashMap<String, JCheckBox> codeSmell = new HashMap<String, JCheckBox>();
    private HashMap<String, JCheckBox> algoritmi = new HashMap<String, JCheckBox>();
    private HashMap<String, Integer> threshold = new HashMap<String, Integer>();
    private ArrayList<JPanel> algo;

    private static ArrayList<String> smellName;
    private static ArrayList<String> blobThresholdName;
    private static ArrayList<String> promiscuousThresholdName;
    private int maxS = 0;
    private String algorithm;
    private double sogliaCoseno;
    private ArrayList<Integer> sogliaDipendenze;

    public CheckProjectPage(Project currentProj, List<PackageBean> packages, double sogliaCoseno, ArrayList<Integer> sogliaDipendenze, String algorithm) {
        super(true);
        smellName = new ArrayList<String>();
        smellName.add("Feature Envy");
        smellName.add("Misplaced Class");
        smellName.add("Blob");
        smellName.add("Promiscuous Package");
        //Nuovi code smell
        smellName.add("Divergent Change");
        smellName.add("Shotgun Surgery");
        smellName.add("Parallel Inheritance");

        blobThresholdName = new ArrayList<String>();
        blobThresholdName.add("LCOM");
        blobThresholdName.add("FeatureSUM");
        blobThresholdName.add("ELOC");
        promiscuousThresholdName = new ArrayList<String>();
        promiscuousThresholdName.add("InverseMIntraC");
        promiscuousThresholdName.add("MInterC");

        promiscuousPackageList = new ArrayList<PackageBean>();
        featureEnvyList = new ArrayList<MethodBean>();
        misplacedClassList = new ArrayList<ClassBean>();
        blobList = new ArrayList<ClassBean>();
        divergentChangeList = new ArrayList<ClassBean>();
        parallelInheritanceList = new ArrayList<ClassBean>();
        shotgunSurgeryList = new ArrayList<ClassBean>();

        this.currentProject = currentProj;
        this.packages = packages;
        this.algorithm = algorithm;
        this.sogliaCoseno = sogliaCoseno;
        this.sogliaDipendenze = sogliaDipendenze;
        setResizable(false);
        init();
        setTitle("CODE SMELL ANALYSIS");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        File f = new File(System.getProperty("user.home") + File.separator + ".casper" + File.separator + "refactoring.txt");
        if (f.exists()) {
            f.delete();
        }

        //I for annidati verificano quali sono i code smells di ogni package, classe e metodo e li inseriscono in una lista
        String name = "";
        for (PackageBean p : packages) {
            if (p.getAffectedSmell().size() != 0)
                promiscuousPackageList.add(p);
            for (ClassBean c : p.getClassList()) {
                for (CodeSmell smellC : c.getAffectedSmell()) {

                    if (!name.equals(smellC.getSmellName()))
                        switch (smellC.getSmellName()) {
                            case "Blob":
                                name = "Blob";
                                blobList.add(c);
                                break;
                            case "Misplaced Class":
                                name = "Misplaced Class";
                                misplacedClassList.add(c);
                                break;
                            case "Divergent Change":
                                name = "Divergent Change";
                                divergentChangeList.add(c);
                                break;
                            case "Shotgun Surgery":
                                name = "Shotgun Surgery";
                                shotgunSurgeryList.add(c);
                                break;
                            case "Parallel Inheritance":
                                name = "Parallel Inheritance";
                                parallelInheritanceList.add(c);
                                break;
                        }
                }
                name = "";
                for (MethodBean m : c.getMethodList()) {
                    if (m.getAffectedSmell().size() != 0)
                        featureEnvyList.add(m);
                }
            }
        }

        threshold = new HashMap<String, Integer>();
        int i = 0;
        while (i < blobThresholdName.size()) {
            threshold.put(blobThresholdName.get(i), sogliaDipendenze.get(i + 1));
            i++;
        }
        i = 0;
        while (i < promiscuousThresholdName.size()) {
            threshold.put(promiscuousThresholdName.get(i), sogliaDipendenze.get(i + blobThresholdName.size() + 1));
            i++;
        }

        for (PackageBean p : promiscuousPackageList) {
            for (CodeSmell s : p.getAffectedSmell()) {
                if (s.getIndex().containsKey("InverseMIntraC") && s.getIndex().containsKey("MInterC")) {
                    s.getIndex().put("InverseMIntraC", CKMetrics.computeMediumIntraConnectivity(p));
                    s.getIndex().put("MInterC", CKMetrics.computeMediumInterConnectivity(p, packages));
                }
            }
        }

        contentPanel = new JPanel(); //pannello principale
        contentPanel.setLayout(new BorderLayout(0, 0));
        smellVisual = new JPanel(); //pannello per visualizzare lista di smell
        codeVisual = new JTextPane(); //pannello per visualizzare il codice dello smell

        smellVisual.setPreferredSize(new Dimension(350, 800));

        //roba della combobox
        panel = new JPanel();
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);
        panel.setMaximumSize(maxSize);
        panel.setLayout(new BorderLayout(0, 0));
        //fine roba della combobox

        contentPanel.setLayout(new GridLayout(0, 2));//layout pannello principale
        contentPanel.setPreferredSize(new Dimension(1250, 900));
        smellVisual.setLayout(new BorderLayout(0, 0));
        smellVisual.add(panel, BorderLayout.NORTH);

        pannello = new JPanel();
        panel.add(pannello, BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        nuovo = new JPanel();
        centerPanel.add(nuovo);
        nuovo.setLayout(new BoxLayout(nuovo, BoxLayout.X_AXIS));
        smell = new JPanel();
        nuovo.add(smell);
        smell.setLayout(new BoxLayout(smell, BoxLayout.Y_AXIS));

        algo = new ArrayList<JPanel>();
        smellPanel = new ArrayList<JPanel>();
        for (i = 0; i < smellName.size(); i++) {
            smellPanel.add(new JPanel());
            smellPanel.get(i).setBorder(new EmptyBorder(0, 0, 10, 0));
            smellPanel.get(i).setLayout(new GridLayout(0, 2, 0, 0));
            smell.add(smellPanel.get(i));
            codeSmell.put(smellName.get(i), new JCheckBox(smellName.get(i)));
            smellPanel.get(i).add(codeSmell.get(smellName.get(i)));
            algo.add(new JPanel());
            if (smellName.get(i).equalsIgnoreCase("blob") || smellName.get(i).equalsIgnoreCase("promiscuous package")) {
                smellPanel.get(i).setBorder(new TitledBorder("* does not use dependencies"));
            }
            smellPanel.get(i).add(algo.get(i));
            algo.get(i).setLayout(new GridLayout(0, 1, 0, 0));

            if(smellName.get(i).equalsIgnoreCase("Divergent Change") || smellName.get(i).equalsIgnoreCase("Shotgun Surgery") || smellName.get(i).equalsIgnoreCase("Parallel Inheritance") ){

                algoritmi.put("history"+smellName.get(i).substring(0,1), new JCheckBox("History"));
                algo.get(i).add(algoritmi.get("history"+smellName.get(i).substring(0,1)));

            }

            if(smellName.get(i).equalsIgnoreCase("blob") || smellName.get(i).equalsIgnoreCase("Feature Envy")){

                    algoritmi.put("textual" + smellName.get(i).substring(0, 1), new JCheckBox("Textual"));
                    algo.get(i).add(algoritmi.get("textual" + smellName.get(i).substring(0, 1)));
                    algoritmi.put("structural" + smellName.get(i).substring(0, 1), new JCheckBox("Structural"));
                    algo.get(i).add(algoritmi.get("structural" + smellName.get(i).substring(0, 1)));
                    algoritmi.put("history"+smellName.get(i).substring(0,1), new JCheckBox("History"));
                    algo.get(i).add(algoritmi.get("history"+smellName.get(i).substring(0,1)));

                }

            if(smellName.get(i).equalsIgnoreCase("Promiscuous Package") || smellName.get(i).equalsIgnoreCase("Misplaced Class")){
                    algoritmi.put("textual" + smellName.get(i).substring(0, 1), new JCheckBox("Textual"));
                    algo.get(i).add(algoritmi.get("textual" + smellName.get(i).substring(0, 1)));
                    algoritmi.put("structural" + smellName.get(i).substring(0, 1), new JCheckBox("Structural"));
                    algo.get(i).add(algoritmi.get("structural" + smellName.get(i).substring(0, 1)));
                }



/*
            algoritmi.put("textual" + smellName.get(i).substring(0, 1), new JCheckBox("Textual"));
            algo.get(i).add(algoritmi.get("textual" + smellName.get(i).substring(0, 1)));
            algoritmi.put("structural" + smellName.get(i).substring(0, 1), new JCheckBox("Structural"));
            algo.get(i).add(algoritmi.get("structural" + smellName.get(i).substring(0, 1)));
            //History
            algoritmi.put("history" + smellName.get(i).substring(0,1), new JCheckBox("History"));
            algo.get(i).add(algoritmi.get("history" + smellName.get(i).substring(0,1)));
*/
        }

        for (JCheckBox c : codeSmell.values()) {
            c.setSelected(true);
            c.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    JCheckBox cb = (JCheckBox) a.getSource();
                    createTable();
                    table.repaint();
                }
            });
        }

        JCheckBox c;
        for (String s : algoritmi.keySet()) {

            c = algoritmi.get(s);
            c.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    createTable();
                    table.repaint();
                }
            });
            if (algorithm.equalsIgnoreCase("All")) {
                c.setSelected(true);
            } else {
                if ((s.contains("textual") && algorithm.equalsIgnoreCase("Textual")) || (s.contains("structural") && algorithm.equalsIgnoreCase("Structural"))) {
                    c.setSelected(true);
                }
            }

        }

        slider = new JPanel();
        nuovo.add(slider);
        slider.setLayout(new BoxLayout(slider, BoxLayout.Y_AXIS));

        textual = new JPanel();
        textual.setBorder(new TitledBorder("Textual"));
        slider.add(textual);
        textual.setLayout(new BoxLayout(textual, BoxLayout.Y_AXIS));

        JPanel bar1 = new JPanel();
        textual.add(bar1);
        JPanel s = new JPanel();
        JLabel soglia1 = new JLabel();
        soglia1.setText("coseno >= [" + sogliaCoseno + ";1]");
        textual.add(s);
        s.add(soglia1);

        coseno = new JSlider();
        coseno.setForeground(Color.WHITE);
        coseno.setFont(new Font("Arial", Font.PLAIN, 12));
        coseno.setPaintTicks(true);
        coseno.setMinorTickSpacing(10);
        coseno.setValue((int) (sogliaCoseno * 100));
        bar1.add(coseno);

        coseno.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                valCoseno.setText(String.valueOf(((double) coseno.getValue()) / 100));
                createTable();
                table.repaint();
            }
        });

        valCoseno = new JTextField();
        valCoseno.setText(String.valueOf(0));
        s.add(valCoseno);
        valCoseno.setColumns(5);

        valCoseno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent c) {

                try {
                    JTextField t = (JTextField) c.getSource();
                    double valore = Double.parseDouble(t.getText());
                    if (valore >= 0.0 && valore <= 1.0) {
                        coseno.setValue((int) (valore * 100));
                    } else {
                        if (valore < 0.0) {
                            coseno.setValue(0);
                            valCoseno.setText(0.0 + "");
                        } else {
                            if (valore > 1.0) {
                                coseno.setValue(100);
                                valCoseno.setText(1.0 + "");
                            } else {
                                coseno.setValue((int) valore);
                                valCoseno.setText(valore + "");
                            }
                        }
                        ;
                    }
                } catch (NumberFormatException e) {
                    coseno.setValue((int) sogliaCoseno * 100);
                    valCoseno.setText(sogliaCoseno + "");
                    JOptionPane.showMessageDialog(null, "Enter decimal values with \".\" [" + sogliaCoseno + ";1]", "Errore", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        structural = new JPanel();
        structural.setBorder(new TitledBorder("Structural"));
        slider.add(structural);
        structural.setLayout(new BoxLayout(structural, BoxLayout.Y_AXIS));

        JPanel bar2 = new JPanel();
        structural.add(bar2);
        JPanel s2 = new JPanel();
        soglia2 = new JLabel();
        structural.add(s2);
        s2.add(soglia2);

        dipendenze = new JSlider();
        dipendenze.setForeground(Color.WHITE);
        dipendenze.setFont(new Font("Arial", Font.PLAIN, 12));
        dipendenze.setValue(sogliaDipendenze.get(0));
        dipendenze.setPaintTicks(true);
        dipendenze.setMinorTickSpacing(1);
        dipendenze.setMinimum(0);
        bar2.add(dipendenze);

        dipendenze.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                try {
                    valDipendenza.get(0).setText(String.valueOf(dipendenze.getValue()));
                    createTable();
                    table.repaint();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Enter integer values", "", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        valDipendenza = new ArrayList<JTextField>();
        valDipendenza.add(new JTextField());
        s2.add(valDipendenza.get(0));
        valDipendenza.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent c) {
                JTextField t = (JTextField) c.getSource();
                try {
                    if (Integer.parseInt(t.getText()) < sogliaDipendenze.get(0)) {
                        t.setText(sogliaDipendenze.get(0) + "");
                        dipendenze.setValue(sogliaDipendenze.get(0));
                    } else {
                        dipendenze.setValue(Integer.parseInt(t.getText()));
                    }
                } catch (NumberFormatException e) {
                    t.setText(sogliaDipendenze.get(0) + "");
                    valDipendenza.get(0).setText(sogliaDipendenze.get(0) + "");
                    JOptionPane.showMessageDialog(null, "Minimum threshold not respected");
                }
                ;
            }
        });
        valDipendenza.get(0).setText(dipendenze.getValue() + "");

        int[] list1 = new int[3];
        list1[0] = 350;
        list1[1] = 20;
        list1[2] = 500;
        int[] list2 = new int[2];
        list2[0] = 50;
        list2[1] = 50;

        JPanel blobThreshold = sectionMetric(0, blobThresholdName, list1);
        JPanel promiscuousThreshold = sectionMetric(blobThresholdName.size(), promiscuousThresholdName, list2);

        blobThreshold.setBorder(new TitledBorder("Blob"));
        promiscuousThreshold.setBorder(new TitledBorder("Promiscuous package"));

        structural.add(blobThreshold);
        structural.add(promiscuousThreshold);
        pannello.add(centerPanel);
        createTable();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() != false) {
                    setArea();
                }
            }
        });

        JPanel text = new JPanel();
        text.setLayout(new BorderLayout(0, 0));
        text.setBorder(new TitledBorder("Text content"));
        JPanel tab = new JPanel();
        tab.setLayout(new BorderLayout(0, 0));
        tab.setBorder(new TitledBorder("List"));
        contentPanel.setLayout(new GridLayout(0, 2));//layout pannello principale

        JScrollPane scroll = new JScrollPane(table);
        tab.add(scroll, BorderLayout.CENTER);// aggiunta tabella smell
        smellVisual.add(tab);

        contentPanel.add(smellVisual);

        codeVisual.setEditable(false);
        JScrollPane scroll2 = new JScrollPane(codeVisual);
        text.add(scroll2, BorderLayout.CENTER);
        contentPanel.add(text);

        return contentPanel;
    }

    private JPanel sectionMetric(int pos, ArrayList<String> ThresholdName, int[] valori) {

        JPanel panelThreshold = new JPanel();
        JPanel livello;
        JLabel scritta, vincolo;
        int j;
        panelThreshold.setLayout(new BoxLayout(panelThreshold, BoxLayout.Y_AXIS));
        for (j = 1; j <= ThresholdName.size(); j++) {

            valDipendenza.add(new JTextField());
            if (ThresholdName.get(j - 1).equalsIgnoreCase("inversemintrac") || ThresholdName.get(j - 1).equalsIgnoreCase("minterc")) {
                valDipendenza.get(j + pos).setText(((double) threshold.get(ThresholdName.get(j - 1))) / 100 + "");
            } else {
                valDipendenza.get(j + pos).setText(df2.format(threshold.get(ThresholdName.get(j - 1))));
            }

            int finalJ = j;
            valDipendenza.get(j + pos).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent c) {
                    JTextField f = (JTextField) c.getSource();
                    try {
                        if (ThresholdName.get(finalJ - 1).equalsIgnoreCase("inversemintrac") || ThresholdName.get(finalJ - 1).equalsIgnoreCase("minterc")) {
                            if (Double.parseDouble(f.getText()) < ((double) threshold.get(ThresholdName.get(finalJ - 1))) / 100) {
                                f.setText(((double) threshold.get(ThresholdName.get(finalJ - 1))) / 100 + "");
                                JOptionPane.showMessageDialog(null, "Minimum threshold not respected");
                            } else {
                                if (Double.parseDouble(f.getText()) > 1.0) {
                                    f.setText("1");
                                    JOptionPane.showMessageDialog(null, "Minimum threshold not respected");
                                }
                            }
                        } else {
                            if (Integer.parseInt(f.getText()) < threshold.get(ThresholdName.get(finalJ - 1))) {
                                f.setText(threshold.get(ThresholdName.get(finalJ - 1)) + "");
                                JOptionPane.showMessageDialog(null, "Minimum threshold not respected");
                            }
                        }
                    } catch (NumberFormatException e) {
                        if (ThresholdName.get(finalJ - 1).equalsIgnoreCase("inversemintrac") || ThresholdName.get(finalJ - 1).equalsIgnoreCase("minterc")) {
                            f.setText(((double) threshold.get(ThresholdName.get(finalJ - 1))) / 100 + "");
                        } else {
                            f.setText(threshold.get(ThresholdName.get(finalJ - 1)) + "");
                        }
                        JOptionPane.showMessageDialog(null, "Format not allowed");
                    }

                    createTable();
                    table.repaint();
                }
            });

            livello = new JPanel();
            livello.setLayout(new GridLayout(2, 2, 0, 0));
            scritta = new JLabel(ThresholdName.get(j - 1));
            scritta.setHorizontalAlignment(SwingConstants.CENTER);
            livello.add(scritta);
            livello.add(valDipendenza.get(j + pos));
            if (ThresholdName.get(j - 1).equalsIgnoreCase("inversemintrac") || ThresholdName.get(j - 1).equalsIgnoreCase("minterc")) {
                vincolo = new JLabel("value in [" + ((double) threshold.get(ThresholdName.get(finalJ - 1))) / 100 + ";1]");
            } else {
                vincolo = new JLabel("value min = " + threshold.get(ThresholdName.get(j - 1)));
            }
            vincolo.setHorizontalAlignment(SwingConstants.CENTER);
            livello.add(vincolo);
            panelThreshold.add(livello);
        }

        return panelThreshold;
    }

    @NotNull
    @Override
    //Apre le GUI per i singoli smell
    protected Action[] createActions() {
        Action okAction = new DialogWrapperAction("INSPECT") {

            @Override
            protected void doAction(ActionEvent actionEvent) {
                try {
                    String whatToReturn; //fullqualified name dell'elemento selezionato nella tabella
                    String whereToSearch; //tipo di smell, indice della lista dove cercare il bean
                    whatToReturn = (String) table.getValueAt(table.getSelectedRow(), 0);
                    whereToSearch = (String) table.getValueAt(table.getSelectedRow(), 1);
                    if (whereToSearch.equalsIgnoreCase("blob")) {
                        for (ClassBean c : blobList) {
                            if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                DialogWrapper blob = new BlobPage(c, currentProject);
                                blob.show();
                            }
                        }
                    }

                    if (whereToSearch.equalsIgnoreCase("feature envy")) {
                        for (MethodBean m : featureEnvyList) {
                            if (m.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                DialogWrapper feat = new FeatureEnvyPage(m, currentProject, packages);
                                feat.show();
                            }
                        }
                    }

                    if (whereToSearch.equalsIgnoreCase("promiscuous package")) {
                        for (PackageBean p : promiscuousPackageList) {
                            if (p.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                DialogWrapper prom = new PromiscuousPackagePage(p, packages, currentProject);
                                prom.show();
                            }
                        }
                    }

                    if (whereToSearch.equalsIgnoreCase("misplaced class")) {
                        for (ClassBean c : misplacedClassList) {
                            if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                DialogWrapper mis = new MisplacedClassPage(c, currentProject);
                                mis.show();
                            }
                        }
                    }

                    if (whereToSearch.equalsIgnoreCase("Divergent Change")) {
                        for (ClassBean c : divergentChangeList) {
                            if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                DialogWrapper divergentChange = new DivergentChangePage(c, currentProject);
                                divergentChange.show();
                            }
                        }
                    }

                    if (whereToSearch.equalsIgnoreCase("Shotgun Surgery")) {
                        for (ClassBean c : shotgunSurgeryList) {
                            if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                DialogWrapper shotgunSurgery = new ShotgunSurgeryPage(c, currentProject);
                                shotgunSurgery.show();
                            }
                        }
                    }

                    if (whereToSearch.equalsIgnoreCase("Parallel Inheritance")) {
                        for (ClassBean c : parallelInheritanceList) {
                            if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                DialogWrapper parallelInheritance = new ParallelInheritancePage(c, currentProject, packages);
                                parallelInheritance.show();
                            }
                        }
                    }

                } catch (ArrayIndexOutOfBoundsException ex) {
                    String message = "Select an item";
                    Messages.showMessageDialog(message, "Warning", Messages.getWarningIcon());
                }

                File f = new File(System.getProperty("user.home") + File.separator + ".casper" + File.separator + "refactoring.txt");
                if (f.exists()) {
                    f.delete();
                    close(0);
                }
            }
        };

        return new Action[]{okAction, new DialogWrapperExitAction("EXIT", 0)};
    }

    //crea la tabella in basso a sinistra
    private void createTable() {


        Vector<String> columnNames = new Vector<>();
        columnNames.add("Member name");
        columnNames.add("Smell detected");
        columnNames.add("Textual algorithm");
        columnNames.add("Structural algorithm");
        //History analysis
        columnNames.add("History algorithm");

        columnNames.add("priority");
        model = new DefaultTableModel(columnNames, 0);

        if (blobList.size() != 0) {
            if (codeSmell.get("Blob").isSelected()) {
                for (ClassBean c : blobList) {
                    gestione(c.getAffectedSmell(), "Blob", c.getFullQualifiedName());
                }
            }
        }

//        if(divergentChangeList.size() != 0){
//            if(codeSmell.get("Divergent Change").isSelected()){
//                for (ClassBean c : divergentChangeList){
//                    gestione(c.getAffectedSmell(), "Divergent Change", c.getFullQualifiedName());
//                }
//            }
//        }

//        if(shotgunSurgeryList.size() != 0){
//            if(codeSmell.get("Shotgun Surgery").isSelected()){
//                for (ClassBean c : shotgunSurgeryList){
//                    gestione(c.getAffectedSmell(), "Shotgun Surgery", c.getFullQualifiedName());
//                }
//            }
//        }

//        if(parallelInheritanceList.size() != 0){
//            if(codeSmell.get("Parallel Inheritance").isSelected()){
//                for(ClassBean c : parallelInheritanceList){
//                    gestione(c.getAffectedSmell(), "Parallel Inheritance", c.getFullQualifiedName());
//                }
//            }
//        }

        if (misplacedClassList.size() != 0) {
            if (codeSmell.get("Misplaced Class").isSelected()) {
                for (ClassBean c : misplacedClassList) {
                    gestione(c.getAffectedSmell(), "Misplaced Class", c.getFullQualifiedName());
                }
            }
        }

        if (promiscuousPackageList.size() != 0) {
            if (codeSmell.get("Promiscuous Package").isSelected()) {
                for (PackageBean pp : promiscuousPackageList) {
                    gestione(pp.getAffectedSmell(), "Promiscuous Package", pp.getFullQualifiedName());
                }
            }
        }

        if (featureEnvyList.size() != 0) {
            if (codeSmell.get("Feature Envy").isSelected()) {
                for (MethodBean m : featureEnvyList) {
                    gestione(m.getAffectedSmell(), "Feature Envy", m.getFullQualifiedName());
                }
            }
        }

        if (this.table == null) {
            JTable table = new JBTable();
            this.table = table;
        }

        soglia2.setText("dipendenze >= [" + "0" + "-" + maxS + "]");
        dipendenze.setMaximum(maxS);
        this.table.setModel(model);
        table.setDefaultEditor(Object.class, null);

    }

    private void gestione(List<CodeSmell> list, String codeSmell, String bean) {

        boolean basso = false;
        int alto = 0;
        int complessita = 0;
        Vector<String> tableItem;
        String used;

        double cos = 0.0, indice = 0.0;
        int dip = 0;
        boolean controllo = false;
        int i = 0;

        for (CodeSmell smell : list) {

            if (smell.getSmellName().equalsIgnoreCase(codeSmell)) {
                used = smell.getAlgoritmsUsed();
                tableItem = new Vector<String>();

                HashMap<String, Double> listThreschold = smell.getIndex();
                if (  algoritmi.get("textual" + codeSmell.substring(0, 1)) != null  && algoritmi.get("textual" + codeSmell.substring(0, 1)).isSelected() && listThreschold.get("coseno") != null) {
                    if (used.equalsIgnoreCase("textual") && Double.parseDouble(valCoseno.getText()) <= listThreschold.get("coseno")) {
                        complessita++;
                        if (cos >= sogliaCoseno + (sogliaCoseno * 0.10)) {
                            complessita += 2;
                        }
                        indice = Double.parseDouble(valCoseno.getText());
                        cos = listThreschold.get("coseno");
                        if (cos < sogliaCoseno + (sogliaCoseno * 0.10)) {
                            basso = true;
                        }
                        ;
                        if (cos >= 0.75) {
                            alto++;
                        }
                        ;
                    } else {
                        controllo = true;
                    }

                } else {
                    controllo = true;
                }
                if (!(codeSmell.equalsIgnoreCase("Promiscuous package") || codeSmell.equalsIgnoreCase("Blob")) && used.equalsIgnoreCase("structural") && Double.parseDouble(valDipendenza.get(0).getText()) <= listThreschold.get("dipendenza")) {

                    complessita += 2;
                    indice = Double.parseDouble(valDipendenza.get(0).getText());
                    dip = listThreschold.get("dipendenza").intValue();

                    if (dip <= sogliaDipendenze.get(0)) {
                        basso = true;
                    }
                    ;
                    if (dip >= (int) (maxS - (maxS * 0.25))) {
                        alto++;
                    }
                    ;
                    if (dip >= maxS) {
                        maxS = dip;
                    }

                } else {
                    if (smell.getSmellName().equalsIgnoreCase("blob") && algoritmi.get("structural" + codeSmell.substring(0, 1)).isSelected() && used.equalsIgnoreCase("structural")) {
                        Double lcom = Double.parseDouble(valDipendenza.get(1).getText());
                        Double fsum = Double.parseDouble(valDipendenza.get(2).getText());
                        Double eloc = Double.parseDouble(valDipendenza.get(3).getText());
                        if (lcom <= listThreschold.get("LCOM") || fsum <= listThreschold.get("featureSum") || eloc <= listThreschold.get("ELOC")) {
                            complessita += 2;
                            alto++;
                            if (lcom == listThreschold.get("LCOM") || fsum == listThreschold.get("featureSum") || eloc == listThreschold.get("ELOC")) {
                                basso = true;
                            }
                        }

                    } else {
                        if (smell.getSmellName().equalsIgnoreCase("promiscuous package") && algoritmi.get("structural" + codeSmell.substring(0, 1)).isSelected() && used.equalsIgnoreCase("structural")) {
                            Double inversemintrac = Double.parseDouble(valDipendenza.get(4).getText());
                            Double minterc = Double.parseDouble(valDipendenza.get(5).getText());
                            if (inversemintrac <= listThreschold.get("InverseMIntraC") || minterc <= listThreschold.get("MInterC")) {
                                complessita += 2;
                                alto++;
                                if (listThreschold.get("InverseMIntraC") >= 0.75 || listThreschold.get("MInterC") >= 0.75) {
                                    alto++;
                                }
                                if (inversemintrac == listThreschold.get("InverseMIntraC") && minterc == listThreschold.get("MInterC")) {
                                    basso = true;
                                }
                            } else {
                                controllo = true;
                            }
                        }
                    }
                }

                if (i + 1 >= list.size() || !list.get(i + 1).getSmellName().equalsIgnoreCase(smell.getSmellName())) {

                    boolean prima, seconda, terza;
                    prima = ((cos != 0.0 || dip != 0) && ((listThreschold.get("coseno") != null && indice <= listThreschold.get("coseno") && algoritmi.get("textual" + codeSmell.substring(0, 1)).isSelected()) ||
                            (listThreschold.get("dipendenza") != null && indice <= listThreschold.get("dipendenza") && algoritmi.get("structural" + codeSmell.substring(0, 1)).isSelected())));
                    seconda = (smell.getSmellName().equalsIgnoreCase("Blob") && smell.getAlgoritmsUsed().equalsIgnoreCase("structural") && algoritmi.get("structural" + codeSmell.substring(0, 1)).isSelected() &&
                            (Double.parseDouble(valDipendenza.get(1).getText()) <= listThreschold.get("LCOM") || Double.parseDouble(valDipendenza.get(2).getText()) <= listThreschold.get("featureSum") || Double.parseDouble(valDipendenza.get(3).getText()) <= listThreschold.get("ELOC")));
                    terza = (smell.getSmellName().equalsIgnoreCase("Promiscuous package") && smell.getAlgoritmsUsed().equalsIgnoreCase("structural") && algoritmi.get("structural" + codeSmell.substring(0, 1)).isSelected() &&
                            (Double.parseDouble(valDipendenza.get(4).getText()) <= listThreschold.get("InverseMIntraC") || Double.parseDouble(valDipendenza.get(5).getText()) <= listThreschold.get("MInterC")));

                    if (prima || seconda || terza) {
                        tableItem.add(bean);
                        tableItem.add(smell.getSmellName());
                        if (cos == 0.0) {
                            tableItem.add("---");
                        } else {
                            tableItem.add(df.format(cos));
                        }

                        if (dip == 0) {
                            if (smell.getSmellName().equalsIgnoreCase("Blob") && algoritmi.get("structural" + codeSmell.substring(0, 1)).isSelected() && alto > 0) {
                                HashMap<String, Double> soglie = smell.getIndex();
                                tableItem.add(soglie.get("LCOM") + "-" + soglie.get("featureSum") + "-" + soglie.get("ELOC"));
                            } else {
                                if (smell.getSmellName().equalsIgnoreCase("Promiscuous package") && algoritmi.get("structural" + codeSmell.substring(0, 1)).isSelected() && alto > 0) {
                                    HashMap<String, Double> soglie = smell.getIndex();
                                    tableItem.add(df3.format(soglie.get("InverseMIntraC")) + "-" + df3.format(soglie.get("MInterC")));
                                } else {
                                    tableItem.add("---");
                                }
                            }
                            ;
                        } else {
                            tableItem.add(df2.format(dip));
                        }
                        ;
                        tableItem.add(prioritySmell(controllo, complessita, basso, alto));
                        //History
                        tableItem.insertElementAt("---",4);

                        model.addRow(tableItem);
                        cos = 0.0;
                        dip = 0;
                        complessita = 0;
                        alto = 0;
                        basso = false;
                        controllo = false;
                    }
                    indice = 0.0;
                }
                //History
                if(used.equalsIgnoreCase("history") && algoritmi.get("history" + codeSmell.substring(0, 1)).isSelected()){
                    HashMap<String, Double> threshold = smell.getIndex();

                    tableItem.add(bean);
                    tableItem.add(smell.getSmellName());
                    tableItem.add("---");
                    tableItem.add("---");
                    tableItem.add(""+threshold.get("threshold"));
                    tableItem.add(HistoryAnalysisStartup.historyPriority(smell));
                    model.addRow(tableItem);
                }
            }

            i++;
        }

    }

    private String prioritySmell(boolean controllo, int complessita, boolean basso, int alto) {

        if (complessita <= 2 && !controllo && alto < 1) {
            return "low";
        } else {
            if (!basso) {
                switch (alto) {
                    case 1:
                        return "high";
                    case 2:
                        return "urgent";
                    default:
                        return "medium";
                }
            }
            return "medium";
        }
    }

    //scrive il contenuto dell'elemento selezionato in LIST nella tabella TEXT CONTENT
    private void setArea() {
        String whatToReturn; //fullqualified name dell'elemento selezionato nella tabella
        String whereToSearch; //tipo di smell, indice della lista dove cercare il bean
        try {
            whatToReturn = (String) table.getValueAt(table.getSelectedRow(), 0);
            whereToSearch = (String) table.getValueAt(table.getSelectedRow(), 1);
            String textContent = null;

            if (whereToSearch.equalsIgnoreCase("blob")) {
                for (ClassBean c : blobList) {
                    if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                        textContent = c.getTextContent();
                    }
                }
            } else {
                if (whereToSearch.equalsIgnoreCase("feature envy")) {
                    for (MethodBean m : featureEnvyList) {
                        if (m.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                            textContent = m.getTextContent();
                            textContent = textContent + "\n\nThis method envies the class: " + m.getEnviedClass().getFullQualifiedName() + "\n";
                            textContent = textContent + "\n" + m.getEnviedClass().getTextContent();
                        }
                    }
                } else {
                    if (whereToSearch.equalsIgnoreCase("promiscuous package")) {
                        int i = 0;
                        textContent = "";
                        for (PackageBean p : promiscuousPackageList) {
                            if (p.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                for (ClassBean c : p.getClassList()) {
                                    if (i < p.getClassList().size())
                                        textContent += "-------------------------------------------" + c.getFullQualifiedName() + "-------------------------------------------\n";
                                    i++;
                                    textContent += c.getTextContent();
                                }
                            }
                        }
                    } else {
                        if (whereToSearch.equalsIgnoreCase("misplaced class")) {
                            for (ClassBean c : misplacedClassList) {
                                if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                    textContent = c.getTextContent();
                                }
                            }
                        } else{
                            if(whereToSearch.equalsIgnoreCase("Divergent Change")){
                                for (ClassBean c : divergentChangeList) {
                                    if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                        textContent = c.getTextContent();
                                    }
                                }
                            }else{
                                if(whereToSearch.equalsIgnoreCase("Shotgun Surgery")){
                                    for (ClassBean c : shotgunSurgeryList) {
                                        if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                            textContent = c.getTextContent() + "-------------------------------------------\n";
                                            for(ClassBean classe : c.getShotgunSurgeryHittedClasses()){
                                                textContent = textContent + classe.getTextContent() + "-------------------------------------------\n";
                                            }

                                        }
                                    }
                                }else{
                                    if(whereToSearch.equalsIgnoreCase("Parallel Inheritance")){
                                        for (ClassBean c : parallelInheritanceList) {
                                            if (c.getFullQualifiedName().equalsIgnoreCase(whatToReturn)) {
                                                textContent = c.getTextContent();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            StyleText generator = new StyleText();
            codeVisual.setStyledDocument(generator.createDocument(textContent));
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
    }
}