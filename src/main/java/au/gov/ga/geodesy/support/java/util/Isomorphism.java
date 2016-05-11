package au.gov.ga.geodesy.support.java.util;

public interface Isomorphism<A, B> {
    B to(A a);
    A from(B b);
}
