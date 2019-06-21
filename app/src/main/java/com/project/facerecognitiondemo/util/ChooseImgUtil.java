package com.project.facerecognitiondemo.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/21 11:39
 * Description: It's use to ...
 */
public class ChooseImgUtil {

    public static String FolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CameraCache";

    public static File createImageFile(boolean isCorp){
        Log.d("—ZRT—","FolderPath = "+FolderPath+"------->>");
        File rootFile = new File(FolderPath + File.separator +"capture");
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "";
        if (isCorp){
            fileName = "IMG_"+timeStamp+"CROP.jpg";
        }else {
            fileName = "IMG_"+timeStamp+".jpg";
        }
        File file = new File(rootFile.getAbsoluteFile() + File.separator + fileName);
        return file;
    }

    private static File createCameraFile(String folderName) {
        File rootFile = new File(FolderPath + File.separator + folderName);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";
        File file = new File(rootFile.getAbsoluteFile() + File.separator + fileName);
        return file;
    }

}
