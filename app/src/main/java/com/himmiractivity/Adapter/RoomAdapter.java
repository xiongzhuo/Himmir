package com.himmiractivity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.himmiractivity.entity.UserRoom;

import java.util.List;

import activity.hamir.com.himmir.R;

public class RoomAdapter extends BaseAdapter {
    private List<UserRoom> data;
    private LayoutInflater inflater;
    private Context context;
    private int position2 = -1;

    public RoomAdapter(Context mContext, List<UserRoom> data) {
        this.data = data;
        inflater = LayoutInflater.from(mContext);
        context = mContext;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition2() {
        return position2;
    }

    public void setPosition2(int position) {
        this.position2 = position;
    }

    public void setData(List<UserRoom> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<UserRoom> getData() {
        return data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final UserRoom dafalutUserRoom = data.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.head_item,
                    null);
        }
        final RadioButton rb_ad_type = ViewHolder.get(convertView,
                R.id.rb_ad_type);
        rb_ad_type.setText(dafalutUserRoom.getRoom_name());
        rb_ad_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dafalutUserRoom.isChecK()) {
                    for (UserRoom dafalutUserRoom : data) {
                        dafalutUserRoom.setChecK(false);
                    }
                    dafalutUserRoom.setChecK(!dafalutUserRoom.isChecK());
                    setPosition2(position);
                    notifyDataSetChanged();
                } else {
                    for (UserRoom dafalutUserRoom : data) {
                        dafalutUserRoom.setChecK(false);
                    }
                    setPosition2(-1);
                    notifyDataSetChanged();
                }
            }
        });
        rb_ad_type.setChecked(data.get(position).isChecK());
        return convertView;
    }

//    public void setResetRdio() {
//        for (DafalutUserRoom dafalutUserRoom : data) {
//            dafalutUserRoom.setChecK(false);
//        }
////		data.get(0).setChecK(true);
//        setPosition2(-1);
//        notifyDataSetChanged();
//    }

    public void setSaveRdio(String str) {
        int index = 0;
        for (UserRoom dafalutUserRoom : data) {
            if (str == dafalutUserRoom.getRoom_name()) {
                dafalutUserRoom.setChecK(true);
                setPosition2(index);
            } else {
                dafalutUserRoom.setChecK(false);
            }
            index++;
        }
        notifyDataSetChanged();
    }

}
