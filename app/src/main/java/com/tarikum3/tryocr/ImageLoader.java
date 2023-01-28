/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tarikum3.tryocr;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import androidx.loader.content.AsyncTaskLoader;

/**
 * AsyncTaskLoader implementation that opens a network connection and
 * query's the Book Service API.
 */
public class ImageLoader extends AsyncTaskLoader<String> {

    // Variable that stores the search string.
    private String mQueryString="";
    private Activity mActivity;
    private String path="";
    private int pos=0;
    private int indicate=0;
    // Constructor providing a reference to the search term.
    public ImageLoader(Context context) {
        super(context);

        mActivity= (Activity) context;
    }
    public ImageLoader(Context context,String pth,int poss) {
        super(context);
        path=pth;
        pos=poss;
        mActivity= (Activity) context;

        indicate=1;

    }
    /**
     * This method is invoked by the LoaderManager whenever the loader is started
     */
    @Override
    protected void onStartLoading() {
        forceLoad(); // Starts the loadInBackground method
    }

    /**
     * Connects to the network and makes the Books API request on a background thread.
     *
     * @return Returns the raw JSON response from the API call.
     */
    @Override
    public String loadInBackground() {
        if(indicate==0) {
          ((MainActivity) mActivity).scanEach();
        }
        if(indicate==1) {
         //   ((MainActivity) mActivity).scanEachBitmap( path, pos );
        }
        return mQueryString;
    }
    public  Bitmap getScaledDrawable(Activity a, String path, int w, int h) {

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
}

