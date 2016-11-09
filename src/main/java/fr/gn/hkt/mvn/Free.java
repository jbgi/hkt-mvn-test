package fr.gn.hkt.mvn;

import fj.Unit;
import fr.gn.hkt.mvn.FunctorTC.Applicative;
import fr.gn.hkt.mvn.FunctorTC.Functor;
import fr.gn.hkt.mvn.FunctorTC.Monad;
import fr.gn.hkt.mvn.data.Either;
import fr.gn.hkt.mvn.fun.F;
import fr.gn.hkt.mvn.fun.F0;
import org.derive4j.Data;
import org.derive4j.Flavour;
import org.derive4j.hkt.Hkt;
import org.derive4j.hkt.__;
import org.derive4j.hkt.__2;

import static fr.gn.hkt.mvn.Eithers.Left;
import static fr.gn.hkt.mvn.Eithers.Right;
import static fr.gn.hkt.mvn.Frees.*;

@Data(flavour = Flavour.FJ) @Hkt(generatedIn = "Free_", delegateTo = {})
public abstract class Free<f, A> implements __2<Free.µ, f, A> {
    Free() {}
    interface Cases<f, A, R> {
        R Return(A a);
        R Suspend(__<f, A> fa);
        R Gosub(Sub<f, A, ?> sub);
    }
    abstract <R> R match(Cases<f, A, R> cases);

    static final class Sub<f, B, C> {
        final Free<f, C> a;
        final F<C, Free<f, B>> f;

        Sub(Free<f, C> a, F<C, Free<f, B>> f) {
            this.a = a;
            this.f = f;
        }
    }

    public <B> Free<f, B> map(F<A, B> f) { return bind(f.andThen(Frees::Return)); }

    public <B> Free<f, B> bind(F<A, Free<f, B>> f) { return Gosub(new Sub<>(this, f)); }

    public <B> B fold(Functor<f> F, F<A, B> r, F<__<f, Free<f, A>>, B> s) { return resume(F).fold(s, r); }

    public Either<__<f, Free<f, A>>, A> resume(Functor<f> F) {
        Free<f, A> self = this;

        while (true) {
            final Either<Free<f, A>, Either<__<f, Free<f, A>>, A>> either =
                self.match(Frees.
                    cases(a -> Right(Right(a))
                        , fa -> Right(Left(F.fmap(fa, Frees::Return)))
                        , sub -> resumeGosub(F, sub)));

            if (either.isLeft()) self = either.left();
            else return either.right();
        }
    }
    private static <f, A, C> Either<Free<f, A>, Either<__<f, Free<f, A>>, A>> resumeGosub
        (Functor<f> F, Sub<f, A, C> b) {
        return b.a.match(Frees.
            cases(a -> Left(b.f.f(a))
                , fa -> Right(Left(F.fmap(fa, b.f)))
                , c -> resumeSubGosub(b, c)));
    }
    private static <f, A, C, D> Either<Free<f, A>, Either<__<f, Free<f, A>>, A>> resumeSubGosub
        (Sub<f, A, C> b, Sub<f, C, D> c) {
        return Left(c.a.bind(z -> c.f.f(z).bind(b.f)));
    }

    public Free<f, A> step() {
        Free<f, A> self = this;

        while (true) {
            final Free<f, A> fSelf = self;
            final Either<Free<f, A>, Free<f, A>> either = Frees.<f, A>cases()
                .Gosub(x -> stepGosub(fSelf, x))
                .otherwise(() -> Right(fSelf))
                .f(self);

            if (either.isLeft()) self = either.left();
            else return either.right();
        }
    }
    private static <f, A, C> Either<Free<f, A>, Free<f, A>> stepGosub(Free<f, A> self, Sub<f, A, C> x) {
        return Frees.<f, C>cases().<Either<Free<f, A>, Free<f, A>>>
            Return(b -> Left(x.f.f(b)))
            .Gosub(sub -> stepSubGosub(x, sub))
            .otherwise(() -> Right(self))
            .f(x.a);
    }
    private static <f, A, C, D> Either<Free<f, A>, Free<f, A>> stepSubGosub(Sub<f, A, C> x, Sub<f, C, D> b) {
        return Left(b.a.bind(a -> b.f.f(a).bind(x.f)));
    }

    public A go(Functor<f> F, F<__<f, Free<f, A>>, Free<f, A>> f) {
        Free<f, A> self = this;

        while (true) {
            final Either<__<f, Free<f, A>>, A> res = self.resume(F);

            if (res.isLeft()) self = f.f(res.left());
            else return res.right();
        }
    }

    public <m> __<m, A> foldMap(Monad<m> M, NF<f, m> f) {
        return Frees.<f, A>cases()
            .Return(a -> M.pure(() -> a))
            .Suspend(f::f)
            .Gosub(a -> foldMapGosub(M, f, a))
            .f(step());
    }
    private static <m, f, A, C> __<m, A> foldMapGosub(Monad<m> M, NF<f, m> f, Sub<f, A, C> a) {
        return M.bind(a.a.foldMap(M, f), c -> a.f.f(c).foldMap(M, f));
    }

    public static <f, A> Free<f, A> point(A a) { return Return(a); }

    public static <f, A> Free<f, A> liftF(__<f, A> fa) { return Suspend(fa); }

    public static <f, A> Free<f, A> suspend(Applicative<f> A, F0<Free<f, A>> f0) {
        return liftF(A.pure(Unit::unit)).bind(__ -> f0.f());
    }

    public static <f, A> Free<f, A> roll(__<f, Free<f, A>> v) { return liftF(v).bind(__ -> __); }

    @SuppressWarnings("unchecked")
    public static <f> Instances<f> instances() { return (Instances<f>) Instances.self; }

    public static class Instances<f> implements Monad<__<Free.µ, f>> {
        private static final Instances<?> self = new Instances<>();
        private Instances() {}
        @Override
        public <A> Free<f, A> pure(F0<A> a) { return Free.point(a.f()); }
        @Override
        public <A, B> Free<f, B> bind(__<__<Free.µ, f>, A> fa, F<A, __<__<Free.µ, f>, B>> f) {
            return coerce(fa).bind(a -> coerce(f.f(a)));
        }
    }

    @FunctionalInterface
    public interface Of<s, A> extends __2<Free.Of.µ, s, A> {
        Free<s, A> free();

        default <B> Free.Of<s, B> map(F<A, B> f) { return () -> free().map(f); }

        default <B> Free.Of<s, B> bind(F<A, Free.Of<s, B>> f) {
            return () -> free().bind(f.andThen(Free.Of::free));
        }

        default <B> Free.Of<s, B> apply(Free.Of<s, F<A, B>> f) {
            return f.bind(this::map);
        }

        static <s, A> Free.Of<s, A> pure(A a) { return () -> Free.point(a); }

        final class Instances<s> implements Monad<__<Free.Of.µ, s>> {
            @Override
            public <A> Free.Of<s, A> pure(F0<A> a) { return () -> Free.point(a.f()); }
            @Override
            public <A, B> Free.Of<s, B> bind(__<__<Free.Of.µ, s>, A> fa, F<A, __<__<Free.Of.µ, s>, B>> f) {
                return Free.Of.coerce(fa).bind(a -> Free.Of.coerce(f.f(a)));
            }
        }

        static <s, A> Free.Of<s, A> coerce(__<__<Free.Of.µ, s>, A> fof) { return (Free.Of<s, A>) fof; }
        final class µ { private µ(){} }
    }

    public static final class Trampoline<A> implements __<Trampoline.µ, A> {
        private final Free<F0.µ, A> free;
        private Trampoline(Free<F0.µ, A> free) {this.free = free;}

        public Free<F0.µ, A> free() { return free; }

        public <B> Trampoline<B> map(F<A, B> f) {
            return new Trampoline<>(free.map(f));
        }

        public <B> Trampoline<B> bind(F<A, Trampoline<B>> f) {
            return new Trampoline<>(free.bind(f.andThen(Trampoline::free)));
        }

        public A run() { return free.go(F0.instances(), f0 -> F0.coerce(f0).f()); }

        public static <A> Trampoline<A> done(A a) {
            return new Trampoline<>(Free.point(a));
        }

        public static <A> Trampoline<A> delay(F0<A> a) { return suspend(() -> done(a.f())); }

        public static <A> Trampoline<A> suspend(F0<Trampoline<A>> a) {
            return new Trampoline<>(Free.suspend(F0.instances(), () -> a.f().free));
        }

        public static Instances instances() { return Instances.self; }

        public enum  Instances implements Monad<Trampoline.µ> { self;
            @Override
            public <A> Trampoline<A> pure(F0<A> a) { return Trampoline.done(a.f()); }
            @Override
            public <A, B> Trampoline<B> bind(__<Trampoline.µ, A> fa, F<A, __<Trampoline.µ, B>> f) {
                return coerce(fa).bind(a -> coerce(f.f(a)));
            }
        }

        public static <A> Trampoline<A> coerce(__<Trampoline.µ, A> t) { return (Trampoline<A>) t; }
        public static final class µ { private µ() {} }
    }

    static class TestTrampo<A> extends Free<F0.µ, A> {
        private TestTrampo() {}

        @Override
        <R> R match(Cases<F0.µ, A, R> cases) {
            return Frees.lazy(() -> this).match(cases);
        }

        @Override
        public <B> TestTrampo<B> map(F<A, B> f) {
            return (TestTrampo<B>) super.map(f);
        }

        public static <A> TestTrampo<A> done(A a) {
            return (TestTrampo<A>) Free.<F0.µ, A>point(a);
        }
    }


    public static <f, R> Free<f, R> coerce(__2<Free.µ, f, R> free) { return (Free<f, R>) free; }
    public static <f, R> Free<f, R> coerce(__<__<Free.µ, f>, R> free) { return (Free<f, R>) free; }
    public static final class µ {}
}
