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
        setRefinements();
    }

    @Override
    public void process(Page page) {
        refinements.forEach(r -> r.refine(page));
    }

    private void setRefinements() {
        refinements.addAll(Arrays.asList(new ChunkIDRefinement(), new IntersectedBlocks(),
                new IsolatedBlocks(), new SeparatedWords()));
    }
}
