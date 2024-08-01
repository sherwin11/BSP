package com.bsp.bspregistration;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class Receipt extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;


    TextView amounttxt,mobileNumber,refnumber,amountlabel;
    Button backtomenu;
    private View formView;

    boolean alreadycapture;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        formView = findViewById(R.id.formx);

        amounttxt = (TextView) findViewById(R.id.amounttxt);
//        mobileNumber = (TextView) findViewById(R.id.mobileNumber);
        refnumber = (TextView) findViewById(R.id.refnumber);
        backtomenu = (Button) findViewById(R.id.backtomenu);
        amountlabel = (TextView) findViewById(R.id.amountlabel);

        alreadycapture = false;

        formView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!alreadycapture){
                    captureScreenshot();
                }
            }
        });


        String refnumberx = getIntent().getStringExtra("REF");
        String amount = getIntent().getStringExtra("AMOUNT");
        String from = getIntent().getStringExtra("FROM");
        if (refnumberx.contains("BSP")){
            regconfirm("Registration Confirm","Please take note you're reference Number");
        }
        else {
            regconfirm("Registration is Complete","Please take note you're reference Number");
        }

        if(from.contains("his")){
            backtomenu.setText("BACK TO PREVIOUS PAGE");
        }
        if (amount.equals("")){
            amounttxt.setVisibility(View.INVISIBLE);
            amountlabel.setVisibility(View.INVISIBLE);
        }
        else {
            amounttxt.setVisibility(View.VISIBLE);
            amountlabel.setVisibility(View.VISIBLE);
        }

        amounttxt.setText(amount);
        refnumber.setText("Reference Number : " + refnumberx);
//        mobileNumber.setText("Mobile Number : 09123456789");


        backtomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from.contains("his")){
                    Receipt.super.onBackPressed();
                }else {
                    Intent intent = new Intent(v.getContext(), SplashScreenx.class);
                    v.getContext().startActivity(intent);
                }

            }
        });
        if(!isconnected() && ModeOL.isOnline)
        {
            showWarningDialogx("Unable to connect to internet.", "All the data you save will not be send to our server but automatically send it once the program detect an internet.");
        }
    }
    public void regconfirm(String warning, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(warning);
        builder.setMessage(message);
        if (warning.contains("Confirm")){
            builder.setIcon(android.R.drawable.ic_dialog_email);
        }else {
            builder.setIcon(android.R.drawable.ic_dialog_info);
        }

        // Add a positive button (optional)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code to be executed when the user clicks the "OK" button
                // (optional, you can leave this empty or remove it if not needed)
            }
        });

        // Add a negative button (optional)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code to be executed when the user clicks the "Cancel" button
                // (optional, you can leave this empty or remove it if not needed)
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                Intent refresh = new Intent(getContext(), MainActivity.class);
//                startActivity(refresh);
                //finish();
            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbggreen);

        dialog.show();
    }

    public void showWarningDialogx(String warning, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(warning);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Add a positive button (optional)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code to be executed when the user clicks the "OK" button
                // (optional, you can leave this empty or remove it if not needed)
            }
        });

        // Add a negative button (optional)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code to be executed when the user clicks the "Cancel" button
                // (optional, you can leave this empty or remove it if not needed)
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                Intent refresh = new Intent(getContext(), MainActivity.class);
//                startActivity(refresh);
                //finish();
            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbggreen);

        dialog.show();
    }
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
    @Override
    public void onBackPressed() {
        Boolean bo = false;
        if (bo == true){
            super.onBackPressed();
        }else{
            Intent intent = new Intent(formView.getContext(), SplashScreenx.class);
            formView.getContext().startActivity(intent);
        }
    }

    private void captureScreenshot() {
        try {
            alreadycapture = true;
            formView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(formView.getDrawingCache());
            formView.setDrawingCacheEnabled(false);

            String fileName = "screenshot_" + new Date().getTime() + ".png";

            // Save the screenshot to the MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();

          //  Toast.makeText(this, "Screenshot saved to Gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Please screenshot it manually.", Toast.LENGTH_SHORT).show();
        }
    }
    public Boolean isconnected(){
        boolean ret = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    int networkType = activeNetworkInfo.getType();
                    // Check if the active network is either Wi-Fi or cellular
                    if (networkType == ConnectivityManager.TYPE_WIFI) {
                        if (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED){
                            ret = true;
                        }
                        else {
                            ret = false;
                        }
                    } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
                        if (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED){
                            ret = true;
                        }
                        else {
                            ret = false;
                        }
                    }
                } else {
                    // Not connected to any network
                    ret = false;
                }
            } else {
                // Handle the case when ConnectivityManager is null
                ret = false;
            }
        }catch (Exception e){
            ret = false;
        }
        return ret;
    }

//    public Boolean isconnected(){
//        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
//        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//            //we are connected to a network
//            return   true;
//        }
//        else{
//            return   false;
//        }
//    }
}