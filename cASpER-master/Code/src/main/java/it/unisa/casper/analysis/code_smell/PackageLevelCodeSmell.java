package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.analysis.code_smell_detection.strategy.PackageSmellDetectionStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.PackageBean;

/**
 * Classe astratta figlia di CodeSmell, estendendo questa classe verranno istanziati tutti
 * gli oggetti CodeSmell che riguardano l'ambito dei package
 */

public abstract class PackageLevelCodeSmell extends CodeSmell<PackageBean> {

    /**
     * Costruttore
     *
     * @param name              rappresenta nome dello smell
     * @param detectionStrategy Strategy associato agli smell che riguardano i package
     * @param algoritmsUsed     Stringa che rappresenta il tipo di algoritmo che ha rilevato tale smell
     */
    public PackageLevelCodeSmell(String name, PackageSmellDetectionStrategy detectionStrategy, String algoritmsUsed) {
        super(name, detectionStrategy, algoritmsUsed);
    }

    public abstract boolean accept(Visitor visitor, PackageBean bean);

}

