package com.wyh.happyyousdk.Sonde.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wyh.happyyousdk.R

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityFailedResponseScreenBinding
import com.wyh.happyyousdk.utils.SharedPref

class FailedResponseScreen : AppCompatActivity() {

    lateinit var binding : ActivityFailedResponseScreenBinding
    var comingFrom = ""
    var erroMsgString = ""
    var failedType = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(this)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_failed_response_screen)
        setContentView(binding.root)

        comingFrom = intent.getStringExtra("comingFrom").toString()
        erroMsgString = intent.getStringExtra("errorString").toString()
        failedType = intent.getStringExtra("failedType").toString()

        if(comingFrom.equals("metalFitness",true)){
            SharedPref.putSondeUserRegistered(true)
            if(erroMsgString == "" || erroMsgString.isEmpty()){
                binding.failedResponseTv.text = getString(R.string.mental_fitness_failed_response)
            }else{
                binding.failedResponseTv.text = erroMsgString
            }
        }else{
            if(erroMsgString == "" || erroMsgString.isEmpty()){
                binding.failedResponseTv.text = getString(R.string.wrong_voice)
            }else{

                binding.failedResponseTv.text = erroMsgString
            }
        }

        binding.failedResponseOkBtn.setOnClickListener {
            if(erroMsgString.contains("reliable result") || erroMsgString.contains("Sorry, there was an error in calculating the scores") ||
                    erroMsgString.contains("There was a problem calculating the result") || erroMsgString.contains("Sorry, there was an Technical error")){
                SharedPref.putSondeUserRegistered(false)
                startActivity(Intent(this@FailedResponseScreen,RespiratoryCheck::class.java)
                    .putExtra("comingFrom",comingFrom))
                finish()
            }else{
                if(erroMsgString.contains("Please repeat this activity")){
                    SharedPref.putSondeUserRegistered(false)
                    startActivity(Intent(this@FailedResponseScreen,RespiratoryCheck::class.java)
                        .putExtra("comingFrom",comingFrom))
                    finish()
                } else if(failedType.equals("respiratoryFirstAudioFailed",true)){
                    startActivity(Intent(this@FailedResponseScreen,CountdownActivity::class.java)
                        .putExtra("comingFrom",comingFrom)
                        .putExtra("errorText","respiratoryFirstAudioFailed"))
                    finish()
                }  else if(failedType.equals("respiratorySecondAudioFailed",true)){
                    startActivity(Intent(this@FailedResponseScreen,CountdownActivity::class.java)
                        .putExtra("comingFrom",comingFrom)
                        .putExtra("errorText","respiratorySecondAudioFailed"))
                    finish()
                }
                else{
                    startActivity(Intent(this@FailedResponseScreen,DirectionScreen::class.java)
                        .putExtra("comingFrom",comingFrom))
                    finish()
                }
            }


        }

        binding.failedBackLayout.setOnClickListener {
            startActivity(Intent(this@FailedResponseScreen,DirectionScreen::class.java)
                .putExtra("comingFrom",comingFrom))
            finish()
        }

        binding.failedResponseIvHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }


    }
}