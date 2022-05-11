package it.unisa.casper.refactor.splitting_algorithm;

import it.unisa.casper.refactor.splitting_algorithm.irEngine.VectorSpaceModel;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.InstanceVariableBean;
import it.unisa.casper.storage.beans.MethodBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class MethodByMethodMatrixConstruction {

    private String casperDirectoryPath;
    private File matrixFolder;
    private File stopwordList;
    private FileInputStream fs;
    private InputStreamReader isr;
    private BufferedReader br;

    public MethodByMethodMatrixConstruction() {
        casperDirectoryPath = System.getProperty("user.home") + "/.casper";
        matrixFolder = new File(casperDirectoryPath + "/matrix");
        stopwordList = new File(casperDirectoryPath + "stopwordlist.txt");
    }

    public double[][] buildMethodByMethodMatrix(double pWcdm, double pWssm, double pWcsm, ClassBean pToSplit) throws Exception {

        if (!stopwordList.exists()) {
            stopwordList.createNewFile();
            Utility.createStopwordList(stopwordList);
        }

        File CDMmatrixFile = new File(matrixFolder.getAbsolutePath() + "/" + "CDM_matrix" + pToSplit.getFullQualifiedName() + ".txt");
        File CSMmatrixFile = new File(matrixFolder.getAbsolutePath() + "/" + "CSM_matrix" + pToSplit.getFullQualifiedName() + ".txt");
        File SSMmatrixFile = new File(matrixFolder.getAbsolutePath() + "/" + "SSM_matrix" + pToSplit.getFullQualifiedName() + ".txt");

        Collection<MethodBean> methodsAll = pToSplit.getMethodList();
        Collection<MethodBean> methods = new Vector<MethodBean>();

        for (MethodBean m : methodsAll) {
            if (!m.getFullQualifiedName().equals(pToSplit.getFullQualifiedName())) {
                methods.add(m);
            }
        }

        Iterator<MethodBean> it = methods.iterator();
        double[][] methodByMethodMatrix = new double[methods.size()][methods.size()];
        double[][] CDMmatrix = new double[methodByMethodMatrix.length][methodByMethodMatrix.length];
        double[][] CSMmatrix = new double[methodByMethodMatrix.length][methodByMethodMatrix.length];
        double[][] SSMmatrix = new double[methodByMethodMatrix.length][methodByMethodMatrix.length];
        MethodBean tmpMethod = null;
        Vector<MethodBean> vectorOfMethods = new Vector<MethodBean>();

        while (it.hasNext()) {
            tmpMethod = (MethodBean) it.next();
            vectorOfMethods.add(tmpMethod);
        }

        Collections.sort(vectorOfMethods);

        matrixFolder.mkdirs();
        MethodBean methodSource, methodTarget;
        for (int i = 0; i < CDMmatrix.length; i++) {
            for (int j = i; j < CDMmatrix.length; j++) {
                if (i != j) {
                    methodSource = vectorOfMethods.elementAt(i);
                    methodTarget = vectorOfMethods.elementAt(j);
                    CDMmatrix[i][j] = getCCM(methodSource.getMethodsCalls(), methodTarget.getMethodsCalls(), methodSource.getFullQualifiedName(), methodTarget.getFullQualifiedName());
                } else {
                    CDMmatrix[i][j] = 1.0;
                }
                CDMmatrix[j][i] = CDMmatrix[i][j];
            }
        }
        for (int i = 0; i < SSMmatrix.length; i++) {
            for (int j = i; j < SSMmatrix.length; j++) {
                if (i != j) {
                    methodSource = vectorOfMethods.elementAt(i);
                    methodTarget = vectorOfMethods.elementAt(j);
                    SSMmatrix[i][j] = getSSM(methodSource.getInstanceVariableList(), methodTarget.getInstanceVariableList());
                } else {
                    SSMmatrix[i][j] = 1.0;
                }
                SSMmatrix[j][i] = SSMmatrix[i][j];
            }
        }
        for (int i = 0; i < CSMmatrix.length; i++) {
            for (int j = i; j < CSMmatrix.length; j++) {
                if (i != j) {
                    methodSource = vectorOfMethods.elementAt(i);
                    methodTarget = vectorOfMethods.elementAt(j);
                    CSMmatrix[i][j] = getCSM(methodSource, methodTarget);
                } else {
                    CSMmatrix[i][j] = 1.0;
                }
                CSMmatrix[j][i] = CSMmatrix[i][j];
            }
        }

        CDMmatrix = filterMatrix(CDMmatrix, pWcdm);
        CSMmatrix = filterMatrix(CSMmatrix, pWcsm);
        SSMmatrix = filterMatrix(SSMmatrix, pWssm);

        for (int i = 0; i < methodByMethodMatrix.length; i++) {
            for (int j = i; j < methodByMethodMatrix.length; j++) {
                if (i != j) {
                    methodByMethodMatrix[i][j] = CDMmatrix[i][j] * pWcdm + CSMmatrix[i][j] * pWcsm + SSMmatrix[i][j] * pWssm;
                } else {
                    methodByMethodMatrix[i][j] = 1.0;
                }
                methodByMethodMatrix[j][i] = methodByMethodMatrix[i][j];
            }
        }

        return methodByMethodMatrix;
    }

    private double getSSM(List<InstanceVariableBean> variablesSourceMethod, List<InstanceVariableBean> variablesTargetMethod) {
        double ssm = 0;
        Iterator itSource = variablesSourceMethod.iterator();
        int shared = 0;

        if (variablesSourceMethod.size() == 0 || variablesTargetMethod.size() == 0) {
            return 0;
        }

        InstanceVariableBean tmpSourceVariable = null;
        while (itSource.hasNext()) {
            tmpSourceVariable = (InstanceVariableBean) itSource.next();

            InstanceVariableBean tmpTargetVariable = null;
            Iterator itTarget = variablesTargetMethod.iterator();
            while (itTarget.hasNext()) {
                tmpTargetVariable = (InstanceVariableBean) itTarget.next();
                if (tmpSourceVariable.getFullQualifiedName().equals(tmpTargetVariable.getFullQualifiedName())) {
                    shared++;
                    break;
                }
            }
        }
        ssm = (double) shared / (variablesSourceMethod.size() + variablesTargetMethod.size() - shared);

        return ssm;
    }

    private static double getCSM(MethodBean sourceMethod, MethodBean targetMethod) throws Exception {
        VectorSpaceModel ir = new VectorSpaceModel();
        double csm = 0;
        Collection<String[]> methods = new ArrayList<String[]>();

        String[] methods1 = new String[2];
        methods1[0] = sourceMethod.getFullQualifiedName();
        methods1[1] = sourceMethod.getTextContent();
        methods.add(methods1);

        String[] methods2 = new String[2];
        methods2[0] = targetMethod.getFullQualifiedName();
        methods2[1] = targetMethod.getTextContent();
        methods.add(methods2);
        //Call ir engine to indexing the methods
        ir.generateMatrix(methods);
        csm = ir.getSimilarity(sourceMethod.getFullQualifiedName(), targetMethod.getFullQualifiedName());
        return csm;
    }

    private static double getCCM(Collection<MethodBean> callsSourceMethod, Collection<MethodBean> callsTargetMethod, String sourceMethodName, String targetMethodName) {
        double ccm = 0;
        Iterator itSource = callsSourceMethod.iterator();
        Iterator itTarget = callsTargetMethod.iterator();
        int calls = 0;

        if (callsSourceMethod.size() == 0 && callsTargetMethod.size() == 0) {
            return 0;
        }
        MethodBean tmpSourceMethodCall = null;
        while (itSource.hasNext()) {
            tmpSourceMethodCall = (MethodBean) itSource.next();
            if (tmpSourceMethodCall.getFullQualifiedName().equals(targetMethodName)) {
                calls++;
            }
        }
        MethodBean tmpTargetMethodCall = null;
        while (itTarget.hasNext()) {
            tmpTargetMethodCall = (MethodBean) itTarget.next();
            if (tmpTargetMethodCall.getFullQualifiedName().equals(sourceMethodName)) {
                calls++;
            }
        }
        ccm = (double) calls / (callsSourceMethod.size() + callsTargetMethod.size());
        return ccm;
    }

    public static double[][] filterMatrix(double[][] methodByMethodMatrix, double pThreshold) {
        for (int i = 0; i < methodByMethodMatrix.length; i++) {
            for (int j = 0; j < methodByMethodMatrix.length; j++) {
                if (methodByMethodMatrix[i][j] < pThreshold)
                    methodByMethodMatrix[i][j] = 0;
            }
        }
        return methodByMethodMatrix;
    }


}
