package com.bsp.bspregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private Context mContext;
    static ArrayList<HistoryList> historyLists;
    View view;
    DBHandler dbHandler;

    public HistoryAdapter(Context mcontext, ArrayList<HistoryList> historyLists){
        this.mContext = mcontext;
        this.historyLists = historyLists;
    }
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.history_item,parent,false);
        dbHandler = new DBHandler(mContext, "forupload.db");
        return new HistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
        holder.ref.setText("Ref #: " + historyLists.get(position).getTempref());
        holder.amount.setText( historyLists.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
       return historyLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ref,amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ref = itemView.findViewById(R.id.refx);
            amount = itemView.findViewById(R.id.amountx);
        }
    }
}
