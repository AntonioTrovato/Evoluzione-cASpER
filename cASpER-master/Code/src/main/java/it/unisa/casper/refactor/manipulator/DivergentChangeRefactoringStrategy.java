package it.unisa.casper.refactor.manipulator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.refactoring.extractclass.ExtractClassProcessor;
import it.unisa.casper.refactor.exceptions.BlobException;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.InstanceVariableBean;
import it.unisa.casper.storage.beans.MethodBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DivergentChangeRefactoringStrategy implements RefactoringStrategy {

    private ClassBean originalClass;
    private List<ClassBean> splittedList;
    protected Project project;
    private PsiClass psiOriginalClass = null;

    public DivergentChangeRefactoringStrategy(ClassBean divergentChangeClass, List<ClassBean> splittedList, Project project) {
        this.originalClass = divergentChangeClass;
        this.splittedList = splittedList;
        this.project = project;
    }

    @Override
    public void doRefactor() throws RefactorException {

        List<MethodBean> listaMetodi = originalClass.getMethodList();
        for(ClassBean c : splittedList){
            for(MethodBean m : c.getMethodList()){
                listaMetodi.remove(m);
            }
        }


        String pathClass = originalClass.getPathToFile();
        String incopletePath = pathClass.substring(0, pathClass.lastIndexOf('/'));
        String packageName = originalClass.getBelongingPackage().getFullQualifiedName();
        List<PsiMethod> methodsToMove = null;
        List<PsiField> fieldsToMove;
        List<PsiClass> innerClasses;
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        psiOriginalClass = PsiUtil.getPsi(originalClass, project);

        for (ClassBean classBean : splittedList) {
            methodsToMove = new ArrayList<>();
            fieldsToMove = new ArrayList<>();
            innerClasses = new ArrayList<>();
            String classShortName = classBean.getFullQualifiedName().substring(classBean.getFullQualifiedName().lastIndexOf('.') + 1);

            // creo una lista di metodi
            for (MethodBean metodoSplittato : classBean.getMethodList()) {
                String methodShortName = metodoSplittato.getFullQualifiedName().substring(metodoSplittato.getFullQualifiedName().lastIndexOf('.') + 1);
                methodsToMove.add(psiOriginalClass.findMethodsByName(methodShortName, true)[0]);
            }

            //creo una lista di fields
            for (InstanceVariableBean instanceVariableBean : classBean.getInstanceVariablesList()) {
                fieldsToMove.add(psiOriginalClass.findFieldByName(instanceVariableBean.getFullQualifiedName(), true));
            }

            for (PsiClass innerClass : psiOriginalClass.getInnerClasses()) {
                innerClasses.add(innerClass);
            }

            try {
                ExtractClassProcessor processor = new ExtractClassProcessor(psiOriginalClass, fieldsToMove, methodsToMove, innerClasses, packageName, classShortName);
                processor.run();
            } catch (Exception e) {

                throw new RefactorException(e.getMessage());
            }
        }

        Boolean bool = true;
        for(MethodBean m : listaMetodi){
            if(!m.getDefaultCostructor()){
                bool = bool && true;
            }else{
                bool = bool && false;
            }
        }

        if(bool){
            UpdateClassUtility.deleteClassFile(originalClass);
        }
    }
}
