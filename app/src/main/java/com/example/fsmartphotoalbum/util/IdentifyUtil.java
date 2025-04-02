package com.example.fsmartphotoalbum.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.example.fsmartphotoalbum.app.App;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.tensorflow.Classifier;
import com.example.fsmartphotoalbum.tensorflow.ImagePreprocessor;
import com.example.fsmartphotoalbum.tensorflow.TensorFlowImageClassifier;

import java.util.ArrayList;
import java.util.List;

public class IdentifyUtil {
    private static final int PREVIEW_IMAGE_WIDTH = 640;
    private static final int PREVIEW_IMAGE_HEIGHT = 480;
    private static final int TF_INPUT_IMAGE_WIDTH = 224;
    private static final int TF_INPUT_IMAGE_HEIGHT = 224;

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";
    private static final String MODEL_FILE = "file:///android_asset/retrained_graph_low.pb";
    private static final String LABEL_FILE = "file:///android_asset/retrained_labels_low.txt";
//    private static final String MODEL_FILE = "file:///android_asset/retrained_graph_0318.pb";
//    private static final String LABEL_FILE = "file:///android_asset/retrained_labels_0318.txt";


//    // nf 配置
//    public static final int INPUT_SIZE = 299;
//    public static final int IMAGE_MEAN = 128;
//    public static final float IMAGE_STD = 128;
//    public static final String INPUT_NAME = "Mul";
//    public static final String OUTPUT_NAME = "final_result";
//    public static final String MODEL_FILE = "file:///android_asset/stripped_graph_nf.pb";
//    public static final String LABEL_FILE = "file:///android_asset/retrained_labels_nf.txt";

    private static ImagePreprocessor mImagePreprocessor;

    private static Classifier classifier;

    private static Bitmap bitmap;
    private static Bitmap newBitmap;

    static {
        mImagePreprocessor = new ImagePreprocessor(TF_INPUT_IMAGE_WIDTH, TF_INPUT_IMAGE_HEIGHT);

        try {
            classifier = TensorFlowImageClassifier.create(
                    App.getContext().getAssets(),
                    MODEL_FILE,
                    LABEL_FILE,
                    INPUT_SIZE,
                    IMAGE_MEAN,
                    IMAGE_STD,
                    INPUT_NAME,
                    OUTPUT_NAME);
        } catch (final Exception e) {
            throw new RuntimeException("Error initializing TensorFlow!", e);
        }
    }

    public static List<Classifier.Recognition> identify(Photo photo) {
        List<Classifier.Recognition> res = new ArrayList<>();

        try {
            if (photo != null && !TextUtils.isEmpty(photo.getPath())) {
                bitmap = BitmapFactory.decodeFile(photo.getPath());

                newBitmap = mImagePreprocessor.preprocessImage(bitmap);

                res =  classifier.recognizeImage(newBitmap);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return res;
    }
}
