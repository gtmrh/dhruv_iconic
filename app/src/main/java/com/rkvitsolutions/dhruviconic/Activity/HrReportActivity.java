package com.rkvitsolutions.dhruviconic.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.rkvitsolutions.dhruviconic.Model.SourceStsModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.rkvitsolutions.dhruviconic.Utils.Constant;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class HrReportActivity extends AppCompatActivity {

    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "App Downloads";
    private static final String[] DAYS = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    String Assign, INN, NI, NR, NP, OFF, CB;
    private int IntINN, IntNI, IntNR, IntNP, IntOFF, IntCB;
    private BarChart chart;
    //    private HorizontalBarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;
    private ArrayList barEntries;
    private LinearLayout layoutview, sourceReport;
    private AVLoadingIndicatorView loader;
    private TextView ttlAssigned, ttlTarget, ttlCall, ttlAns, ttlAchieved, ttlIN, ttlNI, ttlNR, ttlNP, ttlOff, ttlCB, ttlOngoing, ttlWon, ttlLoss, ttlNone,
            ttlLoginProcess, ttlMeeting, ttlSV, ttlMeetingSched, ttlSVSched;
    private TextView filterReport, todaysReport;
    private EditText fromDate, toDate;
    private String FromDate, ToDate;
    private boolean valid;
    private BarChart barChart;
    private JSONArray resArray;
    private TextView noChartData;
    private String UserCode, UserType;
    private ApiInterface apiInterface;
    private List<SourceStsModel> sourceStsReport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);
        UserCode = pref.getString("userCode", null);
        UserType = pref.getString("userType", null);

        barChart = findViewById(R.id.chart);
        layoutview = findViewById(R.id.report_view);
        sourceReport = findViewById(R.id.source_report_layout);
        loader = findViewById(R.id.loader);
        ttlAssigned = findViewById(R.id.ttl_assigned);
        ttlTarget = findViewById(R.id.ttl_target);
        ttlAchieved = findViewById(R.id.ttl_achieved);
        noChartData = findViewById(R.id.no_chart);

        ttlCall = findViewById(R.id.ttl_call);
        ttlAns = findViewById(R.id.ttl_ans);

        ttlIN = findViewById(R.id.ttl_in);
        ttlNI = findViewById(R.id.ttl_ni);
        ttlNR = findViewById(R.id.ttl_nr);
        ttlNP = findViewById(R.id.ttl_np);
        ttlOff = findViewById(R.id.ttl_off);
        ttlCB = findViewById(R.id.ttl_cb);

        ttlOngoing = findViewById(R.id.ttl_ongoing);
        ttlWon = findViewById(R.id.ttl_won);
        ttlLoss = findViewById(R.id.ttl_loss);
        ttlNone = findViewById(R.id.ttl_none);
        ttlLoginProcess = findViewById(R.id.ttl_lpc);
        ttlMeeting = findViewById(R.id.ttl_meeting);
        ttlSV = findViewById(R.id.ttl_sv);
        ttlMeetingSched = findViewById(R.id.ttl_meeting_shed);
        ttlSVSched = findViewById(R.id.ttl_sv_sched);


        FromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ToDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        getAchievmntReport();
        getLeadsReport();
        getLeadsStsReport();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_filter_date) {
            openFilter();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        MenuItem filter = menu.findItem(R.id.menu_filter_date);

//        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                openFilter();
//                return true;
//            }
//        });

        return true;
    }

    private void openFilter() {

        final AlertDialog.Builder diolog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_services_layout, null);

        diolog.setView(dialogView);
        AlertDialog alertDialog = diolog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);

        ImageView close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });


        filterReport = dialogView.findViewById(R.id.filter_report);
        filterReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validDate()) {
                    getLeadsReport();
                    getLeadsStsReport();
                    alertDialog.cancel();
                }

            }
        });

        todaysReport = dialogView.findViewById(R.id.reset);
        todaysReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                ToDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                getLeadsReport();
                getLeadsStsReport();
                alertDialog.cancel();
            }
        });

        fromDate = dialogView.findViewById(R.id.from_date);
        toDate = dialogView.findViewById(R.id.to_date);

        fromDate.setOnClickListener(new View.OnClickListener() {
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
                        String myFormat = "yyyy-MM-dd"; //In which you need put here  //game_date=2020/12/12
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                        fromDate.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(HrReportActivity.this, date, myCalendar
                        .get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //Max Date
                datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

                //Min Date
//                Calendar minCal = Calendar.getInstance();
//                minCal.set(myCalendar
//                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
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
                        String myFormat = "yyyy-MM-dd"; //In which you need put here  //game_date=2020/12/12
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                        toDate.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(HrReportActivity.this, date, myCalendar
                        .get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //Max Date
                datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

                //Min Date
//                Calendar minCal = Calendar.getInstance();
//                minCal.set(myCalendar
//                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());

                datePickerDialog.show();
            }
        });

    }

    private boolean validDate() {

        FromDate = fromDate.getText().toString();
        ToDate = toDate.getText().toString();

        valid = true;
        if (fromDate.getText().toString().isEmpty()) {
            fromDate.setError("Please enter date from");
            fromDate.requestFocus();
            valid = false;
        }
        if (toDate.getText().toString().isEmpty()) {
            toDate.setError("Please enter date to");
            toDate.requestFocus();
            valid = false;
        }

        return valid;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getAchievmntReport() {

        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);
        String UserCode = pref.getString("userCode", null);

        String URL = Constant.GetTargetSts + UserCode;
        Log.v("response", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                Log.v("achievmnt_response", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("data");

                    Log.v("responseArray", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String TtlTarget = jsonObject.getString("Targt");
                        String TtlAchieved = jsonObject.getString("Acheived");

                        ttlTarget.setText("Target: " + TtlTarget + " sq.ft");
                        ttlAchieved.setText("Achieved: " + TtlAchieved + " sq.ft");

                        Log.v("UserCode", TtlTarget + "---" + TtlAchieved);

                    }

                } catch (JSONException e) {
                    System.out.print("Exception is :" + e.getMessage());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void getLeadsReport() {

        String URL = Constant.GetCandidateLeadsSts + UserCode + "&fdate=" + FromDate + "&edate=" + ToDate;
        Log.v("response", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                Log.v("response", response);
                try {
                    resArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("resArrayLength", String.valueOf(resArray.length()));
                if (resArray.length() != 0) {
                    try {
                        Log.v("resArray", resArray.toString());
                        Log.v("resObject", resArray.getJSONObject(0).getString("Name"));

                        Assign = resArray.getJSONObject(0).getString("Assign");
                        ttlAssigned.setText("Total Assigned- " + Assign);

                        NI = resArray.getJSONObject(0).getString("NI");
                        ttlNI.setText("NI: " + NI);
                        IntNI = Integer.parseInt(NI);

                        INN = resArray.getJSONObject(0).getString("INN");
                        ttlIN.setText("IN: " + INN);
                        IntINN = Integer.parseInt(INN);

                        NR = resArray.getJSONObject(0).getString("NR");
                        ttlNR.setText("NR: " + NR);
                        IntNR = Integer.parseInt(NR);

                        NP = resArray.getJSONObject(0).getString("NP");
                        ttlNP.setText("NP: " + NP);
                        IntNP = Integer.parseInt(NP);

                        OFF = resArray.getJSONObject(0).getString("OFFF");
                        ttlOff.setText("OFF: " + OFF);
                        IntOFF = Integer.parseInt(OFF);

                        CB = resArray.getJSONObject(0).getString("CB");
                        ttlCB.setText("CB: " + CB);
                        IntCB = Integer.parseInt(CB);

                        System.out.println("Check>>>>>>>" + "-" + IntCB + "-" + IntOFF + "-" + IntNP + "-" + IntNP + "-" + IntINN + "-" + IntNR);

                        setBarGraph();

                        loader.setVisibility(View.GONE);
                        layoutview.setVisibility(View.VISIBLE);
//                        sourceReport.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        System.out.print("Exception is :" + e.getMessage());

                    }
                } else {
                    layoutview.setVisibility(View.VISIBLE);
//                    sourceReport.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    FancyToast.makeText(HrReportActivity.this, "Sorry! There is no data .", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    barChart.setVisibility(View.GONE);
                    noChartData.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.GONE);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getLeadsStsReport() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
        pd.show();

        Call<List<SourceStsModel>> call = apiInterface.getSourceStsReport(UserCode, FromDate, ToDate);
        call.enqueue(new Callback<List<SourceStsModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<SourceStsModel>> call, retrofit2.Response<List<SourceStsModel>> response) {

//                loader.setVisibility(View.GONE);
//                layoutview.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {

                        pd.cancel();

                        sourceStsReport = response.body();

                        ttlOngoing.setText("OnGoing: " + sourceStsReport.get(0).getOnGoing());
                        ttlWon.setText("WON: " + sourceStsReport.get(0).getWon());
                        ttlLoss.setText("Loss: " + sourceStsReport.get(0).getLoss());
                        ttlNone.setText("None: " + sourceStsReport.get(0).getNone());
                        ttlLoginProcess.setText("Login Process: " + sourceStsReport.get(0).getLoginProcessClient());
                        ttlMeeting.setText("Meeting Done: " + sourceStsReport.get(0).getMeetingDone());
                        ttlSV.setText("Site Visit Done: " + sourceStsReport.get(0).getSiteVisitDone());
                        ttlMeetingSched.setText("Meeting Scheduled: " + sourceStsReport.get(0).getMeetingScheduled());
                        ttlSVSched.setText("Site Visited Scheduled: " + sourceStsReport.get(0).getSiteVisitScheduled());

                    } else {
                        pd.cancel();
//                        noOfFollowup.setText("0");
                    }


                } else {
                    pd.cancel();
                    FancyToast.makeText(HrReportActivity.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                }
            }

            @Override
            public void onFailure(Call<List<SourceStsModel>> call, Throwable t) {
                pd.cancel();
//                loader.setVisibility(View.GONE);
                FancyToast.makeText(HrReportActivity.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });

        String URL = Constant.GetLeadsSts + UserCode + "&fdate=" + FromDate + "&edate=" + ToDate;
        Log.v("response", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                Log.v("response", response);
                try {
                    resArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("resArrayLength", String.valueOf(resArray.length()));
                if (resArray.length() != 0) {
                    try {
                        Log.v("resArray", resArray.toString());
                        Log.v("resObject", resArray.getJSONObject(0).getString("Name"));

                        Assign = resArray.getJSONObject(0).getString("Assign");
                        ttlAssigned.setText("Total Assigned- " + Assign);

                        NI = resArray.getJSONObject(0).getString("NI");
                        ttlNI.setText("NI: " + NI);
                        IntNI = Integer.parseInt(NI);

                        INN = resArray.getJSONObject(0).getString("INN");
                        ttlIN.setText("IN: " + INN);
                        IntINN = Integer.parseInt(INN);

                        NR = resArray.getJSONObject(0).getString("NR");
                        ttlNR.setText("NR: " + NR);
                        IntNR = Integer.parseInt(NR);

                        NP = resArray.getJSONObject(0).getString("NP");
                        ttlNP.setText("NP: " + NP);
                        IntNP = Integer.parseInt(NP);

                        OFF = resArray.getJSONObject(0).getString("OFFF");
                        ttlOff.setText("OFF: " + OFF);
                        IntOFF = Integer.parseInt(OFF);

                        CB = resArray.getJSONObject(0).getString("CB");
                        ttlCB.setText("CB: " + CB);
                        IntCB = Integer.parseInt(CB);

                        System.out.println("Check>>>>>>>" + "-" + IntCB + "-" + IntOFF + "-" + IntNP + "-" + IntNP + "-" + IntINN + "-" + IntNR);

                        setBarGraph();

                        loader.setVisibility(View.GONE);
                        layoutview.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        System.out.print("Exception is :" + e.getMessage());

                    }
                } else {
                    layoutview.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    FancyToast.makeText(HrReportActivity.this, "Sorry! There is no data .", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    barChart.setVisibility(View.GONE);
                    noChartData.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setBarGraph() {
        noChartData.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);
        BarDataSet set1;
        set1 = new BarDataSet(getDataSet(), "The year 2017");

        set1.setColors(Color.parseColor("#4BB806"), Color.parseColor("#D50000"),
                Color.parseColor("#FDD930"), Color.parseColor("#ADD137"), Color.parseColor("#A0C25A"));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        // custom X-axis labels
        String[] values = new String[]{" ", "IN", "NI", "NR", "NP", "OFF", "CB"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

        barChart.setData(data);

        // custom description
        Description description = new Description();
        description.setText("Report");
        barChart.setDescription(description);

        // hide legend
        barChart.getLegend().setEnabled(false);

        barChart.animateY(1000);
        barChart.invalidate();
    }

    private ArrayList<BarEntry> getDataSet() {

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        BarEntry v1e2 = new BarEntry(1, IntINN);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2, IntNI);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(3, IntNR);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(4, IntNP);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(5, IntOFF);
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(6, IntCB);
        valueSet1.add(v1e7);
        return valueSet1;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }

}