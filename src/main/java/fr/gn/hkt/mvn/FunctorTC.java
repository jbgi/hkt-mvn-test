package fr.gn.hkt.mvn;

import fr.gn.hkt.mvn.fun.F;
import fr.gn.hkt.mvn.fun.F0;
import org.derive4j.hkt.__;

public final class FunctorTC {
    private FunctorTC() {}

    public interface Functor<f> {
        <A, B> __<f, B> fmap(__<f, A> fa, F<A, B> f);
    }

    public interface Apply<f> extends Functor<f> {
        <A, B> __<f, B> ap(__<f, A> fa, __<f, F<A, B>> hf);
    }

    public interface Applicative<f> extends Apply<f> {
        <A> __<f, A> pure(F0<A> a);
    }

    public interface Bind<f> extends Apply<f> {
        <A, B> __<f, B> bind(__<f, A> fa, F<A, __<f, B>> f);
    }

    public interface Monad<f> extends Applicative<f>, Bind<f> {
        @Override
        default <A, B> __<f, B> ap(__<f, A> fa, __<f, F<A, B>> hf) {
            return bind(hf, f -> fmap(fa, f));
        }
        @Override
        default <A, B> __<f, B> fmap(__<f, A> fa, F<A, B> f) {
            return bind(fa, f.andThen(b -> pure(() -> b)));
        }

        default <A> __<f, A> join(__<f, __<f, A>> ffa) { return bind(ffa, __ -> __); }
    }

    public interface Traversable<t> extends Functor<t> {
        <A, B, TB extends __<t, B>, f>
        __<f, TB> traverse(Coerce<t, B, TB> N,
                           Applicative<f> A, F<A, __<f, B>> f, __<t, A> ta);

        default <A, TA extends __<t, A>, m>
        __<m, TA> sequence(Coerce<t, A, TA> N,
                           Applicative<m> A, __<t, ? extends __<m, A>> tma) {
            return traverse(N, A, __ -> __, tma);
        }
    }

}
