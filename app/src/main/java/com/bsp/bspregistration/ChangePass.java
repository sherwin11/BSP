package com.bsp.bspregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangePass extends AppCompatActivity {

    EditText emailchange,newpas,retrypas;
    Button butchange;
    Loading loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        emailchange = (EditText) findViewById(R.id.emailchange);
        emailchange.setText(CurrentUser.username);
        newpas = (EditText) findViewById(R.id.newpas);
        retrypas = (EditText) findViewById(R.id.retrypas);
        butchange = (Button) findViewById(R.id.butchange);
        loadingDialog = new Loading(ChangePass.this);
        butchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAPI();
            }
        });

        showWarningDialogx("Password default","You must change your password first.");
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
    public void sendAPI(){
        if (isconnected()) {


            if(!String.valueOf(newpas.getText()).equals("")  && !String.valueOf(retrypas.getText()).equals("")){
                if (String.valueOf(retrypas.getText()).equals(String.valueOf(newpas.getText()))){


                ArrayList<ChangepassCollection> regCollections = new ArrayList<>();
                ChangepassCollection reg = new ChangepassCollection("bsp_leyte-ddc-1989",
                        String.valueOf(emailchange.getText()),String.valueOf(newpas.getText()),CurrentUser.user_id);
                regCollections.add(reg);
                Gson gson = new Gson();
                String json = gson.toJson(regCollections);
                   // ApiHost.user_id + "
                   // StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/Api/changeUnitLeaderPassword", new Response.Listener<String>() {
                loadingDialog.startLoadingDialog();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiHost.user_id + "bsp-api/Api/changeUnitLeaderPassword", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissdialog();
                        if (response.contains("Password Changed Successfully")){
                            showWarningDialogx("Congratulations","Password Changed Successfully, tap anywhere to close.");
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
                }
                else {
                    showWarningDialogx("Password issue.", "New password don't match to retry password.");
                }

            }else{
                showWarningDialogx("Field issue.", "Please fill up all the fields.");
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
                if(warning.contains("Congratulations")){
                    finish();
                }
            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbggreen);

        dialog.show();
    }
}