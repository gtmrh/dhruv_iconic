package com.rkvitsolutions.dhruviconic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rkvitsolutions.dhruviconic.Fragmnet.CandidatesFragment;
import com.rkvitsolutions.dhruviconic.Model.CandidateModel;
import com.rkvitsolutions.dhruviconic.Model.DesignationModel;
import com.rkvitsolutions.dhruviconic.Model.ProjectModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.rkvitsolutions.dhruviconic.Utils.Constant;
import com.rkvitsolutions.dhruviconic.Utils.HelperActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;


public class CandidateAdaptor extends RecyclerView.Adapter<CandidateAdaptor.MyViewHlder> {

    private Activity activity;
    private List<CandidateModel> list;
    private AlertDialog followAlert;
    private ImageView close;
    private EditText followupAltNo, followupLocation, followupDate, city, followupPurpose, followupConversation, nextFollowupDate, followupName, remarks;
    private AutoCompleteTextView hiringFor, designation;
    private Spinner mode, callCode, callStatus;
    private Button saveBtn;
    private boolean valid;
    private String FollowupLocation, FollowupAltNo, FollowupDate, ConversationMode,
            CallCode, FollowupPurpose, FollowupConversation, CallStatus, NextFollowupDate, FollowupName;
    private String LeadCode;
    private ProgressBar progressbar;
    private ScrollView mainScroll;
    private int itemPosition;
    private LinearLayout nameLinear, altNoLinear, locationLinear;
    private List<ProjectModel> projectList = new ArrayList<>();
    private List<String> projectName = new ArrayList<>();
    private List<String> projectId = new ArrayList<>();

    private List<DesignationModel> designationList = new ArrayList<>();
    private List<String> designationName = new ArrayList<>();
    private List<String> designationId = new ArrayList<>();

    private String ProjectName, ProjectId, DesignationName, DesignationId;
    private CandidatesFragment fragment;

    public CandidateAdaptor(Activity activity, List<CandidateModel> list, CandidatesFragment fragment) {
        this.activity = activity;
        this.list = list;
        this.fragment = fragment;
    }

    public void setPostList(List<CandidateModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.candidate_list_layout, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);

        getProjects();
        getDesignation();

        return my;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHlder myViewHlder, int i) {

        myViewHlder.leadName.setText(list.get(i).getName());
        myViewHlder.leadContactNo.setText(list.get(i).getMobileNo());

        if (list.get(i).getEmail()!=null) {
            myViewHlder.leadMail.setVisibility(View.VISIBLE);
            myViewHlder.leadMail.setText(list.get(i).getEmail());
        }
        else
            myViewHlder.leadMail.setVisibility(View.GONE);

        if (list.get(i).getQualification()!=null) {
            myViewHlder.leadProfesn.setVisibility(View.VISIBLE);
            myViewHlder.leadProfesn.setText(list.get(i).getQualification());
        }
        else
            myViewHlder.leadProfesn.setVisibility(View.GONE);

        if (list.get(i).getExperience()!=null) {
            myViewHlder.leadExp.setVisibility(View.VISIBLE);
            myViewHlder.leadExp.setText(list.get(i).getExperience());
        }
        else
            myViewHlder.leadExp.setVisibility(View.GONE);

//        if (!model.getLeadLocation().isEmpty() && model.getLeadLocation() != null
//                && !model.getLeadLocation().equals("NA")) {
//            myViewHlder.leadLocation.setVisibility(View.VISIBLE);
//            myViewHlder.leadLocation.setText(model.getLeadLocation());
//        }
//        if (model.getLeadAltNo() != null && !model.getLeadAltNo().isEmpty()
//                && !model.getLeadAltNo().equals("NA")) {
//            myViewHlder.altNoView.setVisibility(View.VISIBLE);
//            myViewHlder.leadAltContactNo.setText(model.getLeadAltNo());
//        }

        myViewHlder.addFollowup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = i;
                LeadCode = list.get(i).getCode();
                openFollowUp();
            }
        });

        myViewHlder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HelperActivity(activity).doWhatsApp(list.get(i).getMobileNo());
            }
        });

        myViewHlder.callOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HelperActivity(activity).makeCall(list.get(i).getMobileNo());
            }
        });

        myViewHlder.msgOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HelperActivity(activity).doMsg(list.get(i).getMobileNo());
            }
        });

        myViewHlder.sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(itemPosition).getEmail() != null)
                    new HelperActivity(activity).sendEmail(list.get(itemPosition).getEmail());
                else
                    Toast.makeText(activity, "Email id not available", Toast.LENGTH_SHORT).show();
            }
        });

//        myViewHlder.whatsappAlt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new HelperActivity(activity).doWhatsApp(list.get(i).getMobileNo());
//            }
//        });

//        myViewHlder.callOnAlt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new HelperActivity(activity).makeCall(list.get(i).getLeadAltNo());
//            }
//        });

//        myViewHlder.msgOnAlt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new HelperActivity(activity).doMsg(list.get(i).getLeadAltNo());
//            }
//        });
    }

    private void openFollowUp() {

        final AlertDialog.Builder diolog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_candidate_followup_layout, null);
        diolog.setView(dialogView);
        followAlert = diolog.create();

//        followAlert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        followAlert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        followAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        followAlert.show();

        followAlert.setCancelable(false);
        initView(dialogView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View dialogView) {
        close = dialogView.findViewById(R.id.close);
//        mainScroll = dialogView.findViewById(R.id.mainScroll);
        hiringFor = dialogView.findViewById(R.id.hiring_for);
        designation = dialogView.findViewById(R.id.hiring_designation);
        mode = dialogView.findViewById(R.id.mode);
        callCode = dialogView.findViewById(R.id.call_code);
        followupAltNo = dialogView.findViewById(R.id.add_alt_no);
        followupLocation = dialogView.findViewById(R.id.add_location);
        followupName = dialogView.findViewById(R.id.add_name);
        nameLinear = dialogView.findViewById(R.id.name_linear);
        altNoLinear = dialogView.findViewById(R.id.altno_linear);
        locationLinear = dialogView.findViewById(R.id.location_linear);

        remarks = dialogView.findViewById(R.id.remarks);

        if (list.get(itemPosition).getName() == null || list.get(itemPosition).getName().isEmpty() || list.get(itemPosition).getName().equals("NA"))
            nameLinear.setVisibility(View.VISIBLE);

        callStatus = dialogView.findViewById(R.id.call_status);
        nextFollowupDate = dialogView.findViewById(R.id.next_followup_date);
        saveBtn = dialogView.findViewById(R.id.save_btn);
        progressbar = dialogView.findViewById(R.id.progressbar);

        followupName.setText(list.get(itemPosition).getName());
        followupAltNo.setText(list.get(itemPosition).getMobileNo());

        hiringFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, projectName);
                hiringFor.setAdapter(adapter);

                hiringFor.showDropDown();
                hiringFor.setFocusable(false);
                hiringFor.setCursorVisible(false);

            }
        });
        hiringFor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                ProjectName = parent.getItemAtPosition(position).toString();
                ProjectId = projectId.get(position);
                hiringFor.setText(ProjectName);
            }
        });

        designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, designationName);
                designation.setAdapter(adapter);

                designation.showDropDown();
                designation.setFocusable(false);
                designation.setCursorVisible(false);

            }
        });
        designation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                DesignationName = parent.getItemAtPosition(position).toString();
                DesignationId = designationId.get(position);
                designation.setText(DesignationName);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followAlert.cancel();
            }
        });

        nextFollowupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy/MM/dd"; //In which you need put here  //game_date=2020/12/12
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                        nextFollowupDate.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //Max Date
//                datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

                //Min Date
                Calendar minCal = Calendar.getInstance();
                minCal.set(myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (followupValid())
                    addCandidatFollowup();
            }
        });

    }

    private void getProjects() {

        projectList.clear();
        projectName.clear();
        projectId.clear();

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<List<ProjectModel>> call = apiInterface.getProjects();
        call.enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, retrofit2.Response<List<ProjectModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        projectList = response.body();

                        for (int i = 0; i < projectList.size(); i++) {

                            projectName.add(projectList.get(i).getName());
                            projectId.add(projectList.get(i).getCode());

                        }


                    }
                }

            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable t) {
//                avi.hide();
//                FancyToast.makeText(Projects.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void getDesignation() {

        designationList.clear();
        designationName.clear();
        designationId.clear();

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<List<DesignationModel>> call = apiInterface.getDesignation();
        call.enqueue(new Callback<List<DesignationModel>>() {
            @Override
            public void onResponse(Call<List<DesignationModel>> call, retrofit2.Response<List<DesignationModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        designationList = response.body();

                        for (int i = 0; i < projectList.size(); i++) {

                            designationName.add(designationList.get(i).getName());
                            designationId.add(designationList.get(i).getCode());
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<List<DesignationModel>> call, Throwable t) {
//                avi.hide();
//                FancyToast.makeText(Projects.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private boolean followupValid() {

//        FollowupDate = followupDate.getText().toString().trim();
//        FollowupDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        ConversationMode = mode.getSelectedItem().toString().trim().replace(" ", "%20");
        CallCode = callCode.getSelectedItem().toString().trim();
//        FollowupPurpose = followupPurpose.getText().toString().trim().replace(" ", "%20");

        if (!followupName.getText().toString().isEmpty())
            FollowupName = followupName.getText().toString().trim().replace(" ", "%20");
        else
            FollowupName = "NA";

        if (!followupAltNo.getText().toString().isEmpty())
            FollowupAltNo = followupAltNo.getText().toString().trim().replace(" ", "%20");
        else
            FollowupAltNo = "NA";

        if (!followupLocation.getText().toString().isEmpty())
            FollowupLocation = followupLocation.getText().toString().trim().replace(" ", "%20");
        else
            FollowupLocation = "NA";

//        FollowupConversation = followupConversation.getText().toString().trim().replace(" ", "%20");
        CallStatus = callStatus.getSelectedItem().toString().trim().replace(" ", "%20");

        if (CallCode.equals("IN") || CallCode.equals("CB"))
            NextFollowupDate = nextFollowupDate.getText().toString().trim();
        else
            NextFollowupDate = getYesterdayDateString();


        //validation starts

        valid = true;

//        if (followupPurpose.getText().toString().isEmpty()) {
//            followupPurpose.setError("Please enter followup purpose");
//            followupPurpose.requestFocus();
//            valid = false;
//        }
//
//        if (followupConversation.getText().toString().isEmpty()) {
//            followupConversation.setError("Please enter followup conversation");
//            followupConversation.requestFocus();
//            valid = false;
//        }

        if (CallCode.equals("IN") || CallCode.equals("CB")) {
            if (nextFollowupDate.getText().toString().isEmpty()) {
                nextFollowupDate.setError("required");
                nextFollowupDate.requestFocus();
                valid = false;
            }

            if (ProjectName.isEmpty()) {
                hiringFor.setError("required");
                hiringFor.requestFocus();
                valid = false;
            }

            if (DesignationName.isEmpty()) {
                designation.setError("required");
                designation.requestFocus();
                valid = false;
            }
        }

        return valid;

    }

    private String getYesterdayDateString() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    private void addCandidatFollowup() {

        progressbar.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);

        SharedPreferences pref = activity.getSharedPreferences("UserData", MODE_PRIVATE);
        String EmplyCode = pref.getString("userCode", null);


        String URL = Constant.AddCandidateFollowup
                + "Code=" + list.get(itemPosition).getCode()
                + "&Project=" + ProjectId
                + "&Location=" + FollowupLocation.replace(" ", "%20").trim()
                + "&CallCode=" + CallCode
                + "&Status=" + CallStatus
                + "&Dig=" + DesignationId
                + "&InterviewDate=" + NextFollowupDate
                + "&Remarks=" + remarks.getText().toString().replace(" ", "%20").trim();


        Log.v("URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("response", response);

                if (response.contains("Record Updated Successfully")) {

                    FancyToast.makeText(activity, "Followup Added", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    //remove lead from list
                    list.remove(itemPosition);
                    notifyItemRemoved(itemPosition);
                    notifyItemRangeChanged(itemPosition, list.size());
                    fragment.setTotalLead(list.size());

                    followAlert.cancel();

                } else if (response.contains("error")) {
                    FancyToast.makeText(activity, "Something went wrong, Try again.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    Log.d("error", response);
                    progressbar.setVisibility(View.GONE);
                    saveBtn.setVisibility(View.VISIBLE);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FancyToast.makeText(activity, "Something went wrong, Try again.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progressbar.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHlder extends RecyclerView.ViewHolder {

        private TextView leadDate, leadFor, leadMail, leadName, leadContactNo, leadProfesn, leadBudget, addFollowup, leadAltContactNo, leadLocation, leadExp;
        private ImageView callOn, whatsapp, msgOn, callOnAlt, whatsappAlt, msgOnAlt, sendMail;
        private LinearLayout altNoView;

        public MyViewHlder(@NonNull View itemView) {
            super(itemView);
            leadDate = itemView.findViewById(R.id.lead_date);
            leadName = itemView.findViewById(R.id.lead_name);
            leadMail = itemView.findViewById(R.id.lead_mail);
            leadFor = itemView.findViewById(R.id.lead_for);
            leadExp = itemView.findViewById(R.id.lead_exp);
            leadContactNo = itemView.findViewById(R.id.lead_contact);
            leadLocation = itemView.findViewById(R.id.lead_location);
            leadAltContactNo = itemView.findViewById(R.id.lead_alt_contact);
            leadProfesn = itemView.findViewById(R.id.lead_proffesion);
            leadBudget = itemView.findViewById(R.id.lead_budget);
            addFollowup = itemView.findViewById(R.id.add_followup);
            altNoView = itemView.findViewById(R.id.alt_no_view);

            callOn = itemView.findViewById(R.id.call_on);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            msgOn = itemView.findViewById(R.id.msg_on);
            sendMail = itemView.findViewById(R.id.email);

            callOnAlt = itemView.findViewById(R.id.call_on_alt);
            whatsappAlt = itemView.findViewById(R.id.whatsapp_alt);
            msgOnAlt = itemView.findViewById(R.id.msg_on_alt);
        }
    }
}