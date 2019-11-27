package com.photo.selectlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jaeger.library.StatusBarUtil;
import com.photo.selectlib.R;
import com.photo.selectlib.R2;
import com.photo.selectlib.adapter.ImageGridApter;
import com.photo.selectlib.bean.ImageFolderBean;
import com.photo.selectlib.core.ImageSelectObservable;
import com.photo.selectlib.listener.OnRecyclerViewClickListener;
import com.photo.selectlib.utils.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 *
 * 系统相册选择
 */
public class ImageSelectActivity extends BaseExtendActivity implements Callback, OnRecyclerViewClickListener, Observer {

    @BindView(R2.id.iv_select_back)
    ImageView iv_select_back;
    @BindView(R2.id.tv_select_finish)
    TextView tv_select_finish;
    @BindView(R2.id.rb_original_image)
    CheckBox rb_original_image;
    @BindView(R2.id.ll_photo_operation)
    RelativeLayout ll_photo_operation;
    @BindView(R2.id.lv_photo_folder)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_photo_ok)
    TextView mOkTv;
    @BindView(R2.id.tv_photo_scan)
    TextView tv_photo_scan;


    public static void startPhotoSelectGridActivity(Activity activity, String folder, boolean singleSelect, int maxCount, int requestCode) {
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra("data", folder);
        intent.putExtra("single", singleSelect);
        intent.putExtra("maxCount", maxCount);
        activity.startActivityForResult(intent, requestCode);
    }

    private final int REQUEST_PREVIEW_PHOTO = 10;

    /**
     * 返回消息what
     */
    private final int MSG_PHOTO = 11;

    /**
     * 图片选择适配器
     */
    private ImageGridApter mAdapter;

    private Handler mHandler;

    private boolean mIsSelectSingleImge;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.photo_gridview_main;
    }

    @Override
    protected void initTool() {
        StatusBarUtil.setColor(ImageSelectActivity.this, getResources().getColor(R.color.album_finish));
    }

    @Override
    protected void initData() {
        rb_original_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rb_original_image.setChecked(isChecked);
            }
        });

        ll_photo_operation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        /*这里直接设置表格布局，三列*/
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new ImageGridApter(this, ImageSelectObservable.getInstance().getFolderAllImages(), mIsSelectSingleImge, getIntent().getIntExtra("maxCount", 1));
        mAdapter.setOnClickListener(this);
        recyclerView.setAdapter(mAdapter);

        mOkTv.setText(String.format(getResources().getString(R.string.photo_ok), mAdapter.getSelectlist().size()));
        tv_select_finish.setText(String.format(getResources().getString(R.string.photo_ok_finish), mAdapter.getSelectlist().size())+ "/"+getIntent().getIntExtra("maxCount", 1)+")");

        ImageSelectObservable.getInstance().addObserver(this);
        mHandler = new Handler(this);
        mIsSelectSingleImge = getIntent().getBooleanExtra("single", false);
        ImageUtil.queryGalleryPicture(this, getIntent().getStringExtra("data"), mHandler, MSG_PHOTO);
        if (mIsSelectSingleImge) {
            ll_photo_operation.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageSelectObservable.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    protected boolean initButterKnife() {
        return true;
    }

    @OnClick({R2.id.tv_photo_scan, R2.id.tv_photo_ok, R2.id.iv_left_image, R2.id.tv_select_finish, R2.id.iv_select_back})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_photo_scan) {
            if (mAdapter.getSelectlist().size() <= 0) {
                Toast.makeText(this, R.string.photo_no_select, Toast.LENGTH_SHORT).show();
            } else {
                PreviewImageActivity.startPreviewActivity(this, false ,false,REQUEST_PREVIEW_PHOTO);
            }
        } else if (id == R.id.tv_photo_ok) {
            setResult(RESULT_OK);
            this.finish();
        } else if (id == R.id.iv_left_image) {
            this.finish();
        }else if(id == R.id.tv_select_finish){
            setResult(RESULT_OK);
            this.finish();
        }else if(id == R.id.iv_select_back){
            this.finish();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PREVIEW_PHOTO) {
                mAdapter.notifyDataSetChanged();
                mOkTv.setText(getResources().getString(R.string.photo_ok, mAdapter.getSelectlist().size()));
                tv_select_finish.setText(getResources().getString(R.string.photo_ok_finish, mAdapter.getSelectlist().size())+ "/"+getIntent().getIntExtra("maxCount", 1)+")");
                mOkTv.setBackgroundResource(mAdapter.getSelectlist().size() > 0?R.drawable.shape_light_red_bg:R.drawable.shape_light_nomal_bg);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_PHOTO:
                ImageSelectObservable.getInstance().addFolderImagesAndClearBefore((Collection<? extends ImageFolderBean>) msg.obj);
                mAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }


    @Override
    public void onItemClick(View v, int position) {
        if (mIsSelectSingleImge) {
            setResult(RESULT_OK);
            this.finish();
            return;
        }

        if (position >= 0) {
            PreviewImageActivity.startPreviewPhotoActivityForResult(this, position, false ,false, REQUEST_PREVIEW_PHOTO,getIntent().getIntExtra("maxCount", 1));
        }
        mOkTv.setText(getResources().getString(R.string.photo_ok, mAdapter.getSelectlist().size()));
        tv_select_finish.setText(getResources().getString(R.string.photo_ok_finish, mAdapter.getSelectlist().size())+ "/"+getIntent().getIntExtra("maxCount", 1)+")");
        mOkTv.setBackgroundResource(mAdapter.getSelectlist().size() > 0?R.drawable.shape_light_red_bg:R.drawable.shape_light_nomal_bg);

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void update(Observable o, Object arg) {
        mAdapter.notifyDataSetChanged();
        mOkTv.setText(getResources().getString(R.string.photo_ok, mAdapter.getSelectlist().size()));
        tv_select_finish.setText(getResources().getString(R.string.photo_ok_finish, mAdapter.getSelectlist().size())+ "/"+getIntent().getIntExtra("maxCount", 1)+")");
        mOkTv.setBackgroundResource(mAdapter.getSelectlist().size() > 0?R.drawable.shape_light_red_bg:R.drawable.shape_light_nomal_bg);
    }
}
