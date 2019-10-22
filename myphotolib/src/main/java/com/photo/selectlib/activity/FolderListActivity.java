package com.photo.selectlib.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.selectlib.R;
import com.photo.selectlib.adapter.ImageFolderAdapter;
import com.photo.selectlib.bean.ImageFolderBean;
import com.photo.selectlib.core.ImageSelectObservable;
import com.photo.selectlib.listener.OnRecyclerViewClickListener;
import com.photo.selectlib.utils.ImageUtils;
import com.photo.selectlib.utils.TitleView;
import com.photo.selectlib.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 本地图片浏览 list列表
 */
public class FolderListActivity extends Activity implements Callback, OnRecyclerViewClickListener, View.OnClickListener {

    private static boolean isRound = false;
    private final int MREQUEST_CODE = 1000;
    // 图片临时保存路径
    private static String mTemporaryPath;

    public static void startFolderListActivity(Activity context, int REQUEST_CODE, ArrayList<ImageFolderBean> photos, int sMaxPicNum) {
        isRound = false;
        Intent addPhoto = new Intent(context, FolderListActivity.class);
        addPhoto.putExtra("list", photos);
        addPhoto.putExtra("max_num", sMaxPicNum);
        context.startActivityForResult(addPhoto, REQUEST_CODE);
    }

    public static void startSelectSingleImgActivity(Activity context, int REQUEST_CODE,boolean isTailor) {
        isRound = isTailor;
        if(isTailor){
            mTemporaryPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                    + File.separator + System.currentTimeMillis() + "photo.jpg";
        }
        Intent addPhoto = new Intent(context, FolderListActivity.class);
        addPhoto.putExtra("single", true);
        context.startActivityForResult(addPhoto, REQUEST_CODE);
    }

    /**
     * 图片所在文件夹适配器
     */
    private ImageFolderAdapter mFloderAdapter;
    /**
     * 图片列表
     */
    ArrayList<ImageFolderBean> mImageFolderList;
    /**
     * 消息处理
     */
    private Handler mHandler;

    private final int MSG_PHOTO = 10;

    private static final int DEFAULT_MAX_PIC_NUM = 9;
    /**
     * 可选择图片总数
     */
    public static int sMaxPicNum = DEFAULT_MAX_PIC_NUM;

    private final int REQUEST_ADD_OK_CODE = 22;

    private RecyclerView mRecyclerView;

    /**
     * 是否选择单张图片
     */
    private boolean mIsSelectSingleImge = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_folder_main);
        mHandler = new Handler(this);
        mImageFolderList = new ArrayList<>();
        sMaxPicNum = getIntent().getIntExtra("max_num", DEFAULT_MAX_PIC_NUM);

        mIsSelectSingleImge = getIntent().getBooleanExtra("single", false);
        initView();

        ImageUtils.loadLocalFolderContainsImage(this, mHandler, MSG_PHOTO);
        ImageSelectObservable.getInstance().addSelectImagesAndClearBefore((ArrayList<ImageFolderBean>) getIntent().getSerializableExtra("list"));

        mFloderAdapter = new ImageFolderAdapter(this, mImageFolderList);
        mRecyclerView.setAdapter(mFloderAdapter);
        mFloderAdapter.setOnClickListener(this);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.lv_photo_folder);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        TitleView titleView = (TitleView) findViewById(R.id.tv_photo_title);
        titleView.getLeftBackImageTv().setOnClickListener(this);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_ADD_OK_CODE:
                        if(isRound){
                            List<ImageFolderBean> selectImages = ImageSelectObservable.getInstance().getSelectImages();
                            if (selectImages != null && selectImages.size() > 0) {
//                                cropPhoto(getMediaUriFromPath(this,selectImages.get(selectImages.size()-1).path));
                                gotoClipActivity(getMediaUriFromPath(this,selectImages.get(selectImages.size()-1).path));
                            } else {
                                ToastUtils.getInstance().showShort(this,getString(R.string.select_photo_error),false);
                            }
                        }else{
                            Intent intent = getIntent();
                            ArrayList<ImageFolderBean> list = new ArrayList<>();
                            list.addAll(ImageSelectObservable.getInstance().getSelectImages());
                            intent.putExtra("list", list);
                            setResult(RESULT_OK, intent);
                            this.finish();
                        }
                    break;

                case MREQUEST_CODE:
                    if(data != null){
                        Uri uri = data.getData();
                        String cropImagePath = getRealFilePathFromUri(this,uri);
                        File cropFile = new File(cropImagePath);
                        if (cropFile.exists()) {
                            Intent intent = getIntent();
                            ArrayList<ImageFolderBean> list = new ArrayList<>();
                            ImageSelectObservable.getInstance().getSelectImages().get(0).setPath(cropImagePath);
                            list.addAll(ImageSelectObservable.getInstance().getSelectImages());
                            intent.putExtra("list", list);
                            setResult(RESULT_OK, intent);
                            this.finish();
                        }
                    }
                    break;
                    default:
                        break;
            }
        }else{
            //单选模式设定list只存一张图片
            if (isRound){
                ImageSelectObservable.getInstance().clearSelectImgs();
            }
        }
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 480);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTemporaryPath)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, MREQUEST_CODE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_PHOTO:
                mImageFolderList.clear();
                mImageFolderList.addAll((Collection<? extends ImageFolderBean>) msg.obj);
                mFloderAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }


    @Override
    public void onItemClick(View view, int position) {
        File file = new File(mImageFolderList.get(position).path);
        ImageSelectActivity.startPhotoSelectGridActivity(this, file.getParentFile().getAbsolutePath(), mIsSelectSingleImge, sMaxPicNum, REQUEST_ADD_OK_CODE);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_left_image) {
            this.finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageSelectObservable.getInstance().clearSelectImgs();
        ImageSelectObservable.getInstance().clearFolderImages();
    }

    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if(cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;
    }

    /**
     250      * 打开截图的界面
     251      * @param uri
     252      */
     private void gotoClipActivity(Uri uri){
                 if(uri == null){
                         return;
                     }
                 Intent intent = new Intent(this,ClipImageActivity.class);
                 intent.putExtra("type",1);
                 intent.setData(uri);
                 startActivityForResult(intent,MREQUEST_CODE);
             }
}
