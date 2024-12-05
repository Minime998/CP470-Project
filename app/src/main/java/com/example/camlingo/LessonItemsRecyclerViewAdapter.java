package com.example.camlingo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import model.GlobalUserCache;
import model.LessonItemModel;

public class LessonItemsRecyclerViewAdapter extends RecyclerView.Adapter<LessonItemsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<LessonItemModel> lessonItemModels;
    private boolean audioPlaying = false;
    private final String TAG = "RecyclerViewAdapter";
    private final String lessonCollection;
    private final String lessonName;
    private final String lessonNameModiefied;
    private final String lessonDocumentName;

    public LessonItemsRecyclerViewAdapter(Context context, ArrayList<LessonItemModel> lessonItemModels, String lessonNameModified, String lessonName, String lessonCollection, String lessonDocumentName){
        this.context = context;
        this.lessonItemModels = lessonItemModels;
        this.lessonName = lessonName;
        this.lessonNameModiefied = lessonNameModified;
        this.lessonCollection = lessonCollection;
        this.lessonDocumentName = lessonDocumentName;
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
        LessonItemModel lessonItemModel = lessonItemModels.get(position);

        // Set values for the text and labels
        holder.itemText.setText(lessonItemModel.getItemText());
        holder.itemPhrase.setText(lessonItemModel.getPhrase());
        holder.itemTextLabel.setText(lessonNameModiefied);

        // Set initial checkbox state based on the "complete" field
        holder.checkBox.setOnCheckedChangeListener(null); // Clear previous listener
        holder.checkBox.setChecked(lessonItemModel.isComplete());

        // Set up click listener for the play audio button
        holder.playAudioBtn.setOnClickListener(v -> {
            if (!audioPlaying) {
                audioPlaying = true;
                playAudio(lessonItemModel.getMedia());
            }
        });

        // Handle checkbox state changes
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update local model
            lessonItemModel.setComplete(isChecked);

            // Log progress
            Integer currentProgress = GlobalUserCache.getCurrentUser()
                    .getProgress()
                    .getOrDefault(lessonName, 0);
            Log.i(TAG, "Lesson: " + lessonName + ", progress: " + currentProgress);


            Log.i(TAG, "Checkbox was clicked.");

            // Update in Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance("camlingo");
            db.collection("lessons")
                    .document(lessonDocumentName.toLowerCase()) // Match document name structure
                    .collection(lessonCollection.toLowerCase())
                    .document(lessonItemModel.getItemId())
                    .update("complete", isChecked)
                    .addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "Lesson item updated successfully in Firestore.");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating lesson item in Firestore.", e);
                    });
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
        CheckBox checkBox;
        TextView itemText, itemPhrase, itemTextLabel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            playAudioBtn = itemView.findViewById(R.id.playAudioButton);
            itemText = itemView.findViewById(R.id.item_text);
            itemPhrase = itemView.findViewById(R.id.item_phrase);
            itemTextLabel = itemView.findViewById(R.id.item_text_label);
            checkBox = itemView.findViewById(R.id.lesson_item_check_box);

        }
    }
}
