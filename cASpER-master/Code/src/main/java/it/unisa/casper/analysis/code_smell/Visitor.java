package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;

public interface Visitor {

    boolean visit(BlobCodeSmell smell, ClassBean bean);
    boolean visit(DivergentChangeCodeSmell smell, ClassBean bean);
    boolean visit(FeatureEnvyCodeSmell smell, MethodBean bean);
    boolean visit(MisplacedClassCodeSmell smell, ClassBean bean);
    boolean visit(ParallelInheritanceCodeSmell smell, ClassBean bean);
    boolean visit(PromiscuousPackageCodeSmell smell, PackageBean bean);
    boolean visit(ShotgunSurgeryCodeSmell smell, ClassBean bean);
}
