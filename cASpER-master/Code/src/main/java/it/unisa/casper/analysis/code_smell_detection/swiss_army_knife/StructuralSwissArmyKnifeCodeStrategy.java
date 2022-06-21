package it.unisa.casper.analysis.code_smell_detection.swiss_army_knife;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;

import java.util.HashMap;

public class StructuralSwissArmyKnifeCodeStrategy implements ClassSmellDetectionStrategy {

    private static int NI;

    public StructuralSwissArmyKnifeCodeStrategy(int NI) {
        this.NI = NI;

    }

    public boolean isSmelly(ClassBean pClass) {
        if(pClass.getClassiImplementate()!=null){
            if (pClass.getClassiImplementate().size()>=NI)
                return true;
            else
                return false;
        }
        return false;
    }


    public HashMap<String, Double> getThresold(ClassBean pClass) {
        HashMap<String, Double> list = new HashMap<String, Double>();
        list.put("dipendenza", (double) pClass.getClassiImplementate().size());
        return list;
    }
}
