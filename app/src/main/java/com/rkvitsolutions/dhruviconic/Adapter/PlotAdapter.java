package com.rkvitsolutions.dhruviconic.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rkvitsolutions.dhruviconic.Activity.AbstractItem;
import com.rkvitsolutions.dhruviconic.Activity.OnSeatSelected;
import com.rkvitsolutions.dhruviconic.Model.PlotModel;
import com.rkvitsolutions.dhruviconic.R;

import java.util.List;

public class PlotAdapter extends SelectableAdapter<PlotAdapter.MyViewHlder> {

    private OnSeatSelected mOnSeatSelected;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Activity activity;
    private List<PlotModel> list;
    private List<AbstractItem> mItems;

    public PlotAdapter(Context context, List<AbstractItem> items, List<PlotModel> plotList) {
        mOnSeatSelected = (OnSeatSelected) context;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items;
        list = plotList;
    }
    public PlotAdapter(Activity activity, List<PlotModel> list) {
        mOnSeatSelected = (OnSeatSelected) activity;
        this.activity = activity;
        this.list = list;
    }

    public void setPostList(List<PlotModel> dataList) {
        list = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public MyViewHlder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_plot, viewGroup, false);
        MyViewHlder my = new MyViewHlder(view);
        return my;
    }

    @Override
    public void onBindViewHolder(MyViewHlder myViewHlder, final int i) {

        if (list.get(i).getStatus().equalsIgnoreCase("Empty")) {
            myViewHlder.available.setVisibility(View.VISIBLE);
            myViewHlder.booked.setVisibility(View.GONE);
            myViewHlder.availableTxt.setText(list.get(i).getPLName());

//            myViewHlder.available.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    myViewHlder.available.setBackgroundResource(R.drawable.plot_unavailable);
//
//                    toggleSelection(i);
//
//                    mOnSeatSelected.onSeatSelected(getSelectedItemCount());
//                }
//            });

            myViewHlder.available.setBackgroundResource(isSelected(i) ? R.drawable.plot_hold : R.drawable.plot_available);

        } else {
            myViewHlder.available.setVisibility(View.GONE);
            myViewHlder.booked.setVisibility(View.VISIBLE);
            myViewHlder.bookedTxt.setText(list.get(i).getPLName());

            myViewHlder.booked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(activity, "Not available", Toast.LENGTH_SHORT).show();

//                    toggleSelection(i);
//
//                    mOnSeatSelected.onSeatSelected(getSelectedItemCount());
                }
            });
        }

    }

    private static class EdgeViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgSeatSelected;
        ImageView imgSeat;


        public EdgeViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);

        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        return list.get(position).getStatus();
//    }

    private static class CenterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgSeatSelected;
        ImageView imgSeat;

        public CenterViewHolder(View itemView) {
            super(itemView);
            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);
        }

    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

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
