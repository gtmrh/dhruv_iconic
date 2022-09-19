package com.rkvitsolutions.dhruviconic.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.rkvitsolutions.dhruviconic.Model.LeaveModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class SelfEvalution extends AppCompatActivity {

    private AppCompatRatingBar work_consis, communication, independent_work, team_work, target_achieved, honestly_towards, client_relations, sales_tech, punctuality_attendance, compliance, dress_code,
            team_feedback, floor_behaviour, mile_stone, integrity;
    private Button submit;
    private EditText remark;
    private boolean valid = false;
    private String UserCode, UserName, UserType, UserTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_evalution);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getSharedPreferences("UserData", MODE_PRIVATE);
        UserCode = pref.getString("userCode", null);
        UserName = pref.getString("username", null);
        UserType = pref.getString("userType", null);
        UserTeam = pref.getString("userTeam", null);

        work_consis = findViewById(R.id.work_consis);
        communication = findViewById(R.id.communication);
        independent_work = findViewById(R.id.independent_work);
        team_work = findViewById(R.id.team_work);
        honestly_towards = findViewById(R.id.honestly_towards);
        target_achieved = findViewById(R.id.target_achieved);
        client_relations = findViewById(R.id.client_relations);
        sales_tech = findViewById(R.id.sales_tech);

        punctuality_attendance = findViewById(R.id.punctuality_attendance);
        compliance = findViewById(R.id.compliance);
        dress_code = findViewById(R.id.dress_code);
        team_feedback = findViewById(R.id.team_feedback);
        floor_behaviour = findViewById(R.id.floor_behaviour);
        mile_stone = findViewById(R.id.mile_stone);
        integrity = findViewById(R.id.integrity);

        remark = findViewById(R.id.remark);

        submit = findViewById(R.id.save_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validData())
                    sendData();
            }
        });
    }

    private boolean validData() {

        valid = true;
        if (work_consis.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (communication.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (independent_work.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (team_work.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (honestly_towards.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (target_achieved.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (client_relations.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (sales_tech.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }



        if (punctuality_attendance.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (compliance.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (dress_code.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (team_feedback.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (floor_behaviour.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (mile_stone.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (integrity.getRating() == 0) {
            Toast.makeText(this, "Provide all ratings", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }


    private void sendData() {

        String date =  new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Submitting");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        Call<LeaveModel> call = apiInterface.applySelfEvl(UserCode, date, date.substring(5,7), date.substring(0,4), remark.getText().toString(),
                "NONE", "NONE","NONE",UserType, String.valueOf(work_consis.getRating()), String.valueOf(communication.getRating()), String.valueOf(independent_work.getRating()),
                String.valueOf(team_work.getRating()), String.valueOf(honestly_towards.getRating()), String.valueOf(target_achieved.getRating()), String.valueOf(client_relations.getRating()),
                String.valueOf(sales_tech.getRating()), String.valueOf(punctuality_attendance.getRating()), String.valueOf(compliance.getRating()), String.valueOf(dress_code.getRating()),
                String.valueOf(team_feedback.getRating()), String.valueOf(floor_behaviour.getRating()), String.valueOf(mile_stone.getRating()), String.valueOf(integrity.getRating()));

        call.enqueue(new Callback<LeaveModel>() {
            @Override
            public void onResponse(Call<LeaveModel> call, retrofit2.Response<LeaveModel> response) {

                progress.cancel();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("Record Saved Successfully")) {

                        FancyToast.makeText(SelfEvalution.this, "Submitted", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        finish();

                    } else {
                        Log.v("Error", response.errorBody().toString());
                        FancyToast.makeText(SelfEvalution.this, "Sorry! Something went wrong! Please try again later.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<LeaveModel> call, Throwable t) {
                progress.cancel();
                FancyToast.makeText(SelfEvalution.this, "Sorry! Something went wrong! Please try again later.", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                call.cancel();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}