package ru.icc.td.tabbypdf2.detect.processing.recognition;

import ru.icc.td.tabbypdf2.model.Block;

import java.util.*;

class ProjectionRecognizer implements Recognition<List<Block>, List<Projection>> {
    private List<Projection> projections;

    @Override
    public List<Projection> recognize(List<Block> blocks) {
        setProjections(blocks);
        uniteProjections();
        setLevels();

        return projections;
    }

    private void uniteProjections() {
        Projection pI;
        Projection pJ;

        for (int i = 0; i < projections.size(); i++) {
            pI = projections.get(i);

            for (int j = 0; j < projections.size(); j++) {
                pJ = projections.get(j);

                if (pI != pJ && pI.intersectsProjection(pJ)) {
                    pI = pI.createIntersection(pJ);
                    projections.remove(pJ);
                    j--;
                    i = -1;
                }
            }
        }
    }

    private void setLevels(){
        Projection p;
        projections.sort(Comparator.comparing(Projection::getEnd).reversed());

        for (int i = 0; i < projections.size(); i++) {
            p = projections.get(i);
            p.setLevel(i);
        }
    }

    private void setProjections(List<Block> blocks) {
        projections = new ArrayList<>();
        blocks.sort(Comparator.comparing(Block::getMaxY).reversed().thenComparing(Block::getMinX));
        blocks.forEach(block -> projections.add(createProjection(block)));
    }

    private Projection createProjection(Block block) {
        float y1 = block.y;
        float y2 = block.y + block.height;

        return new Projection(y1, y2);
    }
}