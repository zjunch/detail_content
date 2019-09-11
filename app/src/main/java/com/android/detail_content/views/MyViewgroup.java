package com.android.detail_content.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;


public class MyViewgroup extends LinearLayout implements TopOrBottomInterface {
    MyTopScollview topView;
    MyBottomScollview bottomView;
    private Scroller mScroller;
    int mHeight = 0;
    int mCurrentY, startY;
    boolean isTopnToBottom = false, isBottomToTop = false;
    boolean isIntercept = false;
    boolean isToBottom,istoTop;
    int viewIndex = 0;
   int mTouchSlop;
    public MyViewgroup(Context context) {
        super(context);
    }

    public MyViewgroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        setOrientation(VERTICAL);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }


    public void setViews(MyTopScollview topView, MyBottomScollview bottomView) {
        this.bottomView = bottomView;
        this.topView = topView;
        addView(topView);
        addView(bottomView);
        postInvalidate();
        topView.addIsBottomListener(this);
        bottomView.addIsTopListener(this);

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = 0;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, heightMeasureSpec);
                int height = child.getMeasuredHeight();
                mHeight += height;
            }
            int width = getResources().getDisplayMetrics().widthPixels;
            if (mHeight > 0) {
                setMeasuredDimension(width, mHeight);
            }

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int tempY = (int) ev.getY();
                    if(isTopnToBottom&&getScrollY()<mTouchSlop){     //判断手势抬起时是否需要切换页面
                        isToBottom=false;
                    }else if(isTopnToBottom&&getScrollY()>=mTouchSlop){
                        isToBottom=true;
                    }else if(isBottomToTop&&(tempY-startY)>=mTouchSlop){
                        istoTop=true;
                    }else if(isBottomToTop&&(tempY-startY)<mTouchSlop){
                        istoTop=false;
                    }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("event", "onInterceptTouchEvent");
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mCurrentY = (int) ev.getY();
                startY=mCurrentY;
                isIntercept = false;
            case MotionEvent.ACTION_MOVE:
                int tempY = (int) ev.getY();
                int deceseY = tempY - mCurrentY;
                if (isTopnToBottom && deceseY < 0) {//继续向上，出现下面的页面
                    isIntercept = true;
                } else if (isBottomToTop && deceseY > 0) {
                    isIntercept = true;
                } else {
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isIntercept = false;
                break;
        }
        return isIntercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("event", "onTouchEvent");
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mCurrentY = (int) event.getY();
                startY = mCurrentY;
                break;
            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();
                int deceseY = (tempY - mCurrentY)/2;
                if(getScrollY()-deceseY>=0&&  getScrollY()-deceseY<=mHeight/2){   //在0- mHeight/2 之间滚动
                    scrollBy(0, -deceseY);
                    mCurrentY = tempY;
                }
                break;
            case MotionEvent.ACTION_UP:
                int tempYY = (int) event.getY();
                mCurrentY = tempYY;
                isIntercept = false;
                int value = getScrollY();    //已经滚动的距离
                 if(isTopnToBottom&&isToBottom) {
                    addtoBottomAnimation(value, mHeight / 2);
                    isTopnToBottom = false;
                    isBottomToTop = true;
                }else if(isTopnToBottom&&!isToBottom) {
                    addtoTopAnimation(value);
                    isTopnToBottom = true;
                    isBottomToTop = false;
                }else if(isBottomToTop&&istoTop){
                     addtoTopAnimation(value);
                     isTopnToBottom = true;
                     isBottomToTop = false;
                 }else {
                     addtoBottomAnimation(value, mHeight / 2);
                     isTopnToBottom = false;
                     isBottomToTop = true;
                 }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 前往上页面动画
     * @param decese
     */
    private void addtoTopAnimation(int decese) {
        int duration=500;
        if(decese<300){
            duration=300;
        }
        ValueAnimator animator = ValueAnimator.ofInt(decese, 0).setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0, value);
            }
        });
        animator.start();
        viewIndex = 0;
    }

    /**
     * 前往下页面动画
     * @param decese
     */
    private void addtoBottomAnimation(int value, int decese) {
        int duration=500;
        if(Math.abs(decese-value)<=300){
            duration=300;
        }
        ValueAnimator animator = ValueAnimator.ofInt(value, decese).setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0, value);
            }
        });
        animator.start();
        viewIndex = 1;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    @Override
    public void isTopToBottom(boolean isBottom) {
        isTopnToBottom = isBottom;
    }

    @Override
    public void eventY(int desence) {    //此处为， 子页面的down/move--->拦截之后viewgroup的move,
        mCurrentY = desence;
    }

    @Override
    public void isBottomToTop(boolean isTop) {
        isBottomToTop = isTop;
    }


}
