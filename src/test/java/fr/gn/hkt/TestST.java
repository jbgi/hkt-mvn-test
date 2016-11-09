package fr.gn.hkt;

import fr.gn.hkt.mvn.effect.ST;
import fr.gn.hkt.mvn.effect.ST.STRef;
import fr.gn.hkt.mvn.fun.P3;
import fr.gn.hkt.mvn.syntax.Do;

public final class TestST {
    private TestST() {}

    static <S> ST<S, STRef<S, Integer>> zero() { return ST.newVar(0); }

    static <S> ST<S, Integer> addOneAndRead() { return  Do.$(ST.instances(), ST::coerce
        , TestST::<S>zero
        , r -> r.mod(__ -> __ + 1)
        , __ -> __._2().read()
        , P3::_3);
    }

    static <S> ST<S, Integer> addTwoAndRead() { return  Do.$(ST.instances(), ST::coerce
        , TestST::<S>zero
        , r -> r.write(() -> 2)
        , __ -> __._2().read()
        , P3::_3);
    }

    public static void main(String[] args) {
        System.out.println(ST.<Integer>runST(TestST::addOneAndRead));
        System.out.println(ST.<Integer>runST(TestST::addTwoAndRead));
    }


}
