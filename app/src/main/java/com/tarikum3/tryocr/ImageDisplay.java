package com.tarikum3.tryocr;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tarikum3.tryocr.fragments.pictureBrowserFragment;
import com.tarikum3.tryocr.utils.MarginDecoration;
import com.tarikum3.tryocr.utils.PicHolder;
import com.tarikum3.tryocr.utils.itemClickListener;
import com.tarikum3.tryocr.utils.pictureFacer;
import com.tarikum3.tryocr.utils.picture_Adapter;
import java.util.ArrayList;




public class ImageDisplay extends AppCompatActivity implements itemClickListener {
    ArrayList<Model> mData = new ArrayList<>();
    public static final String PATH_SELECTED =
            "com.tarikum3.tryocr.extra.pathselected";
    public static boolean isInActionMode = false;
    public static ArrayList<pictureFacer> selectionList = new ArrayList<>();
    public static int [] selectholder;
    public static String [] selectedpathholder;
    private RecyclerView.Adapter mAdapter;
    RecyclerView imageRecycler;
    ArrayList<pictureFacer> allpictures;
    ProgressBar load;
    String foldePath;
    TextView folderName;
    ImageView photoitem;
    public static int photoitemh;
    public static int photoitemw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        photoitem=(ImageView)findViewById(R.id.imageitem);

        folderName = findViewById(R.id.foldername);
        folderName.setText(getIntent().getStringExtra("folderName"));

        foldePath =  getIntent().getStringExtra("folderPath");
        allpictures = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler2);
        imageRecycler.addItemDecoration(new MarginDecoration(this));
        imageRecycler.hasFixedSize();
        load = findViewById(R.id.loader);


        if(allpictures.isEmpty()){
            load.setVisibility(View.VISIBLE);
            allpictures = getAllImagesByFolder(foldePath);
            mAdapter =new picture_Adapter(allpictures,ImageDisplay.this,this);
            //   imageRecycler.setAdapter(new picture_Adapter(allpictures,ImageDisplay.this,this));
            imageRecycler.setAdapter(mAdapter);
            load.setVisibility(View.GONE);
        }else{

        }
        selectholder= new int[allpictures.size()];
        selectedpathholder= new String[allpictures.size()];
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){


      //  photoitemh=photoitem.getHeight();
      //   photoitemw=photoitem.getWidth();



    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {
        pictureBrowserFragment browser = pictureBrowserFragment.newInstance(pics,position,ImageDisplay.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //browser.setEnterTransition(new Slide());
            //browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }

        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position+"picture")
                .add(R.id.displayContainer, browser)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onPicClicked(String pictureFolderPath,String folderName) {

    }


    public ArrayList<pictureFacer> getAllImagesByFolder(String path){
        ArrayList<pictureFacer> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ImageDisplay.this.getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);
        try {
            cursor.moveToFirst();
            do{
                pictureFacer pic = new pictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<pictureFacer> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }
    public void prepareToolbar(int position) {

        // prepare action mode
        // toolbar.getMenu().clear();
        //toolbar.inflateMenu(R.menu.menu_action_mode);
        isInActionMode = true;
        folderName.setText("Select"+position);
        // mAdapter.notifyDataSetChanged();

    }


    public void prepareSelection(int position) {



        if (selectholder[position]!=1) {
            selectholder[position]=1;
            selectionList.add(allpictures.get(position));
        } else {
            selectholder[position]=0;
            selectionList.remove(allpictures.get(position));
        }
        updateViewCounter();
    }


    private void updateViewCounter() {
        int counter = selectionList.size();
        if (counter == 1) {
            // edit
            //  toolbar.getMenu().getItem(0).setVisible(true);
        } else {
            //  toolbar.getMenu().getItem(0).setVisible(false);
        }

        // toolbar.setTitle(counter + " item(s) selected");
    }
    public void clearActionMode() {
        isInActionMode = false;
        // toolbar.getMenu().clear();
        //  toolbar.inflateMenu(R.menu.menu_main);
        // if (getSupportActionBar() != null) {
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //  }
        //  toolbar.setTitle(R.string.app_name);
        selectionList.clear();
    }

    @Override
    public void onBackPressed() {
        if (isInActionMode) {
            clearActionMode();
            //    mAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }
    public void returnReply(View view) {
        // Get the reply message from the edit text.
        String[] path = selectedpathholder;

        // Create a new intent for the reply, add the reply message to it as an extra,
        // set the intent result, and close the activity.
        Intent replyIntent = new Intent();
        replyIntent.putExtra(PATH_SELECTED, path);
        setResult(RESULT_OK, replyIntent);



        finish();
    }
}
