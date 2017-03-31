package com.yazhi1992.practice.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by zengyazhi on 17/3/31.
 */

public class Utils {
    /**
     * 根据drawble文件名得到bitmap
     *
     * @param context 用来获得Resources
     * @param width   想要获得的bitmap的宽
     * @param height  想要获得的bitmap的高
     * @param drawble drawble图片名称
     * @return
     */
    public static Bitmap getBitmap(Context context, int width, int height, int drawble) {
        // 得到图像
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawble);

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        float scaleX = (float) width / bWidth;
        float scaleY = (float) height / bHeight;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);

        return Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);
    }

}
