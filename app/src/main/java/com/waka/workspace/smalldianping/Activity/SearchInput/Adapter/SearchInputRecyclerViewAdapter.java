package com.waka.workspace.smalldianping.Activity.SearchInput.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waka.workspace.smalldianping.Constant;
import com.waka.workspace.smalldianping.DataBase.DBHelper;
import com.waka.workspace.smalldianping.R;
import com.waka.workspace.smalldianping.Activity.SearchInput.SearchInputActivity;

import java.util.ArrayList;

/**
 * Created by waka on 2015/12/24.
 */
public class SearchInputRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "SearchInputAdapter";
    //标志常量
    public static final int TYPE_FOOTER = 0;//代表footerView，footer:页脚，代表尾view
    public static final int TYPE_NORMAL = 1;
    private Context mContext;
    private ArrayList<String> mDatas;
    private View mFooterView;

    /**
     * 构造方法
     *
     * @param context
     * @param datas
     */
    public SearchInputRecyclerViewAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    /**
     * 设置FooterView
     *
     * @param footerView
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        mDatas.add("footerView");
        notifyItemInserted(mDatas.size());//这个方法是在第position位置被插入了一条数据的时候可以使用这个方法刷新，注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
    }

    public void addData(String s) {
        mDatas.add(mDatas.size() - 1, s);
        notifyItemInserted(mDatas.size() - 1);
    }

    /**
     * 重写得到ItemView类型方法
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == mDatas.size() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SearchInputRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            SearchInputRecyclerViewHolder viewHolder = new SearchInputRecyclerViewHolder(mFooterView);
            viewHolder.tvHistory.setTag(TYPE_FOOTER);//为footerView设置标识
            return viewHolder;
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_item_history_in_activity_search_input, parent, false);
        return new SearchInputRecyclerViewHolder(view);
    }

    /**
     * 绑定ViewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            return;
        }
        ((SearchInputRecyclerViewHolder) holder).tvHistory.setText(mDatas.get(position));
    }

    /**
     * 得到Item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    /**
     * 内部类ViewHolder，继承自ViewHolder
     */
    class SearchInputRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layoutHistory;
        TextView tvHistory;

        public SearchInputRecyclerViewHolder(View itemView) {
            super(itemView);
            //initView
            tvHistory = (TextView) itemView.findViewById(R.id.tvHistory);
            layoutHistory = (LinearLayout) itemView.findViewById(R.id.layoutHistory);

            //initEvent
            layoutHistory.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layoutHistory:
                    //如果是footerView
                    if (tvHistory.getTag() != null) {
                        if ((int) tvHistory.getTag() == TYPE_FOOTER) {
                            //清除历史记录
                            DBHelper helper = new DBHelper(mContext);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            for (int i = mDatas.size() - 2; i >= 0; i--) {
                                mDatas.remove(i);
                                notifyItemRemoved(i);
                            }
                            db.execSQL("delete from " + Constant.TABLE_NAME_SEARCH_HISTORY);
                            ((SearchInputActivity) mContext).clearmTips();
                        }
                    }
                    //正常View
                    else {
                        ((SearchInputActivity) mContext).setSearchText(tvHistory.getText().toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
