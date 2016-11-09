package fr.gn.hkt.mvn;

import fj.Bottom;
import fj.Show;
import fr.gn.hkt.mvn.FunctorTC.Monad;
import fr.gn.hkt.mvn.fun.F;
import fr.gn.hkt.mvn.fun.F0;
import org.derive4j.Data;
import org.derive4j.Flavour;
import org.derive4j.hkt.__;
import org.derive4j.hkt.__2;

import java.util.Iterator;

import static fr.gn.hkt.mvn.Lists.*;

public final class data {
    private data() {}

    @Data(flavour = Flavour.FJ)
    public static abstract class Maybe<A> implements __<Maybe.µ, A> {
        Maybe() {}
        interface Cases<R, T> {
            R Just(T t);
            R Nothing();
        }
        public abstract <R> R match(Cases<R, A> cases);

        public <B> Maybe<B> bind(F<A, Maybe<B>> f) {
            return Maybes.<A>cases()
                .Just(f::f)
                .Nothing(Maybes::Nothing)
                .f(this);
        }

        public boolean isJust() {
            return Maybes.<A>cases()
                .Just(__ -> true)
                .otherwise(false)
                .f(this);
        }

        public boolean isNothing() { return !isJust(); }

        public A just() { return Maybes.getT(this).some(); }

        public static Instance instances() { return Instance.self; }

        public static final class Instance implements Monad<Maybe.µ> {
            private Instance() {}
            private static final Instance self = new Instance();
            @Override
            public <A> Maybe<A> pure(F0<A> a) { return Maybes.Just(a.f()); }
            @Override
            public <A, B> Maybe<B> bind(__<Maybe.µ, A> fa, F<A, __<µ, B>> f) {
                return coerce(fa).bind(a -> coerce(f.f(a)));
            }
        }

        public static final <A> Maybe<A> coerce(__<Maybe.µ, A> ma) {
            return (Maybe<A>) ma;
        }
        public static final class µ {}
    }

    @Data(flavour = Flavour.FJ)
    public static abstract class Either<A, B> implements __2<Either.µ, A, B> {
        Either() {}
        interface Cases<R, A, B> {
            R Right(B right);
            R Left(A left);
        }
        public abstract <R> R match(Cases<R, A, B> cases);

        public <C> Either<A, C> map(F<B, C> f) {
            return fold(Eithers::Left, f.andThen(Eithers::Right));
        }

        public <C> Either<A, C> bind(F<B, Either<A, C>> f) {
            return fold(Eithers::Left, f);
        }

        public <C> C fold(F<A, C> a, F<B, C> b) {
            return Eithers.<A, B>cases()
                .Right(b::f)
                .Left(a::f)
                .f(this);
        }

        public final boolean isRight() {
            return Eithers.<A, B>cases()
                .Right(__ -> true)
                .otherwise(() -> false)
                .f(this);
        }

        public final boolean isLeft() { return !isRight(); }

        public final A left() {
            return Eithers.<A, B>cases()
                .Left(__ -> __)
                .otherwise(Bottom.error_("Calling left on a Either.Right"))
                .f(this);
        }

        public final B right() {
            return Eithers.<A, B>cases()
                .Right(__ -> __)
                .otherwise(Bottom.error_("Calling right on a Either.Left"))
                .f(this);
        }

        public static final class Instances<R> implements Monad<__<Either.µ, R>> {
            private static final Instances<?> self = new Instances<>();
            @Override
            public <A> Either<R, A> pure(F0<A> a) { return Eithers.Right(a.f()); }
            @Override
            public <A, B> Either<R, B> bind(__<__<Either.µ, R>, A> fa, F<A, __<__<Either.µ, R>, B>> f) {
                return coerce(fa).bind(a -> coerce(f.f(a)));
            }
        }

        public static <A, B> Either<A, B> coerce(__<__<Either.µ, A>, B> ei) { return (Either<A, B>) ei; }
        public static final class µ {}
    }

    @Data(flavour = Flavour.FJ)
    public static abstract class List<A> implements __<List.µ, A>, Iterable<A> {
        List() {}
        interface Cases<R, T> {
            R Cons(T head, List<T> tail);
            R Nil();
        }
        public abstract <R> R match(Cases<R, A> cases);

//        public final <B> List<B> map(F<A, B> f) {
//            return bind(a -> Cons(f.f(a), Nil()));
//        }

        public <B> B foldRight(F<A, F<F0<B>, B>> f, B b) {
            return Lists.<A>cases()
                .Cons((x, xs) -> f.f(x).f(() -> xs.foldRight(f, b)))
                .Nil(() -> b)
                .f(this);
        }

        public static List<Integer> range(int start, int end) {
            return Cons(start, start == end ? Nil() : lazy(() -> range(start + 1, end)));
        }

        public static <R> Show<List<R>> show(Show<R> show) {
            return Show.showS(Lists.<R>cases()
                .Cons((r, rs) -> show.showS(r) + "," + show(show).showS(rs))
                .Nil(() -> "Nil"));
        }

        @Override
        public Iterator<A> iterator() {
            return new Iterator<A>() {
                private List<A> self = List.this;
                public boolean hasNext() {
                    return Lists.<A>cases()
                        .Cons((x, xs) -> true)
                        .Nil(() -> false)
                        .f(self);
                }
                public A next() {
                    return Lists.<A>cases()
                        .Cons((x, xs) -> {
                            self = xs;
                            return x;
                        })
                        .Nil(Bottom.<A>error_("No more elements")::_1)
                        .f(self);
                }
            };
        }

        public static <A> List<A> coerce(__<List.µ, A> as) { return (List<A>) as; }
        public static final class µ {}
    }

}
