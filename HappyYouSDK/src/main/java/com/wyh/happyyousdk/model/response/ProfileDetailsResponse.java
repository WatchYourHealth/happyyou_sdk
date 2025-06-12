package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProfileDetailsResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;
    @SerializedName("rewards")
    @Expose
    private Object rewards;

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

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public class Data {

        @SerializedName("profile")
        @Expose
        private Profile profile;
        @SerializedName("badges")
        @Expose
        private List<Object> badges;
        @SerializedName("latestWinning")
        @Expose
        private List<LatestWinning> latestWinning;
        @SerializedName("latestMissions")
        @Expose
        private List<LatestMission> latestMissions;
        @SerializedName("tribes")
        @Expose
        private List<Tribe> tribes;
        @SerializedName("blogs")
        @Expose
        private List<Blog> blogs;
        @SerializedName("healthTv")
        @Expose
        private List<GetDashboardDataResponse.Data.HealthTv> healthTv;

        @SerializedName("connectionLists")
        @Expose
        private List<ConnectionList> connectionLists;

        @SerializedName("categoryCount")
        @Expose
        private CategoryCount categoryCount;


        public CategoryCount getCategoryCount() {
            return categoryCount;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public List<Object> getBadges() {
            return badges;
        }

        public void setBadges(List<Object> badges) {
            this.badges = badges;
        }

        public List<LatestWinning> getLatestWinning() {
            return latestWinning;
        }

        public void setLatestWinning(List<LatestWinning> latestWinning) {
            this.latestWinning = latestWinning;
        }

        public List<LatestMission> getLatestMissions() {
            return latestMissions;
        }

        public void setLatestMissions(List<LatestMission> latestMissions) {
            this.latestMissions = latestMissions;
        }

        public List<Tribe> getTribes() {
            return tribes;
        }

        public void setTribes(List<Tribe> tribes) {
            this.tribes = tribes;
        }

        public List<Blog> getBlogs() {
            return blogs;
        }

        public void setBlogs(List<Blog> blogs) {
            this.blogs = blogs;
        }

        public List<GetDashboardDataResponse.Data.HealthTv> getHealthTv() {
            return healthTv;
        }

        public void setHealthTv(List<GetDashboardDataResponse.Data.HealthTv> healthTv) {
            this.healthTv = healthTv;
        }

        public List<ConnectionList> getConnectionLists() {
            return connectionLists;
        }

        public class CategoryCount {

            @SerializedName("tribeCount")
            @Expose
            private String tribeCount;

            @SerializedName("referalCount")
            @Expose
            private String referalCount;

            @SerializedName("dareReferalCount")
            @Expose
            private String dareReferalCount;

            public String getTribeCount() {
                return tribeCount;
            }

            public String getReferalCount() {
                return referalCount;
            }

            public String getDareReferalCount() {
                return dareReferalCount;
            }
        }

        public class ConnectionList {

            @SerializedName("name")
            @Expose
            private String name;

            @SerializedName("type")
            @Expose
            private String type;

            @SerializedName("tribeId")
            @Expose
            private String tribeId;

            public String getName() {
                return name;
            }

            public String getType() {
                return type;
            }

            public String getTribeId() {
                return tribeId;
            }
        }

        public class Tribe {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("communityType")
            @Expose
            private String communityType;
            @SerializedName("name")
            @Expose
            private String name;

            /**
             * No args constructor for use in serialization
             */
            public Tribe() {
            }

            /**
             * @param communityType
             * @param name
             * @param id
             */
            public Tribe(Integer id, String communityType, String name) {
                super();
                this.id = id;
                this.communityType = communityType;
                this.name = name;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getCommunityType() {
                return communityType;
            }

            public void setCommunityType(String communityType) {
                this.communityType = communityType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }

        public class Profile {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("profileImg")
            @Expose
            private String profileImg;
            @SerializedName("nickname")
            @Expose
            private String nickname;
            @SerializedName("level")
            @Expose
            private String level;
            @SerializedName("weight")
            @Expose
            private double weight;
            @SerializedName("height")
            @Expose
            private BigDecimal height;
            @SerializedName("bloodGroup")
            @Expose
            private String bloodGroup;
            @SerializedName("healthScore")
            @Expose
            private String healthScore;
            @SerializedName("badgesCount")
            @Expose
            private Integer badgesCount;
            @SerializedName("tribeCount")
            @Expose
            private Integer tribeCount;
            @SerializedName("connectionsCount")
            @Expose
            private Integer connectionsCount;


            @SerializedName("referralCode")
            @Expose
            private String referralCode;


            public String getReferralCode() {
                return referralCode;
            }

            public void setReferralCode(String referralCode) {
                this.referralCode = referralCode;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProfileImg() {
                return profileImg;
            }

            public void setProfileImg(String profileImg) {
                this.profileImg = profileImg;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(Integer weight) {
                this.weight = weight;
            }

            public BigDecimal getHeight() {
                return height;
            }

            public void setHeight(BigDecimal height) {
                this.height = height;
            }

            public String getBloodGroup() {
                return bloodGroup;
            }

            public void setBloodGroup(String bloodGroup) {
                this.bloodGroup = bloodGroup;
            }

            public String getHealthScore() {
                return healthScore;
            }

            public void setHealthScore(String healthScore) {
                this.healthScore = healthScore;
            }

            public Integer getBadgesCount() {
                return badgesCount;
            }

            public void setBadgesCount(Integer badgesCount) {
                this.badgesCount = badgesCount;
            }

            public Integer getTribeCount() {
                return tribeCount;
            }

            public void setTribeCount(Integer tribeCount) {
                this.tribeCount = tribeCount;
            }

            public Integer getConnectionsCount() {
                return connectionsCount;
            }

            public void setConnectionsCount(Integer connectionsCount) {
                this.connectionsCount = connectionsCount;
            }

        }

        public class LatestMission {

            @SerializedName("eventId")
            @Expose
            private Integer eventId;
            @SerializedName("level")
            @Expose
            private String level;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("point")
            @Expose
            private Integer point;

            public Integer getEventId() {
                return eventId;
            }

            public void setEventId(Integer eventId) {
                this.eventId = eventId;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Integer getPoint() {
                return point;
            }

            public void setPoint(Integer point) {
                this.point = point;
            }

        }

        public class Blog {

            @SerializedName("articleID")
            @Expose
            private String articleID;
            @SerializedName("articleName")
            @Expose
            private String articleName;
            @SerializedName("tags")
            @Expose
            private String tags;
            @SerializedName("articlePath")
            @Expose
            private String articlePath;
            @SerializedName("articleCode")
            @Expose
            private String articleCode;
            @SerializedName("imgPath")
            @Expose
            private String imgPath;

            /**
             * No args constructor for use in serialization
             */
            public Blog() {
            }

            /**
             * @param articleName
             * @param imgPath
             * @param articleID
             * @param articlePath
             * @param articleCode
             * @param tags
             */
            public Blog(String articleID, String articleName, String tags, String articlePath, String articleCode, String imgPath) {
                super();
                this.articleID = articleID;
                this.articleName = articleName;
                this.tags = tags;
                this.articlePath = articlePath;
                this.articleCode = articleCode;
                this.imgPath = imgPath;
            }

            public String getArticleID() {
                return articleID;
            }

            public void setArticleID(String articleID) {
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

        }

        public class LatestWinning implements Serializable {

            @SerializedName("eventDetailID")
            @Expose
            private Integer eventDetailID;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("point")
            @Expose
            private String point;
            @SerializedName("eventImage")
            @Expose
            private String eventImage;


            public Integer getEventDetailID() {
                return eventDetailID;
            }

            public void setEventDetailID(Integer eventDetailID) {
                this.eventDetailID = eventDetailID;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPoint() {
                return point;
            }

            public void setPoint(String point) {
                this.point = point;
            }

            public String getEventImage() {
                return eventImage;
            }

            public void setEventImage(String eventImage) {
                this.eventImage = eventImage;
            }

        }

    }
}
