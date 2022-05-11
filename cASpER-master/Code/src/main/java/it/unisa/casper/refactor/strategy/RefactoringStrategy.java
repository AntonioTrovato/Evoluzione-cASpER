package it.unisa.casper.refactor.strategy;

import it.unisa.casper.refactor.exceptions.*;

public interface RefactoringStrategy {
    void doRefactor() throws RefactorException /*FeatureEnvyException, MisplacedClassException, PromiscuousPackageException, BlobException*/;
}
