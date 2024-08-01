package com.bsp.bspregistration;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
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
import java.util.stream.IntStream;


public class AASRold extends Fragment {


    EditText oldaasrnumber,oldnameofinstitution,oldaasrlocalcouncil,oldasrunitno,
            scoutcnt,scoutreptotal;

    RadioButton radioButton;
    RadioGroup radiogroup;
    AlertDialog dialog;
    LinearLayout layout;
    Button oldbtnAdd,oldmainSub;

    Loading loadingDialog;
    RadioButton radioButtonna;
    RadioGroup radiogroupnature;

    private DBHandler dbHandler;
    ArrayList<OldAddFieldHolder> oldAddFieldHolders;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_a_a_s_rold, container, false);
        dbHandler = new DBHandler(getContext(), "forupload.db");
        layout = view.findViewById(R.id.oldaddLin);
        radiogroup = (RadioGroup) view.findViewById(R.id.oldaasrradiogroup);
        radiogroupnature = (RadioGroup) view.findViewById(R.id.radiogroupnature);
        oldaasrnumber = view.findViewById(R.id.oldaasrnumber);
        oldnameofinstitution = view.findViewById(R.id.oldnameofinstitution);
        oldnameofinstitution.setText(CurrentUser.school);
        oldasrunitno = view.findViewById(R.id.oldasrunitno);
        oldasrunitno.setText(CurrentUser.unitnumber);
        oldaasrlocalcouncil = view.findViewById(R.id.oldaasrlocalcouncil);
        scoutreptotal = view.findViewById(R.id.asrscoutreptotal);
        scoutcnt = view.findViewById(R.id.scoutcnt);
        oldbtnAdd = (Button) view.findViewById(R.id.oldbtnAdd);
        oldmainSub = (Button)  view.findViewById(R.id.oldmainSub);
        oldAddFieldHolders = new ArrayList<>();
        loadingDialog = new Loading(getActivity());
        builDialog();
        oldbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builDialog();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbg);
                dialog.show();;
            }
        });
        oldmainSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialforSubmit("Are you sure to submit?", "Please click OK to submit.");
            }
        });

        warningPrivacyConsent("DATA PRIVACY CONSENT","I understand that BSP Leyte collects and uses my personal information in order for me to get registered with Boy Scouts of the Philippines. By signing this application form and all other forms attached to it, I agree that this information may be processed, shared, disclosed, transferred, or used by BSP Leyte in accordance with the Data Privacy Act if 2012, its implementing rules, and regulations.");
        return view;
    }

    public boolean checkfields(){
        boolean ret = false;

        if (String.valueOf(oldnameofinstitution.getText()).equals("") ||
                String.valueOf(oldaasrlocalcouncil.getText()).equals("") ||
                String.valueOf(oldasrunitno.getText()).equals("")){
            ret =false;
        }
        else {
            ret = true;
        }
        return ret;
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

    public void sendAPI(){

        boolean b = isconnected();
        ArrayList<OldAasrHolder> aasrHolder = new ArrayList<>();

        int radioid = radiogroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioid);

        int radioidna = radiogroupnature.getCheckedRadioButtonId();
        radioButtonna = view.findViewById(radioidna);

        if (checkfields()){


            if (radioid != -1 && radioidna != -1){
                String unitx = (String) radioButton.getText();
                String unitxx = (String) radioButtonna.getText();
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String referenceNumber = "ASR" + sdf.format(currentDate);
                OldAasrHolder aasrHolder1 = new OldAasrHolder("bsp_leyte-ddc-1989",String.valueOf(oldaasrnumber.getText()),CurrentUser.schoolID,String.valueOf(oldaasrlocalcouncil.getText()),
                        unitx,String.valueOf(oldasrunitno.getText()),unitxx,referenceNumber,oldAddFieldHolders,CurrentUser.user_id, CurrentUser.getDistrictID(), CurrentUser.getUnitGroupID());

                aasrHolder.add(aasrHolder1);
                Gson gson = new Gson();
                String json = gson.toJson(aasrHolder);
                if (b && ModeOL.isOnline){
                   // ApiHost.user_id + "
                    //StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/AndroidApi/additionalRegistration", new Response.Listener<String>() {
                  loadingDialog.startLoadingDialog();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiHost.user_id + "bsp-api/AndroidApi/additionalRegistration", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loadingDialog.dismissdialog();
                            if (response.contains("success")){


                                try{
                                    if (response.contains("success")) {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String rep = jsonObject.getString("reference_number");

                                        dbHandler.addNewData(json,String.valueOf(oldaasrnumber.getText()), rep, String.valueOf(scoutreptotal.getText()),"1");
                                        Intent intent = new Intent(getContext(), Receipt.class);
                                        intent.putExtra("REF",rep);
                                        String am = String.valueOf(scoutreptotal.getText());
                                        intent.putExtra("AMOUNT",am);
                                        intent.putExtra("FROM","");
                                        getContext().startActivity(intent);

                                        //showWarningDialogx("Congratulations", "Successfully registered, tap anywhere to close.");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                               // showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadingDialog.dismissdialog();
                            Toast.makeText(getContext(), "errror", Toast.LENGTH_LONG).show();
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
                    MyInsertAPI.getInstance(getContext().getApplicationContext()).addTorequest(stringRequest);
                }
                else {
                    dbHandler.addNewData(json,String.valueOf(oldaasrnumber.getText()), referenceNumber, String.valueOf(scoutreptotal.getText()),"0");
                    Intent intent = new Intent(getContext(), Receipt.class);
                    intent.putExtra("REF",referenceNumber);
                    String am = String.valueOf(scoutreptotal.getText());
                    intent.putExtra("AMOUNT",am);
                    intent.putExtra("FROM","");
                    getContext().startActivity(intent);
                    //showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                }
            }else {
                Toast.makeText(getContext(), "Please select your UNIT and NATURE before submitting.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Please fill all the required fields(RED LINE).", Toast.LENGTH_LONG).show();
        }
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
                sendAPI();
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

    public static final String[] genlist = new String[]{"N","RR"};
    public static final String[] genderList = new String[]{"Male","Female"};
    private void builDialog() {
        AlertDialog. Builder builder = new AlertDialog.Builder(getContext());
        View vi1 = getLayoutInflater().inflate(R.layout.oldaasrpopup, null);

        EditText tsur = vi1.findViewById(R.id.oldaasrsurname);
        EditText tfname = vi1.findViewById(R.id.oldaasrfirstname);
        EditText tfmid = vi1.findViewById(R.id.oldaasrmidname);
        EditText treg = vi1.findViewById(R.id.oldaasrreligion);
        EditText tagex = vi1.findViewById(R.id.oldaasrage);
        EditText tmemcard = vi1.findViewById(R.id.oldaasrmemcardNo);
        EditText thigh = vi1.findViewById(R.id.oldaasrhighestrank);
        EditText tyear = vi1.findViewById(R.id.oldaasrtennureinscounting);
        ImageView buttmp = vi1.findViewById(R.id.oldaasrregstatimg);
        AutoCompleteTextView tgender = vi1.findViewById(R.id.oldaasrregstat);
        ImageView gnderimg = vi1.findViewById(R.id.oldaasrgenderimg);
        AutoCompleteTextView gndertxt = vi1.findViewById(R.id.oldaasrgendertxt);

        if(leaderEdit){
            tsur.setText(oldAddFieldHolders.get(editID).getOldaasrsurname());
            tfname.setText(oldAddFieldHolders.get(editID).getOldaasrfirstname());
            tfmid.setText(oldAddFieldHolders.get(editID).getOldaasrmidname());
            treg.setText(oldAddFieldHolders.get(editID).getOldaasrreligion());
            tagex.setText(oldAddFieldHolders.get(editID).getOldaasrage());
            tmemcard.setText(oldAddFieldHolders.get(editID).getOldaasrmemcardNo());
            thigh.setText(oldAddFieldHolders.get(editID).getOldaasrhighestrank());
            tyear.setText(oldAddFieldHolders.get(editID).getOldaasrtennureinscounting());
            tgender.setText(oldAddFieldHolders.get(editID).getOldaasrregstat());
            gndertxt.setText(oldAddFieldHolders.get(editID).getGender());

        }


        tgender.setInputType(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,genlist);
        tgender.setAdapter(adapter);
        buttmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgender.showDropDown();
            }
        });
        tgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgender.showDropDown();
            }
        });

        gndertxt.setInputType(0);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,genderList);
        gndertxt.setAdapter(adapter1);
        gnderimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gndertxt.showDropDown();
            }
        });
        gndertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gndertxt.showDropDown();
            }
        });


        Button butsubx = vi1.findViewById(R.id.oldtmpsubmmit);
        Button butclox = vi1.findViewById(R.id.oldtmpclose);


        builder.setView(vi1);
        butclox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        butsubx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkems()){


                    if (!leaderEdit){



                addCard(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                        String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()),String.valueOf(gndertxt.getText()));

                OldAddFieldHolder aasrHolder = new OldAddFieldHolder(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                        String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()),String.valueOf(gndertxt.getText()));

                oldAddFieldHolders.add(aasrHolder);


                    scoutcnt.setText(String.valueOf(oldAddFieldHolders.size()));
                    scoutreptotal.setText(String.valueOf(oldAddFieldHolders.size() * 50));
                    }else {

                        oldAddFieldHolders.remove(editID);
                        layout.removeView(viewEdit);

                        addCard(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                                String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()),String.valueOf(gndertxt.getText()));

                        OldAddFieldHolder aasrHolder = new OldAddFieldHolder(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                                String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()),String.valueOf(gndertxt.getText()));

                        oldAddFieldHolders.add(aasrHolder);


                        scoutcnt.setText(String.valueOf(oldAddFieldHolders.size()));
                        scoutreptotal.setText(String.valueOf(oldAddFieldHolders.size() * 50));

                    }

                tsur.setText("");
                tfname.setText("");
                tfmid.setText("");
                treg.setText("");
                tagex.setText("");
                tgender.setText("");
                tmemcard.setText("");
                thigh.setText("");
                tyear.setText("");
                gndertxt.setText("");
                    leaderEdit = false;
                    editID = 0;
                }
                else {
                    Toast.makeText(getContext(), "Please fill all the required fields(RED LINE).", Toast.LENGTH_LONG).show();
                }
            }
            public boolean checkems(){
                boolean ret = false;

                if(String.valueOf(tsur.getText()).equals("") ||
                        String.valueOf(tfname.getText()).equals("") ||
                        String.valueOf(tagex.getText()).equals("") ||
                        String.valueOf(tgender.getText()).equals("") ||
                        String.valueOf(gndertxt.getText()).equals("") ||
                        String.valueOf(tyear.getText()).equals(""))
                {
                    ret = false;
                }else {
                    ret = true;
                }

                return  ret;
            }
        });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

    }
    private void addCard(String surname, String fname, String Mname, String reg, String agex, String gen, String memca, String thig, String tye, String gnder) {
        View v1 = getLayoutInflater().inflate(R.layout.oldaddfield, null);

        EditText tsur = v1.findViewById(R.id.toldaarsurname);
        EditText tfname = v1.findViewById(R.id.toldaarname);
        EditText tfmid = v1.findViewById(R.id.toldaarmidname);
        EditText treg = v1.findViewById(R.id.toldaasrregstatus);
        EditText tagex = v1.findViewById(R.id.toldaasrage);
        EditText tgender = v1.findViewById(R.id.toldaasrgender);
        EditText tmemcard = v1.findViewById(R.id.toldaasrmemcardnum);
        EditText thigh = v1.findViewById(R.id.toldaasrrankearned);
        EditText tyear = v1.findViewById(R.id.toldaasryearsof);
        EditText tgendertxt = v1.findViewById(R.id.toldergendertxt);


        tsur.setText(surname);
        tfname.setText(fname);
        tfmid.setText(Mname);
        treg.setText(reg);
        tagex.setText(agex);
        tgender.setText(gen);
        tmemcard.setText(memca);
        thigh.setText(thig);
        tyear.setText(tye);
        tgendertxt.setText(gnder);

        Button butclo = v1.findViewById(R.id.toldremove);
        Button butedit = v1.findViewById(R.id.toldedit);

        butedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningdialogEdit("Are you sure to Edit " + tfname.getText() + "?","Click Ok to proceed.", v1,String.valueOf(tfname.getText()));
            }
        });
        butclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningdialog("Are you sure to delete " + tsur.getText() + "?","Click Ok to proceed.", v1,String.valueOf(tsur.getText()));
            }
        });

        layout.addView(v1);
        dialog.dismiss();
    }
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                    if (dateofApp){
//                        aasrdatefees.setText(selectedDate);
//                        dateofApp = false;
//                    }

                },
                // Initial date values
                2023, 7, 11 // Year, month (0-11), day
        );
        datePickerDialog.show();
    }

    public void warningdialog(String warning, String message, View v1,String tsur) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(warning);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Add a positive button (optional)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int index = 0;
                // Toast.makeText(getContext(), tsur.getText(),Toast.LENGTH_LONG).show();
                String sda = String.valueOf(tsur);
                index = IntStream.range(0, oldAddFieldHolders.size())
                        .filter(i -> oldAddFieldHolders.get(i).getOldaasrsurname().equals(sda))
                        .findFirst()
                        .orElse(-1);
                oldAddFieldHolders.remove(index);
                layout.removeView(v1);
                scoutcnt.setText(String.valueOf(oldAddFieldHolders.size()));
                scoutreptotal.setText(String.valueOf(oldAddFieldHolders.size() * 50));
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
    boolean leaderEdit  = false;
    int editID = 0;
    View viewEdit;

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
    public void warningdialogEdit (String warning, String message, View v1,String tsur) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(warning);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Add a positive button (optional)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                leaderEdit = false;
                int index = 0;
                // Toast.makeText(getContext(), tsur.getText(),Toast.LENGTH_LONG).show();
                String sda = String.valueOf(tsur);
                index = IntStream.range(0, oldAddFieldHolders.size())
                        .filter(i -> oldAddFieldHolders.get(i).getOldaasrfirstname().equals(sda))
                        .findFirst()
                        .orElse(-1);

                if (index != -1){
                    editID = index;
                    viewEdit = v1;
                    leaderEdit = true;
                    oldbtnAdd.performClick();
                }
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

}