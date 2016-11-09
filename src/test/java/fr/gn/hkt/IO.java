package fr.gn.hkt;

import fj.F;
import fr.gn.hkt.mvn.FunctorTC;
import fr.gn.hkt.mvn.fun;
import org.derive4j.hkt.__;

@FunctionalInterface
interface IO<A> extends __<IO.µ, A> {
    A run() throws Throwable;

    default <B> IO<B> map(F<A, B> f) {
        return () -> f.f(run());
    }

    default <B> IO<B> bind(F<A, IO<B>> f) {
        return () -> f.f(run()).run();
    }

    static Instances instances() { return Instances.self; }

    enum Instances implements FunctorTC.Monad<µ> { self;
        @Override
        public <A> IO<A> pure(fun.F0<A> a) { return a::f; }
        @Override
        public <A, B> IO<B> bind(__<µ, A> fa, fun.F<A, __<µ, B>> f) {
            return coerce(fa).bind(a -> coerce(f.f(a)));
        }
    }

    static <A> IO<A> coerce(__<µ, A> io) { return (IO<A>) io; }
    final class µ { private µ() {} }
}
