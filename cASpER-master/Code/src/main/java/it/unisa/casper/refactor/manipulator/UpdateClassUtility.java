package it.unisa.casper.refactor.manipulator;

import it.unisa.casper.storage.beans.ClassBean;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class UpdateClassUtility {

    public static void migrateInterface(ClassBean fromClass, ClassBean toClass){
        String nameDir = System.getProperty("user.home") + File.separator + ".casper";
        String fileName = nameDir + File.separator + "tmp.java";
        ArrayList<String> result = getInterfaces(fromClass);

        FileWriter f = null;
        try {
            File file = new File(getFilePath(toClass));
            f = new FileWriter(fileName);
            Scanner scanner = new Scanner(file);

            String tp = "";
            while(scanner.hasNextLine()) {

                tp = scanner.nextLine();
                if(tp.contains("implements")){
                   ArrayList<String> destinationResult = getInterfaces(toClass);
                   tp = buildClassSignature(tp, result, destinationResult);
                }

                f.write(tp+"\n");
                f.flush();

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        copyFyle(fileName, getFilePath(toClass));
        File fileToDelete = new File(fileName);
        fileToDelete.delete();
    }

    private static String buildClassSignature(String full, ArrayList<String> fromList, ArrayList<String> destinationList){


        String[] tmpArray = full.split(" ");


        Boolean bool = false;
        String result = "";
        int i = 0;
        for(String s : tmpArray){
            result = result + " " + s;
            if(s.equalsIgnoreCase("implements")){

                for(String from : fromList){
                    from = from.replace(",", "");


                    for(String destination : destinationList){
                        destination = destination.replace(",", "");

                        if(from.equalsIgnoreCase(destination)){
                            bool = bool || true;
                        }


                    }

                    if(!bool){
                        if(tmpArray[i+1].equalsIgnoreCase(",")) {
                            result = result + " " + from;
                            bool = false;
                        }else{
                            result = result + " " + from + ",";
                            bool = false;
                        }
                    }
                }
            }
            i++;
        }

        return result;
    }

    private static ArrayList<String> getInterfaces(ClassBean fromClass){
        try {
            File file = new File(getFilePath(fromClass));
            Scanner scanner = new Scanner(file);

            String tp = "";
            while(scanner.hasNextLine()) {

                tp = scanner.nextLine();
                if(tp.contains("implements")){
                   return findInterfaces(tp);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void modifyExtend(ClassBean targetClass, ClassBean newSuperClass){
        String nameDir = System.getProperty("user.home") + File.separator + ".casper";
        String fileName = nameDir + File.separator + "tmp.java";
        Boolean bool = false;
        String stringToSubstitute = "extends "+ getClassNameFromString(targetClass.getSuperclass());
        String stringToWrite = "extends " + getClassName(newSuperClass);

        FileWriter f = null;
        try {
            File file = new File(getFilePath(targetClass));
            f = new FileWriter(fileName);
            Scanner scanner = new Scanner(file);

            String tp = "";
            while(scanner.hasNextLine()) {

                tp = scanner.nextLine();
                System.out.println(tp);
                tp = tp.replaceAll(stringToSubstitute, stringToWrite);
                System.out.println(tp);
                f.write(tp+"\n");
                f.flush();

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        copyFyle(fileName, getFilePath(targetClass));
        File fileToDelete = new File(fileName);
        fileToDelete.delete();

    }

    public static void addImport(ClassBean targetClass, ClassBean classToImport) {

        if(targetClass == null || classToImport == null)
            return;

        String nameDir = System.getProperty("user.home") + File.separator + ".casper";
        String fileName = nameDir + File.separator + "tmp.java";

        Boolean bool = false;
        String add ="import "+ classToImport.getBelongingPackage().getFullQualifiedName()+"."+getClassName(classToImport)+";";
        FileWriter f = null;
        try {
            File file = new File(getFilePath(targetClass));
            f = new FileWriter(fileName);
            Scanner scanner = new Scanner(file);


            String tp = "";
            while(scanner.hasNextLine()) {

                tp = scanner.nextLine();
                f.write(tp+"\n");
                f.flush();

                if(tp.contains("package") && !bool) {
                    bool = true;
                    f.write("\n"+add);
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        copyFyle(fileName, getFilePath(targetClass));
        File fileToDelete = new File(fileName);
        fileToDelete.delete();
        System.out.println("FINE METODO IMPORT");
    }

    private static void copyFyle(String fileToCopyPath, String destination){

        FileWriter f = null;
        try {
            File fileToCopy = new File(fileToCopyPath);
            f = new FileWriter(destination);
            Scanner scanner = new Scanner(fileToCopy);

            String tp = "";
            while(scanner.hasNextLine()) {

                tp = scanner.nextLine();
                f.write(tp+"\n");
                f.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFile(String fileName, String fileText){
        FileWriter f = null;
        try {
            f = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(f);
            out.write(fileText);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteClassFile(ClassBean classeDaEliminare){

       // System.out.println(replaceSeparator(superClass1.getPathToFile())+File.separator+getClassName(superClass1));
        File f = new File(getFilePath(classeDaEliminare));
        if(f.exists()) {
            f.delete();
        }
    }


    private static String getFilePath(ClassBean aClass){
        return replaceSeparator(aClass.getPathToFile()) + File.separator + getClassName(aClass)+".java";
    }

    private static String replaceSeparator(String string){
        return string.replaceAll("/", "\\"+ File.separator);
    }


    private static String getClassName(ClassBean classBean){
        return getClassNameFromString(classBean.getFullQualifiedName());
    }

    private static String getClassNameFromString(String name){
        String[] tmpArray = name.split("\\.");
        String tmp = tmpArray[tmpArray.length-1];
        return tmp;
    }

    private static ArrayList<String> findInterfaces(String full){
        ArrayList<String> result = new ArrayList<>();
        String[] tmpArray = full.split(" ");
        for(int i = 0; i < tmpArray.length; i++){
            if(tmpArray[i].equalsIgnoreCase("implements")){
                for(int j = i + 1; j < tmpArray.length; j++){
                    String stop = tmpArray[j];
                    if(!(stop.equalsIgnoreCase("{") || stop.equalsIgnoreCase("extends"))) {
                        result.add(stop);
                    }
                }
            }
        }

        return  result;
    }
}
