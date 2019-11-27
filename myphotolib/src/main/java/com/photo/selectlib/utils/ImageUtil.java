package com.photo.selectlib.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.photo.selectlib.bean.ImageFolderBean;
import com.photo.selectlib.core.ImageSelectObservable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 *
 * 图片加载类
 */
public class ImageUtil {

    /**
     * <p>加载所有包含图片的文件夹</p>
     * @param context Activity
     * @param handler Handler 异步加载完成后通知
     * @param what Handler.what
     */
    public static void loadLocalFolderContainsImage(final Activity context, final Handler handler, final int what) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                ArrayList<ImageFolderBean> imageFolders = new ArrayList<>();
                ContentResolver contentResolver = context.getContentResolver();
                /*查询id、  缩略图、原图、文件夹ID、 文件夹名、 文件夹分类的图片总数*/
                String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "COUNT(1) AS count"};
                String selection = "0==0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;
                String sortOrder = MediaStore.Images.Media.DATE_MODIFIED;
                Cursor cursor = null;
                try {
                    cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, null, sortOrder);
                    if (cursor != null && cursor.moveToFirst()) {

                        int columnPath = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        int columnId = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                        int columnFileName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                        int columnCount = cursor.getColumnIndex("count");

                        do {
                            ImageFolderBean folderBean = new ImageFolderBean();
                            folderBean.path = cursor.getString(columnPath);
                            folderBean._id = cursor.getInt(columnId);
                            folderBean.pisNum = cursor.getInt(columnCount);

                            String bucketName = cursor.getString(columnFileName);
                            folderBean.fileName = bucketName;

                            if (!Environment.getExternalStorageDirectory().getPath().contains(bucketName)) {
                                imageFolders.add(0, folderBean);
                            }
                        } while (cursor.moveToNext());
                    }
                    Message msg = new Message();
                    msg.what = what;
                    msg.obj = imageFolders;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        }).start();
    }


    /**
     * 获取相册指定目录下的全部图片路径
     * @param c Context
     * @param folderPath 指定目录
     * @param handler Handler 异步加载完成后通知
     * @param what Handler.what
     */
    public static void queryGalleryPicture(final Context c, final String folderPath, final Handler handler, final int what) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                ArrayList<ImageFolderBean> list = new ArrayList<>();
                String[] columns = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};

                /*查询文件路径包含上面指定的文件夹路径的图片--这样才能保证查询到的文件属于当前文件夹下*/
                String whereclause = MediaStore.Images.ImageColumns.DATA + " like'" + folderPath + "/%'";
                Log.i("queryGalleryPicture", "galleryPath:" + folderPath);

                Cursor corsor = null;
                List<ImageFolderBean> selects = ImageSelectObservable.getInstance().getSelectImages();

                try {
                    corsor = c.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, whereclause, null,
                            null);
                    if (corsor != null && corsor.getCount() > 0 && corsor.moveToFirst()) {
                        do {
                            String path = corsor.getString(corsor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                            int id = corsor.getInt(corsor.getColumnIndex(MediaStore.Images.ImageColumns._ID));

                            ImageFolderBean photoItem = new ImageFolderBean();
                            photoItem.path = path;
                            photoItem._id = id;

                            /**遍历查询之前选择的图片是否在其中*/
                            for (int index = 0, len = selects.size(); index < len; index ++) {
                                if (selects.get(index).path.equals(photoItem.path)) {
                                    photoItem.selectPosition = selects.get(index).selectPosition;
                                    selects.remove(index);
                                    selects.add(photoItem);
                                    break;
                                }
                            }

                            list.add(0, photoItem);
                        } while (corsor.moveToNext());
                    }

                    Message msg = new Message();
                    msg.what = what;
                    msg.obj = list;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (corsor != null)
                        corsor.close();
                }
            }
        }).start();
    }

    /*
     * 保存文件，文件名为当前日期
     */
    public static boolean saveBitmap(Activity context, Bitmap bitmap, String bitName){
        String fileName ;
        File file ;
        if(Build.BRAND .equals("Xiaomi") ){ // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName ;
        }else{  // Meizu 、Oppo
            Log.v("qwe","002");
            fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+bitName ;
        }
        file = new File(fileName);

        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
            {
                out.flush();
                out.close();
                // 插入图库
//                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

        return true;
    }

}
