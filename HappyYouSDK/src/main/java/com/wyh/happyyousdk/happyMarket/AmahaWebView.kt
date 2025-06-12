package com.wyh.happyyousdk.happyMarket

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.ActivityAmahaWebViewBinding
import java.io.File

class AmahaWebView : AppCompatActivity() {

    lateinit var binding : ActivityAmahaWebViewBinding
    private val MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1
    private val REQ_CODE = 2
    private var downloadUrl = ""
    private var downloadFileName = ""
    var url = ""
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    private val requirePermissionArray = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private val MY_PERMISSIONS_REQUEST_LIVE_MODE = 111

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WebView.setWebContentsDebuggingEnabled(true)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_amaha_web_view)
        url = intent.getStringExtra("Url").toString()

        val webSettings = binding.amahaWebView.settings
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
        //webSettings.setAppCacheEnabled(true)
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        //webSettings.setAppCacheEnabled(true)
        webSettings.loadsImagesAutomatically = true
        webSettings.setGeolocationEnabled(false)
        webSettings.setNeedInitialFocus(false)

        webSettings.saveFormData = false
        webSettings.allowFileAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        binding.amahaWebView.loadUrl(url);
        binding.amahaWebView.addJavascriptInterface(this, "HappyYou")

        binding.amahaWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (request?.url.toString().startsWith("tel:")) {
                    startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse(request?.url.toString())))
                    return true
                }
                if (request?.url.toString().startsWith("mailto:")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(request?.url.toString())))
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request) }
        }

        binding.amahaWebView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                uploadMessage = filePathCallback
                selectFromDevice()
                return true
            }
        }


    }

    private fun selectFromDevice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.MANUFACTURER == "samsung") {
                try {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    startActivityForResult(Intent.createChooser(intent, "Select File"), REQ_CODE)
                } catch (e: Exception) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Downloads.EXTERNAL_CONTENT_URI)
                    intent.type = "*/*"
                    val mimeTypes = arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "*/*")
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                    startActivityForResult(Intent.createChooser(intent, "Select File"), REQ_CODE)
                }
            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Downloads.EXTERNAL_CONTENT_URI)
                intent.type = "*/*"
                val mimeTypes = arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "*/*")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(Intent.createChooser(intent, "Select File"), REQ_CODE)
            }
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            val mimetypes = arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "*/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            startActivityForResult(intent, REQ_CODE)
        }

    }


    @JavascriptInterface
    fun copyCoupon(coupon: String) {
        (this.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(ClipData.newPlainText("coupon_code_copy", coupon))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    when {
                        binding?.amahaWebView?.canGoBack() == true -> {
                            binding?.amahaWebView?.goBack()
                        }

                        else -> {
                            finish()
                            return super.onKeyDown(keyCode, event)
                        }
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @JavascriptInterface
    fun downloadFile(url: String, name: String) {
        downloadUrl = url
        downloadFileName = name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            downloadFileFromUrl()
        } else {
            requestAccessForWriteExternalStorage()
        }
    }

    private fun downloadFileFromUrl() {
        downloadUrl?.let { url ->
            val fileName = downloadFileName
            val request = DownloadManager.Request(Uri.parse(url))
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName ?: "file")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            (this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        }}
    private fun requestAccessForWriteExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            createAlertDialogToAskWriteExternalPermission()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_STORAGE)
        }
    }

    private fun createAlertDialogToAskWriteExternalPermission() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("Storage permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes) { _, _ ->
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_STORAGE)
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun   checkAndTriggerPermissionRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
        ) {
            createAlertDialogToAskCameraAndMicPermission()
        } else {
            ActivityCompat.requestPermissions(this, requirePermissionArray, MY_PERMISSIONS_REQUEST_LIVE_MODE)
        }
    }




    @JavascriptInterface
    fun addToCalendar(bookingDetails: String) {
        val currentBookingName = "booking_"
        getExternalFilesDir(null)?.absolutePath?.let {
            val storageDir = File(it)
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            val file = File.createTempFile(currentBookingName, ".ics", storageDir)
            file.writeText(bookingDetails)
            val bookingUri = FileProvider.getUriForFile(this, applicationContext.packageName.toString() + ".provider", file)
            val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
            val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(bookingUri, mimetype)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
    }


    @JavascriptInterface
    fun closeWebView() {
        finish()
    }


    @JavascriptInterface
    fun checkAndPromptForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            checkAndTriggerPermissionRequest()
        }
    }

    @JavascriptInterface
    fun shareProvider(title: String, body: String, url: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TITLE, title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$body: $url")
        startActivity(Intent.createChooser(shareIntent, "Share Using"))
    }


    @JavascriptInterface
    fun openMap(lat: String, long: String, label: String) {
        try {
            val gmmIntentUri = Uri.parse("geo:<$lat>,<$long>?z=15&q=<$lat>,<$long>($label)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:<$lat>,<$long>?q=<$lat>,<$long>($label)"))
            startActivity(intent)
        }
    }

    @JavascriptInterface
    fun openLink(url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } catch (e: Exception) {
            //LogHelper.e(TAG, e)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val array = arrayOf(resultIntent?.data!!)
                uploadMessage?.onReceiveValue(array)
            } else {
                uploadMessage?.onReceiveValue(arrayOf())
            }
            uploadMessage = null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LIVE_MODE) {
            if (grantResults.isEmpty() || grantResults.any { tt -> tt != PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permission is needed for live session", Toast.LENGTH_SHORT).show()
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
                ) {
                    createAlertDialogForPermissionDenial()
                } else {
                    createAlertDialogToAskCameraAndMicPermission()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun createAlertDialogToAskCameraAndMicPermission() {
        runOnUiThread {
            try {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setCancelable(true)
                alertBuilder.setIcon(R.drawable.amaha_logo)
                alertBuilder.setTitle("Permission necessary")
                alertBuilder.setMessage("Camera and Microphone permission is necessary")
                alertBuilder.setPositiveButton(R.string.yes) { _, _ -> ActivityCompat.requestPermissions(this, requirePermissionArray, MY_PERMISSIONS_REQUEST_LIVE_MODE) }
                val alert = alertBuilder.create()
                alert.show()
            } catch (e: Exception) {
                //LogHelper.e(TAG, e)
            }
        }
    }

    private fun createAlertDialogForPermissionDenial() {
        runOnUiThread {
            try {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setCancelable(true)
                alertBuilder.setIcon(R.drawable.amaha_logo)
                alertBuilder.setTitle("Permission necessary")
                alertBuilder.setMessage("Camera and Microphone permissions were denied. Please enable Camera and Microphone permissions in application settings.")
                alertBuilder.setPositiveButton(R.string.yes) { _, _ ->
                    settingsPageLauncher.launch(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", this@AmahaWebView.packageName, null)
                        }
                    )
                }
                val alert = alertBuilder.create()
                alert.show()
            } catch (e: Exception) {
                //LogHelper.e(TAG, e)
            }
        }
    }


    private val settingsPageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Cannot continue session without camera and microphone permissions.", Toast.LENGTH_SHORT).show()
            /*if (binding?.wvAssessmentPWA?.canGoBack() == true) {
                binding?.wvAssessmentPWA?.goBack()
            } else {
                finish()
            }*/
            finish()

        }
    }







}
