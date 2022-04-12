package com.android.vidrebany.adapters;

import static android.text.TextUtils.isEmpty;
import static com.android.vidrebany.AccountActivity.isValidIndex;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.vidrebany.ProcessesActivity;
import com.android.vidrebany.R;
import com.android.vidrebany.models.ModelOrders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.MyHolder> {

    private Context context;
    List<ModelOrders> ordersList;
    RecyclerView ordersRecyclerView;

    public AdapterOrders(Context context, List<ModelOrders> ordersList, RecyclerView ordersRecyclerView) {
        this.context = context;
        this.ordersList = ordersList;
        this.ordersRecyclerView = ordersRecyclerView;


    }
    @NonNull
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout row_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_orders, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {


        if (ordersList.get(position).isCorte()) {

            String corteName = ordersList.get(position).getCorteUser();
            Long corteStartedLong = ordersList.get(position).getCorteStarted();
            long corteEndedLong = ordersList.get(position).getCorteEnded();
            Date corteStartedDate = new Date(corteStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String corteStarted = df.format(corteStartedDate);
            String corteEnded;

            if (corteEndedLong == 0) {
                corteEnded = "s/a";
            } else {
                Date corteEndedDate = new Date(corteEndedLong);
                corteEnded = df.format(corteEndedDate);
            }
            String corteDate = corteStarted+"\n"+corteEnded;

            holder.corteLayout.setVisibility(View.VISIBLE);
            holder.corteTv.setText("Corte");
            holder.corteNameTv.setText(corteName);

            holder.corteDateTv.setText(corteDate);
        }

        if (ordersList.get(position).isCanteado()) {
            String canteadoName = ordersList.get(position).getCanteadoUser();
            long canteadoStartedLong = ordersList.get(position).getCanteadoStarted();
            long canteadoEndedLong = ordersList.get(position).getCanteadoEnded();
            Date canteadoStartedDate = new Date(canteadoStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String canteadoStarted = df.format(canteadoStartedDate);
            String canteadoEnded;

            if (canteadoEndedLong == 0) {
                canteadoEnded = "s/a";
            } else {
                Date canteadondedDate = new Date(canteadoEndedLong);
                canteadoEnded = df.format(canteadondedDate);
            }
            String canteadoDate = canteadoStarted+"\n"+canteadoEnded;

            holder.canteadoLayout.setVisibility(View.VISIBLE);
            holder.canteadoTv.setText("Canteado");
            holder.canteadoNameTv.setText(canteadoName);

            holder.canteadoDateTv.setText(canteadoDate);
        }

        if (ordersList.get(position).isMecanizado()) {
            String mecanizadoName = ordersList.get(position).getMecanizadoUser();
            long mecanizadoStartedLong = ordersList.get(position).getMecanizadoStarted();
            long mecanizadoEndedLong = ordersList.get(position).getMecanizadoEnded();
            Date mecanizadoStartedDate = new Date(mecanizadoStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String mecanizadoStarted = df.format(mecanizadoStartedDate);
            String mecanizadoEnded;

            if (mecanizadoEndedLong == 0) {
                mecanizadoEnded = "s/a";
            } else {
                Date mecanizadoEndedDate = new Date(mecanizadoEndedLong);
                mecanizadoEnded = df.format(mecanizadoEndedDate);
            }
            String mecanizadoDate = mecanizadoStarted+"\n"+mecanizadoEnded;

            holder.mecanizadoLayout.setVisibility(View.VISIBLE);
            holder.mecanizadoTv.setText("Mecanizado");
            holder.mecanizadoNameTv.setText(mecanizadoName);

            holder.mecanizadoDateTv.setText(mecanizadoDate);
        }

        if (ordersList.get(position).isLaca()) {
            String lacaName = ordersList.get(position).getLacaUser();
            long lacaStartedLong = ordersList.get(position).getLacaStarted();
            long lacaEndedLong = ordersList.get(position).getLacaEnded();
            Date lacaStartedDate = new Date(lacaStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String lacaStarted = df.format(lacaStartedDate);
            String lacaEnded;

            if (lacaEndedLong == 0) {
                lacaEnded = "s/a";
            } else {
                Date lacaEndedDate = new Date(lacaEndedLong);
                lacaEnded = df.format(lacaEndedDate);
            }
            String lacaDate = lacaStarted+"\n"+lacaEnded;

            holder.lacaLayout.setVisibility(View.VISIBLE);
            holder.lacaTv.setText("Laca");
            holder.lacaNameTv.setText(lacaName);

            holder.lacaDateTv.setText(lacaDate);
        }

        if (ordersList.get(position).isMontaje()) {
            String montajeName = ordersList.get(position).getMontajeUser();
            long montajeStartedLong = ordersList.get(position).getMontajeStarted();
            long montajeEndedLong = ordersList.get(position).getMontajeEnded();
            Date montajeStartedDate = new Date(montajeStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String montajeStarted = df.format(montajeStartedDate);
            String montajeEnded;

            if (montajeEndedLong == 0) {
                montajeEnded = "s/a";
            } else {
                Date montajeEndedDate = new Date(montajeEndedLong);
                montajeEnded = df.format(montajeEndedDate);
            }
            String montajeDate = montajeStarted+"\n"+montajeEnded;

            holder.montajeLayout.setVisibility(View.VISIBLE);
            holder.montajeTv.setText("Montaje");
            holder.montajeNameTv.setText(montajeName);

            holder.montajeDateTv.setText(montajeDate);
        }

        if (ordersList.get(position).isEmbalaje()) {
            String embalajeName = ordersList.get(position).getEmbalajeUser();
            long embalajeStartedLong = ordersList.get(position).getEmbalajeStarted();
            long embalajeEndedLong = ordersList.get(position).getEmbalajeEnded();
            Date embalajeStartedDate = new Date(embalajeStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String embalajeStarted = df.format(embalajeStartedDate);
            String embalajeEnded;

            if (embalajeEndedLong == 0) {
                embalajeEnded = "s/a";
            } else {
                Date embalajeEndedDate = new Date(embalajeEndedLong);
                embalajeEnded = df.format(embalajeEndedDate);
            }
            String embalajeDate = embalajeStarted+"\n"+embalajeEnded;

            holder.embalajeLayout.setVisibility(View.VISIBLE);
            holder.embalajeTv.setText("Embalaje");
            holder.embalajeNameTv.setText(embalajeName);

            holder.embalajeDateTv.setText(embalajeDate);
        }

        if (ordersList.get(position).isTransporte()) {
            String transporteName = ordersList.get(position).getTransporteUser();
            long transporteStartedLong = ordersList.get(position).getTransporteStarted();
            long transporteEndedLong = ordersList.get(position).getTransporteEnded();
            Date transporteStartedDate = new Date(transporteStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String transporteStarted = df.format(transporteStartedDate);
            String transporteEnded;

            if (transporteEndedLong == 0) {
                transporteEnded = "s/a";
            } else {
                Date transporteEndedDate = new Date(transporteEndedLong);
                transporteEnded = df.format(transporteEndedDate);
            }
            String transporteDate = transporteStarted+"\n"+transporteEnded;

            holder.transporteLayout.setVisibility(View.VISIBLE);
            holder.transporteTv.setText("Transporte");
            holder.transporteNameTv.setText(transporteName);

            holder.transporteDateTv.setText(transporteDate);
        }

        if (ordersList.get(position).isCajones()) {
            String cajonesName = ordersList.get(position).getCajonesUser();
            long cajonesStartedLong = ordersList.get(position).getCajonesStarted();
            long cajonesEndedLong = ordersList.get(position).getCajonesEnded();
            Date cajonesStartedDate = new Date(cajonesStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String cajonesStarted = df.format(cajonesStartedDate);
            String cajonesEnded;

            if (cajonesEndedLong == 0) {
                cajonesEnded = "s/a";
            } else {
                Date cajonesEndedDate = new Date(cajonesEndedLong);
                cajonesEnded = df.format(cajonesEndedDate);
            }
            String cajonesDate = cajonesStarted+"\n"+cajonesEnded;

            holder.cajonesLayout.setVisibility(View.VISIBLE);
            holder.cajonesTv.setText("Cajones");
            holder.cajonesNameTv.setText(cajonesName);

            holder.cajonesDateTv.setText(cajonesDate);
        }

        if (ordersList.get(position).isEspejo()) {
            String espejoName = ordersList.get(position).getEspejosUser();
            long espejoStartedLong = ordersList.get(position).getEspejosStarted();
            long espejoEndedLong = ordersList.get(position).getEspejosEnded();
            Date espejoStartedDate = new Date(espejoStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");
            String espejoStarted = df.format(espejoStartedDate);
            String espejoEnded;

            if (espejoEndedLong == 0) {
                espejoEnded = "s/a";
            } else {
                Date espejoEndedDate = new Date(espejoEndedLong);
                espejoEnded = df.format(espejoEndedDate);
            }
            String espejoDate = espejoStarted+"\n"+espejoEnded;
            holder.espejosLayout.setVisibility(View.VISIBLE);
            holder.espejosTv.setText("Espejo");
            holder.espejosNameTv.setText(espejoName);
            holder.espejosDateTv.setText(espejoDate);
        }


        if (ordersList.get(position).isUnero()) {
            String uneroName = ordersList.get(position).getUneroUser();
            long uneroStartedLong = ordersList.get(position).getUneroStarted();
            long uneroEndedLong = ordersList.get(position).getUneroEnded();
            Date uneroStartedDate = new Date(uneroStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String uneroStarted = df.format(uneroStartedDate);
            String uneroEnded;

            if (uneroEndedLong == 0) {
                uneroEnded = "s/a";
            } else {
                Date uneroEndedDate = new Date(uneroEndedLong);
                uneroEnded = df.format(uneroEndedDate);
            }
            String uneroDate = uneroStarted+"\n"+uneroEnded;
            holder.uneroLayout.setVisibility(View.VISIBLE);
            holder.uneroTv.setText("Unero");
            holder.uneroNameTv.setText(uneroName);
            holder.uneroDateTv.setText(uneroDate);
        }

        if (ordersList.get(position).isAdmin()) {
            String adminName = ordersList.get(position).getAdminUser();
            long adminStartedLong = ordersList.get(position).getAdminStarted();
            long adminEndedLong = ordersList.get(position).getAdminEnded();
            Date adminStartedDate = new Date(adminStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String adminStarted = df.format(adminStartedDate);
            String adminEnded;

            if (adminEndedLong == 0) {
                adminEnded = "s/a";
            }
            else {
                Date adminEndedDate = new Date(adminEndedLong);
                adminEnded = df.format(adminEndedDate);
            }
            String adminDate = adminStarted+"\n"+adminEnded;
            holder.adminLayout.setVisibility(View.VISIBLE);
            holder.adminTv.setText("Admin");
            holder.adminNameTv.setText(adminName);
            holder.adminDateTv.setText(adminDate);

        }


        String code = ordersList.get(position).getCode();

        String[] codeparts = code.split("X");

        String puntuacio;
        if (isValidIndex(codeparts, 1)) {
            puntuacio = codeparts[1];
        } else {
            puntuacio = "s/p";
        }
        String codeNumber = "CODI: "+codeparts[0]+"\n PUNTUACIÓ: "+puntuacio;


        holder.order.setText(codeNumber);




   /*     holder.ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProcessesActivity.class);
                intent.putExtra("code", ordersList.get(position).getCode());
                context.startActivity(intent);
            }
        });
*/
        holder.deleteBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar ordre")
                    .setMessage("Estàs segur de que vols eliminar aquest número d'ordre?")

                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).removeValue();
                            Toast.makeText(context, "Eliminat!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .setNegativeButton("No", null)
                    .setIcon(R.drawable.ic_delete)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }






    class MyHolder extends RecyclerView.ViewHolder {
        TextView order,
                adminTv, adminNameTv, adminDateTv,
                corteTv, corteNameTv, corteDateTv,
                canteadoTv, canteadoNameTv, canteadoDateTv,
                cajonesTv, cajonesNameTv, cajonesDateTv,
                espejosTv, espejosNameTv, espejosDateTv,
                mecanizadoTv, mecanizadoNameTv, mecanizadoDateTv,
                embalajeTv, embalajeNameTv, embalajeDateTv,
                transporteTv, transporteNameTv, transporteDateTv,
                lacaTv, lacaNameTv, lacaDateTv,
                montajeTv, montajeNameTv, montajeDateTv,
                uneroTv, uneroNameTv, uneroDateTv;

        ImageButton deleteBtn;
        LinearLayout ordersLayout;
        CardView cardPertanyaan;
        ConstraintLayout adminLayout, corteLayout, canteadoLayout, cajonesLayout, espejosLayout,
                mecanizadoLayout, lacaLayout, montajeLayout, embalajeLayout, transporteLayout, uneroLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            order = itemView.findViewById(R.id.orderTv);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            ordersLayout = itemView.findViewById(R.id.ordersLayout);
            adminLayout = itemView.findViewById(R.id.adminLayout);
            cardPertanyaan = itemView.findViewById(R.id.card_pertanyaan);
            corteLayout = itemView.findViewById(R.id.corteLayout);
            canteadoLayout = itemView.findViewById(R.id.canteadoLayout);
            cajonesLayout = itemView.findViewById(R.id.cajonesLayout);
            espejosLayout = itemView.findViewById(R.id.espejosLayout);
            mecanizadoLayout = itemView.findViewById(R.id.mecanizadoLayout);
            lacaLayout = itemView.findViewById(R.id.lacaLayout);
            lacaTv = itemView.findViewById(R.id.lacaTv);
            lacaNameTv = itemView.findViewById(R.id.lacaNameTv);
            lacaDateTv = itemView.findViewById(R.id.lacaDateTv);
            montajeLayout = itemView.findViewById(R.id.montajeLayout);
            embalajeLayout = itemView.findViewById(R.id.embalajeLayout);
            transporteLayout = itemView.findViewById(R.id.transporteLayout);
            uneroLayout = itemView.findViewById(R.id.uneroLayout);
            adminTv = itemView.findViewById(R.id.adminTv);
            corteTv = itemView.findViewById(R.id.corteTv);
            corteNameTv = itemView.findViewById(R.id.corteNameTv);
            corteDateTv = itemView.findViewById(R.id.corteDateTv);
            canteadoTv = itemView.findViewById(R.id.canteadoTv);
            canteadoNameTv = itemView.findViewById(R.id.canteadoNameTv);
            canteadoDateTv = itemView.findViewById(R.id.canteadoDateTv);
            cajonesTv = itemView.findViewById(R.id.cajonesTv);
            cajonesNameTv = itemView.findViewById(R.id.cajonesNameTv);
            cajonesNameTv = itemView.findViewById(R.id.cajonesNameTv);
            cajonesDateTv = itemView.findViewById(R.id.cajonesDateTv);
            espejosTv = itemView.findViewById(R.id.espejosTv);
            espejosNameTv = itemView.findViewById(R.id.espejosNameTv);
            espejosDateTv = itemView.findViewById(R.id.espejosDateTv);
            mecanizadoTv = itemView.findViewById(R.id.mecanizadoTv);
            mecanizadoNameTv = itemView.findViewById(R.id.mecanizadoNameTv);
            mecanizadoDateTv = itemView.findViewById(R.id.mecanizadoDateTv);
            embalajeTv = itemView.findViewById(R.id.embalajeTv);
            embalajeNameTv = itemView.findViewById(R.id.embalajeNameTv);
            embalajeDateTv = itemView.findViewById(R.id.embalajeDateTv);
            transporteTv = itemView.findViewById(R.id.transporteTv);
            transporteNameTv = itemView.findViewById(R.id.transporteNameTv);
            transporteDateTv = itemView.findViewById(R.id.transporteDateTv);
            montajeTv = itemView.findViewById(R.id.montajeTv);
            montajeNameTv = itemView.findViewById(R.id.montajeNameTv);
            montajeDateTv = itemView.findViewById(R.id.montajeDateTv);
            transporteDateTv = itemView.findViewById(R.id.transporteDateTv);
            uneroTv = itemView.findViewById(R.id.uneroTv);
            uneroNameTv = itemView.findViewById(R.id.uneroNameTv);
            uneroDateTv = itemView.findViewById(R.id.uneroDateTv);
            adminNameTv = itemView.findViewById(R.id.adminNameTv);
            adminDateTv = itemView.findViewById(R.id.adminDateTv);







        }
    }


}
