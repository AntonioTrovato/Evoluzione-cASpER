package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.analysis.code_smell_detection.strategy.MethodSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;

/**
 * Classe concreta che istanzia lo smell "Feature Envy"
 */

public class FeatureEnvyCodeSmell extends MethodLevelCodeSmell {

    /**
     * Costruttore
     *
     * @param detectionStrategy Strategy associato al rilevamento di smell che
     *                          riguardano i metodi. Il nome viene istanziato nella classe padre
     * @param algoritmsUsed     Stringa che rappresenta il tipo di algoritmo che ha rilevato tale smell
     */
    public FeatureEnvyCodeSmell(MethodSmellDetectionStrategy detectionStrategy, String algoritmsUsed) {
        super(FEATURE_ENVY, detectionStrategy, algoritmsUsed);
    }

    /**
     * @param visitor
     * @param bean
     * @return
     */
    @Override
    public boolean accept(Visitor visitor, MethodBean bean) {
        return visitor.visit(this,bean);
    }



}
