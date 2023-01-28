package com.tarikum3.tryocr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SampleDialogFragment extends DialogFragment implements


        DialogInterface.OnClickListener {

    private View form=null;
    MainActivity activity ;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        form=getActivity().getLayoutInflater()
                        .inflate(R.layout.dialog, null);
        TextView filepath=(TextView)form.findViewById(R.id.filepath);
        RadioButton RBtext=(RadioButton)form.findViewById(R.id.txt);

         String directorypath = "storage"+ "/"+getString(R.string.app_name)+"/";
         filepath.setText(directorypath);
        RBtext.setChecked(true);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());



        return(builder.setTitle(R.string.dialogtitle).setView(form)
                .setPositiveButton(R.string.savefile, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
       // String template=getActivity().getString(R.string.toast);


        RadioButton RBtext=(RadioButton)form.findViewById(R.id.txt);
        RadioButton RBpdf=  (RadioButton)form.findViewById(R.id.pdf);

        EditText filname=(EditText)form.findViewById(R.id.filename);
     //   String msg=String.format(filname.getText().toString())+MainActivity.selectionPosition.size();

        String msg=String.format(filname.getText().toString());
        if(msg!=null) {
            String val="";
             val=  activity.returnOcrString();
            if (RBtext.isChecked()) {
                activity.createTxt(msg,val);
            }
            if (RBpdf.isChecked()) {
                activity.savePdf(msg,val);

            }
        }

     //   Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

        // Invoke Main Activity's processDatePickerResult() method.
     //  activity.processDialogResult(msg);
        activity.clearActionMode();
    }
    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
       // Log.d(getClass().getSimpleName(), "Goodbye!");
       // • onCancel(), which is called if the user presses the BACK button to exit the
       //         dialog
        //• onDismiss(), which is called whenever the dialog goes away for any reason
       // (BACK or a button click)
        activity. clearActionMode();
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
       // Toast.makeText(getActivity(), R.string.back, Toast.LENGTH_LONG).show();
        activity.clearActionMode();
    }

}
