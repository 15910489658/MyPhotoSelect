# MyPhotoSelect
初始化仿QQ上传朋友圈相册选择Project

Step 1. Add the JitPack repository to your build file 


Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.JulyJiangL:MyPhotoSelect:1.0.0'
	}

单选时，在需要的地方调用：

   /*单选，参数对应的是context, 回调*/
   
        FolderListActivity.startSelectSingleImgActivity(this, 2);

多选时：

/*参数对应context, 回调code, 传入的图片List, 可选的最大张数*/

        FolderListActivity.startFolderListActivity(this, 1, null, 9);

最后Activity的onActivityResult中接收返回的图片数据：

 	List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
	
Toast使用方法：
	/**
     * 传入文字，在中间显示
     * @param context 上下文
     * @param text 需要toast的文字
     * @param isCenter 文字是否中间显示（手机屏幕正中间）
     */
	ToastUtils.getInstance().showShort(this,"hello Toast!",false);
	
原文地址：https://github.com/JarekWang/photoselect
