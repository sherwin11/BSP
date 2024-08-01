package com.bsp.bspregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    Button butcreate;
    EditText surreg,firstreg,midreg,agereg,emailreg,schoolreg,birthreg;
    AutoCompleteTextView genderreg;
    ImageView genView;
    Loading loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        butcreate = (Button) findViewById(R.id.createreg);

        surreg = (EditText) findViewById(R.id.surreg);
        firstreg = (EditText) findViewById(R.id.firstreg);
        midreg = (EditText) findViewById(R.id.midreg);
        agereg = (EditText) findViewById(R.id.agereg);
        emailreg = (EditText) findViewById(R.id.emailreg);
        schoolreg = (EditText) findViewById(R.id.schoolreg);
        birthreg = (EditText) findViewById(R.id.birthreg);
        genderreg = (AutoCompleteTextView) findViewById(R.id.genderreg);
        genView = (ImageView) findViewById(R.id.gnbiew) ;

        loadingDialog = new Loading(Register.this);
        genderreg.setInputType(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,genlist);
        genderreg.setAdapter(adapter);
        genView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderreg.showDropDown();
            }
        });
        genderreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderreg.showDropDown();
            }
        });

        birthreg.setInputType(0);
        birthreg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDatePicker();
                }
            }
        });

        butcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAPI();
            }
        });

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
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    birthreg.setText(selectedDate);
                },
                // Initial date values
                2023, 7, 11 // Year, month (0-11), day
        );
        datePickerDialog.show();
    }

    public static final String[] genlist = new String[]{"Male","Female"};
    public void sendAPI(){
        if (isconnected()) {
            if(isValidEmail(String.valueOf(emailreg.getText()))){


                if(!String.valueOf(surreg.getText()).equals("")  && !String.valueOf(firstreg.getText()).equals("") && !String.valueOf(midreg.getText()).equals("") &&
                        !String.valueOf(agereg.getText()).equals("") &&
                        !String.valueOf(emailreg.getText()).equals("") && !String.valueOf(schoolreg.getText()).equals("") && !String.valueOf(birthreg.getText()).equals("") &&
                !String.valueOf(genderreg.getText()).equals("")){

                    ArrayList<RegCollection> regCollections = new ArrayList<>();
                    RegCollection reg = new RegCollection("bsp_leyte-ddc-1989",
                            String.valueOf(surreg.getText()),String.valueOf(firstreg.getText()),String.valueOf(midreg.getText()),String.valueOf(agereg.getText()),
                            String.valueOf(emailreg.getText()),String.valueOf(schoolreg.getText()),String.valueOf(birthreg.getText()),String.valueOf(genderreg.getText()));
                    regCollections.add(reg);
                    Gson gson = new Gson();
                    String json = gson.toJson(regCollections);
                   // ApiHost.user_id + "
                    //StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/Api/addUnitLeader", new Response.Listener<String>() {
                        loadingDialog.startLoadingDialog();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiHost.user_id + "bsp-api/Api/addUnitLeader", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loadingDialog.dismissdialog();
                            if (response.contains("Registered Successfully")){
                                showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadingDialog.dismissdialog();
                            Toast.makeText(getApplicationContext(), "errror", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("data",json);
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
                }else{
                    showWarningDialogx("Field issue.", "Please fill up all the fields.");
                }

            }else {
                showWarningDialogx("Email issue.", "Please input a valid email.");
            }

        }else {
            showWarningDialogx("Connection issue.", "Unable to connect to internet.");
        }

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
}