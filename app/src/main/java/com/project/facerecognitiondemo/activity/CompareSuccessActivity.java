package com.project.facerecognitiondemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.facerecognitiondemo.R;
import com.project.facerecognitiondemo.util.SharedPrefrenceUtils;

public class CompareSuccessActivity extends AppCompatActivity {

    private TextView textFaceTokenFlag;
    private Button btnBackMain;
    private Button btnComeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_success);
        initView();
        init();
        initListener();
    }

    private void init() {
        String faceFlag = (String) SharedPrefrenceUtils.getSharedPreference("face_flag", "");
        if (faceFlag!=null){
            textFaceTokenFlag.setText(faceFlag);
        }
    }

    private void initListener() {
        btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回主界面
                startActivity(new Intent(CompareSuccessActivity.this,MainActivity.class));
                onDestroy();
            }
        });
        btnComeManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompareSuccessActivity.this,FaceInfoManagerActivity.class));
            }
        });
    }

    private void initView() {
        textFaceTokenFlag = findViewById(R.id.text_face_token_flag);
        btnBackMain = findViewById(R.id.btn_back_main);
        btnComeManager = findViewById(R.id.btn_come_facemanager);
    }
}
