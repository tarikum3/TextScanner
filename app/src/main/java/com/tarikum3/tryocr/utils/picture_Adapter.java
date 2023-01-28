package com.tarikum3.tryocr.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tarikum3.tryocr.ImageDisplay;
import com.tarikum3.tryocr.PictureUtils;
import com.tarikum3.tryocr.R;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;


public class picture_Adapter extends RecyclerView.Adapter<picture_Adapter.ViewHolder> {

    private ArrayList<pictureFacer> pictureList;
    private Context pictureContx;
    private final itemClickListener picListerner;
    private Activity mActivity;
    private static ArrayList<pictureFacer> mDataset;
    static int[] nullholder;
   static Bitmap[] Bitmapholder;
    String [] path;
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView picture;
        public ImageButton mImageButton;
        public ImageButton mImageButton2;
        TextView mTextView;

        View mView;

        ViewHolder(View v) {
            super(v);
            picture = (ImageView) v.findViewById(R.id.imageitem);
            mImageButton = (ImageButton) v.findViewById(R.id.ivRowItem);
            mImageButton2 = (ImageButton) v.findViewById(R.id.ivRowItem22);
            mView = v;
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);
            mImageButton.setOnClickListener(this);
            mImageButton2.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
           // ((ImageDisplay) mActivity).prepareToolbar(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
          //  ((ImageDisplay) mActivity).prepareToolbar(getAdapterPosition());
         //  if (ImageDisplay.isInActionMode) {
                ((ImageDisplay) mActivity).prepareSelection(getAdapterPosition());
              notifyItemChanged(getAdapterPosition());
          //  }
        }



    }

    public picture_Adapter(ArrayList<pictureFacer> pictureList, Context pictureContx,itemClickListener picListerner) {
        this.pictureList = pictureList;
        this.mDataset=pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;
        this.mActivity = (Activity) pictureContx;
this.nullholder=new int [pictureList.size()];
       this.Bitmapholder=new Bitmap [pictureList.size()];
        path =new String [pictureList.size()];
        for	(int	i	=	0;	i	<	pictureList.size();	i++)	{
            final pictureFacer image = pictureList.get(i);
            path[i]=image.getPicturePath();

        }

    }

    @NonNull
    @Override
    public picture_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        return new picture_Adapter.ViewHolder(cell);
    }


    @Override
    public void onBindViewHolder(@NonNull final picture_Adapter.ViewHolder holder, final int position) {
       // ((ImageDisplay) mActivity).prepareToolbar(position);
        Bitmap   imagebitmap= null;
        final pictureFacer image = pictureList.get(position);
        String iPath="file://"+image.getPicturePath();

        Glide.with(pictureContx)
                .load(image.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);



        if(nullholder[position]==0){

          // holder.picture.setImageBitmap(PictureUtils.getScaledDrawable(mActivity,image.getPicturePath(),0,0));

        }
        if(nullholder[position]==1){

           // holder.picture.setImageBitmap( Bitmapholder[position]);
        }
        if(position==0){
         //  new	DownloadFilesTask(0).execute(path);

        }
       // setTransitionName(holder.picture, String.valueOf(position) + "_image");

      //  holder.picture.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
               // picListerner.onPicClicked(holder,position, pictureList);
               // ((ImageDisplay) mActivity).prepareToolbar(position);
           // }
       // });

        holder.mImageButton.setImageResource(R.drawable.ic_circle_24dp);

      //  if (ImageDisplay.isInActionMode) {
        /*
           if (ImageDisplay.selectionList.contains(mDataset.get(position))) {
               // holder.mView.setBackgroundResource(R.color.grey_200);
               holder.mImageButton.setImageResource(R.drawable.ic_check_circletwo_24dp);
           }
           */
     //   }

        holder.mImageButton.setVisibility(View.INVISIBLE);
        holder.mImageButton2.setVisibility(View.INVISIBLE);
        if (ImageDisplay.selectholder[position]==1) {
            ImageDisplay.selectedpathholder[position]=iPath;
            // holder.mView.setBackgroundResource(R.color.grey_200);
            //holder.mView.setForegroundResource(R.color.bluetrans);
           // holder.mView.setAlpha((float) 0.0);


           // holder.mView.setBackgroundResource(R.color.colorPrimary);

            holder.mImageButton.setImageResource(R.drawable.ic_check_circletwo_24dp);
            holder.mImageButton2.setVisibility(View.VISIBLE);
            holder.mImageButton.setVisibility(View.INVISIBLE);
        }
        if (ImageDisplay.selectholder[position]==0) {
            ImageDisplay.selectedpathholder[position]=null;
          //  holder.mView.setAlpha((float) 1.0);
        }
   }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
    	class	DownloadFilesTask	extends AsyncTask<String,	Integer,	Bitmap> {

int p=0;
            public DownloadFilesTask(int position) {
                p=position;
            }

            protected	Bitmap doInBackground(String...	urls)	{
                for	(int	i	=	0;	i	<	pictureList.size();	i++)	{
                    final pictureFacer image = pictureList.get(i);
                    path[i]=image.getPicturePath();
                    Bitmapholder[i]=PictureUtils.getScaledDrawable(mActivity,path[i],0,0);
                    nullholder[i]=1;
                }
            int	totalSize	=	0;

            Bitmap  map = PictureUtils.getScaledDrawable(mActivity,urls[0],0,0);

            return	map;
        }
        protected	void	onProgressUpdate(Integer...	progress)	{
            //setProgressPercent(progress[0]);
            //notifyItemChanged(p);
          //  nullholder[p]=0;
        }
        protected	void	onPostExecute(Bitmap	bitm)	{
           // showDialog("Downloaded	"	+	result	+	"	bytes");
           // notifyItemChanged(p);
          //  nullholder[p]=1;
           // Bitmapholder[p]=bitm;
        }
    }
}
