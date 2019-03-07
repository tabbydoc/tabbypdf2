package ru.icc.td.tabbypdf2.detect;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Table;

import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.function.Predicate;

public class PostProcessing {
    private Table table;
    private Page page;

    public PostProcessing(Table table) {
        this.table = table;
        this.page = table.getPage();
    }

    private List<Block> blocks;

    public boolean isTable() {
        if(table == null)
            return false;

        blocks = table.getBlocks();

        if(blocks.isEmpty())
            return false;

        return processTable();
    }

    public Table getTable() {
        return newTable;
    }

    private Table newTable;

    private boolean processTable() {
        //TODO: не таблица, а подпись: eu-027.pdf
        //TODO: абзацы: us-010.pdf;
        //TODO: диаграммы: us-028.pdf вычислить отношение площадей?
        //TODO: убрал таблицы: us-014.pdf

        if(blocks.size() == 0 && isThereImage())
            return false;

        Processing processing = new Processing(blocks);
        processing.processBlocks();
        List<Rectangle2D> unions = processing.getUnions();

        if (unions.size() == 0) {
            return false;
        } else {
            if (unions.size() == 1) {
                boolean exit = false;
                List<Rectangle2D> uni = new ArrayList<>(unions);
                List<Block> blocks = new ArrayList<>(this.blocks);
                int previousSize;

                do {
                    blocks.remove(0);

                    if(blocks.size() < 1 || uni.size() == 0) {
                        exit = true;
                        break;
                    }

                    previousSize = uni.size();

                    Processing processing1 = new Processing(blocks);
                    uni = processing1.getUnions();
                } while (uni.size() == 1 || previousSize != uni.size());

                if(!exit) {
                    this.newTable = new Table(blocks, table.getPage());
                    this.newTable.setColumns(uni);
                }

                return !exit;
            } else {
                //TODO: разоразвало таблицу: us-037.pdf
                //TODO: улучшить точности границы таблицы: us-018.pdf - абзацы под таблицей
                //TODO: us-027.pdf - слишком большая граница. Зацепило абзац

                this.newTable = table;
                this.newTable.setColumns(unions);
                return true;
            }
        }
    }

    private boolean isRelationLikeTable(){
        Rectangle2D r = table.getBox();
        double squareB = 0;
        double squareR = r.getWidth() * r.getHeight();

        for(Block block : blocks){
            squareB = 0 + block.getWidth() * block.getHeight();
        }

        double relation = squareB/squareR;

        if(relation < 0.02)
            return false;

        return true;
    }

    private boolean isThereImage(){
        List<Rectangle2D> images = page.getImageBounds();

        for(Rectangle2D image : images) {
            if(table.getBox().intersects(image))
                return true;
        }

        return false;
    }

    private class Processing {
        private List<Block> blocksBuffer = new ArrayList<>();
        private Map<Rectangle2D, List<Block>> map = new HashMap<>();
        private List<Rectangle2D> unions = new ArrayList<>();
        private List<Block> blocks;

        private List<Rectangle2D> getUnions() {
            return unions;
        }

        private Processing(List<Block> blocks){
            this.blocks = blocks;
        }

        private void processBlocks() {
            List<List<Block>> lists = new ArrayList<>();
            blocks.sort(Comparator.comparing(Block::getMinX).reversed().thenComparing(Block::getMinY).reversed());
            boolean isThereNorthNeighbour;

            for (int i = 0; i < blocks.size(); i++) {
                blocksBuffer = new ArrayList<>();
                isThereNorthNeighbour = false;
                Block blockI = blocks.get(i);
                blocksBuffer.add(blockI);

                Rectangle2D r = new Rectangle2D.Double(blockI.getMinX(), blockI.getMinY(),
                        blockI.getWidth(), Math.abs(blockI.getMinY() - table.getBox().getMaxY()));

                for (int k = 0; k < blocks.size(); k++) {
                    Block blockK = blocks.get(k);

                    if (blockK.equals(blockI))
                        continue;

                    if (r.intersects(blockK)) {
                        isThereNorthNeighbour = true;
                        break;
                    }
                }

                if (isThereNorthNeighbour)
                    continue;

                Rectangle2D rectangle2D = new Rectangle2D.Double(blockI.getMinX(), table.getBox().getMinY(), blockI.getWidth(),
                        Math.abs(blockI.getMaxY() - table.getBox().getMinY()));

                for (int j = 0; j < blocks.size(); j++) {
                    Block blockJ = blocks.get(j);

                    if (blockJ.equals(blockI))
                        continue;

                    if (rectangle2D.intersects(blockJ)) {
                        addBlock(blockJ);
                    }
                }

                blocksBuffer.sort(Comparator.comparing(Block::getMaxY).reversed());
                lists.add(blocksBuffer);
            }

            for (List<Block> list : lists) {
                Rectangle2D union = new Rectangle2D.Double();
                union.setRect(list.get(0));

                for (int i = 1; i < list.size(); i++) {
                    Block block = list.get(i);
                    union.add(block);
                }

                unions.add(union);
                map.put(union, list);
            }

            processUnions();
        }

        private void addBlock(Block block) {
            blocksBuffer.add(block);
            hasBlockIntersections(block);
        }

        private void hasBlockIntersections(Block block) {
            Rectangle2D rectangle2D = new Rectangle2D.Double(block.getMinX(), table.getBox().getMinY(), block.getWidth(),
                    Math.abs(block.getMaxY() - table.getBox().getMinY()));

            for (Block block1 : blocks) {

                if (blocksBuffer.contains(block1))
                    continue;

                if (rectangle2D.intersects(block1))
                    addBlock(block1);
            }
        }

        private void processUnions() {
            List<Block> blocksI;
            List<Block> blocksJ;
            List<Rectangle2D> buffer = new ArrayList<>();

            for (int i = 0; i < unions.size(); i++) {
                Rectangle2D rectangleI = unions.get(i);
                Rectangle2D rectangle1 = rectangleI;
                blocksI = map.get(rectangleI);

                for (int j = 0; j < unions.size() && blocksI != null; j++) {
                    Rectangle2D rectangleJ = unions.get(j);
                    Rectangle2D rectangle2 = rectangleJ;
                    blocksJ = map.get(rectangleJ);

                    if (rectangleI.equals(rectangleJ) || blocksJ == null)
                        continue;

                    if (rectangleI.intersects(rectangleJ)) {
                        List<Block> intersectedBlocks = new ArrayList<>();
                        Rectangle2D rectangle = rectangleI.createIntersection(rectangleJ);

                        for (Block blockI : blocksI) {
                            if (blockI.intersects(rectangle))
                                intersectedBlocks.add(blockI);
                        }

                        for (Block blockJ : blocksJ) {
                            if (blockJ.intersects(rectangle) && !intersectedBlocks.contains(blockJ))
                                intersectedBlocks.add(blockJ);
                        }

                        intersectedBlocks.sort(Comparator.comparing(Block::getWidth).reversed());

                        while (rectangle1.intersects(rectangle2) && intersectedBlocks.size() > 0) {
                            Block block = intersectedBlocks.remove(0);

                            blocksI.remove(block);
                            blocksJ.remove(block);

                            if (blocksI.size() < 2 || blocksJ.size() < 2){
                                break;
                            }

                            rectangle1 = union(blocksI);
                            rectangle2 = union(blocksJ);
                        }
                    }

                    blocksI.sort(Comparator.comparing(Block::getMaxY).reversed());
                    blocksJ.sort(Comparator.comparing(Block::getMaxY).reversed());
                    map.remove(rectangleI);
                    map.remove(rectangleJ);
                    unions.remove(rectangleI);
                    unions.remove(rectangleJ);
                    i = -1;
                    j = -1;

                    if(rectangle1.intersects(rectangle2)){
                        rectangle1.add(rectangle2);
                        blocksI.addAll(blocksJ);
                        map.put(rectangle1, blocksI);
                        buffer.add(rectangle1);
                    } else {
                        buffer.add(rectangle2);
                        map.put(rectangle1, blocksI);
                        map.put(rectangle2, blocksJ);
                        buffer.add(rectangle1);
                    }
                }
            }
            this.unions = new ArrayList<>(buffer);
        }

        private Rectangle2D union(List<Block> blocks) {
            Rectangle2D rectangle = new Rectangle2D.Double();
            rectangle.setRect(blocks.get(0));

            for (int i = 1; i < blocks.size(); i++) {
                rectangle.add(blocks.get(i));
            }

            return rectangle;
        }

        private boolean isThereImage() {
            List<Block> blocks = table.getBlocks();
            List<Rectangle2D> images = page.getImageBounds();

            for (Rectangle2D image : images) {
                for (Block block : blocks) {
                    if (image.intersects(block))
                        return true;
                }
            }

            return false;
        }
    }
}