package com.android.vidrebany.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.vidrebany.R;
import com.android.vidrebany.UserOrderDatesActivity;
import com.android.vidrebany.models.ModelUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterAdmin extends FirebaseRecyclerAdapter<ModelUsers, AdapterAdmin.MyHolder> {

    private Context context;

    public AdapterAdmin(@NonNull FirebaseRecyclerOptions<ModelUsers> options) { super(options);}

    @Override
    protected void onBindViewHolder(@NonNull MyHolder myHolder, int i, @NonNull ModelUsers modelUsers) {

        String code = "Codi: "+modelUsers.getCode();
        String process = "Procès: "+modelUsers.getProcess();

        myHolder.name.setText(modelUsers.getName());
        myHolder.code.setText(code);
        myHolder.process.setText(process);
        myHolder.usersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserOrderDatesActivity.class);
                intent.putExtra("name", modelUsers.getName());
                intent.putExtra("process", modelUsers.getProcess());
                intent.putExtra("number", modelUsers.getNumber());
                context.startActivity(intent);

            }
        });


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);

        context = parent.getContext();

        return new MyHolder(view);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, code, process;
        LinearLayout usersLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTv);
            code = itemView.findViewById(R.id.codeTv);
            process = itemView.findViewById(R.id.processTv);
            usersLayout = itemView.findViewById(R.id.usersLayout);
        }
    }
}
