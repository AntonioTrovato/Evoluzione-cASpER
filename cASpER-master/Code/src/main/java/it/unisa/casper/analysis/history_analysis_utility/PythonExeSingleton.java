package it.unisa.casper.analysis.history_analysis_utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PythonExeSingleton {

    private static volatile PythonExeSingleton istance;
    private volatile Boolean isSet;
    private volatile String pathToExe;

    private PythonExeSingleton(String nameDir){
        checkPath(nameDir);
    }

    public static PythonExeSingleton getIstance(String nameDir){
        if(istance == null) {
            istance = new PythonExeSingleton(nameDir);
        }
        return istance;
    }

    private void checkPath(String nameDir){
        try {
            FileReader f = new FileReader(nameDir + File.separator + "pythonPath.txt");
            BufferedReader b = new BufferedReader(f);
            String path = b.readLine();
            if(path.isEmpty()){
                this.isSet = false;
                this.pathToExe = "";
            }else {
                this.isSet = true;
                this.pathToExe = path;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String formatString(String stringa){

        String stringaFormattata = "";

        for(int i = 0; i < stringa.length(); i++){
            if(stringa.charAt(i) == File.separatorChar) {
                stringaFormattata = stringaFormattata + File.separatorChar + File.separatorChar;
            }else
                stringaFormattata = stringaFormattata + stringa.charAt(i);
        }

        return stringaFormattata;
    }


    public Boolean isSet() {
        return isSet;
    }

    public String getPathToExe() {
        return pathToExe;
    }

    public String getFormattedPathToExe() {
        return formatString(this.pathToExe);
    }

}
