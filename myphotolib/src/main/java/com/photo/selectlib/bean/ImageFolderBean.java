package com.photo.selectlib.bean;

import java.io.Serializable;

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
 * 图片所在文件夹类
 */
public class ImageFolderBean implements Serializable {

    /**  */
    private static final long serialVersionUID = 6645873496414509455L;
    /** 文件夹下第一张图片路径 */
    public String path;
    /**缩略图*/
    public String thumbnailsPath;
    /** 总图片数 */
    public int pisNum = 0;
    /** 文件夹名 */
    public String fileName;

    /**当图片选择后，索引值*/
    public int selectPosition;

    public int _id;

    /**当前图片在列表中顺序*/
    public int position;

    /**
     * 是否为查看图片
     */
    public boolean isSelect;

    /**
     * 图片缩略图
     * @return
     */

    public String ImageThumbnail;

    public String getImageThumbnail() {
        return ImageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        ImageThumbnail = imageThumbnail;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @param path 下载地址
     * @param imagethumbnail 缩略图地址
     */
    public ImageFolderBean(String path,String imagethumbnail) {
        this.path = path;
        this.ImageThumbnail = imagethumbnail;
    }

    public ImageFolderBean() {
    }
}
