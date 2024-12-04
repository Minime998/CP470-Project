package com.example.camlingo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import model.LessonModel;

public class LessonsRecyclerViewAdapter extends RecyclerView.Adapter<LessonsRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<LessonModel> lessonModels;

    public LessonsRecyclerViewAdapter(Context context, ArrayList<LessonModel> lessonModels) {
        this.context = context;
        this.lessonModels = lessonModels;
    }

    @NonNull
    @Override
    public LessonsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lessons_row_layout, parent, false); // Fix this to reference a layout, not an ID
        return new LessonsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsRecyclerViewAdapter.MyViewHolder holder, int position) {
        LessonModel lesson = lessonModels.get(position);
        holder.lessonName.setText(lessonModels.get(position).getLessonName());
        String lessonNumText = "Lesson " + lessonModels.get(position).getLessonNumber();
        holder.lessonNum.setText(lessonNumText);
        Glide.with(context).load(lessonModels.get(position).getLessonMediaUrl())
                .into(holder.lessonImage);

    }

    @Override
    public int getItemCount() {
        return lessonModels.size(); // Return the size of the lessonModels list
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lessonNum;
        TextView lessonName;
        TextView lessonProgress;
        ImageView lessonImage;
        androidx.cardview.widget.CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonNum = itemView.findViewById(R.id.lesson_num);
            lessonName = itemView.findViewById(R.id.lesson_type);
            lessonProgress = itemView.findViewById(R.id.lesson_progress);
            lessonImage = itemView.findViewById(R.id.lesson_icon);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
