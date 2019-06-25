package com.project.facerecognitiondemo.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/18 10:51
 * Description: It's use to ...
 */
public class AccessibilityAPIsUtil {

    private static String api_key = "QdDi-tndO4cmK1q8psfxNfPlheBdLB2C";
    private static String api_secret = "ZRHkswx29qdtYF9yz4qgbioiZY4iu7wg";
    private static HashMap<String, Object> queryMap = new HashMap<>();
    private static String TAG = "—ZRT—";
    private static String INFO = "------->>";

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 通过传入图片路径处理人脸检测和人脸分析数据
     *
     * @param imgPath    图片路径
     * @param landmark   是否检测并返回人脸关键点（默认为0）
     *                   0         不返回
     *                   1         返回83个关键点
     *                   2         返回106个关键点
     * @param attributes 检测人脸属性特征的集合
     *                   gender            性别
     *                   age               年龄分析结果
     *                   smiling           笑容分析结果
     *                   eyestatus         眼睛状态信息
     *                   emotion           情绪识别结果
     *                   ethnicity         人种分析结果
     *                   beauty            颜值识别结果
     *                   skinstatus        面部特征识别结果
     * @return HashMap<String,RequestBody>     可直接用于请求的hashMap
     * @throws NullPointerException 某一数据为空
     */
    public static HashMap<String, RequestBody> detectImg(String imgPath, int landmark, List<String> attributes, boolean isBeautyGrade) {
        String s, attr = "";
        //处理文件
        s = dealPath(imgPath);
        if (TextUtils.isEmpty(s) || attributes.size() == 0 || landmark < 0 || landmark > 2 ) {
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        for (int i = 0; i < attributes.size(); i++) {
            if (i == attributes.size() - 1) {
                attr = attr + attributes.get(i);
            } else {
                attr = attr + attributes.get(i) + ",";
            }
        }
        Log.d(TAG, "attr = " + attr + INFO);
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("image_base64", s);
        queryMap.put("return_landmark", landmark);
        queryMap.put("return_attributes", attr);
        if (isBeautyGrade) {
            queryMap.put("beauty_score_min", 0);
            queryMap.put("beauty_score_max", 100);
        }
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 通过传入图片路径处理人脸检测和人脸分析数据
     *
     * @param imgPath    图片路径
     * @return HashMap<String,RequestBody>     可直接用于请求的hashMap
     * @throws NullPointerException 某一数据为空
     */
    public static HashMap<String, RequestBody> detectImgForFaceToken(String imgPath) {
        String s, attr = "";
        //处理文件
        s = dealPath(imgPath);
        if (TextUtils.isEmpty(s) ) {
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        Log.d(TAG, "attr = " + attr + INFO);
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("image_base64", s);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 通过路径处理两个人脸进行比对的数据
     *
     * @param imgPath1 第一张图片的路径
     * @param imgPath2 第二张图片的路径
     * @return HashMap<String,RequestBody>     可直接用于请求的hashMap
     * @throws NullPointerException 某一数据为空
     */
    public static HashMap<String, RequestBody> compareImgByPath(String imgPath1, String imgPath2) {
        String img1, img2;
        img1 = dealPath(imgPath1);
        img2 = dealPath(imgPath2);
        if (TextUtils.isEmpty(img1) || TextUtils.isEmpty(img2)) {
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("image_base64_1", img1);
        queryMap.put("image_base64_2", img2);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 通过face_token处理两个人脸进行比对的数据
     *
     * @param faceToken1 第一张图片的路径
     * @param faceToken2 第二张图片的路径
     * @return HashMap<String,RequestBody>     可直接用于请求的hashMap
     * @throws NullPointerException 某一数据为空
     */
    public static HashMap<String, RequestBody> compareImgByFaceToken(String faceToken1, String faceToken2) {

        if (TextUtils.isEmpty(faceToken1) || TextUtils.isEmpty(faceToken2) ) {
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("face_token1", faceToken1);
        queryMap.put("face_token2", faceToken2);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 通过face_token处理两个人脸进行比对的数据
     *
     * @param path1 第一张图片的路径
     * @param faceToken2 第二张图片的faceToken
     * @return HashMap<String,RequestBody>     可直接用于请求的hashMap
     * @throws NullPointerException 某一数据为空
     */
    public static HashMap<String, RequestBody> compareImgByFaceTokenAndPath(String path1, String faceToken2) {

        String s = dealPath(path1);
        if (TextUtils.isEmpty(s)|| TextUtils.isEmpty(faceToken2)) {
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("image_base64_1", s);
        queryMap.put("face_token2", faceToken2);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 通过path处理在一个已有的 FaceSet 中找出与目标人脸最相似的一张或多张人脸的数据
     *
     * @param imgPath      图片路径
     * @param facesetToken 用来搜索的 FaceSet 的标识
     * @param size         控制返回比对置信度最高的结果的数量[1-5](默认值为1)
     * @throws NullPointerException 某一数据为空
     */
    public static HashMap<String, RequestBody> searchImgByPath(String imgPath, String facesetToken, int size) {
        String s;
        //处理文件
        s = dealPath(imgPath);
        if (s == null || facesetToken == null || size <= 0 || size > 5) {
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("image_base64", s);
        queryMap.put("faceset_token", facesetToken);
        queryMap.put("return_result_count", size);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 通过path处理对图片进行美颜和美白的数据
     *
     * @param imgPath   图片路径
     * @param whitening 美白程度，取值范围[0,100](本参数默认值为 100)
     * @param smoothing 磨皮程度，取值范围[0,100](本参数默认值为 100)
     * @throws NullPointerException 某一数据为空
     */
    public static HashMap<String, RequestBody> beautifulImgByFaceToken(String imgPath, int whitening, int smoothing) {
        String s;
        //处理文件
        s = dealPath(imgPath);
        if (s == null || whitening <= 0 || whitening > 100 || smoothing <= 0 || smoothing > 100) {
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("image_base64", s);
        queryMap.put("whitening", whitening);
        queryMap.put("smoothing", smoothing);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description:处理获取 API Key 下的 FaceSet 列表及其 faceset_token、outer_id、display_name 和 tags 等信息的数据
     *
     */
    public static HashMap<String, RequestBody> queryAllFaceSets(String tags) {
        if (TextUtils.isEmpty(tags)){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("tags",tags);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description:处理获取 API Key 下的 FaceSet 列表及其 faceset_token、outer_id、display_name 和 tags 等信息的数据
     *
     * @param facesetToken   faceSet标识
     */
    public static HashMap<String, RequestBody> queryInfoFromFaceSets(String facesetToken) {
        if (TextUtils.isEmpty(facesetToken)){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("faceset_token", facesetToken);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description:处理获取 API Key 下的 FaceSet 列表及其 faceset_token、outer_id、display_name 和 tags 等信息的数据
     *
     * @param facesetToken   faceSet标识
     * @param displayName    faceSet名字
     */
    public static HashMap<String, RequestBody> queryInfoFromFaceSets(String facesetToken,String displayName) {
        if (TextUtils.isEmpty(facesetToken)||TextUtils.isEmpty(displayName)){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("faceset_token", facesetToken);
        queryMap.put("display_name", displayName);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description:处理移除一个FaceSet中的某些或者全部face_token的数据
     *
     * @param facesetToken   faceSet标识
     * @param faceToken    faceToken  需要移除的人脸标识
     *        “RemoveAllFaceTokens“     移除全部
     */
    public static HashMap<String, RequestBody> deleteOneFaceTokenFromFaceSets(String facesetToken,String faceToken) {
        if (TextUtils.isEmpty(facesetToken) || TextUtils.isEmpty(faceToken)){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("faceset_token", facesetToken);
        queryMap.put("face_tokens", faceToken);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description:处理移除一个FaceSet中的某些或者全部face_token的数据
     *
     * @param facesetToken   faceSet标识
     * @param faceTokens    faceToken  需要移除的人脸标识
     *        “RemoveAllFaceTokens“     移除全部
     */
    public static HashMap<String, RequestBody> deleteSomeFaceTokenFromFaceSets(String facesetToken,List<String> faceTokens) {
        String s = "";
        if (TextUtils.isEmpty(facesetToken) || faceTokens.size()==0){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        for (int i = 0; i < faceTokens.size(); i++) {
            if (i == faceTokens.size() - 1) {
                s = s + faceTokens.get(i);
            } else {
                s = s + faceTokens.get(i) + ",";
            }
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("faceset_token", facesetToken);
        queryMap.put("face_tokens", s);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description:为一个已经创建的 FaceSet 添加人脸标识 face_token 的数据
     *
     * @param facesetToken   faceSet标识
     * @param faceTokens    faceToken  需要移除的人脸标识
     *        最多不超过5个face_token
     */
    public static HashMap<String, RequestBody> addFaceTokenToFaceSets(String facesetToken,List<String> faceTokens) {
        String s = "";
        if (TextUtils.isEmpty(facesetToken) || faceTokens.size()==0 || faceTokens.size()>5){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        for (int i = 0; i < faceTokens.size(); i++) {
            if (i == faceTokens.size() - 1) {
                s = s + faceTokens.get(i);
            } else {
                s = s + faceTokens.get(i) + ",";
            }
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("faceset_token", facesetToken);
        queryMap.put("face_tokens", s);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description:创建一个人脸的集合 FaceSet，用于存储人脸标识 face_token 的数据
     *
     * @param displayName   faceSet名称
     * @param tags          faceSet标识（与名称保持一致）
     * @param faceTokens    faceToken  需要添加的人脸标识
     *        最多不超过5个face_token
     *
     */
    public static HashMap<String, RequestBody> createFaceSets(String displayName,String tags,List<String> faceTokens) {
        String s = "";
        if (faceTokens.size()==0 || faceTokens.size()>5 || TextUtils.isEmpty(tags)||TextUtils.isEmpty(displayName)){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        for (int i = 0; i < faceTokens.size(); i++) {
            if (i == faceTokens.size() - 1) {
                s = s + faceTokens.get(i);
            } else {
                s = s + faceTokens.get(i) + ",";
            }
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("face_tokens", s);
        queryMap.put("display_name", displayName);
        queryMap.put("tags",tags);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }

    /** Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 删除一个人脸的集合 FaceSet的数据
     *
     * @param faceSetToken   faceSet标识
     *
     */
    public static HashMap<String, RequestBody> removeFaceSet(String faceSetToken) {
        if (TextUtils.isEmpty(faceSetToken)){
            Log.d(TAG, "ERROR_请排查参数" + INFO);
            return null;
        }
        queryMap.put("api_key", api_key);
        queryMap.put("api_secret", api_secret);
        queryMap.put("faceset_token", faceSetToken);
        queryMap.put("check_empty", 0);
        HashMap<String, RequestBody> stringRequestBodyHashMap = generateRequestBody(queryMap);
        return stringRequestBodyHashMap;
    }


    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 进行HashMap<String,Object>与HashMap<String, RequestBody>转换
     *
     * @param requestDataMap 数据集合
     * @return HashMap<String,RequestBody>     可直接用于请求的hashMap
     */
    private static HashMap<String, RequestBody> generateRequestBody(HashMap<String, Object> requestDataMap) {
        HashMap<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requestDataMap.keySet()) {
            String requestKey = "";
            if (requestDataMap.get(key) == null) {
                requestKey = "";
            } else {
                requestKey = String.valueOf(requestDataMap.get(key));
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), requestKey);
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }

    /**
     * Author: ZRT
     * Email: zhuruotong@jeejio.com
     * Date:
     * Description: 进行base64转换
     *
     * @param imgPath 图片路径
     * @return s       base64二进制码
     */
    private static String dealPath(String imgPath) {
        String s = "";
        //判断文件是否存在
        boolean exsit = FileUtil.isExsit(imgPath);
        if (!exsit) {
            Log.d(TAG, "文件不存在" + INFO);
            return null;
        }
        //将文件转换为Base64格式
        try {
            s = FileUtil.encodeBase64File(imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(s)) {
            Log.d(TAG, "Base64转码出错" + INFO);
            return null;
        }
        return s;
    }

}
