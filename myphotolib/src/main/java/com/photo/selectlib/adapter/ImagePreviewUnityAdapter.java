package com.photo.selectlib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.photo.selectlib.R;
import com.photo.selectlib.bean.ImageFolderBean;
import com.photo.selectlib.core.AnimateFirstDisplayListener;
import com.photo.selectlib.core.ImageLoaderHelper;

import java.util.List;

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
 * 图片选择目录适配器
 */
public class ImagePreviewUnityAdapter extends BaseRecyclerAdapter<ImageFolderBean, RecyclerView.ViewHolder> {

	public ImagePreviewUnityAdapter(Context context, List<ImageFolderBean> list) {
		super(context, list);
		displayListener = new AnimateFirstDisplayListener();
	}


	@Override
	public PhotoFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.image_preview_item, parent, false);
		return new PhotoFolderViewHolder(view);
	}


	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		final PhotoFolderViewHolder holder = (PhotoFolderViewHolder) viewHolder;
		ImageFolderBean imageFolderBean = list.get(position);
		ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(imageFolderBean.path), holder.imageIv, ImageLoaderHelper.buildDisplayImageOptionsDefault(R.drawable.defaultpic), displayListener);
		if(imageFolderBean.isSelect){
			holder.rl_item_select.setBackgroundResource(R.drawable.preview_item_box);
		}else{
			holder.rl_item_select.setBackgroundResource(R.color.preview_recycler_background_color);
		}
		if (mOnClickListener != null) {
			holder.imageIv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mOnClickListener.onItemClick(view, holder.getAdapterPosition());
				}
			});
		}

	}


	/**
	 * 自定义ViewHolder
	 */
	protected class PhotoFolderViewHolder extends RecyclerView.ViewHolder {

		public ImageView imageIv;
		RelativeLayout rl_item_select;

		public PhotoFolderViewHolder(View itemView) {
			super(itemView);
			imageIv = (ImageView)itemView.findViewById(R.id.iv_image_preview);
			rl_item_select = itemView.findViewById(R.id.rl_item_select);
		}
	}

}
