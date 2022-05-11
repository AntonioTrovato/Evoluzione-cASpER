package it.unisa.casper.analysis.code_smell_detection.divergent_change;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.analysis.history_analysis_utility.PythonExeSingleton;
import it.unisa.casper.analysis.history_analysis_utility.RepositorySingleton;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryDivergentChangeStrategy  implements ClassSmellDetectionStrategy {

    private String pathToRepo;
    private final  String PATH_TO_DIVERGENT_CHANGE =  System.getProperty("user.home") + File.separator + ".casper" + File.separator + "HistoryAnaliysisScripts" + File.separator + "DivergentChange.py";
    private String pathToExe;
    private double threshold;

    @Override
    public boolean isSmelly(ClassBean aClass) {

        RepositorySingleton s = RepositorySingleton.getInstance(aClass);
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

        String nomeClasse = getClassName(aClass.getFullQualifiedName());
        String line = "";

        try {
            Process p = Runtime.getRuntime().exec( pathToExe + " " + PATH_TO_DIVERGENT_CHANGE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

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
        if(getResult(line, aClass)) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public HashMap<String, Double> getThresold(ClassBean aClass) {
        HashMap<String, Double> list = new HashMap<String, Double>();
        list.put("threshold", this.threshold);
        return list;
    }

    private String getClassName(String fullQualifiedName){
        String[] list = null;
        list = fullQualifiedName.split("\\.");
        return list[list.length - 1] + ".java";
    }

    private boolean getResult(String result, ClassBean aClass){

        if(result == null){
            this.threshold = 0;
            return false;
        }

        List<MethodBean> metodiClasse = aClass.getMethodList();
        List<List<MethodBean>> listaInsiemiMetodi = new ArrayList<>();

        String[] list = null;
        list = result.split(",");

        if(list[0].equalsIgnoreCase("true")) {
            for (int i = 1; i < list.length-1; i++) {
                String[] methods = list[i].split("-");
                List<MethodBean> insiemeMetodi = new ArrayList<>();
                for(int j = 1; j < methods.length; j++){
                    for(int x = 0; x < metodiClasse.size(); x++){
                        String[] temp = metodiClasse.get(x).getFullQualifiedName().split("\\.");
                        if(temp[temp.length-1].equalsIgnoreCase(methods[j])){
                           insiemeMetodi.add(metodiClasse.get(x));
                        }
                    }
                }
                listaInsiemiMetodi.add(insiemeMetodi);
            }
            this.threshold = Double.parseDouble(list[list.length - 1]);
            aClass.setDivergentChangeMethodsSet(listaInsiemiMetodi);
            return true;
        }else{
            this.threshold = 0;
            return false;
        }

    }
}
