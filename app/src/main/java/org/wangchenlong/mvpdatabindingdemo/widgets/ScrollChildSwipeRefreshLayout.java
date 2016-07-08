package org.wangchenlong.mvpdatabindingdemo.widgets;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * 下拉刷新控件, 处理与ListView的滚动冲突
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {
    private View mScrollUpChild;

    public ScrollChildSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollChildSwipeRefreshLayout(Context context) {
        super(context);
    }

    @Override public boolean canChildScrollUp() {
        if (mScrollUpChild != null) {
            // 向上滑
            return ViewCompat.canScrollVertically(mScrollUpChild, -1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View scrollUpChild) {
        mScrollUpChild = scrollUpChild;
    }
}
