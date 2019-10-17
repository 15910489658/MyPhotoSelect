package com.DefaultCompany.ProductName.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.DefaultCompany.ProductName.core.AnimateFirstDisplayListener;
import com.DefaultCompany.ProductName.listener.OnRecyclerViewClickListener;

import java.util.List;

/**
 *  　　　　　　　　┏┓　　　┏┓
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
 * RecyclerAdapter 适配器基类
 */
public class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected OnRecyclerViewClickListener mOnClickListener;

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected AnimateFirstDisplayListener displayListener;

    protected BaseRecyclerAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(mContext);
    }

    protected List<T> list;
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public void setOnClickListener(OnRecyclerViewClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

}
