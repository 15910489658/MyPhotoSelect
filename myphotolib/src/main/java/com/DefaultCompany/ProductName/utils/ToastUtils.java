package com.DefaultCompany.ProductName.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * create 2019/10/18 jiang
 */
public class ToastUtils {

    private static Toast mToast;
    private static ToastUtils mToastUtils = null;

    private ToastUtils(){}

    /**
     * 单例模式，双重锁判断，防止多次实例
     * @return
     */
    public static ToastUtils getInstance(){
        if(mToastUtils == null){
            //双重所判断，只实例化一次，才启动同步模式，提高性能，防止异步操作时多次实例化
            synchronized (ToastUtils.class){
                if(mToastUtils == null){
                    mToastUtils = new ToastUtils();
                }
            }
        }
        return mToastUtils;
    }

    /**
     * 传入文字，在中间显示
     * @param context 上下文
     * @param text 需要toast的文字
     * @param isCenter 文字是否中间显示（手机屏幕正中间）
     */
    public void showShort(Context context,String text,boolean isCenter){
        if (mToast == null){
            //不存在Toast实例，实例化
            mToast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }else{
            //存在Toast实例
            mToast.setText(text);
        }
        if(isCenter){
            mToast.setGravity(Gravity.CENTER,0,0);
        }
        mToast.show();
    }

    /**
     * 传入文字，中间显示
     * @param context 上下文
     * @param text 需要toast的文字
     */
    public void showLong(Context context,String text,boolean isCenter){
        if(mToast == null){
            mToast = Toast.makeText(context,text,Toast.LENGTH_LONG);
        }else{
            mToast.setText(text);
        }
        if(isCenter){
            mToast.setGravity(Gravity.CENTER,0,0);   
        }
        mToast.show();
    }

    /**
     * 传入资源文件
     * @param context 上下文
     * @param resIId 资源文件
     */
    public void showResShort(Context context,int resIId,boolean isCenter){
        if(mToast == null){
            mToast = Toast.makeText(context,resIId,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(resIId);
        }
        if(isCenter){
            mToast.setGravity(Gravity.CENTER,0,0);   
        }
        mToast.show();
    }

    /**
     * 传入资源文件
     * @param context 上下文
     * @param resId 资源文件
     */
    public void showResLong(Context context,int resId,boolean isCenter){
        if(mToast == null){
            mToast = Toast.makeText(context,resId,Toast.LENGTH_LONG);
        }else{
            mToast.setText(resId);
        }
        if(isCenter){
            mToast.setGravity(Gravity.CENTER,0,0);   
        }
        mToast.show();
    }

    /**
     * 传入图片，带文字
     * @param context
     * @param text
     * @param resImg
     */
    public void showImgShort(Context context,String text,int resImg,boolean isCenter){
        if(mToast == null){
            mToast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(text);
        }
        //添加图片，这里没有设置图片和文字显示一行的操作
        LinearLayout linearLayout = (LinearLayout) mToast.getView();
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resImg);
        linearLayout.addView(imageView);
        mToast.show();
    }

    /**
     * 传入图片，带文字
     * @param context
     * @param text
     * @param resImg
     */
    public void showImgLong(Context context,String text,int resImg,boolean isCenter){
        if(mToast == null){
            mToast = Toast.makeText(context,text,Toast.LENGTH_LONG);
        }else{
            mToast.setText(text);
        }
        //添加图片，这里没有设置图片和文字显示一行的操作
        LinearLayout linearLayout = (LinearLayout) mToast.getView();
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resImg);
        linearLayout.addView(imageView);
        mToast.show();
    }
}
