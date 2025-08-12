package com.wyh.happyyousdk.Sonde.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityRespiratoryCheckBinding
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref

class RespiratoryCheck : AppCompatActivity() {

    lateinit var binding: ActivityRespiratoryCheckBinding
    var comingFrom = ""
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(this)
        context = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_respiratory_check)
        setContentView(binding.root)

        comingFrom = intent.getStringExtra("comingFrom")!!

        SharedPref.putSondeUserRegistered(false)

        if (comingFrom.equals("metalFitness", true)) {
            //Changed images
            Glide.with(context).load(
                CommonUtils.getBaseUrlForAPI(context) +
                        SDKConstants.endPointForImages + "sonde_mental_fitness_img.png"
            ).into(binding.respiratoryCheckBg)
            binding.respiratoryInfoTv.setText(R.string.mental_fitness)
            binding.includeBack.tvBack.text = getString(R.string.mental_wellness_heading)
        } else {
            //Changed images
            Glide.with(context).load(
                CommonUtils.getBaseUrlForAPI(context) +
                        SDKConstants.endPointForImages + "sonde_respiratory_lungs_img.png"
            ).into(binding.respiratoryCheckBg)
            binding.respiratoryInfoTv.setText(R.string.respiratory_details)
            binding.includeBack.tvBack.text = getString(R.string.respiratory_heading)
        }

        binding.includeBack.tvBack.setTextColor(getColor(R.color.white))
        binding.includeBack.ivBack.setColorFilter(getColor(R.color.white))


        binding.respiratoryIvHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.includeBack.llBack.setOnClickListener {
            finish()
        }

        binding.DashboardHistoryImg.setOnClickListener {
            if (comingFrom.equals("metalFitness", true)) {
                APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_His", this@RespiratoryCheck)
            } else {
                APILogs.activityTracker("A_DB_BI_AZ_KYW_RH_History", this@RespiratoryCheck)
            }
            startActivity(
                Intent(this, SondeHistory::class.java)
                    .putExtra("comingFrom", comingFrom)
            )
        }



        binding.respiratoryProceedBtn.setOnClickListener {
            if (comingFrom.equals("metalFitness", true)) {
                APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_Proceed", this@RespiratoryCheck)
            } else {
                APILogs.activityTracker("A_DB_BI_AZ_KYW_RH_Proceed", this@RespiratoryCheck)
            }
            startActivity(
                Intent(this, DirectionScreen::class.java).putExtra(
                    "comingFrom",
                    comingFrom
                )
            )
        }


    }
}