package com.rkvitsolutions.dhruviconic.Activity;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Adapter.LeadsAdaptor;
import com.rkvitsolutions.dhruviconic.Adapter.LeaveListAdaptor;
import com.rkvitsolutions.dhruviconic.Fragmnet.SalesEmpFragment;
import com.rkvitsolutions.dhruviconic.Model.EmpListModel;
import com.rkvitsolutions.dhruviconic.Model.LeaveModel;
import com.rkvitsolutions.dhruviconic.Model.LeaveListModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ApplyLeave extends AppCompatActivity implements View.OnClickListener {

    private String UserCode, UserName, UserType, UserTeam;
    private EditText department, name, fromDate, toDate, reason;
    private Button apply;
    private String FromDate, ToDate, Reason, currentDate, LeaveType;
    private boolean valid;
    private Spinner leaveType;
    private AutoCompleteTextView empListAutoText;
    private List<EmpListModel> empList = new ArrayList<>();
    private List<String> empNameList = new ArrayList<>();
    private List<String> empIdList = new ArrayList<>();
    private String EmpId;
    private CardView leave_card, leave_list_card;
    private RecyclerView leave_rv;
    private ScrollView mainScroll;
    private ApiInterface apiInterface;
    private List<LeaveListModel> leaveList = new ArrayList<>();
    private LeaveListAdaptor leeveListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = APIClient.getClient().create(ApiInterface.class);


        getEmp();

        initView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    private void initView() {

        leave_rv = findViewById(R.id.leave_rv);
        mainScroll = findViewById(R.id.mainScroll);

        leave_card = findViewById(R.id.leave_card);
        leave_card.setOnClickListener(this);

        leave_list_card = findViewById(R.id.leave_list_card);
        leave_list_card.setOnClickListener(this);

        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);
        UserCode = pref.getString("userCode", null);
        UserName = pref.getString("username", null);
        UserType = pref.getString("userType", null);
        UserTeam = pref.getString("userTeam", null);

        department = findViewById(R.id.add_department);
        department.setText(UserType);

        name = findViewById(R.id.add_name);
        name.setText(UserName);

        leaveType = findViewById(R.id.leave_type);

        empListAutoText = findViewById(R.id.emp_list);

        empListAutoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ApplyLeave.this, android.R.layout.simple_dropdown_item_1line, empNameList);
                empListAutoText.setAdapter(adapter);

                empListAutoText.showDropDown();

            }
        });
        empListAutoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                String EmpName = parent.getItemAtPosition(position).toString();
                EmpId = empIdList.get(position);
                empListAutoText.setText(EmpName);
            }
        });

        fromDate = findViewById(R.id.from_date);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(ApplyLeave.this, date, myCalendar
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

        toDate = findViewById(R.id.to_date);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(ApplyLeave.this, date, myCalendar
                        .get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //Max Date
//                datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

//                Min Date
                Calendar minCal = Calendar.getInstance();
                minCal.set(myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());

                datePickerDialog.show();

            }
        });


        reason = findViewById(R.id.leave_reason);

        apply = findViewById(R.id.apply_btn);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validData())
                    applyForLeave();

            }
        });
    }


    private void getEmp() {

        empList.clear();
        empNameList.clear();
        empIdList.clear();

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<List<EmpListModel>> call = apiInterface.getEmpList();
        call.enqueue(new Callback<List<EmpListModel>>() {
            @Override
            public void onResponse(Call<List<EmpListModel>> call, retrofit2.Response<List<EmpListModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        empList = response.body();

                        for (int i = 0; i < empList.size(); i++) {
                            empNameList.add(empList.get(i).getName());
                            empIdList.add(empList.get(i).getEMPCode());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EmpListModel>> call, Throwable t) {
//                avi.hide();
//                FancyToast.makeText(Projects.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private boolean validData() {

        FromDate = fromDate.getText().toString();
        ToDate = toDate.getText().toString();
        Reason = reason.getText().toString();
        LeaveType = leaveType.getSelectedItem().toString();

        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        currentDate = sdf.format(d);

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

        if (reason.getText().toString().isEmpty()) {
            reason.setError("Please enter leave reason");
            reason.requestFocus();
            valid = false;
        }
        return valid;
    }

    private void applyForLeave() {

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Applying");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Call<LeaveModel> call = apiInterface.applyLeave(UserCode, currentDate, LeaveType,FromDate, ToDate, Reason, EmpId);
        call.enqueue(new Callback<LeaveModel>() {
            @Override
            public void onResponse(Call<LeaveModel> call, retrofit2.Response<LeaveModel> response) {

                progress.cancel();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("Record Saved Successfully")) {

                        FancyToast.makeText(ApplyLeave.this, "Applied For Leave", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        finish();

                    } else {
                        Log.v("Error", response.errorBody().toString());
                        FancyToast.makeText(ApplyLeave.this, "Sorry! Please try again later.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<LeaveModel> call, Throwable t) {
                progress.cancel();
                FancyToast.makeText(ApplyLeave.this, "Sorry! Something went wrong!.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });

    }


    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.leave_card:
                if (!mainScroll.isShown()) {

                    leave_rv.setVisibility(View.GONE);
                    mainScroll.setVisibility(View.VISIBLE);

                    leave_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selected_card));
                    leave_card.setRadius(10);
                    leave_list_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.unselected_card));
                    leave_list_card.setRadius(10);

                }

                break;

            case R.id.leave_list_card:
                if (!leave_rv.isShown()) {

                    mainScroll.setVisibility(View.GONE);
                    leave_rv.setVisibility(View.VISIBLE);

                    leave_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.unselected_card));
                    leave_card.setRadius(10);
                    leave_list_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selected_card));
                    leave_list_card.setRadius(10);

                    getLeave();
                    setLeaves();

                }
                break;

//            case R.id.save_btn:
////                startActivity(new Intent(getActivity(), ReportActivity.class));
//                break;

            default:
        }
    }

    private void getLeave() {

        AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(this);
        avLoadingIndicatorView.setIndicator("BallPulseIndicator");
        avLoadingIndicatorView.setIndicatorColor(R.color.colorPrimary);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
        pd.show();
// Set custom view(Loading Indicator View) after showing dialog
//        pd.setContentView(avLoadingIndicatorView);


        leaveList.clear();

        Call<List<LeaveListModel>> call = apiInterface.getLeaveList(UserCode);
        call.enqueue(new Callback<List<LeaveListModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<LeaveListModel>> call, retrofit2.Response<List<LeaveListModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {

                        leaveList = response.body();

                        leeveListAdaptor.setPostList(leaveList);

                        pd.cancel();
//                        loader.setVisibility(View.GONE);
//                        layoutview.setVisibility(View.VISIBLE);

                    } else {
                        pd.cancel();

                        /*loader.setVisibility(View.GONE);
                        layoutview.setVisibility(View.VISIBLE);
                        noOfLead.setText(String.valueOf(leadList.size()));*/


//                        FancyToast.makeText(getActivity(), "No Data for today! Contact the admin.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }


                } else {
                    pd.cancel();
//                    loader.setVisibility(View.GONE);
//                    layoutview.setVisibility(View.VISIBLE);
                    FancyToast.makeText(ApplyLeave.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<List<LeaveListModel>> call, Throwable t) {
//                loader.setVisibility(View.GONE);
                pd.cancel();
                FancyToast.makeText(ApplyLeave.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }


    private void setLeaves() {
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(false);
        leave_rv.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        leeveListAdaptor = new LeaveListAdaptor(this, leaveList);
        leave_rv.setAdapter(leeveListAdaptor);
        leave_rv.setItemAnimator(null);

        leave_rv.setNestedScrollingEnabled(false);

    }
}