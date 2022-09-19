package com.rkvitsolutions.dhruviconic.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Adapter.ProjectListAdaptor;
import com.rkvitsolutions.dhruviconic.Model.ProjectModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Projects extends AppCompatActivity {

    private RecyclerView projectRv;
    private List<ProjectModel> projectList = new ArrayList<>();
    private ProjectListAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectRv = findViewById(R.id.project_rv);

        getProjects();
        setProjects();
    }

    private void getProjects() {

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();

        Call<List<ProjectModel>> call = apiInterface.getProjects();
        call.enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, retrofit2.Response<List<ProjectModel>> response) {

                progress.cancel();

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        projectList = response.body();
                        adapter.setPostList(projectList);


                    } else
                        FancyToast.makeText(Projects.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                } else
                    FancyToast.makeText(Projects.this, "Something went wrong!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable t) {
                progress.cancel();
//                avi.hide();
                FancyToast.makeText(Projects.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setProjects() {

        adapter = new ProjectListAdaptor(Projects.this, projectList);
        projectRv.setAdapter(adapter);
        projectRv.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Projects.this, 1);
        projectRv.setLayoutManager(gridLayoutManager);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}