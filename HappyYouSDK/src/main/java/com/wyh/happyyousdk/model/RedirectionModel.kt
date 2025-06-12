package com.wyh.happyyousdk.model

import com.wyh.happyyousdk.model.response.BannerQuizModel

class RedirectionModel(
    val redirectionKey: String?,
    val isRedirectedToWeb: Boolean?,
    val came_from: String?,
    val redirectionUrl: String?,
    val toolbarname: String?,
    val disclaimer: String?,
    val disclaimerURL: String?,
    val activityType: String?,
    val communityID: Int,
    val bannerID: String? = "",
    val happyMartKey: String?,
    val happyMartValue: String?,
    val communityName: String?,
    val communityType: String?,
    val isQuizCompleted: Boolean? = null,  // Optional, defaults to null
    val bannerQuizModel: BannerQuizModel? = null,  // Optional, defaults to null
    val rewardDate: String?
) {
    // Secondary constructor for backward compatibility without new fields
    constructor(
        redirectionKey: String?,
        isRedirectedToWeb: Boolean?,
        came_from: String?,
        redirectionUrl: String?,
        toolbarname: String?,
        disclaimer: String?,
        disclaimerURL: String?,
        activityType: String?,
        communityID: Int,
        bannerID: String? = "",
        happyMartKey: String?,
        happyMartValue: String?,
        communityName: String?,
        communityType: String?
    ) : this(
        redirectionKey,
        isRedirectedToWeb,
        came_from,
        redirectionUrl,
        toolbarname,
        disclaimer,
        disclaimerURL,
        activityType,
        communityID,
        bannerID,
        happyMartKey,
        happyMartValue,
        communityName,
        communityType,
        isQuizCompleted = null,  // Default to null for compatibility
        bannerQuizModel = null,
        rewardDate = ""// Default to null for compatibility
    )


}