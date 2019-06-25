package com.project.facerecognitiondemo.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Explode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.facerecognitiondemo.R;

public class AddMoreCompareInfoActivity extends AppCompatActivity {

    private EditText editFaceFlag;
    private TextView jumpView;
    private ImageView nextView;
    private TextView textNumHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_compare_info);
        initView();
        initListener();
    }

    private void initListener() {



        editFaceFlag.setFocusable(true);
        editFaceFlag.requestFocus();
        editFaceFlag.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = editFaceFlag.getSelectionStart();
                editEnd = editFaceFlag.getSelectionEnd();
                if (temp.length()>12) {
                    textNumHint.setVisibility(View.VISIBLE);
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    editFaceFlag.setText(s);
                    editFaceFlag.setSelection(tempSelection);
                }
            }
        });

        jumpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMoreCompareInfoActivity.this,FaceInfoManagerActivity.class));
            }
        });

        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editFaceFlag.getText().toString();
                if (!TextUtils.isEmpty(s)){
                    Intent intent = new Intent(AddMoreCompareInfoActivity.this,FaceInfoManagerActivity.class);
                    intent.putExtra("face_flag",s);
                    startActivity(intent);
                }else {
                    Toast.makeText(AddMoreCompareInfoActivity.this,"请输入标识！",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView() {
        editFaceFlag = findViewById(R.id.add_more_edit_face_flag);
        jumpView = findViewById(R.id.add_more_text_jump_view);
        nextView = findViewById(R.id.add_more_img_next_view);
        textNumHint = findViewById(R.id.add_more_edit_hint_num);
    }

}
