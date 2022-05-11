package it.unisa.casper.refactor.manipulator;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.refactoring.move.moveMembers.MoveMembersOptions;
import com.intellij.refactoring.move.moveMembers.MoveMembersProcessor;
import com.intellij.refactoring.rename.RenameJavaMemberProcessor;
import com.intellij.refactoring.rename.RenameJavaVariableProcessor;
import com.intellij.refactoring.rename.RenameVariableUsageInfo;
import com.intellij.usageView.UsageInfo;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.InstanceVariableBean;
import it.unisa.casper.storage.beans.InstanceVariableBeanList;
import it.unisa.casper.storage.beans.MethodBean;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;

public class FieldMover{
    private ClassBean targetClass, sourceClass;
    private List<InstanceVariableBean> fieldsToMove;
    private Project project;
    private PsiClass psiTargetClass, psiSourceClass;
    private Vector<UsageInfo> usages;
    private String scope, returnType, name, parameters, throwsList, body;

    public FieldMover(ClassBean targetClass, ClassBean sourceClass, List<InstanceVariableBean> fieldsToMove, Project project){
        this.targetClass = targetClass;
        this.sourceClass = sourceClass;
        this.fieldsToMove = fieldsToMove;
        this.project = project;
        this.psiTargetClass = PsiUtil.getPsi(targetClass, project);
        this.psiSourceClass = PsiUtil.getPsi(sourceClass, project);
    }

    public void MoveField(){

            LinkedHashSet<PsiMember> memberSet = new LinkedHashSet<>();
            for (InstanceVariableBean i : fieldsToMove) {
                PsiMember member = PsiUtil.getPsi(i, sourceClass, project);
                //verifica che nella target class esiste una variabile di istanza con lo stesso nome
                if(existsFieldWithName(member.getName())){
                    String oldName = (member.getName());
                    String newName = changeName(member.getName(), member);
                    updateMethods(newName, oldName, i);
                }

                memberSet.add(member);
            }

            MockMoveMembersOptions options =  new MockMoveMembersOptions(psiTargetClass.getQualifiedName(), memberSet);
            options.setMemberVisibility(null);
            new MoveMembersProcessor(project, null, options).run();
    }

    private void updateMethods(String newName, String oldName, InstanceVariableBean instanceVariableBean){
        for(MethodBean m : sourceClass.getMethodList()) {
            String tmp = m.getFullQualifiedName();
            if(!(tmp.contains("set") || tmp.contains("get"))) {
                if (m.getInstanceVariableList().contains(instanceVariableBean)) {
                    PsiMethod psiMethod = PsiUtil.getPsi(m, project);

                    scope = psiMethod.getModifierList().getText();
                    returnType = psiMethod.getReturnType().getPresentableText();
                    name = psiMethod.getName();
                    parameters = psiMethod.getParameterList().getText();
                   /* parameters = parameters.substring(0, parameters.length() - 1);
                    if (parameters.length() > 1) {
                        parameters = parameters + ", ";
                    }
                    parameters = parameters + instanceVariableBean.getType() + " " + newName + ")";*/
                    throwsList = psiMethod.getThrowsList().getText();
                    body = psiMethod.getBody().getText();
                    body = body.replaceAll(oldName, newName);


                    String methodToWrite = MethodMover.buildMethod(scope, returnType, name, parameters, throwsList, body);

                    WriteCommandAction.runWriteCommandAction(project, () -> {
                        MethodMover.methodWriter(methodToWrite, psiMethod, psiTargetClass, true, project);
                    });
                }
            }
        }
    }

    private String getFieldName(InstanceVariableBean fieldBean){
        String fieldName = fieldBean.getFullQualifiedName().substring(fieldBean.getFullQualifiedName().lastIndexOf('.') + 1);
        return fieldName;
    }

    private String changeName(String name, PsiMember member){
        String newName = name + "_refactor";
        WriteCommandAction.runWriteCommandAction(project, () -> {
            RenameJavaMemberProcessor r = new RenameJavaVariableProcessor();
            RenameVariableUsageInfo u = new RenameVariableUsageInfo((PsiVariable) member, newName);
            UsageInfo[] array = new UsageInfo[1];
            array[0] = u;
            r.renameElement(member, newName, array, null);
        });

        return newName;
    }

    private boolean existsFieldWithName(String name) {
        final PsiField[] allFields = psiTargetClass.getAllFields();
        for (PsiField field : allFields) {
            if (name.equals(field.getName())) {
                return true;
            }
        }
        return false;
    }
}
