package it.unisa.casper;

import com.intellij.testFramework.fixtures.*;
import org.jetbrains.annotations.NotNull;

public class TestConfig extends LightJavaCodeInsightFixtureTestCase {

    private boolean setUpEseguito=false;

    public void setFileName(String fileName) throws Exception {
        if (setUpEseguito==false){
            super.setUp();
            setUpEseguito=true;
        }
        myFixture.configureByFile(fileName);
    }

    protected String getTestDataPath(){
        return "src/testData";
    }
}
