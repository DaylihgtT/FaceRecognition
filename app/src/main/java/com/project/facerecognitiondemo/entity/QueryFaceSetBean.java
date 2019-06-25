package com.project.facerecognitiondemo.entity;

import java.util.List;

/**
 * Author: ZRT
 * Email: zhuruotong@jeejio.com
 * Date: 2019/6/18 10:06
 * Description: It's use to ...
 */
public class QueryFaceSetBean {

    /**
     * time_used : 18
     * facesets : [{"faceset_token":"c0096aad1e5b76a8653f5bfb12ac997d","outer_id":null,"display_name":"","tags":""},{"faceset_token":"d203a107c943d47bb0efbb25a16cf84b","outer_id":null,"display_name":"","tags":""}]
     * request_id : 1470478468,e288a50c-6606-490f-8c7d-9cf68f6ea496
     */

    private int time_used;
    private String request_id;
    private List<FacesetsBean> facesets;

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public List<FacesetsBean> getFacesets() {
        return facesets;
    }

    public void setFacesets(List<FacesetsBean> facesets) {
        this.facesets = facesets;
    }

    public static class FacesetsBean {
        /**
         * faceset_token : c0096aad1e5b76a8653f5bfb12ac997d
         * outer_id : null
         * display_name :
         * tags :
         */

        private String faceset_token;
        private Object outer_id;
        private String display_name;
        private String tags;

        public String getFaceset_token() {
            return faceset_token;
        }

        public void setFaceset_token(String faceset_token) {
            this.faceset_token = faceset_token;
        }

        public Object getOuter_id() {
            return outer_id;
        }

        public void setOuter_id(Object outer_id) {
            this.outer_id = outer_id;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
    }
}
