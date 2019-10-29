package com.photo.selectlib.utils;

import com.unity3d.player.UnityPlayer;

public class UnitySendMessageUtil {

    private static UnitySendMessageUtil mUnitySendMessageUtil;
    private String s = "Main Camera";

    public static UnitySendMessageUtil getInstance(){
        if(mUnitySendMessageUtil == null){
            synchronized (UnitySendMessageUtil.class){
                if (mUnitySendMessageUtil == null){
                    mUnitySendMessageUtil = new UnitySendMessageUtil();
                }
            }
        }
        return mUnitySendMessageUtil;
    }

    /**
     * 回调给unity定位权限授权信息
     * @param content 要返回的信息
     */
    public void sendGPSPermission(String content){
        /**
         * 参数一：为游戏物体名（挂脚本的名称）
         * 参数二：为Android调用Unity方法（.cs 脚本方法名称）
         * 参数三：为Android要传递给Unity的数据
         */
        UnityPlayer.UnitySendMessage(s, "responseGPSPermissionCode", content);
    }

    /**
     * 回调给Unity选择相册Json
     * @param content 照片信息Json
     */
    public void senPhotoSelectResponse(String content){
        /**
         * 参数一：为游戏物体名（挂脚本的名称）
         * 参数二：为Android调用Unity方法（.cs 脚本方法名称）
         * 参数三：为Android要传递给Unity的数据
         */
        UnityPlayer.UnitySendMessage(s, "ResponsePhotoSelectCutPath", content);
    }

}
