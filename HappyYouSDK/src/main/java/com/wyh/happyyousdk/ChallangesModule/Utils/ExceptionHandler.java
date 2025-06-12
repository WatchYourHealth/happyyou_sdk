package com.wyh.happyyousdk.ChallangesModule.Utils;

import android.content.Context;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler{

    private Thread.UncaughtExceptionHandler defaultUEH;
    Context context;

    public ExceptionHandler(Context context) {
        this.context = context;
        //Getting the the default exception handler
        //that's executed when uncaught exception terminates a thread
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {
        //Write a printable representation of this Throwable
        //The StringWriter gives the lock used to synchronize access to this writer.
        final Writer stringBuffSync = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringBuffSync);
        e.printStackTrace(printWriter);
        String stacktrace = stringBuffSync.toString();
        printWriter.close();
        String exceptionType = stacktrace.split(":")[0];
        CommonUtils.appendLog(stacktrace,context);

        // Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_message_success));
        //Used only to prevent from any code getting executed.
        // Not needed in this example
        defaultUEH.uncaughtException(t, e);
    }


}
