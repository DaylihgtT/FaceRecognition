package com.project.facerecognitiondemo.net;

import com.project.facerecognitiondemo.entity.BeautifulImgBean;
import com.project.facerecognitiondemo.entity.CompareImgBean;
import com.project.facerecognitiondemo.entity.CreateFaceSetBean;
import com.project.facerecognitiondemo.entity.DeleteFaceSetBean;
import com.project.facerecognitiondemo.entity.DetectImagBean;
import com.project.facerecognitiondemo.entity.QueryFaceSetBean;
import com.project.facerecognitiondemo.entity.SearchImgBean;
import com.project.facerecognitiondemo.entity.TestDataBean;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/14 15:52
 * Description: It's use to ...
 */
public interface ApiService {

    @Multipart
    @POST("v3/detect")
    Observable<DetectImagBean> getDetectResult(@PartMap HashMap<String, RequestBody> detectMap);

    @Multipart
    @POST("v1/beautify")
    Observable<BeautifulImgBean> getBeautifulResult(@PartMap HashMap<String, RequestBody> beautifulMap);

    @Multipart
    @POST("v3/compare")
    Observable<CompareImgBean> getCompareResult(@PartMap HashMap<String, RequestBody> compareMap);

    @Multipart
    @POST("v3/search")
    Observable<SearchImgBean> getSearchResult(@PartMap HashMap<String, RequestBody> searchMap);

    @Multipart
    @POST("v3/faceset/create")
    Observable<CreateFaceSetBean> getCreateFaceSetResult(@PartMap HashMap<String, RequestBody> createMap);

    @Multipart
    @POST("v3/faceset/getfacesets")
    Observable<QueryFaceSetBean> getQueryFaceSetResult(@PartMap HashMap<String, RequestBody> queryMap);

    @Multipart
    @POST("v3/faceset/delete")
    Observable<DeleteFaceSetBean> getDeleteFaceSetResult(@PartMap HashMap<String, RequestBody> deleteMap);
}
