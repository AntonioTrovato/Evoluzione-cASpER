package it.unisa.casper.refactor.manipulator;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.InstanceVariableBean;
import it.unisa.casper.storage.beans.MethodBean;

public class PsiUtil {

    /**
     * Converte un InstanceVariableBean in PsiField
     */
    public static PsiField getPsi(InstanceVariableBean fieldBean, ClassBean classBean, Project project) {
        final PsiField[] foundPsiFields= new PsiField[1];

        ApplicationManager.getApplication().runReadAction(() -> {
                    PsiClass psiClass = getPsi(classBean, project);
                    String fieldName = fieldBean.getFullQualifiedName().substring(fieldBean.getFullQualifiedName().lastIndexOf('.') + 1);
                    foundPsiFields[0] = psiClass.findFieldByName(fieldName, true);
        }
        );
        return foundPsiFields[0];
    }

    /**
     * Converte un MethodBean in PsiMethod
     *
     * @param methodBean da convertire
     * @return psi del metodo del bean
     */
    public static PsiMethod getPsi(MethodBean methodBean, Project project) {
        final PsiMethod[] foundPsiMethod = new PsiMethod[1];

        ApplicationManager.getApplication().runReadAction(() -> {
                    PsiClass psiClass = getPsi(methodBean.getBelongingClass(), project);
                    String methodName = methodBean.getFullQualifiedName().substring(methodBean.getFullQualifiedName().lastIndexOf('.') + 1);
                    foundPsiMethod[0] = psiClass.findMethodsByName(methodName, true)[0];
                }
        );
        return foundPsiMethod[0];
    }

    /**
     * Converte un ClassBean in PsiClass
     *
     * @param classBean da convertire
     * @return psi della classe del bean
     */
    public static PsiClass getPsi(ClassBean classBean, Project project) {
        final PsiClass[] foundClass = new PsiClass[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            foundClass[0] = JavaPsiFacade.getInstance(project).findClass(classBean.getFullQualifiedName(), GlobalSearchScope.allScope(project));
        });
        //https://www.programcreek.com/java-api-examples/?api=com.intellij.psi.JavaPsiFacade

        return foundClass[0];
    }
}
