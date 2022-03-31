package com.android.vidrebany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.vidrebany.adapters.AdapterDates;
import com.android.vidrebany.adapters.AdapterDatesDetails;
import com.android.vidrebany.models.ModelDates;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class UserOrderDatesActivity extends AppCompatActivity {

    RecyclerView datesRecyclerView;
    AdapterDates adapterDates;
    TextView nameTv, processTv, numberTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_dates);

        nameTv = findViewById(R.id.nameTv);
        processTv = findViewById(R.id.processTv);
        numberTv = findViewById(R.id.numberTv);


        String name = null, process = null, number;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Intent intent = new Intent(UserOrderDatesActivity.this, LoginActivity.class);
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

        number = numberTv.getText().toString();



        datesRecyclerView = findViewById(R.id.datesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        datesRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<ModelDates> options = new FirebaseRecyclerOptions.Builder<ModelDates>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(number).child("orders"), ModelDates.class)
                .build();
        adapterDates = new AdapterDates(options, name, process, number);
        datesRecyclerView.setAdapter(adapterDates);





    }

    private void setContent(String name, String process, String number) {
        nameTv.setText(name);
        processTv.setText(process);
        numberTv.setText(number);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapterDates.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterDates.stopListening();
    }
}