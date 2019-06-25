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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/14 17:05
 * Description: It's use to ...
 */
public class ApiMethods {
    /**
     * 封装线程管理和订阅的过程
     */
    public static void ApiSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void getDetectImageResult(Observer<DetectImagBean> observer, HashMap<String,RequestBody> detectMap){
        ApiSubscribe(Api.getApiService().getDetectResult(detectMap),observer);
    }

    public static void getBeautifulImageResult(Observer<BeautifulImgBean> observer, HashMap<String,RequestBody> beautifulMap){
        ApiSubscribe(Api.getApiService().getBeautifulResult(beautifulMap),observer);
    }

    public static void getCompareImageResult(Observer<CompareImgBean> observer, HashMap<String, RequestBody> compareMap){
        ApiSubscribe(Api.getApiService().getCompareResult(compareMap),observer);
    }

    public static void getSearchImageResult(Observer<SearchImgBean> observer, HashMap<String, RequestBody> searchMap){
        ApiSubscribe(Api.getApiService().getSearchResult(searchMap),observer);
    }

    public static void getCreateFaceSetResult(Observer<CreateFaceSetBean> observer, HashMap<String, RequestBody> createMap){
        ApiSubscribe(Api.getApiService().getCreateFaceSetResult(createMap),observer);
    }

    public static void getQueryFaceSetResult(Observer<QueryFaceSetBean> observer, HashMap<String, RequestBody> queryMap){
        ApiSubscribe(Api.getApiService().getQueryFaceSetResult(queryMap),observer);
    }

    public static void getDeleteFaceSetResult(Observer<DeleteFaceSetBean> observer, HashMap<String, RequestBody> deleteMap){
        ApiSubscribe(Api.getApiService().getDeleteFaceSetResult(deleteMap),observer);
    }
}
