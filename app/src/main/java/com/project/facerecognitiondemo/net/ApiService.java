package com.project.facerecognitiondemo.net;

import com.project.facerecognitiondemo.entity.TestDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/14 15:52
 * Description: It's use to ...
 */
public interface ApiService {
    @GET("query")
    Observable<TestDataBean> getTopMovie(@Query("key") String key);
}
