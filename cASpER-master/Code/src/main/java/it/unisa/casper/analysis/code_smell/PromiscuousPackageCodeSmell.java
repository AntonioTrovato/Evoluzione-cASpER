package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.analysis.code_smell_detection.strategy.PackageSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.PackageBean;

/**
 * Classe concreta che istanzia lo smell "Promiscuous Package"
 */
public class PromiscuousPackageCodeSmell extends PackageLevelCodeSmell {

    /**
     * Costruttore
     *
     * @param detectionStrategy Strategy associato al rilevamento di smell che
     *                          riguardano i package. Il nome viene istanziato nella classe padre
     * @param algoritmsUsed     Stringa che rappresenta il tipo di algoritmo che ha rilevato tale smell
     */
    public PromiscuousPackageCodeSmell(PackageSmellDetectionStrategy detectionStrategy, String algoritmsUsed) {
        super(PROMISCUOUS_PACKAGE, detectionStrategy, algoritmsUsed);
    }

    /**
     * @param visitor
     * @param bean
     * @return
     */
    @Override
    public boolean accept(Visitor visitor, PackageBean bean) {
        return visitor.visit(this,bean);
    }
}
