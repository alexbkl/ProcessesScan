package com.android.vidrebany.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.vidrebany.AccountActivity;
import com.android.vidrebany.R;
import com.android.vidrebany.models.ModelUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import static com.android.vidrebany.AccountActivity.isValidIndex;

public class AdapterUsers extends FirebaseRecyclerAdapter<ModelUsers, AdapterUsers.MyHolder> {


    public AdapterUsers(@NonNull FirebaseRecyclerOptions<ModelUsers> options) { super(options);}

    @Override
    protected void onBindViewHolder(@NonNull MyHolder myHolder, int i, @NonNull ModelUsers modelUsers) {
        String code;
        if (modelUsers.getCode() == null) {
            code = "sense codi actiu";
        } else {
            String codeRaw = modelUsers.getCode();

            String[] codeparts = codeRaw.split("X");

            String puntuacio;
            if (isValidIndex(codeparts, 1)) {
                puntuacio = codeparts[1];
            } else {
                puntuacio = "s/p";
            }
            code = "CODI: "+codeparts[0]+"\n PUNTUACIÓ: "+puntuacio;

        }



        myHolder.code.setText(code);
        String process = "Procès assignat: "+modelUsers.getProcess();

        myHolder.name.setText(modelUsers.getName());
        myHolder.process.setText(process);
        myHolder.usersLayout.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AccountActivity.class);
            intent.putExtra("name",modelUsers.getName());
            intent.putExtra("process",modelUsers.getProcess());
            intent.putExtra("number", modelUsers.getNumber());
            view.getContext().startActivity(intent);
            Toast.makeText(view.getContext(), ""+modelUsers.getName(), Toast.LENGTH_SHORT).show();
        });


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);


        return new MyHolder(view);
    }





    static class MyHolder extends RecyclerView.ViewHolder {
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
