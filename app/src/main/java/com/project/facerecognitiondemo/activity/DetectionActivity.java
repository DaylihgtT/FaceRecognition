package com.project.facerecognitiondemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.facerecognitiondemo.R;
import com.project.facerecognitiondemo.entity.DetectImagBean;
import com.project.facerecognitiondemo.net.ApiMethods;
import com.project.facerecognitiondemo.net.MyObserver;
import com.project.facerecognitiondemo.net.ObserverOnNextListener;
import com.project.facerecognitiondemo.util.AccessibilityAPIsUtil;
import com.project.facerecognitiondemo.util.ChooseImgUtil;
import com.project.facerecognitiondemo.util.SharedPrefrenceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class DetectionActivity extends AppCompatActivity {

    private ImageView imgHolder;
    private View contentView;
    private PopupWindow popupWindow;
    private ImageView imgShowDetection;
    private Uri imgUri = null;              //拍照后保存照片的uri
    private File imgFile = null;            //拍照后保存的照片
    private File cropFile = null;
    private static String AUTHORITY = "com.project.facedemo.fileProvider";      //FileProvider的签名
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static int REQUEST_CODE_CAPTUR = 1;
    private static int REQUEST_CODE_ALBUM = 2;
    private static int REQUEST_CODE_CAPTURE_CROP = 3;
    private Button btnStartDetect;
    private RelativeLayout relativeProgressLayout;
    private static final String SP_SAVE_DETECT_FACE_TOKEN_NAME = "";
    private RelativeLayout showAllAttrRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        initView();
        initListener();
    }

    private void initListener() {
        imgHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //底部弹框
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                setPopwindowListener(contentView);
            }
        });
        btnStartDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgHolder.getVisibility()==View.VISIBLE){
                    Toast.makeText(DetectionActivity.this,"请先选择需要检测的图片",Toast.LENGTH_SHORT).show();
                }else {
                    relativeProgressLayout.setVisibility(View.VISIBLE);
                    //开始检测
                    getDetectFaceToken();
                }
            }
        });
    }

    private void getDetectFaceToken() {
        HashMap<String, RequestBody> stringRequestBodyHashMap = AccessibilityAPIsUtil.detectImgForFaceToken(cropFile.getAbsolutePath());
        ObserverOnNextListener<DetectImagBean> getFaceTokenlistener = new ObserverOnNextListener<DetectImagBean>() {
            @Override
            public void onNext(DetectImagBean detectImagBean) {
                String face_token = null;
                List<DetectImagBean.FacesBean> faces = detectImagBean.getFaces();
                //判断faces是否为null，如果为null则提示用户，未能检测到人脸数据
                if (faces.size() == 0) {
                    Log.d("—ZRT—", "faces为null--------->>");
                    relativeProgressLayout.setVisibility(View.GONE);
                    Toast.makeText(DetectionActivity.this, "未能检测出人脸数据，请对准面部重试", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < detectImagBean.getFaces().size(); i++) {
                    /*face_token = detectImagBean.getFaces().get(i).getFace_token();
                    Log.d("—ZRT—", "face_token = " + face_token);*/
                    //赋值 progress 开始检测按钮消失
                }
                //将face_token保存
                SharedPrefrenceUtils.put(SP_SAVE_DETECT_FACE_TOKEN_NAME, face_token);
            }
        };
        ApiMethods.getDetectImageResult(new MyObserver<DetectImagBean>(DetectionActivity.this, getFaceTokenlistener), stringRequestBodyHashMap);
    }

    private void initView() {
        imgHolder = findViewById(R.id.img_holder_place);
        imgShowDetection = findViewById(R.id.img_show_detetion);
        showAllAttrRelativeLayout = findViewById(R.id.show_all_attr_relative_layout);
        btnStartDetect = findViewById(R.id.btn_start_detect);
        relativeProgressLayout = findViewById(R.id.relative_layout_progress);
        showPopwindow();
    }

    private void showPopwindow() {
        contentView = LayoutInflater.from(DetectionActivity.this).inflate(R.layout.popwindow_layout_show, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
    }

    private void setPopwindowListener(View contentView) {
        if (popupWindow == null || !popupWindow.isShowing()) {
            Log.d("—ZRT—", "请初始化popwindow");
            return;
        }

        TextView openAlbum = contentView.findViewById(R.id.text_open_album);
        TextView openCamera = contentView.findViewById(R.id.text_open_camera);

        openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相册
                Log.d("—ZRT—", "打开相册");
                takeAlbum();
                popupWindow.dismiss();
            }
        });
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相机
                Log.d("—ZRT—", "打开相机");
                gotoCaptureCrop();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("—ZRT-","已执行----------->>");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Log.d("—ZRT-","已执行----------->>");
                    if (imgFile!=null) {
                        Uri sourceUri = FileProvider.getUriForFile(this, AUTHORITY, imgFile); //通过FileProvider创建一个content类型的Uri
                        takeCrop(sourceUri);
                    }
                    break;
                case 2:
                    if (data != null) {
                        takeCrop(data.getData());
                    }
                    break;
                case 3:
                    if (cropFile != null) {
                        checkPermission();
                        String imagePath = cropFile.getAbsolutePath();
                        Log.d("—ZRT—", "imagePath "+imagePath+"------------->>");
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        if (bitmap != null) {
                            Log.d("—ZRT—", "bitmap = " + bitmap + "------------->>");
                        } else {
                            Log.d("—ZRT—", "bitmap为空------------->>");
                        }
                        imgShowDetection.setImageBitmap(bitmap);
                        imgHolder.setVisibility(View.GONE);
                    }
                    break;
            }
        } else {
            Log.i("—ZRT—", resultCode + "------->>");
        }
    }

    //打开系统相册
    private void takeAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    //裁剪
    private void takeCrop(Uri sourceUri) {
        Log.d("—ZRT—", "已执行------------->>");
        cropFile = ChooseImgUtil.createImageFile(true);
        if (cropFile != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);    //X方向上的比例
            intent.putExtra("aspectY", 1);    //Y方向上的比例
            intent.putExtra("outputX", 550);  //裁剪区的宽
            intent.putExtra("outputY", 550); //裁剪区的高
            intent.putExtra("scale ", true);  //是否保留比例
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.setDataAndType(sourceUri, "image/*");  //设置数据源
                Uri imgCropUri = Uri.fromFile(cropFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgCropUri); //设置输出  不需要ContentUri,否则失败
                Log.d("—ZRT—", "sourceUri = " + sourceUri);
                Log.d("—ZRT—", "imgCropUri = " + imgCropUri);
            } else {
                if (imgFile != null) {
                    intent.setDataAndType(Uri.fromFile(imgFile), "image/*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
                }
            }
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CROP);

        }
    }

    // 拍照 + 裁切
    private void gotoCaptureCrop() {
        imgFile = ChooseImgUtil.createImageFile(false);
        checkPermission();
        if (imgFile!=null){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imgUri = FileProvider.getUriForFile(this, AUTHORITY, imgFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
            }
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, REQUEST_CODE_CAPTUR);
        }
    }


    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_WRITE_EXTERNAL_STORAGE);

        }else if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("—ZRT—", "checkPermission: 已经授权！");
        }
    }

}
