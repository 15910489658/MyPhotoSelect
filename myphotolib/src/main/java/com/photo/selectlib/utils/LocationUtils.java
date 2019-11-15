package com.photo.selectlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 权限工具类
 */
public class LocationUtils {

    public static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    public static final int PRIVATE_CODE = 1315;//开启GPS权限

    public static void getGPSPermission(Activity activity){
        LocationManager lm = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions(activity, LOCATIONGPS,
                            BAIDU_READ_PHONE_STATE);
                } else {
                    //有权限
                    UnitySendMessageUtil.getInstance().sendGPSPermission("200");
                }
            } else {
                //有权限
                UnitySendMessageUtil.getInstance().sendGPSPermission("200");
            }
        } else {
            Toast.makeText(activity, "系统检测到未开启GPS定位服务,请手动开启", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(intent, PRIVATE_CODE);
//            activity.startActivity(intent);
        }
    }

    private static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};


}
