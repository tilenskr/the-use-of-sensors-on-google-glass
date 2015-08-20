package com.glass.tilen.theuseofsensorsongoogleglass.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Tilen on 26.7.2015.
 */
public class FrequentAnimations {

    public static void fadeIn(final View view, final String text)
    {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(view instanceof TextView && !text.equals("")) // just to be sure
                {
                    TextView tv = (TextView) view;
                    tv.setText(text);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.setDuration(200);
        anim.start();
    }

    public static void fadeOutFadeIn(final View view, final String newText)
    {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(view instanceof TextView) // just to be sure
                {
                    TextView tv = (TextView) view;
                    tv.setText(newText);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeOut).before(fadeIn);
        animatorSet.setDuration(400);
        animatorSet.start();;
    }
}
