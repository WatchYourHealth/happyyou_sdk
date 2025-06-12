package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JokeOfTheDayResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("setup")
    @Expose
    private String setup;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("flags")
    @Expose
    private Flags flags;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("safe")
    @Expose
    private Boolean safe;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("joke")
    @Expose
    private String joke;

    /**
     * No args constructor for use in serialization
     */
    public JokeOfTheDayResponse() {
    }

    public JokeOfTheDayResponse(Boolean error, String category, String type, String setup, String delivery, Flags flags, Integer id, Boolean safe, String lang, String joke) {
        super();
        this.error = error;
        this.category = category;
        this.type = type;
        this.setup = setup;
        this.delivery = delivery;
        this.flags = flags;
        this.id = id;
        this.safe = safe;
        this.lang = lang;
        this.joke = joke;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSafe() {
        return safe;
    }

    public void setSafe(Boolean safe) {
        this.safe = safe;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = lang;
    }

    public class Flags {

        @SerializedName("nsfw")
        @Expose
        private Boolean nsfw;
        @SerializedName("religious")
        @Expose
        private Boolean religious;
        @SerializedName("political")
        @Expose
        private Boolean political;
        @SerializedName("racist")
        @Expose
        private Boolean racist;
        @SerializedName("sexist")
        @Expose
        private Boolean sexist;
        @SerializedName("explicit")
        @Expose
        private Boolean explicit;

        /**
         * No args constructor for use in serialization
         */
        public Flags() {
        }

        /**
         * @param sexist
         * @param explicit
         * @param religious
         * @param nsfw
         * @param political
         * @param racist
         */
        public Flags(Boolean nsfw, Boolean religious, Boolean political, Boolean racist, Boolean sexist, Boolean explicit) {
            super();
            this.nsfw = nsfw;
            this.religious = religious;
            this.political = political;
            this.racist = racist;
            this.sexist = sexist;
            this.explicit = explicit;
        }

        public Boolean getNsfw() {
            return nsfw;
        }

        public void setNsfw(Boolean nsfw) {
            this.nsfw = nsfw;
        }

        public Boolean getReligious() {
            return religious;
        }

        public void setReligious(Boolean religious) {
            this.religious = religious;
        }

        public Boolean getPolitical() {
            return political;
        }

        public void setPolitical(Boolean political) {
            this.political = political;
        }

        public Boolean getRacist() {
            return racist;
        }

        public void setRacist(Boolean racist) {
            this.racist = racist;
        }

        public Boolean getSexist() {
            return sexist;
        }

        public void setSexist(Boolean sexist) {
            this.sexist = sexist;
        }

        public Boolean getExplicit() {
            return explicit;
        }

        public void setExplicit(Boolean explicit) {
            this.explicit = explicit;
        }

    }


}