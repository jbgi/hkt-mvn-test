package fr.gn.hkt;

import fj.F;
import fj.Unit;
import fr.gn.hkt.mvn.*;
import fr.gn.hkt.mvn.syntax.Do;
import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.hkt.__;

import java.io.Console;

import static fj.Unit.unit;
import static fr.gn.hkt.Interacts.Ask;
import static fr.gn.hkt.Interacts.Tell;
import static fr.gn.hkt.ResultSetOps.*;
import static org.derive4j.Make.constructors;
import static org.derive4j.Make.patternMatching;

@SuppressWarnings({"WeakerAccess", "unused", "TrivialMethodReference"})
public final class TestFree {
    private TestFree() {}

    // ################################ LANGUAGES ############

    // ## ResultSetOp (Doobie)

    @Data(value = @Derive(make = {constructors, patternMatching}), flavour = Flavour.FJ)
    static abstract class ResultSetOp<A> implements __<ResultSetOp.µ, A> {
        ResultSetOp() {}
        interface Cases<R, A> {
            R Next(F<Boolean, A> __);
            R GetInt(Integer i, F<Integer, A> __);
            R GetString(Integer i, F<String, A> __);
            R Close(F<Unit, A> __);
        }
        abstract <R> R match(Cases<R, A> cases);

        static <A> ResultSetOp<A> coerce(__<ResultSetOp.µ, A> r) { return (ResultSetOp<A>) r; }
        public static final class µ { private µ() {} }
    }
// TYPE ALIAS FOR FREE
//    @FunctionalInterface
//    interface ResultSetM<A> extends Free.Of<ResultSetOp.µ, A> {
//        static Instances<ResultSetOp.µ> instances() { return new Instances<>(); }
//        static <A> ResultSetM<A> coerce(__<__<Free.Of.µ, ResultSetOp.µ>, A> fof) {
//            return Free.Of.coerce(fof)::free;
//        }
//    }
//
//    // Smart constructors (single language)
//    static final ResultSetM<Boolean> next = () -> Free.liftF(Next());
//    static ResultSetM<Integer> getInt(Integer i) { return () -> Free.liftF(GetInt(i)); }
//    static ResultSetM<String> getString(Integer i) { return () -> Free.liftF(GetString(i)); }
//    static final ResultSetM<Unit> close = () -> Free.liftF(Close());

    // Smart constructors (several languages)
    @FunctionalInterface
    interface ResultSetOpCs<f> {
        Inject<ResultSetOp.µ, f> I();

        default Free<f, Boolean> next() { return Inject.lift(I(), Next()); }
        default Free<f, Integer> getInt(Integer i) { return Inject.lift(I(), GetInt(i)); }
        default Free<f, String> getString(Integer i) { return Inject.lift(I(), GetString(i)); }
        default Free<f, Unit> close() { return Inject.lift(I(), Close()); }
    }


    // ## Interact

    @Data(value = @Derive(make = {constructors, patternMatching}), flavour = Flavour.FJ)
    static abstract class Interact<A> implements __<Interact.µ, A> {
        Interact() {}
        interface Cases<R, A> {
            R Ask(String prompt, F<String, A> __);
            R Tell(String msg, F<Unit, A> __);
        }
        abstract <R> R match(Cases<R, A> cases);

        static <A> Interact<A> coerce(__<Interact.µ, A> i) { return (Interact<A>) i;}
        static final class µ { private µ() {} }
    }

    // smart constructors
    @FunctionalInterface
    interface InteractCs<f> {
        Inject<Interact.µ, f> I();

        default Free<f, String> ask(String prompt) { return Inject.lift(I(), Ask(prompt)); }
        default Free<f, Unit> tell(String msg) { return Inject.lift(I(), Tell(msg)); }
    }

    // ################################ PROGRAM ############

    static <f> Free<f, Unit> prg(InteractCs<f> I, ResultSetOpCs<f> R) {
        return Do.$(Free.instances(), Free::coerce

            , () -> I.ask("String or Int ?")

            , __ -> I.ask("Row number ?").map(Integer::valueOf)

            , (r, i) -> r.equals("string")
                ? R.getString(i).map(Eithers::<String, Integer>Left)
                : R.getInt(i).map(Eithers::<String, Integer>Right)

            , __ -> Eithers.<String, Integer>cases()
                .Right(ri -> I.tell("Int found : " + ri))
                .Left(ls -> I.tell("String found : " + ls))
                .f(__._3())

            , __ -> unit());
    }

    // ################################ INTERPRETERS ############

    static final NF<Interact.µ, IO.µ> console = TestFree::consoleImpl;

    private static <A> IO<A> consoleImpl(__<Interact.µ, A> fa) {
        final IO<Console> cs = System::console;
        return Interacts.<A>cases()
            .Ask((prompt, __) -> cs.bind(c -> () -> __.f(c.readLine(prompt))))
            .Tell((msg, __) -> cs.bind(c -> () -> { c.printf(msg + "\n"); return unit(); }).map(__))
            .f(Interact.coerce(fa));
    }

    static final NF<ResultSetOp.µ, IO.µ> fakeDB = TestFree::fakeDBImpl;

    private static <A> IO<A> fakeDBImpl(__<ResultSetOp.µ, A> fa) {
        return ResultSetOps.<A>cases().<IO<A>>
            Next(__ -> () -> __.f(true))
            .GetInt((i, __) -> () -> __.f(33))
            .GetString((i, __) -> () -> __.f("toto"))
            .Close(__ -> () -> __.f(unit()))
            .f(ResultSetOp.coerce(fa));
    }

    // ################################ APPLICATION ############

    static final Free<__<__<Coproduct.µ, ResultSetOp.µ>, Interact.µ>, Unit> app =
        prg(() -> Inject.rightInstance(Inject.reflexiveInstance()), () -> Inject.leftInstance());

    static IO<Unit> runApp()  {
        return IO.coerce(app.foldMap(IO.instances(), fakeDB.or(console)));
    }

    public static void main(String[] args) throws Throwable {
        runApp().run();
    }
}
