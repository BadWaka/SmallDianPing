package com.waka.workspace.smalldianping.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可以嵌套进ScrollView中的ListView
 * Created by waka on 2016/1/4.
 */
public class ListViewForScrollView extends ListView {

    public ListViewForScrollView(Context context) {
        this(context, null);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
