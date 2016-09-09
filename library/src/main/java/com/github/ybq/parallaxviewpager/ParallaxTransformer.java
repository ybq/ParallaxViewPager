package com.github.ybq.parallaxviewpager;

import android.animation.FloatEvaluator;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Author : ybq
 * Date :  15/8/15.
 */
public class ParallaxTransformer implements ViewPager.PageTransformer {

    private Mode mMode;
    private Interpolator mInterpolator = new LinearInterpolator();
    private FloatEvaluator mEvaluator;
    private int mOutset;
    private float mOutsetFraction = 0.5f;

    public ParallaxTransformer() {
        mEvaluator = new FloatEvaluator();
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        mMode = mode;
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public int getOutset() {
        return mOutset;
    }

    public void setOutset(int outset) {
        this.mOutset = outset;
    }

    public void setOutsetFraction(float outsetFraction) {
        this.mOutsetFraction = outsetFraction;
    }

    @Override
    public void transformPage(View page, float position) {
        page.setTranslationX(0);
        if (position == 0) {
            return;
        }
        switch (mMode) {
            case LEFT_OVERLAY:
                if (position > 0) {
                    transform(page, position);
                } else if (position < 0) {
                    bringViewToFront(page);
                }
                break;
            case RIGHT_OVERLAY:
                if (position < 0) {
                    transform(page, position);
                } else if (position > 0) {
                    bringViewToFront(page);
                }
                break;
            case NONE:
                break;
        }
    }

    private void bringViewToFront(View view) {
        ViewGroup group = (ViewGroup) view.getParent();
        int index = group.indexOfChild(view);
        if (index != group.getChildCount() - 1) {
            view.bringToFront();
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                view.requestLayout();
                group.invalidate();
            }
        }
    }

    private void transform(View page, float position) {
        float interpolatorPosition;
        float translationX;
        int pageWidth = page.getWidth();
        if (mOutset <= 0) {
            mOutset = (int) (mOutsetFraction * page.getWidth());
        }

        if (position < 0) {
            interpolatorPosition = mInterpolator.getInterpolation(Math.abs(position));
            translationX = -mEvaluator.evaluate(interpolatorPosition, 0, (pageWidth - mOutset));
        } else {
            interpolatorPosition = mInterpolator.getInterpolation(position);
            translationX = mEvaluator.evaluate(interpolatorPosition, 0, (pageWidth - mOutset));
        }
        translationX += -page.getWidth() * position;
        page.setTranslationX(translationX);
    }

}