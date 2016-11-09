package fr.gn.hkt.mvn;

import fr.gn.hkt.mvn.data.Either;
import org.derive4j.hkt.__;
import org.derive4j.hkt.__3;

import static fr.gn.hkt.mvn.Eithers.Left;
import static fr.gn.hkt.mvn.Eithers.Right;

public abstract class Coproduct<f, g, A> implements __3<Coproduct.µ, f, g, A> {
    private final Either<__<f, A>, __<g, A>> run;
    protected Coproduct(Either<__<f, A>, __<g, A>> run) {this.run = run;}

    public final Either<__<f, A>, __<g, A>> run() { return run; }

    public static <f, g, A> Coproduct<f, g, A> leftc(__<f, A> x) { return new Coproduct<f, g, A>(Left(x)) {}; }

    public static <f, g, A> Coproduct<f, g, A> rightc(__<g, A> x) { return new Coproduct<f, g, A>(Right(x)) {}; }

    public static <g> CoproductLeft<g> left() { return new CoproductLeft<>(); }

    public static <f> CoproductRight<f> right() { return new CoproductRight<>(); }

    public static final class CoproductLeft<g> {
        private CoproductLeft() {}
        public <f, A> Coproduct<f, g, A> of(__<f, A> fa) { return new Coproduct<f, g, A>(Left(fa)) {}; }
    }

    public static final class CoproductRight<f> {
        private CoproductRight() {}
        public <g, A> Coproduct<f, g, A> of(__<g, A> ga) { return new Coproduct<f, g, A>(Right(ga)) {}; }
    }

    public static <f, g, A> Coproduct<f, g, A> coerce(__<__<__<Coproduct.µ, f>, g>, A> c) {
        return (Coproduct<f, g, A>) c;
    }
    public static final class µ { private µ(){} }
}
