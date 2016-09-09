package com.github.ybq.parallaxviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;



import static android.graphics.drawable.GradientDrawable.Orientation.*;

/**
 * Created: ybq
 * Date: 2014-08-15
 * Description:
 */
public class ParallaxViewPager extends ViewPager {

    private Mode mMode;
    private int mShadowStart = Color.parseColor("#33000000");
    private int mShadowMid = Color.parseColor("#11000000");
    private int mShadowEnd = Color.parseColor("#00000000");
    private Drawable mRightShadow =
            new GradientDrawable(LEFT_RIGHT, new int[]{
                    mShadowStart,
                    mShadowMid,
                    mShadowEnd
            });
    private Drawable mLeftShadow =
            new GradientDrawable(RIGHT_LEFT, new int[]{
                    mShadowStart,
                    mShadowMid,
                    mShadowEnd
            });
    private int mShadowWidth;
    private ParallaxTransformer mParallaxTransformer;
    private Interpolator mInterpolator;
    private int mOutset;
    private float mOutsetFraction = 0.5f;

    public ParallaxViewPager(Context context) {
        this(context, null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mParallaxTransformer = new ParallaxTransformer();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxViewPager, 0,
                0);
        mMode =
                Mode.values()[a.getInt(R.styleable.ParallaxViewPager_mode, 0)];
        setMode(mMode);

        if (a.hasValue(R.styleable.ParallaxViewPager_right_shadow)) {
            mRightShadow = a.getDrawable(R.styleable.ParallaxViewPager_right_shadow);
        }
        if (a.hasValue(R.styleable.ParallaxViewPager_left_shadow)) {
            mLeftShadow = a.getDrawable(R.styleable.ParallaxViewPager_left_shadow);
        }
        mShadowWidth = a.getDimensionPixelSize(R.styleable.ParallaxViewPager_shadow_width, (int) dp2px(20, getContext()));
        TypedValue tv = a.peekValue(
                R.styleable.ParallaxViewPager_outset);
        if (tv != null) {
            if (tv.type == TypedValue.TYPE_FRACTION) {
                mOutsetFraction = a.getFraction(R.styleable.ParallaxViewPager_outset, 1, 1, 0);
                setOutsetFraction(mOutsetFraction);
            } else if (tv.type == TypedValue.TYPE_DIMENSION) {
                mOutset = (int) TypedValue.complexToDimension(tv.data, getResources().getDisplayMetrics());
                setOutset(mOutset);
            }
        }
        final int resID = a.getResourceId(R.styleable.ParallaxViewPager_interpolator, 0);
        if (resID > 0) {
            setInterpolator(context, resID);
        }
        a.recycle();
    }

    public int getOutset() {
        return mOutset;
    }

    public void setOutset(int outset) {
        this.mOutset = outset;
        mOutsetFraction = 0;
        mParallaxTransformer.setOutset(mOutset);
    }

    public float getOutsetFraction() {
        return mOutsetFraction;
    }

    public void setOutsetFraction(float outsetFraction) {
        this.mOutsetFraction = outsetFraction;
        mOutset = 0;
        mParallaxTransformer.setOutsetFraction(mOutsetFraction);
    }

    public void setRightShadow(GradientDrawable rightShadow) {
        this.mRightShadow = rightShadow;
    }

    public void setLeftShadow(GradientDrawable leftShadow) {
        this.mLeftShadow = leftShadow;
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator i) {
        mInterpolator = i;
        ensureInterpolator();
    }

    public void setInterpolator(Context context, int resID) {
        setInterpolator(AnimationUtils.loadInterpolator(context, resID));
    }

    protected void ensureInterpolator() {
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
        if (mParallaxTransformer != null) {
            mParallaxTransformer.setInterpolator(mInterpolator);
        }
    }

    public void drawShadow(Canvas canvas) {
        if (mMode == Mode.NONE) {
            return;
        }
        if (getScrollX() % getWidth() == 0) {
            return;
        }
        switch (mMode) {
            case LEFT_OVERLAY:
                drawRightShadow(canvas);
                break;
            case RIGHT_OVERLAY:
                drawLeftShadow(canvas);
                break;
        }
    }

    private void drawRightShadow(Canvas canvas) {
        canvas.save();
        float translate = (getScrollX() / getWidth() + 1) * getWidth();
        canvas.translate(translate, 0);
        mRightShadow.setBounds(0, 0, mShadowWidth, getHeight());
        mRightShadow.draw(canvas);
        canvas.restore();
    }

    private void drawLeftShadow(Canvas canvas) {
        canvas.save();
        float translate = (getScrollX() / getWidth() + 1) * getWidth() - mShadowWidth;
        canvas.translate(translate, 0);
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        mLeftShadow.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setPageMargin(int marginPixels) {
        super.setPageMargin(0);
    }

    private float dp2px(int dip, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dip * scale + 0.5f;
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        if (offset == 0) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                mParallaxTransformer.transformPage(getChildAt(i), 0);
            }
        }
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        mMode = mode;
        mParallaxTransformer.setMode(mode);
        if (mode == Mode.LEFT_OVERLAY) {
            setPageTransformer(true, mParallaxTransformer);
        } else if (mode == Mode.RIGHT_OVERLAY) {
            setPageTransformer(false, mParallaxTransformer);
        }
    }

}
