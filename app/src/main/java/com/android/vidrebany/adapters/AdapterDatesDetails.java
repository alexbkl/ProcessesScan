package com.android.vidrebany.adapters;

import static com.android.vidrebany.AccountActivity.isValidIndex;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.vidrebany.R;
import com.android.vidrebany.models.ModelDatesDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdapterDatesDetails extends FirebaseRecyclerAdapter<ModelDatesDetails, AdapterDatesDetails.MyHolder> {

    private final String process;
    private final String number;
    private final String date;


    public AdapterDatesDetails(FirebaseRecyclerOptions<ModelDatesDetails> options, String process, String number, String date) {
        super(options);
        this.process = process;
        this.number = number;
        this.date = date;

    }



    @Override
    protected void onBindViewHolder(@NonNull MyHolder myHolder, int i, @NonNull ModelDatesDetails modelDatesDetails) {

        String code = modelDatesDetails.getCode(),
                temp1 = modelDatesDetails.getStarted(),
                temp3 = modelDatesDetails.getEnded();
        if (temp1 == null) {
            String temp2 = "a un altre dia o sense començar";
            String temp4 = temp3.substring(temp3.lastIndexOf(" ")+1);
            String started = "Començat: "+temp2,
                    ended = "Acabat: "+temp4;
            myHolder.startedTv.setText(started);
            myHolder.endedTv.setText(ended);

            String[] codeparts = code.split("X");

            String puntuacio;
            if (isValidIndex(codeparts, 1)) {
                puntuacio = codeparts[1];
            } else {
                puntuacio = "s/p";
            }
            String codeNumber = "CODI: "+codeparts[0]+" PUNTUACIÓ: "+puntuacio;


            myHolder.codeTv.setText(codeNumber);
        } else if (temp3 == null) {
            String temp2 = temp1.substring(temp1.lastIndexOf(" ") + 1);
            String temp4 = "a un altre dia o sense acabar";
            String started = "Començat: "+temp2,
                    ended = "Acabat: "+temp4;

            String[] codeparts = code.split("X");

            String puntuacio;
            if (isValidIndex(codeparts, 1)) {
                puntuacio = codeparts[1];
            } else {
                puntuacio = "s/p";
            }
            String codeNumber = "CODI: "+codeparts[0]+" PUNTUACIÓ: "+puntuacio;

            myHolder.startedTv.setText(started);
            myHolder.endedTv.setText(ended);
            myHolder.codeTv.setText(codeNumber);
        }

        else {
            String temp2 = temp1.substring(temp1.lastIndexOf(" ") + 1);
            String temp4 = temp3.substring(temp3.lastIndexOf(" ")+1);
            String started = "Començat: \n"+temp2,
                    ended = "Acabat: \n"+temp4;
            String[] codeparts = code.split("X");

            String puntuacio;
            if (isValidIndex(codeparts, 1)) {
                puntuacio = codeparts[1];
            } else {
                puntuacio = "s/p";
            }



            String codeNumber = "CODI: "+codeparts[0]+" PUNTUACIÓ: "+puntuacio;


            myHolder.startedTv.setText(started);
            myHolder.endedTv.setText(ended);
            myHolder.codeTv.setText(codeNumber);



        }
        myHolder.deleteBtn.setOnClickListener(v -> {
            //get last index of the code
            String codeIndex = code.substring(code.lastIndexOf(" ")+1);
            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference().child("users").child(number).child("orders").child(date).child(date).child(codeIndex);
            DatabaseReference codesRef = FirebaseDatabase.getInstance().getReference().child("codes").child(codeIndex);

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar")
                    .setMessage("Eliminar aquesta ordre?")
                    .setPositiveButton("Si", (dialog, which) -> {
                        profileRef.removeValue();
                        codesRef.child(process.toLowerCase()).removeValue();
                        codesRef.child(process.toLowerCase()+"Started").removeValue();
                        codesRef.child(process.toLowerCase()+"Ended").removeValue();
                        codesRef.child(process.toLowerCase()+"User").removeValue();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    }
                    ).show();

        });








    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data_details, parent, false);

        /*context = parent.getContext();*/

        return new MyHolder(view);
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout ordersLayout;
        TextView codeTv, startedTv, endedTv;
        ImageButton deleteBtn;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            codeTv = itemView.findViewById(R.id.codeTv);
            startedTv = itemView.findViewById(R.id.startedTv);
            endedTv = itemView.findViewById(R.id.endedTv);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            ordersLayout = itemView.findViewById(R.id.ordersLayout);
        }
    }
}
