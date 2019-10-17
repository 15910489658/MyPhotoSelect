package com.DefaultCompany.ProductName.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.DefaultCompany.ProductName.R;
import com.DefaultCompany.ProductName.adapter.ImageFolderAdapter;
import com.DefaultCompany.ProductName.bean.ImageFolderBean;
import com.DefaultCompany.ProductName.core.ImageSelectObservable;
import com.DefaultCompany.ProductName.listener.OnRecyclerViewClickListener;
import com.DefaultCompany.ProductName.utils.ImageUtils;
import com.DefaultCompany.ProductName.utils.TitleView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 本地图片浏览 list列表
 */
public class FolderListActivity extends Activity implements Callback, OnRecyclerViewClickListener, View.OnClickListener {

    public static void startFolderListActivity(Activity context, int REQUEST_CODE, ArrayList<ImageFolderBean> photos, int sMaxPicNum) {
        Intent addPhoto = new Intent(context, FolderListActivity.class);
        addPhoto.putExtra("list", photos);
        addPhoto.putExtra("max_num", sMaxPicNum);
        context.startActivityForResult(addPhoto, REQUEST_CODE);
    }

    public static void startSelectSingleImgActivity(Activity context, int REQUEST_CODE) {
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
            if (requestCode == REQUEST_ADD_OK_CODE) {
                Intent intent = getIntent();
                ArrayList<ImageFolderBean> list = new ArrayList<>();
                list.addAll(ImageSelectObservable.getInstance().getSelectImages());
                intent.putExtra("list", list);
                setResult(RESULT_OK, intent);
                this.finish();
            }
        }
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
}
