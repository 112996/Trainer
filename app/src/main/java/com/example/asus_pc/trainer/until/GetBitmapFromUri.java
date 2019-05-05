package com.example.asus_pc.trainer.until;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * 通过Uri获取图片并进行压缩
 */
public class GetBitmapFromUri {
    private static GetBitmapFromUri mInstance;

    public static GetBitmapFromUri getInstance() {
        if (mInstance == null) {
            synchronized (GetBitmapFromUri.class) {
                if (mInstance == null) {
                    mInstance = new GetBitmapFromUri();
                }
            }
        }
        return mInstance;
    }


    public Bitmap getBitmapFormUri(Context context, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;

        //图片分辨率以480*800为标准
        float hh = 800f;  //高度800f
        float ww = 480f;  //宽度480f
        int be = 1;  //缩放比，=1表示不进行缩放
        if (originalWidth > originalHeight && originalWidth > ww) { //如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度大的话根据高度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return compressImage(bitmap); //质量压缩

    }


    //质量压缩法
    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //质量压缩法，100表示不压缩，将压缩后的数据存放到baos

        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {//循环判断压缩后的图片是否大于1024，大于的话继续压缩
            baos.reset();

            //参数分别为：图片格式；图片质量，最高为100，最低为0；用于保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
            if (options <= 0) break;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//将ByteArrayInputStream生成图片
        return bitmap;
    }

}





