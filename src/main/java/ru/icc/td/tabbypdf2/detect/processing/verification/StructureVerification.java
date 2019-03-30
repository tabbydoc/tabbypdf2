package ru.icc.td.tabbypdf2.detect.processing.verification;

import ru.icc.td.tabbypdf2.model.Prediction;

public class StructureVerification implements Verification {

    @Override
    public boolean verify(Prediction prediction) {
        return prediction.getStructure() != null;
    }
}
