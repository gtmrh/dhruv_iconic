package com.rkvitsolutions.dhruviconic.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rkvitsolutions.dhruviconic.Adapter.FollowupDetailsAdaptor;
import com.rkvitsolutions.dhruviconic.Model.FollowupDetailsModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class FollowupReport extends AppCompatActivity {

    private AVLoadingIndicatorView loader;
    private SwipeRefreshLayout flowupSwipeRefreshLayout;
    private RecyclerView followupRv;
    private List<FollowupDetailsModel> followupList = new ArrayList<>();
    private ApiInterface apiInterface;
    private FollowupDetailsAdaptor folloupAdaper;
    private String mobileNo;
    private EditText clientMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        flowupSwipeRefreshLayout = findViewById(R.id.swipeContainer_followup);
        followupRv = findViewById(R.id.followup_rv);
        mobileNo = getIntent().getStringExtra("mobileNo");
//        Log.v("mobileNo", mobileNo);

        clientMobile = findViewById(R.id.client_mobile);

        if (mobileNo!=null)
            clientMobile.setText(mobileNo);

        clientMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mobileNo = clientMobile.getText().toString();

                    if (mobileNo != null) {
                        getFollowup(mobileNo);
                        setFollowup();
                    }
                }
                return false;
            }
        });

        flowupSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flowupSwipeRefreshLayout.setRefreshing(false);
                if (mobileNo != null) {
                    getFollowup(mobileNo);
                    setFollowup();
                }
            }
        });

        if (mobileNo != null) {
            getFollowup(mobileNo);
            setFollowup();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    private void getFollowup(String mobileNo) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
        pd.show();

        followupList.clear();

        Call<List<FollowupDetailsModel>> call = apiInterface.followpupDetails(mobileNo);
        call.enqueue(new Callback<List<FollowupDetailsModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<FollowupDetailsModel>> call, retrofit2.Response<List<FollowupDetailsModel>> response) {

//                loader.setVisibility(View.GONE);
//                layoutview.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {

                        pd.cancel();

                        followupList = response.body();

//                        todaysFollowupList = filterByDatenSts();
                        folloupAdaper.setPostList(followupList);

//                        if (todaysFollowupList.isEmpty() || todaysFollowupList == null)
//                            noOfFollowup.setText("0");
//
//                        else
//                        noOfFollowup.setText(String.valueOf(todaysFollowupList.size()));

//                        noOfFollowup.setText(String.valueOf(followupList.size()));
//                        folloupAdaper.setPostList(followupList);

                    } else {
                        pd.cancel();
//                        noOfFollowup.setText("0");
                    }


                } else {
                    pd.cancel();
                    FancyToast.makeText(FollowupReport.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                }
            }

            @Override
            public void onFailure(Call<List<FollowupDetailsModel>> call, Throwable t) {
                pd.cancel();
//                loader.setVisibility(View.GONE);
                FancyToast.makeText(FollowupReport.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });

    }

    private void setFollowup() {
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FollowupReport.this, VERTICAL, false);
        followupRv.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        folloupAdaper = new FollowupDetailsAdaptor(FollowupReport.this, followupList);
        followupRv.setAdapter(folloupAdaper);
        followupRv.setItemAnimator(null);
        followupRv.setNestedScrollingEnabled(false);
    }
}