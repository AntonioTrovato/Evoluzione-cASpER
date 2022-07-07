package it.unisa.casper.analysis.code_smell_detection.spaghetti_code;

import it.unisa.casper.analysis.code_smell_detection.BeanDetection;
import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.structuralMetrics.CKMetrics;

import java.util.HashMap;

public class StructuralSpaghettiCodeStrategy implements ClassSmellDetectionStrategy {

    private static int LOC;

    public StructuralSpaghettiCodeStrategy(int LOC) {
        this.LOC = LOC;

    }

    public boolean isSmelly(ClassBean pClass) {
        //1 Verifico se la classe non utilizza l'ereditarieta
        if(pClass.getClasseEstesa()==null){
            //2 Verifico se un metodo interno alla classe non possiede parametri ed e troppo lungo
            for (MethodBean method: pClass.getMethodList()){
                if (CKMetrics.getLOC(method)>=LOC && method.getParameters().size()==0) {
                    //3 Verifico se il nome della classe contiene parole come Make, Create, Execute
                    if(pClass.getFullQualifiedName().contains("Make") || pClass.getFullQualifiedName().contains("Create") || pClass.getFullQualifiedName().contains("Execute"))
                        return true;
                }
            }
        }
        return false;
    }


    public HashMap<String, Double> getThresold(ClassBean pClass) {
        HashMap<String, Double> list = new HashMap<String, Double>();
        list.put("dipendenza", (double) (CKMetrics.getLOC(pClass)));
        return list;
    }
}
