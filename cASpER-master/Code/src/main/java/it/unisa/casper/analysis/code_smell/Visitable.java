package it.unisa.casper.analysis.code_smell;

public interface Visitable<T> {

    public abstract boolean accept(Visitor visitor,T bean);
}
