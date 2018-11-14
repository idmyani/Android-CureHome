package com.example.id_myani.curehomesolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference getStatusLampu = database.getReference("Lampu/status");
    final DatabaseReference timeStampLampu = database.getReference("Lampu/time_stamp");
    final DatabaseReference getStatusPintu = database.getReference("Pintu/status");
    final DatabaseReference timeStampPintu = database.getReference("Pintu/time_stamp");
    final DatabaseReference logLampu = database.getReference("Log/Lampu");
    final DatabaseReference logPintu = database.getReference("Log/Pintu");
    final DatabaseReference statusAsap = database.getReference("Asap/status");
    public String Pintu = "Pintu";
    public String Lampu = "Lampu";
    public static final int CHANNEL_ID = 1;

    LinearLayout asapBackground;
    TextView asapInformation, lampuButton, pintuButton, lampuInformation, pintuInformation;
    Switch lampuSwitch, pintuSwitch;
    ArrayList<String> arrayListLampu = new ArrayList<>();
    ArrayList<String> arrayListPintu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asapInformation = findViewById(R.id.deskripsiAsap);
        asapBackground = findViewById(R.id.linearAsap);
        lampuButton = findViewById(R.id.lampu);
        pintuButton = findViewById(R.id.pintu);
        lampuInformation = findViewById(R.id.deskripsiLampu);
        pintuInformation = findViewById(R.id.deskripsiPintu);
        lampuSwitch = findViewById(R.id.lampuSwitch);
        pintuSwitch = findViewById(R.id.pintuSwitch);

        SharedPreferences sharedPrefs = getSharedPreferences("com.example.id_myani.curehome", MODE_PRIVATE);
        lampuSwitch.setChecked(sharedPrefs.getBoolean(Lampu, false));
        pintuSwitch.setChecked(sharedPrefs.getBoolean(Pintu, false));

        getLastStat(getStatusLampu, lampuInformation);
        getLastStat(getStatusPintu, pintuInformation);
        getAsapInformation();
        switchAction(Lampu,lampuSwitch,getStatusLampu, timeStampLampu,logLampu);
        getAllHistory(logLampu, arrayListLampu);
        buttonObject(Lampu,lampuButton, HistoryLampu.class, arrayListLampu);
        switchAction(Pintu, pintuSwitch, getStatusPintu, timeStampPintu, logPintu);
        getAllHistory(logPintu, arrayListPintu);
        buttonObject(Pintu, pintuButton, HistoryPintu.class, arrayListPintu);

    }

    private String timeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        return format;
    }

    private void pushHistory(DatabaseReference referenceHistory, String status, String timeStamp){
        History data = new History(status, timeStamp);
        referenceHistory.push().setValue(data);
    }

    private void getAsapInformation(){
        statusAsap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int data = dataSnapshot.getValue(Integer.class);
                if(data < 5){
                    asapBackground.setBackgroundColor(Color.parseColor("#49da50"));
                    asapInformation.setText("Asap tidak terdeteksi");
                }else {
                    asapBackground.setBackgroundColor(Color.parseColor("#d13000"));
                    asapInformation.setText("Bahaya!! Asap terdeteksi");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void switchAction(final String object, final Switch switchObject, final DatabaseReference getStatusRef,
                              final DatabaseReference getTimeRef, final DatabaseReference history){
        switchObject.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchObject.isChecked()){
                    String time = timeStamp();
                    getStatusRef.setValue("On");
                    getTimeRef.setValue(time);
                    pushHistory(history, "On", time);
                    SharedPreferences.Editor data = getSharedPreferences("com.example.id_myani.curehome", MODE_PRIVATE).edit();
                    data.putBoolean(object, true);
                    data.commit();
                }else {
                    String time = timeStamp();
                    getStatusRef.setValue("Off");
                    getTimeRef.setValue(time);
                    pushHistory(history, "Off", time);
                    SharedPreferences.Editor data = getSharedPreferences("com.example.id_myani.curehome", MODE_PRIVATE).edit();
                    data.putBoolean(object, false);
                    data.commit();
                }
            }
        });
    }

    private void getLastStat(DatabaseReference objectReference, final TextView textInfo){
        objectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                Log.d("Status", data);
                if (data.equals("On")){
                    textInfo.setText("Keadaan saat ini "+data);
                }
                if (data.equals("Off")){
                    textInfo.setText("Keadaan saat ini "+data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void buttonObject(final String tag, TextView button, final Class nextClass, final ArrayList<String> arrayData){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, nextClass);
                intent.putExtra(tag, arrayData);
                startActivity(intent);
            }
        });
    }

    private void getAllHistory(DatabaseReference history, final ArrayList<String> arrayList){
        history.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String listData = ds.getValue(String.class);
                    arrayList.add(listData);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
