package ru.icc.td.tabbypdf2.detect;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Table;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostProcessing {

    public PostProcessing() {

    }

    private Page page;
    private List<Table> buffer = new ArrayList<>();

    public void process(Document document) {
        if (null == document)
            return;

        List<Page> pages = document.getPages();

        if (pages.isEmpty())
            return;

        for (Page page : pages) {
            List<Table> tables = page.getTables();

            if(tables.isEmpty())
                continue;

            this.page = page;

            for(Table table : tables)
                processTable(table);

            page.getTables().removeAll(buffer);
        }
    }

    private void processTable(Table table){
        buffer.clear();

        List<Block> blocks = table.getBlocks();
        List<List<Block>> lists = processBlocks(blocks, table);

        if(lists.size() == 0) {
            buffer.add(table);
            return;
        } else
            if(lists.size() < 2) {
                boolean exit = false;
                while (lists.size() < 2 && !exit) {
                    List<Block> blocks1 = new ArrayList<>();

                    for (List<Block> list : lists) {
                        list.remove(0);
                        blocks1.addAll(list);

                        if(list.size() < 1) {
                            exit = true;
                        }
                    }

                    buffer.add(table);
                    if(!exit)
                        lists = processBlocks(blocks1, table);
                }

            } else {
                List<Line2D> lines = new ArrayList<>();

                for(List<Block> list : lists){
                    Block block = Collections.max(list, Comparator.comparing(Block::getWidth));
                    Line2D line2D = new Line2D.Double(block.getMinX(), 0, block.getMinX() + block.getWidth(),
                            0);
                    lines.add(line2D);
                }

                for(int i = 0; i < lines.size(); i++){
                    Line2D lineI = lines.get(i);

                    for(int j = 0; j < lines.size(); j++){
                        Line2D lineJ = lines.get(j);

                        if(lineI.equals(lineJ))
                            continue;

                        if(lineI.intersectsLine(lineJ)) {
                            buffer.add(table);
                        }
                    }
                }
            }


        System.out.println("Done " + page.getIndex());
    }

    private List<List<Block>> processBlocks(List<Block> blocks, Table table){
        List<List<Block>> lists = new ArrayList<>();
        blocks.sort(Comparator.comparing(Block::getMinX).reversed().thenComparing(Block::getMinY).reversed());
        boolean isThereNorthNeighbour;

        for(int i = 0; i < blocks.size(); i++){
            isThereNorthNeighbour = false;
            List<Block> blocksBuffer = new ArrayList<>();
            Block blockI = blocks.get(i);
            blocksBuffer.add(blockI);

            Rectangle2D r = new Rectangle2D.Double(blockI.getMinX(), blockI.getMinY(),
                    blockI.getWidth(), Math.abs(blockI.getMinY() - table.getBox().getMaxY()));


            for(int k = 0; k < blocks.size(); k++){
                Block blockK = blocks.get(k);

                if(blockK.equals(blockI))
                    continue;

                if(r.intersects(blockK)) {
                    isThereNorthNeighbour = true;
                    break;
                }
            }

            if(isThereNorthNeighbour)
                continue;

            blocks.remove(blockI);
            Rectangle2D rectangle2D = new Rectangle2D.Double(blockI.getMinX(), table.getBox().getMinY(), blockI.getWidth(),
                    Math.abs(blockI.getMaxY() - table.getBox().getMinY()));

            for(int j = 0; j < blocks.size(); j++){
                Block blockJ = blocks.get(j);

                if(blockJ.equals(blockI))
                    continue;

                if(rectangle2D.intersects(blockJ)){
                    blocksBuffer.add(blockJ);
                    blocks.remove(blockJ);
                }
            }

            blocksBuffer.sort(Comparator.comparing(Block::getMaxY).reversed());
            lists.add(blocksBuffer);
        }

        return lists;
    }


}




















