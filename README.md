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
	        implementation 'com.github.JulyJiangL:MyPhotoSelect:1.0.8'
	}

单选时，在需要的地方调用：

   /*单选，参数对应的是context, 回调*/
   
        FolderListActivity.startSelectSingleImgActivity(this, 2);

多选时：

/*参数对应context, 回调code, 传入的图片List, 可选的最大张数*/

        FolderListActivity.startFolderListActivity(this, 1, null, 9);

最后Activity的onActivityResult中接收返回的图片数据：

 	List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
	
有反应内存溢出的,在onDestory里需要把注入的观察者对象移除就Ok了
	
	ImageSelectObservable.getInstance().deleteObserver(this);
	
Toast使用方法：
		
		/**
	     * 传入文字，在中间显示
	     * @param context 上下文
	     * @param text 需要toast的文字
	     * @param isCenter 文字是否中间显示（手机屏幕正中间）
	     */
	     
	ToastUtils.getInstance().showShort(this,"hello Toast!",false);
	
删除 app 目录下 unity-classes.jar

在 app 目录下 UnityPlayerActivity onCreate方法进行初始化
		
	BaseActivity.getInstance(this);
		
重写 onActivityResult 方法调用 BaseActivity.onActivityResponse(requestCode,resultCode,data);进行回传
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseActivity.onActivityResponse(requestCode,resultCode,data);
    }
    
重写 onRequestPermissionsResult 方法，调用BaseActivity.onRequestPermissionsResponse(requestCode,permissions,grantResults);进行回调

	@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivity.onRequestPermissionsResponse(requestCode,permissions,grantResults);
    }
	
原文地址：https://github.com/JarekWang/photoselect
