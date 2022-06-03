package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;

public class ShotgunSurgeryCodeSmell extends ClassLevelCodeSmell{
    /**
     * Costruttore
     *
     * @param detectionStrategy Strategy associato ai code smell che interessano le classi
     * @param algoritmsUsed     Stringa che rappresenta il tipo di algoritmo che ha rilevato tale smell
     */
    public ShotgunSurgeryCodeSmell(ClassSmellDetectionStrategy detectionStrategy, String algoritmsUsed) {
        super(SHOTGUN_SURGERY, detectionStrategy, algoritmsUsed);
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
