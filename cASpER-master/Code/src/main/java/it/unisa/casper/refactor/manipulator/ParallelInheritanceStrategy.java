package it.unisa.casper.refactor.manipulator;

import com.intellij.ide.ui.EditorOptionsTopHitProvider;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;

import java.util.List;

public class ParallelInheritanceStrategy implements RefactoringStrategy {

    private ClassBean super1, super2;
    private Project project;
    private List<PackageBean> packageBeans;

    public ParallelInheritanceStrategy(ClassBean super1, ClassBean super2, Project project, List<PackageBean> packageBeans){
        this.super1 = super1;
        this.super2 = super2;
        this.project = project;
        this.packageBeans = packageBeans;
    }

    @Override
    public void doRefactor() throws RefactorException {
        try {
           // WriteCommandAction.runWriteCommandAction(project, () -> {
            FieldMover fieldMover = new FieldMover(super1, super2, super2.getInstanceVariablesList(), project);
            fieldMover.MoveField();
         //   });
        }catch (Exception e){
            e.printStackTrace();
        }

        for(MethodBean m : super2.getMethodList()) {
            if(m != null && super1 != null) {
                MethodMover methodMover = new MethodMover(m, super1, project);
                methodMover.moveMethod();
            }
        }


/*
            //UpdateClassUtility.migrateInterface(super2, super1);
            for (PackageBean p : packageBeans) {
                System.out.println("PACKAGE: " + p.getFullQualifiedName());
                for (ClassBean c : p.getClassList()) {
                    System.out.println("CLASSE: " + c.getFullQualifiedName());
                    if (c.getSuperclass().equalsIgnoreCase(super2.getFullQualifiedName())) {
                        System.out.println("IF TRUE");
                        UpdateClassUtility.modifyExtend(c, super1);
                    }
                }
            }

            UpdateClassUtility.deleteClassFile(super2);
*/
    }
}
