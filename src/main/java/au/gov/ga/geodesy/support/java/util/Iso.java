package au.gov.ga.geodesy.support.java.util;

/**
 * Isomorphisms with composition.
 *
 * An isomorphism is a pair of functions, {@code A -> B} and {@code B -> A}.
 * Composing {@code A <-> B} with {@code B <-> C} gives {@code A <-> C}.
 */
public interface Iso<A, B> {
    B to(A a);
    A from(B b);

    public default <C> Iso<A, C> compose(Iso<B, C> iso) {
        return new Iso<A, C>() {
            public C to(A a) {
                return iso.to(Iso.this.to(a));
            }

            public A from(C c) {
                return Iso.this.from(iso.from(c));
            }
        };
    }
}
