package com.android.vidrebany.adapters;

import android.app.AlertDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.android.vidrebany.AccountActivity.isValidIndex;

import java.util.Objects;

public class AdapterUsers extends FirebaseRecyclerAdapter<ModelUsers, AdapterUsers.MyHolder> {


    public AdapterUsers(@NonNull FirebaseRecyclerOptions<ModelUsers> options) { super(options);}

    private FirebaseDatabase fd;
    private DatabaseReference dr;

    @Override
    protected void onBindViewHolder(@NonNull MyHolder myHolder, int i, @NonNull ModelUsers modelUsers) {
        String code;
        fd = FirebaseDatabase.getInstance();

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
        String process = "Procés assignat: "+modelUsers.getProcess();

        myHolder.name.setText(modelUsers.getName());
        myHolder.process.setText(process);
        myHolder.usersLayout.setOnClickListener(view -> {
        boolean isAdmin = Objects.equals(modelUsers.getProcess(), "Admin");
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Escolliu el vostre procés");

            // add a list
            String[] processList = {"Corte", "Canteado", "Mecanizado", "Laca", "Montaje","Embalaje", "Transporte", "Cajones", "Unero", "Espejos"};
            builder.setItems(processList, (dialog, which) -> {
                switch (which) {
                    case 0: // horse
                        modelUsers.setProcess("Corte");
                        break;
                    case 1: // cow
                        modelUsers.setProcess("Canteado");
                        break;
                    case 2: // camel
                        modelUsers.setProcess("Mecanizado");
                        break;
                    case 3: // sheep
                        modelUsers.setProcess("Laca");
                        break;
                    case 4: // goat
                        modelUsers.setProcess("Montaje");
                        break;
                    case 5: // goat
                        modelUsers.setProcess("Embalaje");
                        break;
                    case 6: // goat
                        modelUsers.setProcess("Transporte");
                        break;
                    case 7: // goat
                        modelUsers.setProcess("Cajones");
                        break;
                    case 8: // goat
                        modelUsers.setProcess("Unero");
                        break;
                    case 9: // goat
                        modelUsers.setProcess("Espejos");
                        break;
                }
                //assign process to user in database
                dr = fd.getReference("users").child(modelUsers.getNumber());
                dr.child("process").setValue(modelUsers.getProcess());

                //pass intent data to AccountActivity
                Intent intent = new Intent(view.getContext(), AccountActivity.class);
                intent.putExtra("name",modelUsers.getName());
                intent.putExtra("process",modelUsers.getProcess());
                intent.putExtra("number", modelUsers.getNumber());
                view.getContext().startActivity(intent);


                Toast.makeText(view.getContext(), ""+modelUsers.getName()+": "+modelUsers.getProcess(), Toast.LENGTH_SHORT).show();
            });

// create and show the alert dialog
            if (!isAdmin) {
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //pass intent data to AccountActivity
                Intent intent = new Intent(view.getContext(), AccountActivity.class);
                intent.putExtra("name",modelUsers.getName());
                intent.putExtra("process",modelUsers.getProcess());
                intent.putExtra("number", modelUsers.getNumber());
                view.getContext().startActivity(intent);


                Toast.makeText(view.getContext(), ""+modelUsers.getName()+": "+modelUsers.getProcess(), Toast.LENGTH_SHORT).show();

            }


          
        });


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);


        return new MyHolder(view);
    }





    static class MyHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView code;
        final TextView process;
        final LinearLayout usersLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTv);
            code = itemView.findViewById(R.id.codeTv);
            process = itemView.findViewById(R.id.processTv);
            usersLayout = itemView.findViewById(R.id.usersLayout);

        }
    }
}
