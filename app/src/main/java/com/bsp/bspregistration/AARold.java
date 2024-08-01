package com.bsp.bspregistration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AARold extends Fragment {


    View view;

    EditText oldaarnumber,oldaarsurname,oldaarname,oldaarmidname,oldaaroldemail,oldaardistrict,oldaarregion,oldaarserveas,oldaarsponinstitution,oldaarcounsil,oldaarmailingadd,
            oldaardateofbirth,oldaarplaceofbirth,oldaarreligion,aarprofession,oldaarposition,oldaaraffiliation,oldaarcitizenship,aaroldunitnumber,aaroldtenurescouting;
    AutoCompleteTextView oldaargender,oldaarcivil,oldscouting;
    ImageView oldaargendropimg,oldaarcivildropimg,oldscoutingimg;
    Button oldaarsub;
    Loading loadingDialog;
    RadioButton radioButton;
    RadioGroup radiogroup;

    RadioButton radioButtonna;
    RadioButton radioButtonna1;
    RadioGroup radiogroupnature,radiogroupx1;
String ammount;
    private DBHandler dbHandler;
    public static final String[] genlist = new String[]{"Male","Female"};
    public static final String[] civillist = new String[]{"Single","Separated", "Merried", "Divorced",  "Widowed"};
    public  String[] scoutpos = new String[]{"INSTITUTIONAL SCOUTING  REPRESENTATIVE (ISR) ' 100",
            "INSTITUTIONAL SCOUTING  COORDINATOR (ISC) ' 100",
            "INSTITUTIONAL COMMITTEE  CHAIRMAN (ICC) ' 100",
            "DISTRICT SCOUTNG COMMISSIONER (DSC) ' 100",
            "COUNCIL STAFF (CS) ' 100",
            "COUNCIL SCOUTER ' 100",
            "DISTRICT/MUNICIPALITY  COMMISSIONER/COORDINATOR ' 100",
            "DISTRICT/MUNICIPALITY CHAIRMAN ' 100",
            "DMAL/CMAL ' 100",
            "ROVER PEERS ' 100",
            "CSE/FSE/OICS ' 200",
            "LANGKAY LEADER / ASSISTANT ' 60",
            "KAWAN LEADER / ASSISTANT ' 60",
            "TROOP LEADER / ASSISTANT ' 60",
            "OUTFIT ADVISOR / ASSISTANT ' 60",
            "CIRCLE ADVISOR / ASSISTANT ' 60",
            "CBS-UL ' 60",
            "COUNCIL CHAIRMAN ' 500",
            "COUNCIL COMMISSIONER ' 200",
            "LCEB MEMBER ' 200"};




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_a_a_rold, container, false);

        oldaarcitizenship = (EditText) view.findViewById(R.id.oldaarcitizenship);
        oldaarsub = (Button) view.findViewById(R.id.oldaarsub);
        radiogroup = view.findViewById(R.id.aaroldradiogroup);
        radiogroupnature = view.findViewById(R.id.radiogroupx);
        radiogroupx1 = view.findViewById(R.id.radiogroupx1);
        oldaarnumber = (EditText) view.findViewById(R.id.oldaarnumber);
        oldaarsurname = (EditText) view.findViewById(R.id.oldaarsurname);
        oldaarname = (EditText) view.findViewById(R.id.oldaarname);
        oldaarmidname = (EditText) view.findViewById(R.id.oldaarmidname);
        oldaaroldemail = (EditText) view.findViewById(R.id.oldaaroldemail);
        oldaardistrict = (EditText) view.findViewById(R.id.oldaardistrict);
        oldaarregion = (EditText) view.findViewById(R.id.oldaarregion);
        //oldaarserveas = (EditText) view.findViewById(R.id.oldaarserveas);
        oldaarsponinstitution = (EditText) view.findViewById(R.id.oldaarsponinstitution);
        oldaarcounsil = (EditText) view.findViewById(R.id.oldaarcounsil);
        oldaarmailingadd = (EditText) view.findViewById(R.id.oldaarmailingadd);
        oldaarreligion = (EditText) view.findViewById(R.id.oldaarreligion);
        aarprofession = (EditText) view.findViewById(R.id.aarprofession);
        oldaarposition = (EditText) view.findViewById(R.id.oldaarposition);
        oldaaraffiliation = (EditText) view.findViewById(R.id.oldaaraffiliation);
        oldaarplaceofbirth = (EditText) view.findViewById(R.id.oldaarplaceofbirth);
        aaroldunitnumber = (EditText) view.findViewById(R.id.aaroldunitnumber);
        aaroldtenurescouting = (EditText) view.findViewById(R.id.aaroldtenurescouting);

        oldaargender = (AutoCompleteTextView) view.findViewById(R.id.oldaargender);
        oldaarcivil = (AutoCompleteTextView) view.findViewById(R.id.oldaarcivil);
        oldscouting = (AutoCompleteTextView) view.findViewById(R.id.oldscouting);

        oldaargendropimg = (ImageView) view.findViewById(R.id.oldaargendropimg);
        oldaarcivildropimg = (ImageView) view.findViewById(R.id.oldaarcivildropimg);
        oldscoutingimg = (ImageView) view.findViewById(R.id.oldscoutingimg);

        oldaarsponinstitution.setText(CurrentUser.school);
        oldaardistrict.setText(CurrentUser.district);
        aaroldunitnumber.setText(CurrentUser.unitnumber);
        loadingDialog = new Loading(getActivity());
        dbHandler = new DBHandler(getContext(), "forupload.db");
        insertDefaultPosition();
            scoutpos = dbHandler.selectpos();

        ammount = "";
        isOver = "";
        radiogroupx1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioid2 = radiogroupx1.getCheckedRadioButtonId();
                radioButtonna1 = view.findViewById(radioid2);

                String unitxx1 = (String) radioButtonna1.getText();
                if (unitxx1.equals("New")){
                    aaroldtenurescouting.setText("0");
                    aaroldtenurescouting.setEnabled(false);
                }else {
                    aaroldtenurescouting.setText("");
                    aaroldtenurescouting.setEnabled(true);
                }
            }
        });
        oldscouting.setInputType(0);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,scoutpos);
        oldscouting.setAdapter(adapter3);
        oldscoutingimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldscouting.showDropDown();
            }
        });
        oldscouting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldscouting.showDropDown();
            }
        });


        oldaargender.setInputType(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,genlist);
        oldaargender.setAdapter(adapter);
        oldaargendropimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldaargender.showDropDown();
            }
        });
        oldaargender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldaargender.showDropDown();
            }
        });


        oldaarcivil.setInputType(0);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,civillist);
        oldaarcivil.setAdapter(adapter1);
        oldaarcivildropimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldaarcivil.showDropDown();
            }
        });
        oldaarcivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldaarcivil.showDropDown();
            }
        });


        oldaardateofbirth = (EditText) view.findViewById(R.id.oldaardateofbirth);
        oldaardateofbirth.setInputType(0);
        oldaardateofbirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDatePicker();
                }
            }
        });

        oldaarsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialforSubmit("Are you sure to submit?", "Please click OK to submit.");


            }
        });

        warningPrivacyConsent("DATA PRIVACY CONSENT","I understand that BSP Leyte collects and uses my personal information in order for me to get registered with Boy Scouts of the Philippines. By signing this application form and all other forms attached to it, I agree that this information may be processed, shared, disclosed, transferred, or used by BSP Leyte in accordance with the Data Privacy Act if 2012, its implementing rules, and regulations.");

        return view;
    }

    public void insertDefaultPosition(){
        for (int i = 0; i < scoutpos.length; i++){

            String str[] = scoutpos[i].split("'");

            dbHandler.addNewposition(str[0].trim(),str[1].trim(),"0","0");

        }

    }
    public void warningPrivacyConsent (String warning, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(warning);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        final boolean[] ifcon = {false};
        // Add a positive button (optional)
        builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ifcon[0] = true;
            }
        });

        // Add a negative button (optional)
        builder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code to be executed when the user clicks the "Cancel" button
                // (optional, you can leave this empty or remove it if not needed)
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!ifcon[0]){
                    Intent intent = new Intent(getContext(), SplashScreenx.class);
                    getContext().startActivity(intent);
                }
            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void dialforSubmit(String warning, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(warning);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Add a positive button (optional)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isOver = "";
                sendAPI();
//                if (!isOver.equals("")){
//                    sendAPI();
//                }
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

            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
String isOver = "";

    public void sendAPI(){
        ArrayList<AarOldHolder> aarOldHolders = new ArrayList<>();
        boolean b = isconnected();

        int radioid = radiogroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioid);

        int radioid1 = radiogroupnature.getCheckedRadioButtonId();
        radioButtonna = view.findViewById(radioid1);

        int radioid2 = radiogroupx1.getCheckedRadioButtonId();
        radioButtonna1 = view.findViewById(radioid2);

        if (checkfields()) {

            if (radioid != -1 && radioid1 != -1 && radioid2 != -1) {

                String unitx = (String) radioButton.getText();
                String unitxx = (String) radioButtonna.getText();
                String unitxx1 = (String) radioButtonna1.getText();

                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String referenceNumber = "AAR" + sdf.format(currentDate);
                String[] scoutix = String.valueOf(oldscouting.getText()).split("'");
                ammount = dbHandler.selectprice(scoutix[0].trim());

                AarOldHolder aarOldHolder = new AarOldHolder("bsp_leyte-ddc-1989", String.valueOf(oldaarnumber.getText()), String.valueOf(oldaarsurname.getText()), String.valueOf(oldaarname.getText()),
                        String.valueOf(oldaarmidname.getText()), String.valueOf(oldaaroldemail.getText()), CurrentUser.districtID, String.valueOf(oldaarregion.getText()), scoutix[0].trim(),
                        CurrentUser.schoolID, String.valueOf(oldaarcounsil.getText()), String.valueOf(oldaarmailingadd.getText()), String.valueOf(oldaardateofbirth.getText()), String.valueOf(oldaarplaceofbirth.getText()),
                        String.valueOf(oldaarreligion.getText()), String.valueOf(aarprofession.getText()), String.valueOf(oldaarposition.getText()), String.valueOf(oldaaraffiliation.getText()), unitx,
                        String.valueOf(oldaargender.getText()), String.valueOf(oldaarcivil.getText()), String.valueOf(oldaarcitizenship.getText()),unitxx, CurrentUser.unitnumber,referenceNumber,CurrentUser.user_id, unitxx1, String.valueOf(aaroldtenurescouting.getText()), ammount,CurrentUser.getUnitGroupID(),isOver);
                aarOldHolders.add(aarOldHolder);
                Gson gson = new Gson();
                String json = gson.toJson(aarOldHolders);
                if (b && ModeOL.isOnline){

                   // StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/AndroidApi/adultRegistration", new Response.Listener<String>() {
                       loadingDialog.startLoadingDialog();


                        StringRequest stringRequest = new StringRequest(Request.Method.POST,  ApiHost.user_id + "bsp-api/AndroidApi/adultRegistration", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                loadingDialog.dismissdialog();
                                
                                if (response.contains("success") ) {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String rep = jsonObject.getString("reference_number");
                                    if (!rep.equals("null")){
                                        Intent intent = new Intent(getContext(), Receipt.class);
                                        dbHandler.addNewData(json, String.valueOf(oldaarnumber.getText()),  rep, ammount,"1");
                                        intent.putExtra("REF",rep);
                                        intent.putExtra("AMOUNT",ammount);
                                        intent.putExtra("FROM","");
                                        isOver = "";
                                        getContext().startActivity(intent);
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
                                    String cudID = dupsInfo.getString("curId");

//                                     curpos = position;Mr/Ms Jereco Adonis is currently assigned to you as your INSTITUTIONAL SCOUTING REPRESENTATIVE (ISR). Do you wish to change this person to Mr/Ms Sherwin Ravaya?
                                    String msgx = "You have an existing " + position + " assign which is Mr/Ms " + curName + " do you want to change the position holder Mr/Ms " + newName + "?";
                                    String msxg = "Mr/Ms " + curName + " is currently assigned to you as your " + position + ". Do you wish to change this person to Mr/Ms " + newName +"?";
                                    askdup("Position already taken",msxg, cudID);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadingDialog.dismissdialog();
                            String sad = error.getMessage();
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("data", json);
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
                    MyInsertAPI.getInstance(getContext().getApplicationContext()).addTorequest(stringRequest);

                } else {
                    dbHandler.addNewData(json, String.valueOf(oldaarnumber.getText()),  referenceNumber, ammount,"0");
                    Intent intent = new Intent(getContext(), Receipt.class);
                    intent.putExtra("REF",referenceNumber);
                    intent.putExtra("AMOUNT",ammount);
                    intent.putExtra("FROM","");
                    getContext().startActivity(intent);
                   // showWarningDialogx("Congratulations", "Successfully registered, tap anywhere to close.");
                }
            } else {
                Toast.makeText(getContext(), "Please select your UNIT, NATURE and Register status before submitting.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Please fill all the required fields(RED LINE).", Toast.LENGTH_LONG).show();
        }
    }
    public Boolean isconnected(){
        boolean ret = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public boolean checkfields(){
        boolean ret = false;

        if (
                String.valueOf(oldaarsurname.getText()).equals("") ||
                String.valueOf(oldaarname.getText()).equals("")  ||
                String.valueOf(oldaardistrict.getText()).equals("") ||
                String.valueOf(oldaarregion.getText()).equals("") ||
                String.valueOf(oldaarsponinstitution.getText()).equals("") ||
                String.valueOf(oldaarcounsil.getText()).equals("") ||
                String.valueOf(oldaarmailingadd.getText()).equals("") ||
                String.valueOf(oldaardateofbirth.getText()).equals("") ||
                String.valueOf(oldaarplaceofbirth.getText()).equals("") ||
                String.valueOf(oldaargender.getText()).equals("") ||
                String.valueOf(oldaarcivil.getText()).equals("") ||
                String.valueOf(aaroldunitnumber.getText()).equals("") ||
                String.valueOf(oldaarcitizenship.getText()).equals("") ||
                        String.valueOf(aaroldtenurescouting.getText()).equals("") ||
                        String.valueOf(oldscouting.getText()).equals("") ||
                        String.valueOf(aarprofession.getText()).equals("") ||
                        String.valueOf(oldaarposition.getText()).equals("")
        )
        {
            ret = false;
        }
        else
        {
            ret = true;
        }
        return ret;
    }
    public void askdup(String warning, String message, String fuln) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(warning);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        // Add a positive button (optional)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isOver = "yes*" + fuln;
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
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
                getActivity().finish();
            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbggreen);

        dialog.show();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        oldaardateofbirth.setText(selectedDate);
                },
                // Initial date values
                2023, 7, 11 // Year, month (0-11), day
        );
        datePickerDialog.show();
    }

}