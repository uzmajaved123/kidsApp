package com.mts.kidsapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mts.kidsapp.MusicService;
import com.mts.kidsapp.R;
import com.mts.kidsapp.activities.CategoryVideosActivity;
import com.mts.kidsapp.activities.PlayVideoActivity;
import com.mts.kidsapp.model.CategoryModel;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Typeface typeface;
    Context context;
    ArrayList<Object> list;

    public CategoryAdapter(Activity investActivity, ArrayList<Object> list) {
        this.context = investActivity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.category_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/appfont.ttf");
        CategoryModel dataModel = (CategoryModel) list.get(position);
        String title = dataModel.getpoem_title().replaceAll("^[\"']+|[\"']+$", "");
        holder.tvTitle.setText(title);
        holder.tvTitle.setTypeface(typeface);
        holder.layoutSubCatItem.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(context, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                MusicService.pauseMusic();
                Intent intent = new Intent(context, CategoryVideosActivity.class);
                intent.putExtra("id", dataModel.getPoem_id());
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

        TextView tvTitle;
        RelativeLayout layoutSubCatItem;

        private ViewHolder(View itemView) {
            super(itemView);
            tvTitle =  itemView.findViewById(R.id.tvTitle);
            layoutSubCatItem =  itemView.findViewById(R.id.layoutSubCatItem);
        }
    }
}

