package com.himmiractivity.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.himmiractivity.base.BaseSwipeAdapter;
import com.himmiractivity.entity.ManagerShardBean;
import com.himmiractivity.enums.DragEdge;
import com.himmiractivity.enums.ShowMode;
import com.himmiractivity.interfaces.OnllClick;
import com.himmiractivity.interfaces.SwipeListener;
import com.himmiractivity.request.DelChareUserRequest;
import com.himmiractivity.zlistview.ZSwipeItem;

import java.util.List;

import activity.hamir.com.himmir.R;

public class ListViewAdapter extends BaseSwipeAdapter {
    private OnllClick onLLClick;

    protected static final String TAG = "ListViewAdapter";
    private Activity context;
    List<ManagerShardBean.ManagerShardSum> lists;

    public ListViewAdapter(Activity context, List<ManagerShardBean.ManagerShardSum> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_item;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return context.getLayoutInflater().inflate(R.layout.item_listview, parent, false);
    }

    public void setOnLLClick(OnllClick onLLClick) {
        this.onLLClick = onLLClick;
    }

    public List<ManagerShardBean.ManagerShardSum> getLists() {
        return lists;
    }

    public void setLists(List<ManagerShardBean.ManagerShardSum> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final ZSwipeItem swipeItem = (ZSwipeItem) convertView.findViewById(R.id.swipe_item);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);

        TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
        tv.setText(lists.get(position).getUser_name());
        swipeItem.setShowMode(ShowMode.PullOut);
        swipeItem.setDragEdge(DragEdge.Right);

        swipeItem.addSwipeListener(new SwipeListener() {
            @Override
            public void onOpen(ZSwipeItem layout) {
                Log.d(TAG, "打开:" + position);
            }

            @Override
            public void onClose(ZSwipeItem layout) {
                Log.d(TAG, "关闭:" + position);
            }

            @Override
            public void onStartOpen(ZSwipeItem layout) {
                Log.d(TAG, "准备打开:" + position);
            }

            @Override
            public void onStartClose(ZSwipeItem layout) {
                Log.d(TAG, "准备关闭:" + position);
            }

            @Override
            public void onHandRelease(ZSwipeItem layout, float xvel, float yvel) {
                Log.d(TAG, "手势释放");
            }

            @Override
            public void onUpdate(ZSwipeItem layout, int leftOffset, int topOffset) {
                Log.d(TAG, "位置更新");
            }
        });
        // 如果设置了回调，则设置点击事件
        if (onLLClick != null) {
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeItem.close();
                    onLLClick.onLLClick(position);
                }
            });
        }
    }
}
