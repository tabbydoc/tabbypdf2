package ru.icc.td.tabbypdf2.detect;

import com.google.protobuf.TextFormat;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;
import org.tensorflow.types.UInt8;
import ru.icc.td.tabbypdf2.protos.StringIntLabelMapOuterClass.StringIntLabelMap;
import ru.icc.td.tabbypdf2.protos.StringIntLabelMapOuterClass.StringIntLabelMapItem;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AnnModel {

    private static AnnModel instance;
    private Path pathToModel;
    private Path pathToLabelMap;
    private String[] labels;
    private SavedModelBundle model;

    private AnnModel(Path pathToModel, Path pathToLabelMap) {
        this.pathToModel = pathToModel;
        this.pathToLabelMap = pathToLabelMap;
    }

    public static AnnModel getInstance(Path pathToModel, Path pathToLabelMap) throws Exception {
        if (instance == null) {
            instance = new AnnModel(pathToModel, pathToLabelMap);
            instance.init();
        }
        return instance;
    }

    public boolean init() throws Exception {
        try {
            model = SavedModelBundle.load(pathToModel.toFile().getName(), "serve");
            labels = loadLabels(pathToLabelMap);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<TensorBox> detectTables(BufferedImage img) throws IOException {
        List<Tensor<?>> outputs = null;
        List<TensorBox> result = new ArrayList<TensorBox>();
        try (Tensor<UInt8> input = makeImageTensor(img)) {
            outputs =  model
                            .session()
                            .runner()
                            .feed("image_tensor", input)
                            .fetch("detection_scores")
                            .fetch("detection_classes")
                            .fetch("detection_boxes")
                            .run();
        }
        try (Tensor<Float> scoresT = outputs.get(0).expect(Float.class);
             Tensor<Float> classesT = outputs.get(1).expect(Float.class);
             Tensor<Float> boxesT = outputs.get(2).expect(Float.class)) {
            int maxObjects = (int) scoresT.shape()[1];
            float[] scores = scoresT.copyTo(new float[1][maxObjects])[0];
            float[] classes = classesT.copyTo(new float[1][maxObjects])[0];
            float[][] boxes = boxesT.copyTo(new float[1][maxObjects][4])[0];
            boolean foundSomething = false;
            for (int i = 0; i < scores.length; ++i) {
                if (scores[i] < 0.1) {
                    continue;
                }
                foundSomething = true;
                String labelClass = labels[(int) classes[i]];
                TensorBox tensorBox = new TensorBox(scores[i], labelClass, boxes[i]);
                result.add(tensorBox);
            }
            if (!foundSomething) {
                return null;
            }
            return result;
        }

    }

    private String[] loadLabels(Path filename) throws Exception {
        String text = new String(Files.readAllBytes(filename), StandardCharsets.UTF_8);
        StringIntLabelMap.Builder builder = StringIntLabelMap.newBuilder();
        TextFormat.merge(text, builder);
        StringIntLabelMap proto = builder.build();
        int maxId = 0;
        for (StringIntLabelMapItem item : proto.getItemList()) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }
        String[] ret = new String[maxId + 1];
        for (StringIntLabelMapItem item : proto.getItemList()) {
            ret[item.getId()] = item.getDisplayName();
        }
        return ret;
    }

    private void bgr2rgb(byte[] data) {
        for (int i = 0; i < data.length; i += 3) {
            byte tmp = data[i];
            data[i] = data[i + 2];
            data[i + 2] = tmp;
        }
    }

    private Tensor<UInt8> makeImageTensor(BufferedImage img) throws IOException {
        if (img.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            throw new IOException(
                    String.format(
                            "Expected 3-byte BGR encoding in BufferedImage, found %d. This code could be made more robust",
                            img.getType()));
        }
        byte[] data = ((DataBufferByte) img.getData().getDataBuffer()).getData();
        bgr2rgb(data);
        final long BATCH_SIZE = 1;
        final long CHANNELS = 3;
        long[] shape = new long[] {BATCH_SIZE, img.getHeight(), img.getWidth(), CHANNELS};
        return Tensor.create(UInt8.class, shape, ByteBuffer.wrap(data));
    }


}
