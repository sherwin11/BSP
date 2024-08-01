package com.bsp.bspregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AasrAddAdapter extends RecyclerView.Adapter<AasrAddAdapter.MyViewHolder> {
    private Context mContext;
    static ArrayList<AasrHolder> aasrHolder;
    View view;
    public AasrAddAdapter(Context mContext, ArrayList<AasrHolder> aasrHolder) {
        this.mContext = mContext;
        this.aasrHolder = aasrHolder;
    }

    public AasrAddAdapter(View view) {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.addfield, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return aasrHolder.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView foldername;
        LinearLayout linearLayout;
        RelativeLayout expandebleLayout;
        ImageView imgView;
        RecyclerView nestedRV;
        public MyViewHolder( View itemView) {
            super(itemView);

        }
    }

}
