package com.photo.selectlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photo.selectlib.activity.FolderListActivity;

/**
 * 读取相册信息工具类
 */
public class StorageUtil {

    public final static int STORAGE_PERMISSION = 101;
    private static StorageUtil mStorageUtil;

    public static StorageUtil getInstance(){
        if(mStorageUtil == null){
            synchronized (StorageUtil.class){
                if(mStorageUtil == null){
                    mStorageUtil = new StorageUtil();
                }
            }
        }
        return mStorageUtil;
    }

    public void getStoragePermission(Activity mActivity,int code) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            } else {
                //有权限直接执行,docode()不用做处理
                doCode(mActivity,code);

            }
        } else {
            //小于6.0，不用申请权限，直接执行
            doCode(mActivity,code);
        }
    }

    private void doCode(Activity mActivity,int code) {
        switch (code) {
            case 1:
                /*单选，参数对应的是context, 回调*/
                FolderListActivity.startSelectSingleImgActivity(mActivity, 2,false);
                break;

            case 2:
                /*多选，参数对应context, 回调code, 传入的图片List, 可选的最大张数*/
                FolderListActivity.startFolderListActivity(mActivity, 1, null, 9);
                break;

            default:
                break;
        }
    }

}
