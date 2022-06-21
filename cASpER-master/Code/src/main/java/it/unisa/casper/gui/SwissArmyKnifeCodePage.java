package it.unisa.casper.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import it.unisa.casper.gui.radarMap.RadarMapUtils;
import it.unisa.casper.gui.radarMap.RadarMapUtilsAdapter;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.structuralMetrics.CKMetrics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import src.main.java.it.unisa.casper.gui.StyleText;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class SwissArmyKnifeCodePage extends DialogWrapper implements AbstractCodeSmellGUI {

    private RadarMapUtils radarMapUtils;//roba che serve per le radar map

    private ClassBean classBeanSpaghettiCode;            //ClassBean sul quale avviene l'analisi

    private Project project;
    private JTextPane area;                     //area di testo dove viene mostrato in dettaglio il codice del CodeSmell selezionato
    private JPanel contentPanel;                //panel che raggruppa tutti gli elementi
    private JPanel panelRadarMapMaster;         //panel che ingloba la radar map
    private JPanel panelRadarMap;
    private JPanel panelMetric;                 //panel per le metriche
    private JPanel panelButton;                 //panel che raggruppa i bottoni
    private JPanel panelWest;                   //panel che raggruppa gli elementi a sinistra
    private JPanel panelEast;                   //panel che raggruppa gli elementi a destra
    private JPanel panelGrid2;                  //panel inserito nella seconda cella del gridLayout
    private JBTable table;                      //tabella dove sono visualizzati i codeSmell



    private boolean errorOccured;               //serve per determinare se qualcosa è andato storto

    public SwissArmyKnifeCodePage(ClassBean classBeanSpaghettiCode, Project project) {
        super(true);
        this.classBeanSpaghettiCode = classBeanSpaghettiCode;
        this.project = project;
        this.errorOccured = false;
        setResizable(false);
        init();
        setTitle("SWISS ARMY KNIFE PAGE");
    }

    @Nullable
    @Override
    public JComponent createCenterPanel() {

        radarMapUtils = new RadarMapUtilsAdapter();

        panelRadarMap = radarMapUtils.createRadarMapFromClassBean(classBeanSpaghettiCode, classBeanSpaghettiCode.getFullQualifiedName());

        //INIZIALIZZO I PANEL
        contentPanel = new JPanel();            //pannello principale
        panelButton = new JPanel();             //pannello dei bottoni
        panelRadarMapMaster = new JPanel();     //pannello che ingloba le radarMap
        panelMetric = new JPanel();             //pannello che ingloba le metriche
        panelWest = new JPanel();               //pannello che ingloba gli elementi di sinistra
        panelEast = new JPanel();               //pannello che ingloba gli elementi di destra

        //INIZIALIZZO LA TABELLA E LA TEXT AREA
        area = new JTextPane();                 //text area dove viene visualizzato il codice in esame
        table = new JBTable();                  //tabella dove sono presenti gli smell da prendere in esame

        //SETTO TESTO NELLA TEXT AREA
        JPanel app = new JPanel();
        app.setLayout(new BorderLayout(0, 0));
        app.setBorder(new TitledBorder("Text content"));
        StyleText generator = new StyleText();
        area.setStyledDocument(generator.createDocument(classBeanSpaghettiCode.getTextContent()));

        //SETTO LA TABELLA PER LE METRICHE
        table = new JBTable();
        Vector<String> tableHeaders = new Vector<>();
        tableHeaders.add("NI");


        Vector<String> tableElemet = new Vector<>();
        tableElemet.add(classBeanSpaghettiCode.getClassiImplementate().size()+ "");


        DefaultTableModel model = new DefaultTableModel(tableHeaders, 0);
        model.addRow(tableElemet);

        table.setModel(model);

        //SETTO I LAYOUT DEI PANEL
        panelButton.setLayout(new FlowLayout());
        panelRadarMapMaster.setLayout(new BorderLayout());
        panelWest.setLayout(new GridLayout(2, 1));
        panelEast.setLayout(new BorderLayout());
        panelMetric.setLayout((new BorderLayout()));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

        panelGrid2 = new JPanel();
        panelGrid2.setLayout(new BorderLayout());


        panelRadarMapMaster.add(panelRadarMap, BorderLayout.CENTER);

        panelMetric.setBorder(new TitledBorder("Metrics"));
        panelMetric.add(new JBScrollPane(table));
        table.setFillsViewportHeight(true);
        panelGrid2.add(panelMetric, BorderLayout.CENTER);
        panelGrid2.add(panelButton, BorderLayout.SOUTH);

        panelWest.add(panelRadarMapMaster);
        panelWest.add(panelGrid2);

        contentPanel.add(panelWest);
        JScrollPane scroll = new JScrollPane(area);
        app.add(scroll, BorderLayout.CENTER);
        contentPanel.add(app);

        contentPanel.setPreferredSize(new Dimension(1050, 900));

        return contentPanel;
    }

    @NotNull
    @Override
    public Action[] createActions() {
        Action okAction = new DialogWrapperAction("FIND SOLUTION") {

            String message;

            @Override
            protected void doAction(ActionEvent actionEvent) {
                    Messages.showMessageDialog(message, "Oh!No!", Messages.getErrorIcon());
            }
        };

        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }
}