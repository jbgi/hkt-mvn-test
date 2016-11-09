package fr.gn.hkt.mvn;

import org.derive4j.hkt.__;

/** Natural transformation */
public interface NF<f, g> {
    <A> __<g, A> f(__<f, A> fa);

    default  <e> NF<__<__<Coproduct.µ, f>, e>, g> or(NF<e, g> f) { return new NF<__<__<Coproduct.µ, f>, e>, g>() {
        public <A> __<g, A> f(__<__<__<Coproduct.µ, f>, e>, A> c) {
            return Eithers.<__<f, A>, __<e, A>>cases()
                .Right(f::f)
                .Left(NF.this::f)
                .f(Coproduct.coerce(c).run());
        }
    };}

     interface InR<f, g1, g2> {
        <A, G2A extends __<g2, A>> __<g1, G2A> f(Coerce<g2, A, G2A> C, __<f, A> fa);
    }
}
