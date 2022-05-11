package it.unisa.casper.refactor.strategy;


import it.unisa.casper.refactor.exceptions.*;

public class RefactoringManager {
    private RefactoringStrategy refactoringStrategy;

    public RefactoringManager(RefactoringStrategy Rstrategy) {

        this.refactoringStrategy = Rstrategy;
    }

    public void executeRefactor() throws RefactorException /*PromiscuousPackageException, BlobException, FeatureEnvyException, MisplacedClassException*/ {
        refactoringStrategy.doRefactor();
    }

}
