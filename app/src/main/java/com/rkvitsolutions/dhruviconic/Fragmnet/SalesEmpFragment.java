package com.rkvitsolutions.dhruviconic.Fragmnet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rkvitsolutions.dhruviconic.Activity.ReportActivity;
import com.rkvitsolutions.dhruviconic.Adapter.FollowupAdaptor;
import com.rkvitsolutions.dhruviconic.Adapter.LeadsAdaptor;
import com.rkvitsolutions.dhruviconic.Filter.FlwFilterAdapter;
import com.rkvitsolutions.dhruviconic.Filter.FollowupFilter;
import com.rkvitsolutions.dhruviconic.Filter.FollowupFilterPreferences;
import com.rkvitsolutions.dhruviconic.Filter.LeadFilter;
import com.rkvitsolutions.dhruviconic.Filter.LeadFilterAdapter;
import com.rkvitsolutions.dhruviconic.Filter.LeadFilterPreferences;
import com.rkvitsolutions.dhruviconic.Model.FollowupModel;
import com.rkvitsolutions.dhruviconic.Model.ProjectModel;
import com.rkvitsolutions.dhruviconic.Model.SalesLeadsModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
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

import static android.content.Context.MODE_PRIVATE;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class SalesEmpFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView leadsCard, followupCard, reportCard;
    private String UserCode, UserName, UserType, UserTeam, BookingSts;
    private String Name, Code, Dated, MobileNo, Project, Profession, Budget, Status, AltNo, Location;
    private List<SalesLeadsModel> leadList = new ArrayList<>();
    private LeadsAdaptor leadsAdaptor;
    private RecyclerView leadRv, followupRv;
    private TextView noOfLead, noOfFollowup, textLeadFilter, dateFilter, followupFilter;
    private RelativeLayout layoutview;
    private AVLoadingIndicatorView loader;
    private SwipeRefreshLayout leadSwipeRefreshLayout, flowupSwipeRefreshLayout;
    private AlertDialog followAlert;

    private EditText followupAltNo, followupLocation, followupDate, city, followupPurpose, followupConversation, nextFollowupDate, followupName;
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
    private ImageView close;
    private FrameLayout frameLayout;
    private Fragment fragment;
    private ApiInterface apiInterface;
    private List<FollowupModel> followupList = new ArrayList<>();
    private List<FollowupModel> todaysFollowupList = new ArrayList<>();
    private FollowupAdaptor folloupAdaper;
    private String todaysDate;
    private LinearLayout filter;
    private Spinner statusFilter;
    private String filtersts;
    private BottomSheetDialog dialog;
    private RecyclerView leadFilterRV, leadFilterValuesRV, flwFilterRV, flwFilterValuesRV;
    private List<ProjectModel> projectList = new ArrayList<>();
    private List<String> projectName = new ArrayList<>();
    private LeadFilterAdapter filterAdapter;
    private List<SalesLeadsModel> filteredItems;
    private List<SalesLeadsModel> filteredList;
    private FlwFilterAdapter flwFilterAdapter;
    private List<FollowupModel> flwFilteredItems;
    private List<FollowupModel> flwFilteredList;
    private List<String> leadSourceList = new ArrayList<>();
    private List<String> followupSourceList = new ArrayList<>();


    public SalesEmpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SalesEmpFragment newInstance(String param1, String param2) {
        SalesEmpFragment fragment = new SalesEmpFragment();
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
        View v = inflater.inflate(R.layout.fragment_sales_emp, container, false);

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        todaysDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        leadSwipeRefreshLayout = v.findViewById(R.id.swipeContainer_lead);
        flowupSwipeRefreshLayout = v.findViewById(R.id.swipeContainer_followup);
        leadRv = v.findViewById(R.id.leads_rv);
        followupRv = v.findViewById(R.id.followup_rv);

        initIds(v);

        getLeads();
        setLeads();

        getFollowup(todaysDate);
        setFollowup();

        getProjects();

        leadSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leadSwipeRefreshLayout.setRefreshing(false);
                initIds(v);
                getLeads();
                setLeads();
//                getFollowup(todaysDate);
//                setFollowup();
            }
        });

        flowupSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flowupSwipeRefreshLayout.setRefreshing(false);
                initIds(v);
//                getLeads();
                getFollowup(todaysDate);
                setFollowup();
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


        textLeadFilter = v.findViewById(R.id.text_lead_filter);
        textLeadFilter.setOnClickListener(this);

        followupFilter = v.findViewById(R.id.followup_filter);
        followupFilter.setOnClickListener(this);


        noOfLead = v.findViewById(R.id.lead_no);
        noOfFollowup = v.findViewById(R.id.followup_no);

        layoutview = v.findViewById(R.id.layout_view);
        loader = v.findViewById(R.id.loader);
        filter = v.findViewById(R.id.filter);

        leadsCard = v.findViewById(R.id.leads_card);
        leadsCard.setOnClickListener(this);

        followupCard = v.findViewById(R.id.followup_card);
        followupCard.setOnClickListener(this);

        reportCard = v.findViewById(R.id.report_card);
        reportCard.setOnClickListener(this);

        dateFilter = v.findViewById(R.id.date_filter);
        dateFilter.setOnClickListener(this);

        dateFilter.setText(todaysDate);

//        statusFilter = v.findViewById(R.id.status_filter);
//        statusFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                getFollowup(todaysDate);
//                setFollowup();
//
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void getLeads() {

            AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(getActivity());
            avLoadingIndicatorView.setIndicator("BallPulseIndicator");
            avLoadingIndicatorView.setIndicatorColor(R.color.colorPrimary);

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Loading");
            pd.setMessage("Please wait...");
            pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
            pd.show();
    // Set custom view(Loading Indicator View) after showing dialog
    //        pd.setContentView(avLoadingIndicatorView);


            leadList.clear();

            Call<List<SalesLeadsModel>> call = apiInterface.getLeads(UserCode);
            call.enqueue(new Callback<List<SalesLeadsModel>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<List<SalesLeadsModel>> call, retrofit2.Response<List<SalesLeadsModel>> response) {

                    if (response.isSuccessful()) {
                        if (response.body().size() > 0) {
                            leadList = response.body();

                            //number of Candidate
                            noOfLead.setText(String.valueOf(leadList.size()));

                            for (int i =0; i<leadList.size(); i++){
                                leadSourceList.add(leadList.get(i).getSourse());
                            }

                            leadsAdaptor.setPostList(leadList);

                            pd.cancel();
                            loader.setVisibility(View.GONE);
                            layoutview.setVisibility(View.VISIBLE);

                        } else {
                            pd.cancel();
                            loader.setVisibility(View.GONE);
                            layoutview.setVisibility(View.VISIBLE);
                            noOfLead.setText(String.valueOf(leadList.size()));
    //                        FancyToast.makeText(getActivity(), "No Data for today! Contact the admin.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }


                    } else {
                        pd.cancel();
                        loader.setVisibility(View.GONE);
                        layoutview.setVisibility(View.VISIBLE);
                        FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                }

                @Override
                public void onFailure(Call<List<SalesLeadsModel>> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    pd.cancel();
                    FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    Log.v("ImageUploadError", t.toString());
                    call.cancel();
                }
            });

    }

    private void setLeads() {
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(false);
        leadRv.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        leadsAdaptor = new LeadsAdaptor(getActivity(), leadList, SalesEmpFragment.this);
        leadRv.setAdapter(leadsAdaptor);
        leadRv.setItemAnimator(null);

        leadRv.setNestedScrollingEnabled(false);

    }

    public void setTotalLead(int ttlLead) {
        noOfLead.setText(String.valueOf(ttlLead));
        getFollowup(todaysDate);
    }

    public void setTotalFollowup(int ttlLead) {
        noOfFollowup.setText(String.valueOf(ttlLead));
//        getLeadsNo();
    }

    private void getFollowup(String NDate) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false); // disable dismiss by tapping outside of the dialog
        pd.show();

        followupList.clear();
        todaysFollowupList.clear();

        Call<List<FollowupModel>> call = apiInterface.getFollowup(UserCode, NDate);
        call.enqueue(new Callback<List<FollowupModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<FollowupModel>> call, retrofit2.Response<List<FollowupModel>> response) {


                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {

                        pd.cancel();

                        followupList = response.body();

                        for (int i =0; i<followupList.size(); i++){
                            followupSourceList.add(followupList.get(i).getSourse());
                        }

//                        todaysFollowupList = filterByDatenSts();
                        Log.v("fno", String.valueOf(todaysFollowupList.size()));
                        folloupAdaper.setPostList(followupList);

//                        if (todaysFollowupList.isEmpty() || todaysFollowupList == null)
//                            noOfFollowup.setText("0");
//
//                        else
                        noOfFollowup.setText(String.valueOf(followupList.size()));

//                        noOfFollowup.setText(String.valueOf(followupList.size()));
//                        folloupAdaper.setPostList(followupList);

                    } else {
                        pd.cancel();
                        noOfFollowup.setText("0");
                    }


                } else {
                    pd.cancel();
                    FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                }
            }

            @Override
            public void onFailure(Call<List<FollowupModel>> call, Throwable t) {
                pd.cancel();
//                loader.setVisibility(View.GONE);
                FancyToast.makeText(getActivity(), "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setFollowup() {
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);
        followupRv.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        folloupAdaper = new FollowupAdaptor(getActivity(), todaysFollowupList, SalesEmpFragment.this);
        followupRv.setAdapter(folloupAdaper);
        followupRv.setItemAnimator(null);
        followupRv.setNestedScrollingEnabled(false);
    }

    @SuppressLint("SimpleDateFormat")
//    private List<FollowupModel> filterByDatenSts() {
//        todaysFollowupList.clear();
//
//        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//        Date date = new Date();
//
////        String filterSts = statusFilter.getSelectedItem().toString().toLowerCase();
//
//        for (FollowupModel followupModel : followupList) {
//            String nextDate = followupModel.getNDate();
//
//            Log.v("dates", dateFormat.format(date) + "------nextdate" + nextDate);
//
//            todaysDate = dateFilter.getText().toString();
//            String dateSts = compareDate(nextDate, todaysDate);
//            String sts = followupModel.getStatus().toLowerCase();
//
//
//            if (!filterSts.equals("all")) {
//
//                if (sts.equals(filterSts)) {
//
//                    Log.v("All", sts + ">>>>>" + filterSts);
//
////                if (dateSts.equals("Date1 is equal to Date2") && sts.equals(filterSts))
////                    todaysFollowupList.add(followupModel);
//
////                if (sts.equals(filterSts))
//                    todaysFollowupList.add(followupModel);
//
//                }
//            } else {
//
////                Log.v("All", sts+">>>>>"+filterSts);
////
////                if (dateSts.equals("Date1 is equal to Date2"))
//                todaysFollowupList.add(followupModel);
//            }
//
//        }
//
//        return todaysFollowupList;
//    }

    private String compareDate(String nextDate, String todaysDate) {

        String result = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
//        dd-MM-yyyy
        Date date1 = null;
        try {
            date1 = sdf.parse(nextDate);
            Log.v("nextD", date1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = sdf.parse(todaysDate);
            Log.v("D2", date2.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1 != null) {
            if (date1.compareTo(date2) > 0) {
                System.out.println("Date1 is after Date2");
                result = "Date1 is after Date2";
            } else if (date1.compareTo(date2) < 0) {
                System.out.println("Date1 is before Date2");
                result = "Date1 is before Date2";
            } else if (date1.compareTo(date2) == 0) {
                System.out.println("Date1 is equal to Date2");
                result = "Date1 is equal to Date2";
            }
//            else {
//                System.out.println("How to get here?");
//            }
        }


        return result;
    }

    private List<FollowupModel> filterByStatus(String status) {
//        todaysFollowupList.clear();

        for (FollowupModel followupModel : todaysFollowupList) {
            String sts = followupModel.getStatus().toLowerCase();

            if (sts.contains(filtersts)) {
                {
                    todaysFollowupList.add(followupModel);

                }
            }
        }

        return todaysFollowupList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.leads_card:
                if (!leadSwipeRefreshLayout.isShown()) {

                    flowupSwipeRefreshLayout.setVisibility(View.GONE);
                    leadSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    filter.setVisibility(View.GONE);
                    textLeadFilter.setVisibility(View.VISIBLE);

                    leadsCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_card));
                    leadsCard.setRadius(10);
                    followupCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));
                    followupCard.setRadius(10);

                    getLeads();
                    setLeads();
                    getFollowup(todaysDate);

                }

                break;

            case R.id.followup_card:
                if (!flowupSwipeRefreshLayout.isShown()) {

                    flowupSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    leadSwipeRefreshLayout.setVisibility(View.GONE);
                    filter.setVisibility(View.VISIBLE);
                    textLeadFilter.setVisibility(View.GONE);

                    leadsCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselected_card));
                    followupCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_card));
                    leadsCard.setRadius(10);
                    followupCard.setRadius(10);

//                    textLead.setText("Today's Followups");

                    getLeads();
                    getFollowup(todaysDate);
                    setFollowup();

                }
                break;

            case R.id.report_card:
                startActivity(new Intent(getActivity(), ReportActivity.class));
                break;

            case R.id.date_filter:
                dateWiseFilter();
                break;

            case R.id.text_lead_filter:
                leadFilterMenu();
                break;

            case R.id.followup_filter:
                followupFilterMenu();
                break;

            default:
        }
    }

    private void dateWiseFilter() {

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                todaysDate = sdf.format(myCalendar.getTime());

                dateFilter.setText(sdf.format(myCalendar.getTime()));

                getFollowup(todaysDate);
                setFollowup();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        //Max Date
//                datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

        //Min Date
//        Calendar minCal = Calendar.getInstance();
//        minCal.set(myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());

        datePickerDialog.show();
    }


    private void getProjects() {

        projectList.clear();
        projectName.clear();

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

                        }


                    }
                }

            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable t) {
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }


    // <---------------------------------------------Filter Leads------------------------------------------------------------------------------------>
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void leadFilterMenu() {

        dialog = new BottomSheetDialog(getActivity());
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
        leadFilterRV = v.findViewById(R.id.filterRV);
        leadFilterValuesRV = v.findViewById(R.id.filterValuesRV);
        leadFilterRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        leadFilterValuesRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> projects = projectName;
        if (!LeadFilterPreferences.filters.containsKey(LeadFilter.INDEX_PROJECT)) {
            LeadFilterPreferences.filters.put(LeadFilter.INDEX_PROJECT, new LeadFilter("Project", projects, new ArrayList()));
        }

        List<String> source = leadSourceList.stream().distinct().collect(Collectors.toList());
        if (!LeadFilterPreferences.filters.containsKey(LeadFilter.INDEX_SOURCE)) {
            LeadFilterPreferences.filters.put(LeadFilter.INDEX_SOURCE, new LeadFilter("Source", source, new ArrayList()));
        }

        filterAdapter = new LeadFilterAdapter(getActivity(), LeadFilterPreferences.filters, leadFilterValuesRV);
        leadFilterRV.setAdapter(filterAdapter);

        Button clearB = v.findViewById(R.id.clearB);
        clearB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeadFilterPreferences.filters.get(LeadFilter.INDEX_PROJECT).setSelected(new ArrayList());
                LeadFilterPreferences.filters.get(LeadFilter.INDEX_SOURCE).setSelected(new ArrayList());

                leadsAdaptor.setFilter(leadList);
                dialog.hide();

            }
        });

        Button applyB = v.findViewById(R.id.applyB);
        applyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilteredData();
//                filterDialog.cancel();
//                startActivity(new Intent(FilterActivity.this, HotelListing.class));
            }
        });
    }

    private void updateFilteredData() {
        if (!LeadFilterPreferences.filters.isEmpty()) {
            filteredItems = new ArrayList<SalesLeadsModel>();
            List<String> projects = LeadFilterPreferences.filters.get(LeadFilter.INDEX_PROJECT).getSelected();
            List<String> source = LeadFilterPreferences.filters.get(LeadFilter.INDEX_SOURCE).getSelected();
            for (SalesLeadsModel item : leadList) {
                boolean projectMatched = true;
                if (projects.size() > 0 && !projects.contains(item.getProject())) {
                    projectMatched = false;
                }
                boolean sourceMatched = true;
                if (source.size() > 0 && !source.contains(item.getSourse())) {
                    sourceMatched = false;
                }

                if (projectMatched && sourceMatched) {
                    filteredItems.add(item);
                }
            }
            filteredList = filteredItems;
        }

        leadsAdaptor.setFilter(filteredList);
        dialog.hide();
    }


    // <---------------------------------------------Filter Leads------------------------------------------------------------------------------------>
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void followupFilterMenu() {

        dialog = new BottomSheetDialog(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_filter, null);
        dialog.setContentView(dialogView);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        initFlwControls(dialogView);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    private void initFlwControls(View v) {
        flwFilterRV = v.findViewById(R.id.filterRV);
        flwFilterValuesRV = v.findViewById(R.id.filterValuesRV);
        flwFilterRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        flwFilterValuesRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> callcode = Arrays.asList(getResources().getStringArray(R.array.call_code_data));
        if (!FollowupFilterPreferences.filters.containsKey(FollowupFilter.INDEX_CALLCODE)) {
            FollowupFilterPreferences.filters.put(FollowupFilter.INDEX_CALLCODE, new FollowupFilter("Call Code", callcode, new ArrayList()));
        }

//        List<String> source = Arrays.asList(getResources().getStringArray(R.array.source));

        List<String> source = followupSourceList.stream().distinct().collect(Collectors.toList());

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

        List<String> projects = projectName;
        if (!FollowupFilterPreferences.filters.containsKey(FollowupFilter.INDEX_Project)) {
            FollowupFilterPreferences.filters.put(FollowupFilter.INDEX_Project, new FollowupFilter("Project", projects, new ArrayList()));
        }

        flwFilterAdapter = new FlwFilterAdapter(getActivity(), FollowupFilterPreferences.filters, flwFilterValuesRV);
        flwFilterRV.setAdapter(flwFilterAdapter);

        Button clearB = v.findViewById(R.id.clearB);
        clearB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CALLCODE).setSelected(new ArrayList());
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_SOURCE).setSelected(new ArrayList());
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_STATUS).setSelected(new ArrayList());
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CLIENTTYPE).setSelected(new ArrayList());
                FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_Project).setSelected(new ArrayList());

                folloupAdaper.setFilter(followupList);
                noOfFollowup.setText(String.valueOf(followupList.size()));

                dialog.hide();

            }
        });

        Button applyB = v.findViewById(R.id.applyB);
        applyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFlwFilteredData();
            }
        });
    }

    private void updateFlwFilteredData() {
        if (!FollowupFilterPreferences.filters.isEmpty()) {
            flwFilteredItems = new ArrayList<FollowupModel>();
            List<String> callCode = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CALLCODE).getSelected();
            List<String> source = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_SOURCE).getSelected();
            List<String> status = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_STATUS).getSelected();
            List<String> clientType = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_CLIENTTYPE).getSelected();
            List<String> projects = FollowupFilterPreferences.filters.get(FollowupFilter.INDEX_Project).getSelected();

            for (FollowupModel item : followupList) {

                boolean callCodeMatched = true;
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

                boolean projectMatched = true;
                if (projects.size() > 0 && !projects.contains(item.getProject())) {
                    projectMatched = false;
                }

                if (callCodeMatched && statusMatched && sourceMatched && clientTypeMatched && projectMatched) {
                    flwFilteredItems.add(item);
                }
            }
            flwFilteredList = flwFilteredItems;
        }

        folloupAdaper.setFilter(flwFilteredList);
        noOfFollowup.setText(String.valueOf(flwFilteredList.size()));
        dialog.hide();
    }

}
