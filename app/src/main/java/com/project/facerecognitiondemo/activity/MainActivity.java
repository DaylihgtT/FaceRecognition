package com.project.facerecognitiondemo.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.facerecognitiondemo.R;

public class MainActivity extends AppCompatActivity {

    //人脸检测
    private Button mBtnFaceDetect;
    private Button mBtnFaceCompare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        mBtnFaceDetect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //跳转到人脸检测Activity
                startActivity(new Intent(MainActivity.this,DetectActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
        mBtnFaceCompare.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //跳转到人脸识别Activity
                startActivity(new Intent(MainActivity.this,CompareActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
    }

    private void initView() {
        mBtnFaceDetect = findViewById(R.id.btn_face_detect);
        mBtnFaceCompare = findViewById(R.id.btn_face_compare);
    }
}
