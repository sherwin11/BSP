package com.bsp.bspregistration;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;


public class AURold extends Fragment {


    View view;

    EditText aurnumber,aurinstitution,aurdistrict,aurcounsil,aurunitnumber;

    Loading loadingDialog;
    RadioButton radioButton;
    RadioGroup radiogroup;
    private DBHandler dbHandler;
    AlertDialog dialog;
    AlertDialog dialogrost;
    LinearLayout layout;
    LinearLayout layoutrost;
    Button aurbtnadd,aurrostadd,aursubmit;

    EditText scoutrepcnt,scoutreptotal,iscchair,iscchairtotal,unitasscnt,unitasstotal,scountcnt,scounttotal,totalfees,isctotal,isccnt,parentreptotal,parentrepcnt;

    ArrayList<AdultLeaders> adultLeaders;
    ArrayList<RosterScoutmember> rosterScoutmembers;

    RadioButton radioButtonna;
    RadioGroup radiogroupnature;

    int totalfee = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_a_u_rold, container, false);
        dbHandler = new DBHandler(getContext(), "forupload.db");
        layout = view.findViewById(R.id.adultaddlin);
        layoutrost = view.findViewById(R.id.aurrostlin);
        radiogroup = (RadioGroup) view.findViewById(R.id.aurradiogroup);
        radiogroupnature = (RadioGroup) view.findViewById(R.id.radiogroupnature);
        aurbtnadd = (Button) view.findViewById(R.id.aurbtnadd);
        aurnumber = (EditText) view.findViewById(R.id.aurnumber);
        aurinstitution = (EditText) view.findViewById(R.id.aurinstitution);
        aurdistrict = (EditText) view.findViewById(R.id.aurdistrict);
        aurcounsil = (EditText) view.findViewById(R.id.aurcounsil);
        aursubmit = (Button) view.findViewById(R.id.aursubmit);
        adultLeaders = new ArrayList<>();
        rosterScoutmembers = new ArrayList<>();
        scoutrepcnt = (EditText) view.findViewById(R.id.scoutrepcnt);
        scoutreptotal = (EditText) view.findViewById(R.id.scoutreptotal);
        iscchair = (EditText) view.findViewById(R.id.iscchair);
        iscchairtotal = (EditText) view.findViewById(R.id.iscchairtotal);
        unitasstotal = (EditText) view.findViewById(R.id.unitasstotal);
        unitasscnt = (EditText) view.findViewById(R.id.unitasscnt);
        scounttotal = (EditText) view.findViewById(R.id.scounttotal);
        scountcnt = (EditText) view.findViewById(R.id.scountcnt);
        totalfees = (EditText) view.findViewById(R.id.totalfees);
        aurunitnumber = (EditText) view.findViewById(R.id.aurunitnumber);
        isctotal = (EditText) view.findViewById(R.id.isctotal);
                isccnt = (EditText) view.findViewById(R.id.isccnt);
        parentreptotal = (EditText) view.findViewById(R.id.parentreptotal);
                parentrepcnt = (EditText) view.findViewById(R.id.parentrepcnt);
        aurunitnumber.setText(CurrentUser.unitnumber);
        aurdistrict.setText(CurrentUser.district);
        aurinstitution.setText(CurrentUser.school);

        loadingDialog = new Loading(getActivity());
         leaderEdit = false;
        isOver = "";
         editID = 0;
        builDialog();
        aurrostadd = (Button) view.findViewById(R.id.aurrostadd);

        aurbtnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builDialog();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbg);
                dialog.show();;
            }
        });
        builDialogRost();
        aurrostadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builDialogRost();
                dialogrost.getWindow().setBackgroundDrawableResource(R.drawable.dialbg);
                dialogrost.show();
            }
        });


        aursubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMainSub()){
                    dialforSubmit("Are you sure to submit?", "Please click OK to submit.");
                }else {
                    Toast.makeText(getContext(),"Please fill all the required fields(RED LINE).", Toast.LENGTH_LONG).show();
                }
            }
        });
        warningPrivacyConsent("DATA PRIVACY CONSENT","I understand that BSP Leyte collects and uses my personal information in order for me to get registered with Boy Scouts of the Philippines. By signing this application form and all other forms attached to it, I agree that this information may be processed, shared, disclosed, transferred, or used by BSP Leyte in accordance with the Data Privacy Act if 2012, its implementing rules, and regulations.");
        return view;
    }
    public boolean checkfields(){
        boolean ret = false;
        if ( String.valueOf(aurinstitution.getText()).equals("") ||
                String.valueOf(aurdistrict.getText()).equals("") || String.valueOf(aurcounsil.getText()).equals("")){
            ret = false;
        }
        else
        {
            ret = true;
        }
        return ret;
    }
    public static final String[] rosterpositionlist = new String[]{"SPL/SCL/RL","ARL(CIRCLE)","AUDITOR(CIRCLE)","SCRIBE/SECRETARY","TREASURER","QUARTERMASTER","MEMBER"};
    public static final String[] reglist = new String[]{"N","RR"};

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
                getActivity().finish();
            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbggreen);

        dialog.show();
    }

    public static final String[] genlist = new String[]{"Instl. Scouting Rep",
            "Parent Representative",
            "ISC Chair/Coor/Memb",
            "Inst'l. Scouting Coordinator",
            "Assistant Unit Leader/ACA"};
    public static final String[] genderList = new String[]{"Male",
            "Female"};
    boolean leaderEdit = false;
    int editID = 0;
    View viewEdit;

    boolean leaderEditRost = false;
    int editIDRost = 0;
    View viewEditRost;
    private void builDialog() {
        AlertDialog. Builder builder = new AlertDialog.Builder(getContext());
        View vi1 = getLayoutInflater().inflate(R.layout.adultleaderspopup, null);

        EditText tsur = vi1.findViewById(R.id.aursurname);
        EditText tfname = vi1.findViewById(R.id.aurfirstname);
        EditText tfmid = vi1.findViewById(R.id.aurmidname);
        EditText treg = vi1.findViewById(R.id.aurreligion);
        EditText tagex = vi1.findViewById(R.id.aurage);
        EditText tmemcard = vi1.findViewById(R.id.aurmemcard);

        EditText thigh = vi1.findViewById(R.id.aurhighestrank);
        EditText tyear = vi1.findViewById(R.id.aurscouting);
        ImageView buttmp = vi1.findViewById(R.id.aurpositionimg);
        AutoCompleteTextView tgender = vi1.findViewById(R.id.aurposition);
        ImageView genderpic = vi1.findViewById(R.id.aurgenderpic);
        AutoCompleteTextView gendertxt = vi1.findViewById(R.id.aurgender);

        if (leaderEdit){
            tsur.setText(adultLeaders.get(editID).getAursurname());
            tfname.setText(adultLeaders.get(editID).getAurfirstname());
            tfmid.setText(adultLeaders.get(editID).getAurmidname());
            treg.setText(adultLeaders.get(editID).getAurreligion());
            tagex.setText(adultLeaders.get(editID).getAurage());
            tmemcard.setText(adultLeaders.get(editID).getAurmemcard());
            thigh.setText(adultLeaders.get(editID).getAurhighestrank());
            tyear.setText(adultLeaders.get(editID).getAurscouting());
            tgender.setText(adultLeaders.get(editID).getAurposition());
            gendertxt.setText(adultLeaders.get(editID).getGender());
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

        gendertxt.setInputType(0);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,genderList);
        gendertxt.setAdapter(adapter1);
        genderpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gendertxt.showDropDown();
            }
        });
        gendertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gendertxt.showDropDown();
            }
        });


        Button butsubx = vi1.findViewById(R.id.auradsubmit);
        Button butclox = vi1.findViewById(R.id.auradclose);


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


                if(checkPos(String.valueOf(tgender.getText()))){



                if (checkems()){

                    if(!leaderEdit){


                addCard(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                        String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()), String.valueOf(gendertxt.getText()));

                AdultLeaders aasrHolder = new AdultLeaders(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),
                        String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                        String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),
                        String.valueOf(tyear.getText()),String.valueOf(gendertxt.getText()));
                adultLeaders.add(aasrHolder);



                    long scoutrep = adultLeaders.stream()
                            .filter(person -> person.getAurposition().equalsIgnoreCase("Instl. Scouting Rep"))
                            .count();
                    if (scoutrep != 0){
                        scoutrepcnt.setText(String.valueOf((int) scoutrep));
                    }else {
                        scoutrepcnt.setText("0");
                    }
                    scoutreptotal.setText(String.valueOf((int)(100 * scoutrep)));

                    long isc = adultLeaders.stream()
                            .filter(person -> person.getAurposition().equalsIgnoreCase("ISC Chair/Coor/Memb"))
                            .count();

                    if (isc != 0){
                        iscchair.setText(String.valueOf((int) isc));
                    }else {
                        iscchair.setText("0");
                    }
                    iscchairtotal.setText(String.valueOf((int)(100 * isc)));

                    long un1 = adultLeaders.stream()
                            .filter(person -> person.getAurposition().equalsIgnoreCase("Assistant Unit Leader/ACA"))
                            .count();
                    long un2 = adultLeaders.stream()
                            .filter(person -> person.getAurposition().equalsIgnoreCase("Unit Leader/Circle Adviser"))
                            .count();
                    if (un1 != 0 || un2 != 0){
                        unitasscnt.setText(String.valueOf((int) un1 + un2));
                    }else {
                        unitasscnt.setText("0");
                    }
                    unitasstotal.setText(String.valueOf((int)(60 * ((int) un1 + (int) un2))));

                        long ppr = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("Parent Representative"))
                                .count();

                        if (ppr != 0){
                            parentrepcnt.setText(String.valueOf((int) ppr));
                        }
                        else {
                            parentrepcnt.setText("0");
                        }
                        parentreptotal.setText(String.valueOf((int)(100 * ppr)));

                        long iscc = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("Inst'l. Scouting Coordinator"))
                                .count();

                        if (iscc != 0){
                            isccnt.setText(String.valueOf((int) iscc));
                        }
                        else {
                            isccnt.setText("0");
                        }
                        isctotal.setText(String.valueOf((int)(100 * iscc)));


                    totalfees.setText(String.valueOf( Integer.valueOf( String.valueOf(isctotal.getText())) + Integer.valueOf( String.valueOf(parentreptotal.getText())) + Integer.valueOf( String.valueOf(unitasstotal.getText())) + Integer.valueOf( String.valueOf(iscchairtotal.getText())) + Integer.valueOf( String.valueOf(scoutreptotal.getText())) + Integer.valueOf( String.valueOf(scounttotal.getText()))));
                    }
                    else {


                        adultLeaders.remove(editID);
                        layout.removeView(viewEdit);


                        addCard(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                                String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()), String.valueOf(gendertxt.getText()));

                        AdultLeaders aasrHolder = new AdultLeaders(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),
                                String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                                String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),
                                String.valueOf(tyear.getText()),String.valueOf(gendertxt.getText()));
                        adultLeaders.add(aasrHolder);



                        long scoutrep = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("Instl. Scouting Rep"))
                                .count();
                        if (scoutrep != 0){
                            scoutrepcnt.setText(String.valueOf((int) scoutrep));
                        }else {
                            scoutrepcnt.setText("0");
                        }
                        scoutreptotal.setText(String.valueOf((int)(100 * scoutrep)));

                        long isc = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("ISC Chair/Coor/Memb"))
                                .count();

                        if (isc != 0){
                            iscchair.setText(String.valueOf((int) isc));
                        }
                        else {
                            iscchair.setText("0");
                        }
                        iscchairtotal.setText(String.valueOf((int)(100 * isc)));

                        long un1 = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("Assistant Unit Leader/ACA"))
                                .count();
                        long un2 = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("Unit Leader/Circle Adviser"))
                                .count();
                        if (un1 != 0 || un2 != 0){
                            unitasscnt.setText(String.valueOf((int) un1 + un2));
                        }else {
                            unitasscnt.setText("0");
                        }
                        unitasstotal.setText(String.valueOf((int)(60 * ((int) un1 + (int) un2))));


                        long ppr = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("Parent Representative"))
                                .count();

                        if (ppr != 0){
                            parentrepcnt.setText(String.valueOf((int) ppr));
                        }
                        else {
                            parentrepcnt.setText("0");
                        }
                        parentreptotal.setText(String.valueOf((int)(100 * ppr)));

                        long iscc = adultLeaders.stream()
                                .filter(person -> person.getAurposition().equalsIgnoreCase("Inst'l. Scouting Coordinator"))
                                .count();

                        if (iscc != 0){
                            isccnt.setText(String.valueOf((int) iscc));
                        }
                        else {
                            isccnt.setText("0");
                        }
                        isctotal.setText(String.valueOf((int)(100 * iscc)));

                        totalfees.setText(String.valueOf( Integer.valueOf( String.valueOf(isctotal.getText())) + Integer.valueOf( String.valueOf(parentreptotal.getText())) + Integer.valueOf( String.valueOf(unitasstotal.getText())) + Integer.valueOf( String.valueOf(iscchairtotal.getText())) + Integer.valueOf( String.valueOf(scoutreptotal.getText())) + Integer.valueOf( String.valueOf(scounttotal.getText()))));

                    }



                    leaderEdit = false;
                    editID = 0;
                tsur.setText("");
                tfname.setText("");
                tfmid.setText("");
                treg.setText("");
                tagex.setText("");
                tgender.setText("");
                tmemcard.setText("");
                thigh.setText("");
                tyear.setText("");
                gendertxt.setText("");
                }
                else {
                    String k = msgRequired(String.valueOf(tsur.getText()),
                            String.valueOf(tfname.getText()),String.valueOf(tagex.getText()),
                            String.valueOf(tyear.getText()), String.valueOf(gendertxt.getText()));

                    Toast.makeText(getContext(), " Please fill all the required fields(RED LINE).", Toast.LENGTH_LONG).show();
                  //  Toast.makeText(getContext(), "Please fill " + k.trim() + ".", Toast.LENGTH_LONG).show();
                }
                }else {
                    Toast.makeText(getContext(), "Please choose your position.",Toast.LENGTH_LONG).show();
                }
            }
            public String msgRequired(String surname, String lastname, String age, String tenure, String gender){
               String ret = "";
               if (surname.equals("")){
                ret = "Last Name ";
               }
                if (gender.equals("")){
                    ret = ret.concat("Gender ");
                }
                if (lastname.equals("")){
                    ret = ret.concat("First Name ");
                }
                if (age.equals("")){
                    ret = ret.concat("Age ");
                }
                if (tenure.equals("")){
                    ret = ret.concat("Tenure");
                }
               return ret;
            }
            public boolean checkems(){
                boolean ret = false;
                if (String.valueOf(tsur.getText()).equals("") || String.valueOf(tfname.getText()).equals("") || String.valueOf(tagex.getText()).equals("") ||
                         String.valueOf(tyear.getText()).equals("") || String.valueOf(gendertxt.getText()).equals("")){
                    ret = false;
                }else {
                    ret = true;
                }
                return  ret;
            }

            public boolean checkPos(String str){
                boolean ret = false;
                for (String stx: genlist) {
                    if (stx.equals(str)){
                        ret = true;
                        break;
                    }
                }
                return ret;
            }

        });


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
    }

    public boolean checkMainSub(){
        boolean ret = false;
        int radioid = radiogroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioid);
        if (radioid != -1){
            String unitx = (String) radioButton.getText();
            if (String.valueOf(aurinstitution.getText()).equals("") || String.valueOf(aurdistrict.getText()).equals("") || String.valueOf(aurcounsil.getText()).equals("") ||
                    String.valueOf(aurunitnumber.getText()).equals("") || unitx.equals("")){
                ret = false;
            }else {
                ret = true;
            }
        }else {
            ret = false;
        }

        return  ret;
    }

    private void addCard(String surname, String fname, String Mname, String reg, String agex, String gen, String memca, String thig, String tye, String gender) {
        View v1 = getLayoutInflater().inflate(R.layout.adultleadaddfield, null);

        EditText tsur = v1.findViewById(R.id.taursurname);
        EditText tfname = v1.findViewById(R.id.taurname);
        EditText tfmid = v1.findViewById(R.id.taurmidname);
        EditText treg = v1.findViewById(R.id.taurrregstatus);
        EditText tagex = v1.findViewById(R.id.taurage);
        EditText tgender = v1.findViewById(R.id.taurgender);
        EditText tmemcard = v1.findViewById(R.id.taurmemcardnum);
        EditText thigh = v1.findViewById(R.id.taurankearned);
        EditText tyear = v1.findViewById(R.id.taurryearsof);
        EditText tttaurgender = v1.findViewById(R.id.tttaurgender);




        tsur.setText(surname);
        tfname.setText(fname);
        tfmid.setText(Mname);
        treg.setText(reg);
        tagex.setText(agex);
        tgender.setText(gen);
        tmemcard.setText(memca);
        thigh.setText(thig);
        tyear.setText(tye);
        tttaurgender.setText(gender);

        Button butclo = v1.findViewById(R.id.taurremove);
        Button butEdit = v1.findViewById(R.id.tauredit);
        butEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                warningdialogEdit("Are you sure to Edit " + tfname.getText() + "?","Click Ok to proceed.", v1,String.valueOf(tfname.getText()));
            }
        });

        butclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningdialog1("Are you sure to delete " + tfname.getText() + "?","Click Ok to proceed.", v1,String.valueOf(tfname.getText()));

//                int index = 0;
//                String sda = String.valueOf(tsur.getText());
//                index = IntStream.range(0, adultLeaders.size())
//                        .filter(i -> adultLeaders.get(i).getAursurname().equals(sda))
//                        .findFirst()
//                        .orElse(-1);
//                adultLeaders.remove(index);
//                layout.removeView(v1);

            }
        });

        layout.addView(v1);
        dialog.dismiss();
    }
    ArrayList<String> positionx;
    ArrayList<String> curnamex;
    ArrayList<String> curidx;
    public void sendAPI(){
        boolean b = isconnected();
        ArrayList<Aurholder> aurholders = new ArrayList<>();
        int radioid = radiogroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioid);

        int radioidx = radiogroupnature.getCheckedRadioButtonId();
        radioButtonna = view.findViewById(radioidx);
        if ( checkfields()){

            if (radioid != -1 && radioidx != -1){

                String unitx = (String) radioButton.getText();
                String unitxx = (String) radioButtonna.getText();
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String referenceNumber = "AUR" + sdf.format(currentDate);


                Aurholder aurholder = new Aurholder("bsp_leyte-ddc-1989",String.valueOf(aurnumber.getText()),CurrentUser.schoolID,
                        CurrentUser.districtID,String.valueOf(aurcounsil.getText()),unitx,String.valueOf(aurunitnumber.getText()),unitxx,referenceNumber,CurrentUser.user_id,rosterScoutmembers,adultLeaders,isOver);
                aurholders.add(aurholder);
                Gson gson = new Gson();
                String json = gson.toJson(aurholders);
                if(json.contains("Instl. Scouting Rep")){
                if (b && ModeOL.isOnline){
                    //ApiHost.user_id + "
                    //StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/AndroidApi/unitRegistration", new Response.Listener<String>() {
    loadingDialog.startLoadingDialog();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiHost.user_id + "bsp-api/AndroidApi/unitRegistration", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{

                            loadingDialog.dismissdialog();
                            if (response.contains("success")){


                                    if (response.contains("success")) {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String rep = jsonObject.getString("reference_number");

                                        dbHandler.addNewData(json,String.valueOf(aurnumber.getText()), rep, String.valueOf(totalfees.getText()),"1");
                                        Intent intent = new Intent(getContext(), Receipt.class);
                                        intent.putExtra("REF",rep);
                                        String am = String.valueOf(totalfees.getText());
                                        intent.putExtra("AMOUNT",am);
                                        intent.putExtra("FROM","");
                                        CurrentUser.setHasaur("true");
                                        getContext().startActivity(intent);

                                        //showWarningDialogx("Congratulations", "Successfully registered, tap anywhere to close.");
                                    }

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
                                    askdup("Position already taken",msxg);
                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    dbHandler.addNewData(json,String.valueOf(aurnumber.getText()), referenceNumber, String.valueOf(totalfees.getText()),"0");
                    Intent intent = new Intent(getContext(), Receipt.class);
                    intent.putExtra("REF",referenceNumber);
                    String am = String.valueOf(totalfees.getText());
                    intent.putExtra("AMOUNT",am);
                    intent.putExtra("FROM","");
                    getContext().startActivity(intent);
                  //  showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                }
                }else {
                    Toast.makeText(getContext(), "Inst'l. Scouting Rep is required in Adult Leaders.", Toast.LENGTH_LONG).show();
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
    private void builDialogRost() {
        AlertDialog. Builder builder = new AlertDialog.Builder(getContext());
        View vi1 = getLayoutInflater().inflate(R.layout.rosterpopup, null);

        EditText tsur = vi1.findViewById(R.id.rostsurname);
        EditText tfname = vi1.findViewById(R.id.rostfirstname);
        EditText tfmid = vi1.findViewById(R.id.rostmidname);
        EditText treg = vi1.findViewById(R.id.rostreligion);
        EditText tagex = vi1.findViewById(R.id.rostage);
        EditText tmemcard = vi1.findViewById(R.id.rostmemcardNo);
        EditText thigh = vi1.findViewById(R.id.rosthighestrank);
        AutoCompleteTextView tregstat = vi1.findViewById(R.id.rostregstat);
        EditText tyear = vi1.findViewById(R.id.rosttennureinscounting);
        ImageView buttmp = vi1.findViewById(R.id.rostregstatimg);
        AutoCompleteTextView gendertxt = vi1.findViewById(R.id.rostgender);
        ImageView genderimg = vi1.findViewById(R.id.rostgenderimg);

        AutoCompleteTextView tgpos = vi1.findViewById(R.id.rostposition);
        ImageView buttmpx = vi1.findViewById(R.id.rostpositionimg);

        if (leaderEditRost){
            tsur.setText(rosterScoutmembers.get(editIDRost).getRostsurname());
            tfname.setText(rosterScoutmembers.get(editIDRost).getRostfirstname());
            tfmid.setText(rosterScoutmembers.get(editIDRost).getRostmidname());
            treg.setText(rosterScoutmembers.get(editIDRost).getRostreligion());
            tagex.setText(rosterScoutmembers.get(editIDRost).getRostage());
            tmemcard.setText(rosterScoutmembers.get(editIDRost).getRostmemcardNo());
            thigh.setText(rosterScoutmembers.get(editIDRost).getRosthighestrank());
            tregstat.setText(rosterScoutmembers.get(editIDRost).getRostregstat());
            tyear.setText(rosterScoutmembers.get(editIDRost).getRosttennureinscounting());
            gendertxt.setText(rosterScoutmembers.get(editIDRost).getGender());
            tgpos.setText(rosterScoutmembers.get(editIDRost).getRostposition());

        }


        tregstat.setInputType(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,reglist);
        tregstat.setAdapter(adapter);
        buttmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tregstat.showDropDown();
            }
        });
        tregstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tregstat.showDropDown();
            }
        });

        gendertxt.setInputType(0);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,genderList);
        gendertxt.setAdapter(adapter1);
        genderimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gendertxt.showDropDown();
            }
        });
        gendertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gendertxt.showDropDown();
            }
        });


        tgpos.setInputType(0);
        ArrayAdapter<String> adapterx = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,rosterpositionlist);
        tgpos.setAdapter(adapterx);
        buttmpx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgpos.showDropDown();
            }
        });
        tgpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgpos.showDropDown();
            }
        });


        Button butsubx = vi1.findViewById(R.id.rostsub);
        Button butclox = vi1.findViewById(R.id.rostclose);


        builder.setView(vi1);
        butclox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogrost.dismiss();
            }
        });
        butsubx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkemx()){

                    if (!leaderEditRost){


                addCardRost(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                        String.valueOf(tregstat.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()),String.valueOf(tgpos.getText()),String.valueOf(gendertxt.getText()));

                RosterScoutmember aasrHolder = new RosterScoutmember(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),
                        String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                        String.valueOf(tregstat.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),
                        String.valueOf(tyear.getText()),String.valueOf(tgpos.getText()),String.valueOf(gendertxt.getText()));

                rosterScoutmembers.add(aasrHolder);


                    if (rosterScoutmembers.size() != 0){
                        scountcnt.setText(String.valueOf((int) rosterScoutmembers.size()));
                    }
                    scounttotal.setText(String.valueOf((int)(50 * rosterScoutmembers.size())));
                    totalfees.setText(String.valueOf( Integer.valueOf( String.valueOf(isctotal.getText())) + Integer.valueOf( String.valueOf(parentreptotal.getText())) + Integer.valueOf( String.valueOf(unitasstotal.getText())) + Integer.valueOf( String.valueOf(iscchairtotal.getText())) + Integer.valueOf( String.valueOf(scoutreptotal.getText())) + Integer.valueOf( String.valueOf(scounttotal.getText()))));

                    }
                    else {


                        rosterScoutmembers.remove(editIDRost);
                        layoutrost.removeView(viewEditRost);

                        addCardRost(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                                String.valueOf(tregstat.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()),String.valueOf(tgpos.getText()),String.valueOf(gendertxt.getText()));

                        RosterScoutmember aasrHolder = new RosterScoutmember(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),
                                String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                                String.valueOf(tregstat.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),
                                String.valueOf(tyear.getText()),String.valueOf(tgpos.getText()),String.valueOf(gendertxt.getText()));

                        rosterScoutmembers.add(aasrHolder);


                        if (rosterScoutmembers.size() != 0){
                            scountcnt.setText(String.valueOf((int) rosterScoutmembers.size()));
                        }
                        scounttotal.setText(String.valueOf((int)(50 * rosterScoutmembers.size())));
                        totalfees.setText(String.valueOf( Integer.valueOf( String.valueOf(isctotal.getText())) + Integer.valueOf( String.valueOf(parentreptotal.getText())) + Integer.valueOf( String.valueOf(unitasstotal.getText())) + Integer.valueOf( String.valueOf(iscchairtotal.getText())) + Integer.valueOf( String.valueOf(scoutreptotal.getText())) + Integer.valueOf(String.valueOf(scounttotal.getText()))));




                    }

                    tsur.setText("");
                    tfname.setText("");
                    tfmid.setText("");
                    treg.setText("");
                    tagex.setText("");
                    tregstat.setText("");
                    tmemcard.setText("");
                    thigh.setText("");
                    tyear.setText("");
                    tgpos.setText("");
                    gendertxt.setText("");
                    leaderEditRost = false;
                    editIDRost = 0;
                }
                else {
                    Toast.makeText(getContext(), "Please fill all the required fields(RED LINE).", Toast.LENGTH_LONG).show();
                }
            }
            public boolean checkemx(){
                boolean ret = false;

                String s1 = String.valueOf(tsur.getText());
                        String s11 = String.valueOf(tfname.getText());
                        String s12 = String.valueOf(tagex.getText());
                        String s13 = String.valueOf(tregstat.getText());
                        String s14 = String.valueOf(tyear.getText());
                        String s15 = String.valueOf(gendertxt.getText());
                        String s16 = String.valueOf(tgpos.getText());

                if(String.valueOf(tsur.getText()).equals("")
                        ||
                        String.valueOf(tfname.getText()).equals("")
                        ||
                         String.valueOf(tagex.getText()).equals("")
                        ||
                        String.valueOf(tregstat.getText()).equals("")
                        ||
                        String.valueOf(tyear.getText()).equals("")
                        ||
                        String.valueOf(gendertxt.getText()).equals("")
                        ||
                        String.valueOf(tgpos.getText()).equals("")
                ){
                    ret = false;
                }else {
                    ret = true;
                }

                return ret;
            }
            public String msgRequired(String surname, String lastname, String age, String tenure, String gender, String regStat, String position){
                String ret = "";
                if (surname.equals("")){
                    ret = "Last Name ";
                }
                if (gender.equals("")){
                    ret = ret.concat("Gender ");
                }
                if (lastname.equals("")){
                    ret = ret.concat("First Name ");
                }
                if (age.equals("")){
                    ret = ret.concat("Age ");
                }
                if (tenure.equals("")){
                    ret = ret.concat("Tenure");
                }
                if (regStat.equals("")){
                    ret = ret.concat("Reg");
                }
                if (tenure.equals("")){
                    ret = ret.concat("Tenure");
                }
                return ret;
            }
        });

        dialogrost = builder.create();
        dialogrost.setCanceledOnTouchOutside(false);
    }

    private void addCardRost(String surname, String fname, String Mname, String reg, String agex, String gen, String memca, String thig, String tye, String pos, String gender) {
        View v1 = getLayoutInflater().inflate(R.layout.rosteraddfield, null);

        EditText tsur = v1.findViewById(R.id.trostersurname);
        EditText tfname = v1.findViewById(R.id.trosterfirstname);
        EditText tfmid = v1.findViewById(R.id.trostermidname);
        EditText treg = v1.findViewById(R.id.trosterreligion);
        EditText tagex = v1.findViewById(R.id.trosterage);
        EditText tgender = v1.findViewById(R.id.trosterreg);
        EditText tmemcard = v1.findViewById(R.id.trostermemcard);
        EditText thigh = v1.findViewById(R.id.trosterhighest);
        EditText tyear = v1.findViewById(R.id.trostertenureinscouting);
        EditText tpos = v1.findViewById(R.id.trosterposition);
        EditText tttgender = v1.findViewById(R.id.tttgender);


        tsur.setText(surname);
        tfname.setText(fname);
        tfmid.setText(Mname);
        treg.setText(reg);
        tagex.setText(agex);
        tgender.setText(gen);
        tmemcard.setText(memca);
        thigh.setText(thig);
        tyear.setText(tye);
        tpos.setText(pos);
        tttgender.setText(gender);


        Button butclo = v1.findViewById(R.id.trostmove);
        Button butedit = v1.findViewById(R.id.trostedit);

                butedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        warningdialogEditRost("Are you sure to Edit " + tfname.getText() + "?","Click Ok to proceed.", v1,String.valueOf(tfname.getText()));

                    }
                });

        butclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningdialog("Are you sure to delete " + tsur.getText() + "?","Click Ok to proceed.", v1,String.valueOf(tsur.getText()));

//                int index = 0;
//                String sda = String.valueOf(tsur.getText());
//                index = IntStream.range(0, rosterScoutmembers.size())
//                        .filter(i -> rosterScoutmembers.get(i).getRostsurname().equals(sda))
//                        .findFirst()
//                        .orElse(-1);
//                rosterScoutmembers.remove(index);
//                layoutrost.removeView(v1);

            }
        });

        layoutrost.addView(v1);
        dialogrost.dismiss();
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
                 positionx = new ArrayList<>();
                 curnamex = new ArrayList<>();
                 curidx = new ArrayList<>();
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
    public void warningdialogEditRost (String warning, String message, View v1,String tsur) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(warning);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Add a positive button (optional)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                leaderEditRost = false;
                int index = 0;
                // Toast.makeText(getContext(), tsur.getText(),Toast.LENGTH_LONG).show();
                String sda = String.valueOf(tsur);
                index = IntStream.range(0, rosterScoutmembers.size())
                        .filter(i -> rosterScoutmembers.get(i).getRostfirstname().equals(sda))
                        .findFirst()
                        .orElse(-1);

                if (index != -1){
                    editIDRost = index;
                    viewEditRost = v1;
                    leaderEditRost = true;
                    aurrostadd.performClick();
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
                index = IntStream.range(0, adultLeaders.size())
                        .filter(i -> adultLeaders.get(i).getAurfirstname().equals(sda))
                        .findFirst()
                        .orElse(-1);

                if (index != -1){
                    editID = index;
                    viewEdit = v1;
                    leaderEdit = true;
                    aurbtnadd.performClick();
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
    String isOver = "";
    public void askdup(String warning, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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

    public void warningdialog1(String warning, String message, View v1,String tsur) {
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
                index = IntStream.range(0, adultLeaders.size())
                        .filter(i -> adultLeaders.get(i).getAurfirstname().equals(sda))
                        .findFirst()
                        .orElse(-1);
                adultLeaders.remove(index);
                layout.removeView(v1);


//                long scoutrep = adultLeaders.stream()
//                        .filter(person -> person.getAurposition().equalsIgnoreCase("Inst'l. Scouting Rep"))
//                        .count();
//                if (scoutrep != 0){
//                    scoutrepcnt.setText(String.valueOf((int) scoutrep));
//                }
//                scoutreptotal.setText(String.valueOf((int)(100 * scoutrep)));
//
//                long isc = adultLeaders.stream()
//                        .filter(person -> person.getAurposition().equalsIgnoreCase("ISC Chair/Coor/Memb"))
//                        .count();
//
//                if (isc != 0){
//                    iscchair.setText(String.valueOf((int) isc));
//                }
//                iscchairtotal.setText(String.valueOf((int)(100 * isc)));
//
//                long un1 = adultLeaders.stream()
//                        .filter(person -> person.getAurposition().equalsIgnoreCase("Assistant Unit Leader/ACA"))
//                        .count();
//                long un2 = adultLeaders.stream()
//                        .filter(person -> person.getAurposition().equalsIgnoreCase("Unit Leader/Circle Adviser"))
//                        .count();
//                if (un1 != 0 || un2 != 0){
//                    unitasscnt.setText(String.valueOf((int) un1 + un2));
//                }
//                unitasstotal.setText(String.valueOf((int)(60 * ((int) un1 + (int) un2))));
                long scoutrep = adultLeaders.stream()
                        .filter(person -> person.getAurposition().equalsIgnoreCase("Instl. Scouting Rep"))
                        .count();
                if (scoutrep != 0){
                    scoutrepcnt.setText(String.valueOf((int) scoutrep));
                }else {
                    scoutrepcnt.setText("0");
                }
                scoutreptotal.setText(String.valueOf((int)(100 * scoutrep)));

                long isc = adultLeaders.stream()
                        .filter(person -> person.getAurposition().equalsIgnoreCase("ISC Chair/Coor/Memb"))
                        .count();

                if (isc != 0){
                    iscchair.setText(String.valueOf((int) isc));
                }else {
                    iscchair.setText("0");
                }
                iscchairtotal.setText(String.valueOf((int)(100 * isc)));

                long un1 = adultLeaders.stream()
                        .filter(person -> person.getAurposition().equalsIgnoreCase("Assistant Unit Leader/ACA"))
                        .count();
                long un2 = adultLeaders.stream()
                        .filter(person -> person.getAurposition().equalsIgnoreCase("Unit Leader/Circle Adviser"))
                        .count();
                if (un1 != 0 || un2 != 0){
                    unitasscnt.setText(String.valueOf((int) un1 + un2));
                }else {
                    unitasscnt.setText("0");
                }
                unitasstotal.setText(String.valueOf((int)(60 * ((int) un1 + (int) un2))));

                long ppr = adultLeaders.stream()
                        .filter(person -> person.getAurposition().equalsIgnoreCase("Parent Representative"))
                        .count();

                if (ppr != 0){
                    parentrepcnt.setText(String.valueOf((int) ppr));
                }
                else {
                    parentrepcnt.setText("0");
                }
                parentreptotal.setText(String.valueOf((int)(100 * ppr)));

                long iscc = adultLeaders.stream()
                        .filter(person -> person.getAurposition().equalsIgnoreCase("Inst'l. Scouting Coordinator"))
                        .count();

                if (iscc != 0){
                    isccnt.setText(String.valueOf((int) iscc));
                }
                else {
                    isccnt.setText("0");
                }
                isctotal.setText(String.valueOf((int)(100 * iscc)));

                totalfees.setText(String.valueOf( Integer.valueOf( String.valueOf(isctotal.getText())) + Integer.valueOf( String.valueOf(parentreptotal.getText())) + Integer.valueOf( String.valueOf(unitasstotal.getText())) + Integer.valueOf( String.valueOf(iscchairtotal.getText())) + Integer.valueOf( String.valueOf(scoutreptotal.getText())) + Integer.valueOf( String.valueOf(scounttotal.getText()))));


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
                index = IntStream.range(0, rosterScoutmembers.size())
                        .filter(i -> rosterScoutmembers.get(i).getRostsurname().equals(sda))
                        .findFirst()
                        .orElse(-1);
                rosterScoutmembers.remove(index);
                layoutrost.removeView(v1);


                if (rosterScoutmembers.size() != 0){
                    scountcnt.setText(String.valueOf((int) rosterScoutmembers.size()));
                }
                scounttotal.setText(String.valueOf((int)(50 * rosterScoutmembers.size())));
                totalfees.setText(String.valueOf( Integer.valueOf( String.valueOf(isctotal.getText())) + Integer.valueOf( String.valueOf(parentreptotal.getText())) + Integer.valueOf( String.valueOf(unitasstotal.getText())) + Integer.valueOf( String.valueOf(iscchairtotal.getText())) + Integer.valueOf( String.valueOf(scoutreptotal.getText())) + Integer.valueOf(String.valueOf(scounttotal.getText()))));


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
}
