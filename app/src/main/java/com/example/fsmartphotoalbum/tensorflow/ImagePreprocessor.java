package com.example.fsmartphotoalbum.tensorflow;
/*
 * Copyright 2017 The Android Things Samples Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Class that process an Image and extracts a Bitmap in a format appropriate for
 * the TensorFlow model.
 */
public class ImagePreprocessor {

    private Bitmap croppedBitmap;

    public ImagePreprocessor(int croppedwidth, int croppedHeight) {
        this.croppedBitmap = Bitmap.createBitmap(croppedwidth, croppedHeight, Config.ARGB_8888);
    }

    public Bitmap preprocessImage(final Bitmap rgbFrameBitmap) {
        if (croppedBitmap != null) {
            cropAndRescaleBitmap(rgbFrameBitmap, croppedBitmap);
        }
        return croppedBitmap;
    }

    static void cropAndRescaleBitmap(final Bitmap src, final Bitmap dst) {
        if (src == null) {
            return;
        }
        final float minDim = Math.min(src.getWidth(), src.getHeight());

        final Matrix matrix = new Matrix();

        // We only want the center square out of the original rectangle.
        final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
        final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
        matrix.preTranslate(translateX, translateY);

        final float scaleFactor = dst.getHeight() / minDim;
        matrix.postScale(scaleFactor, scaleFactor);

        final Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(src, matrix, null);
    }
}
