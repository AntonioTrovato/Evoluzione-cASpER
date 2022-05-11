package it.unisa.casper.analysis.history_analysis_utility;

import it.unisa.casper.analysis.code_smell.CodeSmell;
import it.unisa.casper.storage.beans.ClassBean;

public class AnalyzerThread implements Runnable{

    private ClassBean classBean;
    private CodeSmell codeSmell;

    public AnalyzerThread(ClassBean classBean, CodeSmell codeSmell){
        this.classBean = classBean;
        this.codeSmell = codeSmell;
    }

    @Override
    public void run() {
        this.classBean.isAffected(this.codeSmell);
    }
}
