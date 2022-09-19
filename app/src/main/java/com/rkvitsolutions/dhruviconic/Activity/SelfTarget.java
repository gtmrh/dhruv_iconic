package com.rkvitsolutions.dhruviconic.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Adapter.SelfTargetAdaptor;
import com.rkvitsolutions.dhruviconic.Model.EmpListModel;
import com.rkvitsolutions.dhruviconic.Model.TargetsModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class SelfTarget extends AppCompatActivity implements View.OnClickListener {

    private CardView add, list;
    private String UserCode, UserName, UserType, UserTeam;
    private EditText department, name, fromDate, toDate, reason, dataCollection, add_sitevisit, add_login, add_meeting, dialCall, ansCall;
    private Button apply;
    private String FromDate, ToDate, Reason, currentDate, LeaveType;
    private boolean valid;
    private Spinner leaveType;
    private AutoCompleteTextView empListAutoText;
    private List<EmpListModel> empList = new ArrayList<>();
    private List<String> empNameList = new ArrayList<>();
    private List<String> empIdList = new ArrayList<>();
    private String EmpId;
    private RecyclerView targetRv;
    private LinearLayout linearForm, dialLinearForm, data_coll_linear;
    private List<TargetsModel> targetList = new ArrayList<>();
    private ApiInterface apiInterface;
    private SelfTargetAdaptor targetAdapter;
    private Spinner dateSlot;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_target);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);
        UserCode = pref.getString("userCode", null);
        UserName = pref.getString("username", null);
        UserType = pref.getString("userType", null);
        UserTeam = pref.getString("userTeam", null);

        gender = getIntent().getStringExtra("Gender");

        add = findViewById(R.id.text_add);
        add.setOnClickListener(this);
        list = findViewById(R.id.text_list);
        list.setOnClickListener(this);

        linearForm = findViewById(R.id.linear_form);
        dialLinearForm = findViewById(R.id.dial_target_linear);
        data_coll_linear = findViewById(R.id.data_coll_linear);
        targetRv = findViewById(R.id.leads_rv);

        dialCall = findViewById(R.id.add_dial_call);
        ansCall = findViewById(R.id.add_ans_call);
        dataCollection = findViewById(R.id.add_data_collection);
        add_sitevisit = findViewById(R.id.add_sitevisit);
        add_meeting = findViewById(R.id.add_meeting);
        add_login = findViewById(R.id.add_login);
        dateSlot = findViewById(R.id.date_slot);

        if (gender.equals("Girls")) {
            dialLinearForm.setVisibility(View.VISIBLE);
            data_coll_linear.setVisibility(View.GONE);
        } else {
            dialLinearForm.setVisibility(View.GONE);
            data_coll_linear.setVisibility(View.VISIBLE);
        }

        apply = findViewById(R.id.apply_btn);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!gender.equals("Girls")) {

                    if (validData())
                        setBoysTarget();
                } else {
                    if (validGirlData())
                        setGirlsTarget();
                }

            }
        });
    }

    private boolean validData() {

//        FromDate = fromDate.getText().toString();
//        ToDate = toDate.getText().toString();

        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        currentDate = sdf.format(d);

        valid = true;
//        if (fromDate.getText().toString().isEmpty()) {
//            fromDate.setError("required");
//            fromDate.requestFocus();
//            valid = false;
//        }
//        if (toDate.getText().toString().isEmpty()) {
//            toDate.setError("required");
//            toDate.requestFocus();
//            valid = false;
//        }

        if (dataCollection.getText().toString().isEmpty()) {
            dataCollection.setError("required");
            dataCollection.requestFocus();
            valid = false;
        }
        if (add_login.getText().toString().isEmpty()) {
            add_login.setError("required");
            add_login.requestFocus();
            valid = false;
        }
        if (add_meeting.getText().toString().isEmpty()) {
            add_meeting.setError("required");
            add_meeting.requestFocus();
            valid = false;
        }
        if (add_sitevisit.getText().toString().isEmpty()) {
            add_sitevisit.setError("required");
            add_sitevisit.requestFocus();
            valid = false;
        }

        return valid;
    }

    private boolean validGirlData() {

//        FromDate = fromDate.getText().toString();
//        ToDate = toDate.getText().toString();

        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        currentDate = sdf.format(d);

        valid = true;
//        if (fromDate.getText().toString().isEmpty()) {
//            fromDate.setError("required");
//            fromDate.requestFocus();
//            valid = false;
//        }
//        if (toDate.getText().toString().isEmpty()) {
//            toDate.setError("required");
//            toDate.requestFocus();
//            valid = false;
//        }

        if (dialCall.getText().toString().isEmpty()) {
            dialCall.setError("required");
            dialCall.requestFocus();
            valid = false;
        }

        if (ansCall.getText().toString().isEmpty()) {
            ansCall.setError("required");
            ansCall.requestFocus();
            valid = false;
        }
        if (add_login.getText().toString().isEmpty()) {
            add_login.setError("required");
            add_login.requestFocus();
            valid = false;
        }
        if (add_meeting.getText().toString().isEmpty()) {
            add_meeting.setError("required");
            add_meeting.requestFocus();
            valid = false;
        }
        if (add_sitevisit.getText().toString().isEmpty()) {
            add_sitevisit.setError("required");
            add_sitevisit.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void setBoysTarget() {

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Submitting");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Call<String> call = apiInterface.addBoysTarget("0", UserCode, dateSlot.getSelectedItem().toString(), "Patna", dataCollection.getText().toString(), add_meeting.getText().toString(), add_sitevisit.getText().toString(), add_login.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                progress.cancel();
                if (response.isSuccessful()) {
//                    if (response.body().equalsIgnoreCase("Record Saved Successfully")) {

                    FancyToast.makeText(SelfTarget.this, "Target Submitted", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    finish();

//                    } else {
////
//                        FancyToast.makeText(SelfTarget.this, "Sorry! Please try again later.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.cancel();
                FancyToast.makeText(SelfTarget.this, "Sorry! Something went wrong!.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });

    }

    private void setGirlsTarget() {

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Submitting");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Call<String> call = apiInterface.addGirlsfTarget("0", UserCode, dateSlot.getSelectedItem().toString(), "Patna",
                dialCall.getText().toString(), ansCall.getText().toString(), add_meeting.getText().toString(), add_sitevisit.getText().toString(), add_login.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                progress.cancel();
                if (response.isSuccessful()) {
//                    if (response.body().equalsIgnoreCase("Record Saved Successfully")) {

                    FancyToast.makeText(SelfTarget.this, "Target Submitted", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    finish();

//                    } else {
////
//                        FancyToast.makeText(SelfTarget.this, "Sorry! Please try again later.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.cancel();
                FancyToast.makeText(SelfTarget.this, "Sorry! Something went wrong!.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_add:
                if (!linearForm.isShown()) {
                    linearForm.setVisibility(View.VISIBLE);
                    targetRv.setVisibility(View.GONE);
                    add.setCardBackgroundColor(ContextCompat.getColor(SelfTarget.this, R.color.yellow));
                    list.setCardBackgroundColor(ContextCompat.getColor(SelfTarget.this, R.color.unselected_card));
                    add.setRadius(10);
                    list.setRadius(10);
                }
                break;

            case R.id.text_list:
                if (!targetRv.isShown()) {
                    linearForm.setVisibility(View.GONE);
                    targetRv.setVisibility(View.VISIBLE);
                    add.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.unselected_card));
                    list.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
                    add.setRadius(10);
                    list.setRadius(10);

                    if (gender.equals("Girls"))
                        getGirlsTargets();
                    else
                        getBoysTargets();

                    setTargets();
                }
                break;

            default:
        }

    }

    private void getBoysTargets() {

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        final ProgressDialog pd = new ProgressDialog(SelfTarget.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
        pd.show();

        targetList.clear();

        Call<List<TargetsModel>> call = apiInterface.getBoysTargets(UserCode);
        call.enqueue(new Callback<List<TargetsModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<TargetsModel>> call, retrofit2.Response<List<TargetsModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        targetList = response.body();

                        targetAdapter.setPostList(targetList);

                        pd.cancel();

                    } else {
                        pd.cancel();
                    }


                } else {
                    pd.cancel();
                    FancyToast.makeText(SelfTarget.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<List<TargetsModel>> call, Throwable t) {
                pd.cancel();
                FancyToast.makeText(SelfTarget.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void getGirlsTargets() {

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        final ProgressDialog pd = new ProgressDialog(SelfTarget.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
        pd.show();

        targetList.clear();

        Call<List<TargetsModel>> call = apiInterface.getGirlsTargets(UserCode);
        call.enqueue(new Callback<List<TargetsModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<TargetsModel>> call, retrofit2.Response<List<TargetsModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        targetList = response.body();

                        targetAdapter.setPostList(targetList);

                        pd.cancel();

                    } else {
                        pd.cancel();
                    }


                } else {
                    pd.cancel();
                    FancyToast.makeText(SelfTarget.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<List<TargetsModel>> call, Throwable t) {
                pd.cancel();
                FancyToast.makeText(SelfTarget.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setTargets() {
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelfTarget.this, VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(false);
        targetRv.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        targetAdapter = new SelfTargetAdaptor(SelfTarget.this, targetList);
        targetRv.setAdapter(targetAdapter);
        targetRv.setItemAnimator(null);

        targetRv.setNestedScrollingEnabled(false);

    }

}
