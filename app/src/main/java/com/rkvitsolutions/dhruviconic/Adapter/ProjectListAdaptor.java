package com.rkvitsolutions.dhruviconic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Activity.BookingPlot;
import com.rkvitsolutions.dhruviconic.Model.ProjectModel;
import com.rkvitsolutions.dhruviconic.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectListAdaptor extends RecyclerView.Adapter<ProjectListAdaptor.MyViewHlder> {
    private Activity activity;
    private List<ProjectModel> list = new ArrayList<>();

    public ProjectListAdaptor(Activity activity, List<ProjectModel> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setPostList(List<ProjectModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_list, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);
        return my;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHlder myViewHlder, int i) {

        myViewHlder.name.setText(list.get(i).getName());
        myViewHlder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BookingPlot.class);
                intent.putExtra("Id", list.get(i).getCode());
                intent.putExtra("Project Name", list.get(i).getName());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHlder extends RecyclerView.ViewHolder {

        private TextView name;
        private CardView cardview;

        public MyViewHlder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.project_name);
            cardview = itemView.findViewById(R.id.cardview);

        }
    }
}