package fr.gn.hkt.mvn.syntax;

import fr.gn.hkt.mvn.Coerce;
import fr.gn.hkt.mvn.FunctorTC.Monad;
import fr.gn.hkt.mvn.fun.*;
import org.derive4j.hkt.__;

import static fr.gn.hkt.mvn.fun.P2.p;

public final class Do {
    private Do() {}

    // ############### F<A, B>

    public static <A, B, m, MB> MB $(Monad<m> M, Coerce<m, B, MB> N,
                                     F0<__<m, A>> ma,
                                     F<A, B> f) {
        return N.coerce(M.fmap(ma.f(), f));
    }

    // ############### F2<A, B, C>

    public static <A, B, C, m, MC> MC $(Monad<m> M, Coerce<m, C, MC> N,
                                        F0<__<m, A>> ma,
                                        F<A, __<m, B>> fmb,
                                        F2<A, B, C> f) {
        return N.coerce(M.bind(ma.f(), a -> M.fmap(fmb.f(a), b -> f.f(a, b))));
    }

    public static <A, B, C, m, MC> MC $(Monad<m> M, Coerce<m, C, MC> N,
                                        F0<__<m, A>> ma,
                                        F<A, __<m, B>> fmb,
                                        F<P2<A, B>, C> f) {
        return $(M, N, ma, fmb, (a, b) -> f.f(p(a, b)));
    }

    // ############### F3<A, B, C, D>

    public static <A, B, C, D, m, MD> MD $(Monad<m> M, Coerce<m, D, MD> N,
                                           F0<__<m, A>> ma,
                                           F<A, __<m, B>> fmb,
                                           F2<A, B, __<m, C>> fmc,
                                           F3<A, B, C, D> f) {
        return N.coerce(M.bind(ma.f(), a -> M.bind(fmb.f(a), b -> M.fmap(fmc.f(a, b), c -> f.f(a, b, c)))));
    }

    public static <A, B, C, D, m, MD> MD $(Monad<m> M, Coerce<m, D, MD> N,
                                           F0<__<m, A>> ma,
                                           F<A, __<m, B>> fmb,
                                           F<P2<A, B>, __<m, C>> fmc,
                                           F3<A, B, C, D> f3) {
        return $(M, N, ma, fmb, (a, b) -> fmc.f(p(a, b)), f3);
    }

    public static <A, B, C, D, m, MD> MD $(Monad<m> M, Coerce<m, D, MD> N,
                                           F0<__<m, A>> ma,
                                           F<A, __<m, B>> fmb,
                                           F2<A, B, __<m, C>> fmc,
                                           F<P3<A, B, C>, D> f) {
        return $(M, N, ma, fmb, fmc, (a, b, c) -> f.f(P3.p(a, b, c)));
    }

    public static <A, B, C, D, m, MD> MD $(Monad<m> M, Coerce<m, D, MD> N,
                                           F0<__<m, A>> ma,
                                           F<A, __<m, B>> fmb,
                                           F<P2<A, B>, __<m, C>> fmc,
                                           F<P3<A, B, C>, D> f) {
        return $(M, N, ma, fmb, fmc, (a, b, c) -> f.f(P3.p(a, b, c)));
    }

    // ############### F4<A, B, C, D, E>

    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F2<A, B, __<m, C>> fmc,
                                              F3<A, B, C, __<m, D>> fmd,
                                              F4<A, B, C, D, E> f) {
        return N.coerce(M.bind(ma.f(), a -> M.bind(fmb.f(a), b -> M.bind(fmc.f(a, b), c ->
            M.fmap(fmd.f(a, b, c), d -> f.f(a, b, c, d))))));
    }

    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F<P2<A, B>, __<m, C>> fmc,
                                              F3<A, B, C, __<m, D>> fmd,
                                              F4<A, B, C, D, E> f) {
        return $(M, N, ma, fmb, (a, b) -> fmc.f(p(a, b)), fmd, f);
    }

    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F2<A, B, __<m, C>> fmc,
                                              F<P3<A, B, C>, __<m, D>> fmd,
                                              F4<A, B, C, D, E> f) {
        return $(M, N, ma, fmb, fmc, (a, b, c) -> fmd.f(P3.p(a, b, c)), f);
    }


    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F2<A, B, __<m, C>> fmc,
                                              F3<A, B, C, __<m, D>> fmd,
                                              F<P4<A, B, C, D>, E> f) {
        return $(M, N, ma, fmb, fmc, fmd, (a, b, c, d) -> f.f(P4.p(a, b, c, d)));
    }

    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F<P2<A, B>, __<m, C>> fmc,
                                              F<P3<A, B, C>, __<m, D>> fmd,
                                              F4<A, B, C, D, E> f) {
        return $(M, N, ma, fmb, (a, b) -> fmc.f(p(a, b)), fmd, f);
    }

    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F<P2<A, B>, __<m, C>> fmc,
                                              F3<A, B, C, __<m, D>> fmd,
                                              F<P4<A, B, C, D>, E> f) {
        return $(M, N, ma, fmb, (a, b) -> fmc.f(p(a, b)), fmd, f);
    }

    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F2<A, B, __<m, C>> fmc,
                                              F<P3<A, B, C>, __<m, D>> fmd,
                                              F<P4<A, B, C, D>, E> f) {
        return $(M, N, ma, fmb, fmc, fmd, (a, b, c, d) -> f.f(P4.p(a, b, c, d)));
    }

    public static <A, B, C, D, E, m, ME> ME $(Monad<m> M, Coerce<m, E, ME> N,
                                              F0<__<m, A>> ma,
                                              F<A, __<m, B>> fmb,
                                              F<P2<A, B>, __<m, C>> fmc,
                                              F<P3<A, B, C>, __<m, D>> fmd,
                                              F<P4<A, B, C, D>, E> f) {
        return $(M, N, ma, fmb, fmc, fmd, (a, b, c, d) -> f.f(P4.p(a, b, c, d)));
    }
}
