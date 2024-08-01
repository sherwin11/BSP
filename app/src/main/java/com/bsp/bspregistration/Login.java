package com.bsp.bspregistration;

import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.annotation.JsonProperty;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kotlin.jvm.internal.TypeReference;

public class Login extends AppCompatActivity {

    private DBHandler dbHandler;
    EditText usernamelogin,passwordlogin;
    Button butreg,butlogin,butloginoffline, setuphost;
    PopupWindow popupWindow1;
    View ipview;
    Loading loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        butreg = (Button) findViewById(R.id.butcreate);
        butlogin = (Button) findViewById(R.id.butlogin);
        usernamelogin = (EditText) findViewById(R.id.usernamelogin);
        passwordlogin = (EditText) findViewById(R.id.passwordlogin);
        butloginoffline = (Button) findViewById(R.id.butloginoffline);
        setuphost = (Button) findViewById(R.id.setuphost);
        //ApiHost.setUser_id(readtxtHost());
        ApiHost.setUser_id("http://idcsi-officesuites.com:8282/");
       // ApiHost.setUser_id("http://192.168.23.77/");
        loadingDialog = new Loading(Login.this);
        //String sad = ApiHost.user_id;
        dbHandler = new DBHandler(Login.this, "forupload.db");
        if (isconnected()){
            getadultpost();
        }
//        usernamelogin.setText("jackfroost000@gmail.com");
//        passwordlogin.setText("sher");
        setuphost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isconnected()){

                    //http://192.168.23.77/bsp-api/AndroidApi/test
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    ipview = inflater.inflate(R.layout.ip_setup, null);
                    Button buttest = ipview.findViewById(R.id.btnTest);
                    Button buttsub = ipview.findViewById(R.id.btnSubmit);
                    EditText edtext = ipview.findViewById(R.id.iptext);
                    Button butclo = ipview.findViewById(R.id.clox);

                    if (ApiHost.user_id != "") {
                        edtext.setText(ApiHost.user_id.replace("http://", ""));
                    }


                    butclo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow1.dismiss();
                        }
                    });

                    buttsub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String finaltext = String.valueOf(edtext.getText());

                            String[] ok = String.valueOf(edtext.getText()).split("/");
                            boolean ready = false;
                            for (String ea: ok) {
                                if (ea.contains("bsp-api")){
                                    ready = true;
                                    continue;
                                }
                                if (ready){
                                    if (finaltext.contains(ea)){
                                        finaltext = finaltext.replace(ea,"");
                                    }
                                }
                            }

                            if (finaltext.endsWith("//")) {
                                finaltext = finaltext.substring(0, finaltext.length() - 2);
                            }
                            if (finaltext.contains("https://")) {
                                finaltext = finaltext.replace("https://", "");
                            }
                            if (finaltext.contains("http://")) {
                                finaltext = finaltext.replace("http://", "");
                            }
                            if (finaltext.endsWith("/")) {
                                finaltext = finaltext.substring(0, finaltext.length() - 1);
                            }
                            if (finaltext.startsWith("/")) {
                                finaltext = finaltext.substring(1);
                            }

                            String fortestdd = "http://" + finaltext + "/";

                            String finalTextx = finaltext;
                            loadingDialog.startLoadingDialog();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, fortestdd + "bsp-api/AndroidApi/test", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loadingDialog.dismissdialog();
                                    Toast.makeText(getApplicationContext(), "API SUCCESSFULLY CONNECTED.", Toast.LENGTH_LONG).show();

                                    writetxtHost("http://" + finalTextx + "/");
                                    ApiHost.setUser_id("http://" + finalTextx + "/");

                                    Toast.makeText(getApplicationContext(), "IP set.", Toast.LENGTH_LONG).show();
                                    popupWindow1.dismiss();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loadingDialog.dismissdialog();
                                    Toast.makeText(getApplicationContext(), "API NOT EXISTED.", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
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
                    });

                    buttest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String finaltext = String.valueOf(edtext.getText());
                            String[] ok = String.valueOf(edtext.getText()).split("/");
                            boolean ready = false;
                            for (String ea: ok) {
                                if (ea.contains("bsp-api")){
                                    ready = true;
                                    continue;
                                }
                                if (ready){
                                    if (finaltext.contains(ea)){
                                        finaltext = finaltext.replace(ea,"");
                                    }
                                }
                            }

                            if (finaltext.endsWith("//")) {
                                finaltext = finaltext.substring(0, finaltext.length() - 2);
                            }
                            if (finaltext.contains("https://")) {
                                finaltext = finaltext.replace("https://", "");
                            }
                            if (finaltext.contains("http://")) {
                                finaltext = finaltext.replace("http://", "");
                            }
                            if (finaltext.endsWith("/")) {
                                finaltext = finaltext.substring(0, finaltext.length() - 1);
                            }
                            if (finaltext.startsWith("/")) {
                                finaltext = finaltext.substring(1);
                            }
                            String fortestdd = "http://" + finaltext + "/";
                            loadingDialog.startLoadingDialog();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, fortestdd + "bsp-api/AndroidApi/test", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loadingDialog.dismissdialog();
                                    Toast.makeText(getApplicationContext(), "API SUCCESSFULLY CONNECTED.", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loadingDialog.dismissdialog();
                                    Toast.makeText(getApplicationContext(), "API NOT EXISTED.", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
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
                    });

                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    popupWindow1 = new PopupWindow(ipview, width, height, true);
                    popupWindow1.showAtLocation(butlogin, Gravity.CENTER, 0, 0);

                }
                else {
                    showWarningDialogx("Connection issue.", "Unable to connect to internet.");
                }
            }
        });

        butloginoffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser.setUser_id(readtxt());
                if (!CurrentUser.user_id.equals("")){
                   String[] str = readtxtotherinfo().split(",");
                    if (!readtxtotherinfo().equals("")){

                    CurrentUser.setSchoolID(str[0]);
                    CurrentUser.setDistrictID(str[1]);
                    CurrentUser.setUnitnumber(str[2]);
                    CurrentUser.setSchool(str[3]);
                    CurrentUser.setDistrict(str[4]);
                        CurrentUser.setUnitGroupID(str[5]);

                    ModeOL.switchToOffline();
                    Intent intent = new Intent(getApplicationContext(), SplashScreenx.class);
                    startActivity(intent);
                    }
                    else {
                        showWarningDialogx("Login Issue.", "First run of the program must login first.");
                    }
                }else {
                    showWarningDialogx("Login Issue.", "First run of the program must login first.");
                }

            }
        });
        butlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAPI();
            }
        });

        butreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Register.class);
                v.getContext().startActivity(intent);
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


    public void getadultpost(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiHost.user_id +  "bsp-api/Api/getAllAdultPositions", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    // Loop through the array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String type = jsonObject.getString("type");
                        String price = jsonObject.getString("amount_fee");
                        String posi = jsonObject.getString("position");
                        String arc = jsonObject.getString("archive");
                        dbHandler.addNewposition(posi,price,type,arc);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "API NOT EXISTED.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    public void sendAPI(){
        if (isconnected()) {


                if(!String.valueOf(usernamelogin.getText()).equals("")  && !String.valueOf(passwordlogin.getText()).equals("")){

                    ArrayList<LoginCollection> regCollections = new ArrayList<>();
                    LoginCollection reg = new LoginCollection("bsp_leyte-ddc-1989",
                            String.valueOf(usernamelogin.getText()),String.valueOf(passwordlogin.getText()));
                    regCollections.add(reg);
                    Gson gson = new Gson();
                    String json = gson.toJson(regCollections);
                   // ApiHost.user_id + "
                   // StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/Api/loginUnitLeader", new Response.Listener<String>() {
                    loadingDialog.startLoadingDialog();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiHost.user_id + "bsp-api/Api/loginUnitLeader", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loadingDialog.dismissdialog();
                            if (response.contains("unitNumber")){
                                try {
                                JSONObject jsonObject = new JSONObject(response);

                                // Retrieve values from the JSON object
                                int code = jsonObject.getInt("code");
                                String username = jsonObject.getString("username");
                                String fname = jsonObject.getString("fname");
                                String lname = jsonObject.getString("lname");
                                String id = jsonObject.getString("id");

                                     String schoolID= jsonObject.getString("school_id");
                                    String districtID= jsonObject.getString("district_id");
                                     String unitnumber= jsonObject.getString("unitNumber");
                                     String school= jsonObject.getString("school");
                                     String district= jsonObject.getString("district");

                                    String ischange = jsonObject.getString("is_pass_changed");

                                    String unitgroup = jsonObject.getString("unit_group_id");


                                    String hasu = jsonObject.getString("hasUnit");
                                    String ispa = jsonObject.getString("paid");

                                CurrentUser.setUser_id(id);
                                CurrentUser.setUsername(username);
                                CurrentUser.setFirstname(fname);
                                CurrentUser.setLastname(lname);
                                    CurrentUser.setUnitGroupID(unitgroup);

                                CurrentUser.setDistrict(district);
                                CurrentUser.setDistrictID(districtID);
                                CurrentUser.setSchoolID(schoolID);
                                CurrentUser.setSchool(school);
                                CurrentUser.setUnitnumber(unitnumber);
                                CurrentUser.setHasaur(hasu);
                                CurrentUser.setIspaid(ispa);

                                    if (!ischange.equals("1")){
                                       // showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                                        ModeOL.switchToOnline();
                                        writetxt(id);
                                        writetxtotherinfo(schoolID + "," + districtID + "," + unitnumber + "," + school + "," + district + "," + unitgroup);
                                        Intent intent = new Intent(getApplicationContext(), SplashScreenx.class);
                                        startActivity(intent);
                                }
                                else {
                                   // showWarningDialogx("Password default","You must change your password first.");
                                        usernamelogin.setText("");
                                        passwordlogin.setText("");
                                    Intent intent = new Intent(getApplicationContext(), ChangePass.class);
                                   // intent.putExtra("extra","changepass");
                                    startActivity(intent);
                                }

                                } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            }else {
                                showWarningDialogx("Crendentials issue","Invalid password or username.");
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
            showWarningDialogx("Connection issue.", "Unable to connect to internet.");
        }

    }
    public void writetxtHost(String str)  {
        try {
            FileOutputStream foutput = openFileOutput("Host1", MODE_PRIVATE);
            foutput.write(str.getBytes());
            foutput.close();
            File filedir = new File(getFilesDir(),"Host1");
            // Toast.makeText(getApplicationContext(), "Filesave" + filedir, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  String readtxtHost(){
        String tmp = "";
        try {
            FileInputStream fin = openFileInput("Host1");
            int c;
            while ((c = fin.read()) != -1){
                tmp = tmp + Character.toString((char)c);
            }
            // Toast.makeText(getApplicationContext(),tmp,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  tmp;
    }
    public void writetxt(String str)  {
        try {
            FileOutputStream foutput = openFileOutput("Host", MODE_PRIVATE);
            foutput.write(str.getBytes());
            foutput.close();
            File filedir = new File(getFilesDir(),"Host");
            // Toast.makeText(getApplicationContext(), "Filesave" + filedir, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  String readtxt(){
        String tmp = "";
        try {
            FileInputStream fin = openFileInput("Host");
            int c;
            while ((c = fin.read()) != -1){
                tmp = tmp + Character.toString((char)c);
            }
            // Toast.makeText(getApplicationContext(),tmp,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  tmp;
    }

    public void writetxtotherinfo(String str)  {
        try {
            FileOutputStream foutput = openFileOutput("otherinfo", MODE_PRIVATE);
            foutput.write(str.getBytes());
            foutput.close();
            File filedir = new File(getFilesDir(),"otherinfo");
            // Toast.makeText(getApplicationContext(), "Filesave" + filedir, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  String readtxtotherinfo(){
        String tmp = "";
        try {
            FileInputStream fin = openFileInput("otherinfo");
            int c;
            while ((c = fin.read()) != -1){
                tmp = tmp + Character.toString((char)c);
            }
            // Toast.makeText(getApplicationContext(),tmp,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  tmp;
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