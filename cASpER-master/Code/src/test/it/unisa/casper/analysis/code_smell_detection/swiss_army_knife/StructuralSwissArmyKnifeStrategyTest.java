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
    private ClassBean classIsSmelly,classNotIsSmelly;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        packageList = new List[]{new ArrayList<>()};
    }

    @Test
    public void testIsSmelly() throws Exception {
        boolean isSmelly=false;
        super.setFileName("SwissArmy.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                classIsSmelly=classBean;
            }
        }
        classIsSmelly.getAffectedSmell().clear();
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
        super.setFileName("QualityMake.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classBean: packageBean.getClassList()){
                classNotIsSmelly=classBean;
            }
        }
        classNotIsSmelly.getAffectedSmell().clear();
        StructuralSwissArmyKnifeCodeStrategy swissArmyKnifeStrategy=new StructuralSwissArmyKnifeCodeStrategy(3);
        SwissArmyKnifeCodeSmell smell=new SwissArmyKnifeCodeSmell (swissArmyKnifeStrategy,"Structural");
        isSmelly=classNotIsSmelly.isAffected(smell);
        assertFalse(isSmelly);
        java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
        log.info("isSmelly\n" + isSmelly);
    }


}