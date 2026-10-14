package com.example.foxpuzzle;

import java.util.List;

public class ArrayListView<E> extends java.util.AbstractList<E> {

    private final E[] array;

    ArrayListView(E[] array) {
        this.array = array;
    }

    @Override
    public E get(int index) {
        return array[index];
    }

    @Override
    public E set(int index, E element) {
        E old = array[index];
        array[index] = element;
        return old;
    }

    @Override
    public int size() {
        return array.length;
    }
}
