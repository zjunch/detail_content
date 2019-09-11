package com.android.detail_content;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.android.detail_content.views.MyBottomScollview;
import com.android.detail_content.views.MyTopScollview;
import com.android.detail_content.views.MyViewgroup;


public class TestActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    MyViewgroup myViewgroup= findViewById(R.id.myGroup);
    MyTopScollview top= (MyTopScollview) LayoutInflater.from(this).inflate(R.layout.top,null);
    MyBottomScollview bottom= (MyBottomScollview) LayoutInflater.from(this).inflate(R.layout.bottom,null);
    myViewgroup.setViews(top,bottom);
  }




}
