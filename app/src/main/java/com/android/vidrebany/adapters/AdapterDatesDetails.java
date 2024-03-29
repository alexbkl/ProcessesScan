package com.android.vidrebany.adapters;

import static com.android.vidrebany.AccountActivity.isValidIndex;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.vidrebany.R;
import com.android.vidrebany.models.ModelDatesDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterDatesDetails extends RecyclerView.Adapter<AdapterDatesDetails.MyHolder> {

    private final String number;
    private final String date;
    private final Context context;
    private final List<ModelDatesDetails> datesDetailsList;

    public AdapterDatesDetails(Context context, List<ModelDatesDetails> datesDetailsList, String date, String number) {
        this.number = number;
        this.date = date;
        this.context = context;
        this.datesDetailsList = datesDetailsList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_data_details, viewGroup, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        String process = datesDetailsList.get(i).getProcess();
        String code = datesDetailsList.get(i).getCode(),
                temp1 = datesDetailsList.get(i).getStarted(),
                temp3 = datesDetailsList.get(i).getEnded();
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
            String codeNumber = "CODI: "+codeparts[0]+"\n PUNTUACIÓ: "+puntuacio;


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
            DatabaseReference processesRef = FirebaseDatabase.getInstance().getReference().child("processes").child(process).child(date).child(date).child(codeIndex);

            DatabaseReference codesRef = FirebaseDatabase.getInstance().getReference().child("codes").child(codeIndex);

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar")
                    .setMessage("Eliminar aquesta ordre?")
                    .setPositiveButton("Si", (dialog, which) -> {
                        profileRef.removeValue();
                        processesRef.removeValue();
                        codesRef.child(process.toLowerCase()).removeValue();
                        codesRef.child(process.toLowerCase()+"Started").removeValue();
                        codesRef.child(process.toLowerCase()+"Ended").removeValue();
                        codesRef.child(process.toLowerCase()+"User").removeValue();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()
                    ).show();

        });


        String processTxt = "PROCÉS: "+process;
        myHolder.processTv.setText(processTxt);





    }


    @Override
    public int getItemCount() {
        return datesDetailsList.size();
    }


    static class MyHolder extends RecyclerView.ViewHolder {
        final LinearLayout ordersLayout;
        final TextView codeTv;
        final TextView processTv;
        final TextView startedTv;
        final TextView endedTv;
        final ImageButton deleteBtn;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            codeTv = itemView.findViewById(R.id.codeTv);
            processTv = itemView.findViewById(R.id.processTv);
            startedTv = itemView.findViewById(R.id.startedTv);
            endedTv = itemView.findViewById(R.id.endedTv);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            ordersLayout = itemView.findViewById(R.id.ordersLayout);
        }
    }
}
