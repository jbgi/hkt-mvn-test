package fr.gn.hkt.mvn;


import fr.gn.hkt.mvn.FunctorTC.Functor;
import fr.gn.hkt.mvn.FunctorTC.Monad;
import org.derive4j.hkt.*;

@Hkt(generatedIn = "Funs")
public final class fun {
    private fun() {}

    @FunctionalInterface @Hkt(generatedIn = "F_")
    public interface F<A, B> extends __2<F.µ, A, B> {
        B f(A a);

        default F0<B> lazy(A a) { return () -> f(a); }

        default <C> F<C, B> o(F<C, A> caf) { return c -> f(caf.f(c)); }

        default <C> F<A, C> andThen(F<B, C> bcf) { return bcf.o(this); }

        default <C> F<A, C> bind(F<B, F<A, C>> f) { return a -> f.f(f(a)).f(a); }

        default <C> F<A, C> apply(F<A, F<B, C>> f) { return a -> f.f(a).f(f(a)); }

        static <A, B> F<A, B> cst(B b) { return __ -> b; }

        static <A, B> F<A, F0<B>> lazy(F<A, B> f) { return f::lazy; }

        @SuppressWarnings("unchecked")
        static <R> Instances<R> instances() { return (Instances<R>) Instances.self; }

        abstract class Instances<R> implements Monad<__<F.µ, R>> {
            private Instances() {}
            private static final Instances<?> self = new Instances<Object>() {};
            @Override
            public <A, B> F<R, B> fmap(__<__<F.µ, R>, A> fa, F<A, B> f) {
                return f.o(F.coerce(fa));
            }
            @Override
            public <A> F<R, A> pure(F0<A> a) { return __ -> a.f(); }
            @Override
            public <A, B> F<R, B> ap(__<__<F.µ, R>, A> fa, __<__<F.µ, R>, F<A, B>> hf) {
                return F.coerce(fa).apply(r -> F.coerce(hf).f(r));
            }
            @Override
            public <A, B> F<R, B> bind(__<__<F.µ, R>, A> fa, F<A, __<__<F.µ, R>, B>> f) {
                return F.coerce(fa).bind(a -> F.coerce(f.f(a)));
            }
        }

        static <A, B> F<A, B> vary(F<? super A, ? extends B> f) { return f::f; }

        static <A, B> F<A, B> coerce(__<__<F.µ, A>, B> fab) { return (F<A, B>) fab; }
        final class µ {}
    }

    @FunctionalInterface
    public interface F0<A> extends __<F0.µ, A> {
        A f();

        default <B> F0<B> map(F<A, B> f) { return () -> f.f(f()); }

        default <B> F0<B> bind(F<A, F0<B>> f) { return () -> f.f(f()).f(); }

        default <B> F0<B> apply(F0<F<A, B>> f0) { return f0.bind(this::map); }

        static Instances instances() { return Instances.self; }

        enum  Instances implements Monad<F0.µ> { self;
            @Override
            public <A, B> F0<B> fmap(__<F0.µ, A> fa, F<A, B> f) {
                return coerce(fa).map(f);
            }
            @Override
            public <A> F0<A> pure(F0<A> a) { return a; }
            @Override
            public <A, B> F0<B> bind(__<F0.µ, A> fa, F<A, __<F0.µ, B>> f) {
                return coerce(fa).bind(a -> coerce(f.f(a)));
            }
        }

        static <A> F0<A> coerce(__<F0.µ, A> f0) { return (F0<A>) f0; }
        final class µ {}
    }

    @FunctionalInterface
    public interface F2<A, B, C> extends F<A, F<B, C>> {
        C f(A a, B b);

        @Override
        default F<B, C> f(A a) {  return b -> f(a, b); }
    }

    @FunctionalInterface
    public interface F3<A, B, C, D> extends F2<A, B, F<C, D>> {
        D f(A a, B b, C c);

        @Override
        default F<C, D> f(A a, B b) { return c -> f(a, b, c); }
    }

    @FunctionalInterface
    public interface F4<A, B, C, D, E> extends F3<A, B, C, F<D, E>> {
        E f(A a, B b, C c, D d);

        @Override
        default F<D, E> f(A a, B b, C c) { return d -> f(a, b, c, d); }
    }

    public static final class P2<A, B> implements  __2<P2.µ, A, B> {
        private final A _1;
        private final B _2;

        private P2(A _1, B _2) { this._1 = _1; this._2 = _2; }

        public static <A, B> P2<A, B> p(A a, B b) { return new P2<>(a, b); }

        public A _1() { return _1; }

        public B _2() { return _2; }

        public <R> R match(F2<A, B, R> f) { return f.f(_1, _2); }

        public static final class Instances<X> implements Functor<__<P2.µ, X>> {
            @Override
            public <A, B> P2<X, B> fmap(__<__<P2.µ, X>, A> fa, F<A, B> f) {
                final P2<X, A> pair = coerce(fa);
                return p(pair._1, f.f(pair._2));
            }
        }

        public static <A, B> P2<A, B> coerce(__<__<P2.µ, A>, B> pair) { return (P2<A, B>) pair; }
        public static <A, B> P2<A, B> coerce(__2<P2.µ, A, B> pair) { return (P2<A, B>) pair; }
        public static final class µ {}
    }

    public static final class P3<A, B, C> implements __3<P3.µ, A, B, C> {
        private final A _1;
        private final B _2;
        private final C _3;

        private P3(A _1, B _2, C _3) { this._1 = _1; this._2 = _2; this._3 = _3; }

        public static <A, B, C> P3<A, B, C> p(A a, B b, C c) { return new P3<>(a, b, c); }

        public A _1() { return _1; }

        public B _2() { return _2; }

        public C _3() { return _3; }

        public static final class µ { private µ(){} }
    }

    public static final class P4<A, B, C, D> implements __4<P4.µ, A, B, C, D> {
        private final A _1;
        private final B _2;
        private final C _3;
        private final D _4;

        private P4(A _1, B _2, C _3, D _4) { this._1 = _1; this._2 = _2; this._3 = _3; this._4 = _4; }

        public static <A, B, C, D> P4<A, B, C, D> p(A a, B b, C c, D d) { return new P4<>(a, b, c, d); }

        public A _1() { return _1; }

        public B _2() { return _2; }

        public C _3() { return _3; }

        public D _4() { return _4; }

        public enum µ {}
    }
}
