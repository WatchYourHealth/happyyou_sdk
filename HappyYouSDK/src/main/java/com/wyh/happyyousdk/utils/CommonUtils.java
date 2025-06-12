package com.wyh.happyyousdk.utils;


import static com.wyh.happyyousdk.SDKConstants.environment;
import static com.wyh.happyyousdk.utils.Constants.DAILY;
import static com.wyh.happyyousdk.utils.Constants.MONTHLY;
import static com.wyh.happyyousdk.utils.Constants.QUARTERLY;
import static com.wyh.happyyousdk.utils.Constants.WEEKLY;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.mikephil.charting.data.BarEntry;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.model.request.ClickEventRequest;
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsTribeUserDetail;
import com.wyh.happyyousdk.model.request.challengeTribe.StackedGraphModel;
import com.wyh.happyyousdk.model.response.ClickEventResponse;
import com.wyh.happyyousdk.model.response.MultipleGraphDataResponse;
import com.wyh.happyyousdk.model.request.graph.FetchStepsRequest;
import com.wyh.happyyousdk.trends.BarNameData;
import com.wyh.happyyousdk.trends.MarkerDataClass;
import com.wyh.happyyousdk.model.response.trends.FetchGraphResponse;
import com.wyh.happyyousdk.utils.wheelview.WheelItem;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonUtils {

    public static ProgressDialog progressDialog;
    public static List<WheelItem> wheelItems = new ArrayList<>();
    public static List<com.wyh.happyyousdk.utils.wheelview.WheelItem> mywheelItems = new ArrayList<>();

    private static boolean isDialogShownThisSession = false;
    public static String todayDate() {
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String todayDateInFormat(String pattern) {
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String getDateInFormat(Date date, String pattern) {
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String formattedDate = df.format(date);
        return formattedDate;
    }


    //Use this method to open DatePicker dialog and get date in required format and set it to TextView
    public static String openDatePickerAndSetDate(TextView textView, Context context, String requiredDateFormat) {
        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);
        int month = mcurrentTime.get(Calendar.MONTH);
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        final String[] returnValue = new String[1];

        // Launch Date Picker Dialog
        DatePickerDialog pickerDialog = new DatePickerDialog(context, (view, year1, monthOfYear, dayOfMonth) -> {
            String month1 = String.valueOf(monthOfYear + 1);
            String newDay;
            if (String.valueOf(dayOfMonth).length() == 1) {
                newDay = "0" + dayOfMonth;
            } else {
                newDay = String.valueOf(dayOfMonth);
            }

            String wholeDate = newDay + " " + month1 + " " + year1;

            String newDate = CommonUtils.formatDateFromString("dd MM yyyy", requiredDateFormat, wholeDate);

            textView.setText(newDate);

            returnValue[0] = newDate;

        }, year, month, day);

        pickerDialog.getDatePicker().setMaxDate(mcurrentTime.getTimeInMillis());

        pickerDialog.show();

        return returnValue[0];

    }

    public static String convertDateFormat(String inputDate) {
        String outputDate = "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            outputDate = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static String convertDateIntoMonthString(String imputDate) {
        String dateTime = "";
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLLL dd yyyy");
            dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String formatDateFromString(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;

    }

    public static String DateFromString(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;

    }

    public static String nextDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 6);
        Date c = calendar.getTime();
        int i = calendar.get(Calendar.DATE);
        //System.out.println("Current time => " + i);
        /*int newDateValue = i + 1;
        System.out.println("Current time => " + newDateValue);*/

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static void getStartEndOFWeek(int enterWeek, int enterYear) {
//enterWeek is week number
//enterYear is year
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMM yyyy"); // PST`
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        System.out.println("...date..." + startDateInStr);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
        System.out.println("...date..." + endDaString);
    }

    public static String getAnyDateWithFormat(int amountToAdd, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, amountToAdd);
        Date c = calendar.getTime();
        int i = calendar.get(Calendar.DATE);
        //System.out.println("Current time => " + i);
        /*int newDateValue = i + 1;
        System.out.println("Current time => " + newDateValue);*/

        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String previousDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date c = calendar.getTime();
        int i = calendar.get(Calendar.DATE);
        //System.out.println("Current time => " + i);
        /*int newDateValue = i + 1;
        System.out.println("Current time => " + newDateValue);*/

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

    public static String getCurrentTimeInFormat(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static String getCurrentTimeHoursOnly() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return sdf.format(new Date());
    }

    public static String convertTime(String time) {
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);

        DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("HH:mm");
        Date date = null;
        String output = null;
        try {
            //Conversion of input String to date
            date = df.parse(time);
            //old date format to new date format
            output = outputformat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return output;
    }

    public static String covertDateForMI(String date) {
        SimpleDateFormat spf;
        Date newDate = null;
        try {
            spf = new SimpleDateFormat("dd-MMM-yyyy");
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("yyyy-MM-dd");
        String newDateString = spf.format(newDate);
        //System.out.println(newDateString);
        return newDateString;
    }

    public static String monthsAgoDate(int months, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -months);
        Date date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String convertDateToTimeFitBit(String dateTime) {
        SimpleDateFormat spf;
        Date newDate = null;
        try {
            spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            newDate = spf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("HH:mm");
        String newTimeString = spf.format(newDate);
        //System.out.println(newTimeString);
        return newTimeString;
    }

    public static long diffTime(String sleepDateTime, String wakeDateTime) {
        long min = 0;
        long difference;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // for 12-hour system, hh should be used instead of HH
            // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
            Date date1 = simpleDateFormat.parse(sleepDateTime);
            Date date2 = simpleDateFormat.parse(wakeDateTime);

            difference = (date2.getTime() - date1.getTime()) / 1000;
            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
            min = minute + (hours * 60); // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return min;
    }

    public static String intToStringValue(int value) {
        String output;
        if (String.valueOf(value).length() == 1) {
            output = "0" + value;
        } else {
            output = String.valueOf(value);
        }
        return output;
    }

    public static String add30Minute(String time) {
        String newTime;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 30);
        newTime = df.format(cal.getTime());
        ////Log.v("Wake Time ", time + "," + newTime);
        return newTime;
    }

    public static String convertMinutesIntoHour(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        String convertedHour = "";
        if (minutes < 10) {
            convertedHour = hours + ":0" + minutes + " Hrs";
        } else {
            convertedHour = hours + ":" + minutes + " Hrs";
        }
        return convertedHour;
    }

    public static String convertMinutesIntoHourInteger(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        String convertedHour = "";
        if (minutes < 10) {
            convertedHour = hours + ".0" + minutes;
        } else {
            convertedHour = hours + "." + minutes;
        }
        return convertedHour;
    }

    private int getMonthlySteps(int i, List<String> arrayList) {
        int value = 0;
        value = Integer.parseInt(arrayList.get(i));
        //////Log.v("Week Value ", "" + value);
        return value;
    }

    public static boolean isAvailable(Context context) {
        if (context == null)
            return false;

        NetworkInfo networkInfo = getNetworkInfo(context);
        // return (networkInfo != null && networkInfo.isConnected()) ? isOnline() : false;
        return (networkInfo != null && networkInfo.isConnected()) ? true : false;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }
        return null;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidMobNo(String mobNumber) {
        return mobNumber.startsWith("1") || mobNumber.startsWith("2") || mobNumber.startsWith("3") || mobNumber.startsWith("4") || mobNumber.startsWith("5") || mobNumber.startsWith("6")
                || mobNumber.startsWith("7") || mobNumber.startsWith("8") || mobNumber.startsWith("9");
    }

    public static boolean isValidEmail(String email) {
        //        String PATTERN_EMAIL = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
//                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
//                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{1,50})$";
//        return Pattern.compile(PATTERN_EMAIL, Pattern.CASE_INSENSITIVE).matcher(email.getText().toString()
//        .trim()).matches();

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validateLetters(String txt) {
        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();
        inputMethodManager.toggleSoftInputFromWindow(
                currentFocusedView.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    public static boolean isInterNetAvailable(Context context) {
        if (context == null)
            return false;

        NetworkInfo networkInfo = getNetworkInfo(context);
        // return (networkInfo != null && networkInfo.isConnected()) ? isOnline() : false;
        return (networkInfo != null && networkInfo.isConnected()) ? true : false;
    }

    public static void showRippleEffect(View view, int corner) {
        MaterialRippleLayout.on(view)
                .rippleColor(Color.TRANSPARENT)
                .rippleAlpha(0.2f)
                .rippleRoundedCorners(corner)
                .rippleHover(true)
                .rippleOverlay(true)
                .rippleDuration(350)
                .ripplePersistent(false)
                .create();
    }

    public static void hideRippleEffect(View view, boolean visible) {
        MaterialRippleLayout materialRippleLayout = null;
        if (!visible) {
            materialRippleLayout = MaterialRippleLayout.on(view).create();
            if (materialRippleLayout.getParent() != null) {
                ((ViewGroup) materialRippleLayout.getParent()).removeView(materialRippleLayout); // <- fix
            }
        }
    }

    public static String convertDateToString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static List<String> getStartAndEndDateForWeek() {
        List<String> strings = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        strings.add(startDateInStr);
        calendar.add(Calendar.DATE, 7);
        Date endDate = calendar.getTime();
        String endDateInStr = formatter.format(endDate);
        strings.add(endDateInStr);
        return strings;
    }

    public static List<String> getStartAndEndDateForWeekInFormat(String pattern) {
        List<String> strings = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        strings.add(startDateInStr);
        calendar.add(Calendar.DATE, 7);
        Date endDate = calendar.getTime();
        String endDateInStr = formatter.format(endDate);
        strings.add(endDateInStr);
        return strings;
    }

    public static void getStartAndEndDateForMonth(FetchStepsRequest.DateRange dateRange) {
        Calendar calendar = Calendar.getInstance();
//            int weekCount = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDateInStr = formatter.format(startDate);
        //System.out.println("Month Start Date : " + startDateInStr);

        //calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
        //System.out.println("Month End Date : " + endDaString);
        dateRange.setStartDate(startDateInStr);
        dateRange.setEndDate(endDaString);
    }

    public static void getStartAndEndDateForYear(FetchStepsRequest.DateRange dateRange) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date startDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDateInStr = formatter.format(startDate);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);

        dateRange.setStartDate(startDateInStr);
        dateRange.setEndDate(endDaString);

        ////Log.d("year_date", "Start date: " + startDateInStr + ", End Date: " + endDaString);
    }

    public static class GetDatesForWeek {
        public static Calendar calendar = Calendar.getInstance();

        public static List<String> getCurrentWeek() {
            List<String> strings = new ArrayList<>();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = calendar.getTime();
            String startDateInStr = formatter.format(startDate);
            calendar.add(Calendar.DATE, 6);
            Date endDate = calendar.getTime();
            String endDateInStr = formatter.format(endDate);

            calendar.add(Calendar.DATE, 1);
            Date endDateForAPI = calendar.getTime();
            String endDateForAPIStr = formatter.format(endDateForAPI);

            strings.add(startDateInStr);
            strings.add(endDateInStr);
            strings.add(endDateForAPIStr);
            return strings;
        }

        public static List<String> getNextWeek(String currentEndDate) {
            List<String> strings = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = null;
            try {
                startDate = formatter.parse(currentEndDate);
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, 1);
                startDate = calendar.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startDateInStr = formatter.format(startDate);
            calendar.add(Calendar.DATE, 6);
            Date endDate = calendar.getTime();
            String endDateInStr = formatter.format(endDate);

            calendar.add(Calendar.DATE, 1);
            Date endDateForAPI = calendar.getTime();
            String endDateForAPIStr = formatter.format(endDateForAPI);

            strings.add(startDateInStr);
            strings.add(endDateInStr);
            strings.add(endDateForAPIStr);

            return strings;
        }

        public static List<String> getPreviousWeek(String currentStartDate) {
            List<String> strings = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = null;
            try {
                startDate = formatter.parse(currentStartDate);
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, -7);
                startDate = calendar.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startDateInStr = formatter.format(startDate);
            calendar.add(Calendar.DATE, 6);
            Date endDate = calendar.getTime();
            String endDateInStr = formatter.format(endDate);

            calendar.add(Calendar.DATE, 1);
            Date endDateForAPI = calendar.getTime();
            String endDateForAPIStr = formatter.format(endDateForAPI);

            strings.add(startDateInStr);
            strings.add(endDateInStr);
            strings.add(endDateForAPIStr);

            return strings;
        }
    }

    public static class GetDatesForMonth {
        public static Calendar calendar = Calendar.getInstance();

        public static List<String> getCurrentMonth() {
            List<String> strings = new ArrayList<>();
            /*calendar.set(Calendar.DATE, 1);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = calendar.getTime();
            String startDateInStr = formatter.format(startDate);
            calendar.add(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
            Date endDate = calendar.getTime();
            String endDateInStr = formatter.format(endDate);

            calendar.add(Calendar.DATE, 1);
            Date endDateForAPI = calendar.getTime();
            String endDateForAPIStr = formatter.format(endDateForAPI);*/


            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date endDate = calendar.getTime();
            String endDateInStr = formatter.format(endDate);
            calendar.add(Calendar.DATE, -89);
            Date startDate = calendar.getTime();
            String startDateInStr = formatter.format(startDate);

            calendar.add(Calendar.DATE, 89);
            Date endDateForAPI = calendar.getTime();
            String endDateForAPIStr = formatter.format(endDateForAPI);

            strings.add(startDateInStr);
            strings.add(endDateInStr);
            strings.add(endDateInStr);

            //Log.d("Month_New", new Gson().toJson(strings));
            return strings;
        }

        public static List<String> getNextMonth() {
            List<String> strings = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            /*Date startDate = null;
            try {
                startDate = formatter.parse(currentEndDate);
                calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();
            String startDateInStr = formatter.format(startDate);
            calendar.add(Calendar.DATE, 89);
            Date endDate = calendar.getTime();
            String endDateInStr = formatter.format(endDate);

            //Log.d("Month_next", "Start: " + startDateInStr + "  End: " + endDateInStr);

            /*calendar.add(Calendar.DATE, 1);
            Date endDateForAPI = calendar.getTime();
            String endDateForAPIStr = formatter.format(endDateForAPI);*/

            strings.add(startDateInStr);
            strings.add(endDateInStr);
            strings.add(endDateInStr);

            return strings;
        }

        public static List<String> getPreviousMonth() {
            List<String> strings = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = null;
            /*try {
                startDate = formatter.parse(currentStartDate);
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, -calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                startDate = calendar.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            calendar.add(Calendar.DATE, -90);
//            calendar.set(Calendar.DATE, 1);
            Date endDate = calendar.getTime();
            String endDateInStr = formatter.format(endDate);
            calendar.add(Calendar.DATE, -89);
            startDate = calendar.getTime();
            String startDateInStr = formatter.format(startDate);

            //Log.d("Month_previous", "Start: " + startDateInStr + "  End: " + endDateInStr);

            calendar.add(Calendar.DATE, 90);
            Date endDateForAPI = calendar.getTime();
            String endDateForAPIStr = formatter.format(endDateForAPI);

            strings.add(startDateInStr);
            strings.add(endDateInStr);
            strings.add(endDateInStr);

            return strings;
        }
    }

    public static String[] getWeekStrings(String pattern, Resources resources) {
        String[] week = new String[24];
        if (pattern.equals(DAILY)) {
            week = new String[]{resources.getString(R.string.mon), resources.getString(R.string.tue),
                    resources.getString(R.string.wed), resources.getString(R.string.thu), resources.getString(R.string.fri), resources.getString(R.string.sat), resources.getString(R.string.sunday)};
        } else if (pattern.equals(WEEKLY)) {
            week = new String[]{resources.getString(R.string.week1), resources.getString(R.string.week2), resources.getString(R.string.week3),
                    resources.getString(R.string.week4), resources.getString(R.string.week5), resources.getString(R.string.week6)};
        } else if (pattern.equals(QUARTERLY)) {
            week = new String[]{resources.getString(R.string.quarter1), resources.getString(R.string.quarter2), resources.getString(R.string.quarter3),
                    resources.getString(R.string.quarter4)};
        } else if (pattern.equals(MONTHLY)) {
            week = new String[]{resources.getString(R.string.jan), resources.getString(R.string.feb), resources.getString(R.string.mar), resources.getString(R.string.apr),
                    resources.getString(R.string.may), resources.getString(R.string.jun), resources.getString(R.string.jul), resources.getString(R.string.aug),
                    resources.getString(R.string.sep), resources.getString(R.string.oct), resources.getString(R.string.nov), resources.getString(R.string.dec)};
        } else {
            week = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        }
        return week;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getAge(int year, int month, int dayOfMonth) {

        return Period.between(
                LocalDate.of(year, month, dayOfMonth),
                LocalDate.now()
        ).getYears();
    }

    public static String[] getWeekWithDates(String pattern, List<String> weekRange, Resources resources) {
        String[] week = new String[24];
        List<String> weekDates = new ArrayList<>();
        List<String> weekStartDates = new ArrayList<>();
        List<String> finalWeekRanges = new ArrayList<>();

        if (pattern.equals(DAILY)) {
            week = new String[]{resources.getString(R.string.mon), resources.getString(R.string.tue),
                    resources.getString(R.string.wed), resources.getString(R.string.thu), resources.getString(R.string.fri), resources.getString(R.string.sat), resources.getString(R.string.sunday)};
        } else if (pattern.equals(WEEKLY)) {
            week = new String[weekRange.size()];
            for (int i = 0; i < weekRange.size(); i++) {
                week[i] = weekRange.get(i);
            }
        } else if (pattern.equals(QUARTERLY)) {
            week = new String[]{resources.getString(R.string.quarter1), resources.getString(R.string.quarter2), resources.getString(R.string.quarter3),
                    resources.getString(R.string.quarter4)};
        } else if (pattern.equals(MONTHLY)) {
            week = new String[]{resources.getString(R.string.jan), resources.getString(R.string.feb), resources.getString(R.string.mar), resources.getString(R.string.apr),
                    resources.getString(R.string.may), resources.getString(R.string.jun), resources.getString(R.string.jul), resources.getString(R.string.aug),
                    resources.getString(R.string.sep), resources.getString(R.string.oct), resources.getString(R.string.nov), resources.getString(R.string.dec)};
        } else {
            week = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        }
        return week;
    }

    public static ArrayList<BarEntry> getWeeklyDataBar(FetchGraphResponse stepsResponse, Context context) {
        ArrayList<BarEntry> entries1 = new ArrayList<>();

        for (int i = 0; i < stepsResponse.getData().getDataPoints().size(); i++) {
            float[] steps = new float[stepsResponse.getData().getDataPoints().size()];

            steps[i] = Float.parseFloat(String.valueOf(stepsResponse.getData().getDataPoints().get(i).getPoint()));
            entries1.add(new BarEntry(i, steps));
        }
        return entries1;

    }

    public static ArrayList<BarEntry> getWeeklyTribeDataBar(List<GetCommunityRankDetailsTribeUserDetail> response) {
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        List<String> names = new ArrayList<>();
        float[] steps = new float[response.size()];

        for (int i = 0; i < response.size(); i++) {
            steps[i] = Float.parseFloat(String.valueOf(response.get(i).getTotalSteps()));
            names.add(response.get(i).getName());
            MarkerDataClass markerDataClass = new MarkerDataClass(response.get(i).getName(), steps[i]);
            entries1.add(new BarEntry(i, steps[i], markerDataClass));
        }
        return entries1;

    }

    public static ArrayList<BarEntry> getWeeklyDataBarSingle(List<Double> stepsResponse, String graphName) {
        ArrayList<BarEntry> entries1 = new ArrayList<>();

        if (graphName.equals("Diagnostics")) {
            for (int j = 0; j < stepsResponse.size(); j++) {
                float[] steps = new float[stepsResponse.size()];
//                String[] usernames = new String[stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().size()];
                steps[j] = Float.parseFloat(String.valueOf(stepsResponse.get(j)));

                entries1.add(new BarEntry(j, steps));
            }
        } else if (graphName.equals("happy insights")) {
            for (int i = 0; i < stepsResponse.size(); i++) {
                float[] steps = new float[stepsResponse.size()];
//                String[] usernames = new String[stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().size()];
                steps[i] = Float.parseFloat(String.valueOf(stepsResponse.get(i)));

                entries1.add(new BarEntry(i, steps));
            }
        }
        return entries1;

    }


    public static ArrayList<BarEntry> getWeeklyDataBarCommunity(MultipleGraphDataResponse stepsResponse, String graphName) {
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        int[] barColors = new int[]{R.color.blue_cyan, R.color.blue, R.color.orange, R.color.happy_dark_grey,
                R.color.btn_blue, R.color.dark_pink, R.color.light_blue, R.color.light_orange,
                R.color.purple_500, R.color.kotakDarkBrown};
        int colorQuantity = 0;
        if (graphName.equals("Diagnostics")) {
            for (int j = 0; j < stepsResponse.getData().getDiagnosticGraph().getRecordDate().size(); j++) {
                float[] steps = new float[stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().size()];
                float[] actualPoints = new float[stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().size()];
                List<String> users = new ArrayList<>();

                for (int i = 0; i < stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().size(); i++) {
                    steps[i] = Float.parseFloat(String.valueOf(stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().get(i).getDiffpoint()));
                    actualPoints[i] = Float.parseFloat(String.valueOf(stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().get(i).getPoint()));
                    if (stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().get(i).getName() != null) {
                        if (!users.contains(stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().get(i).getName()))
                            users.add(stepsResponse.getData().getDiagnosticGraph().getRecordDate().get(j).getPoint().get(i).getName().split(" ")[0]);
                    }

                }
                MarkerDataClass markerDataClass = new MarkerDataClass(users, actualPoints);
                entries1.add(new BarEntry(j, actualPoints, markerDataClass));
            }
        } else if (graphName.equals("happy insights")) {
            for (int j = 0; j < stepsResponse.getData().getHappyinsights().getDataPoints().size(); j++) {
                float[] steps = new float[stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().size()];
                float[] actualPoints = new float[stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().size()];
                List<String> users = new ArrayList<>();

                for (int i = 0; i < stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().size(); i++) {
                    steps[i] = Float.parseFloat(String.valueOf(stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().get(i).getDiffpoint()));
                    actualPoints[i] = Float.parseFloat(String.valueOf(stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().get(i).getPoint()));
                    if (stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().get(i).getName() != null) {
                        if (!users.contains(stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().get(i).getName())) {
                            users.add(stepsResponse.getData().getHappyinsights().getDataPoints().get(j).getPoint().get(i).getName().split(" ")[0]);
                        }
                    }
                }
                MarkerDataClass markerDataClass = new MarkerDataClass(users, actualPoints);
                entries1.add(new BarEntry(j, actualPoints, markerDataClass));
            }
        }


//        for (int i = 0; i < stepsResponse.getData().getSt().getDataPoints().size(); i++) {
//            float[] steps = new float[stepsResponse.getData().getSt().getDataPoints().size()];
//
//            steps[i] = Float.parseFloat(String.valueOf(stepsResponse.getData().getSt().getDataPoints().get(i).getPoint().get(i).getPoint()));
//            entries1.add(new BarEntry(i, steps));
//        }
        return entries1;

    }

    public static ArrayList<BarEntry> getWeeklyDataBarChallenges(StackedGraphModel stepsResponse) {
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        int[] barColors = new int[]{R.color.blue_cyan, R.color.blue, R.color.orange, R.color.happy_dark_grey,
                R.color.btn_blue, R.color.dark_pink, R.color.light_blue, R.color.light_orange,
                R.color.purple_500, R.color.kotakDarkBrown};
        int colorQuantity = 0;
        for (int j = 0; j < stepsResponse.getDataPoints().size(); j++) {
            float[] steps = new float[stepsResponse.getDataPoints().get(j).getPoint().size()];
            float[] actualPoints = new float[stepsResponse.getDataPoints().get(j).getPoint().size()];
            List<String> users = new ArrayList<>();

            for (int i = 0; i < stepsResponse.getDataPoints().get(j).getPoint().size(); i++) {
                steps[i] = Float.parseFloat(String.valueOf(stepsResponse.getDataPoints().get(j).getPoint().get(i).getDiffpoint()));
                actualPoints[i] = Float.parseFloat(String.valueOf(stepsResponse.getDataPoints().get(j).getPoint().get(i).getPoint()));
                if (stepsResponse.getDataPoints().get(j).getPoint().get(i).getName() != null) {
                    if (!users.contains(stepsResponse.getDataPoints().get(j).getPoint().get(i).getName())) {
                        users.add(stepsResponse.getDataPoints().get(j).getPoint().get(i).getName().split(" ")[0]);
                    }
                }
            }
            MarkerDataClass markerDataClass = new MarkerDataClass(users, actualPoints);
            entries1.add(new BarEntry(j, actualPoints, markerDataClass));
        }

        return entries1;

    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static Bitmap convertBase64ToBitmap(String imageBase64) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }


    public static String getBaseUrlForAPI(Context context) {
        String url;
        if (environment.contentEquals("debug")) {
            url = context.getString(R.string.test) + context.getString(R.string.a) + context.getString(R.string.b);
//            url = context.getString(R.string.prod_api_url);
        } else if (environment.contentEquals("uat")) {
            url = context.getString(R.string.test) + context.getString(R.string.a) + context.getString(R.string.b);
        } else {
            url = context.getString(R.string.test) + context.getString(R.string.a) + context.getString(R.string.b);
        }
        return url;
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String compressImage(Context context, Bitmap bitmap, String imageName) {

        /*create a file with name as current time stamp in breakin folder*/
//        File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOWNLOADS), "/ILTakeCare/breakInWaterMark/"+imageName+".jpg");
        File pictureFile = new File(imageName);

        try {
            pictureFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return pictureFile.getAbsolutePath();
    }


    public static void compress(Bitmap bitmap, double maxSize) {
        // bitmapbitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        // KB
        double mid = b.length / 1024;
        // bitmap
        double i = mid / maxSize;
        // bitmap
        if (i > 1) {
            //
            //
            bitmap = scale(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
    }

    public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
        // src
        float width = src.getWidth();
        float height = src.getHeight();
        // matrix
        Matrix matrix = new Matrix();
        //
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //
        matrix.postScale(scaleWidth, scaleHeight);
        //
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height,
                matrix, true);
    }

    public static Bitmap scale(Bitmap src, Matrix scaleMatrix) {
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), scaleMatrix, true);
    }

    public static Bitmap scale(Bitmap src, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    public static Bitmap scale(Bitmap src, float scale) {
        return scale(src, scale, scale);
    }

    public static Bitmap getAngledImage(Bitmap bitmap, String filePathFromImage) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePathFromImage);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap newBitmap = null;
        int rotationAngle = 0;


        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                //newBitmap = rotateImage(bitmap, 90);
                rotationAngle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                //newBitmap = rotateImage(bitmap, 180);
                rotationAngle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                //newBitmap = rotateImage(bitmap, 270);
                rotationAngle = 270;
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                //newBitmap = bitmap;
                break;
            case ExifInterface.ORIENTATION_UNDEFINED:
                //newBitmap = bitmap;
                break;
        }
        if (rotationAngle != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationAngle);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle(); // Clean up the original bitmap to free memory
            return rotatedBitmap;
        } else {
            return bitmap; // No rotation needed
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static Double stringtoDouble(String str) {
        Double toBeTruncated = new Double(str);
        return new BigDecimal(toBeTruncated).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double onlyOneDecimalPoint(Double str) {
        return new BigDecimal(str).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static boolean isSameYear(String date1, String date2) {
        boolean result = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObj1 = sdf.parse(date1);
            Date dateObj2 = sdf.parse(date2);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(dateObj1);
            cal2.setTime(dateObj2);
            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                if (!isDateInCurrentYear(dateObj1)) {
                    result = false;
                } else if (!isDateInCurrentYear(dateObj1)) {
                    result = false;
                } else {
                    result = true;
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isDateInCurrentYear(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date);
        cal2.setTime(new Date());
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }


    public static File compressImage(Context context, Uri uri) {
        Bitmap bmp = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String timestamp = "" + System.currentTimeMillis();

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Happy/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, "happyU_image_" + timestamp + ".png");
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] bitmapdata = baos.toByteArray();
            folder.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Log.d("comparedata file1", file.length() + "");

        return file;
    }

    public static File compressImageBase64(Bitmap image, String path, Context context, Uri uri) {
        File file = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            image = Bitmap.createScaledBitmap(
                    getAngledImage(image, path),
                    1000, 1125, false);
            image.compress(Bitmap.CompressFormat.JPEG, 95, baos);// 100baos

            String timestamp = "" + System.currentTimeMillis();

            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Happy/");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            file = new File(folder, "happyU_image_" + timestamp + ".png");
            try {
                //image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                //image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] bitmapdata = baos.toByteArray();
                folder.createNewFile();
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ByteArrayInputStream isBm = new ByteArrayInputStream(
                    baos.toByteArray());// baosByteArrayInputStream
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ByteArrayInputStream

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static Bitmap byteArrayOutputStreamToBitmap(ByteArrayOutputStream stream) {
        byte[] byteArray = stream.toByteArray();  // Step 1: Convert to byte array
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);  // Step 2: Convert to Bitmap
    }

    public static boolean isValidAadharNumber(String aadharNumber) {
        if (aadharNumber.length() != 12) {
            return false;
        }

        // Check if Aadhar number consists only of digits
        if (!aadharNumber.matches("\\d+")) {
            return false;
        }

        // Check if the first digit is not zero
        if (aadharNumber.charAt(0) == '0' || aadharNumber.charAt(0) == '1') {
            return false;
        }

        // Perform Aadhar number validation algorithm
    /*int aadharSum = 0;
    for (int i = 0; i < 12; i++) {
        aadharSum += Character.getNumericValue(aadharNumber.charAt(i)) * (12 - i);
    }*/

        // Validate Aadhar number
        return true;
    }

    public static File compressImageToJPEG(Context context, Uri uri) {

        Bitmap bmp = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String timestamp = "" + System.currentTimeMillis();

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Happy/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, "happyU_image_" + timestamp + ".png");
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] bitmapdata = baos.toByteArray();
            folder.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Log.d("comparedata file1", file.length() + "");

        return file;
    }


    public static void deleteImage(String fileName) {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Happy/");
        File file = new File(folder, fileName);
        boolean deleted = file.delete();
    }

    public static String generateRandonNumber(int len) {
        String result = "";
        try {
            SecureRandom sr = new SecureRandom();
            result = (sr.nextInt(9) + 1) + "";
            for (int i = 0; i < len - 2; i++) result += sr.nextInt(10);
            result += (sr.nextInt(9) + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void showProgressDialige(Context context) {
        try {
            progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
            if (context != null && progressDialog != null) {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void dismissDialoge() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void WriteToFile(String content, Context context) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newDir = new File(context.getFilesDir() + "/" + "Logs.txt");
        try {
            if (!newDir.exists()) {
                newDir.getParentFile().mkdirs();
            }
            FileOutputStream writer = new FileOutputStream(new File(path, "Logs.txt"));
            writer.write(content.getBytes());
            writer.close();
            Log.e("TAG", "Wrote to file: " + "Logs.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void appendLog(String text, Context context) {

        try {
            File logFile = new File(context.getFilesDir() + "/" + "Logs.txt");
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(text);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getRealPathFromURI(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getBaseUrlForABHA(Context context) {
        String url = "";
        if (environment.contentEquals("debug")) {
            url = context.getString(R.string.test_api_url_abha);
        } else if (environment.contentEquals("uat")) {
            url = context.getString(R.string.uat_api_url_abha);
        } else {
            url = context.getString(R.string.prod_api_url_abha);
        }
        return url;
    }


    public static void clickEvent(Context context, String eventType, String subType, String subTypeID, String action, String content) {
        try {
            ClickEventRequest clickEventRequest = new ClickEventRequest(eventType, subType, subTypeID, action, content);
            APIInterface apiInterface = RetrofitHandler.apiInterface();
            apiInterface.clickEvent(SharedPref.getAuthToken(), clickEventRequest).enqueue(new Callback<ClickEventResponse>() {
                @Override
                public void onResponse(Call<ClickEventResponse> call, Response<ClickEventResponse> response) {
                    Log.d("AuthToken", "response " + response.isSuccessful());
                }

                @Override
                public void onFailure(Call<ClickEventResponse> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getCurrentStampDiff(String currentStamp, String pattern) {
        long daysDiff = 0;
        if (!currentStamp.isEmpty()) {
            Calendar todayCalendar = Calendar.getInstance();
            Date todayDate = todayCalendar.getTime();
            Date currentDate;
            try {
                currentDate = new SimpleDateFormat(pattern, Locale.ENGLISH).parse(currentStamp);
                if (currentDate != null) {
                    long msDiff = todayDate.getTime() - currentDate.getTime();
                    daysDiff = TimeUnit.MILLISECONDS.toHours(msDiff);
                }
            } catch (Exception e) {
                Log.d("currentStamp", e.getMessage());
            }
        }

        return daysDiff;
    }

    public static boolean isValidAbhaAddress(String address, Context context) {
        if (address == null || address.length() < 8 || address.length() > 18) {
            // Address must be at least 4 characters long
            Toast.makeText(context, "Address must be between 8 and 18 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Address must not start with a number
        /*if (Character.isDigit(address.charAt(0))) {
            Toast.makeText(context, "Address must not start with a number", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        // Address must not start or end with a dot
        if (address.charAt(0) == '.' || address.charAt(0) == '_' ||
                address.charAt(address.length() - 1) == '.' ||
                address.charAt(address.length() - 1) == '_') {
            Toast.makeText(context, "Special characters cannot be in the beginning or at the end", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if the address is numeric-only, for 14-digit ABHA number
        /*if (address.matches("\\d{14}@abdm")) {
            Toast.makeText(context, "Abha Address is invalid", Toast.LENGTH_SHORT).show();
            return true; // 14-digit ABHA Number
        }*/

        // Check if the address contains only valid characters (alphabets, digits, dot)
        if (!address.matches("[a-zA-Z0-9._]+")) {
            showCustomToast(context, "ABHA Address contains only valid characters (alphabets, digits, dot, underscore)");
            return false;
        }
        int dotCount = address.length() - address.replace(".", "").length();
        int underscoreCount = address.length() - address.replace("_", "").length();

        if (dotCount > 1 || underscoreCount > 1) {
            Toast.makeText(context, "You can only use 1 dot or 1 underscore", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static void showCustomToast(Context context, String message) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        // Get the TextView from the custom layout and set the message
        TextView toastTextView = layout.findViewById(R.id.toast_message);
        toastTextView.setText(message);

        // Create and show the Toast
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG); // Use LENGTH_LONG for longer messages
        toast.setView(layout);
        toast.show();
    }

    // List of common date formats to try
    private static final List<String> DATE_FORMATS = Arrays.asList(
            "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", "dd-MM-yyyy",
            "yyyy/MM/dd", "dd MMM yyyy", "dd-MMM-yyyy", "yyyy.MM.dd"
    );

    public static String formatDate(String inputDate, String outputFormat) {
        for (String format : DATE_FORMATS) {
            try {
                // Try to parse the input date with the current format
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(format);
                LocalDate date = LocalDate.parse(inputDate, inputFormatter);

                // If successful, format the date to the desired output format
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
                return date.format(outputFormatter);
            } catch (DateTimeParseException e) {
                // If parsing fails, continue to the next format
                continue;
            }
        }
        // If no format matches, throw an exception or return a default value
        return inputDate;
    }

    public static String formatDate(String inputDate, String inputFormat, String outputFormat) {

        try {
            // Try to parse the input date with the current format
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);

            // If successful, format the date to the desired output format
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // If parsing fails, continue to the next format
            return inputDate;
        }

        // If no format matches, throw an exception or return a default value

    }

    public static String convertListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            return String.join(",", list); // Converts the list to a comma-separated string
        }
    }

    private static void saveImage(Context context, Bitmap bitmap, String fileName) {
        try {
            File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "HappyYou");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Log.d("Image Saved", "Path: " + file.getAbsolutePath());
            Toast.makeText(context, "Image Download successful ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Save Error", "Failed to save image", e);
        }
    }

    public static void downloadImage(Context context, String imageUrl, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setTitle("Downloading File");
        request.setDescription("Saving " + fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            long downloadId = manager.enqueue(request);

            // Register a broadcast receiver to listen for completion
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context ctx, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        Toast.makeText(context, "Download complete: " + fileName, Toast.LENGTH_SHORT).show();
                        context.unregisterReceiver(this);  // Unregister receiver
                    }
                }
            }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
        }
    }

    public static boolean isDateExpired(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            try {
                Date inputDate = sdf.parse(dateStr);
                Date currentDate = new Date();
                return inputDate.before(currentDate); // Returns true if expired
            } catch (ParseException e) {
                e.printStackTrace();
                return false; // Invalid date format
            }
        } else {
            return false;
        }
    }
    public static void downloadAndViewFile(Context context, String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        File destinationFile = new File(context.getExternalCacheDir(), fileName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setTitle("Downloading " + fileName);
        request.setDescription("Saving file...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(destinationFile));

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = manager.enqueue(request);

        // Listen for download complete
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {
                    context.unregisterReceiver(this);
                    // Open the file
                    Uri contentUri = FileProvider.getUriForFile(context,
                            context.getPackageName() + ".provider", destinationFile);

                    String mimeType = getMimeType(fileName);
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(contentUri, mimeType);
                    viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        context.startActivity(viewIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No app found to open this file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED);
    }
    public static String getMimeType(String fileName) {
        String mimeType = "*/*";
        if (fileName.endsWith(".pdf")) mimeType = "application/pdf";
        else if (fileName.endsWith(".doc")) mimeType = "application/msword";
        else if (fileName.endsWith(".docx")) mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        return mimeType;
    }

    public static boolean showDailyDialogPerSession(Context context) {
        // Show the dialog only if it hasn't been shown in this session
        if (!isDialogShownThisSession) {
            isDialogShownThisSession = true;

            // Optionally show the dialog here
            // showUserDialog(context);

            return true;
        }

        // Dialog has already been shown in this session
        return false;
    }

    public static boolean showDailyDialog(Context context) {
        long lastDialogTime = SharedPref.getLastLogindate();

        Calendar lastDialogCalendar = Calendar.getInstance();
        lastDialogCalendar.setTimeInMillis(lastDialogTime);
        Calendar currentCalendar =
                Calendar.getInstance();

        // Check if a day has passed since the last dialog
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) != lastDialogCalendar.get(Calendar.DAY_OF_YEAR)
                || currentCalendar.get(Calendar.YEAR) != lastDialogCalendar.get(Calendar.YEAR)) {
            // Show the dialog
            //showUserDialog(context);

            // Update the last dialog time
            SharedPref.setLastLoginDate(System.currentTimeMillis());
            return true;
        }
        return false;
    }
}



