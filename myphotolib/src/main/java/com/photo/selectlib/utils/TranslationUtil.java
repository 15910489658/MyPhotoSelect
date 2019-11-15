package com.photo.selectlib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 常用转译工具类
 */
public class TranslationUtil {

    private static TranslationUtil mTranslationUtil;

    public static TranslationUtil getInstance(){
        if(mTranslationUtil == null){
            synchronized (TranslationUtil.class){
                if(mTranslationUtil == null){
                    mTranslationUtil = new TranslationUtil();
                }
            }
        }

        return mTranslationUtil;
    }

    /**
     * 图片编码成字符串
     * @param bitmap
     * @return
     */
    public String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组

//        return Base64.encodeToString(appicon, Base64.DEFAULT);
        return Base64.encodeToString(appicon, Base64.NO_WRAP);
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null; try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
//            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmapArray = Base64.decode(st, Base64.NO_WRAP);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e) {
            return null;
        }
    }

    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if (start!=-1 && end!=-1) {
//            return pathandname.substring(start+1, end);//无后缀
            return pathandname.substring(start+1);//有后缀 例如.png
        }
        else {
            return null;
        }
    }
}
