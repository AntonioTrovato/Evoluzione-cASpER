package it.unisa.casper.analysis.code_smell_detection.spaghetti_code;

import it.unisa.casper.analysis.code_smell.BlobCodeSmell;
import it.unisa.casper.analysis.code_smell.SpaghettiCodeSmell;
import it.unisa.casper.storage.beans.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class StructuralSpaghettiCodeStrategyTest {

    private MethodList methods;
    private MethodBean metodo,methodRefactoring,methodDecrement,methodUpdate,methodUpdate2, methodIncrement;

    private ClassBean isSmelly,noSmelly,belogingClass,belogingClass2;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        methods=new MethodList();
        InstanceVariableBeanList instances=new InstanceVariableList();
        instances.getList().add(new InstanceVariableBean("items","Item[]","","private"));
        belogingClass=new ClassBean.Builder("spaghettiCode.package.QualityMake", "private Item[] items;\n"+
                "public Quality(Item[] items){\n"+
                "this.items=items;\n"+
                "}\n"+
                "public void UpdateQuality(){\n"+
                "for (int i=0;i<items.lenght;i++)\n"+
                "{\n"+
                "if((!items[i].getName().equals(\"Aged Brie\") && (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getQuality() > 0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "if (items[i].getQuality() < 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "if (items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getSellIn() < 11)\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "if (items[i].getSellIn() < 6)\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setSellIn(items[i].getSellIn() - 1);\n"+
                "}\n"+
                "if (items[i].getSellIn() < 0)\n"+
                "{\n"+
                "if(!items[i].getName().equals(\"Aged Brie\"))\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getQuality()>0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "item[i].setQuality=0;\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n" )
                .setInstanceVariables(instances)
                .setMethods(methods)
                .setImports(new ArrayList<String>())
                .setLOC(73)
                .setSuperclass(null)
                .setBelongingPackage(null)
                .setEnviedPackage(null)
                .setEntityClassUsage(0)
                .setPathToFile("")
                .setAffectedSmell()
                .build();



        instances=new InstanceVariableList();
        MethodBeanList listaMetodiChiamati = new MethodList();
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getName()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getQuantity()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.setQuantity()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.setSellIn()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getSellIn()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.equals()", "").build());
        HashMap<String, ClassBean> parametriMetodo = new HashMap<String, ClassBean>();
        metodo=new MethodBean.Builder("spaghettiCode.package.QualityMake.UpdateQuality()","for (int i=0;i<items.lenght;i++)\n"+
                "{\n"+
                "if((!items[i].getName().equals(\"Aged Brie\") && (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getQuality() > 0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "if (items[i].getQuality() < 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "if (items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getSellIn() < 11)\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "if (items[i].getSellIn() < 6)\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setSellIn(items[i].getSellIn() - 1);\n"+
                "}\n"+
                "if (items[i].getSellIn() < 0)\n"+
                "{\n"+
                "if(!items[i].getName().equals(\"Aged Brie\"))\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getQuality()>0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "item[i].setQuality=0;\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"
                )
                .setReturnType(new ClassBean.Builder("Void", "").build())
                .setInstanceVariableList(instances)
                .setMethodsCalls(listaMetodiChiamati)
                .setParameters(parametriMetodo)
                .setStaticMethod(false)
                .setDefaultCostructor(false)
                .setBelongingClass(belogingClass)
                .setVisibility("public")
                .setAffectedSmell()
                .build();

        methods=new MethodList();
        methods.getList().add(metodo);
        instances=new InstanceVariableList();
        instances.getList().add(new InstanceVariableBean("items","Item[]","","private"));
        isSmelly=new ClassBean.Builder("spaghettiCode.package.QualityMake", "private Item[] items;\n"+
                "public Quality(Item[] items){\n"+
                "this.items=items;\n"+
                "}\n"+
                "public void UpdateQuality(){\n"+
                "for (int i=0;i<items.lenght;i++)\n"+
                "{\n"+
                "if((!items[i].getName().equals(\"Aged Brie\") && (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getQuality() > 0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "if (items[i].getQuality() < 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "if (items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getSellIn() < 11)\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "if (items[i].getSellIn() < 6)\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setSellIn(items[i].getSellIn() - 1);\n"+
                "}\n"+
                "if (items[i].getSellIn() < 0)\n"+
                "{\n"+
                "if(!items[i].getName().equals(\"Aged Brie\"))\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "if (items[i].getQuality()>0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "item[i].setQuality=0;\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n" )
                .setInstanceVariables(instances)
                .setMethods(methods)
                .setImports(new ArrayList<String>())
                .setLOC(73)
                .setSuperclass(null)
                .setBelongingPackage(null)
                .setEnviedPackage(null)
                .setEntityClassUsage(0)
                .setPathToFile("")
                .setAffectedSmell()
                .build();



        //Preparo la classe non affetta da smell
        belogingClass2=new ClassBean.Builder("spaghettiCode.package.QualityMake", "private Item[] items;\n"+
                "public Quality(Item[] items){\n"+
                "this.items=items;\n"+
                "}\n"+
                "Public void UpdateQuality(int i){\n"+
                "for (int i=0;i<items.lenght;i++)\n"+
                "{\n"+
                "if((!items[i].getName().equals(\"Aged Brie\") && (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "DecrementQualityForNormalItems(i);\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "UpdateQualityForItemsThatAgeWell(i);\n"+
                "}\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setSellIn(items[i].getSellIn() - 1);\n"+
                "}\n"+
                "if (items[i].getSellIn() < 0)\n"+
                "{\n"+
                "UpdateQualityForExpiredItems(i);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void DecrementQualityForNormalItems(int i){\n"+
                "if (items[i].getQuality() > 0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void UpdateQualityForExpiredItems(int i){\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "if (items[i].getSellIn()<11){\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "if (items[i].getSellIn()<6){\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void UpdateQualityForItemsThatAgeWell(int i){\n"+
                "if(!items[i].getName().equals(\"Aged Brie\"))\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "DecrementQualityForNormalItems(i)\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "item[i].setQuality=0;\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void IncrementQuality(int i){\n"+
                "if (items[i].getQuality() < 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"
        )
                .setInstanceVariables(instances)
                .setMethods(null)
                .setImports(new ArrayList<String>())
                .setLOC(73)
                .setSuperclass(null)
                .setBelongingPackage(null)
                .setEnviedPackage(null)
                .setEntityClassUsage(0)
                .setPathToFile("")
                .setAffectedSmell()
                .build();



        instances=new InstanceVariableList();
        listaMetodiChiamati = new MethodList();
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getName()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.QualityMake.DecrementQualityForNormalItems()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.QualityMake.UpdateQualityForItemsThatAgeWell()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.QualityMake.UpdateQualityForExpiredItems();", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.setSellIn()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getSellIn()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.equals()", "").build());
        parametriMetodo = new HashMap<String, ClassBean>();
        methodRefactoring=new MethodBean.Builder("spaghettiCode.package.QualityMake.UpdateQuality()","for (int i=0;i<items.lenght;i++){\n"+
                "if((!items[i].getName().equals(\"Aged Brie\") && (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\")){\n"+
                "DecrementQualityForNormalItems(i);\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "UpdateQualityForItemsThatAgeWell(i);\n"+
                "}\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setSellIn(items[i].getSellIn() - 1);\n"+
                "}\n"+
                "if (items[i].getSellIn() < 0)\n"+
                "{\n"+
                "UpdateQualityForExpiredItems(i);\n"+
                "}\n"+
                "}\n"+
                "}\n"
        )
                .setReturnType(new ClassBean.Builder("Void", "").build())
                .setInstanceVariableList(instances)
                .setMethodsCalls(listaMetodiChiamati)
                .setParameters(parametriMetodo)
                .setStaticMethod(false)
                .setDefaultCostructor(false)
                .setBelongingClass(belogingClass2)
                .setVisibility("public")
                .setAffectedSmell()
                .build();


        instances=new InstanceVariableList();
        listaMetodiChiamati = new MethodList();
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getQuality()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.setQuality()", "").build());
        parametriMetodo = new HashMap<String, ClassBean>();
        parametriMetodo.put("i",new ClassBean.Builder("int","").build());
        methodIncrement=new MethodBean.Builder("spaghettiCode.package.QualityMake.IncrementQuality()","{\n"+
                "if (items[i].getQuality() < 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"
        )
                .setReturnType(new ClassBean.Builder("Void", "").build())
                .setInstanceVariableList(instances)
                .setMethodsCalls(listaMetodiChiamati)
                .setParameters(parametriMetodo)
                .setStaticMethod(false)
                .setDefaultCostructor(false)
                .setBelongingClass(belogingClass2)
                .setVisibility("private")
                .setAffectedSmell()
                .build();


        instances=new InstanceVariableList();
        listaMetodiChiamati = new MethodList();
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getName()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getQuality()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.setQuality()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.equals()", "").build());
        parametriMetodo = new HashMap<String, ClassBean>();
        parametriMetodo.put("i",new ClassBean.Builder("int","").build());
        methodDecrement=new MethodBean.Builder("spaghettiCode.package.QualityMake.DecrementQualityForNormalItems()","{\n"+
                "if (items[i].getQuality() > 0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"
        )
                .setReturnType(new ClassBean.Builder("Void", "").build())
                .setInstanceVariableList(instances)
                .setMethodsCalls(listaMetodiChiamati)
                .setParameters(parametriMetodo)
                .setStaticMethod(false)
                .setDefaultCostructor(false)
                .setBelongingClass(belogingClass2)
                .setVisibility("private")
                .setAffectedSmell()
                .build();


        instances=new InstanceVariableList();
        listaMetodiChiamati = new MethodList();
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getName()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getQuality()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.setQuality()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.equals()", "").build());
        parametriMetodo = new HashMap<String, ClassBean>();
        parametriMetodo.put("i",new ClassBean.Builder("int","").build());
        methodUpdate=new MethodBean.Builder("spaghettiCode.package.QualityMake.UpdateQualityForExpiredItems()","{\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "if (items[i].getSellIn()<11){\n"+
                "IncrementQuality(i)\n"+
                "}\n"+
                "if (items[i].getSellIn()<6){\n"+
                "IncrementQuality(i)\n"+
                "}\n"+
                "}\n"
        )
                .setReturnType(new ClassBean.Builder("Void", "").build())
                .setInstanceVariableList(instances)
                .setMethodsCalls(listaMetodiChiamati)
                .setParameters(parametriMetodo)
                .setStaticMethod(false)
                .setDefaultCostructor(false)
                .setBelongingClass(belogingClass2)
                .setVisibility("private")
                .setAffectedSmell()
                .build();

        instances=new InstanceVariableList();
        listaMetodiChiamati = new MethodList();
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getName()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.getQuality()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.setQuality()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.QualityMake.DecrementQualityForNormalItems()", "").build());
        listaMetodiChiamati.getList().add(new MethodBean.Builder("spaghettiCode.package.Item.equals()", "").build());
        parametriMetodo = new HashMap<String, ClassBean>();
        parametriMetodo.put("i",new ClassBean.Builder("int","").build());
        methodUpdate2=new MethodBean.Builder("spaghettiCode.package.QualityMake.UpdateQualityForItemsThatAgeWell()","{\n"+
                "if(!items[i].getName().equals(\"Aged Brie\"))\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "DecrementQualityForNormalItems(i)\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "item[i].setQuality=0;\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "IncrementQuality(i)\n"+
                "}\n"+
                "}\n"
        )
                .setReturnType(new ClassBean.Builder("Void", "").build())
                .setInstanceVariableList(instances)
                .setMethodsCalls(listaMetodiChiamati)
                .setParameters(parametriMetodo)
                .setStaticMethod(false)
                .setDefaultCostructor(false)
                .setBelongingClass(belogingClass2)
                .setVisibility("private")
                .setAffectedSmell()
                .build();

        methods=new MethodList();
        methods.getList().add(methodRefactoring);
        methods.getList().add(methodUpdate);
        methods.getList().add(methodUpdate2);
        methods.getList().add(methodRefactoring);
        noSmelly=new ClassBean.Builder("spaghettiCode.package.QualityMake", "private Item[] items;\n"+
                "public Quality(Item[] items){\n"+
                "this.items=items;\n"+
                "}\n"+
                "Public void UpdateQuality(int i){\n"+
                "for (int i=0;i<items.lenght;i++){\n"+
                "if((!items[i].getName().equals(\"Aged Brie\") && (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\")){\n"+
                "DecrementQualityForNormalItems(i);\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "UpdateQualityForItemsThatAgeWell(i);\n"+
                "}\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setSellIn(items[i].getSellIn() - 1);\n"+
                "}\n"+
                "if (items[i].getSellIn() < 0)\n"+
                "{\n"+
                "UpdateQualityForExpiredItems(i);\n"+
                "}\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void DecrementQualityForNormalItems(int i){\n"+
                "if (items[i].getQuality() > 0)\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Sulfuras, Hand of Ragnaros\"))\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()-1);\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void UpdateQualityForExpiredItems(int i){\n"+
                "IncermentQuality(i)\n"+
                "if (items[i].getSellIn()<11){\n"+
                "if (items[i].getQuality()< 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"+
                "if (items[i].getSellIn()<6){\n"+
                "IncermentQuality(i)\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void UpdateQualityForItemsThatAgeWell(int i){\n"+
                "if(!items[i].getName().equals(\"Aged Brie\"))\n"+
                "{\n"+
                "if (!items[i].getName().equals(\"Backstage passes to a TAFKAL80ETC concert\"))\n"+
                "{\n"+
                "DecrementQualityForNormalItems(i)\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "item[i].setQuality=0;\n"+
                "}\n"+
                "}\n"+
                "else\n"+
                "{\n"+
                "IncermentQuality(i)\n"+
                "}\n"+
                "}\n"+
                "}\n"+

                "Public void IncrementQuality(int i){\n"+
                "if (items[i].getQuality() < 50)\n"+
                "{\n"+
                "items[i].setQuality(Items[i].getQuality()+1);\n"+
                "}\n"+
                "}\n"
               )
                .setInstanceVariables(instances)
                .setMethods(methods)
                .setImports(new ArrayList<String>())
                .setLOC(73)
                .setSuperclass(null)
                .setBelongingPackage(null)
                .setEnviedPackage(null)
                .setEntityClassUsage(0)
                .setPathToFile("")
                .setAffectedSmell()
                .build();

    }

    @org.junit.jupiter.api.Test
    void isSmellyTrue() {
        StructuralSpaghettiCodeStrategy strategy=new StructuralSpaghettiCodeStrategy(20);
        SpaghettiCodeSmell smell = new SpaghettiCodeSmell(strategy, "Structural");
        boolean risultato = isSmelly.isAffected(smell);
        assertTrue(isSmelly.getAffectedSmell().contains(smell));
        Logger log = Logger.getLogger(getClass().getName());
        log.info("\n" + risultato);
        assertTrue(risultato);
    }

    @org.junit.jupiter.api.Test
    void isSmellyFalse() {
        StructuralSpaghettiCodeStrategy strategy=new StructuralSpaghettiCodeStrategy(20);
        SpaghettiCodeSmell smell = new SpaghettiCodeSmell(strategy, "Structural");
        boolean risultato = noSmelly.isAffected(smell);
        assertFalse(noSmelly.getAffectedSmell().contains(smell));
        Logger log = Logger.getLogger(getClass().getName());
        log.info("\n" + risultato);
        assertFalse(risultato);
    }
}