package com.bsp.bspregistration;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AAR extends Fragment {
    AutoCompleteTextView argender, aarcivil;
    ImageView genBut,civBut;
    EditText aarexpdate,aardateofbirth,aardatefees,aarlocalcouncildate,aarlocalcouncilappdate,aarregionaldate,aarnumber;

    EditText aarname,aarsurname,aarmidname,aartenure,aarmembershipNo,aarserveas,aarunitNo,aarsponinstitution,aarcounsil,
            aarmailingadd,aarplaceofbirth,aarreligion,aarprofession,aarposition,aaraffiliation,aarpaidunderor,aaramountpaid;

    CheckBox aarcheckNew,aarcheckReregistering;
    String RegStatus = "";
    Button submit;
    View view;
    RadioButton radioButton, aarchecklceb, aarULAUL, aarlayleaders;
    RadioGroup radiogroup;


    private DBHandler dbHandler;
    private Handler timerHandler = new Handler();
    public static final String[] genlist = new String[]{"Male","Female"};
    public static final String[] civillist = new String[]{"Single","Separated", "Merried", "Divorced",  "Widowed"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_a_a_r, container, false);


        aarchecklceb = view.findViewById(R.id.aarchecklceb);
        aarULAUL = view.findViewById(R.id.aarULAUL);
        aarlayleaders = view.findViewById(R.id.aarlayleaders);

        aarsurname = (EditText) view.findViewById(R.id.aarsurname);
        aarmidname = (EditText) view.findViewById(R.id.aarmidname);
        aarname = (EditText) view.findViewById(R.id.aarname);
        aartenure = (EditText) view.findViewById(R.id.aartenure);
        aarmembershipNo = (EditText) view.findViewById(R.id.aarmembershipNo);
        aarserveas = (EditText) view.findViewById(R.id.aarserveas);
        aarunitNo = (EditText) view.findViewById(R.id.aarunitNo);
        aarsponinstitution = (EditText) view.findViewById(R.id.aarsponinstitution);
        aarcounsil = (EditText) view.findViewById(R.id.aarcounsil);
        aarmailingadd = (EditText) view.findViewById(R.id.aarmailingadd);
        aarplaceofbirth = (EditText) view.findViewById(R.id.aarplaceofbirth);
        aarreligion = (EditText) view.findViewById(R.id.aarreligion);
        aarprofession = (EditText) view.findViewById(R.id.aarprofession);
        aarposition = (EditText) view.findViewById(R.id.aarposition);
        aaraffiliation = (EditText) view.findViewById(R.id.aaraffiliation);
        aarnumber = (EditText) view.findViewById(R.id.aarnumber);
        aarpaidunderor = (EditText) view.findViewById(R.id.aarpaidunderor);
        aaramountpaid = (EditText) view.findViewById(R.id.aaramountpaid);

        radiogroup = view.findViewById(R.id.aarradiogroup);

        LocalDate currentDate = LocalDate.now();
//        dbHandler = new DBHandler(getContext(), "forupload.db");
//
//        if(isconnected())
//        {
//            timerHandler.postDelayed(timerhandler, 1000);
//        }


        aarchecklceb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aaramountpaid.setText("500.00");
                }
            }
        });
        aarULAUL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aaramountpaid.setText("60.00");
                }
            }
        });
        aarlayleaders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aaramountpaid.setText("100.00");
                }
            }
        });
        // Add 7 days to the current date
        int daysToAdd = 366;
        LocalDate newDate = currentDate.plusDays(daysToAdd);
        aarexpdate = (EditText) view.findViewById(R.id.aarexpdate);
        aarexpdate.setInputType(0);
        aarexpdate.setText(newDate.toString());
        aarexpdate.setFocusable(false);
        aarexpdate.setEnabled(false);
        aarexpdate.setCursorVisible(false);
        aarexpdate.setKeyListener(null);
        submit = (Button) view.findViewById(R.id.sub);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ArrHolder> arrHolders = new ArrayList<>();
                boolean b = isconnected();

                int radioid = radiogroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(radioid);
                String unitx = (String) radioButton.getText();


                ArrHolder arrHolder = new ArrHolder("bsp_leyte-ddc-1989",String.valueOf(aarname.getText()),String.valueOf(aarsurname.getText()),String.valueOf(aarmidname.getText()),String.valueOf(argender.getText()),String.valueOf(aarcivil.getText()),
                        String.valueOf(aartenure.getText()),String.valueOf(aarmembershipNo.getText()),String.valueOf(aarexpdate.getText()),
                        String.valueOf(aarserveas.getText()),String.valueOf(aarunitNo.getText()),String.valueOf(aarsponinstitution.getText()),
                        String.valueOf(aarcounsil.getText()),String.valueOf(aarmailingadd.getText()),String.valueOf(aardateofbirth.getText()),
                        String.valueOf(aarplaceofbirth.getText()),String.valueOf(aarreligion.getText()),String.valueOf(aarprofession.getText()),
                        String.valueOf(aarposition.getText()),String.valueOf(aaraffiliation.getText()),RegStatus, String.valueOf(aarnumber.getText()),
                        String.valueOf(aardatefees.getText()), String.valueOf(aaramountpaid.getText()), String.valueOf(aarpaidunderor.getText()));

                arrHolders.add(arrHolder);

                Gson gson = new Gson();
                String json = gson.toJson(arrHolders);
                if (b){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/AndroidApi/aarRegistration", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
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
                }else {
                    //dbHandler.addNewData(json,String.valueOf(aarnumber.getText()));
                    showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                }
            }
        });

        aarcheckNew = (CheckBox) view.findViewById(R.id.aarcheckNew);
        aarcheckReregistering = (CheckBox) view.findViewById(R.id.aarcheckReregistering);

        aarcheckNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (aarcheckNew.isChecked()){
                    aarcheckReregistering.setChecked(false);
                    RegStatus = String.valueOf(aarcheckNew.getText());
                }
            }
        });
        aarcheckReregistering.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (aarcheckReregistering.isChecked()) {
                    aarcheckNew.setChecked(false);
                    RegStatus = String.valueOf(aarcheckReregistering.getText());
                }
            }
        });


        argender = (AutoCompleteTextView) view.findViewById(R.id.aargender);
        genBut = (ImageView) view.findViewById(R.id.aargendropimg);
        argender.setInputType(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,genlist);
        argender.setAdapter(adapter);
        genBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    argender.showDropDown();
            }
        });

        argender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                argender.showDropDown();
            }
        });

        aarcivil = (AutoCompleteTextView) view.findViewById(R.id.aarcivil);
        civBut = (ImageView) view.findViewById(R.id.aarcivildropimg);
        aarcivil.setInputType(0);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,civillist);
        aarcivil.setAdapter(adapter1);
        civBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aarcivil.showDropDown();
            }
        });
        aarcivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aarcivil.showDropDown();
            }
        });




        aardateofbirth = (EditText) view.findViewById(R.id.aardateofbirth);
        aardateofbirth.setInputType(0);
        aardateofbirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    dateofB = true;
                    showDatePicker();
                }
            }
        });

        aardatefees = (EditText) view.findViewById(R.id.aardatefees);
        aardatefees.setInputType(0);
        aardatefees.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    aardf = true;
                    showDatePicker();
                }
            }
        });

        aarlocalcouncildate = (EditText) view.findViewById(R.id.aarlocalcouncildate);
        aarlocalcouncildate.setInputType(0);
        aarlocalcouncildate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    aarlcd = true;
                    showDatePicker();
                }
            }
        });

        aarlocalcouncilappdate = (EditText) view.findViewById(R.id.aarlocalcouncilappdate);
        aarlocalcouncilappdate.setInputType(0);
        aarlocalcouncilappdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    aarlcad = true;
                    showDatePicker();
                }
            }
        });

        aarregionaldate = (EditText) view.findViewById(R.id.aarregionaldate);
        aarregionaldate.setInputType(0);
        aarregionaldate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    aarrgd = true;
                    showDatePicker();
                }
            }
        });

        return view;
    }
    boolean dateofB = false;
    boolean dateofExp = false;
    boolean aardf = false;
    boolean aarlcd = false;
    boolean aarlcad = false;
    boolean aarrgd = false;

    public Boolean isconnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService(this.getContext().CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return   true;
        }
        else{
            return   false;
        }
    }


    private void showDatePicker() {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        if (dateofB){
                            aardateofbirth.setText(selectedDate);
                            dateofB = false;
                        }else if(dateofExp){
                            aarexpdate.setText(selectedDate);
                            dateofExp = false;
                        }else if(aardf){
                            aardatefees.setText(selectedDate);
                            aardf = false;
                        }else if(aarlcd){
                            aarlocalcouncildate.setText(selectedDate);
                            aarlcd = false;
                        }else if(aarlcad){
                            aarlocalcouncilappdate.setText(selectedDate);
                            aarlcad = false;
                        }
                        else if(aarrgd){
                            aarregionaldate.setText(selectedDate);
                            aarrgd = false;
                        }
                    },
                    // Initial date values
                    2023, 7, 11 // Year, month (0-11), day
            );
            datePickerDialog.show();
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
                Intent refresh = new Intent(getContext(), MainActivity.class);
                startActivity(refresh);
            }
        });
        //// Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbggreen);

        dialog.show();
    }

}