package it.unisa.casper.analysis.code_smell_detection.feature_envy;

import it.unisa.casper.analysis.code_smell_detection.strategy.MethodSmellDetectionStrategy;
import it.unisa.casper.analysis.history_analysis_utility.PythonExeSingleton;
import it.unisa.casper.analysis.history_analysis_utility.RepositorySingleton;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class HistoryFeatureEnvyStrategy implements MethodSmellDetectionStrategy {

    private List<PackageBean> systemPackages;
    private String pathToRepo;
    private final  String PATH_TO_FEATURE_ENVY =  System.getProperty("user.home") + File.separator + ".casper" + File.separator + "HistoryAnaliysisScripts" + File.separator + "FeatureEnvy.py";
    private String pathToExe;
    private double threshold;
    private String nomeClasseInvidiosa;

    public HistoryFeatureEnvyStrategy(List<PackageBean> systemPackages) {
        this.systemPackages = systemPackages;
    }

    @Override
    public boolean isSmelly(MethodBean aMethod) {

        RepositorySingleton s = RepositorySingleton.getInstance(aMethod.getBelongingClass());
        if(s.isRepo()) {
            this.pathToRepo = s.getPathToRepo();
        }else{
            return false;
        }

        PythonExeSingleton singleton = PythonExeSingleton.getIstance("");
        if(singleton.isSet()){
            this.pathToExe = singleton.getPathToExe();
        }else{
            return false;
        }

        String nomeClasse = getClassName(aMethod.getBelongingClass().getFullQualifiedName());
        String nomeMetodo = getMethodName(aMethod.getFullQualifiedName());
        String line = "";


        try {
            Process p = Runtime.getRuntime().exec( pathToExe + " " + PATH_TO_FEATURE_ENVY);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            //INVIA AL PROCESSO IL NOME DEL METODO
            writer.write(nomeMetodo+"\n");
            writer.flush();

            //INVIA AL PROCESSO IL NOME DELLA CLASSE
            writer.write(nomeClasse+"\n");
            writer.flush();

            //Invia path della repo
            writer.write(this.pathToRepo+"\n");
            writer.flush();


            //leggo il risultato
            line = reader.readLine();

        }catch (Exception e){
            e.printStackTrace();
        }

        //elaboro il risultato
        if(getResult(line)){
            setClasseInvidiosa(aMethod);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public HashMap<String, Double> getThresold(MethodBean aMethod) {
        HashMap<String, Double> list = new HashMap<String, Double>();
        list.put("threshold", this.threshold);
        return list;
    }

    private String getClassName(String fullQualifiedName){
        String[] list = null;
        list = fullQualifiedName.split("\\.");
        return list[list.length - 1] + ".java";
    }

    private String getMethodName(String fullQualifiedName){
        String[] list = null;
        list = fullQualifiedName.split("\\.");
        return list[list.length - 1];
    }


    private boolean getResult(String result){

        if(result == null){
            this.threshold = 0;
            return false;
        }

        String[] list = null;
        list = result.split(",");
        if(list[0].equalsIgnoreCase("true")){
            this.nomeClasseInvidiosa = list[1];
            this.threshold = Double.parseDouble(list[2]);
            return true;
        }else{
            this.threshold = 0;
            return false;
        }
    }

    private void setClasseInvidiosa(MethodBean m){

        for (PackageBean packageBean : systemPackages) {
            for (ClassBean classBean : packageBean.getClassList()) {
                String tmp = classBean.getFullQualifiedName()+".java";
                if (tmp.contains(nomeClasseInvidiosa)) {
                    m.setEnviedClass(classBean);
                }
            }
        }
    }

}
