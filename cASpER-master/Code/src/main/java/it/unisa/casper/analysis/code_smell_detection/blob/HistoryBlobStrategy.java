package it.unisa.casper.analysis.code_smell_detection.blob;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.analysis.history_analysis_utility.HistoryAnalysisStartup;
import it.unisa.casper.analysis.history_analysis_utility.PythonExeSingleton;
import it.unisa.casper.analysis.history_analysis_utility.RepositorySingleton;
import it.unisa.casper.storage.beans.ClassBean;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.HashMap;

public class HistoryBlobStrategy implements ClassSmellDetectionStrategy {

    // C:\\Users\\faded\\PycharmProjects\\CodeSmellsDetection\\blob.py
    private String pathToRepo;
    private final  String PATH_TO_BLOB =  System.getProperty("user.home") + File.separator + ".casper" + File.separator + "HistoryAnaliysisScripts" + File.separator + "blob.py";
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
            Process p = Runtime.getRuntime().exec( pathToExe + " " + PATH_TO_BLOB);
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
        if(getResult(line)) {
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

    private boolean getResult(String result){

        if(result == null){
            this.threshold = 0;
            return false;
        }

        String[] list = null;
        list = result.split(",");
        if(list[0].equalsIgnoreCase("true")){
            this.threshold = Double.parseDouble(list[1]);
            return true;
        }else{
            this.threshold = 0;
            return false;
        }
    }
}
