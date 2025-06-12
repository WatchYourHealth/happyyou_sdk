package com.wyh.happyyousdk.Sonde.Utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wyh.happyyousdk.model.response.VoiceFeature


object Helper {

    var voiceFeatureList : ArrayList<VoiceFeature> = ArrayList()

    fun checkPermission(context : Activity){
        Dexter.withActivity(context)
            .withPermissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Toast.makeText(
                            context,
                            "All the permissions are granted..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(context,"Permission not grante",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest?>?,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener {

            }
            .onSameThread().check()
    }
}