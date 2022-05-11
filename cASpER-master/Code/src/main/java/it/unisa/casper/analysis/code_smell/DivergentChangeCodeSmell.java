package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;

public class DivergentChangeCodeSmell extends ClassLevelCodeSmell {
    /**
     * Costruttore
     *
     * @param detectionStrategy Strategy associato ai code smell che interessano le classi
     * @param algoritmsUsed     Stringa che rappresenta il tipo di algoritmo che ha rilevato tale smell
     */
    public DivergentChangeCodeSmell(ClassSmellDetectionStrategy detectionStrategy, String algoritmsUsed) {
        super(DIVERGENT_CHANGE, detectionStrategy, algoritmsUsed);
    }

    @Override
    public boolean affects(ClassBean aClass) {
        if (detectionStrategy.isSmelly(aClass)) {
            this.setIndex(detectionStrategy.getThresold(aClass));
            aClass.addSmell(this);
            return true;
        }
        return false;
    }

}
