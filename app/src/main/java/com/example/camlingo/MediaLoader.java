package com.example.camlingo;
import android.util.Log;
import android.widget.ImageView;
import android.media.MediaPlayer;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.Glide;

public class MediaLoader {
    public void loadMedia(String mediaUrl, ImageView imageView){

        // Use file extension to determine if its audio or image file
        String fileExt = MimeTypeMap.getFileExtensionFromUrl(mediaUrl);

        if (fileExt != null){
            if (fileExt.equals("png")){
                // load image using Glide
                Glide.with(imageView.getContext())
                        .load(mediaUrl)
                        .into(imageView);
            }else if (fileExt.equals("mp3") || fileExt.equals("wav")){
                // Load audio
                Log.i("MediaLoader", "testing audio");
            }
        }
    }


}
