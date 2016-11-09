package fr.gn.hkt.mvn;

import org.derive4j.hkt.__;

public interface Coerce<f, A, FA> {
    FA coerce(__<f, A> fa);
}
