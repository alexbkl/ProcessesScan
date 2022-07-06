package com.android.vidrebany;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity {

    private FirebaseDatabase fd;
    private DatabaseReference dr;
    TextView codeTv, processTv, startedTv, nameTv, endedTv, numberTv, errorTv;
    Button scanBtn, cancelBtn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_account);

        toolbar = findViewById(R.id.toolbar_main);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compte");

        codeTv = findViewById(R.id.codeTv);
        processTv = findViewById(R.id.processTv);
        startedTv = findViewById(R.id.startedTv);
        scanBtn = findViewById(R.id.scanBtn);
        nameTv = findViewById(R.id.nameTv);
        endedTv = findViewById(R.id.endedTv);
        numberTv = findViewById(R.id.numberTv);
        errorTv = findViewById(R.id.errorTv);
        cancelBtn = findViewById(R.id.cancelBtn);
        scanBtn.setOnClickListener(view -> scan());
        errorTv.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);

        String name, process, number;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Intent intent = new Intent(AccountActivity.this, UsersActivity.class);
                startActivity(intent);
            } else {
                name = extras.getString("name");
                process = extras.getString("process");
                number = extras.getString("number");
                setContent(name, process, number);


            }
        } else {
            name = (String) savedInstanceState.getSerializable("name");
            process = (String) savedInstanceState.getSerializable("process");
            number = (String) savedInstanceState.getSerializable("number");
            setContent(name, process, number);




        }




    }



    private void setContent(String name, String process, String number){
        errorTv.setVisibility(View.GONE);
        String processState = "PROCÈS: "+process;
        String currentProcess = process.toLowerCase();
        boolean fastProcesses = currentProcess.equals("corte") ||
                currentProcess.equals("admin") ||
                currentProcess.equals("canteado") ||
                currentProcess.equals("mecanizado") ||
                currentProcess.equals("laca") ||
                currentProcess.equals("embalaje");
        dr = FirebaseDatabase.getInstance().getReference("users").child(number);

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String barcode = snapshot.child("code").getValue(String.class);

                String codeNumber = "CODI: "+barcode;
                codeTv.setText(codeNumber);

                assert barcode != null;
                if (!barcode.equals("sense codi")) {
                    dr = FirebaseDatabase.getInstance().getReference("codes").child(barcode);

                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(process.toLowerCase()+"Started").getValue(Long.class) != null) {
                                long started = snapshot.child(process.toLowerCase()+"Started").getValue(Long.class);
                                Date startedDate = new Date(started);
                                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");
                                String startedOn = "COMENÇAT: \n"+df.format(startedDate);
                                startedTv.setText(startedOn);


                            }
                            String endedOn = "ACABAT: sense acabar";
                            if (fastProcesses) {
                                endedOn = "";
                                cancelBtn.setVisibility(View.GONE);
                            } else {
                                cancelBtn.setVisibility(View.VISIBLE);
                            }

                            endedTv.setText(endedOn);
                            cancelBtn.setOnClickListener(view -> cancelOrder(barcode, process, number));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                } else {
                    String startedOn = "COMENÇAT: sense començar";
                    startedTv.setText(startedOn);
                    endedTv.setText("");
                    cancelBtn.setVisibility(View.GONE);
                }


                nameTv.setText(name);

                startedTv.setSingleLine(false);
                endedTv.setSingleLine(false);



                processTv.setText(processState);
                numberTv.setText(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void cancelOrder(String barcode, String process, String number) {

        String codeNumber = "CODI: sense codi";
        String started = "COMENÇAT: sense començar";
        codeTv.setText(codeNumber);
        startedTv.setText(started);
        cancelBtn.setVisibility(View.GONE);

       fd = FirebaseDatabase.getInstance();
       dr = fd.getReference("codes").child(barcode);

       dr.child(process.toLowerCase()).removeValue();
       dr.child(process.toLowerCase()+"Started").removeValue();
       dr.child(process.toLowerCase()+"Ended").removeValue();
       dr.child(process.toLowerCase()+"User").removeValue();

       DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("users").child(number);
        ordersRef.child("code").setValue("sense codi");

    }

    private void scan() {
        errorTv.setVisibility(View.GONE);
        IntentIntegrator intentIntegrator = new IntentIntegrator(
                AccountActivity.this
        );
        intentIntegrator.setPrompt("Per utilitzar flash prémer pujar volum")
                .setBeepEnabled(true)
                .setOrientationLocked(true)
                .setCaptureActivity(Capture.class)
                .initiateScan();

    }

    private void endProcess(String process, String code) {
        String currentProcess = process.toLowerCase();
        boolean fastProcesses = currentProcess.equals("corte") ||
                !currentProcess.equals("admin") ||
                !currentProcess.equals("canteado") ||
                !currentProcess.equals("mecanizado") ||
                !currentProcess.equals("laca") ||
                !currentProcess.equals("embalaje");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy-HH:mm", Locale.GERMANY);
        String currentDateAndTime = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MM yyyy", Locale.GERMANY);
        String currentDate = sdf1.format(new Date());
        errorTv.setVisibility(View.GONE);
        String processState = "PROCÈS: "+process;

        String[] codeparts = code.split("X");

        String puntuacio;
        if (isValidIndex(codeparts, 1)) {
            puntuacio = codeparts[1];
        } else {
            puntuacio = "s/p";
        }
        String endedOn = "FINALITZAT: sense finalitzar";
        if (fastProcesses) {
            endedOn = "";
            cancelBtn.setVisibility(View.GONE);
        } else {
            endedOn = "ACABAT: \n"+currentDateAndTime;
            cancelBtn.setVisibility(View.GONE);
        }

        String codeNumber = "CODI: "+codeparts[0]+" PUNTUACIÓ: "+puntuacio;
        String number = numberTv.getText().toString();


        cancelBtn.setVisibility(View.VISIBLE);
        startedTv.setSingleLine(false);

        processTv.setText(processState);
        endedTv.setText(endedOn);
        codeTv.setText(codeNumber);

        // Write a message to the database
        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference("codes").child(code);
        dr.child("code").setValue(code);
        dr.child(process.toLowerCase()).setValue(true);
        dr.child(process.toLowerCase()+"Ended").setValue(System.currentTimeMillis());



        endedTv.setSingleLine(false);
        endedTv.setText(endedOn);
        cancelBtn.setVisibility(View.GONE);
        processTv.setText(processState);


        DatabaseReference ordersRef = fd.getReference("users").child(number);
        ordersRef.child("code").setValue("sense codi");
        ordersRef.child("orders").child(currentDate).child(currentDate).child(code).child("ended").setValue(currentDateAndTime);


    }
    public static boolean isValidIndex(String[] arr, int index) {
        return index >= 0 && index < arr.length;
    }
    private void currentProcess(String code,
                                String process, String user
                                ) {
        String currentProcess = process.toLowerCase();
        boolean fastProcesses = currentProcess.equals("corte") ||
                currentProcess.equals("admin") ||
                currentProcess.equals("canteado") ||
                currentProcess.equals("mecanizado") ||
                currentProcess.equals("laca") ||
                currentProcess.equals("embalaje");


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy-HH:mm", Locale.GERMANY);
        String currentDateAndTime = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MM yyyy", Locale.GERMANY);
        String currentDate = sdf1.format(new Date());
        errorTv.setVisibility(View.GONE);
        String processState = "PROCÈS: "+process;

        String[] codeparts = code.split("X");

        String puntuacio;
        if (isValidIndex(codeparts, 1)) {
            puntuacio = codeparts[1];
        } else {
            puntuacio = "s/p";
        }
        String codeNumber = "CODI: "+codeparts[0]+" PUNTUACIÓ: "+puntuacio;
        String startedOn = "COMENÇAT: \n"+currentDateAndTime;
        String endedOn;
        if (fastProcesses) {
            endedOn = "";
            dr.child(process.toLowerCase()+"Started").setValue(System.currentTimeMillis());
            dr.child(process.toLowerCase()+"Ended").setValue(System.currentTimeMillis());
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        } else {
            endedOn = "ACABAT: sense acabar";
            cancelBtn.setVisibility(View.VISIBLE);
            dr.child(process.toLowerCase()+"Started").setValue(System.currentTimeMillis());
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        }

        String number = numberTv.getText().toString();


        startedTv.setSingleLine(false);
        startedTv.setText(startedOn);

        processTv.setText(processState);
        endedTv.setText(endedOn);
        codeTv.setText(codeNumber);
        cancelBtn.setOnClickListener(view -> cancelOrder(code, process, number));

        // Write a message to the database
        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference("codes").child(code);
        dr.child("code").setValue(code);
        dr.child(process.toLowerCase()+"User").setValue(user);
        dr.child(process.toLowerCase(Locale.ROOT)).setValue(true);



        DatabaseReference ordersRef = fd.getReference("users").child(number);

        ordersRef.child("code").setValue(code);
        ordersRef.child("orders").child(currentDate).child(currentDate).child(code).child("started").setValue(currentDateAndTime);
        ordersRef.child("orders").child(currentDate).child("date").setValue(currentDate);
        ordersRef.child("orders").child(currentDate).child(currentDate).child(code).child("code").setValue(code);

    }

    private String intentResult(String contents) {
        return contents;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );
        if (intentResult.getContents() != null){
            String barcode = intentResult(intentResult.getContents());
            String temp = processTv.getText().toString();
            String process = temp.substring(temp.lastIndexOf(" ")+1);
            String name = nameTv.getText().toString();
            fd = FirebaseDatabase.getInstance();
            dr = fd.getReference("codes").child(barcode);

            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   // boolean corte = snapshot.child("corte").getValue();
                    String currentProcess = process.toLowerCase();
                    boolean fastProcesses = currentProcess.equals("corte") ||
                            currentProcess.equals("admin") ||
                            currentProcess.equals("canteado") ||
                            currentProcess.equals("mecanizado") ||
                            currentProcess.equals("laca") ||
                            currentProcess.equals("embalaje");

                    if (currentProcess.equals("corte") && snapshot.child("adminEnded").exists()||
                            currentProcess.equals("canteado") && snapshot.child("corteEnded").exists() ||
                            currentProcess.equals("mecanizado") && snapshot.child("canteadoEnded").exists() ||
                            currentProcess.equals("laca") && snapshot.child("mecanizadoEnded").exists() ||
                            currentProcess.equals("montaje") && !snapshot.child("montajeStarted").exists()
                                    && snapshot.child("lacaEnded").exists()||
                            currentProcess.equals("montaje") && !snapshot.child("montajeStarted").exists()
                                    && snapshot.child("mecanizadoEnded").exists()||
                            currentProcess.equals("embalaje")
                                    && snapshot.child("montajeEnded").exists()&&snapshot.child("espejosEnded").exists() && snapshot.child("cajonesEnded").exists() && snapshot.child("uneroEnded").exists()
                           /* ||
                            currentProcess.equals("transporte") && !snapshot.child("transporteStarted").exists()
                                    && snapshot.child("embalajeEnded").exists()*/
                    ) {
                        currentProcess(barcode, process, name);

                    } else if (!fastProcesses && snapshot.child(currentProcess+"Started").exists() && Objects.equals(Objects.requireNonNull(snapshot.child(currentProcess + "User").getValue()).toString(), nameTv.getText().toString())) {
                        endProcess(process, barcode);

                    } else if (currentProcess.equals("admin")||
                            currentProcess.equals("cajones") && !snapshot.child("cajonesStarted").exists()||
                            currentProcess.equals("unero") && !snapshot.child("uneroStarted").exists()||
                            currentProcess.equals("espejos") && !snapshot.child("espejosStarted").exists()) {
                        currentProcess(barcode, process, name);
                    } else if (currentProcess.equals("cajones")||currentProcess.equals("unero")||currentProcess.equals("espejos")) {
                        endProcess(process, barcode);
                    }

                    else {
                        errorTv.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(AccountActivity.this, "Ups! No has escanejat res", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}