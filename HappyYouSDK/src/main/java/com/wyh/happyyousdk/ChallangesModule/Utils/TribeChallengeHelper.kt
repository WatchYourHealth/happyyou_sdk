package com.wyh.happyyousdk.ChallangesModule.Utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallangesActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallengeDetailActivity
import com.wyh.happyyousdk.ChallangesModule.Activities.NewTribeChallengeDashboard
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding
import com.wyh.happyyousdk.model.request.StartChallengeRequest
import com.wyh.happyyousdk.model.response.ChallenegActivities
import com.wyh.happyyousdk.model.response.StartResponse
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CustomYesNoDialog
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TribeChallengeHelper {

    lateinit var alertDialog: AlertDialog
    lateinit var activtyID: String

    fun challengeDialog(context : Context, ChallengeID: String, data: ChallenegActivities, cameFromInfo: Boolean, isUserEnrolled: String,recyclerView: RecyclerView, comingFrom: String){
        try{
            val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val view = LayoutInflater.from(context).inflate(R.layout.challenge_journal_upload_dialog,null,false)
            alertBuilder.setView(view)

            alertDialog = alertBuilder.create()


            val logoImage = view.findViewById<ImageView>(R.id.challenge_logo_img)
            val titleTv = view.findViewById<TextView>(R.id.challenge_dialog_title_tv)
            val whatToDo = view.findViewById<TextView>(R.id.challenge_dialog_whattodo_tv)
            val howToDo = view.findViewById<TextView>(R.id.challenge_dialog_tvhowtodo_tv)
            val whyToDo = view.findViewById<TextView>(R.id.challenge_dialog_tvwhytodo)
            val journalDesc = view.findViewById<TextView>(R.id.journal_desc)
            val startButton = view.findViewById<Button>(R.id.challenge_dialog_start_btn)
            val closeButton = view.findViewById<Button>(R.id.challenge_dialoge_close_btn)
            val challengeJournalDespLayout = view.findViewById<LinearLayout>(R.id.challenge_journal_desp_layout)
            val startCancelLayout = view.findViewById<LinearLayout>(R.id.start_cancel_layout)
            val buyBtn = view.findViewById<Button>(R.id.buy_btn)

            whatToDo.text = data.whatToDo
            howToDo.text = data.howToDo
            whyToDo.text = data.whyToDo
            journalDesc.text = data.activityDesc
            titleTv.text = data.activityName

            Glide.with(context)
                .load(data.activityImage)
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(logoImage)

            challengeJournalDespLayout.visibility = View.GONE

          /*  if(SharedPref.getChallengeStarted()){
                startCancelLayout.visibility = View.VISIBLE
                buyBtn.visibility = View.GONE
            }else{
                startCancelLayout.visibility = View.GONE
                buyBtn.visibility = View.VISIBLE
                buyBtn.text = "Close"
            }*/

            closeButton.setOnClickListener {
                alertDialog.dismiss()
            }

            startButton.setOnClickListener {
                alertDialog.dismiss()
                if(startButton.text.contentEquals("enroll",true) || startButton.text.contentEquals("join now",true)){
                    activtyID = data.activityId.toString()
                }else{
                    startChallenge(context,ChallengeID, data.activityId.toString())

                }
            }

            buyBtn.setOnClickListener {
                alertDialog.dismiss()
            }

            if(cameFromInfo){
                if(SharedPref.getChallengeStarted()){
                    if(isUserEnrolled.equals("1")){
                        startCancelLayout.visibility = View.GONE
                        buyBtn.visibility = View.VISIBLE
                        buyBtn.background = context.getDrawable(R.drawable.pink_rc_bg_8dp)
                        buyBtn.text = "Close"
                    }else{
                        startCancelLayout.visibility = View.VISIBLE
                        startButton.text = "Join Now"
                        buyBtn.visibility = View.GONE
                        //buyBtn.text = "Enroll"
                    }
                }else{
                    if(isUserEnrolled.equals("1")){
                        startCancelLayout.visibility = View.GONE
                        buyBtn.visibility = View.VISIBLE
                        buyBtn.background = context.getDrawable(R.drawable.pink_rc_bg_8dp)
                        buyBtn.text = "Close"
                    }else{
                        startCancelLayout.visibility = View.VISIBLE
                        startButton.text = "Enroll"
                        buyBtn.visibility = View.GONE
                        //buyBtn.text = "Enroll"
                    }
                }

                /*startCancelLayout.visibility = View.GONE
                startButton.text = "Enroll"
                buyBtn.visibility = View.VISIBLE
                buyBtn.text = "Close"*/
            }

            alertDialog.setCancelable(true)
            alertDialog.show()

            if(comingFrom.equals("dashboard",true)){
                val displayRectangle = Rect()
                val window = (context as NewDashboardActivity).window

                window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

                alertDialog.window!!.setLayout(
                    (displayRectangle.width() * 0.8f).toInt(), (displayRectangle.height() * 0.7f).toInt()
                )
            }else{
                val displayRectangle = Rect()
                val window = (context as ChallangesActivity).window
                window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
                alertDialog.window!!.setLayout(
                    (displayRectangle.width() * 0.8f).toInt(), (displayRectangle.height() * 0.7f).toInt()
                )
            }

        }catch (e: Exception){
            e.toString()
        }
    }

     fun showConcernInfoLayout(context: Context,challengeID: String) {
        val customYesNoDialog = CustomYesNoDialog(context, R.style.Theme_Dialog)
        customYesNoDialog.show()
        customYesNoDialog.setCancelable(true)
        customYesNoDialog.binding.btnCancel.visibility = View.GONE
        customYesNoDialog.binding.llActionRequired.visibility = View.GONE
        customYesNoDialog.binding.txtInfoPopUpDesc.text = "Walkathon Challenge Has Started"
        customYesNoDialog.binding.btnCancel.visibility = View.GONE
        customYesNoDialog.binding.btnYes.text = "Okay"
        customYesNoDialog.binding.btnYes.background = ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp)
        customYesNoDialog.binding.btnYes.setOnClickListener {
            customYesNoDialog.dismiss()
            startChallenge(context,challengeID, "")

        }

    }

    fun startChallenge(context: Context, challengeID: String,activtyID: String){
        try{
            CommonUtils.showProgressDialige(context)
            val startRequest = StartChallengeRequest(challengeID)
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.startNewChallenge(SharedPref.getAuthToken(),startRequest).enqueue(object : Callback<StartResponse>{
                override fun onResponse(call: Call<StartResponse>, response: Response<StartResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.code() == 200 && response.isSuccessful){
                        if(response.body() != null){
                            val intent = Intent(context,NewTribeChallengeDashboard::class.java)
                            intent.putExtra("challengeID", challengeID)
                            context.startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<StartResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })

        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
     fun showSharePopup(position: Int, context: Context,data: ArrayList<String>,
                        challengeID: String,challengeActivity: ChallenegActivities,btnOk : Button) {
        try{
            val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogInfo)
            val binding: ShareOptionPopUpBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.share_option_pop_up, null, false)
            alertBuilder.setView(binding.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(true)
            if (!alertDialog.isShowing) alertDialog.show()
            binding.tvMsg.text = "Share Greeting"
            binding.ivWhatsapp.setOnClickListener { v ->
                alertDialog.dismiss()
                helperClass.getImageURi(data[position],context)
            }
            val displayRectangle = Rect()
            //        Window window = ((AbsorbActivity) context).getWindow();
            val window: Window = (context as ChallengeDetailActivity).window

            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.88f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }catch (e: Exception){
            e.toString()
        }

    }





}