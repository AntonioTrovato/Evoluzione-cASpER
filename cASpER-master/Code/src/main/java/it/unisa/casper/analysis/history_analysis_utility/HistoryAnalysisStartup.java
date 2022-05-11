package it.unisa.casper.analysis.history_analysis_utility;

import it.unisa.casper.analysis.code_smell.CodeSmell;
import it.unisa.casper.analysis.code_smell.FeatureEnvyCodeSmell;
import it.unisa.casper.storage.beans.ClassBean;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class HistoryAnalysisStartup {



    private String dir;

    public HistoryAnalysisStartup(String dir) {
        this.dir = dir + File.separator + "HistoryAnaliysisScripts";
        File directory = new File(this.dir);
        directory.mkdir();
    }

    public HistoryAnalysisStartup() {
    }

    public void writeScripts(){
        //crea script per la detection del blob
        createFile("blob.py", BLOB_DETECTION);
        createFile("FeatureEnvy.py", FEATURE_ENVY_DETECTION);
        createFile("ShotgunSurgery.py", SHOTGUN_SURGERY_DETECTION);
        createFile("DivergentChange.py", DIVERGENT_CHANGE_DETECTION);
        createFile("ParallelInheritance.py", PARALLEL_INHERITANCE_DETECTION);

    }

    private void createFile(String fileName, String fileText){
        FileWriter f = null;
        try {
            f = new FileWriter(this.dir + File.separator + fileName);
            BufferedWriter out = new BufferedWriter(f);
            out.write(fileText);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void savePythonExePath(String path){
        //System.getProperty("user.home") + File.separator + ".casper" + File.separator + "threshold.txt"
        FileWriter f = null;
        try {
            f = new FileWriter(System.getProperty("user.home") + File.separator + ".casper" + File.separator + "pythonPath.txt");
            BufferedWriter out = new BufferedWriter(f);
            out.write(path);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDir() {
        return dir;
    }

    public static String historyPriority(CodeSmell codeSmell){
            switch (codeSmell.getSmellName()) {
                case "Blob":
                    return blobPriority(codeSmell);
                case "Feature Envy":
                    return getFeatureEnvyPriority(codeSmell);
                case "Divergent Change":
                    return divergentChangePriority(codeSmell);
                case "Shotgun Surgery":
                    return shotgunSurgeryPriority(codeSmell);
                case "Parallel Inheritance":
                    return getParallelInheritancePriority(codeSmell);
            }

        return "";
    }

    private static String getFeatureEnvyPriority(CodeSmell codeSmell){
        HashMap<String, Double> threshold = codeSmell.getIndex();
        Double value = threshold.get("threshold");

        if(value > 0 && value <= 5){
            return "low";
        }else{
            if(value > 5 && value <= 10 ){
                return "medium";
            }else{
                if(value > 15 && value <= 20){
                    return "high";
                }else{
                    return "urgent";
                }
            }
        }
    }

    private static String getParallelInheritancePriority(CodeSmell codeSmell){
        HashMap<String, Double> threshold = codeSmell.getIndex();
        Double value = threshold.get("threshold");

        if(value > 0 && value <= 20){
            return "low";
        }else{
            if(value > 20 && value <= 30 ){
                return "medium";
            }else{
                if(value > 30 && value <= 40){
                    return "high";
                }else{
                    return "urgent";
                }
            }
        }
    }

    private static String shotgunSurgeryPriority(CodeSmell codeSmell){
        HashMap<String, Double> threshold = codeSmell.getIndex();
        Double value = threshold.get("threshold");

        if(value > 0 && value <= 2){
            return "low";
        }else{
            if(value > 2 && value <= 4 ){
                return "medium";
            }else{
                if(value > 4 && value <= 6){
                    return "high";
                }else{
                    return "urgent";
                }
            }
        }
    }

    private static String divergentChangePriority(CodeSmell codeSmell){
        HashMap<String, Double> threshold = codeSmell.getIndex();
        Double value = threshold.get("threshold");

        if(value > 0 && value <= 2){
            return "low";
        }else{
            if(value > 2 && value <= 4 ){
                return "medium";
            }else{
                if(value > 4 && value <= 6){
                    return "high";
                }else{
                    return "urgent";
                }
            }
        }
    }

    private static String blobPriority(CodeSmell codeSmell){
        HashMap<String, Double> threshold = codeSmell.getIndex();
        Double value = threshold.get("threshold") - 8;

        if(value >= 0 && value <= 2){
            return "low";
        }else{
            if(value > 2 && value <= 10 ){
                return "medium";
            }else{
                if(value > 10 && value <= 15){
                    return "high";
                }else{
                    return "urgent";
                }
            }
        }
    }

    private static final String PARALLEL_INHERITANCE_DETECTION = "from pydriller import RepositoryMining\n" +
            "\n" +
            "\n" +
            "def contaClassiJava(commit):\n" +
            "    count = 0\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if 'java' in modifiedFile.filename:\n" +
            "            count = count + 1\n" +
            "    return count\n" +
            "\n" +
            "\n" +
            "def verifyInheritance(modiefiedFile):\n" +
            "    for x in modifiedFile.diff_parsed[\"added\"]:\n" +
            "        for item in x:\n" +
            "            if 'extends' in str(item):\n" +
            "                return True\n" +
            "    return False\n" +
            "\n" +
            "def verifySmell(commit, affectedBoolPar):\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if '.java' in modifiedFile.filename:\n" +
            "            if (verifyInheritance (modifiedFile)):\n" +
            "                affectedBoolPar = affectedBoolPar and True\n" +
            "            else:\n" +
            "                affectedBoolPar = affectedBoolPar and False\n" +
            "    return affectedBoolPar\n" +
            "\n" +
            "# leggo la classe da analizzare\n" +
            "#ParallelInheritanceSub1\n" +
            "classe = input()\n" +
            "#leggo il path della repo\n" +
            "#C:\\Users\\faded\\Desktop\\CasperTest\n" +
            "pathToRepo = input()\n" +
            "\n" +
            "checkBool = False\n" +
            "# MAIN\n" +
            "for commit in RepositoryMining(pathToRepo,\n" +
            "                               only_modifications_with_file_types=['.java']).traverse_commits():\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "\n" +
            "        # verifico la presenta della classe in analisi\n" +
            "        if classe == modifiedFile.filename and contaClassiJava(commit) == 2:\n" +
            "            affectedBool = True\n" +
            "            checkBool = verifySmell(commit, affectedBool)\n" +
            "            result = \"\"\n" +
            "            if checkBool:\n" +
            "                for modifiedFile in commit.modifications:\n" +
            "                    if '.java' in modifiedFile.filename:\n" +
            "                        result = result + modifiedFile.filename + \",\"\n" +
            "\n" +
            "\n" +
            "if checkBool:\n" +
            "    print('true'+ ',' + result + str(2))\n" +
            "else:\n" +
            "    print ('false')";

    private static final String DIVERGENT_CHANGE_DETECTION = "from pydriller import RepositoryMining\n" +
            "\n" +
            "# lista contente i set dei metodi\n" +
            "methodList = []\n" +
            "\n" +
            "def formatResult():\n" +
            "    result = ''\n" +
            "    for x in methodList:\n" +
            "        result = result + ','\n" +
            "        for method in x:\n" +
            "            data = method.name.split(\"::\")\n" +
            "            result = result + '-' + data[len(data) - 1]\n" +
            "\n" +
            "    return result\n" +
            "\n" +
            "def contaClassiJava(commit):\n" +
            "    count = 0\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if 'java' in modifiedFile.filename:\n" +
            "            count = count + 1\n" +
            "    return count\n" +
            "\n" +
            "# leggo la classe da analizzare\n" +
            "classe = input()\n" +
            "#leggo il path della repo\n" +
            "pathToRepo = input()\n" +
            "# MAIN\n" +
            "vartest = 0\n" +
            "for commit in RepositoryMining(pathToRepo,\n" +
            "                               only_modifications_with_file_types=['.java']).traverse_commits():\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        #verifico la presenta della classe in analisi\n" +
            "        if classe == modifiedFile.filename and contaClassiJava(commit) == 1:\n" +
            "            setMetodi = set()\n" +
            "            setMetodi.update(modifiedFile.changed_methods)\n" +
            "            if len(setMetodi) >= 3:\n" +
            "                methodList.append(setMetodi)\n" +
            "\n" +
            "\n" +
            "x = 0\n" +
            "y = 1\n" +
            "bool = True\n" +
            "while x < len(methodList)-1:\n" +
            "    while y < len(methodList):\n" +
            "        insiemeA = methodList[x]\n" +
            "        insiemeB = methodList[y]\n" +
            "        insiemeIntersezione = insiemeA.intersection(insiemeB)\n" +
            "        if len(insiemeIntersezione) != 0:\n" +
            "            bool = False\n" +
            "        y = y + 1\n" +
            "    x = x + 1\n" +
            "    y = x + 1\n" +
            "\n" +
            "\n" +
            "#se rispettano i requisiti\n" +
            "\n" +
            "if len(methodList) < 2:\n" +
            "    print('false,' + str(0))\n" +
            "else:\n" +
            "    if len(methodList) > 0 and bool:\n" +
            "        print('true' + formatResult() + ',' + str(len(methodList)))\n" +
            "    else:\n" +
            "        print('false,' + str(0))\n" +
            "\n";

    private static final String SHOTGUN_SURGERY_DETECTION = "from pydriller import RepositoryMining\n" +
            "\n" +
            "# commit con più classi affette\n" +
            "affectedCommit = None\n" +
            "\n" +
            "#restiruisce True se nel commit tutte le classi java hanno almeno un metodo modificato, false altrimenti\n" +
            "def checkMethods(commit):\n" +
            "    boolean = True\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if '.java' in modifiedFile.filename:\n" +
            "            if len(modifiedFile.changed_methods) != 0:\n" +
            "                boolean = boolean and True\n" +
            "            else:\n" +
            "                boolean = boolean and False\n" +
            "    return boolean\n" +
            "\n" +
            "def contaClassiJava(commit):\n" +
            "    count = 0\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if 'java' in modifiedFile.filename:\n" +
            "            count = count + 1\n" +
            "    return count\n" +
            "\n" +
            "# restituisce True se il numro di classi modificate nel commit è >= 4, False altrimenti.\n" +
            "def checkNumberOfClass(commit):\n" +
            "\n" +
            "    count = contaClassiJava(commit)\n" +
            "\n" +
            "    if count >= 4:\n" +
            "        return True\n" +
            "    else:\n" +
            "        return False\n" +
            "\n" +
            "def formatMethodName(nomeMetodo):\n" +
            "    s = nomeMetodo.split (\"::\");\n" +
            "    return s[len (s) - 1]\n" +
            "\n" +
            "\n" +
            "\n" +
            "def formatResult(commit):\n" +
            "\n" +
            "    result = ''\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if 'java' in modifiedFile.filename:\n" +
            "            result = result + ','+ modifiedFile.filename\n" +
            "            for x in modifiedFile.changed_methods:\n" +
            "                result = result + '-'+ formatMethodName(x.name)\n" +
            "\n" +
            "    return result\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "# leggo la classe da analizzare\n" +
            "# ClienteBean\n" +
            "classe = input()\n" +
            "#leggo il path della repo\n" +
            "pathToRepo = input()\n" +
            "\n" +
            "max = 0\n" +
            "# MAIN\n" +
            "for commit in RepositoryMining(pathToRepo,\n" +
            "                               only_modifications_with_file_types=['.java']).traverse_commits():\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        #verifico se la classe da analizzare è presente nel commit\n" +
            "        if classe == modifiedFile.filename:\n" +
            "            if checkNumberOfClass(commit):\n" +
            "                if checkMethods(commit):\n" +
            "                    numClassiJava = contaClassiJava(commit)\n" +
            "                    if(affectedCommit is None):\n" +
            "                        max = numClassiJava\n" +
            "                        affectedCommit = commit\n" +
            "                    elif numClassiJava > max:\n" +
            "                        max = numClassiJava\n" +
            "                        affectedCommit = commit\n" +
            "\n" +
            "\n" +
            "#stampa il risultato\n" +
            "if(affectedCommit is not None):\n" +
            "    print('true' + formatResult(affectedCommit) + ',' + str(max-3))\n" +
            "else:\n" +
            "    print('false,' + str(0))";

    private static final String FEATURE_ENVY_DETECTION = "from pydriller import RepositoryMining\n" +
            "\n" +
            "# dizionario che contiene le coppie (CLASSE , #COMMIT)\n" +
            "dizionario = {}\n" +
            "# commit totali della classe con il metodo invidiato\n" +
            "totalCommit = 0\n" +
            "# classe che invidia il metodo\n" +
            "enviousClass = None\n" +
            "\n" +
            "\n" +
            "def contaClassiJava(commit):\n" +
            "    count = 0\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if 'java' in modifiedFile.filename:\n" +
            "            count = count + 1\n" +
            "    return count\n" +
            "\n" +
            "\n" +
            "def controllaClasseInvidiose(commit):\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if '.java' in modifiedFile.filename and not classe == modifiedFile.filename:\n" +
            "            #se ho modificato almeno un metodo\n" +
            "            if len(modifiedFile.changed_methods) != 0:\n" +
            "                if modifiedFile.filename in dizionario:\n" +
            "                    dizionario[modifiedFile.filename] = dizionario[modifiedFile.filename] + 1\n" +
            "                else:\n" +
            "                    dizionario[modifiedFile.filename] = 1\n" +
            "\n" +
            "\n" +
            "\n" +
            "# leggo il metodo da analizzare\n" +
            "# getEmail\n" +
            "metodo = input()\n" +
            "\n" +
            "# leggo la classe del metodo da analizzare\n" +
            "#ClienteBean\n" +
            "classe = input()\n" +
            "\n" +
            "#leggo il path della repo\n" +
            "pathToRepo = input()\n" +
            "\n" +
            "# MAIN\n" +
            "for commit in RepositoryMining(pathToRepo,\n" +
            "                               only_modifications_with_file_types=['.java']).traverse_commits():\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if classe == modifiedFile.filename:\n" +
            "            #metodi modificati della classe analizzata\n" +
            "            for method in modifiedFile.changed_methods:\n" +
            "                #verifico se il metodo è stato modificato\n" +
            "                if metodo in method.name:\n" +
            "                    #conto il num. di classi java modificate nel commit in analisi\n" +
            "                    numClassiJava = contaClassiJava(commit)\n" +
            "                    # il metodo è stato modificato insieme a metodi della classe stessa\n" +
            "                    if numClassiJava == 1:\n" +
            "                        totalCommit = totalCommit + 1\n" +
            "                    #il metodo è stato modificato insieme a metodi di un'altra classe\n" +
            "                    elif numClassiJava == 2:\n" +
            "                        controllaClasseInvidiose(commit)\n" +
            "\n" +
            "totalCommitIncrementato = totalCommit + (totalCommit * 80 / 100)\n" +
            "max = 0;\n" +
            "for x in dizionario:\n" +
            "    if dizionario[x] >= totalCommitIncrementato and dizionario[x] > max:\n" +
            "        max = dizionario[x]\n" +
            "        enviousClass = x\n" +
            "\n" +
            "if(enviousClass is not None):\n" +
            "    print('true,' + enviousClass + ',' + str(max))\n" +
            "else:\n" +
            "    print('false,' + str(0))";

    private static final String BLOB_DETECTION="from pydriller import RepositoryMining\n" +
            "from pydriller import ModificationType\n" +
            "\n" +
            "# lista contente l'hash dei commit che rispettano il filtro\n" +
            "commitsList = []\n" +
            "\n" +
            "# dizionario che contiene le coppie (CLASSE , #MODIFICHE)\n" +
            "dizionario = {}\n" +
            "\n" +
            "# lista di blob\n" +
            "blobList = []\n" +
            "\n" +
            "#Leggo classe da verificare\n" +
            "classe = input()\n" +
            "#leggo il path della repo\n" +
            "pathToRepo = input()\n" +
            "\n" +
            "\n" +
            "# seleziona i commit con un numero di file.java modificati >= 2\n" +
            "def commitFilter(commit):\n" +
            "    count = 0\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if '.java' in modifiedFile.filename:\n" +
            "            count += 1\n" +
            "\n" +
            "    if count >= 2:\n" +
            "        return True\n" +
            "    else:\n" +
            "        return False\n" +
            "\n" +
            "\n" +
            "# conta il numero di modifiche per ogni classe\n" +
            "def countMod(commitHash):\n" +
            "    for commit in RepositoryMining(pathToRepo,\n" +
            "                                   single=commitHash).traverse_commits():\n" +
            "        for modifiedFile in commit.modifications:\n" +
            "            if '.java' in modifiedFile.filename:\n" +
            "                # verifica se il file è già presente\n" +
            "                if modifiedFile.filename in dizionario:\n" +
            "                    dizionario[modifiedFile.filename] = dizionario[modifiedFile.filename] + 1\n" +
            "                else:\n" +
            "                    dizionario[modifiedFile.filename] = 1\n" +
            "\n" +
            "\n" +
            "def calculaPercentualePresenza():\n" +
            "    numTotCommits = len(commitsList)\n" +
            "\n" +
            "    for x in dizionario:\n" +
            "        dizionario[x] = dizionario[x] / numTotCommits * 100\n" +
            "        temp = dizionario[x]\n" +
            "        # verifica se si tratta di un blob\n" +
            "        if temp >= 8:\n" +
            "            blobList.append(x)\n" +
            "\n" +
            "\n" +
            "# MAIN\n" +
            "for commit in RepositoryMining(pathToRepo,\n" +
            "                               only_modifications_with_file_types=['.java']).traverse_commits():\n" +
            "\n" +
            "\n" +
            "\n" +
            "    if commitFilter(commit):\n" +
            "        commitsList.append(commit.hash)\n" +
            "\n" +
            "for commitHash in commitsList:\n" +
            "    countMod(commitHash)\n" +
            "\n" +
            "calculaPercentualePresenza()\n" +
            "\n" +
            "if classe in blobList:\n" +
            "    print('true,'+ str(dizionario[classe]))\n" +
            "else:\n" +
            "    print('false,' + str(0))\n" +
            "\n" +
            "\n";
}
