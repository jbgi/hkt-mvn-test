package fr.gn.hkt.mvn;

import org.derive4j.hkt.__;
import org.derive4j.hkt.__2;

abstract class Leibniz<A, B> implements __2<Leibniz.µ, A, B> {
    abstract <f> __<f, B> subst(__<f, A> fa);
    abstract <f, g> __<f, __<g, B>> subst2(__<f, __<g, A>> fga);
//    {
//        return this.subst(fga);
//    }

    final B apply(A a) { return Id.coerce(this.subst(Id.id(a))).is(); }

    static <A> Leibniz<A, A> refl() {
        return new Leibniz<A, A>() {
            <f> __<f, A> subst(__<f, A> fa) { return fa; }
            <f, g> __<f, __<g, A>> subst2(__<f, __<g, A>> fga) { return fga; }
        };
    }

    static <f, A, B> Leibniz<__<f, A>, __<f, B>> lift(Leibniz<A, B> a) {
        return coerce(a.<__<Leibniz.µ, __<f, A>>, f>subst2(Leibniz.<__<f, A>>refl()));

        //return coerce(a.<__<Leibniz.µ, __<f, A>>>subst(Leibniz.refl()));
    }

//    static abstract class Leib2<g, A, B> extends Leibniz<A, __<g, B>> {
//        static <g, A, B> Leib2<g, A, B> from(Leibniz<A, B> l) {
//            return new Leib2<g, A, B>() {
//                <f> __<f, __<g, B>> subst(__<f, A> fa) {
//                    l.subst(Leibniz.<__<f, A>>refl())
//                    return null;
//                }
//            }
//        }
//    }


    public static <A, B> Leibniz<A, B> coerce(__<__<Leibniz.µ, A>, B> l) { return (Leibniz<A, B>) l;}
    public static final class µ { private µ(){} }
}
