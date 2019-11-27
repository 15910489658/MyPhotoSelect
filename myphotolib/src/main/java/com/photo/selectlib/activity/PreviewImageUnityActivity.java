package com.photo.selectlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.jaeger.library.StatusBarUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.photo.selectlib.R;
import com.photo.selectlib.R2;
import com.photo.selectlib.adapter.ImagePreviewUnityAdapter;
import com.photo.selectlib.adapter.ImagePreviewUnityAdapter;
import com.photo.selectlib.bean.ImageFolderBean;
import com.photo.selectlib.core.ImageSelectObservable;
import com.photo.selectlib.listener.OnRecyclerViewClickListener;
import com.photo.selectlib.listener.RecyclerViewSmoothMoveToPositionUtil;
import com.photo.selectlib.listener.SpacesItemDecoration;
import com.photo.selectlib.utils.LogUtil;
import com.photo.selectlib.utils.ShowBottomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


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
 *
 * 预览图片
 */
public class PreviewImageUnityActivity extends BaseExtendActivity implements OnClickListener {
	/** 显示/隐藏 过程持续时间 */
	private static final int SHOW_HIDE_CONTROL_ANIMATION_TIME = 500;
	@BindView(R2.id.vp_preview)
	ViewPager mPhotoPager;
	/**标题栏*/
	@BindView(R2.id.rl_large_title)
	RelativeLayout mTitleView;
	/**选择按钮*/
	@BindView(R2.id.ctv_check)
	TextView mCheckedTv;
	@BindView(R2.id.rl_check)
	View mFooterView;
	@BindView(R2.id.tv_preview_select_finish)
	TextView tv_select_finish;
	@BindView(R2.id.iv_preview_select_back)
	ImageView iv_preview_select_back;
	@BindView(R2.id.rv_preview_photo)
	RecyclerView rv_preview_photo;
	@BindView(R2.id.tv_check)
	TextView tv_check;

	private static boolean isSelect = false;
	private static boolean isUnity = false;
	private int mPosition;

	/**控制显示、隐藏顶部标题栏*/
	private boolean isHeadViewShow = true;

	/**需要预览的所有图片*/
	private List<ImageFolderBean> mAllImage;
	/**x选择的所有图片*/
	private List<ImageFolderBean> mSelectImage;
	private ShowBottomDialog showBottomDialog;
	private int mMaxNum;
	private ImagePreviewUnityAdapter adapter;

	/**
	 * 预览文件夹下所有图片
	 * @param activity Activity
	 * @param mSelect 是否隐藏选中按钮 true 隐藏。，false 不隐藏
	 * @param position position 当前显示位置
	 * @param requestCode requestCode
	 */
	public static void startPreviewPhotoActivityForResult (Activity activity, int position, boolean mSelect, boolean mUnity, int requestCode,int mMaxNum) {
		isSelect = mSelect;
		isUnity = mUnity;
		Intent intent = new Intent(activity, PreviewImageUnityActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("mMaxNum", mMaxNum);
		activity.startActivityForResult(intent, requestCode);
		activity.overridePendingTransition(R.anim.common_scale_small_to_large, 0);
	}

	/**
	 * 预览选择的图片
	 * @param activity Activity
	 * @param mSelect 是否隐藏选中按钮 true 隐藏。，false 不隐藏
	 * @param mUnity 是否是从Unity直接调取的预览
	 * @param requestCode requestCode
	 */
	public static void startPreviewActivity (Activity activity, int position, boolean mSelect, boolean mUnity, int requestCode) {
		isSelect = mSelect;
		isUnity = mUnity;
		Intent intent = new Intent(activity, PreviewImageUnityActivity.class);
		intent.putExtra("preview", true);
		intent.putExtra("position", position);
		activity.startActivityForResult(intent, requestCode);
		activity.overridePendingTransition(R.anim.common_scale_small_to_large, 0);
	}

	@Override
	protected void initWindows() {

	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.preview_image_activity;
	}

	@Override
	protected void initTool() {
		StatusBarUtil.setColor(PreviewImageUnityActivity.this, getResources().getColor(R.color.album_finish));
	}

	@Override
	protected void initData() {
		initImages();
		initView();
		initAdapter();
	}

	@Override
	protected boolean initButterKnife() {
		return true;
	}

	/**
	 * 初始化图片数组
	 */
	private void initImages () {
		mAllImage = new ArrayList<>();
		mSelectImage = ImageSelectObservable.getInstance().getSelectImages();
		mPosition = getIntent().getIntExtra("position", 1);

		if(mSelectImage.size() > 0 && mSelectImage != null) {
			if (getIntent().getBooleanExtra("preview", false)) {
				mAllImage.addAll(ImageSelectObservable.getInstance().getSelectImages());
				mAllImage.get(0).setSelect(true);
				mPosition = 0;
			} else {
				mAllImage.addAll(ImageSelectObservable.getInstance().getFolderAllImages());
				mAllImage.get(mPosition).setSelect(true);
			}
		}
	}

	/**初始化控件*/
	private void initView() {
		/*标题栏*/
		tv_select_finish.setVisibility(View.GONE);
		String title = getIntent().getIntExtra("position", 1) + "/" + mAllImage.size();
		showBottomDialog = new ShowBottomDialog();
		tv_select_finish.setText(String.format(getResources().getString(R.string.photo_ok_finish), mSelectImage.size())+ "/"+getIntent().getIntExtra("mMaxNum", 1)+")");
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		rv_preview_photo.addItemDecoration(new SpacesItemDecoration(4));
		rv_preview_photo.setLayoutManager(linearLayoutManager);
		/*底部菜单栏*/
		mFooterView.setVisibility(View.GONE);
		mMaxNum = getIntent().getIntExtra("mMaxNum", 1);
		mCheckedTv.setEnabled(mSelectImage.contains(mAllImage.get(getIntent().getIntExtra("position", 0))));

		RecyclerViewSmoothMoveToPositionUtil.getInstance().smoothMoveToPosition(rv_preview_photo,mPosition);

	}

	/**
	 * 更新选择的顺序
	 */
	private void subSelectPosition () {
		for (int index = 0, len = mSelectImage.size(); index < len; index ++) {
			ImageFolderBean folderBean = mSelectImage.get(index);
			folderBean.selectPosition = index + 1;
		}
	}

	/**
	 * adapter的初始化
	 */
	private void initAdapter() {
		PreviewAdapter  previewAdapter = new PreviewAdapter(mAllImage);
		mPhotoPager.setAdapter(previewAdapter);
		mPhotoPager.setPageMargin(5);
		mPhotoPager.setCurrentItem(getIntent().getIntExtra("position", 0));

		adapter = new ImagePreviewUnityAdapter(this,mAllImage);
		rv_preview_photo.setAdapter(adapter);
		adapter.setOnClickListener(new OnRecyclerViewClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				mAllImage.get(mPosition).setSelect(false);
				mAllImage.get(position).setSelect(true);
				mPosition = position;
				adapter.notifyDataSetChanged();
				mPhotoPager.setCurrentItem(position);
			}

			@Override
			public void onItemLongClick(View view, int position) {

			}

		});

		mPhotoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				String text = (arg0 + 1) + "/" + mAllImage.size();
//				mTitleView.getTitleTv().setText(text);
				tv_select_finish.setText(getResources().getString(R.string.photo_ok_finish, mSelectImage.size())+ "/"+mMaxNum+")");
				mCheckedTv.setEnabled(mSelectImage.contains(mAllImage.get(arg0)));
				mAllImage.get(mPosition).setSelect(false);
				mAllImage.get(arg0).setSelect(true);
				mPosition = arg0;
				mAllImage.get(mPosition).setSelect(false);
				mAllImage.get(arg0).setSelect(true);
				mPosition = arg0;
				adapter.notifyDataSetChanged();
				RecyclerViewSmoothMoveToPositionUtil.getInstance().smoothMoveToPosition(rv_preview_photo,arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}


	/**
	 * 简单的适配器
	 */
	class PreviewAdapter extends PagerAdapter {
		private List<ImageFolderBean> photos;

		public PreviewAdapter(List<ImageFolderBean> photoList) {
			super();
			this.photos = photoList;
		}

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			LayoutInflater inflater = LayoutInflater.from(PreviewImageUnityActivity.this);
			View view = inflater.inflate(R.layout.preview_image_item, container, false);
			PhotoView bigPhotoIv = view.findViewById(R.id.iv_image_item);
			bigPhotoIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isHeadViewShow) {
						hideControls();
					} else {
						showControls();
					}
				}
			});

			bigPhotoIv.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(isUnity){
						int start = photos.get(position).path.lastIndexOf("/");
						String pathName = photos.get(position).path.substring(start + 1);
						showBottomDialog.BottomDialog(PreviewImageUnityActivity.this,photos.get(position).path,pathName);
					}
					return false;
				}
			});

			if(photos != null && photos.size() > 0){
				if(photos.get(position) != null){
					Drawable fromPath = null;
					if(photos.get(position).thumbnailsPath != null){
						fromPath = Drawable.createFromPath(photos.get(position).thumbnailsPath);
					}else{
						fromPath = getResources().getDrawable(R.drawable.defaultpic);
					}

//					Glide.with(PreviewImageUnityActivity.this).load(photos.get(position).path).error(fromPath).placeholder(fromPath).into(bigPhotoIv);
//					Glide.with(PreviewImageUnityActivity.this).load(new File(photos.get(position).path)).error(fromPath).placeholder(fromPath).into(bigPhotoIv);
					LogUtil.e("UnityFileCompletePath",position+"  "  +photos.get(position).ImageThumbnail);
					ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(photos.get(position).ImageThumbnail), bigPhotoIv);
				}
			}
			container.addView(view);
			return view;
		}

	}


	@OnClick({R2.id.tv_right_text,R2.id.iv_preview_select_back,R2.id.tv_preview_select_finish,
			R2.id.ctv_check,R2.id.tv_check,R2.id.iv_left_image})
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.tv_right_text || id == R.id.iv_preview_select_back || id == R.id.tv_preview_select_finish) {
			onBackPressed();
		} else if (id == R.id.ctv_check || id== R.id.tv_check) {
			addOrRemoveImage();
		} else if (id == R.id.iv_left_image) {
			onBackPressed();
		}
	}

	/**
	 * 添加或者删除当前操作的图片
	 */
	private void addOrRemoveImage () {
		ImageFolderBean imageBean = mAllImage.get(mPhotoPager.getCurrentItem());

		if (mSelectImage.contains(imageBean)) {
			mSelectImage.remove(imageBean);
			subSelectPosition();
			mCheckedTv.setEnabled(false);
		} else {
			if(mSelectImage.size() >= mMaxNum){
				Toast.makeText(this, this.getResources().getString(R.string.publish_select_photo), Toast.LENGTH_SHORT).show();
			}else{
				mSelectImage.add(imageBean);
				imageBean.selectPosition = mSelectImage.size();
				mCheckedTv.setEnabled(true);
			}
		}
		tv_select_finish.setText(getResources().getString(R.string.photo_ok_finish, mSelectImage.size())+ "/"+mMaxNum+")");
	}

	/**
	 * <br>显示顶部，底部view动画 </br>
	 */
	private void showControls() {
		AlphaAnimation animation = new AlphaAnimation(0f, 1f);
		animation.setFillAfter(true);
		animation.setDuration(SHOW_HIDE_CONTROL_ANIMATION_TIME);
		isHeadViewShow = true;

		mTitleView.startAnimation(animation);
		mTitleView.setVisibility(View.VISIBLE);
	}

	/**
	 * <br> 隐藏顶部，底部view 动画</br>
	 */
	private void hideControls() {
		AlphaAnimation animation = new AlphaAnimation(1f, 0f);
		animation.setFillAfter(true);
		animation.setDuration(SHOW_HIDE_CONTROL_ANIMATION_TIME);
		isHeadViewShow = false;

		mTitleView.startAnimation(animation);
		mTitleView.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ImageSelectObservable.getInstance().updateImageSelectChanged();
		overridePendingTransition(0, R.anim.common_scale_large_to_small);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAllImage.get(mPosition).setSelect(false);
	}
}
