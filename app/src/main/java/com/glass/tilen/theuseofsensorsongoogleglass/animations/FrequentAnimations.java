package com.glass.tilen.theuseofsensorsongoogleglass.animations;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Tilen on 26.7.2015.
 */
public class FrequentAnimations {

    public static void fadeIn(View view, long duration)
    {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        anim.setDuration(duration);
        anim.start();
    }
}
