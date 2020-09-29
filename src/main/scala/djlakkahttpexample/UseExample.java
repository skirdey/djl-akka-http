package djlakkahttpexample;

import ai.djl.Application;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class UseExample {

    static final ZooModel<String[], float[][]> model = loadModel();
    static final ThreadLocal<Predictor<String[], float[][]>> predictorHolder = new ThreadLocal<>();


    private final static ZooModel<String[], float[][]> loadModel() {
        try {
            if (!"TensorFlow".equals(Engine.getInstance().getEngineName())) {
                return null;
            }

            String modelUrl =
                    "https://storage.googleapis.com/tfhub-modules/google/universal-sentence-encoder/4.tar.gz";

            Criteria<String[], float[][]> criteria =
                    Criteria.builder()
                            .optApplication(Application.NLP.TEXT_EMBEDDING)
                            .setTypes(String[].class, float[][].class)
                            .optModelUrls(modelUrl)
                            .optTranslator(new MyTranslator())
                            .optProgress(new ProgressBar())
                            .build();

            return ModelZoo.loadModel(criteria);
        }catch (Exception ignored){
            return null;
        }
    }


    public static float[][] predict(List<String> inputs)
            throws TranslateException {
        Predictor<String[], float[][]> predictor = predictorHolder.get();
        if (predictor == null) {
            predictor = model.newPredictor();
            predictorHolder.set(predictor);
        }
        return predictor.predict(inputs.toArray(new String[0]));
    }

    private static final class MyTranslator implements Translator<String[], float[][]> {

        MyTranslator() {}

        @Override
        public NDList processInput(TranslatorContext ctx, String[] inputs) {
            // manually stack for faster batch inference
            NDManager manager = ctx.getNDManager();
            NDList inputsList =
                    new NDList(
                            Arrays.stream(inputs)
                                    .map(manager::create)
                                    .collect(Collectors.toList()));
            return new NDList(NDArrays.stack(inputsList));
        }

        @Override
        public float[][] processOutput(TranslatorContext ctx, NDList list) {
            NDList result = new NDList();
            long numOutputs = list.singletonOrThrow().getShape().get(0);
            for (int i = 0; i < numOutputs; i++) {
                result.add(list.singletonOrThrow().get(i));
            }
            return result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
        }

        @Override
        public Batchifier getBatchifier() {
            return null;
        }
    }
}
