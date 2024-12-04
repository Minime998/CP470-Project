package com.example.camlingo;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import model.LessonItemModel;

public class LessonItemsRecyclerViewAdapter extends RecyclerView.Adapter<LessonItemsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<LessonItemModel> lessonItemModels;
    private boolean audioPlaying = false;

    public LessonItemsRecyclerViewAdapter(Context context, ArrayList<LessonItemModel> lessonItemModels){
        this.context = context;
        this.lessonItemModels = lessonItemModels;
    }

    @NonNull
    @Override
    public LessonItemsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lesson_item_recycler_view_row, parent, false);
        return new LessonItemsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonItemsRecyclerViewAdapter.MyViewHolder holder, int position) {
        // assign values to the views

        holder.itemText.setText(lessonItemModels.get(position).getItemText());
        holder.itemPhrase.setText(lessonItemModels.get(position).getPhrase());
        holder.playAudioBtn.setOnClickListener(v -> {
            if (!audioPlaying){
                audioPlaying = true;
                playAudio(lessonItemModels.get(position).getMedia());
            }
        });
    }

    private void playAudio(String mediaUrl) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mediaUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException e){
            Log.e("AudioPlayer", "Error playing audio",e);
        }

        mediaPlayer.setOnCompletionListener(mp ->{
            mp.release();
            Log.i("AudioPlayer", "Audio playback completed");
            audioPlaying = false;
        });
    }

    @Override
    public int getItemCount() {
        // how many items we have total
        return lessonItemModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        // grab view from recycler_view_layout layout file

        ImageView playAudioBtn;
        TextView itemText, itemPhrase, itemTextLabel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            playAudioBtn = itemView.findViewById(R.id.playAudioButton);
            itemText = itemView.findViewById(R.id.item_text);
            itemPhrase = itemView.findViewById(R.id.item_phrase);
            itemTextLabel = itemView.findViewById(R.id.item_text_label);

        }
    }
}
