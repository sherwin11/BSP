package com.bsp.bspregistration;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.MyViewHolder> {
    private Context mContext;
    static ArrayList<UploadList> uploadLists;
    View view;
    DBHandler dbHandler;

    Loading loadingDialog;
    public UploadAdapter(Context mcontext, ArrayList<UploadList> uploadLists){
            this.mContext = mcontext;
            this.uploadLists = uploadLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(mContext).inflate(R.layout.unit_item,parent,false);
            dbHandler = new DBHandler(mContext, "forupload.db");
            positionx = new ArrayList<>();
            curnamex = new ArrayList<>();
            curidx = new ArrayList<>();
            return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.textView.setText("Temp. Ref #: " + uploadLists.get(position).getTempref());

        }

    @Override
    public int getItemCount() {
        return uploadLists.size();
    }
    String isOver = "";
    String idxx = "";
    String site = "";
    ArrayList<String> positionx;
    ArrayList<String> curnamex;
    ArrayList<String> curidx;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView textView;


        public MyViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.folderImg);
            textView = itemView.findViewById(R.id.unitName);
            loadingDialog = new Loading((Activity) mContext);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendAPI();
                }
                public void sendAPI(){
                    boolean b = isconnected();
                    if (b ){
                        if(ModeOL.isOnline){
                            boolean isoveryet = false;
                            String temprex = uploadLists.get(getAdapterPosition()).getTempref();
                            String ans = dbHandler.selecttomain(temprex);
                            String cary = "";


                            //Toast.makeText(mContext, temprex, Toast.LENGTH_LONG).show();
                            String urllink = "";
                            site = "";
                            if (temprex.contains("ASR")){

                                if(CurrentUser.getHasaur().equals("true")){
//                                    if(CurrentUser.getIspaid().equals("false")){
                                        site = "ASR";
                                        urllink = ApiHost.user_id + "bsp-api/AndroidApi/additionalRegistration";
//                                    }else {
//                                        showWarningDialogx("Payment","Your Application for Unit Registration (AUR) is already paid. Adding members is  not available using ASR form.");
//                                    }
                                }else {
                                    showWarningDialogx("Unit","You need to register a unit (AUR) first to register additional registration.");
                                }
                            } else if(temprex.contains("AAR")) {
                                if (CurrentUser.getHasaur().equals("true")){
                                    if (isOver.contains("yes")){
                                        try {

                                            // Convert the string to a JSONArray
                                            JSONArray jsonArray = new JSONArray(ans);

                                            // Iterate through each object in the array
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                // Update the value of "overwrite" field
                                                jsonObject.put("overwrite", isOver);
                                            }
                                            String modifiedString = jsonArray.toString();
                                            cary = modifiedString;
                                            isoveryet = true;
                                            // Print the modified JSON array
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        ans = cary;
                                    }
                                    site = "AAR";
                                    urllink = ApiHost.user_id + "bsp-api/AndroidApi/adultRegistration";
                                }else
                                {
                                    showWarningDialogx("Unit","You need to register a unit (AUR) first to register adult registration.");
                                }
                            } else if(temprex.contains("AUR")){
                                if(CurrentUser.getHasaur().equals("false")){
                                    if (isOver.contains("yes")){
                                        try {
                                            // Convert the string to a JSONArray
                                            JSONArray jsonArray = new JSONArray(ans);

                                            // Iterate through each object in the array
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                // Update the value of "overwrite" field
                                                jsonObject.put("overwrite", isOver);
                                            }
                                            String modifiedString = jsonArray.toString();
                                            cary = modifiedString;
                                            isoveryet = true;
                                            // Print the modified JSON array
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        ans = cary;
                                    }

                                    site = "AUR";
                                    urllink = ApiHost.user_id + "bsp-api/AndroidApi/unitRegistration";
                                }else {
                                    showWarningDialogx("Unit","You have already register an AUR.");
                                }
                            }
                            if(!urllink.equals("")){

                                loadingDialog.startLoadingDialog();
                                String finalUrllink = urllink;
                                String finalAns = ans;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, finalUrllink, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        loadingDialog.dismissdialog();
                                        if(site.contains("AAR")){
                                            try{
                                                if (response.contains("success") ) {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String rep = jsonObject.getString("reference_number");
                                                    String am = dbHandler.selectamount(temprex);
                                                    dbHandler.updateAnswetoUploaded(finalAns,rep, isOver);
                                                    Foruploads.deleteItem(getAdapterPosition());
                                                    if (!rep.equals("null")){
                                                        Intent intent = new Intent(mContext, Receipt.class);
                                                        intent.putExtra("REF",rep);
                                                        intent.putExtra("AMOUNT",am);
                                                        intent.putExtra("FROM","");
                                                        isOver = "";
                                                   positionx = new ArrayList<>();
                                                       curnamex = new ArrayList<>();
                                                         curidx = new ArrayList<>();
                                                        mContext.startActivity(intent);
                                                    }
                                                    //showWarningDialogx("Congratulations", "Successfully registered, tap anywhere to close.");
                                                } else if (response.contains("duplicate")) {
                                                    // Parse the JSON string
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    JSONObject dupsInfo = jsonObject.getJSONObject("dupsInfo");

                                                    // Retrieve the values
                                                    String newName = dupsInfo.getString("newName");
                                                    String position = dupsInfo.getString("position");
                                                    String curName = dupsInfo.getString("curName");
                                                    String idxx = dupsInfo.getString("curId");


//                                     curpos = position;Mr/Ms Jereco Adonis is currently assigned to you as your INSTITUTIONAL SCOUTING REPRESENTATIVE (ISR). Do you wish to change this person to Mr/Ms Sherwin Ravaya?
                                                    String msgx = "You have an existing " + position + " assign which is Mr/Ms " + curName + " do you want to change the position holder Mr/Ms " + newName + "?";
                                                    String msxg = "Mr/Ms " + curName + " is currently assigned to you as your " + position + ". Do you wish to change this person to Mr/Ms " + newName +"?";
                                                    askdup("Position already taken",msxg, idxx);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else if (site.contains("AUR")) {
                                            try{

                                                loadingDialog.dismissdialog();
                                                if (response.contains("success")){
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String rep = jsonObject.getString("reference_number");
                                                        String am = dbHandler.selectamount(temprex);
                                                        dbHandler.updateAnswetoUploaded(finalAns,rep, isOver);
                                                        Foruploads.deleteItem(getAdapterPosition());
                                                        if (!rep.equals("null")){
                                                            Intent intent = new Intent(mContext, Receipt.class);
                                                            intent.putExtra("REF",rep);
                                                            intent.putExtra("AMOUNT",am);
                                                            intent.putExtra("FROM","");
                                                            isOver = "";
                                                            mContext.startActivity(intent);
                                                        }
                                                        //showWarningDialogx("Congratulations", "Successfully registered, tap anywhere to close.");
                                                    // showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                                                }else if (response.contains("duplicate")) {

                                                    try {
                                                        // Parse the JSON string
                                                        JSONObject jsonObject = new JSONObject(response);

                                                        // Get the "dupsInfo" array
                                                        JSONArray dupsInfoArray = jsonObject.getJSONArray("dupsInfo");



                                                        // Iterate over each object in the array
                                                        for (int i = 0; i < dupsInfoArray.length(); i++) {
                                                            JSONObject dupObject = dupsInfoArray.getJSONObject(i);

                                                            // Get the value of the "dup" tag
                                                            boolean isDup = dupObject.getBoolean("dup");

                                                            curnamex.add(dupObject.getString("curName"));
                                                            curidx.add(dupObject.getString("curId"));
                                                            positionx.add(dupObject.getString("position"));
                                                            String sadsd = positionx.get(0);
                                                            boolean isDup2 = dupObject.getBoolean("dup");
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    String dupx = "";
                                                    for (int i = 0; i < curnamex.size(); i++){
                                                        dupx += curnamex.get(i) + " - " +  positionx.get(i) + "\n";
                                                    }
                                                    String msxg = "";
                                                    if(curnamex.size() == 1){
                                                        msxg = "The position you've selected is already taken by \n" + dupx + " do you want us to replace?";
                                                    }else {
                                                        msxg = "The position you've selected is already taken by \n" + dupx + " do you want us to replace them?";
                                                    }
                                                    askdup1("Position already taken",msxg);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            try{
                                                JSONObject jsonObject = new JSONObject(response);
                                                String rep = jsonObject.getString("reference_number");
                                                if (response.contains("success")){
                                                    String am = dbHandler.selectamount(temprex);
                                                    dbHandler.updateAnswetoUploaded(finalAns,rep, "");
                                                    Foruploads.deleteItem(getAdapterPosition());

                                                    Intent intent = new Intent(mContext, Receipt.class);
                                                    intent.putExtra("REF",rep);
                                                    intent.putExtra("AMOUNT",am);
                                                    intent.putExtra("FROM","his");
                                                    mContext.startActivity(intent);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loadingDialog.dismissdialog();
                                        //Toast.makeText(getApplicationContext(), "errror", Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams()throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("data", finalAns);
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
                                MyInsertAPI.getInstance(mContext).addTorequest(stringRequest);
                            }

                        }
                        else {
                            showWarningDialogx("Login issue.", "You can only upload once you login.");
                        }
                    }else {
                        showWarningDialogx("Connection issue.", "Unable to connect to internet.");
                    }
                }
                public void askdup(String warning, String message,String nnname) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    builder.setTitle(warning);
                    builder.setMessage(message);
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setCancelable(false);
                    // Add a positive button (optional)
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isOver = "yes*" + nnname;
                            sendAPI();
                            // Code to be executed when the user clicks the "OK" button
                            // (optional, you can leave this empty or remove it if not needed)
                        }
                    });

                    // Add a negative button (optional)
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

                        }
                    });
                    //// Create and show the AlertDialog
                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dbwar);
                    dialog.show();
                }
                public void askdup1(String warning, String message) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    builder.setTitle(warning);
                    builder.setMessage(message);
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setCancelable(false);
                    // Add a positive button (optional)
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isOver = "yes";
                            for (String str: curidx) {
                                isOver += "*" + str;
                            }
                            String sadasd = isOver;
                            sendAPI();
                            // Code to be executed when the user clicks the "OK" button
                            // (optional, you can leave this empty or remove it if not needed)
                        }
                    });

                    // Add a negative button (optional)
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
//                getActivity().finish();
                        }
                    });
                    //// Create and show the AlertDialog
                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dbwar);
                    dialog.show();
                }

                public void showWarningDialogx(String warning, String message) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
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
                public Boolean isconnected(){
                    boolean ret = false;
                    try {
                        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
            });
        }


    }
}
