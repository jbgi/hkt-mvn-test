package fr.gn.hkt.mvn;

import fr.gn.hkt.mvn.FunctorTC.Functor;
import fr.gn.hkt.mvn.data.List;
import fr.gn.hkt.mvn.data.Maybe;
import fr.gn.hkt.mvn.Free.Trampoline;
import fr.gn.hkt.mvn.fun.F;
import fr.gn.hkt.mvn.fun.P2;
import org.derive4j.hkt.__;

import static fr.gn.hkt.mvn.Id.id;
import static fr.gn.hkt.mvn.fun.P2.p;

public class Test {
    private Test() {}

    static final NF<Id.µ, List.µ> singletonList = new NF<Id.µ, List.µ>() {
        public <A> List<A> f(__<Id.µ, A> fa) {
            return Lists.Cons(Id.coerce(fa).is(), Lists.Nil());
        }
    };

    static <B> P2<List<B>, List<String>> testNatTr(NF<Id.µ, List.µ> f, B b, String s) {
        return p(List.coerce(f.f(id(b))), List.coerce(f.f(id(s))));
    }

    static <f, A, B> __<f, B> testMap(Functor<f> F, __<f, A> fa, F<A, B> f) {
        return F.fmap(fa, f);
    }

    static <A, B> Trampoline<B> foldrT(List<A> as, F<A, F<B, B>> f, B b) {
        return Trampoline.suspend(() -> Lists.<A>cases()
            .Cons((head, tail) -> foldrT(tail, f, b).map(f.f(head)))
            .Nil(() -> Trampoline.done(b))
            .f(as));
    }

    public static void main(String[] args) {
        final __<Maybe.µ, String> toto = testMap(Maybe.instances(), Maybes.Just("toto"), s -> s + "tata");

        final P2<List<Integer>, List<String>> toto2 = testNatTr(singletonList, 2, "toto");

        final List<Integer> is = List.range(0, 1_000_000);

        //System.out.println(is.foldRight(a -> b -> a + b.f(), 0)); // StackOverflow

        System.out.println(foldrT(is, a -> b -> a + b, 0).run());

    }
}
