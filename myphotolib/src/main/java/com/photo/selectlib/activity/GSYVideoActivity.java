package com.photo.selectlib.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.photo.selectlib.R;
import com.photo.selectlib.R2;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;

public class GSYVideoActivity extends BaseExtendActivity {

    @BindView(R2.id.jz_video)
    StandardGSYVideoPlayer jzvdStd;
    private OrientationUtils orientationUtils;

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_my_gsyvideo;
    }

    @Override
    protected void initTool() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void initData() {
        String video_url = getIntent().getStringExtra("video_url");
        //播放器
        jzvdStd.setUp(video_url
                , true
                , "");

        //设置返回键
        jzvdStd.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, jzvdStd);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        jzvdStd.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        jzvdStd.setIsTouchWiget(true);
        //设置返回按键功能
        jzvdStd.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        jzvdStd.startPlayLogic();
    }

    @Override
    protected boolean initButterKnife() {
        return true;
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            jzvdStd.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        jzvdStd.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jzvdStd.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        jzvdStd.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }
}
