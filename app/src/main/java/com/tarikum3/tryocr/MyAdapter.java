package com.tarikum3.tryocr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import static android.Manifest.permission.CAMERA;
import static com.tarikum3.tryocr.MainActivity.isInActionMode;
import static com.tarikum3.tryocr.MainActivity.isInActionModeSave;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Activity mActivity;
   private static ArrayList<Model> mDataset;
    private static ArrayList<String> ImagepathList ;
    private static ArrayList<String> OcrresultList ;
    private  static ArrayList<String> ScannedList ;



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView mImageView;
        ImageView mImageView2;
        TextView mTextView;
        View mView;

        ViewHolder(View v) {
            super(v);

            mTextView = (TextView) v.findViewById(R.id.tvRowItem);
            mImageView = (ImageView) v.findViewById(R.id.ivRowItem);
            mImageView2 = (ImageView) v.findViewById(R.id.ivRowIt3);
            mView = v;
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            int addpos=(ImagepathList.size()-1)- getAdapterPosition();
            if((isInActionMode||isInActionModeSave)) {
                ((MainActivity) mActivity).prepareSelection(addpos);
                notifyItemChanged(getAdapterPosition());
            }
             if(!(isInActionMode)) {
                 if(!(isInActionModeSave)) {
                    // ((MainActivity) mActivity).prepareToolbar(addpos);
                     //notifyItemChanged(getAdapterPosition());
                     notifyItemChanged(getAdapterPosition());
                 }
             }

            return true;
        }

        @Override
        public void onClick(View view) {
            int addpos=(ImagepathList.size()-1)- getAdapterPosition();
            if (ScannedList.get(addpos).contains("Scanned")) {
                if(!(isInActionMode)) {
                    if(!(isInActionModeSave)) {
                        ((MainActivity) mActivity).onResultClick(addpos, ImagepathList.get(addpos), OcrresultList.get(addpos));
                    }
                }
            }


            if((isInActionMode||isInActionModeSave)) {
                ((MainActivity) mActivity).prepareSelection(addpos);
                notifyItemChanged(getAdapterPosition());
            }
        }

    }

    MyAdapter(Activity activity,ArrayList<String> pathList) {
        this.mActivity = activity;
      // this.mDataset = myDataset;
        this.ImagepathList=pathList;
       OcrresultList =((MainActivity) mActivity).OcrresultList;
        ScannedList=((MainActivity) mActivity).ScannedList;


    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int posO) {
      int position= (ImagepathList.size()-1)-posO;

        holder.mView.setBackgroundResource(R.color.white);



        Uri imageUrii = Uri.parse(ImagepathList.get(position));
             final String pathh=imageUrii.getPath();

            Glide.with(mActivity.getBaseContext())
                    .load(pathh)
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.mImageView);


        if (ImagepathList.size() < 1) {

            holder.mView.setVisibility(View.GONE);

        }
        if (MainActivity.isInActionModeSave) {

            holder.mImageView2.setVisibility(View.VISIBLE);

        }
        if (!MainActivity.isInActionModeSave) {

            holder.mImageView2.setVisibility(View.GONE);

        }

        if (ImagepathList.size() > position && OcrresultList.size() == ImagepathList.size()) {


            int lengtharray = ((MainActivity) mActivity).ImageBitmaparray.length;


                if (ImagepathList.size() > 0) {
                    ((MainActivity) mActivity).diplaytext.setVisibility(View.GONE);


                }
                Bitmap bit = null;
                if (holder.mImageView.getDrawable() != null) {
                    ((MainActivity) mActivity).ImageBitmapArrayflag[position] = 0;
                }
                if (holder.mImageView.getDrawable() == null) {
                    ((MainActivity) mActivity).ImageBitmapArrayflag[position] = 1;


                    if (((MainActivity) mActivity).isInAction == false) {

                        // ((MainActivity) mActivity).isInAction = true;
                        // ((MainActivity) mActivity).createLoadeach();
                    }

                }
                if (holder.mImageView.getDrawable() == null && ((MainActivity) mActivity).ImageBitmaparray[position] != null) {

                }


                holder.mTextView.setText("Scanning......");
            holder.mTextView.setTextColor(((MainActivity) mActivity).getResources().getColor(R.color.bluetrans));

           // holder.mView.setBackgroundResource(R.color.grey_200);


                if (ScannedList.get(position).contains("Scanned")) {

                    holder.mTextView.setText(OcrresultList.get(position));
                    holder.mTextView.setTextColor(((MainActivity) mActivity).getResources().getColor(R.color.black));
                 //   holder.mTextView.setTextSize(10);
                    holder.mView.setBackgroundResource(R.color.white);
                }
                /// holder.mView.setVisibility(View.VISIBLE);

            if (isInActionMode) {
                // if (MainActivity.selectionList.contains(ImagepathList.get(position))) {
                if (MainActivity.selectionPosition.size() > 0) {
                    Integer pos = new Integer(position);
                    if (MainActivity.selectionPosition.contains(pos)) {
                        holder.mView.setBackgroundResource(R.color.grey_200);
                        // holder.mImageView.setImageResource(R.drawable.ic_check_circle_24dp);

                    }

                }
            }
            if (isInActionModeSave) {
              //  mImageView2
               //         = (ImageView) v.findViewById(R.id.ivRowIt3);
                holder.mImageView2.setImageResource(R.drawable.ic_check_box_black_24dp);
               // holder.mImageView2.setBackgroundColor(R.color.bluetrans);
                //holder.mImageView2.setVisibility(View.GONE);
               // holder.mImageView2.setVisibility(View.VISIBLE);
                if (MainActivity.selectionPosition.size() > 0) {
                    Integer pos = new Integer(position);
                    if (MainActivity.selectionPosition.contains(pos)) {
                       // holder.mView.setBackgroundResource(R.color.grey_200);

                        // holder.mImageView.setImageResource(R.drawable.ic_check_circle_24dp);
                        holder.mImageView2.setImageResource(R.drawable.ic_check_box_blue_24dp);
                    }

                }
            }

        }
    }


   @Override
public void onViewAttachedToWindow(ViewHolder holder){
     int pos=  holder.getAdapterPosition();
      // ((MainActivity) mActivity).ImageBitmapArrayflag[pos]=1;
   }


    @Override
    public void onViewDetachedFromWindow(ViewHolder holder){
        int pos=  holder.getAdapterPosition();
     //  ((MainActivity) mActivity).ImageBitmapArrayflag[pos]=0;

    }

    @Override
    public int getItemCount() {
       // return mDataset.size();
        return ImagepathList.size() ;
    }

    public Bitmap returnBit(Uri imageUrii) {
        Bitmap bitmap =null;
        try {

            bitmap =  android.provider.MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUrii);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap ;
    }
    public void removeData(ArrayList<Integer> list) {
       // for (String string : list) {
           // ImagepathList.remove(string);
       // }
        int [] temp= new int[ImagepathList.size()];

        for (int c=0; c<MainActivity.selectionPosition.size();c++) {
           //  ImagepathList.remove(MainActivity.selectionPosition.get(c).intValue());

            temp[MainActivity.selectionPosition.get(c).intValue()]=1;
        }
        int size=ImagepathList.size();
        ArrayList<String> tempImagepathList = new ArrayList<>();
        ArrayList<String> tempScanned = new ArrayList<>();
        ArrayList<String> tempOcr = new ArrayList<>();
        ArrayList<String> tempflag = new ArrayList<>();
        ArrayList<Bitmap> tempBitmap = new ArrayList<>();

        for (int c=0; c<size;c++) {
            if(temp[c]==0){
                tempImagepathList.add(ImagepathList.get(c)) ;
                tempOcr.add(OcrresultList.get(c)) ;
                tempScanned.add(ScannedList.get(c));

            }
        }
        ImagepathList.clear();
        OcrresultList.clear();
        ScannedList.clear();

        ImagepathList.addAll(tempImagepathList);
        OcrresultList.addAll(tempOcr);
        ScannedList.addAll(tempScanned);

        notifyDataSetChanged();
    }
    public String returnOcrString() {
        String result="";
        int [] temp= new int[ImagepathList.size()];

        for (int c=0; c<MainActivity.selectionPosition.size();c++) {
            //  ImagepathList.remove(MainActivity.selectionPosition.get(c).intValue());
           int index=MainActivity.selectionPosition.get(c).intValue();

            result=result+OcrresultList.get(index);
        }

        return result;
    }



    public void changeDataItem(int position, String string) {
        ImagepathList.set(position, string);
       notifyDataSetChanged();
   }

    // for edit
    public  ArrayList<String> getDataSet() {
        return ImagepathList;
    }

    public void scanEachBitmap(String path,int poss ) {
        Bitmap bit=null;
        bit = getScaledDrawable(mActivity, path, 0, 0);

    }
    public  Bitmap getScaledDrawable(Activity a, String path) {

        DisplayMetrics dismet=new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(dismet);
        float destWidth =1+ (dismet.widthPixels);
        float destHeight = 1+(dismet.heightPixels);
// Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        // return new BitmapDrawable(a.getResources(), bitmap);
        return bitmap;
    }

    public  Bitmap getScaledDrawable(Activity a, String path,int w,int h) {

        DisplayMetrics dismet=new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(dismet);
        float destWidth =1+ (dismet.widthPixels/5);
        float destHeight = 1+(dismet.heightPixels/5);
// Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        // return new BitmapDrawable(a.getResources(), bitmap);
        return bitmap;
    }
}