package ru.icc.td.tabbypdf2.model;

public class Gap<T> {

    T left;
    T right;

    public Gap(T left, T right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }
}
