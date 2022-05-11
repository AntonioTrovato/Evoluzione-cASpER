package it.unisa.casper.refactor.manipulator;

import com.intellij.openapi.project.Project;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;

import java.util.List;

public class ShotgunSurgeryRefactoringStrategy implements RefactoringStrategy {

    private ClassBean affectedClass;
    protected Project project;

    public ShotgunSurgeryRefactoringStrategy(ClassBean affectedClass, Project project){
        this.affectedClass = affectedClass;
        this.project = project;
    }

    @Override
    public void doRefactor() throws RefactorException {

        List<ClassBean> listaClassiColpite = affectedClass.getShotgunSurgeryHittedClasses();

        for(ClassBean c : listaClassiColpite){

            if(!c.getFullQualifiedName().equals(affectedClass.getFullQualifiedName())) {
                List<MethodBean> listaMetodiColpiti = c.getShotgunSurgeryHittedMethods();
                for (MethodBean m : listaMetodiColpiti) {
                    MethodMover mover = new MethodMover(m, this.affectedClass, this.project);
                    mover.moveMethod();
                }
            }

            UpdateClassUtility.addImport(affectedClass, c);
        }



    }
}
