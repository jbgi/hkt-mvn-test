package fr.gn.hkt.mvn;

import org.derive4j.hkt.__;

/** A universally quantified value */
public interface Forall<p> {
    <A> __<p, A> f();

    interface R<p, B> {
        <A> __<__<p, A>, B> f();
    }
}
