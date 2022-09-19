package com.rkvitsolutions.dhruviconic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.rkvitsolutions.dhruviconic.Fragmnet.SalesEmpFragment;
import com.rkvitsolutions.dhruviconic.Model.AllFollowupReportModel;
import com.rkvitsolutions.dhruviconic.Model.FollowupDetailsModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.Utils.HelperActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllFollowupAdaptor extends RecyclerView.Adapter<AllFollowupAdaptor.MyViewHlder> {
    private Activity activity;
    private List<AllFollowupReportModel> list;
    private AlertDialog followAlert;
    private ImageView close;
    private EditText followupAltNo, followupLocation, followupDate, city, followupPurpose, followupConversation, nextFollowupDate, followupName;
    private Spinner mode, callCode, callStatus;
    private Button saveBtn;
    private boolean valid;
    private String FollowupDate, ConversationMode, CallCode, FollowupPurpose, FollowupConversation, CallStatus, NextFollowupDate;
    private String LeadCode;
    private ProgressBar progressbar;
    private ScrollView mainScroll;
    private int itemPosition;
    private LinearLayout nameLinear, altNoLinear, locationLinear;
    private String FollowupName, FollowupAltNo, FollowupLocation;
    private SalesEmpFragment fragment;
    private LinearLayout followupDateLinear;
    private String cCode;

    public AllFollowupAdaptor(Activity activity, List<AllFollowupReportModel> list) {
        this.activity = activity;
        this.list = list;
        this.fragment = fragment;
    }

    public void setPostList(List<AllFollowupReportModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    public void setFilter(List<AllFollowupReportModel> FilteredDataList) {
        list = FilteredDataList;
        notifyDataSetChanged();

        Toast.makeText(activity, String.valueOf(list.size()) + " Followup", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public MyViewHlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.followup_details_list_layout, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);
        return my;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHlder myViewHlder, int i) {

//        String followupDate = list.get(i).getNDate().substring(0, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Date date = null;
//        try {
////            String output = input.substring(0, 10);
//            date = sdf.parse(followupDate);
//            followupDate = date.toString();
//            myViewHlder.followupDate.setText(followupDate.substring(3, 10));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        myViewHlder.followupDate.setText(list.get(i).getDate());

        myViewHlder.leadName.setText(list.get(i).getName());
//        myViewHlder.leadFor.setText(list.get(i).getProject());
        myViewHlder.leadContactNo.setText(list.get(i).getMobileNo());
//        myViewHlder.leadProfesn.setText(list.get(i).getProfession());
//        myViewHlder.leadBudget.setText(list.get(i).getBudget());

//        if (!list.get(i).getLocation().isEmpty() && list.get(i).getLocation() != null
//                && !list.get(i).getLocation().equals("NA")) {
//            myViewHlder.leadLocation.setVisibility(View.VISIBLE);
//            myViewHlder.leadLocation.setText(list.get(i).getLocation());
//        }

//        list.get(i).getPhoneNo() != null || !list.get(i).getPhoneNo().isEmpty()
//        if (!list.get(i).getPhoneNo().equals("NA")) {
//            if (list.get(i).getPhoneNo() != null && !list.get(i).getMobileNo().equals(list.get(i).getPhoneNo())) {
//                myViewHlder.altNoView.setVisibility(View.VISIBLE);
//                myViewHlder.leadAltContactNo.setText(list.get(i).getPhoneNo());
//            }
//        } else
//            myViewHlder.altNoView.setVisibility(View.GONE);

        myViewHlder.conversationMode.setVisibility(View.VISIBLE);
        myViewHlder.conversationMode.setText(list.get(i).getMode());

        myViewHlder.status.setVisibility(View.VISIBLE);
        myViewHlder.status.setText("Status: " + list.get(i).getStatus());

        myViewHlder.leadSource.setVisibility(View.VISIBLE);
        myViewHlder.leadSource.setText("Source: " + list.get(i).getSourse());

        myViewHlder.fnDate.setVisibility(View.VISIBLE);
        myViewHlder.fnDate.setText("Followup Date: " + list.get(i).getNDate());

        myViewHlder.ccode.setVisibility(View.VISIBLE);
        myViewHlder.ccode.setText(list.get(i).getCCode());

        myViewHlder.purpose.setVisibility(View.VISIBLE);
        myViewHlder.purpose.setText(list.get(i).getPurpose());

        myViewHlder.conversation.setVisibility(View.VISIBLE);
        myViewHlder.conversation.setText(list.get(i).getConversation());

        myViewHlder.addFollowup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = i;
//                LeadCode = list.get(i).getCode();
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
//                new HelperActivity(activity).doWhatsApp(list.get(i).getPhoneNo());
            }
        });

        myViewHlder.callOnAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new HelperActivity(activity).makeCall(list.get(i).getPhoneNo());
            }
        });

        myViewHlder.msgOnAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new HelperActivity(activity).doMsg(list.get(i).getPhoneNo());
            }
        });
    }

    private void openFollowUp() {

        final AlertDialog.Builder diolog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_followup_layout, null);

        diolog.setView(dialogView);
        followAlert = diolog.create();
        followAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        followAlert.show();
        followAlert.setCancelable(false);
        initView(dialogView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View dialogView) {
        close = dialogView.findViewById(R.id.close);
        mainScroll = dialogView.findViewById(R.id.mainScroll);
        followupDate = dialogView.findViewById(R.id.followup_date);
        mode = dialogView.findViewById(R.id.mode);
        followupDateLinear = dialogView.findViewById(R.id.followup_date_linear);
        callCode = dialogView.findViewById(R.id.call_code);
        callCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cCode = parent.getItemAtPosition(position).toString();
                if (cCode.equals("IN") || cCode.equals("CB") || cCode.equals("NP")) {
                    followupDateLinear.setVisibility(View.VISIBLE);
                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
                    followupConversation.setText(parent.getItemAtPosition(position).toString());
                } else if (cCode.equals("NI") || cCode.equals("NR") || cCode.equals("OFF")) {
                    followupDateLinear.setVisibility(View.GONE);
                    nextFollowupDate.setText(getYesterdayDateString());
                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
                    followupConversation.setText(parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        callStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = parent.getItemAtPosition(position).toString();

//                if (code.equals("WON")) {
//                    followupDateLinear.setVisibility(View.GONE);
//                    nextFollowupDate.setText(getYesterdayDateString());
////                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
////                    followupConversation.setText(parent.getItemAtPosition(position).toString());
//                }

                if (cCode.equals("IN") || cCode.equals("CB") || cCode.equals("NP")) {
                    followupDateLinear.setVisibility(View.VISIBLE);
                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
                    followupConversation.setText(parent.getItemAtPosition(position).toString());
                } else if (cCode.equals("NI") || cCode.equals("NR") || cCode.equals("OFF") || status.equals("WON")) {
                    followupDateLinear.setVisibility(View.GONE);
                    nextFollowupDate.setText(getYesterdayDateString());
                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
                    followupConversation.setText(parent.getItemAtPosition(position).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        followupName = dialogView.findViewById(R.id.add_name);
        followupName.setText(list.get(itemPosition).getName());

        followupAltNo = dialogView.findViewById(R.id.add_alt_no);
//        followupAltNo.setText(list.get(itemPosition).getPhoneNo());

        followupLocation = dialogView.findViewById(R.id.add_location);
//        followupLocation.setText(list.get(itemPosition).getLocation());

        nameLinear = dialogView.findViewById(R.id.name_linear);
        altNoLinear = dialogView.findViewById(R.id.altno_linear);
        locationLinear = dialogView.findViewById(R.id.location_linear);

//        if (list.get(itemPosition).getName() == null || list.get(itemPosition).getName().isEmpty() || list.get(itemPosition).getName().equals("NA"))
//            nameLinear.setVisibility(View.VISIBLE);
//
//        if (list.get(itemPosition).getPhoneNo() == null || list.get(itemPosition).getPhoneNo().isEmpty() || list.get(itemPosition).getPhoneNo().equals("NA"))
//            altNoLinear.setVisibility(View.VISIBLE);
//
//        if (list.get(itemPosition).getLocation() == null || list.get(itemPosition).getLocation().isEmpty() || list.get(itemPosition).getLocation().equals("NA"))
//            locationLinear.setVisibility(View.VISIBLE);

        followupPurpose = dialogView.findViewById(R.id.followup_purpose);
        followupPurpose.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mainScroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        followupConversation = dialogView.findViewById(R.id.followup_conversation);
        followupConversation.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mainScroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        callStatus = dialogView.findViewById(R.id.call_status);
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
                        String myFormat = "dd-MM-yyyy"; //In which you need put here  //game_date=2020/12/12
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                        followupDate.setText(sdf.format(myCalendar.getTime()));
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

//                if (followupValid())
//                    addFollowup();
            }
        });

    }

    private boolean followupValid() {

        FollowupDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        ConversationMode = mode.getSelectedItem().toString().trim().replace(" ", "%20");
        CallCode = callCode.getSelectedItem().toString().trim();
        FollowupPurpose = followupPurpose.getText().toString().trim().replace(" ", "%20");

        FollowupConversation = followupConversation.getText().toString().trim().replace(" ", "%20");

        CallStatus = callStatus.getSelectedItem().toString().trim().replace(" ", "%20");

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


//        if (followupDateLinear.getVisibility() == View.VISIBLE)
        NextFollowupDate = nextFollowupDate.getText().toString().trim();
//        else {
//            nextFollowupDate.setText(getYesterdayDateString());
//            NextFollowupDate = getYesterdayDateString();
//        }

        valid = true;
//        if (followupDate.getText().toString().isEmpty()) {
//            followupDate.setError("Please enter the followup date");
//            followupDate.requestFocus();
//            valid = false;
//        }
        if (followupPurpose.getText().toString().isEmpty()) {
            followupPurpose.setError("Please enter followup purpose");
            followupPurpose.requestFocus();
            valid = false;
        }

        if (followupConversation.getText().toString().isEmpty()) {
            followupConversation.setError("Please enter followup conversation");
            followupConversation.requestFocus();
            valid = false;
        }

        if (nextFollowupDate.getText().toString().isEmpty()) {
            nextFollowupDate.setError("required");
            nextFollowupDate.requestFocus();
            valid = false;
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

//    private void addFollowup() {
//
//        progressbar.setVisibility(View.VISIBLE);
//        saveBtn.setVisibility(View.GONE);
//
//        SharedPreferences pref = activity.getSharedPreferences("UserData", MODE_PRIVATE);
//        String EmplyCode = pref.getString("userCode", null);
//
//        String URL = Constant.AddFollowup
//                + "Code=" + EmplyCode
//                + "&LCode=" + LeadCode
//                + "&Date=" + FollowupDate
//                + "&DMode=" + ConversationMode
//                + "&Purpose=" + FollowupPurpose
//                + "&Conversation=" + FollowupConversation
//                + "&FStatus=" + CallStatus
//                + "&FNDate=" + NextFollowupDate
//                + "&CCode=" + CallCode
//                + "&PhoneNo=" + FollowupAltNo.replace(" ", "%20").trim()
//                + "&Location=" + FollowupLocation.replace(" ", "%20").trim()
//                + "&Name=" + FollowupName.replace(" ", "%20").trim()
//                + "&Mode=" + "I"
//                + "&Source=" + "source"
//                + "&Empcode=" + EmplyCode
//                + "&NDate=" + NextFollowupDate
//                + "&MobileNo=" + list.get(itemPosition).getMobileNo();
//
//        Log.v("url", URL);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.v("response", response);
//
//                if (response.contains("Record Saved Successfully")) {
//                    FancyToast.makeText(activity, "Followup Added", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
//                    followAlert.cancel();
//
//                    //remove lead from list
//                    list.remove(itemPosition);
//                    notifyItemRemoved(itemPosition);
//                    notifyItemRangeChanged(itemPosition, list.size());
//                    fragment.setTotalFollowup(list.size());
//
////                    items.clear(); //here items is an ArrayList populating the RecyclerView
////                    adapter.notifyDataSetChanged();
////                    items.addAll(list);// add new data
////                    adapter.notifyItemRangeInserted(0, items.size);// notify adapter of new data
////                    ((FollowUpActivity) activity).setTotalFollowup(list.size());
//
//                } else if (response.contains("error")) {
//                    FancyToast.makeText(activity, response + "\nTry Again", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                    progressbar.setVisibility(View.GONE);
//                    saveBtn.setVisibility(View.VISIBLE);
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                FancyToast.makeText(activity, "Something went wrong, Try again.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                progressbar.setVisibility(View.GONE);
//                saveBtn.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHlder extends RecyclerView.ViewHolder {

        private TextView followupDate, leadFor, leadName, leadContactNo, leadProfesn, leadBudget, addFollowup,
                conversationMode, ccode, purpose, conversation, leadAltContactNo, leadLocation, status, leadSource, fnDate;

        private ImageView callOn, whatsapp, msgOn, callOnAlt, whatsappAlt, msgOnAlt;

        private LinearLayout altNoView;

        public MyViewHlder(@NonNull View itemView) {
            super(itemView);
            followupDate = itemView.findViewById(R.id.lead_date);
            leadName = itemView.findViewById(R.id.lead_name);
            leadFor = itemView.findViewById(R.id.lead_for);
            leadContactNo = itemView.findViewById(R.id.lead_contact);
            leadProfesn = itemView.findViewById(R.id.lead_proffesion);
            leadBudget = itemView.findViewById(R.id.lead_budget);
            leadSource = itemView.findViewById(R.id.lead_source);
            fnDate = itemView.findViewById(R.id.lead_fn_date);
            addFollowup = itemView.findViewById(R.id.add_followup);

            leadAltContactNo = itemView.findViewById(R.id.lead_alt_contact);
            leadLocation = itemView.findViewById(R.id.lead_location);
            status = itemView.findViewById(R.id.status);
            altNoView = itemView.findViewById(R.id.alt_no_view);

            conversationMode = itemView.findViewById(R.id.conversation_mode);
            ccode = itemView.findViewById(R.id.ccode);
            purpose = itemView.findViewById(R.id.purpose);
            conversation = itemView.findViewById(R.id.conversation);

            callOn = itemView.findViewById(R.id.call_on);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            msgOn = itemView.findViewById(R.id.msg_on);

            callOnAlt = itemView.findViewById(R.id.call_on_alt);
            whatsappAlt = itemView.findViewById(R.id.whatsapp_alt);
            msgOnAlt = itemView.findViewById(R.id.msg_on_alt);
        }
    }
}