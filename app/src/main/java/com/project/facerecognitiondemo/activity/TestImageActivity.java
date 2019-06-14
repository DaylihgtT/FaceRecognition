package com.project.facerecognitiondemo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.project.facerecognitiondemo.R;
import com.project.facerecognitiondemo.application.MyApplication;
import com.project.facerecognitiondemo.util.FileUtil;
import com.project.facerecognitiondemo.util.ImageFormatConversionUtil;

import java.io.File;
import java.io.IOException;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class TestImageActivity extends AppCompatActivity {

    private Button mBtnTestImgCompress;
    private String savePathTestOrgImg;          //保存原图的路径
    private String savePathTestCompressImg;     //保存压缩后图片的路径
    private String saveImgCache;                //保存图片缓存路径
    private File compressFile;                 //保存压缩后的文件
    private RelativeLayout relativeLayoutProgress;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);
        getWindow().setEnterTransition(new Explode().setDuration(2000));
        getWindow().setExitTransition(new Explode().setDuration(2000));
        //动态权限申请
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(MyApplication.getmContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_PERMISSION_CODE);
            }
        }
        initView();
        initListener();
        initGlobal();
    }

    private void initView() {
        mBtnTestImgCompress = findViewById(R.id.btn_test_img_compress);
        relativeLayoutProgress = findViewById(R.id.relative_layout_progress);
    }

    private void initGlobal() {
        saveImgCache = "/cache/imgCache";
        //创建缓存的文件夹
        saveImgCache = FileUtil.createFiles(saveImgCache);
        boolean exsit = FileUtil.isExsit(saveImgCache);
        Log.d("—ZRT—",exsit+"---------->>");
        savePathTestOrgImg = "img_test_org.jpg";
        savePathTestCompressImg = "img_test_compress.jpg";
    }

    private void initListener() {

        mBtnTestImgCompress.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //显示弹框直至压缩结束
                relativeLayoutProgress.setVisibility(View.VISIBLE);
                ProgressBar progressBar = findViewById(R.id.spin_kit_view);
                Sprite doubleBounce = new DoubleBounce();
                progressBar.setIndeterminateDrawable(doubleBounce);
                mBtnTestImgCompress.setClickable(false);
                //计算文件大小
                File file = ImageFormatConversionUtil.drawableToFile(TestImageActivity.this,R.drawable.test_img,savePathTestOrgImg,saveImgCache);
                try {
                    long fileSize = FileUtil.getFileSize(file);
                    String s = FileUtil.toFileSize(fileSize);
                    Log.d("—ZRT—","未压缩前测试图片尺寸为------->> " +
                            s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageCompress(file,50,savePathTestCompressImg);
            }
        });
    }

    //压缩图片
    private void imageCompress(File file,int ignoreBy,String targetDir) {
        Luban.with(this)
                .load(file)
                .ignoreBy(ignoreBy)
                .setTargetDir(targetDir)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path)|| path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onStart() {
                        Log.d("—ZRT—","开始压缩--------------->> ");
                    }

                    @Override
                    public void onSuccess(File file) {

                        //压缩成功后调用，返回压缩后的图片文件
                        try {
                            long fileSize = FileUtil.getFileSize(file);
                            String s = FileUtil.toFileSize(fileSize);
                            Log.d("—ZRT—","压缩后测试图片尺寸为------->> " +
                                    s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        compressFile = file;
                        relativeLayoutProgress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "压缩成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //压缩出现问题时调用
                        Log.d("—ZRT—","onError------->> "+e.getMessage()+"");
                        relativeLayoutProgress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "压缩失败", Toast.LENGTH_SHORT).show();
                    }
                }).launch();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length ; i++) {
                Log.d("—ZRT—","申请结果："+grantResults[i]);
            }
        }
    }
}
