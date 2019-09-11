package com.android.detail_content.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ScrollView;

public class MyTopScollview  extends ScrollView  {

    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    private int desenceY;
    int eventY,startY;
    public MyTopScollview(Context context) {
        super(context);
    }

    public MyTopScollview(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    public void setInterrapter(){
        getParent().requestDisallowInterceptTouchEvent(true);   //通知父view不要拦截了
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addIsBottomListener(final TopOrBottomInterface topOrBottomInterface){
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("zjun","scrollY:"+scrollY+"  oldScrollY:"+oldScrollY);
                if (getScrollY() == 0) {
                    Log.e("zjun","顶部");
                    isScrolledToTop=true;
                    isScrolledToBottom=false;
                    topOrBottomInterface.isTopToBottom(false);
                    //顶部
                }else{
                    View contentView = getChildAt(0);
                    if (contentView != null && contentView.getMeasuredHeight() == (getScrollY() + getHeight())) {
                        //底部
                        Log.e("zjun","底部");
                        isScrolledToTop=false;
                        isScrolledToBottom=true;
                        ViewParent view= (ViewParent) getParent();
                        view.requestDisallowInterceptTouchEvent(false);   //通知父view可以拦截了
                        topOrBottomInterface.eventY(eventY);
                        topOrBottomInterface.isTopToBottom(true);
                    }else{
                        Log.e("zjun","中间");
                        isScrolledToTop=false;
                        isScrolledToBottom=false;
                        topOrBottomInterface.isTopToBottom(false);
                    }
                }



            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            eventY= (int) ev.getY();
            startY=eventY;
        }else if(ev.getAction()==MotionEvent.ACTION_MOVE){
            eventY= (int) ev.getY();
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }

    }

}
