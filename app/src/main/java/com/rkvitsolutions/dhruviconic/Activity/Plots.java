package com.rkvitsolutions.dhruviconic.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rkvitsolutions.dhruviconic.Model.PlotModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Plots extends AppCompatActivity {

    private List<PlotModel> plotList = new ArrayList<>();
    private LinearLayout listPlot;
    private LinearLayout linearLayout;
    private static String blocks;
    private int COLUMNS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plots);

        listPlot = findViewById(R.id.list_plot);

        getPlots();
        setPlots();

    }

    private void getPlots() {

        String Id = getIntent().getStringExtra("Id");

        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();

        Call<List<PlotModel>> call = apiInterface.getPlots(Id);
        call.enqueue(new Callback<List<PlotModel>>() {
            @Override
            public void onResponse(Call<List<PlotModel>> call, retrofit2.Response<List<PlotModel>> response) {

                progress.cancel();

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        plotList = response.body();
//                        adapter.setPostList(projectList);

                        linearLayout = new LinearLayout(Plots.this);
//                        ArrayList<Seat> seats = new ArrayList<>();

                        for (int i = 0; i < plotList.size(); i++) {

                            if (i % COLUMNS == 0 || i % COLUMNS == 4){

                            }
                            }
                        }

                        listPlot.addView(linearLayout);


                    } else
                        FancyToast.makeText(Plots.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }

            @Override
            public void onFailure(Call<List<PlotModel>> call, Throwable t) {
                progress.cancel();
//                avi.hide();
                FancyToast.makeText(Plots.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setPlots() {

//        adapter = new ProjectListAdaptor(Projects.this, projectList);
//        projectRv.setAdapter(adapter);
//        projectRv.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(Projects.this, 2);
//        projectRv.setLayoutManager(gridLayoutManager);

    }

}