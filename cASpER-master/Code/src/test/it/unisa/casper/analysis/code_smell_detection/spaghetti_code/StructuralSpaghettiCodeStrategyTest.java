package it.unisa.casper.analysis.code_smell_detection.spaghetti_code;

import it.unisa.casper.TestConfig;
import it.unisa.casper.analysis.code_smell.CodeSmell;
import it.unisa.casper.analysis.code_smell.SpaghettiCodeSmell;
import it.unisa.casper.parser.PsiParser;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.PackageBean;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class StructuralSpaghettiCodeStrategyTest extends TestConfig {
    @Mock
    private PsiParser parser= Mockito.mock(PsiParser.class);
    private List<PackageBean>[] packageList;
    private ClassBean classIsSmelly,classNotIsSmelly,classNotIsSmelly2,classNotIsSmelly3;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        packageList = new List[]{new ArrayList<>()};
        super.setFileName("Quality.java");
        super.setFileName("QualityMake.java");
        super.setFileName("QualityMakeRefactor.java");
        super.setFileName("ColloInVendita.java");


        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            classNotIsSmelly3= packageBean.getClassList().get(0);
            classIsSmelly= packageBean.getClassList().get(1);
            classNotIsSmelly= packageBean.getClassList().get(2);
            classNotIsSmelly2= packageBean.getClassList().get(3);


        }
        classIsSmelly.getAffectedSmell().clear();
        classNotIsSmelly.getAffectedSmell().clear();
        classNotIsSmelly2.getAffectedSmell().clear();
        classNotIsSmelly3.getAffectedSmell().clear();

    }

    @Test
    public void testIsSmelly() throws Exception {
        boolean isSmelly=false;
        StructuralSpaghettiCodeStrategy spaghettiCodeStrategy=new StructuralSpaghettiCodeStrategy(25);
        SpaghettiCodeSmell smell=new SpaghettiCodeSmell(spaghettiCodeStrategy,"Structural");
        isSmelly=classIsSmelly.isAffected(smell);
        assertTrue(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    @Test
    public void testNotIsSmelly() throws Exception {
        boolean isSmelly=false;
        StructuralSpaghettiCodeStrategy spaghettiCodeStrategy=new StructuralSpaghettiCodeStrategy(25);
        SpaghettiCodeSmell smell=new SpaghettiCodeSmell(spaghettiCodeStrategy,"Structural");
        isSmelly=classNotIsSmelly.isAffected(smell);
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    @Test
    public void testNotIsSmelly2() throws Exception {
        boolean isSmelly=false;
        StructuralSpaghettiCodeStrategy spaghettiCodeStrategy=new StructuralSpaghettiCodeStrategy(25);
        SpaghettiCodeSmell smell=new SpaghettiCodeSmell(spaghettiCodeStrategy,"Structural");
        isSmelly=classNotIsSmelly2.isAffected(smell);
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    @Test
    public void testNotIsSmelly3() throws Exception {
        boolean isSmelly=false;
        StructuralSpaghettiCodeStrategy spaghettiCodeStrategy=new StructuralSpaghettiCodeStrategy(25);
        SpaghettiCodeSmell smell=new SpaghettiCodeSmell(spaghettiCodeStrategy,"Structural");
        isSmelly=classNotIsSmelly3.isAffected(smell);
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }




}