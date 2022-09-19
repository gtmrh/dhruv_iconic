package com.rkvitsolutions.dhruviconic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Fragmnet.SalesEmpFragment;
import com.rkvitsolutions.dhruviconic.Model.LeaveListModel;
import com.rkvitsolutions.dhruviconic.R;

import java.util.List;

public class LeaveListAdaptor extends RecyclerView.Adapter<LeaveListAdaptor.MyViewHlder> {
    private Activity activity;
    private List<LeaveListModel> list;
    private AlertDialog followAlert;
    private ImageView close;
    private EditText followupAltNo, followupLocation, followupDate, city, followupPurpose, followupConversation, nextFollowupDate, followupName;
    private Spinner mode, callCode, callStatus;
    private Button saveBtn;
    private boolean valid;
    private String FollowupDate, ConversationMode, CallCode, FollowupPurpose, FollowupConversation, CallStatus, NextFollowupDate;
    private String LeadCode;
    private ProgressBar progressbar;
    private ScrollView mainScroll;
    private int itemPosition;
    private LinearLayout nameLinear, altNoLinear, locationLinear;
    private String FollowupName, FollowupAltNo, FollowupLocation;
    private SalesEmpFragment fragment;
    private LinearLayout followupDateLinear;
    private String cCode;

    public LeaveListAdaptor(Activity activity, List<LeaveListModel> list) {
        this.activity = activity;
        this.list = list;
        this.fragment = fragment;
    }

    public void setPostList(List<LeaveListModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    public void setFilter(List<LeaveListModel> FilteredDataList) {
        list = FilteredDataList;
        notifyDataSetChanged();

        Toast.makeText(activity, String.valueOf(list.size()) + " Followup", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public MyViewHlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_history_list, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);
        return my;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHlder myViewHlder, int i) {

        myViewHlder.date.setText("Leave Applied On: " + list.get(i).getDated());

        myViewHlder.from_date.setText("From Date:\n" + list.get(i).getFromDate());
        myViewHlder.to_date.setText("To Date:\n" + list.get(i).getToDate());
        myViewHlder.leave_type.setText("Leave Type:\n" + list.get(i).getLType());
        myViewHlder.status.setText("Current Status:\n" + list.get(i).getStatus());
        myViewHlder.reason.setText("Reason:\n" + list.get(i).getRemarks());

    }

    public int getItemCount() {
        return list.size();
    }

    class MyViewHlder extends RecyclerView.ViewHolder {

        private TextView date, to_date, from_date, reason, status, leave_type;


        public MyViewHlder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            from_date = itemView.findViewById(R.id.from_date);
            to_date = itemView.findViewById(R.id.to_date);
            status = itemView.findViewById(R.id.status);
            leave_type = itemView.findViewById(R.id.leave_type);
            reason = itemView.findViewById(R.id.reason);

        }
    }
}