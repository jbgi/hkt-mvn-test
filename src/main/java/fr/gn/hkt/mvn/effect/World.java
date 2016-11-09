package fr.gn.hkt.mvn.effect;

public final class World<A> {
    private final A a;
    private World(A a) {this.a = a;}

    static <A> World<A> of(A a) { return new World<>(a); }

    public enum Real { World; }
    static final World<Real> realWord = World.of(Real.World);
}
