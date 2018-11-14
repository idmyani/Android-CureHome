package com.example.id_myani.curehomesolution;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryPintu extends MainActivity {

    ListView listLogPintu;
    TextView statusPintu;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pintu);
        listLogPintu = findViewById(R.id.listPintu);
        statusPintu = findViewById(R.id.statusPintu);

        list = (ArrayList<String>) getIntent().getSerializableExtra("Pintu");
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listLogPintu.setAdapter(adapter);

        getStatusPintu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                Log.d("Status", data);
                if (data.equals("On")){
                    statusPintu.setText("Terkunci");
                }
                if (data.equals("Off")){
                    statusPintu.setText("Terbuka");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
