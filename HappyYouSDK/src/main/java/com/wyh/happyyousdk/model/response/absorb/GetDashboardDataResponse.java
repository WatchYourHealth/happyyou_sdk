package com.wyh.happyyousdk.model.response.absorb;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDashboardDataResponse implements Serializable {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public GetDashboardDataResponse(String msg, Boolean success, Data data) {
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


    public class Data implements Serializable{

        @SerializedName("tagName")
        @Expose
        private List<TagName> tagName = null;
        @SerializedName("qucikReads")
        @Expose
        private List<QucikRead> qucikReads = null;
        @SerializedName("healthTv")
        @Expose
        private List<HealthTv> healthTv = null;
        @SerializedName("audioFiles")
        @Expose
        private List<AudioFiles> audioFiles = null;
        @SerializedName("webinar")
        @Expose
        private List<Webinar> webinar = null;
        @SerializedName("recommendedVideos")
        @Expose
        private List<HealthTv> recommendedVideos = null;

        @SerializedName("allBlogs")
        @Expose
        private List<QucikRead> allBlogs;

        @SerializedName("allvideos")
        @Expose
        private List<HealthTv> allvideos;


        public Data(List<TagName> tagName, List<QucikRead> qucikReads, List<HealthTv> healthTv, List<HealthTv> recommendedVideos, List<AudioFiles> audioFiles,List<Webinar> webinar) {
            super();
            this.tagName = tagName;
            this.qucikReads = qucikReads;
            this.healthTv = healthTv;
            this.webinar = webinar;
            this.audioFiles = audioFiles;
            this.recommendedVideos = recommendedVideos;
        }


        public List<QucikRead> getAllBlogs() {
            return allBlogs;
        }

        public void setAllBlogs(List<QucikRead> allBlogs) {
            this.allBlogs = allBlogs;
        }

        public List<HealthTv> getAllvideos() {
            return allvideos;
        }

        public void setAllvideos(List<HealthTv> allvideos) {
            this.allvideos = allvideos;
        }

        public List<TagName> getTagName() {
            return tagName;
        }

        public void setTagName(List<TagName> tagName) {
            this.tagName = tagName;
        }

        public List<QucikRead> getQucikReads() {
            return qucikReads;
        }

        public void setQucikReads(List<QucikRead> qucikReads) {
            this.qucikReads = qucikReads;
        }

        public List<HealthTv> getHealthTv() {
            return healthTv;
        }

        public void setHealthTv(List<HealthTv> healthTv) {
            this.healthTv = healthTv;
        }

        public List<Webinar> getWebinar() {
            return webinar;
        }

        public void setWebinar(List<Webinar> webinar) {
            this.webinar = webinar;
        }

        public List<HealthTv> getRecommendedVideos() {
            return recommendedVideos;
        }

        public void setRecommendedVideos(List<HealthTv> recommendedVideos) {
            this.recommendedVideos = recommendedVideos;
        }



        public class HealthTv implements Serializable{

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("Title")
            @Expose
            private String title;
            @SerializedName("Link")
            @Expose
            private String link;
            @SerializedName("Path")
            @Expose
            private String path;
            @SerializedName("Description")
            @Expose
            private String description;
            @SerializedName("ThumbnailImage")
            @Expose
            private String ThumbnailImage;
            @SerializedName("IsBookmarked")
            @Expose
            public int isBookMarked;

            @SerializedName("SearchKey")
            @Expose
            public String SearchKey;



            /**
             * No args constructor for use in serialization
             */
            public HealthTv() {
            }

            /**
             * @param path
             * @param link
             * @param description
             * @param id
             * @param title
             */
            public HealthTv(Integer id, String title, String link, String path, String description) {
                super();
                this.id = id;
                this.title = title;
                this.link = link;
                this.path = path;
                this.description = description;
            }

            public String getSearchKey() {
                return SearchKey;
            }

            public void setSearchKey(String searchKey) {
                SearchKey = searchKey;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumbnailImage() {
                return ThumbnailImage;
            }

            public void setThumbnailImage(String ThumbnailImage) {
                this.ThumbnailImage = ThumbnailImage;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getIsBookMarked() {
                return isBookMarked;
            }

            public void setIsBookMarked(int isBookMarked) {
                this.isBookMarked = isBookMarked;
            }
        }

        public List<AudioFiles> getAudioFiles() {
            return audioFiles;
        }

        public void setAudioFiles(List<AudioFiles> audioFiles) {
            this.audioFiles = audioFiles;
        }




        public class AudioFiles implements Serializable{

            @SerializedName("Id")
            @Expose
            private Integer id;
            @SerializedName("Title")
            @Expose
            private String title;
            @SerializedName("AudioUrl")
            @Expose
            private String audioUrl;
            @SerializedName("ThumbnailImage")
            @Expose
            private String thumbnailImage;


            public AudioFiles() {
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAudioUrl() {
                return audioUrl;
            }

            public void setAudioUrl(String audioUrl) {
                this.audioUrl = audioUrl;
            }

            public String getThumbnailImage() {
                return thumbnailImage;
            }

            public void setThumbnailImage(String thumbnailImage) {
                this.thumbnailImage = thumbnailImage;
            }
        }

        public class QucikRead implements Serializable{

            @SerializedName("ArticleID")
            @Expose
            private Integer articleID;
            @SerializedName("ArticleName")
            @Expose
            private String articleName;
            @SerializedName("Tags")
            @Expose
            private String tags;
            @SerializedName("ArticleCode")
            @Expose
            private String articleCode;
            @SerializedName("ImgPath")
            @Expose
            private String imgPath;
            @SerializedName("IsBookMarked")
            @Expose
            public int isBookMarked;

            @SerializedName("searchkey")
            @Expose
            public String searchkey;

            /**
             * No args constructor for use in serialization
             */
            public QucikRead() {
            }

            /**
             * @param articleName
             * @param imgPath
             * @param articleID
             * @param articleCode
             * @param tags
             */
            public QucikRead(Integer articleID, String articleName, String tags, String articleCode, String imgPath) {
                super();
                this.articleID = articleID;
                this.articleName = articleName;
                this.tags = tags;
                this.articleCode = articleCode;
                this.imgPath = imgPath;
            }

            public String getSearchkey() {
                return searchkey;
            }

            public void setSearchkey(String searchkey) {
                this.searchkey = searchkey;
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

        public class TagName implements Serializable{

            @SerializedName("TagName")
            @Expose
            private String tagName;

            /**
             * No args constructor for use in serialization
             */
            public TagName() {
            }

            /**
             * @param tagName
             */
            public TagName(String tagName) {
                super();
                this.tagName = tagName;
            }

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }

        }

        public class Webinar implements Serializable{

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("Title")
            @Expose
            private String title;
            @SerializedName("Link")
            @Expose
            private String link;
            @SerializedName("ImgPath")
            @Expose
            private String imgPath;
            @SerializedName("HeldOn")
            @Expose
            private String heldOn;
            @SerializedName("Description")
            @Expose
            private Object description;

            /**
             * No args constructor for use in serialization
             */
            public Webinar() {
            }

            /**
             * @param imgPath
             * @param link
             * @param description
             * @param id
             * @param title
             * @param heldOn
             */
            public Webinar(Integer id, String title, String link, String imgPath, String heldOn, Object description) {
                super();
                this.id = id;
                this.title = title;
                this.link = link;
                this.imgPath = imgPath;
                this.heldOn = heldOn;
                this.description = description;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getImgPath() {
                return imgPath;
            }

            public void setImgPath(String imgPath) {
                this.imgPath = imgPath;
            }

            public String getHeldOn() {
                return heldOn;
            }

            public void setHeldOn(String heldOn) {
                this.heldOn = heldOn;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
            }

        }

        public class AllVideos implements Serializable {
            @SerializedName("id")
            @Expose
            private Integer id;

            @SerializedName("Title")
            @Expose
            private String title;

            @SerializedName("Link")
            @Expose
            private String link;

            @SerializedName("Description")
            @Expose
            private String Description;

            @SerializedName("ThumbnailImage")
            @Expose
            private String ThumbnailImage;

            @SerializedName("IsBookmarked")
            @Expose
            private Integer IsBookmarked;

            @SerializedName("SearchKey")
            @Expose
            private String SearchKey;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getDescription() {
                return Description;
            }

            public void setDescription(String description) {
                Description = description;
            }

            public String getThumbnailImage() {
                return ThumbnailImage;
            }

            public void setThumbnailImage(String thumbnailImage) {
                ThumbnailImage = thumbnailImage;
            }

            public Integer getIsBookmarked() {
                return IsBookmarked;
            }

            public void setIsBookmarked(Integer isBookmarked) {
                IsBookmarked = isBookmarked;
            }

            public String getSearchKey() {
                return SearchKey;
            }

            public void setSearchKey(String searchKey) {
                SearchKey = searchKey;
            }
        }

        public class AllBlogs implements Serializable {

            @SerializedName("ArticleID")
            @Expose
            private Integer ArticleID;

            @SerializedName("ArticleName")
            @Expose
            private String ArticleName;

            @SerializedName("Tags")
            @Expose
            private String Tags;

            @SerializedName("ArticleCode")
            @Expose
            private String ArticleCode;

            @SerializedName("ImgPath")
            @Expose
            private String ImgPath;

            @SerializedName("searchkey")
            @Expose
            private String searchkey;

            @SerializedName("IsBookMarked")
            @Expose
            private Integer IsBookMarked;

            public Integer getArticleID() {
                return ArticleID;
            }

            public void setArticleID(Integer articleID) {
                ArticleID = articleID;
            }

            public String getArticleName() {
                return ArticleName;
            }

            public void setArticleName(String articleName) {
                ArticleName = articleName;
            }

            public String getTags() {
                return Tags;
            }

            public void setTags(String tags) {
                Tags = tags;
            }

            public String getArticleCode() {
                return ArticleCode;
            }

            public void setArticleCode(String articleCode) {
                ArticleCode = articleCode;
            }

            public String getImgPath() {
                return ImgPath;
            }

            public void setImgPath(String imgPath) {
                ImgPath = imgPath;
            }

            public String getSearchkey() {
                return searchkey;
            }

            public void setSearchkey(String searchkey) {
                this.searchkey = searchkey;
            }

            public Integer getIsBookMarked() {
                return IsBookMarked;
            }

            public void setIsBookMarked(Integer isBookMarked) {
                IsBookMarked = isBookMarked;
            }
        }

    }

}

