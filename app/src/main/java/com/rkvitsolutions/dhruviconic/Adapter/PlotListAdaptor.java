package com.rkvitsolutions.dhruviconic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Model.PlotModel;
import com.rkvitsolutions.dhruviconic.R;

import java.util.List;

public class PlotListAdaptor extends RecyclerView.Adapter<PlotListAdaptor.MyViewHlder> {
    private Activity activity;
    private List<PlotModel> list;

    public PlotListAdaptor(Activity activity, List<PlotModel> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setPostList(List<PlotModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_plot, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);
        return my;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHlder myViewHlder, int i) {

        if (list.get(i).getStatus().equalsIgnoreCase("Empty")) {
            myViewHlder.available.setVisibility(View.VISIBLE);
            myViewHlder.booked.setVisibility(View.GONE);
            myViewHlder.availableTxt.setText(list.get(i).getPLName());
        } else {
            myViewHlder.available.setVisibility(View.GONE);
            myViewHlder.booked.setVisibility(View.VISIBLE);
            myViewHlder.bookedTxt.setText(list.get(i).getPLName());
        }

//        myViewHlder.name.setText(list.get(i).getName());
//        myViewHlder.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, BookingPlot.class);
//                intent.putExtra("Id", list.get(i).getCode());
//                activity.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHlder extends RecyclerView.ViewHolder {

        private TextView name;
        private CardView cardview;
        private LinearLayout available, booked;
        private TextView availableTxt, bookedTxt;

        public MyViewHlder(@NonNull View itemView) {
            super(itemView);

            available = itemView.findViewById(R.id.plot_empty);
            booked = itemView.findViewById(R.id.plot_booked);

            availableTxt = itemView.findViewById(R.id.plot_name_empty);
            bookedTxt = itemView.findViewById(R.id.plot_name_booked);

        }
    }
}