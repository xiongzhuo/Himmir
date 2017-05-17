package com.himmiractivity.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.himmiractivity.entity.UserDerviceBean;
import com.himmiractivity.view.AlxRefreshLoadMoreRecyclerView;

import java.util.List;

import activity.hamir.com.himmir.R;

/**
 * Created by yxm on 2017.02.26.
 */

public class UserDerviceRvcAdapter extends AlxRefreshLoadMoreRecyclerView.AlxDragRecyclerViewAdapter<UserDerviceBean.UserDerviceSum> {
    //设置事件回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public UserDerviceRvcAdapter(Context context, List<UserDerviceBean.UserDerviceSum> mDatas, int itemLayout, boolean pullEnable) {
        super(context, mDatas, itemLayout, pullEnable);
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

    public void setmDatas(List<UserDerviceBean.UserDerviceSum> mDatas) {
        setDataList(mDatas);
        notifyDataSetChanged();
    }

    @Override
    public void initItemView(RecyclerView.ViewHolder itemHolder, int posion, UserDerviceBean.UserDerviceSum entity) {
        final AlxRecyclerViewHolder myViewHolder = (AlxRecyclerViewHolder) itemHolder;
        if (posion == getDataList().size()) {
            myViewHolder.view_buttom.setVisibility(View.GONE);
        }
        if (getDataList().get(posion - 1).getDevice_nickname() != null) {
            myViewHolder.title.setText(getDataList().get(posion - 1).getDevice_nickname());
        }
        if (getDataList().get(posion - 1).isOnLine()) {
            myViewHolder.tvOnLine.setText("在线");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.round);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, 25, 25);
            myViewHolder.tvOnLine.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.dis_select);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, 25, 25);
            myViewHolder.tvOnLine.setText("离线");
            myViewHolder.tvOnLine.setCompoundDrawables(drawable, null, null, null);
        }
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
        TextView title, tvOnLine;
        View view_buttom;

        public AlxRecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_item_text);
            tvOnLine = (TextView) itemView.findViewById(R.id.tv_on_line);
            view_buttom = itemView.findViewById(R.id.view_buttom);
        }
    }
}
