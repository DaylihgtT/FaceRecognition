package com.project.facerecognitiondemo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.facerecognitiondemo.R;
import com.project.facerecognitiondemo.entity.DeleteFaceSetBean;
import com.project.facerecognitiondemo.entity.QueryFaceSetBean;
import com.project.facerecognitiondemo.net.ApiMethods;
import com.project.facerecognitiondemo.net.MyObserver;
import com.project.facerecognitiondemo.net.ObserverOnNextListener;
import com.project.facerecognitiondemo.util.AccessibilityAPIsUtil;
import com.project.facerecognitiondemo.util.SharedPrefrenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class FaceInfoManagerActivity extends AppCompatActivity {

    private ImageView imgDeleteFaceToken;
    private List<String> faceSetTokenList = new ArrayList<>();
    private int size;
    private int start = 0;
    private static final String SP_SAVE_FACE_SET_NAME = "user_face_info";
    private String faceFlag;
    private TextView textFaceFlag;
    private ImageView imgBack;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                if (start < size){
                    getDeleteFaceSetTokenResult();
                }else {
                    Log.d("—ZRT—", "删除完毕");
                    relativeProgressLayout.setVisibility(View.GONE);
                    Toast.makeText(FaceInfoManagerActivity.this,"删除完毕",Toast.LENGTH_SHORT).show();
                    SharedPrefrenceUtils.put("isInputFace",false);
                    removeCallbacksAndMessages(null);
                }
            }
        }
    };
    private RelativeLayout relativeProgressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_info_manager);
        initView();
        init();
        initListener();
    }

    private void init() {
        Intent intent = getIntent();
        faceFlag = intent.getStringExtra("face_flag");
        if (faceFlag != null) {
            SharedPrefrenceUtils.put("face_flag", faceFlag);
            textFaceFlag.setText(faceFlag);
        }
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgDeleteFaceToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeProgressLayout.setVisibility(View.VISIBLE);
                //查询所有的faceSetToken数据
                getQueryFaceSetResult();
            }
        });
    }

    private void initView() {
        textFaceFlag = findViewById(R.id.text_face_flag);
        imgDeleteFaceToken = findViewById(R.id.img_delete_facetoken);
        imgBack = findViewById(R.id.img_back);
        relativeProgressLayout = findViewById(R.id.relative_layout_progress);
    }

    private void getQueryFaceSetResult() {
        HashMap<String, RequestBody> stringRequestBodyHashMap = AccessibilityAPIsUtil.queryAllFaceSets(SP_SAVE_FACE_SET_NAME);
        ObserverOnNextListener<QueryFaceSetBean> getQueryResultListener = new ObserverOnNextListener<QueryFaceSetBean>() {
            @Override
            public void onNext(QueryFaceSetBean queryFaceSetBean) {
                List<QueryFaceSetBean.FacesetsBean> facesets = queryFaceSetBean.getFacesets();
                if (facesets == null && facesets.size() == 0) {
                    Log.d("—ZRT—", "faceSet 为空");
                } else {
                    for (int i = 0; i < facesets.size(); i++) {
                        Log.d("—ZRT—", "faceSet _ Name = " + facesets.get(i).getDisplay_name() + ",Tag = " + facesets.get(i).getTags() + ",FaceSetToken = " + facesets.get(i).getFaceset_token());
                        faceSetTokenList.add(facesets.get(i).getFaceset_token());
                    }
                    size = facesets.size();
                }
                mHandler.sendEmptyMessageDelayed(0,2000);
            }
        };
        ApiMethods.getQueryFaceSetResult(new MyObserver<QueryFaceSetBean>(FaceInfoManagerActivity.this, getQueryResultListener), stringRequestBodyHashMap);
    }

    private void getDeleteFaceSetTokenResult() {
        if (faceSetTokenList.size() == 0) {
            return;
        }
        HashMap<String, RequestBody> stringRequestBodyHashMap = AccessibilityAPIsUtil.removeFaceSet(faceSetTokenList.get(start));
        ObserverOnNextListener<DeleteFaceSetBean> getRemoveFaceSetTokenResultListener = new ObserverOnNextListener<DeleteFaceSetBean>() {
            @Override
            public void onNext(DeleteFaceSetBean deleteFaceSetBean) {
                String faceset_token = deleteFaceSetBean.getFaceset_token();
                if (faceset_token.equals(faceSetTokenList.get(start))) {
                    Log.d("—ZRT—", "faceSetToken = " + faceset_token + " 删除成功------------>>");
                    if (start <= size - 1) {
                        start++;
                    }
                } else {
                    Log.d("—ZRT—", "faceSetToken = " + faceset_token + " 删除失败------------>>");
                }
                mHandler.sendEmptyMessageDelayed(0,1000);
            }
        };
        ApiMethods.getDeleteFaceSetResult(new MyObserver<DeleteFaceSetBean>(FaceInfoManagerActivity.this, getRemoveFaceSetTokenResultListener), stringRequestBodyHashMap);
    }

}
