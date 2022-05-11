package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;

public class ParallelInheritanceCodeSmell  extends ClassLevelCodeSmell{
    /**
     * Costruttore
     *
     * @param detectionStrategy Strategy associato ai code smell che interessano le classi
     * @param algoritmsUsed     Stringa che rappresenta il tipo di algoritmo che ha rilevato tale smell
     */
    public ParallelInheritanceCodeSmell(ClassSmellDetectionStrategy detectionStrategy, String algoritmsUsed) {
        super(PARALLEL_INHERITANCE, detectionStrategy, algoritmsUsed);
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
