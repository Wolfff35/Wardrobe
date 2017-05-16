package com.wolff.wardrobe.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by wolff on 19.04.2017.
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path,int destWidth,int destHeight){
        Log.e("PICTURE","width = "+destWidth+"; heigth = "+destHeight);
        // Чтение размеров изображения на диске
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Вычисление степени масштабирования
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        // Чтение данных и создание итогового изображения
        return BitmapFactory.decodeFile(path, options);
    }
}
