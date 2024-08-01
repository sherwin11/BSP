package com.bsp.bspregistration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


public class AASR extends Fragment {
     Button add, mainSub;

     AlertDialog dialog;
     LinearLayout layout;

    View view;

    EditText aasrdateapp,aasrsponsoringins,aasrunitno,aasrlocalcouncil,malecnt,femalecnt,
            aasramout1,aasramount2,aasrtotalcountofscount,aarpaidunderor,aasrdatefees,aasrregexpdate,aasrnumber;

    RadioButton radioButton;
    RadioGroup radiogroup,radiogroupnature;

    ArrayList<AddFieldHolder> nAasholder;
    private DBHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_a_a_s_r, container, false);

        dbHandler = new DBHandler(getContext(), "forupload.db");

        aasrnumber = view.findViewById(R.id.aasrnumber);
        aasramout1 = view.findViewById(R.id.aasramout1);
        aasramount2 = view.findViewById(R.id.totalfeeremitted);

        aasrtotalcountofscount = view.findViewById(R.id.aasrtotalcountofscount);
        aarpaidunderor = view.findViewById(R.id.aarpaidunderor);
        aasrdatefees = view.findViewById(R.id.aasrdatefees);
        aasrregexpdate = view.findViewById(R.id.aasrregexpdate);


        aasrregexpdate.setInputType(0);
        aasrregexpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    dateofexpp = true;
                    showDatePicker();
                }
            }
        });

        aasrdatefees.setInputType(0);
        aasrdatefees.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    dateofApp = true;
                    showDatePicker();
                }
            }
        });


        aasrsponsoringins = view.findViewById(R.id.aasrsponsoringins);
        aasrunitno = view.findViewById(R.id.aasrunitno);
        aasrlocalcouncil = view.findViewById(R.id.aasrlocalcouncil);
        nAasholder = new ArrayList<>();
         add = view.findViewById(R.id.btnAdd);
         layout = view.findViewById(R.id.addLin);
         mainSub = view.findViewById(R.id.mainSub);
         aasrdateapp = view.findViewById(R.id.aasrdateapplied);
        LocalDate currentDate = LocalDate.now();
        aasrdateapp.setText(currentDate.toString());
        aasrdateapp.setInputType(0);
        aasrdateapp.setFocusable(false);
        aasrdateapp.setEnabled(false);
        aasrdateapp.setCursorVisible(false);
        aasrdateapp.setKeyListener(null);
        radiogroup = view.findViewById(R.id.radiogroup);
        radiogroupnature = view.findViewById(R.id.radiogroupnature);
        femalecnt = view.findViewById(R.id.femalecnt);
        femalecnt.setInputType(0);
        femalecnt.setFocusable(false);
        femalecnt.setEnabled(false);
        femalecnt.setCursorVisible(false);
        femalecnt.setKeyListener(null);
        malecnt = view.findViewById(R.id.malecnt);
        malecnt.setInputType(0);
        malecnt.setFocusable(false);
        malecnt.setEnabled(false);
        malecnt.setCursorVisible(false);
        malecnt.setKeyListener(null);
        builDialog();
         add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialbg);
                dialog.show();;
             }
         });

         mainSub.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialforSubmit("Are you sure to submit?", "Please click OK to submit.");
             }
         });

        return view;
    }
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
    public static final String[] genlist = new String[]{"Male","Female"};
    private void builDialog() {
        AlertDialog. Builder builder = new AlertDialog.Builder(getContext());
        View vi1 = getLayoutInflater().inflate(R.layout.popupadd, null);

        EditText tsur = vi1.findViewById(R.id.tmpsurname);
        EditText tfname = vi1.findViewById(R.id.tmpname);
        EditText tfmid = vi1.findViewById(R.id.tmpmidname);
        EditText treg = vi1.findViewById(R.id.tmpregstatus);
        EditText tagex = vi1.findViewById(R.id.tmpage);
        EditText tmemcard = vi1.findViewById(R.id.tmpmemcardnum);
        EditText thigh = vi1.findViewById(R.id.tmprankearned);
        EditText tyear = vi1.findViewById(R.id.tmpyearsof);
        ImageView buttmp = vi1.findViewById(R.id.tmpgenderimg);
        AutoCompleteTextView tgender = vi1.findViewById(R.id.tmpgender);



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


        Button butsubx = vi1.findViewById(R.id.tmpsubmmit);
        Button butclox = vi1.findViewById(R.id.tmpclose);


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

               addCard(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                       String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()));

               AddFieldHolder aasrHolder = new AddFieldHolder(String.valueOf(tsur.getText()),String.valueOf(tfname.getText()),String.valueOf(tfmid.getText()),String.valueOf(treg.getText()),String.valueOf(tagex.getText()),
                       String.valueOf(tgender.getText()),String.valueOf(tmemcard.getText()),String.valueOf(thigh.getText()),String.valueOf(tyear.getText()));

               nAasholder.add(aasrHolder);

//             int  index = IntStream.range(0, nAasholder.size())
//                       .filter(i -> nAasholder.get(i).getSurname().equals(sda))
//                       .findFirst()
//                       .orElse(-1);

               long femaleCount = nAasholder.stream()
                       .filter(person -> person.getGender().equalsIgnoreCase("Female"))
                       .count();

               long malecount = nAasholder.stream()
                       .filter(person -> person.getGender().equalsIgnoreCase("Male"))
                       .count();
               if (malecount != 0){
                   malecnt.setText(String.valueOf((int) malecount));
               }

               if (femaleCount != 0){
                   femalecnt.setText(String.valueOf((int) femaleCount));
               }
               int totalreg = (int) femaleCount + (int) malecount;
               aasrtotalcountofscount.setText(String.valueOf(totalreg));

               tsur.setText("");
               tfname.setText("");
               tfmid.setText("");
               treg.setText("");
               tagex.setText("");
               tgender.setText("");
               tmemcard.setText("");
               thigh.setText("");
               tyear.setText("");
           }
       });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
    }


    public void sendAPI(){

        boolean b = isconnected();

        ArrayList<AasrHolder> aasrHolder = new ArrayList<>();
        int radioid = radiogroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioid);
        String unitx = (String) radioButton.getText();
        int radnatu = radiogroupnature.getCheckedRadioButtonId();
        radioButton = view.findViewById(radnatu);
        String nature = (String) radioButton.getText();

        AasrHolder aasrHolder1 = new AasrHolder("bsp_leyte-ddc-1989",String.valueOf(aasrsponsoringins.getText()),String.valueOf(aasrdateapp.getText()),String.valueOf(aasrunitno.getText()),
                String.valueOf(aasrlocalcouncil.getText()),unitx,nature,String.valueOf(femalecnt.getText()),String.valueOf(malecnt.getText()),
                nAasholder,String.valueOf(aasramout1.getText()),String.valueOf(aasramount2.getText()),String.valueOf(aasrtotalcountofscount.getText())
                ,String.valueOf(aarpaidunderor.getText()),String.valueOf(aasrdatefees.getText())
                ,String.valueOf(aasrregexpdate.getText()),String.valueOf(aasrnumber.getText()));

        aasrHolder.add(aasrHolder1);
        Gson gson = new Gson();
        String json = gson.toJson(aasrHolder);
        if (b){



            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.23.77/bsp-api/AndroidApi/aasrRegistration", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
            //dbHandler.addNewData(json,String.valueOf(aasrnumber.getText()));
            showWarningDialogx("Congratulations","Successfully registered, tap anywhere to close.");
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
    private void addCard(String surname, String fname, String Mname, String reg, String agex, String gen, String memca, String thig, String tye) {
            View v1 = getLayoutInflater().inflate(R.layout.addfield, null);

        EditText tsur = v1.findViewById(R.id.aarsurname);
        EditText tfname = v1.findViewById(R.id.aarname);
        EditText tfmid = v1.findViewById(R.id.aarmidname);
        EditText treg = v1.findViewById(R.id.aasrregstatus);
        EditText tagex = v1.findViewById(R.id.aasrage);
        EditText tgender = v1.findViewById(R.id.aasrgender);
        EditText tmemcard = v1.findViewById(R.id.aasrmemcardnum);
        EditText thigh = v1.findViewById(R.id.aasrrankearned);
        EditText tyear = v1.findViewById(R.id.aasryearsof);




        tsur.setText(surname);
        tfname.setText(fname);
        tfmid.setText(Mname);
        treg.setText(reg);
        tagex.setText(agex);
        tgender.setText(gen);
        tmemcard.setText(memca);
        thigh.setText(thig);
        tyear.setText(tye);

        Button butclo = v1.findViewById(R.id.remove);

        butclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 0;
               // Toast.makeText(getContext(), tsur.getText(),Toast.LENGTH_LONG).show();
                String sda = String.valueOf(tsur.getText());
                index = IntStream.range(0, nAasholder.size())
                        .filter(i -> nAasholder.get(i).getSurname().equals(sda))
                        .findFirst()
                        .orElse(-1);
                nAasholder.remove(index);
                layout.removeView(v1);

            }
        });

        layout.addView(v1);
        dialog.dismiss();
    }

    boolean dateofApp = false;
    boolean dateofexpp = false;
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    if (dateofApp){
                        aasrdatefees.setText(selectedDate);
                        dateofApp = false;
                    }
                    else if (dateofexpp){
                        aasrregexpdate.setText(selectedDate);
                        dateofexpp = false;
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