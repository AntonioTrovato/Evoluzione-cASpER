package it.unisa.casper.analysis.code_smell_detection.swiss_army_knife;

import it.unisa.casper.TestConfig;
import it.unisa.casper.analysis.code_smell.SpaghettiCodeSmell;
import it.unisa.casper.analysis.code_smell.SwissArmyKnifeCodeSmell;
import it.unisa.casper.analysis.code_smell_detection.spaghetti_code.StructuralSpaghettiCodeStrategy;
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

public class StructuralSwissArmyKnifeStrategyTest extends TestConfig {
    @Mock
    private PsiParser parser= Mockito.mock(PsiParser.class);
    private List<PackageBean>[] packageList;
    private ClassBean classIsSmelly,classNotIsSmelly,classNotIsSmelly2;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        packageList = new List[]{new ArrayList<>()};
        super.setFileName("SwissArmy.java");
        super.setFileName("QualityMake.java");
        super.setFileName("Test_SP_2.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            classIsSmelly= packageBean.getClassList().get(0);
            classNotIsSmelly= packageBean.getClassList().get(1);
            classNotIsSmelly2=packageBean.getClassList().get(2);
        }
        classIsSmelly.getAffectedSmell().clear();
        classNotIsSmelly.getAffectedSmell().clear();
        classNotIsSmelly2.getAffectedSmell().clear();
    }

    @Test
    public void testIsSmelly() throws Exception {
        boolean isSmelly=false;
        StructuralSwissArmyKnifeCodeStrategy swissArmyKnifeStrategy=new StructuralSwissArmyKnifeCodeStrategy(3);
        SwissArmyKnifeCodeSmell smell=new SwissArmyKnifeCodeSmell (swissArmyKnifeStrategy,"Structural");
        isSmelly=classIsSmelly.isAffected(smell);
        assertTrue(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    @Test
    public void testNotIsSmelly() throws Exception {
        boolean isSmelly=false;
        StructuralSwissArmyKnifeCodeStrategy swissArmyKnifeStrategy=new StructuralSwissArmyKnifeCodeStrategy(3);
        SwissArmyKnifeCodeSmell smell=new SwissArmyKnifeCodeSmell (swissArmyKnifeStrategy,"Structural");
        isSmelly=classNotIsSmelly.isAffected(smell);
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }

    public void testNotIsSmelly2() throws Exception {
        boolean isSmelly=false;
        StructuralSwissArmyKnifeCodeStrategy swissArmyKnifeStrategy=new StructuralSwissArmyKnifeCodeStrategy(3);
        SwissArmyKnifeCodeSmell smell=new SwissArmyKnifeCodeSmell (swissArmyKnifeStrategy,"Structural");
        isSmelly=classNotIsSmelly2.isAffected(smell);
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }


}