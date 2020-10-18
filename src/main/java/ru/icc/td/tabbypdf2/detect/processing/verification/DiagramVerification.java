package ru.icc.td.tabbypdf2.detect.processing.verification;

import ru.icc.td.tabbypdf2.detect.processing.verification.feature.*;
import ru.icc.td.tabbypdf2.interfaces.Verification;
import ru.icc.td.tabbypdf2.model.Prediction;

public class DiagramVerification implements Verification {

    @Override
    public boolean verify(Prediction prediction) {
        ParameterFactory factory = new ParameterFactory(prediction);

        double[] r = Model.score(factory.getParameters());
        return r[1] >= 0.50;
    }

}