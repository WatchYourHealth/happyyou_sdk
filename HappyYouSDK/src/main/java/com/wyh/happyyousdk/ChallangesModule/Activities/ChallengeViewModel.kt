
  package com.wyh.happyyousdk.ChallangesModule.Activities

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsData
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsRequest
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsResponse
import com.wyh.happyyousdk.model.request.challengeTribe.ViewMoreTribeResponseModel
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class ISLoadingEnum{
    Graph, Main, Spinner, Empty
}

class ChallengeViewModel:ViewModel() {

    private var userData = MutableLiveData<GetCommunityRankDetailsData>()
    val userDataLiveData : LiveData<GetCommunityRankDetailsData> get() = userData
    var isLoadingUserData = MutableLiveData<Boolean>()
    var isLoadingUserDataTwo = MutableLiveData<Boolean>()
    var isLoadingUserDataGraph = MutableLiveData<Boolean>()

    private var tribeData = MutableLiveData<GetCommunityRankDetailsData>()
    val tribeLiveData : LiveData<GetCommunityRankDetailsData> get() = tribeData

    private var viewMoreData = MutableLiveData<ViewMoreTribeResponseModel>()
    val viewMoreLiveData : LiveData<ViewMoreTribeResponseModel> get() = viewMoreData

    fun setLoading(isCame:ISLoadingEnum){
        when (isCame) {
            ISLoadingEnum.Main -> {
                isLoadingUserData.value = true
                isLoadingUserDataGraph.value = false
                isLoadingUserDataTwo.value = false
            }
            ISLoadingEnum.Graph -> {
                isLoadingUserDataGraph.value = true
                isLoadingUserData.value = false
                isLoadingUserDataTwo.value = false
            }
            else -> {
                isLoadingUserDataTwo.value = true
                isLoadingUserData.value = false
                isLoadingUserDataGraph.value = false
            }
        }
    }

    fun closedLoading(){
        isLoadingUserData.value = false
        isLoadingUserDataGraph.value = false
        isLoadingUserDataTwo.value = false
    }

    fun getCommunityRankDetails(communityId:Int, challengeID:String, periodIndex: Int, rankTypes: String = "Tribe", isCame:ISLoadingEnum = ISLoadingEnum.Empty) {
        try {
            setLoading(isCame)
            Log.d("communityId3", "$communityId")
            val request = GetCommunityRankDetailsRequest(
                communityId = communityId,
                challengeId = challengeID,
                periodType = "DAILY",
                periodIndex = periodIndex,
                activityType = "STEPS",
                rankType = rankTypes
            )
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.getCommunityRankDetails(SharedPref.getAuthToken(), request).enqueue(object : Callback<GetCommunityRankDetailsResponse> {
                    override fun onResponse(call: Call<GetCommunityRankDetailsResponse>, response: Response<GetCommunityRankDetailsResponse>) {
                        closedLoading()
                        if (response.code() == 200 && response.isSuccessful) {
                            if (response.body() != null) {
                                if(rankTypes.equals("Users", true)){
                                    userData.value = (response.body()!!.data)
                                }else{
                                    tribeData.value = (response.body()!!.data)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<GetCommunityRankDetailsResponse>, t: Throwable) {
                        closedLoading()
                    }

                })

        } catch (e: Exception) {
            closedLoading()
            e.toString()
        }
    }


    /*fun viewMore(context: Context) {
        try {
            CommonUtils.showProgressDialige(context)
            val apiInterface =
                RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.viewAllTribes(SharedPref.getAuthToken())
                .enqueue(object :
                    Callback<ViewMoreTribeResponseModel> {
                    override fun onResponse(
                        call: Call<ViewMoreTribeResponseModel>,
                        response: Response<ViewMoreTribeResponseModel>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.code() == 200 && response.isSuccessful) {
                            if (response.body() != null) {
                                viewMoreData.value = response.body()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<ViewMoreTribeResponseModel>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissDialoge()
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }*/


    fun getIsLoading():LiveData<Boolean>{
        return isLoadingUserData
    }


}