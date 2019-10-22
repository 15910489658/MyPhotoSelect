package com.photo.selectlib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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
public class ImageFolderAdapter extends BaseRecyclerAdapter<ImageFolderBean, RecyclerView.ViewHolder> {


	public ImageFolderAdapter(Context context, List<ImageFolderBean> list) {
		super(context, list);
		displayListener = new AnimateFirstDisplayListener();
	}


	@Override
	public PhotoFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.photo_folder_item, parent, false);
		return new PhotoFolderViewHolder(view);
	}


	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		final PhotoFolderViewHolder holder = (PhotoFolderViewHolder) viewHolder;
		ImageFolderBean imageFolderBean = list.get(position);
		holder.fileNameTv.setText(imageFolderBean.fileName);
		holder.fileNumsTv.setText(String.format(mContext.getResources().getString(R.string.photo_num), imageFolderBean.pisNum));
		ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(imageFolderBean.path), holder.imageIv, ImageLoaderHelper.buildDisplayImageOptionsDefault(R.drawable.defaultpic), displayListener);


		if (mOnClickListener != null) {
			holder.mCardView.setOnClickListener(new View.OnClickListener() {
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
		public TextView fileNameTv;
		public TextView fileNumsTv;
		public CardView mCardView;

		public PhotoFolderViewHolder(View itemView) {
			super(itemView);
			fileNameTv = (TextView)itemView.findViewById(R.id.tv_file_name);
			fileNumsTv = (TextView)itemView.findViewById(R.id.tv_pic_nums);
			imageIv = (ImageView)itemView.findViewById(R.id.iv_icon);
			mCardView = (CardView)itemView.findViewById(R.id.card_view);
		}
	}

}
