package com.tarikum3.tryocr;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tarikum3.tryocr.MainActivity;

public class Rotate_Crop_Activity extends AppCompatActivity {
    Bitmap imagebitmap = null;
   // Bitmap textBit = Image_Display_Activity.bm;
    CropImageView cropImageView;
    int rot = 0;
    Uri imageUriO=null;
    MainActivity activity=new MainActivity();
    String getpath="";
    public static final String PATH_CROP =
            "com.tarikum3.tryocr.extra.path_crop";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
      //  activity=new MainActivity() ;
        getpath=message;
        Uri imageUri=Uri.parse(message);
        imageUriO=imageUri;
        // Captured.setImageURI(Uri.parse(message));
//File image=new File(imageUri.getPath());
      // getpath=activity.getRealPathFromURI(imageUriO);
     /*
        try {
            imagebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    */

        setContentView(R.layout.activity_rotate__crop_);
       getSupportActionBar().setDisplayShowTitleEnabled(false);


        cropImageView = (CropImageView)findViewById(R.id.cropImageView);
       // cropImageView.setImageBitmap(textBit);
       cropImageView.setImageUriAsync(imageUri);
        //cropImageView.setImageBitmap(imagebitmap);
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON);

        final ImageView cropBitmapIcon = (ImageView)findViewById(R.id.cropBitmapIcon);
        /*
        cropBitmapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // textBit = Image_Display_Activity.bm = cropImageView.getCroppedImage();
                imagebitmap = cropImageView.getCroppedImage();
               // (Image_Display_Activity.imageDisplay).setImageBitmap(Image_Display_Activity.bm);
                Rect wh = cropImageView.getCropRect();
              //  Image_Display_Activity.iHeight = wh.height();
                Toast.makeText(getApplicationContext(),"Crop Applied",Toast.LENGTH_SHORT).show();
            }
        });
        */
/*
        SeekBar rotateBar = (SeekBar)findViewById(R.id.rotateBar);
        rotateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cropImageView.setRotatedDegrees(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
*/
        ImageView cancelCropIcon = (ImageView)findViewById(R.id.cancelCropIcon);
        /*
        cancelCropIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
*/
        ImageView saveCroppedIcon = (ImageView)findViewById(R.id.saveCropIcon);
        /*
        saveCroppedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  textBit = Image_Display_Activity.bm = cropImageView.getCroppedImage();
                imagebitmap= cropImageView.getCroppedImage();
                try{saveImage();}catch (Exception e){e.printStackTrace();}
            }
        });
*/
        CheckBox fixedAspectRatioCheck = (CheckBox)findViewById(R.id.fixedAspectRatioCheck);
        /*
        fixedAspectRatioCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){cropImageView.setFixedAspectRatio(true);}
                else{cropImageView.setFixedAspectRatio(false);}
            }
        });
       */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rot_right_action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.rotate_right:
                rot+=90;
                if(rot>360){rot-=360;}
                cropImageView.setRotatedDegrees(rot);
                return true;
            case R.id.rotate_left:
                rot+=270;
                if(rot>360){rot-=360;}
                cropImageView.setRotatedDegrees(rot);

                return true;
            case R.id.crop_image:

               imagebitmap= cropImageView.getCroppedImage();

               // getpath=activity.getRealPathFromURI(imageUriO);
               // activity.saveTempBitmap(imagebitmap,getpath,"");

          MainActivity.imageCropped=imagebitmap;
                    //  if(!getpath.isEmpty()) {
                          Intent replyIntent = new Intent();
                          replyIntent.putExtra(PATH_CROP, getpath);
                          setResult(RESULT_OK, replyIntent);
                          finish();
                    //  }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveImage()throws Exception{

       // (Image_Display_Activity.imageDisplay).setImageBitmap(Image_Display_Activity.bm);
        FileOutputStream fOut = null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_"+timeStamp+"_";
        File file2 = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(imageFileName,".png",file2);

        try{
            fOut = new FileOutputStream(file);
        }catch (Exception e){e.printStackTrace();}
      //  (Image_Display_Activity.bm).compress(Bitmap.CompressFormat.PNG,100,fOut);
        try{
            fOut.flush();
        }catch (Exception e){e.printStackTrace();}
        try{fOut.close();}catch (IOException e){e.printStackTrace();}
        try{
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());}
        catch (FileNotFoundException e){e.printStackTrace();}

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri cUri = Uri.fromFile(file);
        mediaScanIntent.setData(cUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(getApplicationContext(),"Image Saved to Pictures",Toast.LENGTH_SHORT).show();

    }
}
