package com.photo.selectlib.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SaveBitmapToBytesUtil {

    private static SaveBitmapToBytesUtil mSaveBitmapToBytesUtil;

    public static SaveBitmapToBytesUtil getInstance(){
        if(mSaveBitmapToBytesUtil == null){
            synchronized (SaveBitmapToBytesUtil.class){
                if(mSaveBitmapToBytesUtil == null){
                    mSaveBitmapToBytesUtil = new SaveBitmapToBytesUtil();
                }
            }
        }
        return mSaveBitmapToBytesUtil;
    }

    private byte[] bitmapToBytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    public String savePhotoToSDCardByte(String oldPath,final Bitmap photoBitmap, final String photoName){

        final String evPath = Environment.getExternalStorageDirectory()+ "/lxt";
        File file = new File(evPath);
            if (!file.exists()){
                //创建文件夹
                file.mkdirs();
            }
            if(photoBitmap !=null) {
                File file1 = new File(evPath+"/"+photoName);
                if (file1.exists()) {
                    return evPath+"/"+photoName;
                } else {
                    File file2 = new File(oldPath);
                    try {
                        FileUtils.copyFile(file2,file1);
//                        FileUtils.writeByteArrayToFile(file1,FileUtils.readFileToByteArray(file2));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            byte[] byteArray = bitmapToBytes(photoBitmap);
//                            File photoFile = new File(evPath, photoName);
//                            FileOutputStream fileOutputStream = null;
//                            BufferedOutputStream bStream = null;
//                            try {
//                                fileOutputStream = new FileOutputStream(photoFile);
//                                bStream = new BufferedOutputStream(fileOutputStream);
//                                bStream.write(byteArray);
//                            } catch (FileNotFoundException e) {
//                                photoFile.delete();
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                photoFile.delete();
//                                e.printStackTrace();
//                            } finally {
//                                try {
//                                    bStream.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }).start();
                }
            }
        File file2 = new File(evPath+"/"+photoName);
            if(file2.exists()){
                return evPath+"/"+photoName;
            }else{
                return "";
            }
    }
}
