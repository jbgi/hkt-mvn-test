package fr.gn.hkt.mvn.syntax;

import fr.gn.hkt.mvn.Coerce;
import fr.gn.hkt.mvn.FunctorTC.Apply;
import fr.gn.hkt.mvn.fun.F2;
import fr.gn.hkt.mvn.fun.F3;
import fr.gn.hkt.mvn.fun.F4;
import org.derive4j.hkt.__;

public final class ApDo {
    private ApDo() {}

    public static <A, B, C, m, MC> MC $(Apply<m> A, Coerce<m, C, MC> Co,
                                        __<m, A> ma,
                                        __<m, B> mb,
                                        F2<A, B, C> f) {
        return Co.coerce(A.ap(mb, A.fmap(ma, f)));
    }

    public static <A, B, C, D, m, MD> MD $(Apply<m> A, Coerce<m, D, MD> Co,
                                           __<m, A> ma,
                                           __<m, B> mb,
                                           __<m, C> mc,
                                           F3<A, B, C, D> f) {
        return Co.coerce(A.ap(mc, A.ap(mb, A.fmap(ma, f))));
    }

    public static <A, B, C, D, E, m, ME> ME $(Apply<m> A, Coerce<m, E, ME> Co,
                                              __<m, A> ma,
                                              __<m, B> mb,
                                              __<m, C> mc,
                                              __<m, D> md,
                                              F4<A, B, C, D, E> f) {
        return Co.coerce(A.ap(md, A.ap(mc, A.ap(mb, A.fmap(ma, f)))));
    }
}
