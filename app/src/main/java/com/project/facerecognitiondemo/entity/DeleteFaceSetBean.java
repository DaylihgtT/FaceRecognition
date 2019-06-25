package com.project.facerecognitiondemo.entity;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/18 10:31
 * Description: It's use to ...
 */
public class DeleteFaceSetBean {

    /**
     * time_used : 43
     * faceset_token : fe314f398779ce5b1c5787c7fef3f4fd
     * outer_id :
     * request_id : 1470375034,ed492d83-682f-4029-85fa-954b3a661eee
     */

    private int time_used;
    private String faceset_token;
    private String outer_id;
    private String request_id;

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getFaceset_token() {
        return faceset_token;
    }

    public void setFaceset_token(String faceset_token) {
        this.faceset_token = faceset_token;
    }

    public String getOuter_id() {
        return outer_id;
    }

    public void setOuter_id(String outer_id) {
        this.outer_id = outer_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }
}
