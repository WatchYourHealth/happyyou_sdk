package com.wyh.happyyousdk.happyMarket

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.TermsAndConditionActivity
import com.wyh.happyyousdk.WebActivity
import com.wyh.happyyousdk.databinding.ActivityOtherSectionDisclaimerBinding
import com.wyh.happyyousdk.qc.QCProductsActivity
import com.wyh.happyyousdk.rewards.fragment.LevelFragment
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.SharedPref

class OtherSectionDisclaimer : AppCompatActivity() {

    lateinit var binding: ActivityOtherSectionDisclaimerBinding
    var vendorName = ""
    var vendorLogo = ""
    var redirectionURL = ""
    var totalPoints = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_other_section_disclaimer)
        setContentView(binding.root)

        vendorName = intent.getStringExtra("vendorName")!!
        vendorLogo = intent.getStringExtra("vendorLogo")!!
        redirectionURL = intent.getStringExtra("redirectionUrl")!!
        totalPoints = intent.getIntExtra("points", 0)


        Glide.with(this).load(vendorLogo).into(binding.otherSectionPartnerImg)

        if(vendorName.equals("WYH",true)){
            binding.otherSectionPartnerImg.visibility = View.GONE
            binding.tvTitle.text = "You are being guided to\n\nVouchers"
        }else{
            binding.otherSectionPartnerImg.visibility = View.VISIBLE

        }


//        binding.tvDisclaimer.setText(Html.fromHtml(getResources().getString(R.string.disclaimer)));
        val textCondition = SpannableString("I accept the Terms of Use")
        val termsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@OtherSectionDisclaimer, TermsAndConditionActivity::class.java)
                startActivity(intent)
            }
        }
        textCondition.setSpan(termsAndCondition, 13, 25, 0)

        binding.tvTandc.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTandc.setText(textCondition, TextView.BufferType.SPANNABLE)

        binding.otherSectionBtnProceed.setOnClickListener {
            if(binding.otherSectionCheckBox.isChecked){
                if(vendorName.equals("WYH",ignoreCase = true)){
                    intent = Intent(this, QCProductsActivity::class.java)
                    intent.putExtra("points", totalPoints)
                    intent.putExtra("amount", LevelFragment.equivalentAmount)
                    startActivity(intent)
                }else{
                    if(redirectionURL.contains("ku=")){
                        redirectionURL += SharedPref.getAesUuid()
                    }
                    val intent = Intent(this, WebActivity::class.java)
                    intent.putExtra("Url", redirectionURL)
                    intent.putExtra("loginUrl","")
                    intent.putExtra("comingFrom","");
                    Analytics.logEvent(this, "Third_party_vender_url", "A_$redirectionURL")
                    startActivity(intent)
                }

            }else{
                Toast.makeText(this, "Please accept the Terms of Use", Toast.LENGTH_SHORT).show()

            }
        }

        binding.otherSectionTvDisclaimer.loadData(getResources().getString(R.string.disclaimer), "text/html", "utf-8")
        binding.includeBack.tvBack.setTextColor(resources.getColor(R.color.white))
        binding.includeBack.ivBack.setColorFilter(resources.getColor(R.color.white))
        binding.includeBack.llBack.setOnClickListener {
            finish()
        }
    }
}