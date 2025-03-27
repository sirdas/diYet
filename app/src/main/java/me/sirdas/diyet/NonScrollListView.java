package me.sirdas.diyet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

public class NonScrollListView extends ListView {

    public NonScrollListView(Context context) {
        super(context);
    }

    public NonScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (canScrollVertically(this))
        {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (canScrollVertically(this))
        {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }

    public boolean canScrollVertically(AbsListView view)
    {
        boolean canScroll = false;
        if (view != null && view.getChildCount() > 0)
        {
            boolean isOnTop = view.getFirstVisiblePosition() != 0 || view.getChildAt(0).getTop() != 0;
            boolean isAllItemsVisible = isOnTop && view.getLastVisiblePosition() == view.getChildCount();
            if (isOnTop || isAllItemsVisible)
            {
                canScroll = true;
            }
        }
        return canScroll;
    }
}
