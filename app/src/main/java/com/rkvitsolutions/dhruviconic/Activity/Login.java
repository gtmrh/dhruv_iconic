package com.rkvitsolutions.dhruviconic.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.irvingryan.VerifyCodeView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mukesh.OtpView;
import com.rkvitsolutions.dhruviconic.Model.LoginData;
import com.rkvitsolutions.dhruviconic.Model.LoginModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout txtLoginId, txtLoginPassword, txtSignupFName, txtSignupEmail,
            txtSignupPhoneNumber, txtSignupPassword;




    private TextInputEditText loginId, loginPassword, signupFName, signupLName, signupEmail,
            signupPhoneNumber, signupPassword;
    private ImageButton showPassword, hidePassword;

    private TextView loginBtn, signupBtn, close, login, signup;
    private ImageButton fbBtn, googleBtn;
    private LinearLayout loginLayout, signupLayout, btnLoginLayoutBg, btnSignupLayoutBg;
    private String fNameReg;
    private String LNameReg;
    private String userEmail;
    private String userReferralId;
    private String userMobile;
    private String userSocialProfilePic;
    private String userPasswordReg;
    private String userGender;
    private String googleUserName;
    private String userState;
    private String userCountry;
    private String userWhatsAppNo;
    private String userOccupation;
    private int isEmailActicated, isMobileActivated;

    private ApiInterface apiInterface;
    private AVLoadingIndicatorView avi;

    private String userPhoneNo;
    private String userPassword;
    private boolean valid;
    private String userId;
    private String fbId, status, data, token;
    private String gender;
    private String userPhoneOrEmailReg;
    private URL photoUrl;
    private String fbPhotoUrl;
    private String userUuId;
    private String message;
    //    private List<LoginData> list;
    private String loginType;
    private String LoginType;

    private OtpView otpView;
    private VerifyCodeView verifyCodeView;
    private TextView forgetPasswordTxt;
    private String RecoverMobileNo;
    private String OTP;
    private String NewPassword;
    private AlertDialog forgetPasswordAlert;
    private String userUId;
    private String userSocialId;
    private ProgressBar progressBar, progressBar1;
    private String userCode, username, userType, userTeam, bookingStatus;
    private List<LoginData> loginData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        setId();

    }

    private void setId() {

        progressBar = findViewById(R.id.progressbar);
        progressBar1 = findViewById(R.id.progressbar1);

        loginId = findViewById(R.id.login_id);
        loginPassword = findViewById(R.id.login_password);

        txtLoginId = findViewById(R.id.txt_input_id);
        txtLoginPassword = findViewById(R.id.txt_input_password);

        signupFName = findViewById(R.id.signup_fname);
        txtSignupFName = findViewById(R.id.txt_signup_fname);

        signupEmail = findViewById(R.id.signup_email);
        txtSignupEmail = findViewById(R.id.txt_signup_email);

        signupPhoneNumber = findViewById(R.id.signup_phone_number);
        txtSignupPhoneNumber = findViewById(R.id.txt_signup_phone_number);

        signupPassword = findViewById(R.id.signup_password);
        txtSignupPassword = findViewById(R.id.txt_signup_password);

        loginLayout = findViewById(R.id.loginLayout);
        signupLayout = findViewById(R.id.signupLayout);

        btnLoginLayoutBg = findViewById(R.id.btnLoginLayoutBg);
//        btnSignupLayoutBg = findViewById(R.id.btnSignupLayoutBg);

        btnLoginLayoutBg = findViewById(R.id.btnLoginLayoutBg);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);

        forgetPasswordTxt = findViewById(R.id.forget_password);
        forgetPasswordTxt.setOnClickListener(this);

//        signupDatalist = new ArrayList<>();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.loginBtn:
//
//                signupLayout.setVisibility(View.GONE);
//                loginLayout.setVisibility(View.VISIBLE);
//                btnLoginLayoutBg.setVisibility(View.VISIBLE);
//                btnSignupLayoutBg.setVisibility(View.GONE);
//
//                break;
//
//            case R.id.signupBtn:
//                signupLayout.setVisibility(View.VISIBLE);
//                loginLayout.setVisibility(View.GONE);
//
//                btnSignupLayoutBg.setVisibility(View.VISIBLE);
//                btnLoginLayoutBg.setVisibility(View.GONE);
//
//                break;

            case R.id.login:
                if (validLoginData())
                    UserLogin();

                break;
//
//            case R.id.signup:
//                if (validSignUpData())
//                    SignUp();
//                break;
//
//            case R.id.forget_password:
//                forgetPassword();
//                break;
        }
    }

    private boolean validLoginData() {
        userId = loginId.getText().toString();
        userPassword = loginPassword.getText().toString();

        valid = true;

        if (userId.isEmpty()) {
            txtLoginId.setError("Please enter your phone number or email id!");
            txtLoginId.requestFocus();
            valid = false;
        }
        if (userPassword.isEmpty()) {
            txtLoginPassword.setError("Please enter your password!");
            txtLoginPassword.requestFocus();
            valid = false;
        }
        return valid;
    }

    private void UserLogin() {
        progressBar.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);



        Call<LoginModel> call = apiInterface.login(userId, userPassword);
        call.enqueue(new Callback<LoginModel>() {

            @Override
            public void onResponse(Call<LoginModel> call, retrofit2.Response<LoginModel> response) {

                if (response.isSuccessful()) {

                    assert response.body() != null;
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        progressBar.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);

                        loginData = response.body().getData();
                        userCode = loginData.get(0).getUserCode();
                        username = loginData.get(0).getUsername();
                        userType = loginData.get(0).getType();
                        userTeam = loginData.get(0).getTeam();
                        bookingStatus = response.body().getStatus();
                        gender = response.body().getUserType();
                        Log.v("UserCode", userCode);

                        saveUserData();

                        startActivity(new Intent(Login.this, MainHome.class));

                    } else {
                        progressBar.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        FancyToast.makeText(Login.this, "Please check User Id and Password! \n or Contact the admin", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }

                } else {

                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        FancyToast.makeText(Login.this, "Server is slow.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        FancyToast.makeText(Login.this, "Please check your User Id and Password!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                FancyToast.makeText(Login.this, "Server is slow. Try again later.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });

    }

    public void saveUserData() {

        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("Status", "LoggedIn");
        editor.putString("userCode", userCode);
        editor.putString("username", username);
        editor.putString("userType", userType);
        editor.putString("userTeam", userTeam);
        editor.putString("userid", userId);
        editor.putString("password", userPassword);
        editor.putString("gender", gender);

        editor.putString("bookingStatus", bookingStatus);
        editor.apply();

    }

}
