package com.tarikum3.tryocr.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tarikum3.tryocr.PictureUtils;
import com.tarikum3.tryocr.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Author CodeBoy722
 *
 * An adapter for populating RecyclerView with items representing folders that contain images
 */
public class pictureFolderAdapter extends RecyclerView.Adapter<pictureFolderAdapter.FolderHolder>{

    private  ArrayList<imageFolder> folders;

    private Context folderContx;
    private itemClickListener listenToClick;
      Bitmap imagebitmap ;
     Uri imageUrii;

   // final  Bitmap [] bitmap ;
    /**
     *
     * @param folders An ArrayList of String that represents paths to folders on the external storage that contain pictures
     * @param folderContx The Activity or fragment Context
     * @param listen interFace for communication between adapter and fragment or activity
     */
    public pictureFolderAdapter(ArrayList<imageFolder> folders, Context folderContx, itemClickListener listen) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;


    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.picture_folder_item, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final imageFolder folder = folders.get(position);
     //   imageUrii=Uri.parse("file://"+"/storage/emulated/0/download/Screen3.jpg");

        Glide.with(folderContx)
                .load(folder.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.folderPic);


        imageUrii=Uri.parse("file://"+folder.getFirstPic());
        /*
        try {
            imagebitmap = android.provider.MediaStore.Images.Media.getBitmap(folderContx.getContentResolver(),imageUrii);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


     //   Bitmap bmap= PictureUtils.getScaledDrawable((Activity)folderContx,folder.getFirstPic(),0,0);

       // holder.folderPic.setImageBitmap( bmap);

 String text = folder.getFolderName();
      //  String text = "("+folder.getNumberOfPics()+") "+folder.getFolderName();
        holder.folderName.setText(text);

        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenToClick.onPicClicked(folder.getPath(),folder.getFolderName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder{
       ImageView folderPic;
       TextView folderName;
       CardView folderCard;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
           folderPic = itemView.findViewById(R.id.folderPic);
           folderName = itemView.findViewById(R.id.folderName);
           folderCard = itemView.findViewById(R.id.folderCard);
        }
    }

}
