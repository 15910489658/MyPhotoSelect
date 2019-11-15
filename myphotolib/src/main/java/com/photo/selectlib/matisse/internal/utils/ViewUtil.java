package com.photo.selectlib.matisse.internal.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.selectlib.R;

public class ViewUtil {

    public static Dialog loadingDialog;

    //退出等待悬浮
    public static void cancelLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @param flag    true可以点击别处或者返回键取消 false不可取消
     * @return
     */

    public static void createLoadingDialog(final Activity context, String msg, boolean flag) {

        if (context == null) return;

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        if (TextUtils.isEmpty(msg)) {
            tipTextView.setVisibility(View.GONE);

        }
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        if (loadingDialog != null && loadingDialog.isShowing()) return;


        if (context.getParent() != null)
            loadingDialog = new Dialog(context.getParent(), R.style.loading_dialog);// 创建自定义样式dialog
        else {
            loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        }

        //true可以点击别处或者返回键取消
        //false不可取消
        loadingDialog.setCancelable(flag);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局

        loadingDialog.setContentView(layout);

        if (!context.isFinishing())
            loadingDialog.show();
        //lp.alpha=1f;//（0.0-1.0）//透明度，黑暗度为lp.dimAmount=1.0f;
        //return loadingDialog;

    }

    public static void showToast(Context con, String content) {
        Toast.makeText(con, content, Toast.LENGTH_LONG).show();
    }
}
