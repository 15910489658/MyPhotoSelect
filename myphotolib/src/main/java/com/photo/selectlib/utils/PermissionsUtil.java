package com.photo.selectlib.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.photo.selectlib.BaseActivity;

public class PermissionsUtil {

    public static final int MICROPHONE = 201;
    private static PermissionsUtil mPermissionsUtil;

    public static PermissionsUtil getInstance(){
        if(mPermissionsUtil == null){
            synchronized (PermissionsUtil.class){
                if(mPermissionsUtil == null){
                    mPermissionsUtil = new PermissionsUtil();
                }
            }
        }
        return mPermissionsUtil;
    }

    /**
     * 申请麦克风权限
     */
    public void getMicrophone(){
        //判断Android版本是否大于6.0 如果是则需要设置动态权限
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(BaseActivity.mActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                //没有权限申请权限
                ActivityCompat.requestPermissions(BaseActivity.mActivity,new String[]{Manifest.permission.RECORD_AUDIO},MICROPHONE);
            }
        }
    }
}
