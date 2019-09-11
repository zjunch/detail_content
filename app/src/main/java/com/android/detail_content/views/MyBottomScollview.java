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

public class MyBottomScollview extends ScrollView  {

    private int desenceY;
    int eventY,startY;

    public MyBottomScollview(Context context) {
        super(context);
    }

    public MyBottomScollview(Context context, AttributeSet attrs) {
        super(context, attrs);

    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addIsTopListener(final TopOrBottomInterface topOrBottomInterface){
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (getScrollY() == 0) {
                    Log.e("zjun","顶部");
                    topOrBottomInterface.isBottomToTop(true);
                    ViewParent view= (ViewParent) getParent();
                    view.requestDisallowInterceptTouchEvent(false);
                    topOrBottomInterface.eventY(eventY);
                    //顶部
                }else{
                    View contentView = getChildAt(0);
                    if (contentView != null && contentView.getMeasuredHeight() == (getScrollY() + getHeight())) {
                        topOrBottomInterface.isBottomToTop(false);
                    }else{
                        topOrBottomInterface.isBottomToTop(false);
                    }
                }



            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            startY= (int) ev.getY();
            eventY=startY;
        }else if(ev.getAction()==MotionEvent.ACTION_MOVE){
            eventY= (int) ev.getY();
        }
        return super.onTouchEvent(ev);
    }

}
