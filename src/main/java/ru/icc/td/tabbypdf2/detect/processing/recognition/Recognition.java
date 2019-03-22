package ru.icc.td.tabbypdf2.detect.processing.recognition;

interface Recognition<T, S> {

    S recognize(T obj);

}
