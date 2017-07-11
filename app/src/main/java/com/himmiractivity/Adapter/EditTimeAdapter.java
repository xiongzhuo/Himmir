package com.himmiractivity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import activity.hamir.com.himmir.R;

/**
 * Created by yxm on 2017.02.26.
 */

public class EditTimeAdapter extends RecyclerView.Adapter {
    private List<String> mDatas;
    private LayoutInflater mInflater;

    //设置事件回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public EditTimeAdapter(Context context, List<String> mDatas) {
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(context);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView onOff, tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            onOff = (TextView) itemView.findViewById(R.id.tv_on_off);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(mInflater.inflate(R.layout.edittime_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tv_time.setText(mDatas.get(position));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = myViewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(myViewHolder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = myViewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(myViewHolder.itemView, pos);
                    removeData(pos);
                    return false;
                }
            });
        }
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
