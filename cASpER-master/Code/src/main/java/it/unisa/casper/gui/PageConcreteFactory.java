package it.unisa.casper.gui;

import com.intellij.openapi.project.Project;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PageConcreteFactory implements ClassSmellGUIAbstractFactory{
    /**
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createBlobGUI(ClassBean c, List<ClassBean> splitting, Project project) {
        return new BlobPage(c,project);
    }

    /**
     * @param smellClass
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createMisplacedClassGUI(ClassBean smellClass, Project project) {
        return new MisplacedClassPage(smellClass,project);
    }

    /**
     * @param classeAffetta
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createShotgunSurgeryGUI(ClassBean classeAffetta, Project project) {
        return new ShotgunSurgeryPage(classeAffetta,project);
    }

    /**
     * @param classeAffetta
     * @param splittedClasses
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createDivergentChangeGUI(ClassBean classeAffetta, List<ClassBean> splittedClasses, Project project) {
        return new DivergentChangePage(classeAffetta,project);
    }

    /**
     * @param smellMethod
     * @param project
     * @param systemPackages
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createFeatureEnvyGUI(MethodBean smellMethod, Project project, List<PackageBean> systemPackages) {
        return new FeatureEnvyPage(smellMethod, project, systemPackages);
    }

    /**
     * @param PI
     * @param project
     * @param systemPackages
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createParallelInheritanceGUI(ClassBean super1, ClassBean super2, @Nullable Project project, List<PackageBean> systemPackages) {
        return new ParallelInheritancePage(super1,project,systemPackages);
    }

    /**
     * @param packageBeanPP
     * @param packages
     * @param project
     * @return
     */
    @Override
    public AbstractCodeSmellGUI createPromiscuousPackageGUI(PackageBean packageBeanPP, List<PackageBean> packages, Project project) {
        return new PromiscuousPackagePage(packageBeanPP,packages,project);
    }

    @Override
    public AbstractCodeSmellGUI createSpaghettiCodeGUI(ClassBean c, Project project) {
        return new SpaghettiCodePage(c,project);
    }

    @Override
    public AbstractCodeSmellGUI createSwissArmyKnifeGUI(ClassBean c, Project project) {
        return new SwissArmyKnifeCodePage(c,project);
    }

}
