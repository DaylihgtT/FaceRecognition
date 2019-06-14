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

import com.project.facerecognitiondemo.R;
import com.project.facerecognitiondemo.entity.TestDataBean;
import com.project.facerecognitiondemo.net.ApiMethods;
import com.project.facerecognitiondemo.net.MyObserver;
import com.project.facerecognitiondemo.net.ObserverOnNextListener;

public class DetectActivity extends AppCompatActivity {

    private ImageView imgBack;
    private Button mBtnDetectImage;
    private Button mBtnTestImage;

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
        mBtnTestImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //跳转到测试区域
                startActivity(new Intent(DetectActivity.this,TestImageActivity.class), ActivityOptions.makeSceneTransitionAnimation(DetectActivity.this).toBundle());
            }
        });
        mBtnDetectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到检测页面
                //测试接口及网络请求
                ObserverOnNextListener<TestDataBean> listener = new ObserverOnNextListener<TestDataBean>() {
                    @Override
                    public void onNext(TestDataBean testDataBean) {
                        Log.d("—ZRT—","testDataBean----"+testDataBean.toString());
                    }
                };
                ApiMethods.getTopMovie(new MyObserver<TestDataBean>(DetectActivity.this,listener),"f4b23727f09d0bcd819e3d4273ca3ccc");
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
        mBtnTestImage = findViewById(R.id.btn_face_detect_test_img);
        mBtnDetectImage = findViewById(R.id.btn_face_detect_img);
        imgBack = findViewById(R.id.img_back);
    }
}
