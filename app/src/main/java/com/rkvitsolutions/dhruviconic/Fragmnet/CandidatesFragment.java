package com.rkvitsolutions.dhruviconic.Fragmnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.rkvitsolutions.dhruviconic.Activity.HrReportActivity;
import com.rkvitsolutions.dhruviconic.Adapter.CandidateAdaptor;
import com.rkvitsolutions.dhruviconic.Adapter.InterviewsAdaptor;
import com.rkvitsolutions.dhruviconic.Adapter.JoiningsAdaptor;
import com.rkvitsolutions.dhruviconic.Model.CandidateModel;
import com.rkvitsolutions.dhruviconic.Model.JoiningsModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class CandidatesFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView interviewCard, reportCard, leadCard, joiningCard;
    private DrawerLayout drawer;
    private TextView name, mobileNo, txtImg;
    private NavigationView navigationView;
    private String UserCode, UserName, UserType, UserTeam, BookingSts;
    private String Name, Code, Dated, MobileNo, Project, Profession, Budget, Status, AltNo, Location;
    private List<CandidateModel> candidateList = new ArrayList<>();
    private CandidateAdaptor candidateAdaptor;
    private InterviewsAdaptor interviewsAdaptor;
    private RecyclerView leadRV, interviewRV, joiningRV;
    private TextView noOfLead, noOfInterview, text_lead, joiningNo;
    private RelativeLayout layoutview;
    private AVLoadingIndicatorView loader;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private List<CandidateModel> interviewList = new ArrayList<>();
    private List<JoiningsModel> joiningList = new ArrayList<>();
    private JoiningsAdaptor joiningsAdaptor;

    public CandidatesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CandidatesFragment newInstance(String param1, String param2) {
        CandidatesFragment fragment = new CandidatesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_candidates, container, false);


        mySwipeRefreshLayout = v.findViewById(R.id.swipeContainer);
        leadRV = v.findViewById(R.id.leads_rv);
        interviewRV = v.findViewById(R.id.interview_rv);
        joiningRV = v.findViewById(R.id.joining_rv);

        initIds(v);

        getCandidates();
        setCadidates();

        getInterviews();
        setInterviews();

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mySwipeRefreshLayout.setRefreshing(false);
                initIds(v);
                getCandidates();
                setCadidates();
                getInterviews();
                setInterviews();
            }
        });

        return v;
    }

    @SuppressLint("SetTextI18n")
    private void initIds(View v) {

        SharedPreferences pref = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);

        UserCode = pref.getString("userCode", null);
        UserName = pref.getString("username", null);
        UserType = pref.getString("userType", null);
        UserTeam = pref.getString("userTeam", null);
        BookingSts = pref.getString("bookingStatus", null);

        noOfLead = v.findViewById(R.id.lead_no);
        noOfInterview = v.findViewById(R.id.interview_no);
        joiningNo = v.findViewById(R.id.joining_no);

        layoutview = v.findViewById(R.id.layout_view);
        loader = v.findViewById(R.id.loader);

        leadCard = v.findViewById(R.id.leads_card);
        leadCard.setOnClickListener(this);

        interviewCard = v.findViewById(R.id.interview_card);
        interviewCard.setOnClickListener(this);

        joiningCard = v.findViewById(R.id.joining_card);
        joiningCard.setOnClickListener(this);

        reportCard = v.findViewById(R.id.report_card);
        reportCard.setOnClickListener(this);

        text_lead = v.findViewById(R.id.text_lead_filter);

    }

    private void getCandidates() {

        candidateList.clear();

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<List<CandidateModel>> call = apiInterface.getCandidates(UserCode);
        call.enqueue(new Callback<List<CandidateModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<CandidateModel>> call, retrofit2.Response<List<CandidateModel>> response) {

                loader.setVisibility(View.GONE);
                layoutview.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        candidateList = response.body();

                        //number of Candidate
                        noOfLead.setText(String.valueOf(candidateList.size()));

                        candidateAdaptor.setPostList(candidateList);

                    }


                } else
                    FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }

            @Override
            public void onFailure(Call<List<CandidateModel>> call, Throwable t) {
                loader.setVisibility(View.GONE);
                FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setCadidates() {
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);
        leadRV.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        candidateAdaptor = new CandidateAdaptor(getActivity(), candidateList, CandidatesFragment.this);
        leadRV.setAdapter(candidateAdaptor);
        leadRV.setNestedScrollingEnabled(false);

    }

    public void setTotalLead(int ttlLead) {
        noOfLead.setText(String.valueOf(ttlLead));
        getInterviews();
    }

    public void setTotalInterviews(int ttlLead) {
        noOfInterview.setText(String.valueOf(ttlLead));

    }

    private void getInterviews() {


        interviewList.clear();

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<List<CandidateModel>> call = apiInterface.getInterviews(UserCode, "NA");
        call.enqueue(new Callback<List<CandidateModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<CandidateModel>> call, retrofit2.Response<List<CandidateModel>> response) {

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        interviewList = response.body();

                        //number of Candidate
                        noOfInterview.setText(String.valueOf(interviewList.size()));

                        interviewsAdaptor.setPostList(interviewList);

                    }


                } else
                    FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }

            @Override
            public void onFailure(Call<List<CandidateModel>> call, Throwable t) {
                loader.setVisibility(View.GONE);
                FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setInterviews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);
        interviewRV.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        interviewsAdaptor = new InterviewsAdaptor(getActivity(), interviewList, CandidatesFragment.this);
        interviewRV.setAdapter(interviewsAdaptor);
        interviewRV.setNestedScrollingEnabled(false);

    }

    private void getJoinings() {

        joiningList.clear();
        String todaysDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<List<JoiningsModel>> call = apiInterface.getJoinings(UserCode, todaysDate, todaysDate);
        call.enqueue(new Callback<List<JoiningsModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<JoiningsModel>> call, retrofit2.Response<List<JoiningsModel>> response) {

//                loader.setVisibility(View.GONE);
//                layoutview.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        joiningList = response.body();

                        //number of Candidate
                        joiningNo.setText(String.valueOf(joiningList.size()));

                        joiningsAdaptor.setPostList(joiningList);

                    }


                } else
                    FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }

            @Override
            public void onFailure(Call<List<JoiningsModel>> call, Throwable t) {
                loader.setVisibility(View.GONE);
                FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setJoinings() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);
        joiningRV.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        joiningsAdaptor = new JoiningsAdaptor(getActivity(), joiningList, CandidatesFragment.this);
        joiningRV.setAdapter(joiningsAdaptor);
        joiningRV.setNestedScrollingEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.leads_card:
                if (!leadRV.isShown()) {

                    interviewRV.setVisibility(View.GONE);
                    leadRV.setVisibility(View.VISIBLE);
                    joiningRV.setVisibility(View.GONE);

                    joiningCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));
                    leadCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_card));
                    interviewCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));

                    leadCard.setRadius(10);
                    interviewCard.setRadius(10);
                    joiningCard.setRadius(10);


                    getCandidates();
                    setCadidates();
                    getInterviews();

                }

                break;

            case R.id.interview_card:
                if (!interviewRV.isShown()) {

                    interviewRV.setVisibility(View.VISIBLE);
                    leadRV.setVisibility(View.GONE);
                    joiningRV.setVisibility(View.GONE);

                    joiningCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));
                    leadCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));
                    interviewCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_card));

                    leadCard.setRadius(10);
                    interviewCard.setRadius(10);
                    joiningCard.setRadius(10);

                    getCandidates();
                    getInterviews();
                    setInterviews();

                }
                break;

            case R.id.joining_card:
                if (!joiningRV.isShown()) {

                    joiningRV.setVisibility(View.VISIBLE);
                    interviewRV.setVisibility(View.GONE);
                    leadRV.setVisibility(View.GONE);

                    leadCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));
                    interviewCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));
                    joiningCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_card));

                    leadCard.setRadius(10);
                    interviewCard.setRadius(10);
                    joiningCard.setRadius(10);

//                    getCandidates();
//                    getInterviews();
//                    setInterviews();

                    getJoinings();
                    setJoinings();

                }
                break;

            case R.id.report_card:
                startActivity(new Intent(getActivity(), HrReportActivity.class));
                break;

            default:
        }
    }


}