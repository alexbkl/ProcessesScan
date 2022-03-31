package com.android.vidrebany.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.vidrebany.R;
import com.android.vidrebany.models.ModelProcesses;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterProcesses extends FirebaseRecyclerAdapter<ModelProcesses, AdapterProcesses.MyHolder> {

    private Context context;
    private String code;

    public AdapterProcesses(@NonNull FirebaseRecyclerOptions<ModelProcesses> options) {
        super(options);
    }

    public AdapterProcesses(FirebaseRecyclerOptions<ModelProcesses> options, String code) {
        super(options);
        this.code = code;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder myHolder, int i, @NonNull ModelProcesses modelProcesses) {
        String started = "Començat: "+modelProcesses.getStarted();
        if (modelProcesses.getEnded() == null) {
            String ended = "Acabat: sense acabar";
            myHolder.endedTv.setText(ended);

        } else {
            String ended = "Acabat: "+modelProcesses.getEnded();
            myHolder.endedTv.setText(ended);
        }


        myHolder.processTv.setText(modelProcesses.getProcess());
        myHolder.userTv.setText(modelProcesses.getUser());
        myHolder.startedTv.setText(started);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_processes, parent, false);

        context = parent.getContext();

        return new MyHolder(view);


        }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView processTv, userTv, startedTv, endedTv;
        LinearLayout processesLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            processTv = itemView.findViewById(R.id.processTv);
            userTv = itemView.findViewById(R.id.userTv);
            startedTv = itemView.findViewById(R.id.startedTv);
            endedTv = itemView.findViewById(R.id.endedTv);
            processesLayout = itemView.findViewById(R.id.processesLayout);
        }
    }
}
