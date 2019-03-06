package ru.icc.td.tabbypdf2.detect;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Table;

import java.awt.geom.Rectangle2D;
import java.util.*;

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

    public Table getNewTable() {
        return newTable;
    }

    private Table newTable;

    private boolean processTable() {
        //TODO: изображения; разряжённые области;
        Processing processing = new Processing(blocks);
        processing.processBlocks();
        List<Rectangle2D> unions = processing.getUnions();

        if (unions.size() == 0) {
            return false;
        } else {
            if (unions.size() == 1) {
                //TODO: возвращать обновлённую таблицу
                boolean exit = false;
                List<Rectangle2D> uni;
                List<Block> blocks = new ArrayList<>(this.blocks);

                do {
                    blocks.remove(0);

                    if(blocks.size() < 1) {
                        exit = true;
                        break;
                    }

                    Processing processing1 = new Processing(blocks);
                    uni = processing1.getUnions();
                } while (uni.size() == 1);

                if(!exit) {
                    this.newTable = new Table(blocks, table.getPage(), table.getBox());
                }

                return !exit;
            } else {
                this.newTable = table;
                return true;
            }
        }
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
                    union.createUnion(block);
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

            for (int i = 0; i < unions.size(); i++) {
                Rectangle2D rectangleI = unions.get(i);
                Rectangle2D rectangle1 = rectangleI;
                blocksI = map.get(rectangleI);

                for (int j = 0; j < unions.size(); j++) {
                    Rectangle2D rectangleJ = unions.get(j);
                    Rectangle2D rectangle2 = rectangleJ;
                    blocksJ = map.get(rectangleJ);

                    if (rectangleI.equals(rectangleJ))
                        continue;

                    if (rectangleI.intersects(rectangleJ)) {
                        blocksI = map.get(rectangleI);
                        blocksJ = map.get(rectangleJ);
                        HashSet<Block> intersectedBlocks = new HashSet<>();

                        for (Block blockI : blocksI) {
                            for (Block blockJ : blocksJ) {
                                if (blockI.equals(blockJ))
                                    continue;

                                if (blockI.intersects(blockJ)) {
                                    intersectedBlocks.add(blockI);
                                    intersectedBlocks.add(blockJ);
                                }
                            }
                        }

                        Rectangle2D rectangle;

                        do {
                            rectangle = rectangleI.createIntersection(rectangleJ);

                            for (Block block : intersectedBlocks) {
                                if (block.getWidth() == rectangle.getWidth()) {
                                    blocksI.remove(block);
                                    blocksJ.remove(block);

                                    rectangle1 = union(blocksI);
                                    rectangle2 = union(blocksJ);

                                    break;
                                }
                            }
                        } while (rectangleI.intersects(rectangleJ) && blocksI.size() != 0 && blocksJ.size() != 0);
                    }
                    blocksJ.sort(Comparator.comparing(Block::getMaxY).reversed());
                    map.remove(rectangleJ);
                    map.put(rectangle2, blocksJ);
                    unions.remove(rectangleJ);
                    unions.add(rectangle2);
                }
                blocksI.sort(Comparator.comparing(Block::getMaxY).reversed());
                map.remove(rectangleI);
                map.put(rectangle1, blocksI);
                unions.remove(rectangleI);
                unions.add(rectangle1);
            }
        }

        private Rectangle2D union(List<Block> blocks) {
            Rectangle2D rectangle = new Rectangle2D.Double();

            for (Block block : blocks) {
                rectangle.createUnion(block);
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