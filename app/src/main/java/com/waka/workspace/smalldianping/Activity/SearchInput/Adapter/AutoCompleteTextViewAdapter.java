package com.waka.workspace.smalldianping.Activity.SearchInput.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.waka.workspace.smalldianping.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by waka on 2015/12/26.
 */
public class AutoCompleteTextViewAdapter extends ArrayAdapter {

    private Context mContext;
    private int mResourceId;
    private ArrayList<String> mDatas;

    /**
     * 构造方法
     *
     * @param context
     * @param resource
     * @param objects
     */
    public AutoCompleteTextViewAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        mContext = context;
        mResourceId = resource;
        mDatas = (ArrayList<String>) objects;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mResourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        //设置数据
        viewHolder.textView.setText(mDatas.get(position).toString());
        return view;
    }

    /**
     * 内部类ViewHolder
     */
    class ViewHolder {
        TextView textView;
    }
}
