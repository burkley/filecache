/*
 * Class:   Box
 * Born On: Feb 29, 2012
 * Purpose:
 */
package org.fgb.io.filecache.util;

/**
 * Generic version of the Box class.
 * @param <T> the type of value being boxed
 */

public class Box<T> {

    // T stands for "Type"
    private T t;

    public void add(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }
}
