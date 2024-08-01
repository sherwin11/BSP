package com.bsp.bspregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class History extends AppCompatActivity {


    RecyclerView recyclerView;
    private DBHandler dbHandler;
    static HistoryAdapter historyAdapter;

    static ArrayList<HistoryList> historyListArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHandler = new DBHandler(getApplicationContext(), "forupload.db");

        recyclerView = findViewById(R.id.histrecycle);

        historyListArrayList = dbHandler.selecthis();
        historyAdapter = new HistoryAdapter(this,historyListArrayList);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }
}