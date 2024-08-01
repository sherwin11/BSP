package com.bsp.bspregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashScreenx extends AppCompatActivity {

    Button aur,asr,aar,forups;
    private View formView;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private DBHandler dbHandler;
    private Handler timerHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screenx);

        formView = findViewById(R.id.formview);

        aur = (Button) findViewById(R.id.aur);
        aar = (Button) findViewById(R.id.aar);
        asr = (Button) findViewById(R.id.asr);
        forups = (Button) findViewById(R.id.forups);

        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String referenceNumber = sdf.format(currentDate);

        dbHandler = new DBHandler(getApplicationContext(), "forupload.db");
        forups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Foruploads.class);
                v.getContext().startActivity(intent);
            }
        });
        if(isconnected())
        {
            if(ModeOL.isOnline){
                if (!CurrentUser.user_id.equals("")){
                   // timerHandler.postDelayed(timerhandler, 1000);
                }
            }
        }
        aur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkPermission()) {
//                    captureScreenshot();
//                } else {
//                    requestPermission();
//                }
                if(ModeOL.isOnline){
                    if(CurrentUser.getHasaur().equals("false")){
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.putExtra("FORMTYPE","AUR");
                        v.getContext().startActivity(intent);
                    }else {
                        showWarningDialogx("Unit","You have already register an AUR.");
                    }
                }else {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("FORMTYPE","AUR");
                    v.getContext().startActivity(intent);
                }
            }
        });

        aar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ModeOL.isOnline){
                    if (CurrentUser.getHasaur().equals("true")){
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.putExtra("FORMTYPE","AAR");
                        v.getContext().startActivity(intent);
                    }else
                    {
                        showWarningDialogx("Unit","You need to register a unit (AUR) first to register adult registration.");
                    }
                }else {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("FORMTYPE","AAR");
                    v.getContext().startActivity(intent);
                }
            }
        });

        asr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ModeOL.isOnline){
                    if(CurrentUser.getHasaur().equals("true")){
//                        if(CurrentUser.getIspaid().equals("false")){
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            intent.putExtra("FORMTYPE","ASR");
                            v.getContext().startActivity(intent);
//                        }else {
//                            showWarningDialogx("Payment","Your Application for Unit Registration (AUR) is already paid. Adding members is  not available using ASR form.");
//                        }
                    }else {
                        showWarningDialogx("Unit","You need to register a unit (AUR) first to register additional registration.");
                    }
                }else {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("FORMTYPE","ASR");
                    v.getContext().startActivity(intent);
                }


            }
        });
        if (!ModeOL.isOnline){
            showWarningDialogx("You're using the offline mode.", "All the data you save will not be sent to our server. Instead, it will be saved to your local database and uploaded manually.");
        }

        showWarningDialogx("SAFE FROM HARM.", "World Scouting emphasizes that the achievement of Scouting’s Mission makes it essential for the Movement to provide young people with a “safe passage” based on respect for their integrity and their right to develop in a non constraining environment. The Boy Scouts of the Philippines implements “Safe from Harm” on the conviction that all adults and children have a right NOT to be abused. This is a fundamental human right. Abuse can take the form of bullying, physical abuse, emotional abuse, neglect, sexual abuse and exploitation. It is important to note that young people can suffer from one or a combination of these forms of abuse. Abuse can take place at home, at school or anywhere young people spend time. In the great majority of cases, the abuser is someone the young person knows, such as a parent, teacher, relative, leader or friend. The main objective is to ensure that no one will be exposed to abuse. Good child protection practice means making sure that everyone is aware of signs of potential abuse. It is based upon the Declaration on the Rights of the Child and Human Rights. I hereby commit and fully subscribe to the existing Safe From Harm Policy of the Boy Scouts of the Philippines, and that I hereby absolve and free the BSP from any liability arising from any of my acts contrary to the policy. I hereby accept that the BSP may immediately revoke my registration as an adult leader upon violation of such policy.");

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Re-login?")
                .setMessage("Please press OK to proceed.")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent refresh = new Intent(getApplicationContext(), Login.class);
                        startActivity(refresh);
                    }
                }).create().show();
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
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureScreenshot();
            } else {
                Toast.makeText(this, "Permission denied. Cannot capture screenshot.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void captureScreenshot() {
        try {
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

            Toast.makeText(this, "Screenshot saved to Gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
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
    private  Runnable timerhandler = new  Runnable()
    {
        @Override
        public void run() {
            ArrayList<String> tmpAr = new ArrayList<>();
            tmpAr = dbHandler.selectUps();
            for (String ans: tmpAr) {
                String urllink = "";
                if (ans.contains("oldaasrnumber")){

                    urllink = ApiHost.user_id + "bsp-api/AndroidApi/additionalRegistration";
                } else if(ans.contains("oldaarnumber")) {
                    urllink = ApiHost.user_id + "bsp-api/AndroidApi/adultRegistration";
                } else if(ans.contains("aurnumber")){
                    urllink = ApiHost.user_id + "bsp-api/AndroidApi/unitRegistration";
                }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urllink, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("success")){
                                //dbHandler.updateAnswetoUploaded(ans);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(), "errror", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("data",ans);
                            return params;
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    stringRequest.setShouldCache(false);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MyInsertAPI.getInstance(getApplicationContext()).addTorequest(stringRequest);
            }
        }
    };
}