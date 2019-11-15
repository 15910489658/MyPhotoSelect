package com.photo.selectlib.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

public class ClipboardUtils {

    private static ClipboardUtils mClipboardUtils;

    public static ClipboardUtils getInstance(){
        if(mClipboardUtils == null){
            synchronized (ClipboardUtils.class){
                if(mClipboardUtils == null){
                    mClipboardUtils = new ClipboardUtils();
                }
            }
        }
        return mClipboardUtils;
    }

    /**
     * 获取系统剪贴板内容
     */
    public String getClipContent(Activity mActivity) {
        ClipboardManager manager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * 实现文本复制功能
     *
     * @param content 复制的文本
     */
    public boolean copy(Activity mActivity,String content) {
        if (!TextUtils.isEmpty(content)) {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
            // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
            ClipData clipData = ClipData.newPlainText(null, content);
            // 把数据集设置（复制）到剪贴板
            cmb.setPrimaryClip(clipData);
        }

        return true;
    }



}
