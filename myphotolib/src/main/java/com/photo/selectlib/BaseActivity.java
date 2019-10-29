package com.photo.selectlib;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.photo.selectlib.activity.FolderListActivity;
import com.photo.selectlib.bean.ImageFolderBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photo.selectlib.utils.LocationUtils;
import com.photo.selectlib.utils.StorageUtil;
import com.photo.selectlib.utils.TranslationUtil;
import com.photo.selectlib.utils.UnitySendMessageUtil;
import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.photo.selectlib.utils.LocationUtils.BAIDU_READ_PHONE_STATE;
import static com.photo.selectlib.utils.StorageUtil.STORAGE_PERMISSION;

public class BaseActivity extends Activity {

    private static Activity mActivity;

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
    public void openPhotoSingleSelect(){
        StorageUtil.getInstance().getStoragePermission(mActivity,1);
    }

    /**
     * 多选
     */
    public void openPhotoDoubleSelect(){
        StorageUtil.getInstance().getStoragePermission(mActivity,2);
    }

    //相册回调
    public static void onActivityResponse(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                case 2:
                    List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    if (list == null) {
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        StringBuilder stringBuffer = new StringBuilder();
                        for (ImageFolderBean string : list) {
                            JSONObject jsonObject1 = new JSONObject();
                            stringBuffer.append(string.path).append("\n");
                            String InCodeString = TranslationUtil.getInstance().convertIconToString(BitmapFactory.decodeFile(string.path.trim()));
//                            Log.e("inCodeString:",InCodeString);
                            jsonObject1.put("MemoryAddress",string.path.trim());
                            jsonObject1.put("InCodeString",InCodeString);
                            jsonArray.put(jsonObject1);
                        }
                        jsonObject.put("data",jsonArray);
//                        Log.e("ImageInCodeJsonObject",jsonObject.toString());
                        UnitySendMessageUtil.getInstance().senPhotoSelectResponse(jsonObject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    //*************************************************************************************************************
    //权限

    public void openGPSPermission(){
        LocationUtils.getGPSPermission(mActivity);
    }

    /**
     * GPS定位返回code
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public void onRequestPermissionsResponse(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                //如果用户取消，permissions可能为null.
                if (grantResults[0] == PERMISSION_GRANTED && grantResults.length > 0) { //有权限
                    UnitySendMessageUtil.getInstance().sendGPSPermission("200");
                } else {                                                                //无权限
                    UnitySendMessageUtil.getInstance().sendGPSPermission("404");
                }
                break;

            case STORAGE_PERMISSION:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //执行代码,这里是已经申请权限成功了,可以不用做处理

                }else{
                    Toast.makeText(mActivity,"权限申请失败",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


}
