package com.wyh.happyyousdk.policyDetail.helper;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
public class FingerprintHelper {

    public interface FingerprintCallback {
        void onAuthenticationSuccess();
        void onAuthenticationError(String errorMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void authenticateFingerprint(Context context, FingerprintCallback callback) {
        BiometricManager biometricManager = BiometricManager.from(context);

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                != BiometricManager.BIOMETRIC_SUCCESS) {
            callback.onAuthenticationError("No biometric features available or not configured.");
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt biometricPrompt = new BiometricPrompt(
                (androidx.fragment.app.FragmentActivity) context,
                executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        callback.onAuthenticationError(errString.toString());
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        callback.onAuthenticationSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        callback.onAuthenticationError("Authentication failed. Please try again.");
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Place your finger on the sensor")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
    public static boolean isFingerprintAvailable(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false; // Fingerprint not supported before API 23
        }

        KeyguardManager keyguardManager =
                (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager =
                (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

        return fingerprintManager != null
                && fingerprintManager.isHardwareDetected()
                && fingerprintManager.hasEnrolledFingerprints()
                && keyguardManager != null
                && keyguardManager.isKeyguardSecure();
    }
}