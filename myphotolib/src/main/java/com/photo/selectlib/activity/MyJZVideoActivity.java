package com.photo.selectlib.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.photo.selectlib.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class MyJZVideoActivity extends AppCompatActivity {

    private JCVideoPlayerStandard jzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jzvideo);

        initview();
        initdata();
    }

    //加载视频地址
    private void initdata() {
        String video_url = getIntent().getStringExtra("video_url");
        //播放器
        jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                ,jzvdStd.SCREEN_LAYOUT_NORMAL
                ,"");
    }

    //初始化控件
    private void initview() {
        jzvdStd = findViewById(R.id.jz_video);
    }

    @Override
    public void onBackPressed() {
        if(JCVideoPlayerStandard.backPress()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
    }
}
