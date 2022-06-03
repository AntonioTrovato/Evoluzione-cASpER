package it.unisa.casper.analysis.code_smell;

import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;

public class DetectionVisitor  implements Visitor{
    /**
     * @param smell
     * @param bean
     * @return
     */
    @Override
    public boolean visit(BlobCodeSmell smell, ClassBean bean) {
        if (smell.detectionStrategy.isSmelly(bean)) {
            smell.setIndex(smell.detectionStrategy.getThresold(bean));
            bean.addSmell(smell);
            return true;
        }
        return false;
    }

    /**
     * @param smell
     * @param bean
     * @return
     */
    @Override
    public boolean visit(DivergentChangeCodeSmell smell, ClassBean bean) {
        if (smell.detectionStrategy.isSmelly(bean)) {
            smell.setIndex(smell.detectionStrategy.getThresold(bean));
            bean.addSmell(smell);
            return true;
        }
        return false;
    }

    /**
     * @param smell
     * @param bean
     * @return
     */
    @Override
    public boolean visit(FeatureEnvyCodeSmell smell, MethodBean bean) {
        if (smell.detectionStrategy.isSmelly(bean)) {
            smell.setIndex(smell.detectionStrategy.getThresold(bean));
            bean.addSmell(smell);
            return true;
        }
        return false;
    }

    /**
     * @param smell
     * @param bean
     * @return
     */
    @Override
    public boolean visit(MisplacedClassCodeSmell smell, ClassBean bean) {
        if (smell.detectionStrategy.isSmelly(bean)) {
            smell.setIndex(smell.detectionStrategy.getThresold(bean));
            bean.addSmell(smell);
            return true;
        }
        return false;
    }

    /**
     * @param smell
     * @param bean
     * @return
     */
    @Override
    public boolean visit(ParallelInheritanceCodeSmell smell, ClassBean bean) {
        if (smell.detectionStrategy.isSmelly(bean)) {
            smell.setIndex(smell.detectionStrategy.getThresold(bean));
            bean.addSmell(smell);
            return true;
        }
        return false;
    }

    /**
     * @param smell
     * @param bean
     * @return
     */
    @Override
    public boolean visit(PromiscuousPackageCodeSmell smell, PackageBean bean) {
        if (smell.detectionStrategy.isSmelly(bean)) {
            smell.setIndex(smell.detectionStrategy.getThresold(bean));
            bean.addSmell(smell);
            return true;
        }
        return false;
    }

    /**
     * @param smell
     * @param bean
     * @return
     */
    @Override
    public boolean visit(ShotgunSurgeryCodeSmell smell, ClassBean bean) {
        if (smell.detectionStrategy.isSmelly(bean)) {
            smell.setIndex(smell.detectionStrategy.getThresold(bean));
            bean.addSmell(smell);
            return true;
        }
        return false;
    }
}
