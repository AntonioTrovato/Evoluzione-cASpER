package it.unisa.casper.gui;

import com.intellij.openapi.project.Project;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClassSmellGUIAbstractFactory {

    public AbstractCodeSmellGUI createBlobGUI(ClassBean c, List<ClassBean> splitting, Project project);
    public AbstractCodeSmellGUI createMisplacedClassGUI(ClassBean smellClass, Project project);
    public AbstractCodeSmellGUI createShotgunSurgeryGUI(ClassBean classeAffetta, Project project);
    public AbstractCodeSmellGUI createDivergentChangeGUI(ClassBean classeAffetta, List<ClassBean> splittedClasses, Project project);
    public AbstractCodeSmellGUI createFeatureEnvyGUI(MethodBean smellMethod, Project project, List<PackageBean> systemPackages);
    public AbstractCodeSmellGUI createParallelInheritanceGUI(ClassBean super1, ClassBean super2, @Nullable Project project,List<PackageBean> systemPackages);
    public AbstractCodeSmellGUI createPromiscuousPackageGUI(PackageBean packageBeanPP, List<PackageBean> packages, Project project);
}
