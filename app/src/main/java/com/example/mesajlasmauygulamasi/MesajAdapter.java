package com.example.mesajlasmauygulamasi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MesajAdapter extends RecyclerView.Adapter<MesajAdapter.ViewHolder> {

    Context context;
    List<MesajModel> list;
    Activity activity;
    String userName;
    Boolean state;
    int view_send = 1, view_received = 2;


    public MesajAdapter(Context context, List<MesajModel> list, Activity activity, String userName) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName = userName;
        state = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //adapter layout unu burada oluştururuz
        View view;
        if (viewType == view_send) {
            view = LayoutInflater.from(context).inflate(R.layout.send_layout, parent, false);
            return new ViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.received_layout, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //burada atama işlemleri yapılır
        holder.textView.setText(list.get(position).getText().toString());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //burada gerekli tanımlamaları yapmamız lazım
        TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            if (state) {//state true ise
                textView = itemView.findViewById(R.id.sendTextView);
            } else {
                textView = itemView.findViewById(R.id.receivedTextView);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getFrom().equals(userName)) {
            state = true;
            //gönderen kişi bizim user name mimiz ise
            return view_send;
        } else {
            state = false;
            return view_received;
        }
    }
}
