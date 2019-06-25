package com.project.facerecognitiondemo.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.facerecognitiondemo.R;
import com.project.facerecognitiondemo.entity.TestDataBean;
import com.project.facerecognitiondemo.net.ApiMethods;
import com.project.facerecognitiondemo.net.MyObserver;
import com.project.facerecognitiondemo.net.ObserverOnNextListener;

public class DetectActivity extends AppCompatActivity {

    private ImageView imgBack;
    private Button mBtnDetectImage;
    private TextView textIntroDetails;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        getWindow().setEnterTransition(new Explode().setDuration(2000));
        getWindow().setExitTransition(new Explode().setDuration(2000));
        initView();
        initListener();
    }

    private void initListener() {
        mBtnDetectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetectActivity.this,DetectActivity.class));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                onBackPressed();
            }
        });
    }

    private void initView() {
        mBtnDetectImage = findViewById(R.id.btn_face_detect_img);
        imgBack = findViewById(R.id.img_back);
        textIntroDetails = findViewById(R.id.text_intro_details);
        textIntroDetails.setText("您可以上传一张图片，我们通过检测到图片中的人脸（支持一至多张人脸），可以标记出边框。您也可以对尺寸最大的5张人脸进行分析，获得面部关键点、年龄、性别、头部姿态、微笑检测、眼镜检测以及人脸质量等信息。");
    }
}
