package com.photo.selectlib.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;

public abstract class BaseExtendActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //界面未加载之前调用的初始化窗口例如状态栏的显隐
        initWindows();
        //设置setContentView
        setContentView(getContentLayoutId());
        //是否初始化ButterKnife
        if(initButterKnife()){
            ButterKnife.bind(this);
        }
        //初始化一些第三方工具
        initTool();
        //初始化数据
        initData();

    }

    /**
     * 初始化窗口
     */
    protected abstract void initWindows();

    /**
     * 初始化的布局
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initTool();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 是否初始化ButterKnife
     */
    protected abstract boolean initButterKnife();

}
