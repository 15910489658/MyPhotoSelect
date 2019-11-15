package com.photo.selectlib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志显示工具类，通过控制isShowingLog的值控制所有日志是否输出
 *
 * Project Name:WaterDriver File Name:LogUtil.java Package
 * Name:com.mrwater.driver.utils; Date:2015-10-25下午14:12:59 Author:
 * chenrongshang Description： TODO
 *
 */
public class LogUtil {

    private static boolean isShowingLog = true;
    private static int RESERVED_LOG_LEN = 1024 * 1024;
    public static String LOG_FILE = "waterDriver/log.txt";//log路径

    public static void i(String tag, String msg) {
        if (isShowingLog) {

            int p = 2048;
            msg = formatContent(msg);
            long length = msg.length();
            if (length < p || length == p) {
                Log.i(tag, msg);
            } else {
                while (msg.length() > p) {
                    String logContent = msg.substring(0, p);
                    msg = msg.replace(logContent, "");
                    Log.i(tag, logContent);
                }
                Log.i(tag, msg);
            }

            String fileFullPath = getExternalStoragePath() + "/" + LOG_FILE;
            String logMsg = "[info] [tag = " + tag + "] " + msg + "\n";
            writeToFile(fileFullPath, logMsg);
        }
    }

    public static void d(String tag, String msg) {
        if (isShowingLog) {

            int p = 2048;
            msg = formatContent(msg);
            long length = msg.length();
            if (length < p || length == p) {
                Log.d(tag, msg);
            } else {
                while (msg.length() > p) {
                    String logContent = msg.substring(0, p);
                    msg = msg.replace(logContent, "");
                    Log.d(tag, logContent);
                }
                Log.d(tag, msg);
            }

            String fileFullPath = getExternalStoragePath() + "/" + LOG_FILE;
            String logMsg = "[debug] [tag = " + tag + "] " + msg + "\n";
            writeToFile(fileFullPath, logMsg);
        }
    }

    public static void e(String tag, String msg) {
        if (isShowingLog) {

            int p = 2048;
            msg = formatContent(msg);
            long length = msg.length();
            if (length < p || length == p) {
                Log.e(tag, msg);
            } else {
                while (msg.length() > p) {
                    String logContent = msg.substring(0, p);
                    msg = msg.replace(logContent, "");
                    Log.e(tag, logContent);
                }
                Log.e(tag, msg);
            }

            String fileFullPath = getExternalStoragePath() + "/" + LOG_FILE;
            String logMsg = "[error] [tag = " + tag + "] " + msg + "\n";
            writeToFile(fileFullPath, logMsg);
        }
    }


    public static String getExternalStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    private static void writeToFile(String fileFullNmae, String content) {
        synchronized (LogUtil.class) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                Log.d("TestFile", "sd卡不可用");
                return;
            }

            try {
                String[] pathNode = fileFullNmae.split("/");
                String dir = fileFullNmae.replace("/" + pathNode[pathNode.length - 1], "");
                File path = new File(dir);
                if (!path.exists()) {
                    path.mkdirs();
                }

                RandomAccessFile randomFile = new RandomAccessFile(fileFullNmae, "rw");
                long fileLength = randomFile.length();
                if (fileLength > RESERVED_LOG_LEN * 5) {
                    byte[] preData = new byte[RESERVED_LOG_LEN];
                    randomFile.seek(fileLength - RESERVED_LOG_LEN);
                    randomFile.read(preData);
                    randomFile.close();
                    File deleteFile = new File(fileFullNmae);
                    deleteFile.delete();
                    randomFile = new RandomAccessFile(fileFullNmae, "rw");
                    randomFile.seek(0);
                    String preDataStr = new String(preData);
                    randomFile.writeBytes(preDataStr);
                }
                fileLength = randomFile.length();
                randomFile.seek(fileLength);

                Date nowTime=new Date();
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms");
                String timeFromat = time.format(nowTime);
                content = "[" + timeFromat + "] " + content;
                randomFile.writeBytes(content);
                randomFile.close();

            } catch (Exception e) {
                Log.d("TestFile", "sd卡写入失败");
            }
        }
    }

    public static String getException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        String exceptionMessage = "";
        exceptionMessage = sw.toString();

        return exceptionMessage;
    }

    public static void autoSetDebugOrReleaseMode(Context context) {
        if (isApkDebugable(context)) {
            isShowingLog = true;
        } else {
            isShowingLog = false;
        }
    }
    private static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {

        }
        return false;
    }

    private static String formatContent(String content) {
        return String.format("%s %s", Thread.currentThread().getName(), content);
    }
}
