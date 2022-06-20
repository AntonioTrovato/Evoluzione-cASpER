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

public class PsiParserTest extends TestConfig {
    private PsiParser parser;
    private List<PackageBean>[] packageList;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        packageList = new List[]{new ArrayList<>()};
    }

    @Test
    public void testParser_01() throws Exception {

        super.setFileName("QualityMake.java");
        parser=new PsiParser(getProject());
        packageList[0]= parser.parse();
        for (PackageBean packageBean : packageList[0]){
            for (ClassBean classe: packageBean.getClassList())
                for(CodeSmell codeSmell:classe.getAffectedSmell()){
                    assertEquals("Spaghetti Code",codeSmell.getSmellName());
                }
        }
    }


}