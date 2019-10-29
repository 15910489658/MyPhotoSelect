package com.photo.select;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.photo.selectlib.activity.FolderListActivity;
import com.photo.selectlib.activity.MyJZVideoActivity;
import com.photo.selectlib.activity.PreviewImageActivity;
import com.photo.selectlib.bean.ImageFolderBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photo.selectlib.core.ImageSelectObservable;
import com.photo.selectlib.utils.DeviceIdUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int STORAGE_PERMISSION = 101;
    private final int REQUEST_PREVIEW_PHOTO = 10;
    private ImageView iv_headimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_headimg = findViewById(R.id.iv_headimg);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        String uniquePsuedoID = DeviceIdUtil.getUniquePsuedoID();
        Log.e("uniquePsuedoID",uniquePsuedoID);
    }

    //单选
    public void onSingleClick(View view) {
        getStoragePermission(1);
//        ToastUtils.getInstance().showShort(this,"hello Toast!",false);
    }

    //多选
    public void onDoubleClick(View view) {
        getStoragePermission(2);
    }

    //unity返回地址预览图片
    public void onTestImageClick(View view) {

        ImageSelectObservable.getInstance().clearSelectImgs();
        List<ImageFolderBean> list = new ArrayList<>();
        list.add(new ImageFolderBean("/storage/emulated/0/Download/browser/图片收藏/bc305bcee6d91a8e0e8246.jpg"));
        list.add(new ImageFolderBean("/storage/emulated/0/Download/browser/图片收藏/bef53244a078ab382e6750e867f252ca.jpg"));
        list.add(new ImageFolderBean("/storage/emulated/0/Download/browser/图片收藏/469f996ebe4e0c358f94a724eb64ea92.jpg"));
        list.add(new ImageFolderBean("/storage/emulated/0/Download/browser/图片收藏/fa0d4bd17ea85f7f2c3316e071140f3f.jpg"));
        list.add(new ImageFolderBean("/storage/emulated/0/Download/browser/图片收藏/0f6109ffcfce5ca26ec912e5ed55f4c4.jpg"));
        ImageSelectObservable.getInstance().addSelectImagesAndClearBefore(list);
        PreviewImageActivity.startPreviewActivity(this,true, REQUEST_PREVIEW_PHOTO);
    }

    //播放视频
    public void onVideoPlayerClick(View view) {
        Intent intent = new Intent(MainActivity.this, MyJZVideoActivity.class);
        startActivity(intent);
    }

    private void getStoragePermission(int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
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
                FolderListActivity.startFolderListActivity(this, 1, null, 5);
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
                    final List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
                    if (list == null) {
                        return;
                    }
                    final StringBuilder stringBuffer = new StringBuilder();
                    for (ImageFolderBean string : list) {
                        stringBuffer.append(string.path).append("\n");
                        Log.e("imageurl",string.path.trim());
                    }
                    Toast.makeText(MainActivity.this,stringBuffer.toString(),Toast.LENGTH_SHORT).show();

                    //图片展示
                    iv_headimg.setImageURI(Uri.fromFile(new File(stringBuffer.toString().trim())));
                    break;
            }
        }
    }
}
