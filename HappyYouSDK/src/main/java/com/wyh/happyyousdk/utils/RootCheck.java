package com.wyh.happyyousdk.utils;

import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class RootCheck {
    public static boolean isDeviceRooted() {
        boolean isRooted = false;

        // Check if the build tags contain "test-keys" which indicates a rooted device
        String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            isRooted = true;
        }

        // Check if Superuser.apk file is present which indicates a rooted device
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                isRooted = true;
            }
        } catch (Exception e) {
            // Ignore any exceptions that may occur
        }

        // Check if su binary is present which indicates a rooted device
        if (!isRooted) {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                if (reader.readLine() != null) {
                    isRooted = true;
                }
            } catch (Exception e) {
                // Ignore any exceptions that may occur
            } finally {
                if (process != null) {
                    process.destroy();
                }
            }
        }

        /*if (!isRooted) {
            if (canExecuteCommand("/system/xbin/which su")
                    || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su")) {
                isRooted = true;
            }
        }*/

        return isRooted;
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }
}
