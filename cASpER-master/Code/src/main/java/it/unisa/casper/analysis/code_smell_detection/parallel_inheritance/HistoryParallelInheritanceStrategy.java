package it.unisa.casper.analysis.code_smell_detection.parallel_inheritance;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.analysis.history_analysis_utility.PythonExeSingleton;
import it.unisa.casper.analysis.history_analysis_utility.RepositorySingleton;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.PackageBean;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class HistoryParallelInheritanceStrategy implements ClassSmellDetectionStrategy {

    private List<PackageBean> systemPackages;
    private String pathToRepo;
    private final  String PATH_TO_PARALLEL_INHERITANCE =  System.getProperty("user.home") + File.separator + ".casper" + File.separator + "HistoryAnaliysisScripts" + File.separator + "ParallelInheritance.py";
    private String pathToExe;
    private double threshold;

    public HistoryParallelInheritanceStrategy(List<PackageBean> systemPackages) {
        this.systemPackages = systemPackages;
    }

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
            Process p = Runtime.getRuntime().exec( pathToExe + " " + PATH_TO_PARALLEL_INHERITANCE);
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

        String[] list = null;
        list = result.split(",");


        if(list[0].equalsIgnoreCase("true")){
            for (PackageBean packageBean : systemPackages) {
                for (ClassBean classBean : packageBean.getClassList()) {

                    for(int j = 1; j < list.length -1 ; j++){
                        if(getClassName(classBean).equalsIgnoreCase(list[j])){
                            String aClassName = getClassName(aClass);
                            if(!aClassName.equalsIgnoreCase(list[j])){
                                aClass.setParallelInheritanceClass(classBean);
                            }

                        }
                    }
                }
            }

            ClassBean superClass = getSuperClassBean(aClass.getSuperclass());
            this.threshold = superClass.getMethodList().size() + superClass.getInstanceVariablesList().size();
            return true;
        }else{
            this.threshold = 0;
            return false;
        }
    }

    private String getClassName(ClassBean classBean){
        String[] tmpArray = classBean.getFullQualifiedName().split("\\.");
        String tmp = tmpArray[tmpArray.length-1] + ".java";
        return tmp;
    }

    private ClassBean getSuperClassBean(String name){
        for(PackageBean p : systemPackages){
            for(ClassBean c : p.getClassList()){
                if(c.getFullQualifiedName().equalsIgnoreCase(name)){
                    return c;
                }
            }
        }
        return null;
    }
}
