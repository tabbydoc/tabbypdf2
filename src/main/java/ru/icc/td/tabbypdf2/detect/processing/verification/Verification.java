package ru.icc.td.tabbypdf2.detect.processing.verification;

import ru.icc.td.tabbypdf2.model.Prediction;

public interface Verification {

    boolean verify(Prediction prediction);
}
