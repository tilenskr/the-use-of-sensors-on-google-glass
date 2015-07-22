package com.glass.tilen.theuseofsensorsongoogleglass.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.glass.widget.CardScrollView;

/**
 * Created by Tilen on 14.7.2015.
 */
public class CustomCardScrollView extends CardScrollView {
    public CustomCardScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCardScrollView(Context context) {
        super(context);
    }

    private boolean enabled;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
