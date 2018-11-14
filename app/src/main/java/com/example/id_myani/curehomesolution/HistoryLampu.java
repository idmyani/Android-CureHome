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

public class HistoryLampu extends MainActivity {

    ListView listLogLampu;
    TextView statusLampu;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_lampu);
        listLogLampu = findViewById(R.id.listLogLampu);
        statusLampu = findViewById(R.id.statusLampu);

        list = (ArrayList<String>) getIntent().getSerializableExtra("Lampu");
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listLogLampu.setAdapter(adapter);

        getStatusLampu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                Log.d("Status", data);
                if (data.equals("On")){
                    statusLampu.setText("On");
                }
                if (data.equals("Off")){
                    statusLampu.setText("Off");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
