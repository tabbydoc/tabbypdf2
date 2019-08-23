package ru.icc.td.tabbypdf2.detect.processing.recognition;

import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.*;

public class ProjectionAlgorithm implements Algorithm<Prediction> {

    @Override
    public void start(Prediction prediction) {
        Horizontal horizontal = new Horizontal(prediction.getBlocks(), prediction.getMaxY());
        Vertical vertical = new Vertical(prediction.getBlocks());
        Map<Projection.Horizontal, List<Projection.Vertical>> map = new HashMap<>();

        List<Projection.Vertical> verticals = vertical.start(new Projection.Horizontal(prediction.getMinX(),
                prediction.getMaxX(), prediction.getMaxY()));

        for (Projection.Horizontal projection : horizontal.start()) {
            map.put(projection, vertical.start(projection));
        }

        prediction.setMap(map);
        prediction.setVerticals(verticals);
    }

    private <T extends Projection> void setLevels(List<T> projections) {
        for (int i = 0; i < projections.size(); i++) {
            Projection p = projections.get(i);
            p.level = i;
        }
    }

    private <T extends Projection> void unite(List<T> projections) {
        for (int i = 0; i < projections.size(); i++) {
            Projection pI = projections.get(i);

            for (int j = 0; j < projections.size(); j++) {
                Projection pJ = projections.get(j);

                if (!pI.equals(pJ) && pI.intersectsLine(pJ)) {
                    pI.createUnion(pJ);
                    projections.remove(j);

                    j--;
                    i = -1;
                }
            }
        }

        Set<T> projectionSet = new HashSet<>(projections);
        projections.clear();
        projections.addAll(projectionSet);
    }

    private class Horizontal {
        private List<Block> blocks;
        private double position;

        Horizontal(List<Block> blocks, double maxPosition) {
            this.blocks = blocks;
            this.position = maxPosition;
        }

        List<Projection.Horizontal> start() {
            List<Projection.Horizontal> horizontals = new ArrayList<>();

            blocks.forEach(block -> horizontals.add(new Projection.Horizontal(block.getMinX(),
                    block.getMaxX(), position)));

            unite(horizontals);

            horizontals.sort(Comparator.comparing(Projection.Horizontal::getStart));
            setLevels(horizontals);

            return horizontals;
        }
    }

    private class Vertical {
        private List<Block> blocks;

        Vertical(List<Block> blocks) {
            this.blocks = blocks;
        }

        List<Projection.Vertical> start(Projection.Horizontal projection) {
            List<Projection.Vertical> verticals = new ArrayList<>();

            blocks.forEach(block -> {
                if (projection.start <= block.getMinX() && block.getMaxX() <= projection.end) {
                    verticals.add(new Projection.Vertical(block.getMinY(), block.getMaxY(), projection.start));
                }
            });

            unite(verticals);

            verticals.sort(Comparator.comparing(Projection.Vertical::getEnd).reversed());
            setLevels(verticals);

            return verticals;
        }
    }
}