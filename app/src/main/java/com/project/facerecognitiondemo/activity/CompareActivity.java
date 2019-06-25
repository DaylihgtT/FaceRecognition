package com.project.facerecognitiondemo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.project.facerecognitiondemo.R;
import com.project.facerecognitiondemo.entity.CompareImgBean;
import com.project.facerecognitiondemo.entity.CreateFaceSetBean;
import com.project.facerecognitiondemo.entity.DetectImagBean;
import com.project.facerecognitiondemo.entity.QueryFaceSetBean;
import com.project.facerecognitiondemo.net.ApiMethods;
import com.project.facerecognitiondemo.net.MyObserver;
import com.project.facerecognitiondemo.net.ObserverOnNextListener;
import com.project.facerecognitiondemo.util.AccessibilityAPIsUtil;
import com.project.facerecognitiondemo.util.ChooseImgUtil;
import com.project.facerecognitiondemo.util.ImageFormatConversionUtil;
import com.project.facerecognitiondemo.util.SharedPrefrenceUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public class CompareActivity extends AppCompatActivity {

    private ImageView imgBack;
    private Button btnCompareFaceBegin;
    private TextView textHelpContent;
    private ImageView imgHelpBack;
    private Uri imgUri = null;              //拍照后保存照片的uri
    private File imgFile = null;            //拍照后保存的照片
    private File cropFile = null;
    private static String AUTHORITY = "com.project.facedemo.fileProvider";      //FileProvider的签名
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static int REQUEST_CODE_CAPTUR = 1;
    private static int REQUEST_CODE_CAPTURE_CROP = 2;
    private ImageView imgHelp;
    private boolean isInputFace;
    private RelativeLayout relativeLayoutProgress;
    private TextView relativeLayoutProgressHintText;
    private HashMap<String, RequestBody> stringRequestBodyHashMap = null;
    private static final String SP_SAVE_FACE_SET_NAME = "user_face_info";
    private static final String SP_SAVE_FACE_TOKEN_NAME = "face_token_save";


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        getWindow().setEnterTransition(new Explode().setDuration(2000));
        getWindow().setExitTransition(new Explode().setDuration(2000));
        SharedPrefrenceUtils.put("isInputFace",false);
        initView();
        initListener();
        checkPermission();
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                onBackPressed();
            }
        });
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到帮助界面
                setContentView(R.layout.activity_compare_help);
                initHelp();
            }
        });
        btnCompareFaceBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统相机
                gotoCaptureCrop();
            }
        });
    }

    private void initHelp() {
        initHelpView();
        initHelpListener();
    }

    private void initHelpView() {
        imgHelpBack = findViewById(R.id.img_help_back);
        textHelpContent = findViewById(R.id.text_help_content);
        textHelpContent.setText("点击 “开始” 按钮后，系统将会为您跳转到相机界面，通过拍照可以将您的正面照片返回给我们，经数据分析我们将会存储您的面部信息，下一次检测时，我们将会根据您本次录入的信息进行比对，验证您的身份。");
    }

    private void initHelpListener() {
        imgHelpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        imgHelp = findViewById(R.id.img_help);
        btnCompareFaceBegin = findViewById(R.id.btn_compare_face_begin);
        relativeLayoutProgress = findViewById(R.id.relative_layout_progress);
        relativeLayoutProgressHintText = findViewById(R.id.relative_progress_hint_text);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
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
        if (imgFile != null) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Log.d("—ZRT-", "已执行----------->>");
                    if (imgFile != null) {
                        Uri sourceUri = FileProvider.getUriForFile(this, AUTHORITY, imgFile); //通过FileProvider创建一个content类型的Uri
                        takeCrop(sourceUri);
                    }
                    break;
                case 2:
                    if (cropFile != null) {
                        String imagePath = cropFile.getAbsolutePath();
                        Log.d("—ZRT—", "imagePath " + imagePath + "------------->>");
                        dealFaceInfo(cropFile);
                    } else {
                        Log.d("—ZRT—", "imagePath为空------------->>");
                    }
                    break;
            }
        } else {
            Log.i("—ZRT—", "resultCode = " + resultCode + "------->>");
        }
    }

    private void dealFaceInfo(File cropFile) {

        boolean isInputFace;

        //设置progressBar
        ProgressBar progressBar = findViewById(R.id.spin_kit_view);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        //获取isInputFace的值
        isInputFace = (boolean) SharedPrefrenceUtils.getSharedPreference("isInputFace", false);
        //判断是否已录入人脸数据
        if (isInputFace) {
            //显示弹框直至检测结束
            relativeLayoutProgressHintText.setText("人脸检测中，请稍后");
            relativeLayoutProgress.setVisibility(View.VISIBLE);
            //对比两张人脸数据是否为同一人
            getCompareResult();
        } else {
            relativeLayoutProgressHintText.setText("人脸录入中，请稍后");
            relativeLayoutProgress.setVisibility(View.VISIBLE);
            //根据DetectApi获得文件路径的face_token
            getDetectFaceToken();
            createFaceSet();
            /*startActivity(new Intent(this,AddMoreCompareInfoActivity.class));*/
        }
    }

    private void createFaceSet() {
        if (!SharedPrefrenceUtils.contain(SP_SAVE_FACE_TOKEN_NAME)) {
            Log.d("—ZRT—", "录入信息有误------->>");
            return;
        }
        //从sp中取出存入的facetoken
        String faceToken = (String) SharedPrefrenceUtils.getSharedPreference(SP_SAVE_FACE_TOKEN_NAME, "");

        //创建faceSet并存入数据
        final List<String> faceTokenList = new ArrayList<>();
        faceTokenList.add(faceToken);
        stringRequestBodyHashMap = AccessibilityAPIsUtil.createFaceSets(SP_SAVE_FACE_SET_NAME, SP_SAVE_FACE_SET_NAME, faceTokenList);
        ObserverOnNextListener<CreateFaceSetBean> getCreateFaceSetListener = new ObserverOnNextListener<CreateFaceSetBean>() {
            @Override
            public void onNext(CreateFaceSetBean createFaceSetBean) {
                //创建FaceSet并存入
                int face_added = createFaceSetBean.getFace_added();
                if (face_added > 0) {
                    Log.d("—ZRT—", "faceSet创建成功");
                    relativeLayoutProgress.setVisibility(View.GONE);
                    Toast.makeText(CompareActivity.this, "人脸信息录入成功!", Toast.LENGTH_SHORT).show();
                    SharedPrefrenceUtils.put("isInputFace", true);
                } else {
                    String reason = createFaceSetBean.getFailure_detail().get(0).getReason();
                    Log.d("—ZRT—", "createFaceSet ERROR " + reason + "---------->>");
                }
                startActivity(new Intent(CompareActivity.this,AddMoreCompareInfoActivity.class));
            }
        };
        ApiMethods.getCreateFaceSetResult(new MyObserver<CreateFaceSetBean>(CompareActivity.this, getCreateFaceSetListener), stringRequestBodyHashMap);

    }

    private void getDetectFaceToken() {
        stringRequestBodyHashMap = AccessibilityAPIsUtil.detectImgForFaceToken(cropFile.getAbsolutePath());
        ObserverOnNextListener<DetectImagBean> getFaceTokenlistener = new ObserverOnNextListener<DetectImagBean>() {
            @Override
            public void onNext(DetectImagBean detectImagBean) {
                String face_token = null;
                List<DetectImagBean.FacesBean> faces = detectImagBean.getFaces();
                //判断faces是否为null，如果为null则提示用户，未能检测到人脸数据
                if (faces.size() == 0) {
                    Log.d("—ZRT—", "faces为null--------->>");
                    relativeLayoutProgress.setVisibility(View.GONE);
                    Toast.makeText(CompareActivity.this, "未能检测出人脸数据，请对准面部重试", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < detectImagBean.getFaces().size(); i++) {
                    face_token = detectImagBean.getFaces().get(i).getFace_token();
                    Log.d("—ZRT—", "face_token = " + face_token);
                }
                //将face_token保存
                SharedPrefrenceUtils.put(SP_SAVE_FACE_TOKEN_NAME, face_token);
            }
        };
        ApiMethods.getDetectImageResult(new MyObserver<DetectImagBean>(CompareActivity.this, getFaceTokenlistener), stringRequestBodyHashMap);
    }

    private void getCompareResult() {
        String faceTokenSave = (String) SharedPrefrenceUtils.getSharedPreference(SP_SAVE_FACE_TOKEN_NAME, "");
        stringRequestBodyHashMap = AccessibilityAPIsUtil.compareImgByFaceTokenAndPath(cropFile.getPath(), faceTokenSave);
        ObserverOnNextListener<CompareImgBean> getCompareResultListener = new ObserverOnNextListener<CompareImgBean>() {
            @Override
            public void onNext(CompareImgBean compareImgBean) {
                //获取比较结果
                CompareImgBean.ThresholdsBean thresholds = compareImgBean.getThresholds();
                if (thresholds == null) {
                    Log.d("—ZRT—", "未检测到人脸数据----->>");
                    Toast.makeText(CompareActivity.this, "未能检测出人脸数据，请对准面部重试", Toast.LENGTH_SHORT).show();
                }
                double confidence = compareImgBean.getConfidence();
                if (thresholds.get_$1e5() > 0 && confidence > 50) {
                    Log.d("—ZRT—", "置信度为 ——" + confidence + " ,十万分之一的置信度阈值为 ——" + thresholds.get_$1e5());
                    relativeLayoutProgress.setVisibility(View.GONE);
                    Toast.makeText(CompareActivity.this, "识别成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CompareActivity.this, "识别失败，请对准面部重试", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ApiMethods.getCompareImageResult(new MyObserver<CompareImgBean>(CompareActivity.this, getCompareResultListener), stringRequestBodyHashMap);
    }

    private boolean getQueryFaceSetResult() {
        stringRequestBodyHashMap = AccessibilityAPIsUtil.queryAllFaceSets(SP_SAVE_FACE_SET_NAME);
        ObserverOnNextListener<QueryFaceSetBean> getQueryResultListener = new ObserverOnNextListener<QueryFaceSetBean>() {
            @Override
            public void onNext(QueryFaceSetBean queryFaceSetBean) {
                List<QueryFaceSetBean.FacesetsBean> facesets = queryFaceSetBean.getFacesets();
                for (int i = 0; i < facesets.size(); i++) {
                    Log.d("—ZRT—", "faceSet _ Name = " + facesets.get(i).getDisplay_name() + ",Tag = " + facesets.get(i).getTags() + ",FaceSetToken = " + facesets.get(i).getFaceset_token());
                }
            }
        };
        ApiMethods.getQueryFaceSetResult(new MyObserver<QueryFaceSetBean>(CompareActivity.this, getQueryResultListener), stringRequestBodyHashMap);
        return false;
    }

}
