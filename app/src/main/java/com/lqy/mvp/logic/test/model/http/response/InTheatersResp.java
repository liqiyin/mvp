package com.lqy.mvp.logic.test.model.http.response;

import com.google.gson.annotations.SerializedName;
import com.lqy.mvp.api.BaseResponse;

import java.util.List;

/**
 * Created by slam.li on 2017/3/20.
 * 测试：上映电影响应
 */

public class InTheatersResp extends BaseResponse {

    public int count;
    public int start;
    public int total;
    public String title;
    public List<SubjectsBean> subjects;

    public static class SubjectsBean {
        public RatingBean rating;
        public String title;
        public int collect_count;
        public String mainland_pubdate;
        public boolean has_video;
        public String original_title;
        public String subtype;
        public String year;
        public ImagesBean images;
        public String alt;
        public String id;
        public List<String> genres;
        public List<CastsBean> casts;
        public List<String> durations;
        public List<DirectorsBean> directors;
        public List<String> pubdates;

        public static class RatingBean {
            public int max;
            public double average;
            public DetailsBean details;
            public String stars;
            public int min;

            public static class DetailsBean {
                @SerializedName("1")
                public int _$1;
                @SerializedName("2")
                public int _$2;
                @SerializedName("3")
                public int _$3;
                @SerializedName("4")
                public int _$4;
                @SerializedName("5")
                public int _$5;
            }
        }

        public static class ImagesBean {
            public String small;
            public String large;
            public String medium;
        }

        public static class CastsBean {
            public ImagesBean avatars;
            public String name_en;
            public String name;
            public String alt;
            public String id;
        }

        public static class DirectorsBean {
            public ImagesBean avatars;
            public String name_en;
            public String name;
            public String alt;
            public String id;
        }
    }
}
