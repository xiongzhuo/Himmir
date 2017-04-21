package com.himmiractivity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.himmiractivity.entity.ManagerShardBean;
import com.himmiractivity.interfaces.MyOnSlipStatusListener;
import com.himmiractivity.view.SwipeListLayout;

import java.util.List;
import java.util.Set;

import activity.hamir.com.himmir.R;

/**
 * Created by Administrator on 2017/4/21.
 */

public class AddedAdapter extends BaseAdapter {
    List<ManagerShardBean.ManagerShardSum> list;
    Set<SwipeListLayout> sets;
    Context context;

    public AddedAdapter(Context context, Set<SwipeListLayout> sets, List<ManagerShardBean.ManagerShardSum> list) {
        this.context = context;
        this.sets = sets;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(final int arg0, View view, ViewGroup arg2) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.slip_added_item, null);
        }
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText(list.get(arg0).getUser_name());
        final SwipeListLayout sll_main = (SwipeListLayout) view
                .findViewById(R.id.sll_main);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(
                sll_main, sets));
        tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);
                list.remove(arg0);
                notifyDataSetChanged();
            }
        });
        return view;
    }

}
