package com.DefaultCompany.ProductName.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.DefaultCompany.ProductName.bean.ImageFolderBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class BaseActivity extends Activity {

    private final int STORAGE_PERMISSION = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void openPhotoSingleSelect(){
        getStoragePermission(2);
    }

    private void getStoragePermission(int code) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            } else {
                //有权限直接执行,docode()不用做处理
                doCode(code);

            }
        } else {
            //小于6.0，不用申请权限，直接执行
            doCode(code);
        }
    }

    private void doCode(int code) {
        switch (code) {
            case 1:
                /*单选，参数对应的是context, 回调*/
                FolderListActivity.startSelectSingleImgActivity(this, 2,false);
                break;

            case 2:
                /*多选，参数对应context, 回调code, 传入的图片List, 可选的最大张数*/
                FolderListActivity.startFolderListActivity(this, 1, null, 9);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                case 2:
                    List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    if (list == null) {
                        return;
                    }
                    StringBuilder stringBuffer = new StringBuilder();
                    for (ImageFolderBean string : list) {
                        stringBuffer.append(string.path).append("\n");
                    }
                    Toast.makeText(this,stringBuffer.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
