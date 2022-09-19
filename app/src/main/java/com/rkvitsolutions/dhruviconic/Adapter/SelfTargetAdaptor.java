package com.rkvitsolutions.dhruviconic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Model.TargetsModel;
import com.rkvitsolutions.dhruviconic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SelfTargetAdaptor extends RecyclerView.Adapter<SelfTargetAdaptor.MyViewHlder> {
    private Activity activity;
    private List<TargetsModel> list = new ArrayList<>();

    public SelfTargetAdaptor(Activity activity, List<TargetsModel> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setPostList(List<TargetsModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.self_target_list_layout, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);
        return my;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHlder myViewHlder, int i) {

        String fDate = list.get(i).getFDate().substring(0, 10);
//        String eDate = list.get(i).getEDate().substring(0, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Date date1, date2 = null;
        try {
//            String output = input.substring(0, 10);
            date1 = sdf.parse(fDate);
//            date2 = sdf.parse(eDate);
//            followupDate = date1.toString();
            myViewHlder.date.setText("Date" + date1.toString().substring(3, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myViewHlder.duration.setText("Duration: " + list.get(i).getEDate());

        if (list.get(i).getDialCall().isEmpty())
            myViewHlder.ttlData.setText("Data Collection: " + list.get(i).getTotalData());
        else {
            myViewHlder.ttlDial.setVisibility(View.VISIBLE);
            myViewHlder.ttlDial.setText("Dial: " + list.get(i).getDialCall());
            myViewHlder.ttlData.setVisibility(View.GONE);
            myViewHlder.ttlAns.setVisibility(View.VISIBLE);
            myViewHlder.ttlAns.setText("Answer: " + list.get(i).getAnswerCall());
        }
        myViewHlder.ttlSitevisit.setText("Site Visit: " + list.get(i).getSiteVisit());
        myViewHlder.ttlMeeting.setText("Meeting: " + list.get(i).getMeeting());
        myViewHlder.ttlLogin.setText("Login: " + list.get(i).getLogin());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHlder extends RecyclerView.ViewHolder {

        private TextView date, ttlData, ttlMeeting, ttlSitevisit, ttlLogin, duration, ttlDial, ttlAns;
        private CardView cardview;

        public MyViewHlder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            ttlData = itemView.findViewById(R.id.ttl_data);
            ttlMeeting = itemView.findViewById(R.id.ttl_meeting);
            ttlSitevisit = itemView.findViewById(R.id.ttl_sitevisit);
            ttlLogin = itemView.findViewById(R.id.ttl_login);
            ttlDial = itemView.findViewById(R.id.ttl_call);
            ttlAns = itemView.findViewById(R.id.ttl_answer);
            duration = itemView.findViewById(R.id.duration);

        }
    }
}