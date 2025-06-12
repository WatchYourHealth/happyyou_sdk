package com.wyh.happyyousdk.Sonde.Activities

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Utils.OkHttpHelper
import com.wyh.happyyousdk.ChallangesModule.helperClass
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Adapter.LoaderListAdapter
import com.wyh.happyyousdk.Sonde.Utilities.Helper
import com.wyh.happyyousdk.Sonde.Utilities.NetworkConnection
import com.wyh.happyyousdk.Sonde.Utilities.WaveFirstWriter
import com.wyh.happyyousdk.Sonde.Utilities.WaveFirstWriter.Companion.BufferElements2Rec
import com.wyh.happyyousdk.Sonde.Utilities.WaveFirstWriter.Companion.RECORDER_AUDIO_ENCODING
import com.wyh.happyyousdk.Sonde.Utilities.WaveFirstWriter.Companion.RECORDER_CHANNELS
import com.wyh.happyyousdk.Sonde.Utilities.WaveFirstWriter.Companion.RECORDER_SAMPLE_RATE

import com.wyh.happyyousdk.databinding.ActivityCountdownBinding
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.PopUpShowModel
import com.wyh.happyyousdk.model.request.GetMentalScoreRequest
import com.wyh.happyyousdk.model.request.RespiratoryFirstJobRequest
import com.wyh.happyyousdk.model.request.RespiratorySecondJobRequest
import com.wyh.happyyousdk.model.request.SondeGetUrlRequest
import com.wyh.happyyousdk.model.response.*
import com.wyh.happyyousdk.model.response.AssignRewardsResponse.SpinRewardsData
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData
import com.wyh.happyyousdk.utils.APILogConstant.chat_hc_a_can
import com.wyh.happyyousdk.utils.APILogConstant.chat_hc_a_send
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import kotlinx.coroutines.Runnable
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*
import kotlin.concurrent.thread


class CountdownActivity : AppCompatActivity() {
    lateinit var binding: ActivityCountdownBinding
    private var isRecording = false
    private var recorder: AudioRecord? = null
    private var recordingThread: Thread? = null
    var path = ""
    lateinit var os: FileOutputStream
    private val REQUEST_AUDIO_PERMISSION_CODE = 1
    private var RECORDING_TIMER: Long = 0
    var count = 0
    var filePath = ""
    var signEdUrL = ""
    var comingFrom = ""
    var audioCount = 1
    var listcount = 0
    var question = ""
    var errorString = ""
    var communityID = ""
    var cancelButtonClicked = false
    lateinit var countDownTimer: CountDownTimer
    lateinit var beforeRecordingTimer: CountDownTimer
    lateinit var loaderListTimer: CountDownTimer
    lateinit var networkConnection: NetworkConnection
    lateinit var mWakeLock: PowerManager.WakeLock

    companion object {
        var feedbackModel: FeedbackResponseData = FeedbackResponseData()
    }

    var loaderString =
        arrayOf(
            "Meditation is increasingly being used as a way to alleviate anxiety, stress, and more. Just a few minutes a day can help lead you to a healthier perspective.",
            "The benefits of exercise are more than just physical. Getting your body moving on a regular basis can also enhance your mood, improve your sleep, decrease symptoms of anxiety, and more.",
            "The physical and mental benefits of reading are astounding. Pick up a book today and start to read the lifelong benefits for your mind and your body.",
            "Take a step back from the daily hustle and bustle. Reset your brain and your body with a mindful relaxation exercise.",
            "Fill your nature prescription and discover the benefits of spending time out of doors.",
            "Learn how healthy and Mindful eating can lead to both improved physical and mental health.",
            "A strong social network is linked to better mental health. Making time for family and friends can improve the health and well-being for all involved.",
            "reat your brain like a muscle and give the exercise it needs for it and you to stay healthy.",
            "Improve your mental health and well-being by taking time for yourself to enjoy doing something you love.",
            "How much and how well are you sleeping? The answer could be the key to getting on track to a better state of mental well-being.",
            "Increase your mood and decrease your risk of anxiety by remembering to keep your body hydrated.",
            "Getting outside for a walk is more than just a breath of fresh air. Regular walking can improve energy, enhance mood, and decrease anxiety.",
            "Get in the habit of being grateful. By focusing on the positive, you can improve your perspective and in turn, enhance your overall well-being.",
            "Seeking help when you are feeling down is the first step towards coping with where you are and getting you where you need to be.",
            "Take time away from screens. Constantly being connected can make you feel disconnected from the people and places that matter most. ",
            "As hard as it is to slow down so much that we come to a complete halt, oftentimes that is where our most creative selves can be found.",
            "Organizing and decluttering your space in your physical world will often lead to the same benefits in your psychological one.",
            "Practicing breathing exercises is one immediate way you can address your anxiety and start feeling better.",
            "Taking steps to pause and reflect can help you worry less and calm your stressed-out mind.",
            "Understand the links and differences between exhaustion and depression to help you get the treatment you need."
        )


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_countdown)
        setContentView(binding.root)
        binding.progressBar.max = 3
        SharedPref.init(this)
        comingFrom = intent.getStringExtra("comingFrom").toString()
        question = intent.getStringExtra("question").toString()
        errorString = intent.getStringExtra("errorText").toString()
        communityID = intent.getStringExtra("communityID").toString()
        networkConnection = NetworkConnection(this@CountdownActivity)


        if (comingFrom.equals("metalFitness", true)) {
            RECORDING_TIMER = 30000
            binding.recordingProgressBar.max = 30
            binding.secTv.visibility = View.VISIBLE
            binding.energyTv.visibility = View.VISIBLE
            binding.getReadyTv.visibility = View.VISIBLE
            binding.ahhTv.visibility = View.VISIBLE
            binding.ahhTv.text = "Be prepared to talk about: \n$question"
            binding.energyTv.visibility = View.GONE
            binding.energyTv.text = question
            binding.cancelLayout.visibility = View.VISIBLE
            binding.sendAudioLayout.visibility = View.GONE

        } else if (comingFrom.equals("chat", true)) {
            RECORDING_TIMER = 31000
            binding.recordingProgressBar.max = 31
            binding.secTv.visibility = View.VISIBLE
            binding.energyTv.visibility = View.GONE
            binding.getReadyTv.visibility = View.GONE
            binding.ahhTv.visibility = View.GONE
        } else {
            RECORDING_TIMER = 6000
            binding.recordingProgressBar.max = 6
            if (errorString.equals("respiratoryFirstAudioFailed", true) || errorString.equals(
                    "",
                    true
                )
            ) {
                audioCount = 1
                binding.energyTv.visibility = View.GONE
                binding.ahhTv.visibility = View.GONE
                binding.cancelLayout.visibility = View.GONE
                binding.secTv.visibility = View.GONE
            } else if (errorString.equals("respiratorySecondAudioFailed", true)) {
                audioCount = 2
                binding.recordingLayout.visibility = View.INVISIBLE
                binding.countdownTv.visibility = View.GONE
                binding.sondeStringsImg.visibility = View.GONE
                binding.loaderLayoutParent.visibility = View.GONE
                binding.secondAudioButtonLayout.visibility = View.VISIBLE
                binding.secTv.visibility = View.GONE
            }

        }

        if (CheckPermissions()) {
            if (comingFrom.equals("chat", true)) {
                //startRecording()
                callStartRecording()
                //progressBarTimer()

            } else {
                if (errorString.equals(
                        "",
                        true
                    ) || errorString.equals("respiratoryFirstAudioFailed", true)
                ) {
                    callStartRecording()
                }
            }

        } else {
            RequestPermissions()
        }

        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Network connection lost.Please check your internet connection and try again.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

        binding.secondAudioButton.setOnClickListener {
            if (audioCount == 2) {
                binding.countdownTv.visibility = View.VISIBLE
                binding.sondeStringsImg.visibility = View.VISIBLE
                binding.secondAudioButtonLayout.visibility = View.GONE
                callStartRecording()
            }

        }

        binding.sendAudioLayout.setOnClickListener {
            countDownTimer.cancel()
            stopRecording()
        }

        binding.cancelLayout.setOnClickListener {
            try {
                if (comingFrom.equals("chat", true)) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel()
                    } else {
                        beforeRecordingTimer.cancel()
                    }
                    stopRecording()
                    APILogs.activityTracker(chat_hc_a_can, this)
                    finish()
                } else {
                    APILogs.activityTracker("A_DB_BI_AZ_KYW_MW_BR_Cancel", this@CountdownActivity)
                    if (countDownTimer != null) {
                        countDownTimer.cancel()
                    } else {
                        beforeRecordingTimer.cancel()
                    }

                    Log.d("Authtoken", "Cancel Button $cancelButtonClicked")
                    cancelButtonClicked = true
                    stopRecording()
                    APILogs.activityTracker(chat_hc_a_can, this)
                    finish()
                }

            } catch (e: Exception) {
                e.toString()
            } finally {
                if (!comingFrom.equals("chat", true)) {
                    finish()
                    beforeRecordingTimer.cancel()
                    APILogs.activityTracker(chat_hc_a_can, this)
                }

            }

        }

        try {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            comingFrom = intent.getStringExtra("comingFrom")!!
            SharedPref.putDiaryConcern(false)
        } catch (e: Exception) {
            e.toString()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        try {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } catch (e: Exception) {
            e.toString()
        }

    }

    fun callStartRecording() {
        beforeRecordingTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {
                val count = (p0 / 1000)
                binding.recordingLayout.visibility = View.GONE
                binding.countdownTv.visibility = View.VISIBLE
                binding.sondeStringsImg.visibility = View.GONE
                binding.getReadyTv.visibility = View.VISIBLE
                binding.progressBar.progress = count.toInt()
                (count + 1).toString().also {
                    binding.countdownTv.text = it
                }
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                if (comingFrom.equals("chat", true)) {
                    binding.ahhTv.visibility = View.GONE
                    binding.energyTv.visibility = View.GONE
                } else {
                    if (!comingFrom.equals("metalFitness", true)) {
                        binding.ahhTv.visibility = View.VISIBLE
                        binding.ahhTv.text = getString(R.string.instruction_text)
                        binding.energyTv.visibility = View.GONE
                        binding.secRemainingTv.visibility = View.VISIBLE
                    } else {
                        binding.ahhTv.visibility = View.GONE
                        binding.energyTv.visibility = View.VISIBLE
                        binding.secTv.visibility = View.VISIBLE
                        binding.secRemainingTv.visibility = View.VISIBLE
                    }
                    binding.recordingLayout.visibility = View.VISIBLE
                    binding.countdownTv.visibility = View.INVISIBLE
                    binding.sondeStringsImg.visibility = View.VISIBLE
                    binding.getReadyTv.visibility = View.GONE
                    binding.sondeStringsImg.visibility = View.VISIBLE
                }


                Handler().postDelayed({
                    startRecording()
                    progressBarTimer()
                }, 1000)


            }

        }
        beforeRecordingTimer.start()
    }

    override fun onBackPressed() {


    }

    fun progressBarTimer() {
        binding.recordingProgressBar.rotation = 270F
        countDownTimer = object : CountDownTimer(RECORDING_TIMER, 1000) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTick(p0: Long) {
                val count = (p0 / 1000)
                if (count.toInt() == 31) {
                    binding.cancelLayout.visibility = View.GONE
                    binding.sendAudioLayout.visibility = View.GONE
                } else {
                    if (comingFrom.equals("chat", true)) {
                        binding.sendAudioLayout.visibility = View.VISIBLE
                    }
                    binding.cancelLayout.visibility = View.VISIBLE
                }
                binding.recordingProgressBar.progress = count.toInt()
                binding.recordingLayout.visibility = View.VISIBLE
                binding.countdownTv.visibility = View.INVISIBLE
                binding.sondeStringsImg.visibility = View.VISIBLE
                binding.secRemainingTv.text = "${count}s"
                Log.d(
                    "Authtoken", "T" +
                            "imer $count"
                )
            }

            override fun onFinish() {
                stopRecording()
            }

        }.start()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun startRecording(_filename: String? = null, internalStorage: Boolean = false) {
        val filename = _filename ?: "recording-${System.currentTimeMillis()}.wav"

        path = if (internalStorage) filesDir?.path + "/$filename"
        else externalCacheDir?.path + "/$filename"

        Log.d("AuthToken", "File Path $path")

        if (ActivityCompat.checkSelfPermission(
                this,
                RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING, 512
        )

        recorder?.startRecording()
        isRecording = true


        recordingThread = thread(true) {
            writeAudioDataToFile(path)
        }
    }

    fun CheckPermissions(): Boolean {
        var result = -2
        var result1 = 0
        if (android.os.Build.VERSION.SDK_INT > 30) {
            result = 0
            result1 = ContextCompat.checkSelfPermission(applicationContext, RECORD_AUDIO)
        } else {
            result = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
            result1 = ContextCompat.checkSelfPermission(applicationContext, RECORD_AUDIO)
        }

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED

    }


    private fun RequestPermissions() {
        if (android.os.Build.VERSION.SDK_INT > 30) {
            ActivityCompat.requestPermissions(
                this, arrayOf(RECORD_AUDIO), REQUEST_AUDIO_PERMISSION_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(WRITE_EXTERNAL_STORAGE, RECORD_AUDIO), REQUEST_AUDIO_PERMISSION_CODE
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                if (android.os.Build.VERSION.SDK_INT > 30) {
                    val RecordPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (RecordPermission) {
                        binding.recordingLayout.visibility = View.VISIBLE
                        binding.countdownTv.visibility = View.INVISIBLE
                        binding.sondeStringsImg.visibility = View.VISIBLE
                        callStartRecording()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                        finish()
                    }
                } else {

                    val StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (StoragePermission && RecordPermission) {
                        callStartRecording()
                        //Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun writeAudioDataToFile(path1: String) {
        val sData = ShortArray(BufferElements2Rec)
        try {
            os = FileOutputStream(path1)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val data = arrayListOf<Byte>()

        for (byte in WaveFirstWriter().wavFileHeader()) {
            data.add(byte)
        }

        while (isRecording) {
            recorder?.read(sData, 0, BufferElements2Rec)
            try {
                val bData = WaveFirstWriter().short2byte(sData)
                for (byte in bData)
                    data.add(byte)
            } catch (e: IOException) {
                Log.d("AuthToken", "Exception while writing")
                e.printStackTrace()
            }
        }

        WaveFirstWriter().updateWaveHeader(data)

        Log.d("Authtoken", "File writing started")
        os?.write(data.toByteArray())
        try {
            os?.close()
            Log.d("Authtoken", "File writing completed")
            runOnUiThread(Runnable {
                if (comingFrom.equals("metalFitness", true)) {
                    getSondeUrl()
                } else if (comingFrom.equals("chat", true)) {
                    val intent = Intent().putExtra("filePath", path)
                    setResult(2, intent)
                    finish()
                } else {
                    if (audioCount == 1) {
                        Log.d("Authtoken", "audioCount $audioCount")
                        getRespiratoryFirstFile()
                    } else if (audioCount == 2) {
                        getRespiratorySecondFile()
                    }
                }
            })
        } catch (e: IOException) {
            Log.d("Authtoken", "File writing Exception")
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        recorder?.run {
            isRecording = false;
            stop()
            release()
            recordingThread = null
            recorder = null
        }
    }

    private fun setLoaderList() {
        try {
            binding.loaderLayoutParent.visibility = View.VISIBLE
            //binding.sondeLoaderList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val linearLayoutManager =
                object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
                }
            binding.sondeLoaderList.layoutManager = linearLayoutManager
            binding.sondeLoaderList.hasFixedSize()

            val loaderListAdapter = LoaderListAdapter(this, loaderString)
            binding.sondeLoaderList.adapter = loaderListAdapter


            loaderListTimer = object : CountDownTimer(15000, 3000) {
                override fun onTick(p0: Long) {
                    if (listcount != loaderString.size) {
                        val random = (loaderString.indices).random()
                        listcount++
                        binding.sondeLoaderList.scrollToPosition(random)
                        Log.d("Authtoken", "List Count $random")

                    }
                }

                override fun onFinish() {
                    loaderListTimer.cancel()
                }

            }
            loaderListTimer.start()

        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun getRespiratoryFirstFile() {
        try {
            Log.d("Authtoken", "getRespiratoryFirstFile called")
            binding.recordingLayout.visibility = View.GONE
            binding.countdownTv.visibility = View.GONE
            binding.sondeStringsImg.visibility = View.GONE
            binding.energyTv.visibility = View.GONE
            binding.secTv.visibility = View.GONE
            binding.getReadyTv.visibility = View.GONE
            binding.ahhTv.visibility = View.GONE
            setLoaderList()
            val sondeGetUrlRequest =
                SondeGetUrlRequest(SharedPref.getSondeToken(), SharedPref.getSondeUserIdentifier())
            Log.d("Sondhe", "getRespiratoryFirstFile response ${Gson().toJson(sondeGetUrlRequest)}")

            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getRespiratoryFirstFile(SharedPref.getAuthToken(), sondeGetUrlRequest)
                .enqueue(object : Callback<SondeGetUrlResponse> {
                    override fun onResponse(
                        call: Call<SondeGetUrlResponse>,
                        response: Response<SondeGetUrlResponse>
                    ) {
                        Log.d("Authtoken", "getRespiratoryFirstFile response $response")
                        response.body().let {
                            if (response.code() == 200 && response.body()!!.success) {
                                if (!cancelButtonClicked) {
                                    SharedPref.putSondeAudioUrl(response.body()!!.data.signedURL)
                                    response.body()!!.data.filePath.let {
                                        SharedPref.putSondeFilePath(response.body()!!.data.filePath)
                                        if (response.body()!!.data.filePath != null) {
                                            filePath = response.body()!!.data.filePath
                                        }
                                        if (response.body()!!.data.signedURL != null) {
                                            signEdUrL = response.body()!!.data.signedURL

                                        }
                                        sendAudio()
                                    }
                                } else {
                                    if (loaderListTimer != null) {
                                        loaderListTimer.cancel()
                                    }
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<SondeGetUrlResponse>, t: Throwable) {

                    }

                })

        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun getRespiratorySecondFile() {
        try {
            binding.recordingLayout.visibility = View.GONE
            binding.countdownTv.visibility = View.GONE
            binding.sondeStringsImg.visibility = View.GONE
            binding.energyTv.visibility = View.GONE
            binding.secTv.visibility = View.GONE
            binding.getReadyTv.visibility = View.GONE
            binding.ahhTv.visibility = View.GONE
            binding.secondAudioButtonLayout.visibility = View.GONE
            setLoaderList()

            val sondeGetUrlRequest =
                SondeGetUrlRequest(SharedPref.getSondeToken(), SharedPref.getSondeUserIdentifier())
            Log.d("Sondhe", "getRespiratorySecondFile ${Gson().toJson(sondeGetUrlRequest)}")

            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getRespiratorySecondFile(SharedPref.getAuthToken(), sondeGetUrlRequest)
                .enqueue(object : Callback<SondeGetUrlResponse> {
                    override fun onResponse(
                        call: Call<SondeGetUrlResponse>,
                        response: Response<SondeGetUrlResponse>
                    ) {
                        Log.d(
                            "Sondhe",
                            "getRespiratorySecondFile ${Gson().toJson(sondeGetUrlRequest)}"
                        )
                        //binding.loaderLayoutParent.visibility = View.GONE
                        response.body().let {
                            if (response.code() == 200 && response.body()!!.success) {
                                SharedPref.putSondeAudioUrl(response.body()!!.data.signedURL)
                                response.body()!!.data.filePath.let {
                                    filePath = response.body()!!.data.filePath
                                    signEdUrL = response.body()!!.data.signedURL
                                    sendAudio()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<SondeGetUrlResponse>, t: Throwable) {

                    }

                })


        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun sondeRespiratoryFirstJob() {
        try {
            val apiInterface = RetrofitHandler.apiInterface()
            val respiratoryFirstJobRequest =
                RespiratoryFirstJobRequest(filePath, SharedPref.getSondeToken())
            Log.d("Authtoken", "First Job Request " + Gson().toJson(respiratoryFirstJobRequest))

            apiInterface.respiratoryFirstJob(SharedPref.getAuthToken(), respiratoryFirstJobRequest)
                .enqueue(object : Callback<RespiratoryFirstJobResponse> {
                    override fun onResponse(
                        call: Call<RespiratoryFirstJobResponse>,
                        response: Response<RespiratoryFirstJobResponse>
                    ) {
                        response.body().let {
                            binding.loaderLayoutParent.visibility = View.GONE
                            loaderListTimer.cancel()
                            Log.d(
                                "Authtoken",
                                "First Job Response " + Gson().toJson(response.body())
                            )
                            if (response.body() != null) {
                                if (response.code() == 200 && response.body()!!.success) {
                                    audioCount = 2
                                    listcount = 0
                                    SharedPref.putSondeFirstJobID(response.body()!!.data.id)
                                    //SharedPref.putRespiratoryCount(1)
                                    binding.recordingLayout.visibility = View.INVISIBLE
                                    binding.countdownTv.visibility = View.GONE
                                    binding.sondeStringsImg.visibility = View.GONE
                                    binding.loaderLayoutParent.visibility = View.GONE
                                    binding.secondAudioButtonLayout.visibility = View.VISIBLE
                                } else {
                                    if (SharedPref.getRespiratoryCount() == 3) {
                                        SharedPref.putRespiratoryCount(1)
                                        startActivity(
                                            Intent(
                                                Intent(
                                                    this@CountdownActivity,
                                                    FailedResponseScreen::class.java
                                                )
                                            )
                                                .putExtra("comingFrom", comingFrom)
                                                .putExtra(
                                                    "errorString",
                                                    "Sorry your result was not calculated as we can't process your voice sample Please repeat this activity"
                                                )
                                        )
                                        finish()

                                    } else {
                                        SharedPref.putRespiratoryCount(SharedPref.getRespiratoryCount() + 1)
                                        startActivity(
                                            Intent(
                                                Intent(
                                                    this@CountdownActivity,
                                                    FailedResponseScreen::class.java
                                                )
                                            )
                                                .putExtra("comingFrom", comingFrom)
                                                .putExtra("errorString", response.body()!!.msg)
                                                .putExtra(
                                                    "failedType",
                                                    "respiratoryFirstAudioFailed"
                                                )
                                        )
                                        finish()
                                    }
                                }
                            } else {
                                startActivity(
                                    Intent(
                                        Intent(
                                            this@CountdownActivity,
                                            FailedResponseScreen::class.java
                                        )
                                    )
                                        .putExtra("comingFrom", comingFrom)
                                        .putExtra(
                                            "errorString",
                                            "Something went wrong, Please try again!!"
                                        )
                                        .putExtra("failedType", "respiratoryFirstAudioFailed")
                                )
                                finish()
                            }

                        }
                    }

                    override fun onFailure(call: Call<RespiratoryFirstJobResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                        Log.d("Authtoken", "sondeRespiratoryFirstJob " + t.message)
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            Log.d("Authtoken", "sondeRespiratoryFirstJob " + e.message)
            e.toString()
        }
    }

    private fun sondeRespiratorySecondJob() {
        try {
            val apiInterface = RetrofitHandler.apiInterface()
            val secondJobRequest = RespiratorySecondJobRequest(
                filePath,
                SharedPref.getSondeToken(),
                SharedPref.getSondeFirstJobID(),
                SharedPref.getSondeUserIdentifier(),
                SharedPref.getAudioUrl()
            )
            Log.d("AuthToken", "SecondJobRequest " + Gson().toJson(secondJobRequest))
            apiInterface.respiratorySecondJob(SharedPref.getAuthToken(), secondJobRequest)
                .enqueue(object : Callback<RespiratorySecondJobResponse> {
                    override fun onResponse(
                        call: Call<RespiratorySecondJobResponse>,
                        response: Response<RespiratorySecondJobResponse>
                    ) {
                        Log.d("AuthToken", "SecondJobResponseCode " + response.code())
                        Log.d("AuthToken", "SecondJobResponse " + response.body())
                        binding.loaderLayoutParent.visibility = View.GONE
                        if (loaderListTimer != null) {
                            loaderListTimer.cancel()
                        }
                        if (!cancelButtonClicked) {
                            if (response.body() != null) {
                                if (response.code() == 200 && response.body()!!.success && response.body()!!.data.score != null) {
                                    if (response.body()!!.feedbackDetails != null && response.body()!!.feedbackDetails.starConfig != null) {
                                        feedbackModel = response.body()!!.feedbackDetails
                                        Log.d("AuthToken", "Feedback $feedbackModel")

                                    }

                                    SharedPref.putRespiratoryCount(1)
                                    val spinRewardData: SpinRewardsData
                                    spinRewardData = response.body()!!.spinTheWheelRewardsDetail

                                    val quizathonRewarmodel:QuizathonRewardData
                                    quizathonRewarmodel= response.body()!!.quizathonRewardData

                                    startActivity(
                                        Intent(this@CountdownActivity, AnalysisScreen::class.java)
                                            .putExtra("comingFrom", comingFrom)
                                            .putExtra("spinRewardData", spinRewardData)
                                            .putExtra("quizathonRewardmodel", true)
                                            .putExtra("score", response.body()!!.data.score)
                                    )
                                    finish()
                                } else {
                                    if (response.body()!!.msg.contains(
                                            "Your voice sample can't be processed. Please try again in a quiet place and make sure to say",
                                            true
                                        )
                                    ) {
                                        Log.d("Authtoken", "Condition passed")
                                        if (SharedPref.getRespiratoryCount() == 3) {
                                            SharedPref.putRespiratoryCount(1)
                                            startActivity(
                                                Intent(
                                                    Intent(
                                                        this@CountdownActivity,
                                                        FailedResponseScreen::class.java
                                                    )
                                                )
                                                    .putExtra("comingFrom", comingFrom)
                                                    .putExtra(
                                                        "errorString",
                                                        "Sorry your result was not calculated as we can't process your voice sample Please repeat this activity"
                                                    )
                                            )
                                            finish()

                                        } else {
                                            SharedPref.putRespiratoryCount(SharedPref.getRespiratoryCount() + 1)
                                            startActivity(
                                                Intent(
                                                    Intent(
                                                        this@CountdownActivity,
                                                        FailedResponseScreen::class.java
                                                    )
                                                )
                                                    .putExtra("comingFrom", comingFrom)
                                                    .putExtra("errorString", response.body()!!.msg)
                                                    .putExtra(
                                                        "failedType",
                                                        "respiratorySecondAudioFailed"
                                                    )
                                            )
                                            finish()
                                        }
                                    } else {
                                        if (SharedPref.getRespiratoryCount() == 3) {
                                            SharedPref.putRespiratoryCount(1)
                                        }
                                        startActivity(
                                            Intent(
                                                Intent(
                                                    this@CountdownActivity,
                                                    FailedResponseScreen::class.java
                                                )
                                            )
                                                .putExtra("comingFrom", comingFrom)
                                                .putExtra("errorString", response.body()!!.msg)
                                                .putExtra(
                                                    "failedType",
                                                    "respiratorySecondAudioFailed"
                                                )
                                        )
                                        finish()
                                    }
                                }
                            } else {
                                if (SharedPref.getRespiratoryCount() == 3) {
                                    SharedPref.putRespiratoryCount(1)
                                    startActivity(
                                        Intent(
                                            Intent(
                                                this@CountdownActivity,
                                                FailedResponseScreen::class.java
                                            )
                                        )
                                            .putExtra("comingFrom", comingFrom)
                                            .putExtra(
                                                "errorString",
                                                "Sorry your result was not calculated as we can't process your voice sample Please repeat this activity"
                                            )
                                    )
                                    finish()

                                } else {
                                    SharedPref.putRespiratoryCount(SharedPref.getRespiratoryCount() + 1)
                                    startActivity(
                                        Intent(
                                            Intent(
                                                this@CountdownActivity,
                                                FailedResponseScreen::class.java
                                            )
                                        )
                                            .putExtra("comingFrom", comingFrom)
                                            .putExtra(
                                                "errorString",
                                                "Something went wrong, Please try again!!"
                                            )
                                            .putExtra("failedType", "respiratorySecondAudioFailed")
                                    )
                                    finish()
                                }
                            }
                        }


                    }

                    override fun onFailure(call: Call<RespiratorySecondJobResponse>, t: Throwable) {
                        binding.loaderLayoutParent.visibility = View.GONE
                        loaderListTimer.cancel()
                        Log.d("AuthToken", "SecondJobOnFailure " + t.message)
                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            Log.d("AuthToken", "SecondJobOnException " + e.message)
            e.toString()
        }

    }

    private fun getSondeUrl() {
        try {
            Log.d("Authtoken", "Get Url Called")
            binding.recordingLayout.visibility = View.GONE
            binding.countdownTv.visibility = View.GONE
            binding.sondeStringsImg.visibility = View.GONE
            binding.sondeStringsImg.visibility = View.GONE
            binding.cancelLayout.visibility = View.GONE
            binding.secTv.visibility = View.GONE
            binding.energyTv.visibility = View.GONE
            setLoaderList()
            val sondeGetUrlRequest =
                SondeGetUrlRequest(SharedPref.getSondeToken(), SharedPref.getSondeUserIdentifier())
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getSondeUrl(SharedPref.getAuthToken(), sondeGetUrlRequest)
                .enqueue(object : Callback<SondeGetUrlResponse> {
                    override fun onResponse(
                        call: Call<SondeGetUrlResponse>,
                        response: Response<SondeGetUrlResponse>
                    ) {
                        response.body().let {
                            if (response.code() == 200 && response.body()!!.success) {
                                if (!cancelButtonClicked) {
                                    SharedPref.putSondeAudioUrl(response.body()!!.data.signedURL)
                                    response.body()!!.data.filePath.let {
                                        filePath = response.body()!!.data.filePath
                                        signEdUrL = response.body()!!.data.signedURL
                                        SharedPref.putMentalWellnessFilePath(filePath)
                                        sendAudio()
                                    }
                                } else {
                                    if (loaderListTimer != null) {
                                        loaderListTimer.cancel()
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<SondeGetUrlResponse>, t: Throwable) {
                        Log.d("Authtoken", "Get Url Onfailure")

                    }

                })

        } catch (e: Exception) {
            Log.d("Authtoken", "Get Url Exception $e")
            e.toString()
        }
    }


    private fun sendAudio() {
        try {
            Log.d("AuthToken", "Service Called for audio")
            val file = File(path)
            val mediaType = MediaType.parse("audio/wave");
            val body = RequestBody.create(mediaType, file)
            val parts = ArrayList<MultipartBody.Part>()
            helperClass.prepareFilePart("audio", path)
                ?.let { parts.add(it) }
            val bodyRequest = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts[0])
                .addFormDataPart("signedurl", signEdUrL)
                .build()

            val client: okhttp3.OkHttpClient = OkHttpHelper.okHttpInit()
            val request = Request.Builder()
                .url(CommonUtils.getBaseUrlForAPI(this@CountdownActivity) + "MarketPlace/UploadM3File")
                .method("POST", bodyRequest)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build()

            Log.d("AuthToken", "Service request $request")
            Log.d("AuthToken", "SignedURL $signEdUrL")
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val commonSuccessResponse = Gson().fromJson(
                        response.body()!!.string(),
                        CommonSuccessResponse::class.java
                    )

                    Log.d(
                        "AuthToken",
                        "Code: " + response.code() + ", res: " + Gson().toJson(commonSuccessResponse)
                    )

                    if (response.code() == 200 && commonSuccessResponse.isSuccess) {
                        if (comingFrom.equals("metalFitness", true)) {
                            if (!cancelButtonClicked) {
                                getAudioScore()
                            } else {
                                if (loaderListTimer != null) {
                                    loaderListTimer.cancel()
                                }
                            }
                        } else {
                            if (audioCount == 1) {
                                sondeRespiratoryFirstJob()
                            } else if (audioCount == 2) {
                                Log.d("AuthToken", "SecondJobCalled")
                                sondeRespiratorySecondJob()
                            }
                        }
                    } else {
                        startActivity(
                            Intent(Intent(this@CountdownActivity, FailedResponseScreen::class.java))
                                .putExtra("comingFrom", comingFrom)
                                .putExtra("errorString", commonSuccessResponse.msg)
                        )
                        finish()
                    }
                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.d("AuthToken", "OnFailureText $call")
                }
            })

        } catch (e: Exception) {
            Log.d("AuthToken", "Exception $e")
            e.toString()
        }
    }

    private fun getAudioScore() {
        try {
            val getMentalScoreRequest = GetMentalScoreRequest(
                filePath,
                SharedPref.getSondeToken(),
                SharedPref.getAudioUrl()
            )
            val apiInterface = RetrofitHandler.apiInterface()
            Log.d("Authtoken", "Score Request " + Gson().toJson(getMentalScoreRequest))
            apiInterface.getMentalScore(SharedPref.getAuthToken(), getMentalScoreRequest)
                .enqueue(object : Callback<GetMentalScoreResponse> {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onResponse(
                        call: Call<GetMentalScoreResponse>,
                        response: Response<GetMentalScoreResponse>
                    ) {
                        Log.e("Authtoken", "Score Response " + response.body())
                        Log.e("Authtoken", "Score Response Code " + response.code())
                        binding.loaderLayoutParent.visibility = View.GONE
                        loaderListTimer.cancel()
                        if (response.code() == 200 && response.body() != null) {
                            finish()
                            if (response.body()!!.success) {


                                if (response.body()!!.feedbackDetails != null && response.body()!!.feedbackDetails.starConfig != null) {
                                    feedbackModel = response.body()!!.feedbackDetails
                                }


                                Helper.voiceFeatureList =
                                    response.body()!!.data.result.inference[0].voiceFeatures

                                val newIntent: Intent = Intent(
                                    Intent(
                                        this@CountdownActivity,
                                        AnalysisScreen::class.java
                                    )
                                )
                                    .putExtra(
                                        "score",
                                        response.body()!!.data.result.inference[0].score.value
                                    )
                                    .putExtra("comingFrom", comingFrom)
                                if (response.body()!!.spinTheWheelRewardsDetail != null) {
                                    newIntent.putExtra(
                                        "spinRewardData",
                                        response.body()!!.spinTheWheelRewardsDetail
                                    )
                                }
                                if (response.body()!!.quizathonRewardData != null) {
                                    newIntent.putExtra(
                                        "quizathonRewardmodel",
                                        response.body()!!.quizathonRewardData
                                    )
                                }
                                startActivity(
                                    newIntent
                                )
                                finish()
                            } else {
                                startActivity(
                                    Intent(
                                        Intent(
                                            this@CountdownActivity,
                                            FailedResponseScreen::class.java
                                        )
                                    )
                                        .putExtra("comingFrom", comingFrom)
                                        .putExtra("errorString", response.body()!!.msg)
                                )
                                finish()
                            }
                        } else {
                            startActivity(
                                Intent(
                                    Intent(
                                        this@CountdownActivity,
                                        FailedResponseScreen::class.java
                                    )
                                )
                                    .putExtra("comingFrom", comingFrom)
                                    .putExtra(
                                        "errorString",
                                        "There was a problem calculating the result. Please try again later. "
                                    )
                            )
                            finish()
                        }

                    }

                    override fun onFailure(call: Call<GetMentalScoreResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                        Log.d("AuthToken", "OnFailureScore ${t.message}")
                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            Log.d("AuthToken", "Score  $e")
            e.toString()
        }
    }

}

