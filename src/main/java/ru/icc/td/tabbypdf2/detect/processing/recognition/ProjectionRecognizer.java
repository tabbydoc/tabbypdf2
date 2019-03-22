package ru.icc.td.tabbypdf2.detect.processing.recognition;

import ru.icc.td.tabbypdf2.model.Block;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ProjectionRecognizer implements Recognition<List<Block>, List<Projection>> {
    private final List<Projection> projections = new ArrayList<>();
    private final List<Block> blocks = new ArrayList<>();

    @Override
    public List<Projection> recognize(List<Block> blocks) {
        setAll(blocks);
        refineProjections();
        setLevels();

        return projections;
    }

    private void refineProjections() {
        Projection pI;
        Projection pJ;

        for (int i = 0; i < projections.size(); i++) {
            pI = projections.get(i);

            for (int j = 0; j < projections.size(); j++) {
                pJ = projections.get(j);

                if (pI.equals(pJ)) {
                    continue;
                }

                if (pI.intersectsProjection(pJ)) {
                    pI = pI.createIntersection(pJ);
                    projections.remove(pJ);
                    j--;
                }
            }
        }
    }

    private void setLevels(){
        Projection p;
        projections.sort(Comparator.comparing(Projection::getStart).reversed());

        for (int i = 0; i < projections.size(); i++) {
            p = projections.get(i);
            p.setLevel(i);
        }
    }

    private void setAll(List<Block> blockList) {
        projections.clear();
        blocks.clear();
        blocks.addAll(blockList);
        blocks.sort(Comparator.comparing(Block::getMaxY).reversed().thenComparing(Block::getMinX));
        blocks.forEach(block -> projections.add(createProjection(block)));
    }

    private Projection createProjection(Block block) {
        float y1 = block.y - block.height;
        float y2 = block.y;

        return new Projection(y1, y2);
    }
}