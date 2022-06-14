package it.unisa.casper.analysis.code_smell_detection.spaghetti_code;

import it.unisa.casper.storage.beans.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StructuralSpaghettiCodeStrategyTest {

    private MethodList methods;
    private MethodBean metodo;
    private ClassBean classe;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        InstanceVariableBeanList instances=new InstanceVariableList();
        MethodBeanList vuota = new MethodList();
        HashMap<String, ClassBean> nulla = new HashMap<String, ClassBean>();
        nulla.put("balance",new ClassBean.Builder("int", "").build());
        nulla.put("balance",new ClassBean.Builder("int", "").build());
        instances.getList().add(new InstanceVariableBean("balance", "double", "", "private "));
        metodo= new MethodBean.Builder("blob.package.BankAccount.getBalance", "blance=10;\n"+
                "return balance;")
                .setReturnType(new ClassBean.Builder("Double", "").build())
                .setInstanceVariableList(instances)
                .setMethodsCalls(vuota)
                .setParameters(nulla)
                .setStaticMethod(false)
                .setDefaultCostructor(false)
                .setBelongingClass(null)
                .setVisibility("public")
                .setAffectedSmell()
                .build();

        methods=new MethodList();
        methods.getList().add(metodo);
        classe=new ClassBean.Builder("blob.package.BankAccount", "private final Double balance;\n"+
                "public BankAccunt(Double balance){\n"+
                "this.balance=balance;\n"+
                "}\n"+
                "public Double getBalance(){\n"+
                "return this.balance\n"+
                "}")
                .setInstanceVariables(instances)
                .setMethods(methods)
                .setImports(new ArrayList<String>())
                .setLOC(8)
                .setSuperclass(null)
                .setBelongingPackage(null)
                .setEnviedPackage(null)
                .setEntityClassUsage(1)
                .setPathToFile("")
                .setAffectedSmell()
                .build();
    }

    @org.junit.jupiter.api.Test
    void isSmelly() {
        StructuralSpaghettiCodeStrategy test=new StructuralSpaghettiCodeStrategy(10);
        test.isSmelly(classe);

    }
}