package ru.icc.td.tabbypdf2.comp.block.refinement;

import ru.icc.td.tabbypdf2.interfaces.Processing;
import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.model.Page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockProcessing implements Processing<Page> {
    private final List<Refinement<Page>> refinements = new ArrayList<>();

    public BlockProcessing() {
        setAll();
    }

    @Override
    public void process(Page page) {
        refinements.forEach(r -> r.refine(page));
    }

    private void setAll() {
        refinements.addAll(Arrays.asList(new IntersectedBlocks(), new IsolatedBlocks(),
                new ChunkIDRefinement()));
    }
}
