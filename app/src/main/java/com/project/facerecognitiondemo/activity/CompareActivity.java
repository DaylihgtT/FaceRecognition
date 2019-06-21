package com.project.facerecognitiondemo.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.project.facerecognitiondemo.R;

public class CompareActivity extends AppCompatActivity {

    private ImageView imgBack;
    private Button btnCompareFaceBegin;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        getWindow().setEnterTransition(new Explode().setDuration(2000));
        getWindow().setExitTransition(new Explode().setDuration(2000));
        initView();
        initListener();
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                onBackPressed();
            }
        });
        btnCompareFaceBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击开始人脸检测
            }
        });
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        btnCompareFaceBegin = findViewById(R.id.btn_compare_face_begin);
    }

}
