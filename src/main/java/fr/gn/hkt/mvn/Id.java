package fr.gn.hkt.mvn;

import org.derive4j.hkt.__;

@FunctionalInterface
public interface Id<A> extends __<Id.µ, A> {
    A is();

    static <A> Id<A> id(A a) { return () -> a; }

    static <A> Id<A> coerce(__<µ, A> ida) { return (Id<A>) ida; }
    final class µ {}
}
