package com.wyh.happyyousdk.model.response.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetQuickReadResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public GetQuickReadResponse(String msg, Boolean success, Data data) {
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

        @SerializedName("trendingBlogs")
        @Expose
        private List<TrendingBlog> trendingBlogs = null;
        @SerializedName("blogs")
        @Expose
        private List<Blog> blogs = null;
        @SerializedName("mostRead")
        @Expose
        private List<MostRead> mostRead = null;
        @SerializedName("tribeBlogs")
        @Expose
        private List<TribeBlog> tribeBlogs = null;
        @SerializedName("bookMarkBlogs")
        @Expose
        private List<TribeBlog> bookMarkBlogs = null;
        @SerializedName("tribeVideos")
        @Expose
        private List<GetDashboardDataResponse.Data.HealthTv> tribeVideos = null;

        @SerializedName("allBlogs")
        @Expose
        private List<AllBlogs> allBlogs = null;

        public Data() {
        }

        public Data(List<TrendingBlog> trendingBlogs, List<Blog> blogs, List<MostRead> mostRead, List<TribeBlog> tribeBlogs, List<TribeBlog> bookMarkBlogs, List<GetDashboardDataResponse.Data.HealthTv> tribeVideos) {
            super();
            this.trendingBlogs = trendingBlogs;
            this.blogs = blogs;
            this.mostRead = mostRead;
            this.tribeBlogs = tribeBlogs;
            this.bookMarkBlogs = bookMarkBlogs;
            this.tribeVideos = tribeVideos;
        }

        public List<TrendingBlog> getTrendingBlogs() {
            return trendingBlogs;
        }

        public void setTrendingBlogs(List<TrendingBlog> trendingBlogs) {
            this.trendingBlogs = trendingBlogs;
        }

        public List<Blog> getBlogs() {
            return blogs;
        }

        public void setBlogs(List<Blog> blogs) {
            this.blogs = blogs;
        }

        public List<MostRead> getMostRead() {
            return mostRead;
        }

        public void setMostRead(List<MostRead> mostRead) {
            this.mostRead = mostRead;
        }

        public List<TribeBlog> getTribeBlogs() {
            return tribeBlogs;
        }

        public void setTribeBlogs(List<TribeBlog> tribeBlogs) {
            this.tribeBlogs = tribeBlogs;
        }

        public List<TribeBlog> getBookMarkBlogs() {
            return bookMarkBlogs;
        }

        public void setBookMarkBlogs(List<TribeBlog> bookMarkBlogs) {
            this.bookMarkBlogs = bookMarkBlogs;
        }

        public List<AllBlogs> getAllBlogs() {
            return allBlogs;
        }



        public List<GetDashboardDataResponse.Data.HealthTv> getTribeVideos() {
            return tribeVideos;
        }

        public void setTribeVideos(List<GetDashboardDataResponse.Data.HealthTv> tribeVideos) {
            this.tribeVideos = tribeVideos;
        }

        public class MostRead {

            @SerializedName("ArticleID")
            @Expose
            private Integer articleID;
            @SerializedName("ArticleName")
            @Expose
            private String articleName;
            @SerializedName("Tags")
            @Expose
            private String tags;
            @SerializedName("ArticlePath")
            @Expose
            private String articlePath;
            @SerializedName("ArticleCode")
            @Expose
            private String articleCode;
            @SerializedName("ImgPath")
            @Expose
            private String imgPath;
            @SerializedName("IsBookMarked")
            @Expose
            public int isBookMarked;

            /**
             * No args constructor for use in serialization
             *
             */
            public MostRead() {
            }

            /**
             *
             * @param articleName
             * @param imgPath
             * @param articleID
             * @param articlePath
             * @param articleCode
             * @param tags
             */
            public MostRead(Integer articleID, String articleName, String tags, String articlePath, String articleCode, String imgPath) {
                super();
                this.articleID = articleID;
                this.articleName = articleName;
                this.tags = tags;
                this.articlePath = articlePath;
                this.articleCode = articleCode;
                this.imgPath = imgPath;
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

            public int getIsBookMarked() {
                return isBookMarked;
            }

            public void setIsBookMarked(int isBookMarked) {
                this.isBookMarked = isBookMarked;
            }

        }
        public class TrendingBlog {

            @SerializedName("ArticleID")
            @Expose
            private Integer articleID;
            @SerializedName("ArticleName")
            @Expose
            private String articleName;
            @SerializedName("Tags")
            @Expose
            private String tags;
            @SerializedName("ArticlePath")
            @Expose
            private String articlePath;
            @SerializedName("ArticleCode")
            @Expose
            private String articleCode;
            @SerializedName("ImgPath")
            @Expose
            private String imgPath;
            @SerializedName("IsBookMarked")
            @Expose
            public int isBookMarked;

            /**
             * No args constructor for use in serialization
             *
             */
            public TrendingBlog() {
            }

            /**
             *
             * @param articleName
             * @param imgPath
             * @param articleID
             * @param articlePath
             * @param articleCode
             * @param tags
             */
            public TrendingBlog(Integer articleID, String articleName, String tags, String articlePath, String articleCode, String imgPath) {
                super();
                this.articleID = articleID;
                this.articleName = articleName;
                this.tags = tags;
                this.articlePath = articlePath;
                this.articleCode = articleCode;
                this.imgPath = imgPath;
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

            public int getIsBookMarked() {
                return isBookMarked;
            }

            public void setIsBookMarked(int isBookMarked) {
                this.isBookMarked = isBookMarked;
            }

        }
        public class TribeBlog {

            @SerializedName("ArticleID")
            @Expose
            private Integer articleID;
            @SerializedName("ArticleName")
            @Expose
            private String articleName;
            @SerializedName("Tags")
            @Expose
            private String tags;
            @SerializedName("ArticlePath")
            @Expose
            private String articlePath;
            @SerializedName("ArticleCode")
            @Expose
            private String articleCode;
            @SerializedName("ImgPath")
            @Expose
            private String imgPath;
            @SerializedName("IsBookMarked")
            @Expose
            public int isBookMarked;

            /**
             * No args constructor for use in serialization
             *
             */
            public TribeBlog() {
            }

            /**
             *
             * @param articleName
             * @param imgPath
             * @param articleID
             * @param articlePath
             * @param articleCode
             * @param tags
             */
            public TribeBlog(Integer articleID, String articleName, String tags, String articlePath, String articleCode, String imgPath) {
                super();
                this.articleID = articleID;
                this.articleName = articleName;
                this.tags = tags;
                this.articlePath = articlePath;
                this.articleCode = articleCode;
                this.imgPath = imgPath;
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

            public int getIsBookMarked() {
                return isBookMarked;
            }

            public void setIsBookMarked(int isBookMarked) {
                this.isBookMarked = isBookMarked;
            }

        }
        public class Blog {

            @SerializedName("ArticleID")
            @Expose
            private Integer articleID;
            @SerializedName("ArticleName")
            @Expose
            private String articleName;
            @SerializedName("ArticlePath")
            @Expose
            private String articlePath;
            @SerializedName("ArticleCode")
            @Expose
            private String articleCode;
            @SerializedName("ImgPath")
            @Expose
            private String imgPath;
            @SerializedName("IsBookMarked")
            @Expose
            public int isBookMarked;

            /**
             * No args constructor for use in serialization
             *
             */
            public Blog() {
            }

            /**
             *
             * @param articleName
             * @param imgPath
             * @param articleID
             * @param articlePath
             * @param articleCode
             */
            public Blog(Integer articleID, String articleName, String articlePath, String articleCode, String imgPath) {
                super();
                this.articleID = articleID;
                this.articleName = articleName;
                this.articlePath = articlePath;
                this.articleCode = articleCode;
                this.imgPath = imgPath;
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

            public String getArticlePath() {
                return articlePath;
            }

            public void setArticlePath(String articlePath) {
                this.articlePath = articlePath;
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

            public int getIsBookMarked() {
                return isBookMarked;
            }

            public void setIsBookMarked(int isBookMarked) {
                this.isBookMarked = isBookMarked;
            }

        }

        public class TribeVideos {

            @SerializedName("VideoName")
            @Expose
            private String videoName;
            @SerializedName("VideoUrl")
            @Expose
            private String videoUrl;
            @SerializedName("Description")
            @Expose
            private String description;
            @SerializedName("ThumbnailImage")
            @Expose
            private String thumbnailImage;

            public TribeVideos() {
            }

            public TribeVideos(String videoName, String videoUrl, String description, String thumbnailImage) {
                this.videoName = videoName;
                this.videoUrl = videoUrl;
                this.description = description;
                this.thumbnailImage = thumbnailImage;
            }

            public String getVideoName() {
                return videoName;
            }

            public void setVideoName(String videoName) {
                this.videoName = videoName;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getThumbnailImage() {
                return thumbnailImage;
            }

            public void setThumbnailImage(String thumbnailImage) {
                this.thumbnailImage = thumbnailImage;
            }
        }

        public class AllBlogs {

            @SerializedName("ArticleID")
            @Expose
            private Integer ArticleID;

            @SerializedName("ArticleName")
            @Expose
            private String ArticleName;

            @SerializedName("ArticlePath")
            @Expose
            private String ArticlePath;

            @SerializedName("ArticleCode")
            @Expose
            private String ArticleCode;

            @SerializedName("ImgPath")
            @Expose
            private String ImgPath;

            @SerializedName("IsBookMarked")
            @Expose
            private Integer IsBookMarked;


            public Integer getArticleID() {
                return ArticleID;
            }

            public String getArticleName() {
                return ArticleName;
            }

            public String getArticlePath() {
                return ArticlePath;
            }

            public String getArticleCode() {
                return ArticleCode;
            }

            public String getImgPath() {
                return ImgPath;
            }

            public Integer getIsBookMarked() {
                return IsBookMarked;
            }
        }

    }


}
