package com.rkvitsolutions.dhruviconic.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.rkvitsolutions.dhruviconic.Adapter.LeadsAdaptor;
import com.rkvitsolutions.dhruviconic.Fragmnet.CandidatesFragment;
import com.rkvitsolutions.dhruviconic.Fragmnet.ManagerFragment;
import com.rkvitsolutions.dhruviconic.Fragmnet.OthersEmpFragment;
import com.rkvitsolutions.dhruviconic.Fragmnet.SalesEmpFragment;
import com.rkvitsolutions.dhruviconic.Model.DataModel;
import com.rkvitsolutions.dhruviconic.Model.ProjectModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.rkvitsolutions.dhruviconic.Utils.Constant;
import com.rkvitsolutions.dhruviconic.Utils.HelperActivity;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

//import com.google.android.play.core.appupdate.AppUpdateManager;

//import com.google.android.play.core.appupdate.AppUpdateManager;

public class MainHome extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

//    private static final int REQ_CODE_VERSION_UPDATE = 530;
//    private AppUpdateManager appUpdateManager;
//    private InstallStateUpdatedListener installStateUpdatedListener;

    private static final int RC_APP_UPDATE = 11;
    private CardView followupCard, reportCard;
    private String[] sourceList = {"Fb Lead IR", "Fb Old", "Fb IC", "IDC", "Extracted", "99 Acres", "Others"};
    private DrawerLayout drawer;
    private TextView name, employeeCode, txtImg;
    private NavigationView navigationView;
    private String UserCode, UserName, UserType, UserTeam, BookingSts;
    private String Name, Code, Dated, MobileNo, Project, Profession, Budget, Status, AltNo, Location;
    private ArrayList<DataModel> leadList = new ArrayList<>();
    private LeadsAdaptor leadsAdaptor;
    private RecyclerView leadRecyclerView;
    private TextView noOfLead, noOfFollowup;
    private RelativeLayout layoutview;
    private AVLoadingIndicatorView loader;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private AlertDialog followAlert;
    private EditText followupMobileNo, followupAltNo, followupLocation, followupDate, city, followupPurpose, followupConversation, nextFollowupDate, followupName;
    private Spinner mode, callCode, callStatus, source, clientType;
    //    private AutoCompleteTextView source;
    private Button saveBtn;
    private boolean valid;
    private String FollowupLocation, FollowupMobileNo, FollowupAltNo, FollowupDate, ConversationMode,
            CallCode, FollowupPurpose, FollowupConversation, CallStatus, NextFollowupDate, FollowupName;
    private String LeadCode;
    private ProgressBar progressbar;
    private ScrollView mainScroll;
    private int itemPosition;
    private LinearLayout nameLinear, altNoLinear, locationLinear;
    private ImageView close;
    private FrameLayout frameLayout;
    private Fragment fragment;
    private String SourceStr;
    private LinearLayout sourceLayout;
    private EditText addSource;
    private LinearLayout followupDateLinear;
    //    private AppUpdateManager mAppUpdateManager;
    private String cCode;
    private List<ProjectModel> projectList = new ArrayList<>();
    private List<String> projectName = new ArrayList<>();
    private List<String> projectId = new ArrayList<>();
    private AutoCompleteTextView project;
    private String ProjectId;
    private SharedPreferences mypref;
    private ApiInterface apiInterface;
    private CheckBox back_date_check;
    private String Ndate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        mypref = getSharedPreferences("UserData", MODE_PRIVATE);
        String userType = mypref.getString("userType", null);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            onGps();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initIds();

        frameLayout = findViewById(R.id.frameLayout);


        if (userType.equalsIgnoreCase("Executive")) {

            fragment = new SalesEmpFragment();
            loadFragment(fragment, "fragment_sales");

            navigationView.getMenu().findItem(R.id.nav_booking).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_candidate).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_sales_hr).setVisible(false);

        } else if (userType.equalsIgnoreCase("HR") || userType.equalsIgnoreCase("Human Resource")) {

            fragment = new ManagerFragment();
            loadFragment(fragment, "fragment_manager");

            navigationView.getMenu().findItem(R.id.nav_booking).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_new_followup).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_followup_details).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_followup_all).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_self_target).setVisible(false);

        } else if (userType.equalsIgnoreCase("Manager") || userType.equalsIgnoreCase("Assistant General Manager")
                || userType.equalsIgnoreCase("Team Manager")) {

            fragment = new ManagerFragment();
            loadFragment(fragment, "fragment_manager");

            navigationView.getMenu().findItem(R.id.nav_booking).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_new_followup).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_followup_details).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_candidate).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_sales_hr).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_followup_details).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_followup_all).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_self_target).setVisible(false);


        } else if (userType.equalsIgnoreCase("ACCOUNTANT")) {

            fragment = new ManagerFragment();
            loadFragment(fragment, "fragment_manager");

            navigationView.getMenu().findItem(R.id.nav_booking).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_new_followup).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_candidate).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_sales_hr).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_followup_details).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_followup_all).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_self_target).setVisible(false);

        } else if (userType.equalsIgnoreCase("Body Guard") || userType.equalsIgnoreCase("Driver")) {

            fragment = new OthersEmpFragment();
            loadFragment(fragment, "fragment_hr");

            navigationView.getMenu().findItem(R.id.nav_booking).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_new_followup).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_candidate).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_sales_hr).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_followup_details).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_followup_all).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_self_target).setVisible(false);

        }

    }

    private void loadFragment(Fragment fragment, String tag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    public void onGps() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AppTheme));

        alertDialogBuilder.setTitle("Enable GPS");
        alertDialogBuilder.setMessage("To mark attendance please enable GPS");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.notification_menu, menu);
//        final MenuItem menuItem = menu.findItem(R.id.menu_notification);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        searchItem.setVisible(false);
//        MenuItemCompat.setActionView(menuItem, R.layout.notification_menu_layout);
//        View actionView = MenuItemCompat.getActionView(menuItem);
//        ImageView cartImg = actionView.findViewById(R.id.cartImg);
//        cartImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (UserUid != null)
//                    startActivity(new Intent(MainProducts.this, CartActivity.class));
//                else
//                    LoginDialogue();
//            }
//        });
//        cartItemTxt = actionView.findViewById(R.id.cart_badge);

        //getCartItemsInArray
//        int count = new CartDatabase(getApplicationContext()).getItemCount();
        //getCartItemsInArraySize
//        cartItemTxt.setText(String.valueOf(count));


        return true;
    }

    @SuppressLint("SetTextI18n")
    private void initIds() {

        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);

        UserCode = pref.getString("userCode", null);
        UserName = pref.getString("username", null);
        UserType = pref.getString("userType", null);
        UserTeam = pref.getString("userTeam", null);
        BookingSts = pref.getString("bookingStatus", null);


        View headView = navigationView.getHeaderView(0);
        name = headView.findViewById(R.id.user_name);
        name.setText(UserName);

        employeeCode = headView.findViewById(R.id.mobile_no);
        employeeCode.setText("Code: " + UserCode + "\n" + UserType);

        TextView team = headView.findViewById(R.id.team);
        if (pref.getString("userTeam", null) != null)
            team.setText(pref.getString("userTeam", null).replace(" - ", ": "));

        txtImg = headView.findViewById(R.id.txtImg);

        String firstLetter = UserName.substring(0, 1).toUpperCase();
        txtImg.setText(firstLetter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_booking) {

            if (BookingSts.equalsIgnoreCase("1"))
                startActivity(new Intent(this, Projects.class));
            else
                FancyToast.makeText(MainHome.this, "Sorry! This option is not available for you.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        } else if (id == R.id.nav_new_followup) {
            openFollowUp();
        } else if (id == R.id.nav_apply_resign) {
            startActivity(new Intent(this, ApplyResignation.class));
        } else if (id == R.id.nav_self_evaluation) {
            startActivity(new Intent(this, SelfEvalution.class));
        } else if (id == R.id.nav_mark_attend) {
            startActivity(new Intent(this, MarkAttendance.class));
        } else if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainHome.class));
        } else if (id == R.id.nav_apply_leave) {
            startActivity(new Intent(this, ApplyLeave.class));
        } else if (id == R.id.nav_share) {
            new HelperActivity(this).shareApp();

        } else if (id == R.id.nav_rateus) {
            new HelperActivity((this)).rateUs();

        } else if (id == R.id.nav_logout) {

            SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear().apply();
            FancyToast.makeText(this, "Logged Out!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            startActivity(new Intent(this, Login.class));

            SharedPreferences loginPref = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
            loginPref.edit().clear().apply();


        } else if (id == R.id.nav_about_us) {

            Intent in = new Intent(this, Admin.class);
            in.putExtra("url", Constant.AboutUs);
            startActivity(in);

        } else if (id == R.id.nav_contactus) {

            Intent in = new Intent(this, Admin.class);
            in.putExtra("url", Constant.ContactUs);
            startActivity(in);


        } else if (id == R.id.nav_candidate) {

//            startActivity(new Intent(MainHome.this, CandidatesFragment.class));
            fragment = new CandidatesFragment();
            loadFragment(fragment, "fragment_candidate");

        } else if (id == R.id.nav_sales_hr) {

//            startActivity(new Intent(MainHome.this, CandidatesFragment.class));
            fragment = new SalesEmpFragment();
            loadFragment(fragment, "fragment_sales");

        } else if (id == R.id.nav_followup_details) {
            startActivity(new Intent(MainHome.this, FollowupReport.class));
        } else if (id == R.id.nav_followup_all) {
//            Toast.makeText(this, "Available in next update", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainHome.this, AllFollowupReport.class));
        } else if (id == R.id.nav_self_target) {

            if (mypref.getString("gender", null) != null) {

                if (mypref.getString("gender", null).equalsIgnoreCase("Male")) {
                    Intent i = new Intent(MainHome.this, SelfTarget.class);
                    i.putExtra("Gender", "Boys");
                    startActivity(i);
                } else {
                    Intent i = new Intent(MainHome.this, SelfTarget.class);
                    i.putExtra("Gender", "Girls");
                    startActivity(i);
                }
            } else
                Toast.makeText(this, "Gender Not Found, Ask Admin To Update", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    //For New Leads
    private void openFollowUp() {

        final AlertDialog.Builder diolog = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_field_followup_layout, null);
        diolog.setView(dialogView);
        followAlert = diolog.create();

        followAlert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        followAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        followAlert.show();

        followAlert.setCancelable(false);
        initView(dialogView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View dialogView) {

        getProjects();
        sourceLayout = dialogView.findViewById(R.id.source_layout);
        addSource = dialogView.findViewById(R.id.add_source);

        back_date_check = dialogView.findViewById(R.id.back_date_check);
        Ndate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        back_date_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Ndate = getBackDateString();
                else
                    Ndate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
            }
        });

        followupDateLinear = dialogView.findViewById(R.id.followup_date_linear);

        project = dialogView.findViewById(R.id.add_project);

        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainHome.this, android.R.layout.simple_dropdown_item_1line, projectName);
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
                project.setText(ProjectName);
            }
        });

        source = dialogView.findViewById(R.id.source);
        source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SourceStr = parent.getItemAtPosition(position).toString();

//                if (SourceStr.equals("Others")) {
//                    sourceLayout.setVisibility(View.VISIBLE);
//                } else {
//                    SourceStr = addSource.getText().toString();
//                    sourceLayout.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        close = dialogView.findViewById(R.id.close);
//        mainScroll = dialogView.findViewById(R.id.mainScroll);
        followupDate = dialogView.findViewById(R.id.followup_date);
        mode = dialogView.findViewById(R.id.mode);
        callCode = dialogView.findViewById(R.id.call_code);
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

        followupMobileNo = dialogView.findViewById(R.id.add_mobile_no);
        followupAltNo = dialogView.findViewById(R.id.add_alt_no);
        followupLocation = dialogView.findViewById(R.id.add_location);
        followupName = dialogView.findViewById(R.id.add_name);
        nameLinear = dialogView.findViewById(R.id.name_linear);
        altNoLinear = dialogView.findViewById(R.id.altno_linear);
        locationLinear = dialogView.findViewById(R.id.location_linear);

//        if (list.get(itemPosition).getLeadName() == null || list.get(itemPosition).getLeadName().isEmpty() || list.get(itemPosition).getLeadName().equals("NA"))
//            nameLinear.setVisibility(View.VISIBLE);
//
//        if (list.get(itemPosition).getLeadAltNo() == null || list.get(itemPosition).getLeadAltNo().isEmpty() || list.get(itemPosition).getLeadAltNo().equals("NA"))
//            altNoLinear.setVisibility(View.VISIBLE);
//
//        if (list.get(itemPosition).getLeadLocation() == null || list.get(itemPosition).getLeadLocation().isEmpty() || list.get(itemPosition).getLeadLocation().equals("NA"))
//            locationLinear.setVisibility(View.VISIBLE);

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

                String status = parent.getItemAtPosition(position).toString();
                if (status.equals("WON") || status.equals("Loss") || status.equals("None")) {
                    followupDateLinear.setVisibility(View.GONE);
                    nextFollowupDate.setText(getYesterdayDateString());
//                    followupPurpose.setText(parent.getItemAtPosition(position).toString());
//                    followupConversation.setText(parent.getItemAtPosition(position).toString());
                } else {
                    followupDateLinear.setVisibility(View.VISIBLE);

//                    followupDateLinear.setVisibility(View.GONE);
//                    nextFollowupDate.setText(getYesterdayDateString());
//                    if (cCode.equals("IN") || cCode.equals("CB") || cCode.equals("NP")) {
//                        followupDateLinear.setVisibility(View.VISIBLE);
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

                        followupDate.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainHome.this, date, myCalendar
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

                        //todays date
                        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));
                        String todaysDate = dateFormat.format(date);

//                        if (todaysDate.equals(sdf.format(myCalendar.getTime())))
//                            Toast.makeText(MainHome.this, "Today's date cannot be selected.", Toast.LENGTH_LONG).show();
//                        else
                        nextFollowupDate.setText(sdf.format(myCalendar.getTime()));

                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainHome.this, date, myCalendar
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

    private boolean followupValid() {

//        if (!source.getSelectedItem().toString().equals("Others"))
//            SourceStr = source.getSelectedItem().toString().trim().replace(" ", "%20");
//        else
        SourceStr = addSource.getText().toString().trim().replace(" ", "%20");

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


        if (followupDateLinear.getVisibility() == View.VISIBLE)
            NextFollowupDate = nextFollowupDate.getText().toString().trim();
        else
            NextFollowupDate = getYesterdayDateString();


        FollowupMobileNo = followupMobileNo.getText().toString();


        //validation starts

        valid = true;

        if (project.getText().toString().isEmpty()) {
            project.setError("required");
            project.requestFocus();
            valid = false;
        }

        if (followupMobileNo.getText().toString().length() != 10) {
            followupMobileNo.setError("Please enter valid mobile no.");
            followupMobileNo.requestFocus();
            valid = false;
        }

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

//    @SuppressLint("SimpleDateFormat")
//    public void getCurrentTime() {
//        Date d = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//
//        String currentDateTimeString = sdf.format(d);
//    }

    private String getYesterdayDateString() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    //------------------------------------------------------------------------------App Update------------------------------------------------------------------------------

    private void addFollowup() {

        progressbar.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);

        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);
        String EmplyCode = pref.getString("userCode", null);

        /*

        Call<String> call = apiInterface.addFollowup(EmplyCode, "0", FollowupDate, ConversationMode, FollowupPurpose, FollowupConversation, CallStatus, NextFollowupDate, NextFollowupDate, CallCode, FollowupAltNo,
                FollowupLocation, FollowupName, "1", source.getSelectedItem().toString(), EmplyCode, clientType.getSelectedItem().toString(), FollowupMobileNo, ProjectId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful() && response.body() != null) {

                    String responseStr = response.body();

                    Log.v("response", response.body());


                    if (responseStr.contains("Record Saved Successfully")) {
                        FancyToast.makeText(MainHome.this, "Followup Added", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                        followAlert.cancel();

                    } else if (responseStr.contains("Mobile No allready exists")) {
                        FancyToast.makeText(MainHome.this, "Mobile no. already used in followup", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        progressbar.setVisibility(View.GONE);
                        saveBtn.setVisibility(View.VISIBLE);
                    } else if (responseStr.contains("error")) {
                        FancyToast.makeText(MainHome.this, "Something went wrong, Try again.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        Log.d("error", responseStr);
                        progressbar.setVisibility(View.GONE);
                        saveBtn.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                avi.hide();
//                FancyToast.makeText(Projects.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("error", t.toString());
                call.cancel();
            }
        });


         */

        //get random no.
//        final int min = 20;
//        final int max = 80;
//        final int random = new Random().nextInt((max - min) + 1) + min;
//
//        LeadCode = EmplyCode + random;


        String URL = Constant.AddFollowup
                + "Code=" + EmplyCode
                + "&LCode=" + "0"
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
                + "&Source=" + source.getSelectedItem().toString().replace(" ", "%20").trim()
                + "&Empcode=" + EmplyCode
                + "&CType=" + clientType.getSelectedItem().toString().replace(" ", "%20").trim()
                + "&MobileNo=" + FollowupMobileNo.replace(" ", "%20").trim()
                + "&NDate=" + Ndate
                + "&DDProject=" + ProjectId.replace(" ", "%20").trim();

        Log.v("URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("response", response);

                if (response.contains("Record Saved Successfully")) {
                    FancyToast.makeText(MainHome.this, "Followup Added", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                    followAlert.cancel();

                } else if (response.contains("Mobile No allready exists")) {
                    FancyToast.makeText(MainHome.this, "Mobile no. already used in followup", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressbar.setVisibility(View.GONE);
                    saveBtn.setVisibility(View.VISIBLE);
                } else if (response.contains("error")) {
                    FancyToast.makeText(MainHome.this, "Something went wrong, Try again.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    Log.d("error", response);
                    progressbar.setVisibility(View.GONE);
                    saveBtn.setVisibility(View.VISIBLE);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FancyToast.makeText(MainHome.this, "Something went wrong, Try again.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progressbar.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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

    @Override
    protected void onStart() {
        super.onStart();
        new HelperActivity(this).forceUpdate();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (mAppUpdateManager != null) {
//            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
//        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkNewAppVersionState();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        switch (requestCode) {
//
//            case REQ_CODE_VERSION_UPDATE:
//                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
//                    L.d("Update flow failed! Result code: " + resultCode);
//                    // If the update is cancelled or fails,
//                    // you can request to start the update again.
//                    unregisterInstallStateUpdListener();
//                }
//
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        unregisterInstallStateUpdListener();
//        super.onDestroy();
//    }
//
//
//    private void checkForAppUpdate() {
//        // Creates instance of the manager.
//        appUpdateManager = AppUpdateManagerFactory.create(AppCustom.getAppContext());
//
//        // Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Create a listener to track request state updates.
//        installStateUpdatedListener = new InstallStateUpdatedListener() {
//            @Override
//            public void onStateUpdate(InstallState installState) {
//                // Show module progress, log state, or install the update.
//                if (installState.installStatus() == InstallStatus.DOWNLOADED)
//                    // After the update is downloaded, show a notification
//                    // and request user confirmation to restart the app.
//                    popupSnackbarForCompleteUpdateAndUnregister();
//            }
//        };
//
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                // Request the update.
//                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//
//                    // Before starting an update, register a listener for updates.
//                    appUpdateManager.registerListener(installStateUpdatedListener);
//                    // Start an update.
//                    startAppUpdateFlexible(appUpdateInfo);
//                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ) {
//                    // Start an update.
//                    startAppUpdateImmediate(appUpdateInfo);
//                }
//            }
//        });
//    }
//
//    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
//        try {
//            appUpdateManager.startUpdateFlowForResult(
//                    appUpdateInfo,
//                    AppUpdateType.IMMEDIATE,
//                    // The current activity making the update request.
//                    this,
//                    // Include a request code to later monitor this update request.
//                    MainActivity.REQ_CODE_VERSION_UPDATE);
//        } catch (IntentSender.SendIntentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
//        try {
//            appUpdateManager.startUpdateFlowForResult(
//                    appUpdateInfo,
//                    AppUpdateType.FLEXIBLE,
//                    // The current activity making the update request.
//                    this,
//                    // Include a request code to later monitor this update request.
//                    MainActivity.REQ_CODE_VERSION_UPDATE);
//        } catch (IntentSender.SendIntentException e) {
//            e.printStackTrace();
//            unregisterInstallStateUpdListener();
//        }
//    }
//
//    /**
//     * Displays the snackbar notification and call to action.
//     * Needed only for Flexible app update
//     */
//    private void popupSnackbarForCompleteUpdateAndUnregister() {
//        Snackbar snackbar =
//                Snackbar.make(drawerLayout, getString(R.string.update_downloaded), Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction(R.string.restart, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                appUpdateManager.completeUpdate();
//            }
//        });
//        snackbar.setActionTextColor(getResources().getColor(R.color.action_color));
//        snackbar.show();
//
//        unregisterInstallStateUpdListener();
//    }
//
//    /**
//     * Checks that the update is not stalled during 'onResume()'.
//     * However, you should execute this check at all app entry points.
//     */
//    private void checkNewAppVersionState() {
//        appUpdateManager
//                .getAppUpdateInfo()
//                .addOnSuccessListener(
//                        appUpdateInfo -> {
//                            //FLEXIBLE:
//                            // If the update is downloaded but not installed,
//                            // notify the user to complete the update.
//                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//                                popupSnackbarForCompleteUpdateAndUnregister();
//                            }
//
//                            //IMMEDIATE:
//                            if (appUpdateInfo.updateAvailability()
//                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                                // If an in-app update is already running, resume the update.
//                                startAppUpdateImmediate(appUpdateInfo);
//                            }
//                        });
//
//    }
//
//    /**
//     * Needed only for FLEXIBLE update
//     */
//    private void unregisterInstallStateUpdListener() {
//        if (appUpdateManager != null && installStateUpdatedListener != null)
//            appUpdateManager.unregisterListener(installStateUpdatedListener);
//    }

}
