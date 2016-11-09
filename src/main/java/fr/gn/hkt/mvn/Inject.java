package fr.gn.hkt.mvn;

import fr.gn.hkt.mvn.FunctorTC.Functor;
import fr.gn.hkt.mvn.data.Maybe;
import org.derive4j.hkt.__;

import static fr.gn.hkt.mvn.Maybes.Just;

public interface Inject<f, g> extends NF<f, g> {
    @Override
    default <A> __<g, A> f(__<f, A> fa) { return inj(fa); }
    <A> __<g, A> inj(__<f, A> fa);
    <A> Maybe<__<f, A>> prj(__<g, A> ga);

    static <f> Inject<f, f> reflexiveInstance() { return new Inject<f, f>() {
        @Override
        public <A> __<f, A> inj(__<f, A> fa) { return fa; }
        @Override
        public <A> Maybe<__<f, A>> prj(__<f, A> ga) { return Just(ga); }
    };}

    static <f, g> Inject<f, __<__<Coproduct.µ, f>, g>> leftInstance() { return new Inject<f, __<__<Coproduct.µ, f>, g>>() {
        @Override
        public <A> Coproduct<f, g, A> inj(__<f, A> fa) { return Coproduct.leftc(fa); }
        @Override
        public <A> Maybe<__<f, A>> prj(__<__<__<Coproduct.µ, f>, g>, A> ga) {
            return Coproduct.coerce(ga).run().fold(Maybes::Just, __ -> Maybes.Nothing());
        }
    };}

    static <f, g, h> Inject<f, __<__<Coproduct.µ, h>, g>> rightInstance(Inject<f, g> I) {
        return new Inject<f, __<__<Coproduct.µ, h>, g>>() {
            @Override
            public <A> Coproduct<h, g, A> inj(__<f, A> fa) { return Coproduct.rightc(I.inj(fa)); }
            @Override
            public <A> Maybe<__<f, A>> prj(__<__<__<Coproduct.µ, h>, g>, A> ga) {
                return Coproduct.coerce(ga).run().fold(__ -> Maybes.Nothing(), I::prj);
            }
        };
    }

    static <f, g, A> Free<g, A> lift(Inject<f, g> I, __<f, A> fa) { return Free.liftF(I.inj(fa)); }

    static <f, g, A> Free<f, A> inject(Inject<g, f> I, __<g, Free<f, A>> ga) { return Free.roll(I.inj(ga)); }

    static <f, g, A> Maybe<__<g, Free<f, A>>> match_(Functor<f> F, Inject<g, f> I, Free<f, A> fa) {
        return fa.resume(F).fold(I::prj, __ -> Maybes.Nothing());
    }
}
