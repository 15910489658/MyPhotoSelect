package com.photo.select;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.photo.selectlib.activity.FolderListActivity;
import com.photo.selectlib.activity.GSYVideoActivity;
import com.photo.selectlib.matisse.Matisse;
import com.photo.selectlib.utils.DeviceIdUtil;
import com.photo.selectlib.utils.ToastUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int STORAGE_PERMISSION = 101;
    private final int REQUEST_PREVIEW_PHOTO = 10;
    private ImageView iv_headimg;
    private static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_headimg = findViewById(R.id.iv_headimg);

        String uniquePsuedoID = DeviceIdUtil.getUniquePsuedoID();
        Log.e("uniquePsuedoID",uniquePsuedoID);
    }

    //单选
    public void onSingleClick(View view) {
    }

    //多选
    public void onDoubleClick(View view) {
        getStoragePermission(2);
    }

    //unity返回地址预览图片
    public void onTestImageClick(View view) {

    }

    //播放视频
    public void onVideoPlayerClick(View view) {
        Intent intent = new Intent(MainActivity.this, GSYVideoActivity.class);
        intent.putExtra("video_url","http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
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
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                case 2:
//                    final List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
//                    if (list == null) {
//                        return;
//                    }
//                    final StringBuilder stringBuffer = new StringBuilder();
//                    for (ImageFolderBean string : list) {
//                        stringBuffer.append(string.path).append("\n");
//                        Log.e("imageurl",string.path.trim());
//                    }
//                    Toast.makeText(MainActivity.this,stringBuffer.toString(),Toast.LENGTH_SHORT).show();
//
//                    //图片展示
//                    iv_headimg.setImageURI(Uri.fromFile(new File(stringBuffer.toString().trim())));
//                    break;
//            }

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> list = Matisse.obtainPathResult(data);
            if (list == null && list.size() > 0) {
                return;
            }
            final StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                stringBuffer.append(list.get(i)).append("\n");
                Log.e("imageurl",list.get(i).trim());
            }
            ToastUtils.getInstance().showLong(this,stringBuffer.toString(),false);
        }
        }

    public void onVideoForAlbumClick(View view) {
    }
}
