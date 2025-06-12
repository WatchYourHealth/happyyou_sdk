package com.wyh.happyyousdk.Sonde.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.crypto.RSAEncryption
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityDirectionScreenBinding
import com.wyh.happyyousdk.databinding.UpdateSerDetailsLayoutBinding
import com.wyh.happyyousdk.model.request.DeviceInfo
import com.wyh.happyyousdk.model.request.SondeTokenResponse
import com.wyh.happyyousdk.model.request.SondeUserRegistrationRequest
import com.wyh.happyyousdk.model.request.UserDetailsRequest
import com.wyh.happyyousdk.model.response.SondeQuestionResponse
import com.wyh.happyyousdk.model.response.SondeUserRegistrationResponse
import com.wyh.happyyousdk.model.response.UserDetailResponse
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DirectionScreen : AppCompatActivity() {

    lateinit var binding : ActivityDirectionScreenBinding
    var comingFrom = ""
    val apiInterface: APIInterface = RetrofitHandler.getSondeRetrofitInstance().create(APIInterface::class.java)
    var gender = ""
    var question = ""
    var otherGender = ""
    var birthYear = ""

    lateinit var mainStartCalender : Calendar
    var user = "self"

    var dobStr: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_direction_screen)
        setContentView(binding.root)
        SharedPref.init(this)


        comingFrom = intent.getStringExtra("comingFrom")!!


        if(comingFrom.equals("metalFitness",true)){
            binding.mentalWellnessParentLayout.visibility = View.VISIBLE
            binding.mentalWellnessNoteTv.visibility = View.VISIBLE
            binding.respiratoryDirectionLayout.visibility = View.GONE
            binding.directionProceed.text = "Begin recording"
            binding.includeBack.tvBack.text = getString(R.string.mental_wellness_heading)
            binding.imageView.setImageResource(R.drawable.sonde_respiratory_alert_bear)

            getQuestionRefresh()

        }else{
            binding.mentalWellnessParentLayout.visibility = View.GONE
            binding.mentalWellnessNoteTv.visibility = View.GONE
            binding.respiratoryDirectionLayout.visibility = View.VISIBLE
            binding.includeBack.tvBack.text = getString(R.string.respiratory_heading)
            binding.directionProceed.text = "Let's Begin"
            binding.imageView.setImageResource(R.drawable.sonde_ahh_bear)
        }

        binding.includeBack.tvBack.setTextColor(getColor(R.color.white))
        binding.includeBack.ivBack.setColorFilter(getColor(R.color.white))

        binding.includeBack.llBack.setOnClickListener {
            finish()

        }


        binding.mrentalWellnessRefreshImg.setOnClickListener {
            APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_Refresh", this@DirectionScreen)
            getQuestionRefresh()
        }

        binding.menatlWellnessOwnTopicCheckbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1){
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_Tick", this@DirectionScreen)
                    binding.mentalWellnessQuestionTv.text = "My own topic"
                }else{
                    binding.mentalWellnessQuestionTv.text = question
                }
            }

        })

        binding.directionIvHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.directionProceed.setOnClickListener {
            if(comingFrom.equals("mentalFitness", true))
                APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR", this@DirectionScreen)
            else
                APILogs.activityTracker("A_DB_BI_AZ_KYW_RH_Begin", this@DirectionScreen)
            if(binding.menatlWellnessOwnTopicCheckbox.isChecked){
                question = "Your topic"
            }
            getToken()
        }

    }

    private fun getToken(){
        try{
            CommonUtils.showProgressDialige(this)
            apiInterface.getSondeToken(SharedPref.getAuthToken()).enqueue(object : Callback<SondeTokenResponse>{
                override fun onResponse(call: Call<SondeTokenResponse>, response: Response<SondeTokenResponse>) {
                    CommonUtils.dismissDialoge()
                    response.body().let {
                        if(response.code() == 200 && response.body()!!.success){
                            SharedPref.putSondeToken(response.body()!!.data.access_token)
                            userDetailsPopup("",this@DirectionScreen)
                            //genderDialoge()
                        }
                    }
                }

                override fun onFailure(call: Call<SondeTokenResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    private fun getQuestionRefresh(){
        try{
            CommonUtils.showProgressDialige(this)
            apiInterface.getSondeQuestion(SharedPref.getAuthToken()).enqueue(object : Callback<SondeQuestionResponse>{
                override fun onResponse(call: Call<SondeQuestionResponse>, response: Response<SondeQuestionResponse>) {
                    CommonUtils.dismissDialoge()
                    response.body().let {
                        if(response.code() == 200 && response.body()!!.success){
                            binding.menatlWellnessOwnTopicCheckbox.isChecked = false
                            question = response.body()!!.data.questionname
                            binding.mentalWellnessQuestionTv.text = response.body()!!.data.questionname
                        }
                    }
                }

                override fun onFailure(call: Call<SondeQuestionResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })

        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    private fun registerUser(gender: String){
        try{
            CommonUtils.showProgressDialige(this)
            val deviceModel = Build.BRAND + " " + Build.MODEL
            val deviceInfo = DeviceInfo("MOBILE",deviceModel)
            val birthYear = CommonUtils.formatDateFromString("dd/MM/yyyy","yyyy",birthYear)
            Log.d("AuthToken",birthYear)
            val sondeUserRegistrationRequest = SondeUserRegistrationRequest(SharedPref.getSondeToken(), gender, birthYear,deviceInfo,user)
            apiInterface.sondeUserRegistration(SharedPref.getAuthToken(),sondeUserRegistrationRequest).enqueue(object : Callback<SondeUserRegistrationResponse>{
                override fun onResponse(call: Call<SondeUserRegistrationResponse>, response: Response<SondeUserRegistrationResponse>) {
                    CommonUtils.dismissDialoge()

                    response.body().let {
                        if(response.code() == 200 && response.body()!!.success){
                            SharedPref.putSondeUserRegistered(true)
                            SharedPref.putSondeUserIdentifier(response.body()!!.data.userIdentifier)
                            if(question.equals("Your topic",true)){
                                SharedPref.putWellnessTopic("Mental Fitness Topic")
                            }else{
                                SharedPref.putWellnessTopic(question)
                            }

                            startActivity(Intent(this@DirectionScreen,CountdownActivity::class.java)
                                .putExtra("comingFrom",comingFrom).putExtra("question",question)
                                .putExtra("errorText",""))
                            //finish()
                        }
                    }
                }

                override fun onFailure(call: Call<SondeUserRegistrationResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()
                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun isOver18(dob: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val dateOfBirth = dateFormat.parse(dob)

        // Calculate age
        val diff = currentDate.time - (dateOfBirth?.time ?: 0)
        val age = diff / (1000L * 60 * 60 * 24 * 365)

        return age >= 18
    }


    fun userDetailsPopup(comingFrom: String, context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val binding: UpdateSerDetailsLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.update_ser_details_layout,
                null, false
            )
            builder.setView(binding.getRoot())
            val alertDialog = builder.create()
            alertDialog.setCancelable(true)
            val prevYear = Calendar.getInstance()
            prevYear.add(Calendar.YEAR, -15)
            mainStartCalender = Calendar.getInstance()
            if (SharedPref.getDOB() != null && SharedPref.getDOB() !== "") {
                if(!isValidDateFormat(SharedPref.getDOB())){
                    dobStr = CommonUtils.formatDateFromString("yyyy-MM-dd", "dd/MM/yyyy", SharedPref.getDOB())
                }else{
                    dobStr = SharedPref.getDOB()

                }
                birthYear = dobStr
                binding.updateUserDobEt.isEnabled = false
                binding.updateUserDobEt.setText(SharedPref.getDOB())
            }
            if (SharedPref.getUserGender() != null && SharedPref.getUserGender() !== "") {
                if (SharedPref.getUserGender().equals("male", ignoreCase = true)) {
                    gender = "male"
                    binding.updateUserMaleLayout.setEnabled(false)
                    binding.updateUserFemaleLayout.setEnabled(false)
                    binding.updateUserMaleImg.setImageResource(R.drawable.male_selected)
                    binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
                } else {
                    gender = "female"
                    binding.updateUserFemaleLayout.setEnabled(false)
                    binding.updateUserMaleLayout.setEnabled(false)
                    binding.updateUserFemaleImg.setImageResource(R.drawable.female_selected)
                    binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
                }
            }
            binding.updateUserRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.rb_self -> {
                        user = "self"
                        if (SharedPref.getUserGender() != null && SharedPref.getUserGender() !== "" || SharedPref.getDOB() != null && SharedPref.getDOB() !== "") {
                            binding.updateUserMaleLayout.setEnabled(false)
                            binding.updateUserFemaleLayout.setEnabled(false)
                            binding.updateUserDobEt.setEnabled(false)
                        } else {
                            binding.updateUserMaleLayout.setEnabled(true)
                            binding.updateUserFemaleLayout.setEnabled(true)
                            binding.updateUserDobEt.setEnabled(true)
                        }
                        if (SharedPref.getDOB() != null && SharedPref.getDOB() !== "") {
                            binding.updateUserDobEt.setText(SharedPref.getDOB())
                        }
                        if (SharedPref.getUserGender() != null && SharedPref.getUserGender() !== "") {
                            if (SharedPref.getUserGender().equals("male", ignoreCase = true)) {
                                gender = "male"
                                binding.updateUserMaleImg.setImageResource(R.drawable.male_selected)
                                binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
                            } else {
                                gender = "female"
                                binding.updateUserFemaleImg.setImageResource(R.drawable.female_selected)
                                binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
                            }
                        }
                    }
                    R.id.rb_others -> {
                        user = "others"
                        binding.updateUserMaleLayout.setEnabled(true)
                        binding.updateUserFemaleLayout.setEnabled(true)
                        binding.updateUserDobEt.setEnabled(true)
                        binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
                        binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
                        binding.updateUserDobEt.setText("")
                    }
                }
            })
            binding.updateUserDobEt.setOnClickListener(View.OnClickListener {
                val year: Int = mainStartCalender.get(Calendar.YEAR)
                val month: Int = mainStartCalender.get(Calendar.MONTH)
                val day: Int = mainStartCalender.get(Calendar.DAY_OF_MONTH)
                val pickerDialog = DatePickerDialog(context,
                    { view1: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                        val newDay: String
                        newDay = if (dayOfMonth.toString().length == 1) {
                            "0$dayOfMonth"
                        } else {
                            dayOfMonth.toString()
                        }
                        val newMonth: String = if ((monthOfYear + 1).toString().length == 1) {
                            "0" + (monthOfYear + 1)
                        } else {
                            (monthOfYear + 1).toString()
                        }
                        binding.updateUserDobEt.setText("$newDay/$newMonth/$year1")
                        birthYear = binding.updateUserDobEt.text.toString()
                        dobStr = "$year1-$newMonth-$newDay"
                        //Toast.makeText(getApplicationContext(), dobStr, Toast.LENGTH_LONG).show();
                        mainStartCalender = Calendar.getInstance()
                        mainStartCalender.set(Calendar.YEAR, year1)
                        mainStartCalender.set(Calendar.MONTH, monthOfYear)
                        mainStartCalender.set(Calendar.DATE, dayOfMonth)
                    }, year, month, day
                )
                //pickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                pickerDialog.datePicker.maxDate = prevYear.timeInMillis
                pickerDialog.show()
            })
            binding.updateUserMaleLayout.setOnClickListener(View.OnClickListener {
                if (user.equals("self", ignoreCase = true)) {
                    gender = "male"
                } else {
                    otherGender = "male"
                }
                if(comingFrom.equals("mentalFitness", true))
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_Male", this@DirectionScreen)
                else
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_RH_Begin_Male", this@DirectionScreen)
                binding.updateUserMaleImg.setImageResource(R.drawable.male_selected)
                binding.updateUserFemaleImg.setImageResource(R.drawable.female_unselected)
            })
            binding.updateUserFemaleLayout.setOnClickListener(View.OnClickListener {
                if (user.equals("self", ignoreCase = true)) {
                    gender = "female"
                } else {
                    otherGender = "female"
                }
                if(comingFrom.equals("mentalFitness", true))
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_Female", this@DirectionScreen)
                else
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_RH_Begin_Female", this@DirectionScreen)
                binding.updateUserFemaleImg.setImageResource(R.drawable.female_selected)
                binding.updateUserMaleImg.setImageResource(R.drawable.male_unselected)
            })
            binding.updateUserSubmit.setOnClickListener(View.OnClickListener {

                if (user.equals("self", ignoreCase = true)) {
                    if (gender == "") {
                        Toast.makeText(context, "Please select gender", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                } else {
                    if (otherGender == "") {
                        Toast.makeText(context, "Please select gender", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                }
                if (binding.updateUserDobEt.getText().toString().equals("")) {
                    Toast.makeText(context, "Please select your date of birth", Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                if (user == "") {
                    Toast.makeText(context, "Please select user", Toast.LENGTH_LONG).show()
                    return@OnClickListener
                }
                alertDialog.dismiss()
                if (!user.equals("others", ignoreCase = true)) {
                    if(isOver18(birthYear)){
                        updateUserDetails("", "", dobStr, gender, comingFrom, context)
                    }else{
                        Toast.makeText(context,"We are regret to inform you that, you are not suitable for this test",Toast.LENGTH_LONG).show()
                    }
                } else {
                    user = "self"
                    if(isOver18(birthYear)){
                        registerUser(otherGender)
                    }else{
                        Toast.makeText(context,"We are regret to inform you that, you are not suitable for this test",Toast.LENGTH_LONG).show()

                    }
                }
                APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_Submit", this@DirectionScreen)
            })
            if (!alertDialog.isShowing) alertDialog.show()
            val displayRectangle = Rect()
            val window = window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.88f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun isValidDateFormat(dateStr: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        sdf.isLenient = false // This will make the SimpleDateFormat object strict
        return try {
            // Try to parse the input date string
            val date: Date = sdf.parse(dateStr)
            // If parsing succeeds, then the date string matches the format
            true
        } catch (e: ParseException) {
            // Parsing failed, return false
            false
        }
    }
    private fun updateUserDetails(name: String, email: String, dob: String, gende: String, comingFrom: String, context: Context) {
        try {
            var dobStr = dob
            CommonUtils.showProgressDialige(context)
            SharedPref.putDOB(dobStr)
            SharedPref.putUserGender(gende)
            if(isValidDateFormat(dobStr)){
                dobStr = CommonUtils.formatDateFromString("dd/MM/yyyy", "yyyy/MM/dd", dobStr)
            }
            val userDetailsRequest =
                UserDetailsRequest(name, RSAEncryption.rsaEncrypt(email), dobStr, gende)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.updateUserDetails(SharedPref.getAuthToken(), userDetailsRequest)
                .enqueue(object : Callback<UserDetailResponse?> {
                    override fun onResponse(
                        call: Call<UserDetailResponse?>,
                        response: Response<UserDetailResponse?>
                    ) {
                        CommonUtils.dismissDialoge()
                        if (response.code() == 200  && response.isSuccessful) {
                           // Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                            registerUser(gender)

                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<UserDetailResponse?>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }
                })
        } catch (e: java.lang.Exception) {
            CommonUtils.dismissDialoge()
            e.printStackTrace()
        }
    }


}