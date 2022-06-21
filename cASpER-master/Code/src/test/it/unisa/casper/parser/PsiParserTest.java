package it.unisa.casper.parser;

import it.unisa.casper.TestConfig;
import it.unisa.casper.analysis.code_smell.CodeSmell;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.PackageBean;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PsiParserTest extends TestConfig {

    private PsiParser parser;
    private List<PackageBean>[] packageList;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        packageList = new List[]{new ArrayList<>()};
    }

    @Test
    public void testParserExtractClassBean() throws Exception {
        int numeroClassi=0;
        super.setFileName("QualityMake.java");
        super.setFileName("QualityMakeRefactor.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            numeroClassi=packageBean.getClassList().size();
        }
        assertEquals(2,numeroClassi);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("NumeroClassi\n" + numeroClassi);
    }

    @Test
    public void testParserExtractClasseEstesa1() throws Exception {
        int classiEstese=0;
        super.setFileName("QualityMake.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                if(classBean.getClasseEstesa()!=null)
                    classiEstese=1;
            }
        }
        assertTrue(classiEstese==0);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("ClassiEstese\n" + classiEstese);
    }

    @Test
    public void testParserExtractClasseEstesa2() throws Exception {
        int classiEstese=0;
        super.setFileName("ColloInVendita.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                if(classBean.getClasseEstesa()!=null)
                    classiEstese=1;
            }
        }
        assertTrue(classiEstese==1);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("ClassiEstese\n" + classiEstese);
    }

    @Test
    public void testParserExtractClassImplementate1() throws Exception {
        int classiImplementate=0;
        super.setFileName("QualityMake.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                if(classBean.getClassiImplementate()!=null)
                    classiImplementate=classBean.getClassiImplementate().size();
            }
        }
        assertTrue(classiImplementate==0);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("ClassiImplementate\n" + classiImplementate);
    }

    @Test
    public void testParserExtractClassImplementate2() throws Exception {
        int classiImplementate=0;
        super.setFileName("SwissArmy.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                if(classBean.getClassiImplementate()!=null)
                    classiImplementate=classBean.getClassiImplementate().size();
            }
        }
        assertTrue(classiImplementate==3);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("ClassiImplementate\n" + classiImplementate);
    }


    @Test
    public void testIntegrationSpaghettiCodeIsSmelly() throws Exception {
        boolean isSmelly=false;
        super.setFileName("QualityMake.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                for (CodeSmell codeSmell: classBean.getAffectedSmell())
                    if(codeSmell.getSmellName().equals("Spaghetti Code"))
                        isSmelly=true;
            }
        }
        assertTrue(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    @Test
    public void testIntegrationSpaghettiCodeNotIsSmelly() throws Exception {
        boolean isSmelly=false;
        super.setFileName("QualityMakeRefactor.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                for (CodeSmell codeSmell: classBean.getAffectedSmell())
                    if(codeSmell.getSmellName().equals("Spaghetti Code"))
                        isSmelly=true;
            }
        }
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    @Test
    public void testIntegrationSwissArmyCodeIsSmelly() throws Exception {
        boolean isSmelly=false;
        super.setFileName("SwissArmy.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                for (CodeSmell codeSmell: classBean.getAffectedSmell())
                    if(codeSmell.getSmellName().equals("Swiss Army Knife"))
                        isSmelly=true;
            }
        }
        assertTrue(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    @Test
    public void testIntegrationSwissArmyCodeNotIsSmelly() throws Exception {
        boolean isSmelly=false;
        super.setFileName("QualityMake.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                for (CodeSmell codeSmell: classBean.getAffectedSmell())
                    if(codeSmell.getSmellName().equals("Swiss Army Knife"))
                        isSmelly=true;
            }
        }
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

}