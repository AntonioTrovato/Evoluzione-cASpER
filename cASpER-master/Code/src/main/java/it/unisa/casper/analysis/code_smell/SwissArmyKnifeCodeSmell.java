package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;


public class SwissArmyKnifeCodeSmell extends ClassLevelCodeSmell{

    /**
     * Costruttore
     *
     * @param detectionStrategy Strategy associato al rilevamento di smell che
     *                          riguardano le classi. Il nome viene istanziato nella classe padre
     * @param algoritmsUsed     Stringa che rappresenta il tipo di algoritmo che ha rilevato tale smell
     */
    public SwissArmyKnifeCodeSmell(ClassSmellDetectionStrategy detectionStrategy, String algoritmsUsed) {
        super(SWISS_ARMY_KNIFE, detectionStrategy, algoritmsUsed);
    }

    /**
     * @param visitor
     * @param bean
     * @return
     */
    @Override
    public boolean accept(Visitor visitor, ClassBean bean) {
        return visitor.visit(this,bean);
    }
}
