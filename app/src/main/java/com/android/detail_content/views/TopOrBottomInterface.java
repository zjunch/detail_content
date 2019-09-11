package com.android.detail_content.views;

public interface TopOrBottomInterface  {
    void  isTopToBottom(boolean isBottom);
    void  eventY(int eventY);    //eventY, 解决页面过渡时，继续向下滑动，MyViewgroup拦截执行onTouch的拖动时的距离偏差（不会走MyViewgroup的action_down）
                                           //eventY，上下面进行过渡的时候手指的位置
    void  isBottomToTop(boolean isTop);


}
