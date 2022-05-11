package it.unisa.casper.refactor.manipulator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import it.unisa.casper.refactor.exceptions.FeatureEnvyException;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.MethodBean;

import javax.swing.*;
import java.util.List;

/**
 * Feature Envy Refactor (FE Refactor) è la classe che si occupa del Refactor degli smells di tipo Feature Envy
 * <p>
 * ATTENZIONE CONSIDERANDO CHE LA CLASSE E' STATE REALIZZATA DA NICOLA AMORIELLO E DOMENICO PASCUCCI IL TUO COMPUTER
 * POTREBBE:
 * ->ESPLODERE
 * ->AVERE BUG
 * ->DISINSTALLARE WINDOWS E INSTALLARE AUTOMATICAMENTE UBUNTU 8024
 * ->PRENOTARTI UN BIGLIETTO PER IL MESSICO
 * <p>
 * SI SPERA FIXARE UN FEATURE ENVIE
 **/

public class FeatureEnvyRefactoringStrategy implements RefactoringStrategy {
    private MethodBean methodToMove;
    //PSI Section
    private PsiClass psiSourceClass, psiDestinationClass;
    private PsiMethod psiMethod;
    // Project
    private Project project;
    //Fixing Type
    private int fixtype = -1;
    private String variabileDaTrasformare = "";
    private boolean isStaticMethod;

    private String scope, returnType, name, parameters, throwsList, body;

    /**
     * Costruttore di Feature Envy Refactoring strategy con classe sorgente implicita
     *
     * @param methodToMove methodo da spostare
     * @param project      progetto di analisi
     */
    public FeatureEnvyRefactoringStrategy(MethodBean methodToMove, Project project) {
        this.project = project;
        this.methodToMove = methodToMove;
        /*
        psiMethod = PsiUtil.getPsi(methodToMove, project);
        psiSourceClass = PsiUtil.getPsi(methodToMove.getBelongingClass(), project);
        psiDestinationClass = PsiUtil.getPsi(methodToMove.getEnviedClass(), project);
        fixtype = selectFixingStrategy();
        isStaticMethod = methodToMove.getStaticMethod();
        //setto le stringe per la costruzione del metodo
        scope = psiMethod.getModifierList().getText();
        returnType = psiMethod.getReturnType().getPresentableText();
        name = psiMethod.getName();
        parameters = psiMethod.getParameterList().getText();
        throwsList = psiMethod.getThrowsList().getText();
        body = psiMethod.getBody().getText();
        */
    }

    /**
     * implementazione dell'interfaccia @class {@link RefactoringStrategy}
     *
     * @throws FeatureEnvyException
     */
    @Override
    public void doRefactor() throws RefactorException {
        MethodMover methodMover = new MethodMover(this.methodToMove,this.methodToMove.getEnviedClass(), this.project);
        methodMover.moveMethod();

        /*
        int i = 0;
        String nameMethodCall;
        boolean controllo = false;
        List<MethodBean> methodsCall = methodToMove.getMethodsCalls();

        while (i < methodsCall.size() && !controllo) {
            nameMethodCall = methodsCall.get(i).getFullQualifiedName();
            nameMethodCall = nameMethodCall.substring(nameMethodCall.lastIndexOf(".") + 1);
            if (body.contains(nameMethodCall) && !body.contains("." + nameMethodCall)) controllo = true;
            i++;
        }

        if (i >= methodsCall.size()) {
            try {
                controlName();
                switch (fixtype) {
                    case 1:
                        instanceVariableFeatureEnvy();
                        break;
                    case 2:
                        parametersFeatureEnvy();
                        break;
                    case 0:
                        otherFeatureEnvy();
                }
            } catch (Exception e) {
                throw new FeatureEnvyException(e.getMessage());
            }
        } else {
            throw new FeatureEnvyException("Extract methods application is required.\nThis type of refactoring is not yet possible");
        }
        */
    }


}
