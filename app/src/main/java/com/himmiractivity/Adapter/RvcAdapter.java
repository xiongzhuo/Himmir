package com.himmiractivity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.himmiractivity.entity.Space;
import com.himmiractivity.view.AlxRefreshLoadMoreRecyclerView;

import java.util.List;

import activity.hamir.com.himmir.R;

/**
 * Created by yxm on 2017.02.26.
 */

public class RvcAdapter extends AlxRefreshLoadMoreRecyclerView.AlxDragRecyclerViewAdapter<Space> {
    //设置事件回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public RvcAdapter(List<Space> mDatas, int itemLayout, boolean pullEnable) {
        super(mDatas, itemLayout, pullEnable);
    }

    @Override
    public RecyclerView.ViewHolder setItemViewHolder(View itemView) {
        return new AlxRecyclerViewHolder(itemView);
    }

    public void removeData(int position) {
        getDataList().remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setmDatas(List<Space> mDatas) {
        setDataList(mDatas);
        notifyDataSetChanged();
    }

    @Override
    public void initItemView(RecyclerView.ViewHolder itemHolder, int position, Space entity) {
        final AlxRecyclerViewHolder myViewHolder = (AlxRecyclerViewHolder) itemHolder;
        if (position == getDataList().size()) {
            myViewHolder.view_buttom.setVisibility(View.GONE);
        }
        myViewHolder.title.setText(getDataList().get(position - 1).getUserRoom().getRoom_name());
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = myViewHolder.getLayoutPosition() - 1;
                    mOnItemClickLitener.onItemClick(myViewHolder.itemView, pos);
                }
            });
            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = myViewHolder.getLayoutPosition() - 1;
                    mOnItemClickLitener.onItemLongClick(myViewHolder.itemView, pos);
                    return false;
                }
            });
        }
    }


    class AlxRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        View view_buttom;

        public AlxRecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_item_text);
            view_buttom = itemView.findViewById(R.id.view_buttom);
        }
    }
}
