package com.photo.selectlib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photo.selectlib.activity.GSYVideoActivity;
import com.photo.selectlib.activity.PreviewImageActivity;
import com.photo.selectlib.activity.PreviewImageUnityActivity;
import com.photo.selectlib.bean.ImageFolderBean;
import com.photo.selectlib.bean.UnityPreviewBean;
import com.photo.selectlib.core.ImageSelectObservable;
import com.photo.selectlib.matisse.Matisse;
import com.photo.selectlib.utils.ClipboardUtils;
import com.photo.selectlib.utils.DeviceIdUtil;
import com.photo.selectlib.utils.ImageUtils;
import com.photo.selectlib.utils.LocationUtils;
import com.photo.selectlib.utils.LogUtil;
import com.photo.selectlib.utils.SaveBitmapToBytesUtil;
import com.photo.selectlib.utils.StorageUtil;
import com.photo.selectlib.utils.ToastUtils;
import com.photo.selectlib.utils.TranslationUtil;
import com.photo.selectlib.utils.UnitySendMessageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity {

    public static Activity mActivity;
    private static String request_className ;

    public static void getInstance(Activity activity){
        mActivity = activity;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 单选
     */
    public void OpenPhotoSingleSelectCut(String type_className){
        request_className = type_className;
        LogUtil.e("photoClass",request_className);
        StorageUtil.getInstance().getStoragePermission(mActivity,1,1);
    }

    /**
     * 调用相册多选图片
     * @param maxNumber 最大选择数量
     */
    public void OpenPhotoSelect(int maxNumber,String type_className){
        request_className = type_className;
        LogUtil.e("photoClass",request_className);
        StorageUtil.getInstance().getStoragePermission(mActivity,3,maxNumber);
    }

    /**
     * 获取系统粘贴板内容
     * @return
     */
    public String GetClipboardContent(){
        return ClipboardUtils.getInstance().getClipContent(mActivity);
    }

    /**
     * 拷贝文本到系统粘贴板
     * @param content 需要复制的内容
     * @return 是否执行完毕
     */
    public boolean CopyContent(String content){
        return ClipboardUtils.getInstance().copy(mActivity,content);
    }

    //定位权限
    public void OpenGPSPermission(){
        LocationUtils.getGPSPermission(mActivity);
    }

    /**
     * 保存图片到系统相册
     * @return
     */
    public boolean DownloadImage (String path){

        /**
         * 参数一：当前上下文
         * 参数二：图片Bitmap
         * 参数三：存储图片名称
         */
        return ImageUtils.saveBitmap(mActivity,BitmapFactory.decodeFile(path.trim()),TranslationUtil.getInstance().getFileName(path.trim()));
    }

    /**
     * 获取唯一的谁别ID 即使应用卸载ID也是唯一的，用于快速登录
     * @return
     */
    public String GetDeviceId(){

        return DeviceIdUtil.getUniquePsuedoID();
    }

    /**
     * Unity传递图片给Android由Android来进行预览
     * @param JsonObject 需要解析的Json串
     */
    public void OpenPreviewImage(String JsonObject){
        LogUtil.e("UnityJsonObject",JsonObject);
        //解析Json串
        if(TextUtils.isEmpty(JsonObject)){
            ToastUtils.getInstance().showShort(mActivity,getString(R.string.preview_image_failure),false);
        }else{
            /**
             * 参数一：要解析的Json字符串
             * 参数二：解析成为的对象 例 User.class
             */
            UnityPreviewBean unityPreviewBean = new Gson().fromJson(JsonObject, UnityPreviewBean.class);
            List<UnityPreviewBean.PreviewDataBean> previewbean = unityPreviewBean.getData();
            ImageSelectObservable.getInstance().clearSelectImgs();
            List<ImageFolderBean> list = new ArrayList<>();
            if(previewbean != null && previewbean.size() > 0){
                for (int i = 0; i < previewbean.size(); i++) {
                    LogUtil.e("UnityImageUrl",previewbean.get(i).getImageUrl());
                    LogUtil.e("UnityImagePath",previewbean.get(i).getImagePath());
                    LogUtil.e("UnityFilePath",mActivity.getExternalFilesDir("").getAbsolutePath());

                    LogUtil.e("UnityFileCompletePath",mActivity.getExternalFilesDir("").getAbsolutePath()+"/"+previewbean.get(i).getImagePath());
                    list.add(new ImageFolderBean(previewbean.get(i).getImageUrl(),mActivity.getExternalFilesDir("").getAbsolutePath()+"/"+previewbean.get(i).getImagePath()));
                }
            }
            ImageSelectObservable.getInstance().addSelectImagesAndClearBefore(list);
            PreviewImageUnityActivity.startPreviewActivity(mActivity,unityPreviewBean.getPosition(),false,true, 10);
        }
    }

    /**
     * Unity传递视频URL地址由Android进行视频播放
     * @param url 视频地址
     */
    public void OpenPreviewVideo(String url){
        Intent intent = new Intent(mActivity, GSYVideoActivity.class);
        intent.putExtra("video_url",url);
        startActivity(intent);
    }



    //相册回调
    public static void onActivityResponse(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                case 2:
                    List<ImageFolderBean> list2 = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    if (list2 == null) {
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        StringBuilder stringBuffer = new StringBuilder();
                        for (ImageFolderBean string : list2) {
                            JSONObject jsonObject1 = new JSONObject();
                            stringBuffer.append(string.path).append("\n");
//                            String InCodeString = TranslationUtil.getInstance().convertIconToString(BitmapFactory.decodeFile(string.path.trim()));
//                            String path = SaveBitmapToBytesUtil.getInstance().savePhotoToSDCardByte(string.path.trim(),BitmapFactory.decodeFile(string.path.trim()), TranslationUtil.getInstance().getFileName(string.path.trim()));
                            jsonObject1.put("MemoryAddress",string.path.trim());
                            jsonObject1.put("InCodeString",string.path.trim());
                            int[] imageWidthHeight =  getImageWidthHeight(string.path);
                            if(imageWidthHeight != null && imageWidthHeight.length != 0){
                                float width = imageWidthHeight[0];
                                float height = imageWidthHeight[1];
                                jsonObject1.put("Width",width);
                                jsonObject1.put("Height",height);
                            }else{
                                jsonObject1.put("Width",0);
                                jsonObject1.put("Height",0);
                            }
                            jsonArray.put(jsonObject1);
                        }
                        jsonObject.put("type",request_className);
                        LogUtil.e("photoClass",request_className);
                        jsonObject.put("data",jsonArray);
                        LogUtil.e("ImageInCodeJsonObject:",jsonObject.toString());
                        UnitySendMessageUtil.getInstance().sendPhotoSelectResponse(jsonObject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 23:
                    List<String> list = Matisse.obtainPathResult(data);
                    if (list == null && list.size() > 0) {
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        StringBuilder stringBuffer = new StringBuilder();
                        for (String string : list) {
                            JSONObject jsonObject1 = new JSONObject();
                            stringBuffer.append(string).append("\n");
//                            String InCodeString = TranslationUtil.getInstance().convertIconToString(BitmapFactory.decodeFile(string.trim()));
//                            String path = SaveBitmapToBytesUtil.getInstance().savePhotoToSDCardByte(string.trim(),BitmapFactory.decodeFile(string.trim()), TranslationUtil.getInstance().getFileName(string.trim()));
//                          LogUtil.e("saveBitmapSize",BitmapFactory.decodeFile(path).getByteCount()+" saveBitmapPath:"+path);
                            jsonObject1.put("MemoryAddress",string.trim());
                            jsonObject1.put("InCodeString",string.trim());
                            int[] imageWidthHeight =  getImageWidthHeight(string);
                            if(imageWidthHeight != null && imageWidthHeight.length != 0){
                                float width = imageWidthHeight[0];
                                float height = imageWidthHeight[1];
                                jsonObject1.put("Width",width);
                                jsonObject1.put("Height",height);
                            }else{
                                jsonObject1.put("Width",0);
                                jsonObject1.put("Height",0);
                            }
                            jsonArray.put(jsonObject1);
                        }
                        jsonObject.put("type",request_className);
                        LogUtil.e("photoClass",request_className);
                        jsonObject.put("data",jsonArray);
                        LogUtil.e("ImageInCodeJsonObject:",jsonObject.toString());
                        UnitySendMessageUtil.getInstance().sendPhotoSelectResponse(jsonObject.toString());
//                        ViewUtil.cancelLoadingDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    //*************************************************************************************************************

    public static int[] getImageWidthHeight(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        LogUtil.e("outWidth",options.outWidth+"");
        LogUtil.e("outHeight",options.outHeight+"");
        return new int[]{options.outWidth,options.outHeight};
    }
}
