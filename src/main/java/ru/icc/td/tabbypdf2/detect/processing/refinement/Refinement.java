package ru.icc.td.tabbypdf2.detect.processing.refinement;

import ru.icc.td.tabbypdf2.model.Prediction;

public interface Refinement {

    Prediction refine(Prediction o);

}
