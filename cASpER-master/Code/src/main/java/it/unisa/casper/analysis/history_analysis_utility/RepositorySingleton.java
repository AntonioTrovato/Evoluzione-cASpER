package it.unisa.casper.analysis.history_analysis_utility;

import it.unisa.casper.storage.beans.ClassBean;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class RepositorySingleton {

    private static volatile RepositorySingleton istance;
    private volatile Boolean isRepo;
    private volatile String pathToRepo;

    private RepositorySingleton(ClassBean aClass){
        this.isRepo = false;
        this.pathToRepo = "";
        checkRepo(aClass.getPathToFile());
    }

    public static RepositorySingleton getInstance(ClassBean aClass){
        if (istance == null){
            istance = new RepositorySingleton(aClass);
        }
        return istance;
    }


    private void checkRepo(String path) {
        /*
          Il path dato in input:
          C:/Users/faded/Desktop/Book-a-Book-master/test/src/classi_a_caso
        */

        File parentDir = new File(path);
        while(!parentDir.getAbsolutePath().equalsIgnoreCase(System.getenv("SystemDrive") + File.separator)) {
            //verifico se è una repository
           if(findGitFile(parentDir.getAbsolutePath())){
               return;
           }
            parentDir = new File(parentDir.getParent());

        }
    }

    private boolean findGitFile(String path){
        File[] list = new File(path).listFiles();
        if(list!=null)
            for (File fil : list){
                if(fil.getName().equals(".git")) {
                    this.isRepo = true;
                    this.pathToRepo = fil.getParent();
                    return true;
                }
            }
        return false;
    }

    public Boolean isRepo() {
        return isRepo;
    }

    public String getPathToRepo() {
        return pathToRepo;
    }
}



