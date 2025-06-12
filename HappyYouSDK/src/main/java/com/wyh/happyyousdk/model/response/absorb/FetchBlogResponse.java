package com.wyh.happyyousdk.model.response.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchBlogResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public FetchBlogResponse(String msg, Boolean success, Data data) {
        super();
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("articleID")
        @Expose
        private Integer articleID;
        @SerializedName("articleName")
        @Expose
        private String articleName;
        @SerializedName("tags")
        @Expose
        private String tags;
        @SerializedName("articlePath")
        @Expose
        private String articlePath;
        @SerializedName("active")
        @Expose
        private Integer active;
        @SerializedName("blogBitly")
        @Expose
        private Object blogBitly;
        @SerializedName("articleCode")
        @Expose
        private String articleCode;
        @SerializedName("imgPath")
        @Expose
        private String imgPath;
        @SerializedName("htmlContent")
        @Expose
        private String htmlContent;
        @SerializedName("isBookMarked")
        @Expose
        public Boolean isBookMarked;

        /**
         * No args constructor for use in serialization
         */
        public Data() {
        }

        /**
         * @param articleName
         * @param blogBitly
         * @param imgPath
         * @param articleID
         * @param articlePath
         * @param active
         * @param articleCode
         * @param tags
         * @param htmlContent
         */
        public Data(Integer articleID, String articleName, String tags, String articlePath, Integer active, Object blogBitly, String articleCode, String imgPath, String htmlContent, Boolean isBookMarked) {
            super();
            this.articleID = articleID;
            this.articleName = articleName;
            this.tags = tags;
            this.articlePath = articlePath;
            this.active = active;
            this.blogBitly = blogBitly;
            this.articleCode = articleCode;
            this.imgPath = imgPath;
            this.htmlContent = htmlContent;
            this.isBookMarked = isBookMarked;
        }

        public Integer getArticleID() {
            return articleID;
        }

        public void setArticleID(Integer articleID) {
            this.articleID = articleID;
        }

        public String getArticleName() {
            return articleName;
        }

        public void setArticleName(String articleName) {
            this.articleName = articleName;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getArticlePath() {
            return articlePath;
        }

        public void setArticlePath(String articlePath) {
            this.articlePath = articlePath;
        }

        public Integer getActive() {
            return active;
        }

        public void setActive(Integer active) {
            this.active = active;
        }

        public Object getBlogBitly() {
            return blogBitly;
        }

        public void setBlogBitly(Object blogBitly) {
            this.blogBitly = blogBitly;
        }

        public String getArticleCode() {
            return articleCode;
        }

        public void setArticleCode(String articleCode) {
            this.articleCode = articleCode;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getHtmlContent() {
            return htmlContent;
        }

        public void setHtmlContent(String htmlContent) {
            this.htmlContent = htmlContent;
        }

        public Boolean getIsBookMarked() {
            return isBookMarked;
        }

        public void setIsBookMarked(Boolean isBookMarked) {
            this.isBookMarked = isBookMarked;
        }
    }


}
