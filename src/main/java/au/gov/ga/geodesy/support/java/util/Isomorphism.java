package au.gov.ga.geodesy.support.java.util;

import java.util.function.Function;

public interface Isomorphism<A, B> {
    Function<A, B> to();
    Function<B, A> from();

    public default B to(A a) {
        return to().apply(a);
    }

    public default A from(B b) {
        return from().apply(b);
    }
}
