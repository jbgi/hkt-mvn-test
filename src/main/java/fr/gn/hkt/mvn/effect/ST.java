package fr.gn.hkt.mvn.effect;

import fr.gn.hkt.mvn.Coerce;
import fr.gn.hkt.mvn.Forall;
import fr.gn.hkt.mvn.FunctorTC.Monad;
import fr.gn.hkt.mvn.Id;
import fr.gn.hkt.mvn.NF;
import fr.gn.hkt.mvn.fun.F;
import fr.gn.hkt.mvn.fun.F0;
import fr.gn.hkt.mvn.fun.P2;
import org.derive4j.hkt.__;
import org.derive4j.hkt.__2;

import static fr.gn.hkt.mvn.effect.World.realWord;
import static fr.gn.hkt.mvn.fun.P2.p;

public abstract class ST<S, A> implements __2<ST.µ, S, A> {
    private ST() {}

    public static final class STRef<S, A> implements __2<STRef.µ, S, A> {
        private A value;
        private STRef(A value) { this.value = value; }

        public ST<S, A> read() { return returnST(() -> value); }

        public ST<S, STRef<S, A>> mod(F<A, A> f) { return st(s -> {
            value = f.f(value);
            return p(s, this);
        });}

        public ST<S, STRef<S, A>> write(F0<A> a) { return st(s -> {
            value = a.f();
            return p(s, this);
        });}

        public static <S> NF<Id.µ, __<STRef.µ, S>> stRef() { return STRef::intStRef; }
        private static <S, A> STRef<S, A> intStRef(__<Id.µ, A> fa) { return new STRef<>(Id.coerce(fa).is()); }

        public static <S, A> STRef<S, A> coerce(__<__<STRef.µ, S>, A> str) { return (STRef<S, A>) str; }
        public static final class µ { private µ(){} }
    }


    abstract P2<World<S>, A> f(World<S> s);

    public <B> ST<S, B> bind(F<A, ST<S, B>> g) {
        return st(s -> f(s).match((ns, a) -> g.f(a).f(ns)));
    }

    public <B> ST<S, B> map(F<A, B> g) {
        return st(s -> f(s).match((ns, a) -> p(ns, g.f(a))));
    }

    public static <S, A> ST<S, A> st(F<World<S>, P2<World<S>, A>> f) { return new ST<S, A>() {
        P2<World<S>, A> f(World<S> s) { return f.f(s); }
    };}

    public static <S, A> ST<S, A> returnST(F0<A> a) { return st(s -> p(s, a.f())); }

    public static <A> A runST(Forall.R<ST.µ, A> f) {
        return coerce(f.<World.Real>f()).f(realWord)._2();
    }

    public static <S, A> ST<S, STRef<S, A>> newVar(A a) {
        return returnST(() -> new STRef<>(a));
        //return ST.coerce(ST.<S>newVarIn().f(STRef::coerce, Id.id(a)));
    }

    public static <S> NF.InR<Id.µ, __<ST.µ, S>, __<STRef.µ, S>> newVarIn() {
        return new NF.InR<Id.µ, __<ST.µ, S>, __<STRef.µ, S>>() {
            public <A, STR extends __<__<STRef.µ, S>, A>> ST<S, STR> f(Coerce<__<STRef.µ, S>, A, STR> C, __<Id.µ, A> fa) {
                return returnST(() -> C.coerce(STRef.<S>stRef().f(fa)));
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <S> Instances<S> instances() { return (Instances<S>) Instances.self; }

    public static final class Instances<S> implements Monad<__<ST.µ, S>> {
        private static final Instances<?> self = new Instances<>();
        @Override
        public <A> ST<S, A> pure(F0<A> a) { return returnST(a); }
        @Override
        public <A, B> ST<S, B> bind(__<__<ST.µ, S>, A> fa, F<A, __<__<ST.µ, S>, B>> f) {
            return coerce(fa).bind(a -> coerce(f.f(a)));
        }
    }

    public static <S, A> ST<S, A> coerce(__<__<ST.µ, S>, A> st) { return (ST<S, A>) st; }
    public final static class µ { private µ(){} }
}
