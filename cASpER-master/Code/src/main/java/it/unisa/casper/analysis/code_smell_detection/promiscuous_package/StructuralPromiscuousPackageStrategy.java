package it.unisa.casper.analysis.code_smell_detection.promiscuous_package;

import it.unisa.casper.analysis.code_smell_detection.strategy.PackageSmellDetectionStrategy;
import it.unisa.casper.storage.beans.PackageBean;
import it.unisa.casper.structuralMetrics.CKMetrics;

import java.util.HashMap;
import java.util.List;

public class StructuralPromiscuousPackageStrategy implements PackageSmellDetectionStrategy {

    private static List<PackageBean> projectPackages;
    private static double InverseMIntraC;
    private static double MInterC;

    public StructuralPromiscuousPackageStrategy(List<PackageBean> projectPackages, double InverseMIntraC, double MInterC) {
        this.projectPackages = projectPackages;
        this.InverseMIntraC = InverseMIntraC;
        this.MInterC = MInterC;
    }

    public boolean isSmelly(PackageBean pPackage) {
        if ((CKMetrics.computeMediumInterConnectivity(pPackage, projectPackages) > MInterC) || (CKMetrics.computeMediumIntraConnectivity(pPackage) > InverseMIntraC)) {
            return true;
        }
        return false;
    }

    public HashMap<String, Double> getThresold(PackageBean pPackage) {
        HashMap<String, Double> list = new HashMap<String, Double>();

        list.put("InverseMIntraC", CKMetrics.computeMediumIntraConnectivity(pPackage));
        list.put("MInterC", CKMetrics.computeMediumInterConnectivity(pPackage, projectPackages));
        return list;
    }
}
