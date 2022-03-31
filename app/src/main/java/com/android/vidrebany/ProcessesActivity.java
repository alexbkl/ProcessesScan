package com.android.vidrebany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.vidrebany.adapters.AdapterProcesses;
import com.android.vidrebany.models.ModelProcesses;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ProcessesActivity extends AppCompatActivity {

    RecyclerView processesRecyclerView;
    AdapterProcesses adapterProcesses;
    TextView codeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processes);

        codeTv = findViewById(R.id.codeTv);


        String code = null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Intent intent = new Intent(ProcessesActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                code= extras.getString("code");
                setContent(code);
            }
        } else {
            code= (String) savedInstanceState.getSerializable("code");
            setContent(code);
        }

        processesRecyclerView = findViewById(R.id.processesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        processesRecyclerView.setLayoutManager(layoutManager);

        assert code != null;
        FirebaseRecyclerOptions<ModelProcesses> options = new FirebaseRecyclerOptions.Builder<ModelProcesses>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("codes").child(code).child("process"), ModelProcesses.class)
                .build();
        adapterProcesses = new AdapterProcesses(options, code);
        processesRecyclerView.setAdapter(adapterProcesses);



    }

    private void setContent(String code) {
        codeTv.setText(code);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapterProcesses.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterProcesses.stopListening();
    }
}