package com.rkvitsolutions.dhruviconic.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Adapter.PlotAdapter;
import com.rkvitsolutions.dhruviconic.Model.PlotModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BookingPlot extends AppCompatActivity implements OnSeatSelected {

    private static final int COLUMNS = 5;
    private TextView txtSeatSelected;
    private List<PlotModel> plotList = new ArrayList<>();
//    private PlotListAdaptor adaptor;

    private PlotAdapter adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_plot);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getIntent().getStringExtra("Project Name"));

        txtSeatSelected = (TextView) findViewById(R.id.txt_seat_selected);

        getPlots();
        setPlot();

    }


    private void getPlots() {

        plotList.clear();
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

                        adaptor.setPostList(plotList);

                    }


                } else
                    FancyToast.makeText(BookingPlot.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }

            @Override
            public void onFailure(Call<List<PlotModel>> call, Throwable t) {
                progress.cancel();
//                avi.hide();
                FancyToast.makeText(BookingPlot.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    private void setPlot() {

        GridLayoutManager manager = new GridLayoutManager(BookingPlot.this, 3);
        RecyclerView recyclerView = findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);

        adaptor = new PlotAdapter(BookingPlot.this, plotList);
        recyclerView.setAdapter(adaptor);

    }

    @Override
    public void onSeatSelected(int count) {

        txtSeatSelected.setText("Book " + count + " plot");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}