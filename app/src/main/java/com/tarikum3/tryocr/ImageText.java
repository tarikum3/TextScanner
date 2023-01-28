package com.tarikum3.tryocr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageText extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.tarikum3.tryocr.extra.MESSAGEreply";
    public static final String EXTRA_Pos = "com.tarikum3.tryocr.extra.MESSAGEpos";
    private ImageView Captured ;
    private EditText editText  ;
    private Activity mActivity;
    private ClipboardManager cm=null;
    private String imagepath="";
    private String ocrresult="";
    private int pathPos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text);
        Captured = (ImageView) findViewById(R.id.clickedImage );
        editText= (EditText) findViewById(R.id.clickedText );
        Intent intent = getIntent();
        String path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String result = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);
        int pos =  intent.getIntExtra(MainActivity.EXTRA_MESSAGE22,0);
        pathPos=pos;
         imagepath=   path;
         ocrresult = result;
        Uri imageUrii= Uri.parse(imagepath);
        Captured.setImageURI(imageUrii);
        editText.setText(ocrresult);
        cm=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        String mText = editText.getText().toString();
    }

    public void onclickText(View view) {
        Captured.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);
        Button bI = ( Button) findViewById(R.id.timage);
        Button bT = ( Button) findViewById(R.id.ttext);
        bT.setTextColor(getResources().getColor(R.color.bluetrans));
        bI.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void onclickImage(View view) {
        editText.setVisibility(View.GONE);
        Captured.setVisibility(View.VISIBLE);
        Button bI = ( Button) findViewById(R.id.timage);
        Button bT = ( Button) findViewById(R.id.ttext);
        bI.setTextColor(getResources().getColor(R.color.bluetrans));
        bT.setTextColor(getResources().getColor(R.color.colorPrimary));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_two, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_copy) {

            copyText();



        } else if (item.getItemId() ==  R.id.item_share2) {
            shareText();
        }
       else if (item.getItemId() == android.R.id.home) {

          saveEdited();
          return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void copyText() {
       String txt= ocrresult;

        ClipData clip=ClipData.newPlainText("edittext",txt);
        try {
            cm.setPrimaryClip(clip);

        }
        catch (Exception e) {

            Toast.makeText(this, "Exception: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }



    }
    public void shareText() {
        String txt= ocrresult;
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

    public void saveEdited() {
         View form=null;

            form=this.getLayoutInflater()
                    .inflate(R.layout.dialogsave, null);
        String reply = editText.getText().toString()+"";
    if(!reply.equals(ocrresult)) {
        new AlertDialog.Builder(this)
                .setTitle("Save Text")
                .setView(form)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String     reply = editText.getText().toString();
                         Intent replyIntent = new Intent();
                         replyIntent.putExtra(EXTRA_REPLY, reply);
                        replyIntent.putExtra(EXTRA_Pos, pathPos);
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .create().show();
       // reply = editText.getText().toString();
       // Intent replyIntent = new Intent();
       // replyIntent.putExtra(EXTRA_REPLY, reply);
        //replyIntent.putExtra(EXTRA_Pos, pathPos);
       // setResult(RESULT_OK, replyIntent);

           }
        if(reply.equals(ocrresult)) {
            finish();
        }

    }
    @Override
   public void onBackPressed() {
        saveEdited();
   }
  //  @Override
   // public boolean onNavigateUp() {
    //    saveEdited();
    //    return false;
   // }
}
