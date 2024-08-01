package com.bsp.bspregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Foruploads extends AppCompatActivity {

        RecyclerView recyclerView;
        private DBHandler dbHandler;
        static UploadAdapter uploadAdapter;
        Button clickhis;

    static ArrayList<UploadList> uploadListArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foruploads);


        dbHandler = new DBHandler(getApplicationContext(), "forupload.db");
        clickhis = (Button) findViewById(R.id.clickhis);
        recyclerView = findViewById(R.id.playlistRv);


        clickhis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), History.class);
                v.getContext().startActivity(intent);
            }
        });
        uploadListArrayList = dbHandler.selectUpx();
        uploadAdapter = new UploadAdapter(this,uploadListArrayList);
        recyclerView.setAdapter(uploadAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


    }
    public static void deleteItem(int position) {
        try
        {
            uploadListArrayList.remove(position);
            uploadAdapter.notifyItemRemoved(position);
        }catch (Exception e){
            String x = e.getMessage();
            String xx = e.getMessage();
        }

    }
}