package com.wyh.happyyousdk.ChallangesModule.Activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Adapter.BadgesAdapter
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.BadgesClick
import com.wyh.happyyousdk.model.response.BadgesResponse
import com.wyh.happyyousdk.model.request.ScratchRequest
import com.wyh.happyyousdk.model.response.ScratchResponse
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityBadgesBinding
import com.wyh.happyyousdk.model.response.RewardsBadgesData
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BadgesActivity : AppCompatActivity(), BadgesClick, ScratchListener {

     var unScractedList : ArrayList<String> = ArrayList()
     lateinit var scracthCardDialog: AlertDialog
     lateinit var binding: ActivityBadgesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BadgesActivity, R.layout.activity_badges)
        setContentView(binding.root)

        SharedPref.init(this)
        SharedPreference.init(this)
        init()

        binding.commonToolBar.tvBack.text = "Badges"

        binding.commonToolBar.llBack.setOnClickListener {
            finish()
        }

        binding.ivHome.setOnClickListener {
            startActivity(Intent(this, NewDashboardActivity::class.java))
            finish()
        }
    }

    fun init(){
        getBadges()
    }

    fun getBadges(){
        try{
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.getBadges(SharedPref.getAuthToken()).enqueue(object : Callback<BadgesResponse>{
                override fun onResponse(call: Call<BadgesResponse>, response: Response<BadgesResponse>) {
                    response.let {
                        CommonUtils.dismissDialoge()
                        if(response.code() == 200 && response.body()!!.success){
                            val badgesAdapter = BadgesAdapter(this@BadgesActivity, response.body()!!.data,this@BadgesActivity)
                            binding.badgesRecyclerview.layoutManager = GridLayoutManager(this@BadgesActivity,3)
                            binding.badgesRecyclerview.hasFixedSize()
                            binding.badgesRecyclerview.adapter = badgesAdapter
                            for (item in response.body()!!.data.indices){
                                if(response.body()!!.data[item].IsScratched.equals("0")){
                                    unScractedList.add(response.body()!!.data[item].IsScratched)
                                }
                            }
                            Log.d("AuthToken","Size "+unScractedList.size)
                        }
                    }
                }

                override fun onFailure(call: Call<BadgesResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })
        }catch (e: Exception){
            e.toString()
        }
    }


    fun scracthDialoge(context: Context, data: RewardsBadgesData, scratchListener: ScratchListener){
        try{
            val alertBuilder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.scratch_card_layout,null,false)
            alertBuilder.setView(view)
            val challengesScracth = view.findViewById<ScratchCardLayout>(R.id.challenges_scrach_view)
            val scratchTitleTv = view.findViewById<TextView>(R.id.scratch_title_tv)
            val scratchImg = view.findViewById<ImageView>(R.id.scratch_img)
            val scratchViewBtn = view.findViewById<Button>(R.id.scratch_view_btn)
            val scratchCloseBtn = view.findViewById<Button>(R.id.scratch_close_btn)

            scratchCloseBtn.setOnClickListener {
                scratchComplete(context, data.TransactionId)
            }


            scratchTitleTv.text = data.BadgeName
            Glide.with(context).load(data.BadgeLogo).into(scratchImg)
            scracthCardDialog = alertBuilder.create()

            challengesScracth.setScratchListener(scratchListener)
            challengesScracth.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new))

            alertBuilder.setView(view)


            scracthCardDialog.show()

            scracthCardDialog.setOnDismissListener {


            }

            val displayRectangle = Rect()
            val window = (context as BadgesActivity).window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            scracthCardDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            scracthCardDialog.getWindow()!!.setLayout(
                (displayRectangle.width() *
                        0.7f).toInt(), (displayRectangle.height() * 0.5f).toInt()
            )

        }catch (e: Exception){
            e.toString()
        }
    }

    fun scratchComplete(context: Context,transactionID: String){
        try{
            CommonUtils.showProgressDialige(context)
            val scratchRequest = ScratchRequest(transactionID)
            helperClass.apiInterface.scrathCard(SharedPref.getAuthToken(),scratchRequest).enqueue(object : Callback<ScratchResponse>{
                override fun onResponse(call: Call<ScratchResponse>, response: Response<ScratchResponse>) {
                    response.body().let {
                        if(response.code() == 200 && response.body()!!.success){
                            CommonUtils.dismissDialoge()
                            scracthCardDialog.dismiss()
                            getBadges()
                        }
                    }
                }

                override fun onFailure(call: Call<ScratchResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    override fun clickToScracth(data: RewardsBadgesData) {
        scracthDialoge(this,data,this)
    }

    override fun onScratchComplete() {
        try{

        }catch (e:Exception){
            e.toString()
        }
    }

    override fun onScratchProgress(scratchCardLayout: ScratchCardLayout, atLeastScratchedPercent: Int) {
        if(atLeastScratchedPercent > 20){
            scratchCardLayout.onFullReveal()
        }
    }

    override fun onScratchStarted() {

    }


}