package com.baimeng.loveflower;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by Administrator on 2017/8/6.
 */

public class LoveLayout extends RelativeLayout {
    private Random mRandom ;
    int [] mImageRes ;
    //控件宽高
    private int mWidth ;
    private int mHeight ;
    //图片宽高
    private int mDrawableWidth ;
    private int mDrawableHeight ;

    private Interpolator [] mInterpolator ;
    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRandom = new Random();
        mImageRes = new int[]{R.mipmap.pl_blue,R.mipmap.pl_red,R.mipmap.pl_yellow};
        mInterpolator = new Interpolator[]{new AccelerateDecelerateInterpolator()
        ,new AccelerateInterpolator() , new DecelerateInterpolator() , new LinearInterpolator(),};
        //TODO 获取图片的宽高
        Drawable drawable = ContextCompat.getDrawable(context, mImageRes[0]);
        mDrawableWidth = drawable.getIntrinsicWidth() ;
        mDrawableHeight = drawable.getIntrinsicHeight() ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void addLove(){
        final ImageView loveIv = new ImageView(getContext());
        loveIv.setImageResource(mImageRes[mRandom.nextInt(mImageRes.length-1)]);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        loveIv.setLayoutParams(params);
        addView(loveIv);
        AnimatorSet animator = getAnimatior(loveIv);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //TODO移除
                removeView(loveIv);

            }
        });



    }

    public AnimatorSet getAnimatior(ImageView iv){
        AnimatorSet allAnimatorSet = new AnimatorSet() ;
        AnimatorSet innerAnimator = new AnimatorSet();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(iv, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(iv, "scaleX", 0.3f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iv, "scaleY", 0.3f, 1.0f);
        innerAnimator.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator);
        innerAnimator.setDuration(350);
        innerAnimator.start();
        //按顺序执行
        allAnimatorSet.playSequentially(innerAnimator,getBezierAnimator(iv));
        allAnimatorSet.start();

        return allAnimatorSet ;
    }

    private Animator getBezierAnimator(final ImageView iv) {
        PointF point0 = new PointF(mWidth/2 - mDrawableWidth/2,mHeight - mDrawableHeight/2) ;
        //的Y值一定要大于p1的y
        PointF point1 = getPoint(1) ;
        PointF point2 = getPoint(2) ;
        PointF point3 = new PointF(mRandom.nextInt(mWidth)-mDrawableWidth,0);
        LoveTypeEvaluator typeEvaluator = new LoveTypeEvaluator(point1,point2);
        //第一个参数LoveTypeEvaluator 第二个参数 p0 ，第三个参数
        ValueAnimator bezierAnimator =  ObjectAnimator.ofObject(typeEvaluator,point0,point3);
        bezierAnimator.setDuration(3000);
        bezierAnimator.setInterpolator(mInterpolator[mRandom.nextInt(mInterpolator.length-1)]);
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                iv.setX(pointF.x);
                iv.setY(pointF.y);
                //添加透明度
                float t = animation.getAnimatedFraction();
                iv.setAlpha(1 - t + 0.2f );
            }
        });
        return bezierAnimator ;
    }

    public PointF getPoint(int index) {
        return new PointF(mRandom.nextInt(mWidth) - mDrawableWidth ,mRandom.nextInt(mHeight/2)+(index-1)*(mHeight/2));
    }
}
