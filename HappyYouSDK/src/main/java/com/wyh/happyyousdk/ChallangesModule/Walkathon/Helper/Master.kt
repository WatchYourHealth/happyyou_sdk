package com.wyh.happyyousdk.ChallangesModule.Walkathon.Helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.utils.viewtooltip.ViewTooltip
import com.google.android.exoplayer2.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities.EnrolledChallengeActivity
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities.WalkhatonDetailActivity
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.BranchUserAdapter
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.dashboard.ScannerIntegrate
import com.wyh.happyyousdk.model.response.BranchUserData
import com.wyh.happyyousdk.model.response.GetBranchUserListResponse
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.CustomYesNoDialog
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

object Master {

     fun enrolledDialoge(context: Context, challengeDate : String, comingFrom: String, isStarted : Int) {
        val customYesNoDialog = CustomYesNoDialog(context, R.style.Theme_Dialog)
        customYesNoDialog.show()
        customYesNoDialog.setCancelable(false)
        customYesNoDialog.binding.llActionRequired.visibility = View.GONE
         if(comingFrom.equals("firstTime",true)){
             customYesNoDialog.binding.btnCancel.visibility = View.GONE
             customYesNoDialog.binding.txtInfoPopUpDesc.text = context.resources.getString(R.string.enrolled_text)

         }else{
             customYesNoDialog.binding.btnCancel.visibility = View.VISIBLE
             customYesNoDialog.binding.txtInfoPopUpDesc.text = context.resources.getString(R.string.retry_enrolled_text)

         }
         if(comingFrom.equals("retry",true)){
             customYesNoDialog.binding.btnYes.text = "Rescan"

         }else{
             customYesNoDialog.binding.btnYes.text = "OK"

         }
        customYesNoDialog.binding.btnYes.background = ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp)
        customYesNoDialog.binding.btnCancel.background = ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp)

         customYesNoDialog.binding.btnCancel.setOnClickListener {
             customYesNoDialog.dismiss()
         }

        customYesNoDialog.binding.btnYes.setOnClickListener {
            if(customYesNoDialog.binding.btnYes.text.toString().equals("Rescan",true)){
                val intentIntegrator = IntentIntegrator(context as EnrolledChallengeActivity)
                intentIntegrator.setPrompt("Place a barcode/QR Code inside the viewfinder rectangle to scan it.")
                intentIntegrator.setCameraId(0)
                intentIntegrator.captureActivity = ScannerIntegrate::class.java
                intentIntegrator.setOrientationLocked(false)
                intentIntegrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES)
                customYesNoDialog.dismiss()
            }else{
               /* Log.d("date", "$challengeDate")
                Log.d("date", "$isStarted")
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date: Date = sdf.parse(challengeDate)
                Log.d("date", "$date")*/
                customYesNoDialog.dismiss()
                if (isStarted == 1) {
                    context.startActivity(Intent(context,WalkhatonDetailActivity::class.java))
                    (context as Activity).finish()
                }
            }

        }
    }

    fun showMessagePopUp(context: Context, title:String, message:String) {
        val customYesNoDialog = CustomYesNoDialog(context, R.style.Theme_Dialog)
        customYesNoDialog.show()
        customYesNoDialog.setCancelable(false)
        customYesNoDialog.binding.popupIcon.visibility = View.GONE
        customYesNoDialog.binding.btnCancel.visibility = View.GONE
        customYesNoDialog.binding.popupHeading.text = title
        customYesNoDialog.binding.txtInfoPopUpDesc.text = message
        customYesNoDialog.binding.btnYes.text = "OK"
        customYesNoDialog.binding.popupHeading.setTextColor(ContextCompat.getColor(context, R.color.black))
        customYesNoDialog.binding.btnYes.background = ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp)
        customYesNoDialog.binding.btnCancel.background = ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp)
        //context.resources.getString(R.string.retry_enrolled_text)
        customYesNoDialog.binding.btnCancel.setOnClickListener {
            customYesNoDialog.dismiss()
        }
        customYesNoDialog.binding.btnYes.setOnClickListener {
            customYesNoDialog.dismiss()
        }
    }

    fun viewToolTip(context:Activity, view: ImageView, text: String, position: ViewTooltip.Position, align: ViewTooltip.ALIGN) {
        val viewTooltip = ViewTooltip.on(context, view)
            .autoHide(true, 2000)
            .clickToHide(true)
            .align(align)
            .position(position)
            .text(text)
            .textColor(Color.BLACK)
            .color(context.getResources().getColor(R.color.light_pink))
            .corner(10)
            .arrowWidth(15)
            .arrowHeight(15)
            .distanceWithView(0)
            .setTextGravity(Gravity.CENTER) //change the opening animation
            .animation(ViewTooltip.FadeTooltipAnimation(500)) //listeners
            .onDisplay { }
             .onHide { }
        viewTooltip.show()
    }


    fun showEnrollDetail(context: Context,dataList: ArrayList<BranchUserData>){
        try{
            val alertBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialogInfo)
            val view = LayoutInflater.from(context).inflate(R.layout.enrollment_count_layout,null,false)
            alertBuilder.setView(view)

            val list = view.findViewById<RecyclerView>(R.id.enrollment_list)
            val closeBtn = view.findViewById<Button>(R.id.cancelBtn)
            val adapter = BranchUserAdapter(context,dataList)
            list.layoutManager = LinearLayoutManager(context)
            list.hasFixedSize()
            list.adapter = adapter
            val alertDialog = alertBuilder.create()


            closeBtn.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.setCancelable(true)
            alertDialog.show()


            val displayRectangle = Rect()
            val window = (context as Activity).window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setLayout((displayRectangle.width() * 0.8f).toInt(), (displayRectangle.height() * 0.7f).toInt())


        }catch (e: Exception){
            e.toString()
        }
    }

    fun getBranchUser(context: Context){
        try{
            CommonUtils.showProgressDialige(context)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getBranchUser(SharedPref.getAuthToken()).enqueue(object : Callback<GetBranchUserListResponse>{
                override fun onResponse(call: Call<GetBranchUserListResponse>, response: Response<GetBranchUserListResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.code() == 200 && response.body() != null){
                        if(response.body()!!.data.isNotEmpty()){
                            showEnrollDetail(context,response.body()!!.data)
                        }
                    }
                }

                override fun onFailure(call: Call<GetBranchUserListResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }
            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }
}