package com.rkvitsolutions.dhruviconic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.rkvitsolutions.dhruviconic.Fragmnet.SalesEmpFragment;
import com.rkvitsolutions.dhruviconic.Model.ProjectModel;
import com.rkvitsolutions.dhruviconic.Model.SalesLeadsModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.rkvitsolutions.dhruviconic.Utils.Constant;
import com.rkvitsolutions.dhruviconic.Utils.HelperActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class LeadsAdaptor extends RecyclerView.Adapter<LeadsAdaptor.MyViewHlder> {
    private Activity activity;
    private List<SalesLeadsModel> list;
    private AlertDialog followAlert;
    private ImageView close;
    private EditText followupAltNo, followupLocation, followupDate, city, followupPurpose, followupConversation, nextFollowupDate, followupName;
    private Spinner mode, callCode, callStatus;
    private Button saveBtn;
    private boolean valid;
    private String FollowupLocation, FollowupAltNo, FollowupDate, ConversationMode, CallCode, FollowupPurpose,
            FollowupConversation, CallStatus, NextFollowupDate, FollowupName;
    private String LeadCode;
    private ProgressBar progressbar;
    private ScrollView mainScroll;
    private int itemPosition;
    private LinearLayout nameLinear, altNoLinear, locationLinear;
    private SalesEmpFragment salesEmpFragment;
    private LinearLayout followupDateLinear;
    private String cCode;
    private Spinner clientType;
    private AutoCompleteTextView project;
    private List<ProjectModel> projectList = new ArrayList<>();
    private List<String> projectName = new ArrayList<>();
    private List<String> projectId = new ArrayList<>();
    private String ProjectId;
    private AutoCompleteTextView source;
    private String SourceStr;
    private HashMap<String, String> projectHashMap = new HashMap<>();
    private CheckBox addTodaysFollowup;
    private String Ndate;

    public LeadsAdaptor(Activity activity, List<SalesLeadsModel> list, SalesEmpFragment fragment) {
        this.activity = activity;
        this.list = list;
        this.salesEmpFragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leads_list_layout, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);
        return my;
    }

    public void setPostList(List<SalesLeadsModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    public void setFilter(List<SalesLeadsModel> FilteredDataList) {
        list = FilteredDataList;
        notifyDataSetChanged();

        Toast.makeText(activity, String.valueOf(list.size()) + " Leads", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHlder myViewHlder, int i) {


        myViewHlder.leadName.setText(list.get(i).getName());
        myViewHlder.leadDate.setText(list.get(i).getDated());
        myViewHlder.leadFor.setText(list.get(i).getProject());
        myViewHlder.leadSource.setText(list.get(i).getSourse());
        myViewHlder.leadContactNo.setText(list.get(i).getMobileNo());

        if (list.get(i).getProfession().equals("Not Define") || list.get(i).getProfession() == null)
            myViewHlder.leadProfesn.setVisibility(View.GONE);
        else
            myViewHlder.leadProfesn.setText(list.get(i).getProfession());

        if (!list.get(i).getLocation().isEmpty() && list.get(i).getLocation() != null
                && !list.get(i).getLocation().equals("NA")) {
            myViewHlder.leadLocation.setVisibility(View.VISIBLE);
            myViewHlder.leadLocation.setText(list.get(i).getLocation());
        }
        if (list.get(i).getPhoneNo() != null && !list.get(i).getPhoneNo().isEmpty()
                && !list.get(i).getPhoneNo().equals("NA")) {
            myViewHlder.altNoView.setVisibility(View.VISIBLE);
            myViewHlder.leadAltContactNo.setText(list.get(i).getPhoneNo());
        }

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

        myViewHlder.whatsappAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HelperActivity(activity).doWhatsApp(list.get(i).getPhoneNo());
            }
        });

        myViewHlder.callOnAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HelperActivity(activity).makeCall(list.get(i).getPhoneNo());
            }
        });

        myViewHlder.msgOnAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HelperActivity(activity).doMsg(list.get(i).getPhoneNo());
            }
        });
    }

    private void openFollowUp() {

        final AlertDialog.Builder diolog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_lead_followup_layout, null);
        diolog.setView(dialogView);
        followAlert = diolog.create();

        followAlert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        followAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        followAlert.show();

        //show/hide softkey as per requirement
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        followAlert.setCancelable(false);
        initView(dialogView);
    }

    private String getBackDateString() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

//        if (callStatus.getSelectedItem().toString().equals("None"))
        cal.add(Calendar.DATE, -1);
//        else
//            cal.add(Calendar.DATE, -30);

        return dateFormat.format(cal.getTime());
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void initView(View dialogView) {

        getProjects();


        addTodaysFollowup = dialogView.findViewById(R.id.back_date_check);
        Ndate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        addTodaysFollowup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Ndate = getBackDateString();
                else
                    Ndate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
            }
        });

        source = dialogView.findViewById(R.id.source);

        Log.v("source", list.get(itemPosition).getSourse());

        if (!list.get(itemPosition).getSourse().isEmpty() && list.get(itemPosition).getSourse() != null) {
//            SourceStr = list.get(itemPosition).getSourse();
            source.setText(list.get(itemPosition).getSourse());
        } else {
//            source.setText("NA");
            source.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line,
                            Arrays.asList(activity.getResources().getStringArray(R.array.source)));
                    source.setAdapter(adapter);

                    source.showDropDown();
                    source.setFocusable(false);
                    source.setCursorVisible(false);

                }
            });

            source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SourceStr = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        project = dialogView.findViewById(R.id.add_project);

//        Log.v("projectid", ProjectId);


        //if project not selected
        ProjectId = projectHashMap.get(list.get(itemPosition).getProject());

        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, projectName);
                project.setAdapter(adapter);

                project.showDropDown();
                project.setFocusable(false);
                project.setCursorVisible(false);

            }
        });

        project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                String ProjectName = parent.getItemAtPosition(position).toString();
                ProjectId = projectId.get(position);
                Log.v("pid", ProjectId);
                project.setText(ProjectName);
            }
        });

        project.setText(list.get(itemPosition).getProject());

//        if (list.get(itemPosition).getProject()!=null || !list.get(itemPosition).getProject().isEmpty())
//            project.setSelection(((ArrayAdapter<String>)mode.getAdapter()).getPosition(list.get(itemPosition).getProject()));

        close = dialogView.findViewById(R.id.close);
//        mainScroll = dialogView.findViewById(R.id.mainScroll);
        followupDate = dialogView.findViewById(R.id.followup_date);
        mode = dialogView.findViewById(R.id.mode);
        callCode = dialogView.findViewById(R.id.call_code);

        followupDateLinear = dialogView.findViewById(R.id.followup_date_linear);

        callCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cCode = parent.getItemAtPosition(position).toString();
                followupPurpose.setText(cCode);
                followupConversation.setText(cCode);

//                if (cCode.equals("IN") || cCode.equals("CB") || cCode.equals("NP")) {
//                    followupDateLinear.setVisibility(View.VISIBLE);
//                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
//                    followupConversation.setText(parent.getItemAtPosition(position).toString());
//                } else if (cCode.equals("NI") || cCode.equals("NR") || cCode.equals("OFF")) {
//                    followupDateLinear.setVisibility(View.GONE);
//                    nextFollowupDate.setText(getYesterdayDateString());
//                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
//                    followupConversation.setText(parent.getItemAtPosition(position).toString());
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        followupAltNo = dialogView.findViewById(R.id.add_alt_no);
        followupAltNo.setText(list.get(itemPosition).getPhoneNo());

        followupLocation = dialogView.findViewById(R.id.add_location);
        followupLocation.setText(list.get(itemPosition).getLocation());

        followupName = dialogView.findViewById(R.id.add_name);
        followupName.setText(list.get(itemPosition).getName());

        nameLinear = dialogView.findViewById(R.id.name_linear);
        altNoLinear = dialogView.findViewById(R.id.altno_linear);
        locationLinear = dialogView.findViewById(R.id.location_linear);

        if (list.get(itemPosition).getName() == null || list.get(itemPosition).getName().isEmpty() || list.get(itemPosition).getName().equals("NA"))
            nameLinear.setVisibility(View.VISIBLE);

        if (list.get(itemPosition).getPhoneNo() == null || list.get(itemPosition).getPhoneNo().isEmpty() || list.get(itemPosition).getPhoneNo().equals("NA"))
            altNoLinear.setVisibility(View.VISIBLE);

        if (list.get(itemPosition).getLocation() == null || list.get(itemPosition).getLocation().isEmpty() || list.get(itemPosition).getLocation().equals("NA"))
            locationLinear.setVisibility(View.VISIBLE);

        followupPurpose = dialogView.findViewById(R.id.followup_purpose);
        followupPurpose.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                mainScroll.requestDisallowInterceptTouchEvent(true);

                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        //On EditText touch event releasing intercept touch event for parent
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        followupConversation = dialogView.findViewById(R.id.followup_conversation);
        followupConversation.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                mainScroll.requestDisallowInterceptTouchEvent(true);

                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        //On EditText touch event releasing intercept touch event for parent
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        clientType = dialogView.findViewById(R.id.client_type);

        callStatus = dialogView.findViewById(R.id.call_status);
        callStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String code = parent.getItemAtPosition(position).toString();
//
                String status = parent.getItemAtPosition(position).toString();
                if (status.equals("WON") || status.equals("Loss") || status.equals("None")) {
                    followupDateLinear.setVisibility(View.GONE);
                    nextFollowupDate.setText(getYesterdayDateString());
//                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
//                    followupConversation.setText(parent.getItemAtPosition(position).toString());
                } else {

                    followupDateLinear.setVisibility(View.VISIBLE);

//                    if (cCode.equals("IN") || cCode.equals("CB") || cCode.equals("NP")) {
//                        followupDateLinear.setVisibility(View.VISIBLE);
//
//                    } else if (cCode.equals("NI") || cCode.equals("NR") || cCode.equals("OFF")) {
//                        followupDateLinear.setVisibility(View.GONE);
//                        nextFollowupDate.setText(getYesterdayDateString());
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nextFollowupDate = dialogView.findViewById(R.id.next_followup_date);
        saveBtn = dialogView.findViewById(R.id.save_btn);
        progressbar = dialogView.findViewById(R.id.progressbar);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followAlert.cancel();
            }
        });

        followupDate.setOnClickListener(new View.OnClickListener() {
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
//                        dd-MM-yyyy
                        String myFormat = "dd/MM/yyyy"; //In which you need put here  //game_date=2020/12/12
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                        //todays date
                        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));
                        String todaysDate = dateFormat.format(date);

//                        if (todaysDate.equals(sdf.format(myCalendar.getTime())))
//                            Toast.makeText(activity, "Today's date cannot be selected.", Toast.LENGTH_LONG).show();
//                        else
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
//                Calendar minCal = Calendar.getInstance();
//                minCal.set(myCalendar
//                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (followupValid())
                    addFollowup();
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

                            projectHashMap.put(projectList.get(i).getName(), projectList.get(i).getCode());

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

    private boolean followupValid() {

//        FollowupDate = followupDate.getText().toString().trim();
        FollowupDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        ConversationMode = mode.getSelectedItem().toString().trim().replace(" ", "%20");
        CallCode = callCode.getSelectedItem().toString().trim();
        FollowupPurpose = followupPurpose.getText().toString().trim().replace(" ", "%20");

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

        FollowupConversation = followupConversation.getText().toString().trim().replace(" ", "%20");
        CallStatus = callStatus.getSelectedItem().toString().trim().replace(" ", "%20");

//        if (followupDateLinear.getVisibility() == View.VISIBLE)
        NextFollowupDate = nextFollowupDate.getText().toString().trim();
//        else
//            NextFollowupDate = getYesterdayDateString();


        //validation starts

        valid = true;

        if (followupPurpose.getText().toString().isEmpty()) {
            followupPurpose.setError("Please enter followup purpose");
            followupPurpose.requestFocus();
            valid = false;
        }

        if (project.getText().toString().isEmpty()) {
            project.setError("required");
            project.requestFocus();
            valid = false;
        }

        if (followupConversation.getText().toString().isEmpty()) {
            followupConversation.setError("Please enter followup conversation");
            followupConversation.requestFocus();
            valid = false;
        }

//        if (CallCode.equals("IN") || CallCode.equals("CB")) {
        if (nextFollowupDate.getText().toString().isEmpty()) {
            nextFollowupDate.setError("required");
            nextFollowupDate.requestFocus();
            valid = false;
        }
//        }

        return valid;

    }

    private String getYesterdayDateString() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

        if (callStatus.getSelectedItem().toString().equals("None"))
            cal.add(Calendar.DATE, -3);
        else
            cal.add(Calendar.DATE, -30);

        return dateFormat.format(cal.getTime());
    }

    private void addFollowup() {

        progressbar.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);

        SharedPreferences pref = activity.getSharedPreferences("UserData", MODE_PRIVATE);
        String EmplyCode = pref.getString("userCode", null);


        String URL = Constant.AddFollowup
                + "Code=" + EmplyCode
                + "&LCode=" + LeadCode
                + "&Date=" + FollowupDate
                + "&DMode=" + ConversationMode.replace(" ", "%20").trim()
                + "&Purpose=" + FollowupPurpose.replace(" ", "%20").trim()
                + "&Conversation=" + FollowupConversation.replace(" ", "%20").trim()
                + "&FStatus=" + CallStatus
                + "&FNDate=" + NextFollowupDate
                + "&CCode=" + CallCode
                + "&PhoneNo=" + FollowupAltNo.replace(" ", "%20").trim()
                + "&Location=" + FollowupLocation.replace(" ", "%20").trim()
                + "&Name=" + FollowupName.replace(" ", "%20").trim()
                + "&Mode=" + "I"
//                + "&Source=" + SourceStr.replace(" ", "%20").trim()
                + "&Source=" + source.getText().toString().replace(" ", "%20").trim()
                + "&Empcode=" + EmplyCode
                + "&CType=" + clientType.getSelectedItem().toString().replace(" ", "%20").trim()
                + "&DDProject=" + ProjectId
                + "&NDate=" + new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date())
                + "&MobileNo=" + list.get(itemPosition).getMobileNo();

        Log.v("URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("response", response);

                if (response.contains("Record Saved Successfully")) {
                    FancyToast.makeText(activity, "Followup Added", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                    //remove lead from list
                    list.remove(itemPosition);
                    notifyItemRemoved(itemPosition);
                    notifyItemRangeChanged(itemPosition, list.size());
                    salesEmpFragment.setTotalLead(list.size());

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

        private TextView leadDate, leadFor, leadSource, leadName, leadContactNo, leadProfesn, leadBudget, addFollowup, leadAltContactNo, leadLocation;
        private ImageView callOn, whatsapp, msgOn, callOnAlt, whatsappAlt, msgOnAlt;
        private LinearLayout altNoView;

        public MyViewHlder(@NonNull View itemView) {
            super(itemView);
            leadDate = itemView.findViewById(R.id.lead_date);
            leadName = itemView.findViewById(R.id.lead_name);
            leadFor = itemView.findViewById(R.id.lead_for);
            leadSource = itemView.findViewById(R.id.lead_source);
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

            callOnAlt = itemView.findViewById(R.id.call_on_alt);
            whatsappAlt = itemView.findViewById(R.id.whatsapp_alt);
            msgOnAlt = itemView.findViewById(R.id.msg_on_alt);
        }
    }
}