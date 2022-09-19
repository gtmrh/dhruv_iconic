package com.rkvitsolutions.dhruviconic.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.internal.$Gson$Preconditions;
import com.rkvitsolutions.dhruviconic.Adapter.AllFollowupAdaptor;
import com.rkvitsolutions.dhruviconic.Filter.FlwFilterAdapter;
import com.rkvitsolutions.dhruviconic.Filter.FollowupFilter;
import com.rkvitsolutions.dhruviconic.Filter.FollowupFilterPreferences;
import com.rkvitsolutions.dhruviconic.Model.AllFollowupReportModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class AllFollowupReport extends AppCompatActivity {

    private AVLoadingIndicatorView loader;
    private SwipeRefreshLayout flowupSwipeRefreshLayout;
    private RecyclerView followupRv;
    private List<AllFollowupReportModel> followupList = new ArrayList<>();
    private ApiInterface apiInterface;
    private AllFollowupAdaptor folloupAdaper;
    private String mobileNo;
    private EditText clientMobile;
    private TextView filterReport;
    private TextView todaysReport;
    private String FromDate, ToDate;
    private EditText fromDate, toDate;
    private boolean valid;
    private String EmpCode;
    private BottomSheetDialog dialog;
    private RecyclerView filterRV, filterValuesRV;
    private FlwFilterAdapter filterAdapter;
    private ArrayList<AllFollowupReportModel> filteredItems;
    private List<AllFollowupReportModel> filteredList = new ArrayList<>();
    private List<AllFollowupReportModel> filteredDataList;
    private TextView ttlFlwNo;
    private List<String> sourceList = new ArrayList<>();
    private Spinner reportType;
    private String reportTypeStr = "FDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_followup_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);
        EmpCode = pref.getString("userCode", null);

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        flowupSwipeRefreshLayout = findViewById(R.id.swipeContainer_followup);
        followupRv = findViewById(R.id.followup_rv);

        reportType = findViewById(R.id.spinner_type);
        reportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).toString().equals("Followup Date")) {
                    reportTypeStr = "FDate";

                }
                else {
                    reportTypeStr = "NDate";
                }

                getFollowup();
                setFollowup();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ToDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ttlFlwNo = findViewById(R.id.ttl_flw);

        flowupSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flowupSwipeRefreshLayout.setRefreshing(false);
                if (mobileNo != null) {
                    getFollowup();
                    setFollowup();
                }
            }
        });

        getFollowup();
        setFollowup();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_filter_date) {
            openDateFilter();
        } else if (item.getItemId() == R.id.menu_filter) {
            openFilterMenu();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_followup_menu, menu);
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

    private void openDateFilter() {

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
                    getFollowup();
                    setFollowup();
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

                getFollowup();
                setFollowup();
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AllFollowupReport.this, date, myCalendar
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AllFollowupReport.this, date, myCalendar
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

    private void getFollowup() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
        pd.show();

        followupList.clear();

        Call<List<AllFollowupReportModel>> call = apiInterface.getAllFollowups(EmpCode, reportTypeStr, FromDate, ToDate);
        call.enqueue(new Callback<List<AllFollowupReportModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<AllFollowupReportModel>> call, retrofit2.Response<List<AllFollowupReportModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {

                        pd.cancel();

                        followupList = response.body();

                        for (int i = 0; i < followupList.size(); i++) {

                            sourceList.add(followupList.get(i).getSourse());

                        }

                        folloupAdaper.setPostList(followupList);

                        ttlFlwNo.setText("Total Followup: " + followupList.size());

                    } else {
                        pd.cancel();
//                        noOfFollowup.setText("0");
                    }


                } else {
                    pd.cancel();
                    FancyToast.makeText(AllFollowupReport.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                }
            }

            @Override
            public void onFailure(Call<List<AllFollowupReportModel>> call, Throwable t) {
                pd.cancel();
//                loader.setVisibility(View.GONE);
                FancyToast.makeText(AllFollowupReport.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });

    }

    private void setFollowup() {
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllFollowupReport.this, VERTICAL, false);
        followupRv.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        folloupAdaper = new AllFollowupAdaptor(AllFollowupReport.this, followupList);
        followupRv.setAdapter(folloupAdaper);
        followupRv.setItemAnimator(null);
        followupRv.setNestedScrollingEnabled(false);
    }


    // <---------------------------------------------Filter Data------------------------------------------------------------------------------------>
    private void openFilterMenu() {

        dialog = new BottomSheetDialog(AllFollowupReport.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_filter, null);
        dialog.setContentView(dialogView);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        initControls(dialogView);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    private void initControls(View v) {
        filterRV = v.findViewById(R.id.filterRV);
        filterValuesRV = v.findViewById(R.id.filterValuesRV);
        filterRV.setLayoutManager(new LinearLayoutManager(this));
        filterValuesRV.setLayoutManager(new LinearLayoutManager(this));

//        List<String> callcode = Arrays.asList("IN", "NI", "NP", "NR", "OFF", "CB");
        List<String> callcode = Arrays.asList(getResources().getStringArray(R.array.call_code_data));
        if (!FollowupFilterPreferences.filters.containsKey(FollowupFilter.INDEX_CALLCODE)) {
            FollowupFilterPreferences.filters.put(FollowupFilter.INDEX_CALLCODE, new FollowupFilter("Call Code", callcode, new ArrayList()));
        }


        List<String> source = sourceList.stream().distinct().collect(Collectors.toList());

        if (!FollowupFilterPreferences.filters.containsKey(FollowupFilter.INDEX_SOURCE)) {
            FollowupFilterPreferences.filters.put(FollowupFilter.INDEX_SOURCE, new FollowupFilter("Source", source, new ArrayList()));
        }

        List<String> status = Arrays.asList(getResources().getStringArray(R.array.status));
        if (!FollowupFilterPreferences.filters.containsKey(FollowupFilter.INDEX_STATUS)) {
            FollowupFilterPreferences.filters.put(FollowupFilter.INDEX_STATUS, new FollowupFilter("Status", status, new ArrayList()));
        }
        List<String> clientType = Arrays.asList(getResources().getStringArray(R.array.client_type));
        if (!FollowupFilterPreferences.filters.containsKey(FollowupFilter.INDEX_CLIENTTYPE)) {
            FollowupFilterPreferences.filters.put(FollowupFilter.INDEX_CLIENTTYPE, new FollowupFilter("Client Type", clientType, new ArrayList()));
        }

        filterAdapter = new FlwFilterAdapter(getApplicationContext(), FollowupFilterPreferences.filters, filterValuesRV);
        filterRV.setAdapter(filterAdapter);

        Button clearB = v.findViewById(R.id.clearB);
        clearB.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CALLCODE).setSelected(new ArrayList());
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_SOURCE).setSelected(new ArrayList());
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_STATUS).setSelected(new ArrayList());
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CLIENTTYPE).setSelected(new ArrayList());

                ttlFlwNo.setText("Total Followup: " + followupList.size());

                folloupAdaper.setFilter(followupList);

                dialog.hide();
            }
        });

        Button applyB = v.findViewById(R.id.applyB);
        applyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilteredData();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateFilteredData() {
        if (!FollowupFilterPreferences.filters.isEmpty()) {
            filteredItems = new ArrayList<AllFollowupReportModel>();
            List<String> callCode = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CALLCODE).getSelected();
            List<String> source = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_SOURCE).getSelected();
            List<String> status = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_STATUS).getSelected();
            List<String> clientType = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CLIENTTYPE).getSelected();
            for (AllFollowupReportModel item : followupList) {
                boolean callCodeMatched = true;
//                if (callCode.size() > 0 && item.getCCode()!=null) {
//                    callCodeMatched = false;
//                }
                if (callCode.size() > 0 && !callCode.contains(item.getCCode())) {
                    callCodeMatched = false;
                }
                boolean sourceMatched = true;
                if (source.size() > 0 && !source.contains(item.getSourse())) {
                    sourceMatched = false;
                }

                boolean statusMatched = true;
                if (status.size() > 0 && !status.contains(item.getStatus())) {
                    statusMatched = false;
                }
                boolean clientTypeMatched = true;
                if (clientType.size() > 0 && !clientType.contains(item.getCType())) {
                    clientTypeMatched = false;
                }

                if (callCodeMatched && sourceMatched && statusMatched && clientTypeMatched) {
                    filteredItems.add(item);
                }
            }
            filteredList = filteredItems;
        }

        ttlFlwNo.setText("Total Followup: " + filteredList.size());

        folloupAdaper.setFilter(filteredList);
        dialog.hide();
    }
}