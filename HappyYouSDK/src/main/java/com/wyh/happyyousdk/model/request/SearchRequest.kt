package com.wyh.happyyousdk.model.request

import com.google.gson.annotations.SerializedName
import com.wyh.happyyousdk.model.request.challengeTribe.EnGtokens
import com.wyh.happyyousdk.model.request.challengeTribe.Rewards
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse

data class SearchRequest(@SerializedName("SearchKey") val searchKey: String)

data class SearchResponse(
    val msg: String,
    val success: Boolean,
    val data: SearchResponseData,
    val freevoucher: Any?,
    @SerializedName("enGTokens")
    val enGtokens: EnGtokens,
    val rewards: Rewards,
    val userkey: Any?,
    val isGoogleFit: Boolean,
)

data class SearchResponseData(
    @SerializedName("HappyMartSearchData")
    val happyMartSearchData: List<HappyMartSearchData>,

    @SerializedName("LifestyleSearchData")
    val lifestyleSearchData: List<LifestyleSearchData>,

    @SerializedName("Videos")
    val videos: List<GetDashboardDataResponse.Data.HealthTv>,

    @SerializedName("Blogs")
    val blogs: List<GetDashboardDataResponse.Data.QucikRead>,

    @SerializedName("MyZone")
    val myZone: List<HappyMartSearchData>,

    @SerializedName("KnowYourWellbing")
    val wellBeing: List<HappyMartSearchData>,

    @SerializedName("Services")
    val servicesData: List<HappyMartSearchData>,

    @SerializedName("ActiveZone")
    val activeZoneData: List<HappyMartSearchData>,

    @SerializedName("Others")
    val othersData: List<HappyMartSearchData>,
)

data class HappyMartSearchData(
    @SerializedName("Result")
    val result: String,
)

data class LifestyleSearchData(
    @SerializedName("Result")
    val result: String,
)

data class zone(
    @SerializedName("Result")
    val result: String,
)

data class wellBeing(
    @SerializedName("Result")
    val result: String,
)

data class services(
    @SerializedName("Result")
    val result: String,
)
data class others(
    @SerializedName("Result")
    val result: String,
)