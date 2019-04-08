package ru.icc.td.tabbypdf2.detect.processing.verification;

import ru.icc.td.tabbypdf2.model.Prediction;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class ImageVerification implements Verification {

    @Override
    public boolean verify(Prediction prediction) {
        List<Rectangle2D> images = prediction.getPage().getImageBounds();
        Rectangle2D rectangle = prediction.getBounds2D();

        for(Rectangle2D image : images) {

            if (image.intersects(rectangle)) {
                double square1 = rectangle.getHeight() * rectangle.getWidth();
                Rectangle2D intersection = image.createIntersection(rectangle);
                double square2 = intersection.getHeight() * intersection.getWidth();

                if (square2/square1 > 0.5) {
                    return false;
                }
            }
        }

        return true;
    }
}
