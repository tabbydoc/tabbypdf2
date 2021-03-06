package ru.icc.td.tabbypdf2.detect.processing;

import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

final class TableComposer {

    Table compose(Prediction prediction) {
        return new Table(prediction.getBlocks(), prediction.getPage(), prediction.getStructure());
    }

}
