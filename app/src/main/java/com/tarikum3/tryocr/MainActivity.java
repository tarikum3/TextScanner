package com.tarikum3.tryocr;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;


import android.graphics.Matrix;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;


import java.util.Arrays;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
//import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String>{
    public  ArrayList<String> ImagepathList = new ArrayList<>();
    public  ArrayList<String> OcrresultList = new ArrayList<>();
    public   ArrayList<String> ScannedList = new ArrayList<>();

    private ReviewManager reviewManager;

    private static final int STORAGE_CODE = 1000;

   CropImageView cropImageView2;

    protected static Bitmap imageCropped ;
    protected static String pathCropped="";
    public static Bitmap [] ImageBitmaparray ;
    public static int [] ImageBitmapArrayflag ;
    protected TextView diplaytext;
    protected Button SaveB;
    protected Button CancelB;
    private String imagepaths="Imagepath.json";
    private String ocrresult="Ocrresult.json";

    private String scanned="Scanned.json";
    SavePath saveimagepath;
    SavePath saveocrresult;
    SavePath savescanned;
    ImageLoader imageloader;

    private PictureContent pictureContent;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Model> mData = new ArrayList<>();
    // private ClipboardManager cm=null;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7;
    public static final int CROP_REQUEST = 6 ;
    private static final int PICK_IMAGE = 1 ;
    public static final int PATH_REQUEST2 = 5;
    public static final int EDIT_REQUEST = 4;
    private static final int Image_Capture_Code = 2 ;
    private static final int PIC_CROP = 3;
    public static final String EXTRA_MESSAGE = "com.tarikum3.tryocr.extra.MESSAGEpath";
    public static final String EXTRA_MESSAGE1 = "com.tarikum3.tryocr.extra.MESSAGEresult";
    public static final String EXTRA_MESSAGE22 = "com.tarikum3.tryocr.extra.MESSAGEpos";
    private Toolbar toolbar;
    // action mode
    static Bitmap bitmaptempcam=null;
   static Uri  imageUricam=null;
   static String fnameCam=null;
    public static boolean isPurchased = true;
    public static boolean isInAction = false;
    public static boolean isInActionMode = false;
    public static boolean isInActionModeSave = false;
   // public static ArrayList<String> selectionList = new ArrayList<>();
   public static ArrayList<Integer> selectionPosition = new ArrayList<>();
    private static List<PictureItem> mValues;
    FloatingActionButton fabgal;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        cropImageView2 = (CropImageView)findViewById(R.id.cropImageView2);
        Bitmap bitmap =null;
        //cropImageView2.setImageBitmap(bitmap);


        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pictureContent.loadSavedImages(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        mValues=pictureContent.ITEMS;
        // recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


         diplaytext= (TextView) findViewById(R.id.Diplaytext);
         SaveB= (Button) findViewById(R.id.SaveButton);
          CancelB= (Button) findViewById(R.id.CancelButton);

        // create data
        String[] array = getResources().getStringArray(R.array.array_text);
        for (String text : array) {
            Model model = new Model(R.mipmap.ic_launcher, text);
            mData.add(model);
        }

        saveimagepath= new SavePath(getApplicationContext() ,imagepaths);
        try {
            ImagepathList =saveimagepath.loadCrimes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveocrresult= new SavePath(getApplicationContext() ,ocrresult);
        try {
            OcrresultList =saveocrresult.loadCrimes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        savescanned= new SavePath(getApplicationContext() ,scanned);
        try {
            ScannedList =savescanned.loadCrimes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // adapter
         ImageBitmaparray = new Bitmap[ImagepathList.size()];
        ImageBitmapArrayflag = new int[ImagepathList.size()];
        mAdapter = new MyAdapter(this,ImagepathList);
        mRecyclerView.setAdapter(mAdapter);

         fabgal = (FloatingActionButton) findViewById(R.id.fabgal);
        fabgal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MainActivity.this,MainActivity2.class);

               // startActivity(move);
                startActivityForResult(move, PATH_REQUEST2);
               // Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
               // startActivityForResult(gallery, PICK_IMAGE);
            }
        });
        if(getSupportLoaderManager().getLoader(0)!=null){
            // getSupportLoaderManager().destroyLoader(0);
            //  if(getSupportLoaderManager().getLoader(0).isStarted()!=true) {
            getSupportLoaderManager().initLoader(0, null, this);
            //  }
        }
        if(getSupportLoaderManager().getLoader(0)==null){

            getSupportLoaderManager().restartLoader(0, null, this);
        }

    }

    public void onWindowFocusChanged(boolean hasFocus){


      //  scanEach();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void prepareToolbar(int position) {
/*
        // prepare action mode
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);
        isInActionMode = true;
      //  mAdapter.notifyDataSetChanged();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
*/
        prepareSelection(position);
    }








    public void prepareSelection(int position) {
       // position= (OcrresultList.size()-1)-position;
        Integer pos =new Integer(position );
      //  if (!selectionList.contains(ImagepathList.get(position))) {
        if (!selectionPosition.contains(pos)) {
            selectionPosition.add(pos);
           // selectionList.add(ImagepathList.get(position));
        } else {
           // selectionList.remove(ImagepathList.get(position));

            selectionPosition.remove(pos);
        }
        if (selectionPosition.size()<1) {
            SaveB.setTextColor(getResources().getColor(R.color.bluetrans));
        }
        if (selectionPosition.size()>0) {
            SaveB.setTextColor(getResources().getColor(R.color.colorPrimary));

        }
        if(isInActionModeSave){
       // toolbar.getMenu().findItem(R.id.gallery).setTitle("Selected ("+selectionPosition.size()+")");
            toolbar.setTitle("Selected ("+selectionPosition.size()+")");
        }
        updateViewCounter();
    }


    private void updateViewCounter() {
        int counter = selectionPosition.size();
       // ((MyAdapter) mAdapter).notifyDataSetChanged();
        if (counter == 1) {
            // edit
         //   toolbar.getMenu().getItem(0).setVisible(true);
        } else {
          //  toolbar.getMenu().getItem(0).setVisible(false);
        }

      //  toolbar.setTitle(counter + " item(s) selected");
        if (counter<1) {
            if (isInActionMode) {
                clearActionMode();
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveOcr || item.getItemId() == R.id.item_delete || item.getItemId() == R.id.item_share) {



            if (ImagepathList.size() > 0) {
                boolean action = isInActionModeSave;
                if (action == false) {
                    isInActionModeSave = true;
                    item.setIcon(R.drawable.ic_save_black_24dp);
                    fabgal.hide();
                    if (item.getItemId() == R.id.item_share) {
                        SaveB.setText("Share");
                    }
                    if (item.getItemId() == R.id.saveOcr) {
                        SaveB.setText("Save");
                    }
                    if (item.getItemId() == R.id.item_delete) {
                        SaveB.setText("Delete");
                    }

                    if (item.getItemId() != R.id.item_share) {
                        toolbar.getMenu().findItem(R.id.item_share).setVisible(false);
                    }
                    if (item.getItemId() != R.id.saveOcr) {
                        toolbar.getMenu().findItem(R.id.saveOcr).setVisible(false);
                    }
                    if (item.getItemId() != R.id.item_delete) {
                        toolbar.getMenu().findItem(R.id.item_delete).setVisible(false);
                    }
                    SaveB.setVisibility(View.VISIBLE);
                    CancelB.setVisibility(View.VISIBLE);
                    SaveB.setTextColor(getResources().getColor(R.color.bluetrans));
                    toolbar.getMenu().findItem(R.id.camera).setVisible(false);
                    toolbar.getMenu().findItem(R.id.gallery).setVisible(true);

                    toolbar.setTitle("Selected (" + selectionPosition.size() + ")");
                    toolbar.getMenu().findItem(R.id.gallery).setIcon(R.drawable.ic_done_all_black_24dp);
                    // toolbar.getMenu().findItem(R.id.gallery).setTitle("Selected ("+selectionPosition.size()+")");
                    item.setVisible(false);
                }
                if (action == true) {
                    isInActionModeSave = false;
                    fabgal.show();
                    SaveB.setVisibility(View.GONE);
                    CancelB.setVisibility(View.GONE);
                    selectionPosition.clear();
                }
                mAdapter.notifyDataSetChanged();
            }

    }
        if (item.getItemId() == R.id.item_save) {

            if (selectionPosition.size() == -1&&selectionPosition.size() == 0) {
                final EditText editText = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("Edit")
                        .setView(editText)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               // String model = selectionList.get(0);
                                String model = ImagepathList.get(selectionPosition.get(0));

                                model=editText.getText().toString();
                                isInActionMode = false;

                                ((MyAdapter) mAdapter).changeDataItem(getCheckedLastPosition(), model);
                                clearActionMode();
                            }
                        })
                        .create().show();

                       }
            showDialog();
            //shareText();
            isInActionMode = false;

           // clearActionMode();
        } else if (item.getItemId() == R.id.item_delete) {
           // isInActionMode = false;
            //((MyAdapter) mAdapter).removeData(selectionPosition);
           // clearActionMode();
        } else if (item.getItemId() == R.id.item_share) {
           // isInActionMode = false;
           // shareText();
          //  clearActionMode();
        } else if (item.getItemId() == android.R.id.home) {
            clearActionMode();
            mAdapter.notifyDataSetChanged();
            isInActionModeSave = false;
            return true;
        }
     else if (item.getItemId() == R.id.camera) {
        Intent camera = new Intent(MediaStore. ACTION_IMAGE_CAPTURE );
        startActivityForResult(camera, Image_Capture_Code );
        return true;

        } else if (item.getItemId() == R.id.gallery) {
         int sizee=selectionPosition.size();
         if(ImagepathList.size()!=sizee) {
             selectionPosition.clear();
             for (int c = 0; c < ImagepathList.size(); c++) {
                // if (!selectionPosition.contains(c)) {
                     selectionPosition.add(c);
                // }
             }
         }
            if(ImagepathList.size()==sizee) {
                selectionPosition.clear();
            }
                // selectionList.add(ImagepathList.get(position));
         /*
           // Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            Intent gallery = new Intent();
            gallery.setType("image/*");
           gallery.setAction(Intent.ACTION_GET_CONTENT);
           // gallery.setAction(Intent.ACTION_PICK);
           // startActivityForResult(gallery, PICK_IMAGE);
            startActivityForResult(Intent.createChooser(gallery,"Select Image"), PICK_IMAGE);
            */

            mAdapter.notifyDataSetChanged();
            toolbar.setTitle("Selected ("+selectionPosition.size()+")");
            if (selectionPosition.size()<1) {
                SaveB.setTextColor(getResources().getColor(R.color.bluetrans));
            }
            if (selectionPosition.size()>0) {
                SaveB.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        }
        return true;
    }



    public void clearActionMode() {
        isInActionMode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        toolbar.setTitle(R.string.app_name);
       // selectionList.clear();
        selectionPosition.clear();
        ((MyAdapter) mAdapter).notifyDataSetChanged();

 isInActionModeSave = false;
        SaveB.setVisibility(View.GONE);
        CancelB.setVisibility(View.GONE);
        fabgal.show();
        //clearActionMode();
    }

    @Override
    public void onBackPressed() {
        if (!isInActionModeSave) {

            if (!isInActionMode) {
                super.onBackPressed();
            }
        }
        if (isInActionModeSave) {
            isInActionModeSave = false;
            selectionPosition.clear();
            mAdapter.notifyDataSetChanged();
            SaveB.setVisibility(View.GONE);
            CancelB.setVisibility(View.GONE);
            clearActionMode();
            fabgal.show();

        }
        if (isInActionMode) {
            clearActionMode();
            isInActionModeSave=false;
            ((MyAdapter) mAdapter).notifyDataSetChanged();
        } else {
          //  super.onBackPressed();
        }
    }

    public int getCheckedLastPosition() {
        ArrayList<String> dataSet = ((MyAdapter) mAdapter).getDataSet();
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).equals(ImagepathList.get(selectionPosition.get(0)))) {
                return i;
            }
        }
        return 0;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code ) {
            if (resultCode == RESULT_OK ) {
              Uri  imageUri = data.getData();
                String pss="";
               pss= getRealPathFromURI(imageUri);
                pathCropped=pss;
               // imageUricam=imageUri;
         //  String ipath="file://"+imageUri.getPath();
                launchSecondActivity(imageUri.toString());



            } else if (resultCode == RESULT_CANCELED ) {
                Toast.makeText( this , "Cancelled" , Toast. LENGTH_LONG ).show();
            }
        }
        // Test for the right intent reply
        if (requestCode == PATH_REQUEST2) {
            // Test to make sure the intent reply result was good.
            if (resultCode == RESULT_OK) {
                String [] path = data.getStringArrayExtra(MainActivity2.PATH_SELECTED2);
                for(int c=0;c<path.length;c++){

                    if(path[c]!=null){
                        ImagepathList.add(path[c]);
                       OcrresultList.add("t");
                        ScannedList.add("t");

                    }

                }
                ImageBitmaparray = new Bitmap[ImagepathList.size()];
                ImageBitmapArrayflag = new int[ImagepathList.size()];
                ((MyAdapter) mAdapter).notifyDataSetChanged();
              // getSupportLoaderManager().restartLoader(0, null, this);
              //  createLoadeach();

              //  imageloader.onStartLoading();
               // scanEach();

            }
        }
        if (requestCode == PICK_IMAGE ) {
            /*
            if (resultCode == RESULT_OK ){
                Uri imageUrigal = data.getData();

               // String pss= "";
               String pss= getRealPathFromURI(imageUrigal);
                if(imageUrigal.toString()!=null){
                    ImagepathList.add(pss);
                    OcrresultList.add("t");
                    ScannedList.add("t");

                }
                ImageBitmaparray = new Bitmap[ImagepathList.size()];
                ImageBitmapArrayflag = new int[ImagepathList.size()];

                ((MyAdapter) mAdapter).notifyDataSetChanged();

            } else if (resultCode == RESULT_CANCELED ) {
                Toast.makeText( this , "Cancelled" , Toast. LENGTH_LONG ).show();
            }
            */
        }
        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

               // imgCapture.setImageBitmap(selectedBitmap);
            }
        }
        if (requestCode == CROP_REQUEST) {
            String  path = data.getStringExtra(Rotate_Crop_Activity.PATH_CROP);

            saveTempBitmap(imageCropped,pathCropped,"");
            if(!path.isEmpty()){

                  ImagepathList.add(pathCropped);
                  OcrresultList.add("t");
                ScannedList.add("t");

            }
            ImageBitmaparray = new Bitmap[ImagepathList.size()];
            ImageBitmapArrayflag = new int[ImagepathList.size()];

            ((MyAdapter) mAdapter).notifyDataSetChanged();
        }
        if (requestCode == EDIT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(ImageText.EXTRA_REPLY);
                int getc = data.getIntExtra(ImageText.EXTRA_Pos,0);
                OcrresultList.remove(getc);

                OcrresultList.add(getc, str + "");
                ((MyAdapter) mAdapter).notifyDataSetChanged();
            }
        }
    }
    public void onResultClick( int poss, String  path, String  result) {
        if (!isInActionMode) {
            Intent intent = new Intent(this, ImageText.class);
            String message = path;

            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra(EXTRA_MESSAGE1, result);
            intent.putExtra(EXTRA_MESSAGE22, poss);

            startActivityForResult(intent, EDIT_REQUEST);
           // Intent move = new Intent(MainActivity.this,ImageText.class);

           //  startActivity(move);
        }
    }




    public void scanEach() {
      //  mRecyclerView.setKeepScreenOn(true);
        fnameCam="";
        if (ScannedList.size() > 0) {
            int loop1 = ScannedList.size();
            for (int c = 0; c < loop1; c++) {
                int getc = c;
                //if (c==0) {
                if (!(loop1 == ScannedList.size())) {

                    break;
                }
                if (!(ScannedList.get(getc).contains("Scanned"))) {

                    Uri imageUrii = Uri.parse(ImagepathList.get(getc));

                    String[] str = new String[2];
                    Bitmap bitmap = null;



                    DisplayMetrics dismet=new DisplayMetrics();
                    this.getWindowManager().getDefaultDisplay().getMetrics(dismet);
                    float destWidth =1+ (dismet.widthPixels);
                    float destHeight = 1+(dismet.heightPixels);
                    //(int)destWidth,(int)destHeight
                    try {
                        String iPath = ImagepathList.get(getc);
                        bitmap = Glide.with(getApplicationContext()).asBitmap().load(iPath).submit().get();
                        textRecognizer(bitmap, str);



                        String[] res = new String[4];
                        res[0] = str[0];

                        int[] resSize = new int[4];
                        resSize[0] = str[0].length();
                        for (int cror = 1; cror < 4; cror++) {
                            bitmap= rotateBitmap(bitmap, 90);

                            textRecognizer(bitmap, str);
                            res[cror] = str[0];
                            resSize[cror] = res[cror].length();
                        }
                        Arrays.sort(resSize);
                        for (int cror = 0; cror < 4; cror++) {
                           if (res[cror].length()==resSize[3]){
                               str[0]=res[cror];
                          break;
                           }
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                   //
                   // cropImageView2.setImageBitmap(imageCropped);
                    if ((loop1 == ScannedList.size())) {
                        OcrresultList.remove(getc);

                        OcrresultList.add(getc, str[0] + "");
                        ScannedList.remove(getc);

                        ScannedList.add(getc, "Scanned");
                        //  break;
                    }
                    if (!(loop1 == ScannedList.size())) {

                        loop1 = ScannedList.size();
                         c = 0;



                    }
                }
            }

        }
/*
        if (ScannedList.size() > 0) {
            int loop1 = ScannedList.size();
        for (int c = 0; c < loop1; c++) {
            int getc = c;
            if ( ImageBitmapArrayflag.length!=ScannedList.size()) {
               break;

            }
            //if (c==0) {
            if ( ImageBitmapArrayflag[getc]==1) {
                //   ImageBitmapListflag.add("yet");
                //  ImageBitmapList.add(null);
             //   if (!(ImageBitmapListflag.get(getc).contains("progress"))) {
                   // ImageBitmapListflag.remove(getc);
                  //  ImageBitmapListflag.add(getc, "progress");
                    Uri imageUrii = Uri.parse(ImagepathList.get(getc));

                    String[] str = new String[2];
                    Bitmap bitmap = null;

                    String iPath = ImagepathList.get(getc);
               // if ( ImageBitmaparray[getc]==null) {
                    bitmap = ((MyAdapter) mAdapter).getScaledDrawable(this, imageUrii.getPath());
                    ImageBitmaparray[getc]=bitmap;
               // }

                   // ImageBitmapList.remove(getc);
               ((MyAdapter) mAdapter).notifyItemChanged(getc);
                   // ImageBitmapList.add(getc, bitmap);
                    break;
              // }
            }
        }

    }
*/
    }
   public void scanEachBitmap(String path,int poss ) {
        //((MyAdapter) mAdapter).scanEachBitmap( path, poss );

    }

    public void createLoadeach() {
        if(getSupportLoaderManager().getLoader(0)!=null){
           // getSupportLoaderManager().destroyLoader(0);
         //  if(getSupportLoaderManager().getLoader(0).isStarted()!=true) {
               getSupportLoaderManager().initLoader(0, null, this);
        //  }
        }
        if(getSupportLoaderManager().getLoader(0)==null){

            getSupportLoaderManager().restartLoader(0, null, this);
        }


    }
    public String returnOcrString() {

        String resultOcr="";

        resultOcr=((MyAdapter) mAdapter).returnOcrString();
        return resultOcr;
    }
    @Override
    protected void onStart() {
        super.onStart();
       // Log.d(TAG, "onStart: call getProductData for skus: " + MySku.values());

    }
    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();

        try {
            saveimagepath.saveCrimes(ImagepathList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            saveocrresult.saveCrimes(OcrresultList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            savescanned.saveCrimes(ScannedList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }












    public void launchSecondActivity(String path) {


        // Intent intent = new Intent(this, SecondActivity.class);
        Intent intent = new Intent(this, Rotate_Crop_Activity.class);
        String message = path;

        intent.putExtra(EXTRA_MESSAGE, message);

        startActivityForResult(intent, CROP_REQUEST);
    }

    private  void textRecognizer(Bitmap bitmap, String [] str) {
        TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).build();
        if (!txtRecognizer.isOperational())
        {
            // Shows if your Google Play services is not up to date or OCR is not supported for the device
            // txtView.setText("Detector dependencies are not yet available");
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray items = txtRecognizer.detect(frame);

            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); i++)
            {
                TextBlock item = (TextBlock)items.valueAt(i);
                strBuilder.append(item.getValue());
                strBuilder.append("");
                // The following Process is used to show how to use lines & elements as well

                for (Text line : item.getComponents()) {
                    //extract scanned text lines here
                    Log.v("lines", line.getValue());
                    for (Text element : line.getComponents()) {
                        //extract scanned text words here
                        Log.v("element", element.getValue());
                    }

                }
            }
            //  Captured.setVisibility(View.GONE);
            //  txtView.setVisibility(View.VISIBLE);
            // txtView.setText(strBuilder.toString());
            str[0]=strBuilder.toString();
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
       // String queryString = "queryString";
       // args.putString("queryString", queryString);
        imageloader=new ImageLoader(this);
        return imageloader;
    }



    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
     boolean  isInActiontemp = false;
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
      //  mAdapter.notifyDataSetChanged();
    }

    public void shareText() {
        String txt= "";
        txt=returnOcrString() ;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "scan result");
        // share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");
        share.putExtra(Intent.EXTRA_TEXT, txt);
        startActivity(Intent.createChooser(share, "Share text!"));

    }
    public void showDialog() {
        DialogFragment newFragment = new SampleDialogFragment();
        newFragment.show(getSupportFragmentManager(),"sample");

    }


    protected String getRealPathFromURI(Uri contentURI) {

      Uri pathuri=  MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String thePath = "";
       // String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        String[] filePathColumn = { android.provider.MediaStore.Images.ImageColumns.DATA };

        Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);
        String folder = "";

        String folderpaths =  "";
       // String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder+"/"));
       // folderpaths = folderpaths+folder+"/";
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);

        }

        cursor.close();

        return thePath;
    }



    protected void createTxt(String fname ,String value) {
        Environment.getExternalStorageDirectory().getPath();
        String directorypath = Environment.getExternalStorageDirectory().getPath() + "/"+getString(R.string.app_name)+"/";
        File file = new File(directorypath);
        if (!file.exists()) {
            file.mkdir();
        }
        try {

            String targetTxt = directorypath+fname+".txt";
            File filePath = new File(targetTxt);


            FileOutputStream fileout=new FileOutputStream(filePath);

            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(value);
            outputWriter.close();

            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) { }
    }



    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
       // Canvas canvas = new Canvas(rotatedBitmap);
       // canvas.drawBitmap(original, 5.0f, 0.0f, null);

        return rotatedBitmap;
    }

    public void savePdf(String fname,String value) {

        //create object of Document class
        Document mDoc = new Document();
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //pdf file path
        mFileName=fname+mFileName;
        String mFilePath = Environment.getExternalStorageDirectory().getPath() + "/"+getString(R.string.app_name)+"/" + mFileName + ".pdf";


        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for writing
            mDoc.open();
            //get text from EditText i.e. mTextEt
            String mText = value;

            //add author of the document (optional)
           // mDoc.addAuthor("Atif Pervaiz");

            //add paragraph to the document
            mDoc.add(new Paragraph(mText));

            //close the document
            mDoc.close();
            //show message that file is saved, it will show file name and file path too
            Toast.makeText(this, mFileName +".pdf\nis saved to\n"+ mFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            //if any thing goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //handle permission result

    public void onRadioButtonClicked(View view) {
        // Is a button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.txt:
                if (checked)


                    break;
            case R.id.pdf:
                if (checked)

                    break;
            default:

                break;
        }
    }
    public void saveTempBitmap(Bitmap bitmap,String Dir,String fname) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap,Dir, fname);
        }else{
            //prompt the user or do something
        }
    }

    public  void saveImage(Bitmap finalBitmap,String Dir,String fname) {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.app_name) + "/";
        //  String root = Environment.getExternalStorageDirectory().toString();
        File myDir= new File(Dir);
        if(Dir.isEmpty()) {
             myDir = new File(directory_path + "/saved_images");
        }
        if (!myDir.exists()&&Dir.isEmpty()) {
            myDir.mkdirs();
        }
        if(fname.isEmpty()&&Dir.isEmpty()) {


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            fname = "Ocrimage_" + timeStamp + ".jpg";
        }
        File file= new File(Dir);
        if(fname.isEmpty()&&Dir.isEmpty()) {
            file = new File(myDir, fname);
        }
        if (file.exists()){ file.delete ();}
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void returnCancel(View view) {
        isInActionModeSave = false;
        selectionPosition.clear();
        mAdapter.notifyDataSetChanged();
        SaveB.setVisibility(View.GONE);
        CancelB.setVisibility(View.GONE);
        clearActionMode();
        fabgal.show();
    }

    public void returnSave(View view) {
        if(selectionPosition.size()>0) {
            if (SaveB.getText().equals("Save")) {
                showDialog();
            }
            if (SaveB.getText().equals("Delete")) {
                ((MyAdapter) mAdapter).removeData(selectionPosition);
                 clearActionMode();
            }

            if (SaveB.getText().equals("Share")) {
                shareText();
            }

               // isInActionModeSave = false;
               // SaveB.setVisibility(View.GONE);
                //CancelB.setVisibility(View.GONE);
                //fabgal.show();
            //clearActionMode();

        }
    }


}
