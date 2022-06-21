package it.unisa.casper.gui;

import com.intellij.openapi.project.Project;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WizardConcreteFactory implements ClassSmellGUIAbstractFactory{
    /**
     * @param c
     * @param splitting
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createBlobGUI(ClassBean c, List<ClassBean> splitting, Project project) {
        return new BlobWizard(c,splitting,project);
    }

    /**
     * @param smellClass
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createMisplacedClassGUI(ClassBean smellClass, Project project) {
        return new MisplacedClassWizard(smellClass,project);
    }

    /**
     * @param classeAffetta
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createShotgunSurgeryGUI(ClassBean classeAffetta, Project project) {
        return new ShothunSurgeryWizard(classeAffetta,project);
    }

    /**
     * @param classeAffetta
     * @param splittedClasses
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createDivergentChangeGUI(ClassBean classeAffetta, List<ClassBean> splittedClasses, Project project) {
        return new DivergentChangeWizard(classeAffetta,splittedClasses,project);
    }

    /**
     * @param smellMethod
     * @param project
     * @param systemPackages
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createFeatureEnvyGUI(MethodBean smellMethod, Project project, List<PackageBean> systemPackages) {
        return new FeatureEnvyWizard(smellMethod,project,systemPackages);
    }

    /**
     * @param PI
     * @param project
     * @param systemPackages
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createParallelInheritanceGUI(ClassBean super1, ClassBean super2, @Nullable Project project, List<PackageBean> systemPackages) {
        return new ParallelInheritanceWizard(super1,super2,project,systemPackages);
    }

    /**
     * @param packageBeanPP
     * @param packages
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createPromiscuousPackageGUI(PackageBean packageBeanPP, List<PackageBean> packages, Project project) {
        return new PromiscuousPackageWizard(packageBeanPP,packages,project);
    }

    @Override
    public AbstractCodeSmellGUI createSpaghettiCodeGUI(ClassBean c, Project project) {
        return null;
    }

    @Override
    public AbstractCodeSmellGUI createSwissArmyKnifeGUI(ClassBean c, Project project) {
        return null;
    }

}
