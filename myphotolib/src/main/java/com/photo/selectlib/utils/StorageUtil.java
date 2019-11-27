package com.photo.selectlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.photo.selectlib.BaseActivity;
import com.photo.selectlib.activity.FolderListActivity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.photo.selectlib.utils.LocationUtil.BAIDU_READ_PHONE_STATE;
import static com.photo.selectlib.utils.PermissionsUtil.MICROPHONE;

/**
 * 读取相册信息工具类
 */
public class StorageUtil {

    public final static int STORAGE_PERMISSION = 101;
    private static StorageUtil mStorageUtil;
    private static int mCode;
    private static int mMaxNumber = 1;

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

    public void getStoragePermission(Activity mActivity,int code,int maxNumber) {
        mCode = code;
        mMaxNumber = maxNumber;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(BaseActivity.mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(BaseActivity.mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            } else {
                //有权限直接执行,docode()不用做处理
                doCode(BaseActivity.mActivity,code,maxNumber);

            }
        } else {
            //小于6.0，不用申请权限，直接执行
            doCode(BaseActivity.mActivity,code,maxNumber);
        }
    }

    private static void doCode(Activity mActivity, int code,int maxNumber) {
        switch (code) {
            case 1:
                /*单选，参数对应的是context, 回调*/
                FolderListActivity.startSelectSingleImgActivity(BaseActivity.mActivity, 2,true);
//                Matisse.from(mActivity)
//                        .choose(MimeType.ofImage(), false)      // 展示所有类型文件（图片 视频 gif）
//                        .capture(false)                        // 可拍照
//                        .countable(true)                      // 记录文件选择顺序
//                        .captureStrategy(new CaptureStrategy(true, "cache path"))
//                        .maxSelectable(1)                     // 最多选择一张
//                        .isCrop(true)                         // 开启裁剪
//                        .cropOutPutX(400)                     // 设置裁剪后保存图片的宽高
//                        .cropOutPutY(400)                     // 设置裁剪后保存图片的宽高
//                        .cropStyle(CropImageView.Style.CIRCLE)   // 方形裁剪CIRCLE为圆形裁剪
//                        .isCropSaveRectangle(true)                  // 裁剪后保存方形（只对圆形裁剪有效）
//                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                        .thumbnailScale(0.8f)
//                        .imageEngine(new GlideEngine())
//                        .forResult(23);
                break;

            case 2:
                /*多选，参数对应context, 回调code, 传入的图片List, 可选的最大张数*/
                FolderListActivity.startFolderListActivity(BaseActivity.mActivity, 1, null, 9);
                break;

            case 3:
                FolderListActivity.startFolderListActivity(BaseActivity.mActivity, 1, null, maxNumber);
//                Matisse.from(mActivity)
//                        .choose(MimeType.ofImage(), false)      // 展示所有类型文件（图片 视频 gif）
//                        .capture(false)                        // 可拍照
//                        .countable(true)                      // 记录文件选择顺序
//                        .captureStrategy(new CaptureStrategy(true, "cache path"))
//                        .maxSelectable(maxNumber == 0?1:maxNumber)                     // 最多选择一张
//                        .isCrop(false)                         // 开启裁剪
////                        .cropOutPutX(400)                     // 设置裁剪后保存图片的宽高
////                        .cropOutPutY(400)                     // 设置裁剪后保存图片的宽高
////                        .cropStyle(CropImageView.Style.CIRCLE)   // 方形裁剪CIRCLE为圆形裁剪
////                        .isCropSaveRectangle(true)                  // 裁剪后保存方形（只对圆形裁剪有效）
//                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                        .thumbnailScale(0.8f)
//                        .imageEngine(new GlideEngine())
//                        .forResult(23);
                break;

            default:
                break;
        }
    }

    /**
     * GPS定位返回code
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public static void onRequestPermissionsResponse(Activity mActivity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
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
                    doCode(BaseActivity.mActivity,mCode,mMaxNumber);
                }else{
//                    Toast.makeText(BaseActivity.mActivity,"权限申请失败",Toast.LENGTH_SHORT).show();
                }
                break;

            case MICROPHONE:
                for (int i = 0; i < grantResults.length; i++) {
                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.mActivity, permissions[i])) {
                            ToastUtils.getInstance().showShort(BaseActivity.mActivity,"请授权麦克风权限",false);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", BaseActivity.mActivity.getPackageName(), null);
                            intent.setData(uri);
                            BaseActivity.mActivity.startActivity(intent);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

}
