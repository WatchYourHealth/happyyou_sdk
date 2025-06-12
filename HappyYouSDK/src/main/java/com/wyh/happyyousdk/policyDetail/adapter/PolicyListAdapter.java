package com.wyh.happyyousdk.policyDetail.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.AdapterPolicyCardBinding;
import com.wyh.happyyousdk.model.GetPDRPRODReq;
import com.wyh.happyyousdk.model.GetPolicyDocumentReq;
import com.wyh.happyyousdk.model.GetPremiumCertificateReq;
import com.wyh.happyyousdk.model.GetUnitLinkedPolicyStatementPROReq;
import com.wyh.happyyousdk.model.PolicyListResp;
import com.wyh.happyyousdk.policyDetail.PolicyViewMore;
import com.wyh.happyyousdk.utils.SharedPref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyListAdapter extends RecyclerView.Adapter<PolicyListAdapter.MyViewHolder> {

        Context context;
        List<PolicyListResp.Content> policyDetailsList;
       ProgressDialog progressDialog;
       APIInterface apiInterfaceWyh;
       String startDate="",endDate="";
       File outputFileFinal;
       List<String> yearsStart = new ArrayList<>();
       List<String> yearsEnd = new ArrayList<>();
       List<String> yearsRange = new ArrayList<>();

       private OnItemClickListener onAutoPayClickListener;
       private OnItemClickListener onRenewalPayClickListener;

    List<String> month = new ArrayList<>();
    String StartYear="",EndYear="",Month="",StartEndRange="";
    String PolicyType;
    String PolicyNumber;
    int lastMonthPosition=0,lastStarYearPosition=0,lastEndYearPosition=0,lastYearRangePosition=0;
    String category="";

    public PolicyListAdapter(Context context, List<PolicyListResp.Content> policyDetailsList,
                             OnItemClickListener onAutoPayClickListener) {
        this.context = context;
        this.policyDetailsList = policyDetailsList;
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        this.onAutoPayClickListener = onAutoPayClickListener;
        }

    public interface OnItemClickListener {
        void onItemClick(String clickButton);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPolicyCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_policy_card, parent, false);
        return new MyViewHolder(binding);
    }

@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
    PolicyListResp.Content data = policyDetailsList.get(position);
    String premiumDueDate, issueDate;
    //DecimalFormat format = new DecimalFormat("0.##");
    holder.binding.tvPolicyNumber.setText(data.getPolicyNo());
    holder.binding.tvPolicyHolderName.setText(data.getInsuredName());
    holder.binding.tvPolicyPremiumDue.setText(formatDate(data.getPremiumDueDate(),"yyyy-MM-dd'T'HH:mm","dd MMM yyyy"));
    holder.binding.tvPolicySumAssuredAmt.setText(data.getSumAssured());
    holder.binding.tvPolicyMaturityDate.setText(formatDate(data.getEnddate(),"yyyy-MM-dd'T'HH:mm","dd MMM yyyy"));
    if(data.getEcsStatus() != null && data.getEcsStatus().equalsIgnoreCase("Y")){
//        holder.binding.imAutoPay.setVisibility(View.VISIBLE);
       /* holder.binding.withAutoPay.setVisibility(View.VISIBLE);
        holder.binding.withoutAutoPay.setVisibility(View.GONE);*/

        holder.binding.withAutoPay.setVisibility(View.GONE);
        holder.binding.withoutAutoPay.setVisibility(View.VISIBLE);
    }else {
       /* holder.binding.withAutoPay.setVisibility(View.GONE);
        holder.binding.withoutAutoPay.setVisibility(View.VISIBLE);*/
        /*holder.binding.imAutoPay.setVisibility(View.GONE);
        holder.binding.imAutoPay.setClickable(false);
        holder.binding.imAutoPayView.setVisibility(View.GONE);*/

        holder.binding.withAutoPay.setVisibility(View.VISIBLE);
        holder.binding.withoutAutoPay.setVisibility(View.GONE);
    }
    if (data.getPolicyMasterStatus() != null && data.getPolicyMasterStatus().equalsIgnoreCase("Active")) {
        holder.binding.rlMainBg.setBackground(context.getDrawable(R.drawable.ic_policy_card_green_border));
        holder.binding.imStatus.setImageDrawable(context.getDrawable(R.drawable.ic_policy_card_active));
    }else if (data.getPolicyMasterStatus() != null &&  data.getPolicyMasterStatus().equalsIgnoreCase("Inactive")) {
        holder.binding.rlMainBg.setBackground(context.getDrawable(R.drawable.ic_policy_card_red_border));
        holder.binding.imStatus.setImageDrawable(context.getDrawable(R.drawable.ic_policy_card_expired));
    }
    if (data.getPremiumStatus()!=null&&data.getPremiumStatus().equals("Renewal Due")) {
        holder.binding.rlMainBg.setBackground(context.getDrawable(R.drawable.ic_policy_card_orange_border));
        holder.binding.imStatus.setImageDrawable(context.getDrawable(R.drawable.ic_policy_card_renewal_due));
    }

    holder.binding.imDownloadPolicy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PolicyNumber=data.getPolicyNo();
            category=data.getCategory();
            showCustomDialog();
            APILogs.INSTANCE.activityTracker("Android_POLICY_DOWNLOAD_POLICY",context);
        }
    });
    holder.binding.imDownloadPolicy1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PolicyNumber=data.getPolicyNo();
            category=data.getCategory();
            showCustomDialog();
            APILogs.INSTANCE.activityTracker("Android_POLICY_DOWNLOAD_POLICY",context);
        }
    });
    holder.binding.imAutoPay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onAutoPayClickListener.onItemClick("autoPay");
//            openUrlInBrowser("https://enach.mykotaklife.com/MandateDetails/ActivateECS");
        }
    });
    holder.binding.imRenewPolicy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onAutoPayClickListener.onItemClick("renewPolicy");

//            openUrlInBrowser("https://care.kotaklifeinsurance.com/");
        }
    });
    holder.binding.imRenewPolicy1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onAutoPayClickListener.onItemClick("renewPolicy");

//            openUrlInBrowser("https://care.kotaklifeinsurance.com/");
        }
    });

    holder.binding.tvViewMore.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            APILogs.INSTANCE.activityTracker("Android_POLICY_VIEW_MORE",context);
            Intent intent = new Intent(context, PolicyViewMore.class);
            intent.putExtra("clientID", policyDetailsList.get(position).getClientId());
            intent.putExtra("policyNo", policyDetailsList.get(position).getPolicyNo());
            intent.putExtra("ContentData", new Gson().toJson(policyDetailsList));
            intent.putExtra("intent","");
            context.startActivity(intent);
        }
    });
}
@Override
public int getItemCount() {
        return policyDetailsList.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {
    AdapterPolicyCardBinding binding;

    public MyViewHolder(@NonNull AdapterPolicyCardBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}


    public String numberFormatConverter(String amount) {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("en", "in"));
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(Double.valueOf(amount));
    }
    @SuppressLint("ClickableViewAccessibility")
    private void showCustomDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dailog_policy_download, null);
        String[] policyTypes;
        if (category!=null && category.equalsIgnoreCase("UL"))
        {
             policyTypes = new String[]{
                    "Premium Receipt",
                    "Premium Paid Certificate",
                    "Policy Document",
                    "Product Brochure",
                    "Unit linked Policy statement"
            };
        }
        else {
            policyTypes = new String[]{
                    "Premium Receipt",
                    "Premium Paid Certificate",
                    "Policy Document",
                    "Product Brochure"
            };
        }

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spinner spinner_range1 = dialogView.findViewById(R.id.spinner_range1);
        Spinner spinner_range2 = dialogView.findViewById(R.id.spinner_range2);
        Spinner spinnerMonthRange = dialogView.findViewById(R.id.spinnerMonthRange);
        Spinner spinnerYearRange = dialogView.findViewById(R.id.spinnerYearRange);
        ImageView btnClose = dialogView.findViewById(R.id.btn_close);
        ImageView btnDownload = dialogView.findViewById(R.id.btn_download);
        ImageView btnView = dialogView.findViewById(R.id.btn_view);
        //initializeSpinner(context, spinnerStartDate, "Select Start Date");
        //initializeSpinner(context, spinnerEndDate, "Select End Date");

        // Set dummy data for spinners to make them clickable
        ArrayAdapter<String> dummyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getYearsStart());
        ArrayAdapter<String> dummyAdapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getYearsEnd());
        dummyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dummyAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_range1.setAdapter(dummyAdapter);
        spinner_range2.setAdapter(dummyAdapter2);

        EditText editStartDate = dialogView.findViewById(R.id.edit_start_date);
        EditText editEndDate = dialogView.findViewById(R.id.edit_end_date);

        Spinner spinnerPolicyType = dialogView.findViewById(R.id.spinnerPolicyType);
        LinearLayout llUnitLinkedPolicyStatement = dialogView.findViewById(R.id.llUnitLinkedPolicyStatement);
        LinearLayout llPremiumCertificate = dialogView.findViewById(R.id.llPremiumCertificate);
        LinearLayout llPremiumReceipt = dialogView.findViewById(R.id.llPremiumReceipt);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, policyTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPolicyType.setAdapter(adapter);

        ArrayAdapter<String> adapterMonthRange = new ArrayAdapter<>(context, R.layout.spinner_item, getMonthsNew(0));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonthRange.setAdapter(adapterMonthRange);

        ArrayAdapter<String> adapterYearRange = new ArrayAdapter<>(context, R.layout.spinner_item, getYearsRange());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYearRange.setAdapter(adapterYearRange);

        spinnerPolicyType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = policyTypes[position];
                PolicyType= policyTypes[position];
                if (selectedItem.equals("Unit linked Policy statement")) {
                    llUnitLinkedPolicyStatement.setVisibility(View.VISIBLE);
                    llPremiumCertificate.setVisibility(View.GONE);
                    llPremiumReceipt.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.VISIBLE);
                    btnClose.setVisibility(View.VISIBLE);
                    APILogs.INSTANCE.activityTracker("Android_POLICY_UNIT_LINK_POLICY_STATEMENT",context);
                }else  if (selectedItem.equals("Premium Paid Certificate")){
                    llPremiumCertificate.setVisibility(View.VISIBLE);
                    llUnitLinkedPolicyStatement.setVisibility(View.GONE);
                    llPremiumReceipt.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.VISIBLE);
                    btnClose.setVisibility(View.VISIBLE);
                    APILogs.INSTANCE.activityTracker("Android_POICY_PREMIUM_PAID_CERTIFICATE_DOC",context);
                }else if(selectedItem.equals("Policy Document")){
                    llPremiumCertificate.setVisibility(View.GONE);
                    llUnitLinkedPolicyStatement.setVisibility(View.GONE);
                    llPremiumReceipt.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.VISIBLE);
                    btnClose.setVisibility(View.VISIBLE);
                    APILogs.INSTANCE.activityTracker("Android_POICY_DOCUMENT",context);
                }
            else if(selectedItem.equals("Premium Receipt")){
                llPremiumCertificate.setVisibility(View.GONE);
                llUnitLinkedPolicyStatement.setVisibility(View.GONE);
                    llPremiumReceipt.setVisibility(View.VISIBLE);
                    btnDownload.setVisibility(View.VISIBLE);
                    btnClose.setVisibility(View.VISIBLE);
                    APILogs.INSTANCE.activityTracker("Android_POICY_PREMIUM_RECEIPT_DOC",context);
            }
                else if(selectedItem.equals("Product Brochure")){
                    llPremiumCertificate.setVisibility(View.GONE);
                    llUnitLinkedPolicyStatement.setVisibility(View.GONE);
                    llPremiumReceipt.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.INVISIBLE);
                    //openUrlInBrowser("https://www.kotaklife.com/how-do-i/get-policy-brochure");
                    onAutoPayClickListener.onItemClick("Product Brochure");
                    APILogs.INSTANCE.activityTracker("Android_POLICY_PRODUCT_BROCHURE",context);
                    dialog.dismiss();


                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });

        spinner_range1.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spinner_range1.setSelection(lastStarYearPosition);
                    /* Toast.makeText(context, "Please select a year", Toast.LENGTH_SHORT).show();*/
                } else {
                    // Handle year selection
                    lastStarYearPosition=position;
                    StartYear = yearsStart.get(position);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
        spinner_range2.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spinner_range2.setSelection(lastEndYearPosition);
                   /* Toast.makeText(context, "Please select a year", Toast.LENGTH_SHORT).show();*/
                } else {
                    // Handle year selection
                    lastEndYearPosition=position;
                    EndYear  = yearsEnd.get(position);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });


        spinnerMonthRange.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
               if (position!=0)
               {
                   Month  = month.get(position);
                   lastMonthPosition=position;
               }
               else {
                   spinnerMonthRange.setSelection(lastMonthPosition);
               }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });

        spinnerYearRange.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinnerYearRange.setSelection(lastYearRangePosition);
                    // Handle hint selection
                    //Toast.makeText(context, "Please select a year", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle year selection
                    lastYearRangePosition=position;
                    StartEndRange = yearsRange.get(position);
                    ArrayAdapter<String> adapterMonthRange = new ArrayAdapter<>(context, R.layout.spinner_item, getMonthsNew(Integer.parseInt(StartEndRange)));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMonthRange.setAdapter(adapterMonthRange);
                    if (lastMonthPosition>month.size()-1)
                    {
                        spinnerMonthRange.setSelection(month.size()-1);
                    }
                    else {
                        spinnerMonthRange.setSelection(lastMonthPosition);
                    }
                    //Month="";
                    int  newdyear=Integer.parseInt(StartEndRange)-1;
                    StartEndRange =newdyear+"-"+StartEndRange;

                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
        int currentYear = LocalDate.now().getYear();


        editStartDate.setOnClickListener(v -> {
            openDatePicker(context, true, editStartDate);

        });
        editEndDate.setOnClickListener(v -> {
            openDatePicker(context, false, editEndDate);
        });
        // Close button with validation
        btnClose.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_CLOSE_DOC",context);
            dialog.dismiss();
        });

        btnDownload.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_DOWNLOAD_DOC",context);
            if (PolicyType.equals("Unit linked Policy statement"))
            {
                if (!startDate.isEmpty()&&!endDate.isEmpty())
                {
                    GetUnitLinkedPolicyStatementPRO("Download");
                }
                else {
                    Toast.makeText(context, "Please select Start and end date", Toast.LENGTH_SHORT).show();
                }
            }
            else if (PolicyType.equals("Premium Paid Certificate"))
            {
                if (!StartYear.isEmpty()&&!EndYear.isEmpty())
                {
                    GetPremiumCertificatePRO("Download");
                }
                else {
                    Toast.makeText(context, "Please select Start and end year", Toast.LENGTH_SHORT).show();
                }

            }
            else if (PolicyType.equals("Policy Document")){
                GetPolicyDocumentPRO("Download");

            }
            else if (PolicyType.equals("Premium Receipt")){
                if (!StartEndRange.isEmpty()&&!Month.isEmpty())
                {
                    GetPDRPROD("Download");
                }
                else {
                    Toast.makeText(context, "Please select Month and year", Toast.LENGTH_SHORT).show();
                }

            }



        });

        btnView.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_VIEW_DOC",context);
            if (PolicyType.equals("Unit linked Policy statement"))
            {
                if (!startDate.isEmpty()&&!endDate.isEmpty())
                {
                    GetUnitLinkedPolicyStatementPRO("View");
                }
                else {
                    Toast.makeText(context, "Please select Start and end date", Toast.LENGTH_SHORT).show();
                }

            }
            else if (PolicyType.equals("Premium Paid Certificate"))
            {
                if (!StartYear.isEmpty()&&!EndYear.isEmpty())
                {
                    GetPremiumCertificatePRO("View");
                }
                else {
                    Toast.makeText(context, "Please select Start and end year", Toast.LENGTH_SHORT).show();
                }

            }
            else if (PolicyType.equals("Policy Document")){
                GetPolicyDocumentPRO("View");
            }
            else if (PolicyType.equals("Premium Receipt")){
                if (!StartEndRange.isEmpty()&&!Month.isEmpty())
                {
                    GetPDRPROD("View");
                }
                else {
                    Toast.makeText(context, "Please select Month and year", Toast.LENGTH_SHORT).show();
                }

            }


        });

        dialog.show();
    }
    private void initializeSpinner(Context context, Spinner spinner, String defaultText) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{defaultText});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private List<String> getMonths() {

        String[] monthNames = {"Month","January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        for (String monthname : monthNames) {
            month.add(monthname);
        }
        return month;
    }

    private List<String> getMonthsNew(int selectedYear) {
        month.clear();
        Log.e("selectedYear",selectedYear+"");
        List<String> monthList = new ArrayList<>();

        String[] monthNames = {"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
       if (selectedYear!=0)
       {
           Calendar calendar = Calendar.getInstance();
           int currentYear = calendar.get(Calendar.YEAR);
           int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based

           month.add(monthNames[0]); // Add "Month" as the first option

           // Determine the month limit based on the selected year
           int monthLimit = (selectedYear == currentYear) ? currentMonth : 12;

           // Populate the month list
           for (int i = 1; i <= monthLimit; i++) {
               month.add(monthNames[i]);
           }
       }
       else {

           for (String monthname : monthNames) {
               month.add(monthname);
           }
       }
        // Get the current year and month


        return month;
    }
    private List<String> getYearsStart() {
        yearsStart.clear();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        yearsStart.add("Start Year"); // Add the hint at the first index

        for (int i = currentYear; i >= currentYear - 50; i--) {
            yearsStart.add(String.valueOf(i));
        }

        return yearsStart;
    }
    private List<String> getYearsEnd() {
        yearsEnd.clear();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        yearsEnd.add("End Year"); // Add the hint at the first index

        for (int i = currentYear; i >= currentYear - 50; i--) {
            yearsEnd.add(String.valueOf(i));
        }

        return yearsEnd;
    }

    private List<String> getYearsRange() {
        yearsRange.clear();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        yearsRange.add("Year"); // Add the hint at the first index

        for (int i = currentYear; i >= currentYear - 50; i--) {
            yearsRange.add(String.valueOf(i));
        }

        return yearsRange;
    }
    private void openDatePicker(Context context, boolean isStartDate, EditText spinner) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    String selectedDate = dateFormat.format(calendar.getTime());

                    if (isStartDate) {
                        startDate = selectedDate;
                        spinner.setText(startDate);
                        //Toast.makeText(context, "Start Date: " + selectedDate, Toast.LENGTH_SHORT).show();

                    } else {
                        endDate = selectedDate;
                        spinner.setText(endDate);
                        //Toast.makeText(context, "End Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }


                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }



    private boolean validateDates(Context context) {
        if (startDate == null || endDate == null) {
            Toast.makeText(context, "Please select both dates.", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            if (dateFormat.parse(startDate).after(dateFormat.parse(endDate))) {
                Toast.makeText(context, "Start date cannot be after the end date.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Unit linked Policy statement
    @SuppressLint("StaticFieldLeak")
    private void GetUnitLinkedPolicyStatementPRO(String type) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetUnitLinkedPolicyStatementPROReq request = new GetUnitLinkedPolicyStatementPROReq(startDate, endDate, PolicyNumber);
        Call<ResponseBody> call = apiInterfaceWyh.getUnitLinkedPolicyStatementPRO(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                if (response.code() == 200 && response.body() != null) {
                    new AsyncTask<Void, Void, Void>() {
                        boolean writtenToDisk = false;

                        @Override
                        protected Void doInBackground(Void... voids) {
                            if (type.equals("Download"))
                            {
                                writtenToDisk = writeResponseBodyToDisk(response.body());
                                Log.d("PolicyDownload", "File download successful? " + writtenToDisk);
                            }
                            else {
                                downloadAndViewPdf(response.body());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (type.equals("Download"))
                            {
                                if (writtenToDisk) {
                                    openPDFFile();
                                } else {
                                    Toast.makeText(context, "Failed to write file to disk", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }.execute();
                } else {
                    Toast.makeText(context, "Report not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Premium Paid Certificate
    private void GetPremiumCertificatePRO(String type) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetPremiumCertificateReq request = new GetPremiumCertificateReq(SharedPref.getClientId(),StartYear+"-"+EndYear );
        Call<ResponseBody> call = apiInterfaceWyh.getPremiumCertificatePRO(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                if (response.code() == 200 && response.body() != null) {
                    new AsyncTask<Void, Void, Void>() {
                        boolean writtenToDisk = false;

                        @Override
                        protected Void doInBackground(Void... voids) {
                            if (type.equals("Download"))
                            {
                                writtenToDisk = writeResponseBodyToDisk(response.body());
                                Log.d("PolicyDownload", "File download successful? " + writtenToDisk);
                            }
                            else {
                                downloadAndViewPdf(response.body());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (type.equals("Download"))
                            {
                                if (writtenToDisk) {
                                    openPDFFile();
                                } else {
                                    Toast.makeText(context, "Failed to write file to disk", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }.execute();
                } else {
                    Toast.makeText(context, "Report not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Policy Document
    private void GetPolicyDocumentPRO(String type) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetPolicyDocumentReq request = new GetPolicyDocumentReq(PolicyNumber,SharedPref.getClientId());
        Call<ResponseBody> call = apiInterfaceWyh.getPolicyDocumentPRO(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                if (response.code() == 200 && response.body() != null) {
                    new AsyncTask<Void, Void, Void>() {
                        boolean writtenToDisk = false;

                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected Void doInBackground(Void... voids) {
                            if (type.equals("Download"))
                            {
                                writtenToDisk = writeResponseBodyToDisk(response.body());
                                Log.d("PolicyDownload", "File download successful? " + writtenToDisk);
                            }
                            else {
                                downloadAndViewPdf(response.body());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (type.equals("Download"))
                            {
                                if (writtenToDisk) {
                                    openPDFFile();
                                } else {
                                    Toast.makeText(context, "Failed to write file to disk", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }.execute();
                } else {
                    Toast.makeText(context, "Report not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Premium Receipt
    private void GetPDRPROD(String type) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetPDRPRODReq request = new GetPDRPRODReq(SharedPref.getClientId(),PolicyNumber,StartEndRange,Month);
        Call<ResponseBody> call = apiInterfaceWyh.getPDRPROD(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                if (response.code() == 200 && response.body() != null) {
                    new AsyncTask<Void, Void, Void>() {
                        boolean writtenToDisk = false;

                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected Void doInBackground(Void... voids) {
                            if (type.equals("Download"))
                            {
                                writtenToDisk = writeResponseBodyToDisk(response.body());
                                Log.d("PolicyDownload", "File download successful? " + writtenToDisk);
                            }
                            else {
                                downloadAndViewPdf(response.body());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (type.equals("Download"))
                            {
                                if (writtenToDisk) {
                                    openPDFFile();
                                } else {
                                    Toast.makeText(context, "Failed to write file to disk", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }.execute();
                } else {
                    Toast.makeText(context, "Report not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "HappyYouKLI");

            // Ensure the directory exists
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                Log.d("PolicyDownload", "Failed to create storage directory");
                return false;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File outputFile = new File(storageDir, "HappyYouKLI_" + timeStamp + ".pdf");
            outputFileFinal=outputFile;
            try (InputStream inputStream = body.byteStream(); OutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;
                int read;

                while ((read = inputStream.read(fileReader)) != -1) {
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("PolicyDownload", "File download: " + fileSizeDownloaded + " bytes");
                }

                outputStream.flush();
                return true;

            } catch (IOException e) {
                Log.e("PolicyDownload", "Error writing file to disk", e);
                return false;
            }
        } catch (Exception e) {
            Log.e("PolicyDownload", "Unexpected error", e);
            return false;
        }
    }

    private void openPDFFile() {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/HappyYouKLI/";
        if (!outputFileFinal.exists()) {
            Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri fileUri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            fileUri = Uri.fromFile(outputFileFinal);
        } else {
            fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", outputFileFinal);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found to open PDF", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private void downloadAndViewPdf(ResponseBody responseBody) {
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // Convert ResponseBody to InputStream
                    InputStream inputStream = responseBody.byteStream();

                    // Create a temporary file to store the PDF
                    File tempFile = new File(context.getCacheDir(), "tempPdf.pdf");
                    FileOutputStream fos = new FileOutputStream(tempFile);

                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, length);
                    }
                    fos.flush();
                    fos.close();

                    // Open the PDF file using the default PDF viewer
                    openPdfWithDefaultViewer(tempFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void openPdfWithDefaultViewer(File pdfFile) {
        Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show();
        }
    }
    private void openUrlInBrowser(String url) {
       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        // Check if there is a browser available
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }*/

        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }

    public static String formatDate(String inputDate, String inputPattern, String outputPattern) {
        // Validate input parameters
        if (inputDate == null || inputDate.trim().isEmpty() ||
                inputPattern == null || inputPattern.trim().isEmpty() ||
                outputPattern == null || outputPattern.trim().isEmpty()) {
            return ""; // Return empty string if any input is invalid
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        try {
            Date date = inputFormat.parse(inputDate);  // Parse the input date
            return outputFormat.format(date);          // Format the parsed date
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Return empty string on parsing error
        }
    }
}
