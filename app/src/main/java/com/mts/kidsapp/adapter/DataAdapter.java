package com.mts.kidsapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.mts.kidsapp.MusicService;
import com.mts.kidsapp.activities.PlayVideoActivity;
import com.mts.kidsapp.R;
import com.mts.kidsapp.model.DataModel;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    ArrayList<Object> list;

    public DataAdapter(Activity investActivity, ArrayList<Object> list) {
        this.context = investActivity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.data_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel dataModel = (DataModel) list.get(position);
        String icon = dataModel.getPoem_icon().replaceAll("^[\"']+|[\"']+$", "");

        Glide.with(context)
                .load(icon.replaceAll("^[\"']+|[\"']+$", ""))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivIcon);
        holder.ivIcon.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                MusicService.pauseMusic();
                Intent intent = new Intent(context,PlayVideoActivity.class);
                intent.putExtra("data", dataModel.getPoem_id());
                intent.putExtra("pos", position);
                context.startActivity(intent);
                mp.release();
            });
            mp.start();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        Button btnDwPl;

        private ViewHolder(View itemView) {
            super(itemView);
            ivIcon =  itemView.findViewById(R.id.ivIcon);
            btnDwPl =  itemView.findViewById(R.id.btnDwPl);
        }
    }
}
