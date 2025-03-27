package me.sirdas.diyet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

    public class NonScrollBottomSheetBehavior<V extends View> extends BottomSheetBehavior {
    public NonScrollBottomSheetBehavior() {
        super();
    }

    public NonScrollBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        return false;
    }
}