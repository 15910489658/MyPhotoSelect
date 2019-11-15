package com.photo.selectlib.bean;

import java.util.List;

public class UnityPreviewBean {


    private List<PreviewDataBean> data;
    private int Position;

    public class PreviewDataBean {
        private String ImageUrl;//图片Url地址
        private String ImagePath;//图片本地地址（缩略图）

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            ImageUrl = imageUrl;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String imagePath) {
            ImagePath = imagePath;
        }
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public List<PreviewDataBean> getData() {
        return data;
    }

    public void setData(List<PreviewDataBean> list) {
        this.data = list;
    }
}
