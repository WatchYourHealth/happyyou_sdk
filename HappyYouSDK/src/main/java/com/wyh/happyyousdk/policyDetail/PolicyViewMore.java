package com.wyh.happyyousdk.policyDetail;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityPolicyViewMoreBinding;
import com.wyh.happyyousdk.policyDetail.adapter.PolicyNomineeAdpter;
import com.wyh.happyyousdk.policyDetail.adapter.PolicyRiderAdpter;
import com.wyh.happyyousdk.policyDetail.helper.FingerprintHelper;
import com.wyh.happyyousdk.model.GetClientRes;
import com.wyh.happyyousdk.model.GetOTPReq;
import com.wyh.happyyousdk.model.MPINReq;
import com.wyh.happyyousdk.model.PolicyListReq;
import com.wyh.happyyousdk.model.PolicyListResp;
import com.wyh.happyyousdk.model.VerifyOTP;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyViewMore extends AppCompatActivity {
    ActivityPolicyViewMoreBinding binding;
    Context context;
    boolean expand1=true;
    boolean expand2=true;
    boolean expand3=true;
    boolean expand4=true;
    boolean expand5=true;
    String clientID;
    String PolicyNumber = "";
//    String comingFrom = "";
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    List<PolicyListResp.Content> policyDetailsList;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policy_view_more);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        String intent=getIntent().getStringExtra("intent");
        clientID=getIntent().getStringExtra("clientID");
        PolicyNumber=getIntent().getStringExtra("policyNo");
        if (intent != null && intent.equals("Dashboard"))
        {
            PolicyNumber=getIntent().getStringExtra("policyNo");
                if (!SharedPref.getPolicyMPin().equals("")) {
                    showCustomDialog();
                } else {
                    Intent intentnew = new Intent(context, PolicyCreateMPIN.class);
                    intentnew.putExtra("clientID", clientID);
                    intentnew.putExtra("policyNo",PolicyNumber);
                    intentnew.putExtra("comingFrom","ViewMore");
                    startActivity(intentnew);
                    intentnew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }

        }
        else {
            Type type = new TypeToken<List<PolicyListResp.Content>>() {}.getType();
            String ContentData=getIntent().getStringExtra("ContentData");
            policyDetailsList = new Gson().fromJson(ContentData, type);
            setUI();
        }

        binding.spinnerPolicy.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = policyDetailsList.get(position).getPolicyNo();
                Log.e("policyListResp",selectedItem);
                /*DecimalFormat format = new DecimalFormat("0.##");*/
                setTextOrHide(binding.llPlanName,binding.tvPlanName, policyDetailsList.get(position).getProductName());
                setTextOrHide(binding.llPolicyViewMore,binding.tvPolicyNumber, policyDetailsList.get(position).getPolicyNo());
                setTextOrHide(binding.llInsuranceCover,binding.tvInsuranceCover, policyDetailsList.get(position).getInsurencecover());
                setTextOrHide(binding.llPolicyTerm,binding.tvPolicyTerm, String.valueOf(policyDetailsList.get(position).getPolicyTerm()));
                setTextOrHide(binding.llDateofIssue,binding.tvDateofIssue, formatDate(policyDetailsList.get(position).getPurchaseDate(), "yyyy-MM-dd'T'HH:mm", "dd MMM yyyy"));
                setTextOrHide(binding.llPolicyPremiumDueDate,binding.tvPolicyPremiumDueDate, formatDate(policyDetailsList.get(position).getPremiumDueDate(), "yyyy-MM-dd'T'HH:mm", "dd MMM yyyy"));
                setTextOrHide(binding.llPremiumPayingTerm,binding.tvPremiumPayingTerm, String.valueOf(policyDetailsList.get(position).getPremiumTerm()));
                setTextOrHide(binding.llPremiumFrequency,binding.tvPremiumFrequency, policyDetailsList.get(position).getPremiumCycle());
                //setTextOrHide(binding.llInsuredName,binding.tvInsuredName, policyDetailsList.get(position).getInsuredName());
                setTextOrHide(binding.llInsuredDetailsName,binding.tvInsuredDetailName, policyDetailsList.get(position).getInsuredName());
                setTextOrHide(binding.llInsuredDetailsDOB,binding.tvInsuredDateOfBirth, formatDate(policyDetailsList.get(position).getInsuredDob(), "yyyy-MM-dd'T'HH:mm", "dd MMM yyyy"));
                setTextOrHide(binding.llProposerName,binding.tvProposerName, policyDetailsList.get(position).getProposerName());
                setTextOrHide(binding.llAddress, binding.tvAddress, policyDetailsList.get(position).getClientAddress());
                setTextOrHide(binding.llMobile, binding.tvMobileNumber, policyDetailsList.get(position).getMobileNumber());
                setTextOrHide(binding.llMaturityDate, binding.tvMaturityDate, formatDate(policyDetailsList.get(position).getEnddate(),"yyyy-MM-dd'T'HH:mm", "dd MMM yyyy"));
                setTextOrHide(binding.llProposerDob, binding.tvProposerDob, formatDate(policyDetailsList.get(position).getProposerDob(),"yyyy-MM-dd'T'HH:mm", "dd MMM yyyy"));
                setTextOrHide(binding.llPremiumAmount, binding.tvPremiumAmount, policyDetailsList.get(position).getPremiumAmount());

                if(policyDetailsList.get(position).getPolicyMasterStatus() != null && policyDetailsList.get(position).getPolicyMasterStatus().equalsIgnoreCase("Active")){
                    binding.llPolicyInformationExpand.setBackgroundColor(getResources().getColor(R.color.policy_active));
                    binding.rlRiderDetail.setBackgroundColor(getResources().getColor(R.color.policy_active));
                    binding.llProposerDetails.setBackgroundColor(getResources().getColor(R.color.policy_active));
                    binding.llInsuredDetails.setBackgroundColor(getResources().getColor(R.color.policy_active));
                    binding.rlNomineeDetail.setBackgroundColor(getResources().getColor(R.color.policy_active));
                    binding.ImPolicyPremiumDueDateStatus.setImageResource(R.drawable.ic_policy_green_active);
                    binding.ImPolicyPremiumDueDateStatus.setVisibility(View.VISIBLE);
                    binding.tvAccountNumberStatus.setImageResource(R.drawable.ic_policy_green_active);
                    binding.tvAccountNumberStatus.setVisibility(View.VISIBLE);
                }else if(policyDetailsList.get(position).getPolicyMasterStatus() != null && policyDetailsList.get(position).getPolicyMasterStatus().equalsIgnoreCase("Inactive")){
                    binding.llPolicyInformationExpand.setBackgroundColor(getResources().getColor(R.color.policy_inactive));
                    binding.rlRiderDetail.setBackgroundColor(getResources().getColor(R.color.policy_inactive));
                    binding.llProposerDetails.setBackgroundColor(getResources().getColor(R.color.policy_inactive));
                    binding.llInsuredDetails.setBackgroundColor(getResources().getColor(R.color.policy_inactive));
                    binding.rlNomineeDetail.setBackgroundColor(getResources().getColor(R.color.policy_inactive));
                    binding.ImPolicyPremiumDueDateStatus.setImageResource(R.drawable.ic_policy_pink_expired);
                    binding.ImPolicyPremiumDueDateStatus.setVisibility(View.VISIBLE);
                    binding.tvAccountNumberStatus.setImageResource(R.drawable.ic_policy_pink_expired);
                    binding.tvAccountNumberStatus.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
       /* binding.tvPlanName.setText(policyListResp.getProductName());
        binding.tvPolicyPremiumDue.setText(policyListResp.getPremiumDueDate());
        binding.tvInsuranceCover.setText(policyListResp.getInsuredName());
        binding.tvPolicyTerm.setText(policyListResp.getProductName());
        binding.tvDateofIssue.setText(policyListResp.getProductName());
        binding.tvPolicyPremiumDueDate.setText(policyListResp.getProductName());
        binding.tvPremiumPayingTerm.setText(policyListResp.getProductName());
        binding.tvPremiumFrequency.setText(policyListResp.getProductName());
        binding.tvAccountNumber.setText(policyListResp.getPremiumCycle());*/

        binding.imPolicyInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker("Android_POLICY_INFORMATION",context);
                if (expand1)
                {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_up));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#43A1B8"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.VISIBLE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand1=false;

                }
                else {

                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand1=true;
                    expand2=true;
                    expand3=true;
                    expand4=true;
                    expand5=true;
                }


            }
        });

        binding.imRiderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expand2) {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_up));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#43A1B8"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.VISIBLE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand2=false;
                }
                else {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand1=true;
                    expand2=true;
                    expand3=true;
                    expand4=true;
                    expand5=true;
                }
            }
        });

        binding.imProposerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker("Android_POLICY_PROPOSER_DETAIL",context);
                if (expand3)
                {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_up));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#43A1B8"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.VISIBLE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand3=false;
                }
                else {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand1=true;
                    expand2=true;
                    expand3=true;
                    expand4=true;
                    expand5=true;
                }

            }
        });

        binding.imIsuredDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker("Android_POLICY_INSURED_DETAIL",context);
                if (expand4)
                {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_up));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#43A1B8"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.VISIBLE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand4=false;
                }
                else {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand1=true;
                    expand2=true;
                    expand3=true;
                    expand4=true;
                    expand5=true;
                }

            }

        });

        binding.imNomineeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expand5)
                {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_up));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#43A1B8"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.VISIBLE);
                    expand5=false;
                }
                else {
                    binding.imPolicyInformation.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imRiderDetail.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imProposerDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imIsuredDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));
                    binding.imNomineeDetails.setImageDrawable(getDrawable(R.drawable.ic_policy_arrow_drop_down));

                    binding.tvTitlePolicyInformation.setTextColor(Color.parseColor("#595959"));
                    binding.tvRiderDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvProposerDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvInsuredDetails.setTextColor(Color.parseColor("#595959"));
                    binding.tvNomineeDetails.setTextColor(Color.parseColor("#595959"));

                    binding.llPolicyInformationExpand.setVisibility(View.GONE);
                    binding.rlRiderDetail.setVisibility(View.GONE);
                    binding.llProposerDetails.setVisibility(View.GONE);
                    binding.llInsuredDetails.setVisibility(View.GONE);
                    binding.rlNomineeDetail.setVisibility(View.GONE);
                    expand1=true;
                    expand2=true;
                    expand3=true;
                    expand4=true;
                    expand5=true;
                }

            }
        });

        binding.imBack.setOnClickListener(view -> {

            onBackPressed();
        });

        binding.btnSendOTP.setOnClickListener(view -> {
            if (binding.edtOTP.getText() != null && binding.edtOTP.getText().toString().length()==6) {
                verifyOTP();
            } else {
                binding.edtOTP.requestFocus();
                Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }
        });

        binding.tvResendOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_OTP",context);
            sendOTp();
        });
    }
    public void setSpinnerData(Spinner spinner, List<PolicyListResp.Content> policyList, Context context) {
        // Extract policy numbers from the object list
        List<String> policyNumbers = new ArrayList<>();
        for (PolicyListResp.Content policy : policyList) {
            policyNumbers.add(policy.getPolicyNo()); // Assuming `getPolicyNumber()` exists
        }
        // Create ArrayAdapter and set it to Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, policyNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for(int i = 0; i < policyNumbers.size(); i++){
            if(!PolicyNumber.isEmpty()){
                if(PolicyNumber.equals(policyNumbers.get(i))){
                    binding.spinnerPolicy.setSelection(i);
                    break;
                }
            }
        }
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

    private void getPolicy() {
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        try {
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("ClientID",SharedPref.getClientId());
        PolicyListReq request = new PolicyListReq("0","999",clientID);
        Call<PolicyListResp> call = apiInterfaceWyh.GetPolicyDetailsPROD(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));
        call.enqueue(new Callback<PolicyListResp>() {
            @Override
            public void onResponse(Call<PolicyListResp> call, Response<PolicyListResp> response) {
                try {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    if (response.body().getData().getPolicy().getContent()!=null&&!response.body().getData().getPolicy().getContent().isEmpty())
                    {
                        if(response.body().getData().getPolicy().getPaymentrenewalurl() != null) {
                            SharedPref.setPolicyRenewableURL(response.body().getData().getPolicy().getPaymentrenewalurl());
                        }
                        policyDetailsList=response.body().getData().getPolicy().getContent();
                        setUI();
                        dialog.dismiss();
                    }


                } else {
                    dialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PolicyListResp> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void setUI()
    {
        PolicyNomineeAdpter policyDetailsAdapter = new PolicyNomineeAdpter(context, policyDetailsList);
        PolicyRiderAdpter riderAdpter = new PolicyRiderAdpter(context, policyDetailsList);
        LinearLayoutManager policyDetailsLinearLayoutManager = new LinearLayoutManager(context);
        LinearLayoutManager policyDetailsLinearLayoutManager2 = new LinearLayoutManager(context);
        policyDetailsLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        policyDetailsLinearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        binding.rlRiderDetail.setLayoutManager(policyDetailsLinearLayoutManager);
        binding.rlNomineeDetail.setLayoutManager(policyDetailsLinearLayoutManager2);
        binding.rlRiderDetail.setAdapter(riderAdpter);
        binding.rlNomineeDetail.setAdapter(policyDetailsAdapter);
        binding.llNomineeDetails.setVisibility(View.GONE);
        binding.llRiderDetails.setVisibility(View.GONE);
        binding.llPolicyInfomation6.setVisibility(View.GONE);
        setSpinnerData(binding.spinnerPolicy,policyDetailsList,context);
    }
    private void showCustomDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dailog_login_by_mpin, null);

        // Build the dialog
        dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.9f); // Set dim amount (0.0f = no dim, 1.0f = fully dimmed)
        }
        TextView imSubmitMPIN=dialogView.findViewById(R.id.imSubmitMPIN);
        ImageView  imFingerPrint=dialogView.findViewById(R.id.imFingerPrint);
        TextView tvOr=dialogView.findViewById(R.id.tvOr);
        TextView  tvForget=dialogView.findViewById(R.id.tvForget);
        if (SharedPref.getHasBiometric())
        {
            imFingerPrint.setVisibility(View.VISIBLE);
            tvOr.setVisibility(View.VISIBLE);
        }
        else {
            imFingerPrint.setVisibility(View.GONE);
            tvOr.setVisibility(View.GONE);
        }
        EditText edtOTP=dialogView.findViewById(R.id.edtOTP);
        imSubmitMPIN.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_SUBMIT_MPIN",context);
            if (edtOTP.getText() == null || !edtOTP.getText().toString().equals(SharedPref.getPolicyMPin())){
                Toast.makeText(context, "Please enter Valid MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
           getPolicy();
        });
        tvForget.setOnClickListener(view -> {
            //dialog.dismiss();

            sendOTp();
        });
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                //dialog.dismiss();  // Dismiss the dialog
                Intent intent = new Intent(context, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);// Optionally close the activity
                dialog.dismiss();
                return true;
            }
            return false;
        });
        imFingerPrint.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_LOGIN_WITH_BIOMETRIC",context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHelper.authenticateFingerprint(PolicyViewMore.this, new FingerprintHelper.FingerprintCallback() {
                    @Override
                    public void onAuthenticationSuccess() {
                        dialog.dismiss();
                        getPolicy();
                    }

                    @Override
                    public void onAuthenticationError(String errorMessage) {
                        Toast.makeText(PolicyViewMore.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }
    private void sendOTp( ) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetOTPReq request = new GetOTPReq("1","","",SharedPref.getEncryptedMobileNo(),15);
        Log.e("Mobile",RSAEncryption.callDecryptionMethod(SharedPref.getEncryptedMobileNo()));
        Call<GetClientRes> call = apiInterfaceWyh.GetPolicyOTP( SharedPref.getAuthToken(),request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    binding.tvMessage.setText(response.body().getMsg());
                    binding.imTransparent1.setVisibility(View.VISIBLE);
                    binding.getStartedLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    //Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetClientRes> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
               // Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTextOrHide(LinearLayout layout, TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            layout.setVisibility(View.VISIBLE);
            textView.setText(value);

        } else {
            layout.setVisibility(View.GONE);
            textView.setText(""); // Clear text or handle it as empty
        }
    }
    private void verifyOTP( ) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        VerifyOTP request = new VerifyOTP(RSAEncryption.rsaEncrypt(binding.edtOTP.getText().toString()));
        Call<GetClientRes> call = apiInterfaceWyh.GetPolicyOTP( SharedPref.getAuthToken(),request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    /*Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_success));*/
                    binding.tvMessage.setText(response.body().getMsg());
                    binding.imTransparent1.setVisibility(View.GONE);
                    binding.getStartedLayout.setVisibility(View.GONE);
                    binding.edtOTP.setText("");
                    showCustomCreateMPINDialog();

                } else {
                    //Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetClientRes> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showCustomCreateMPINDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_mpin, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.9f);// Set dim amount (0.0f = no dim, 1.0f = fully dimmed)
        }
        TextView imSubmit = dialogView.findViewById(R.id.imSubmit);
        EditText edtSetMPIN = dialogView.findViewById(R.id.edtSetMPIN);
        EditText edtOTP = dialogView.findViewById(R.id.edtOTP);
        imSubmit.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_SUBMIT_MPIN",context);
            if (edtSetMPIN.getText() == null && edtSetMPIN.getText().toString().length()!=4)
            {
                Toast.makeText(context, "Please enter valid MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtOTP.getText() == null || edtOTP.getText().toString().length()!=4)
            {
                Toast.makeText(context, "Please enter Re-enter  MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!edtSetMPIN.getText().toString().equals(edtOTP.getText().toString())){
                Toast.makeText(context, "The MPINs do not match. Please enter the correct MPIN", Toast.LENGTH_SHORT).show();
            }
            if (SharedPref.getHasBiometric())
            {
                AddUpdateMPIN(edtOTP.getText().toString(),SharedPref.getHasBiometric());
            }
            else {
                showCustomFingerDialog(edtOTP.getText().toString());
            }
            //getPolicy();
            dialog.dismiss();
            dialog.cancel();

            //finish();
        });
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                // dialog.dismiss();  // Dismiss the dialog
                Intent intent = new Intent(context, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);// Optionally close the activity
                dialog.dismiss();
                return true;

            }
            return false;
        });
        dialog.show();
    }
    private void AddUpdateMPIN(String intentOtp,boolean status) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        MPINReq request =new MPINReq(RSAEncryption.rsaEncrypt(intentOtp),status);
        Call<GetClientRes> call = apiInterfaceWyh.AddUpdateMPIN( SharedPref.getAuthToken(),request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    SharedPref.putHasBiometric(status);
                    SharedPref.setPolicyMPin(intentOtp);
                    showCustomDialog();
                } else {
                    // Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetClientRes> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                //Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showCustomFingerDialog(String intentOtp) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dailog_create_finger_print, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvSkip=dialogView.findViewById(R.id.tvSkip);
        ImageView imFingerPrint=dialogView.findViewById(R.id.imFingerPrint);
        tvSkip.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_BIOMETRIC",context);
            dialog.dismiss();
            AddUpdateMPIN(intentOtp,false);
        });
        imFingerPrint.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_LOGIN_WITH_BIOMETRIC",context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHelper.authenticateFingerprint(PolicyViewMore.this, new FingerprintHelper.FingerprintCallback() {
                    @Override
                    public void onAuthenticationSuccess() {
                        dialog.dismiss();
                        AddUpdateMPIN(intentOtp,true);
                    }

                    @Override
                    public void onAuthenticationError(String errorMessage) {
                        Toast.makeText(PolicyViewMore.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                //dialog.dismiss();  // Dismiss the dialog
                Intent intent = new Intent(context, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);// Optionally close the activity
                dialog.dismiss();
                return true;
            }
            return false;
        });
        dialog.show();
    }

}
