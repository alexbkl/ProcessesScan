package com.android.vidrebany.adapters;

import static com.android.vidrebany.AccountActivity.isValidIndex;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.android.vidrebany.R;
import com.android.vidrebany.models.ModelOrders;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.MyHolder> {

    private final Context context;
    private final List<ModelOrders> ordersList;

    public AdapterOrders(Context context, List<ModelOrders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
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
            long corteStartedLong = ordersList.get(position).getCorteStarted();
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

            holder.corteDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteCorte(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        else if (ordersList.get(position).isCanteado()) {
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

            holder.canteadoDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteCanteado(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        else if (ordersList.get(position).isMecanizado()) {
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

            holder.mecanizadoDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteMecanizado(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        else if (ordersList.get(position).isLaca()) {
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

            holder.lacaDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteLaca(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        else if (ordersList.get(position).isMontaje()) {
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

            holder.montajeDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteMontaje(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        else if (ordersList.get(position).isEmbalaje()) {
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

            holder.embalajeDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteEmbalaje(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }
        else if (ordersList.get(position).isCajones()) {
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

            holder.cajonesDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteCajones(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        //isEspejos

        else if (ordersList.get(position).isEspejos()) {
            String espejosName = ordersList.get(position).getEspejosUser();
            long espejosStartedLong = ordersList.get(position).getEspejosStarted();
            long espejosEndedLong = ordersList.get(position).getEspejosEnded();
            Date espejosStartedDate = new Date(espejosStartedLong);
            DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

            String espejosStarted = df.format(espejosStartedDate);
            String espejosEnded;

            if (espejosEndedLong == 0) {
                espejosEnded = "s/a";
            } else {
                Date espejosEndedDate = new Date(espejosEndedLong);
                espejosEnded = df.format(espejosEndedDate);
            }
            String espejosDate = espejosStarted+"\n"+espejosEnded;

            holder.espejosLayout.setVisibility(View.VISIBLE);
            holder.espejosTv.setText("Espejos");
            holder.espejosNameTv.setText(espejosName);

            holder.espejosDateTv.setText(espejosDate);

            holder.espejosDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteEspejos(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        //isUnero

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

            holder.uneroDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteUnero(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }

        else if (ordersList.get(position).isAdmin()) {
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

            holder.adminDeleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar");
                builder.setMessage("¿Está seguro de eliminar el corte?");
                builder.setPositiveButton("Eliminar", (dialog, which) -> deleteAdmin(position));
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
            });

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


    }

    private void deleteCorte(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("corte").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("corteStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("corteEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("corteUser").removeValue();

    }

    private void deleteCanteado(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("canteado").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("canteadoStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("canteadoEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("canteadoUser").removeValue();
    }
    private void deleteMecanizado(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("mecanizado").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("mecanizadoStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("mecanizadoEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("mecanizadoUser").removeValue();
    }
    private void deleteLaca(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("laca").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("lacaStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("lacaEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("lacaUser").removeValue();
    }
    private void deleteEspejos(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("espejos").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("espejosStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("espejosEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("espejosUser").removeValue();
    }
    private void deleteCajones(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("cajones").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("cajonesStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("cajonesEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("cajonesUser").removeValue();
    }
    private void deleteEmbalaje(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("embalaje").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("embalajeStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("embalajeEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("embalajeUser").removeValue();
    }
    private void deleteUnero(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("unero").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("uneroStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("uneroEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("uneroUser").removeValue();
    }

    private void deleteMontaje(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("montaje").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("montajeStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("montajeEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("montajeUser").removeValue();
    }
    //deleteAdmin
    private void deleteAdmin(int position) {
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("admin").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("adminStarted").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("adminEnded").removeValue();
        FirebaseDatabase.getInstance().getReference("codes").child(ordersList.get(position).getCode()).child("adminUser").removeValue();
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }






    static class MyHolder extends RecyclerView.ViewHolder {
        final TextView order;
        final TextView adminTv;
        final TextView adminNameTv;
        final TextView adminDateTv;
        final TextView corteTv;
        final TextView corteNameTv;
        final TextView corteDateTv;
        final TextView canteadoTv;
        final TextView canteadoNameTv;
        final TextView canteadoDateTv;
        final TextView cajonesTv;
        TextView cajonesNameTv;
        final TextView cajonesDateTv;
        final TextView espejosTv;
        final TextView espejosNameTv;
        final TextView espejosDateTv;
        final TextView mecanizadoTv;
        final TextView mecanizadoNameTv;
        final TextView mecanizadoDateTv;
        final TextView embalajeTv;
        final TextView embalajeNameTv;
        final TextView embalajeDateTv;
        final TextView transporteTv;
        final TextView transporteNameTv;
        TextView transporteDateTv;
        final TextView lacaTv;
        final TextView lacaNameTv;
        final TextView lacaDateTv;
        final TextView montajeTv;
        final TextView montajeNameTv;
        final TextView montajeDateTv;
        final TextView uneroTv;
        final TextView uneroNameTv;
        final TextView uneroDateTv;

        final ImageButton uneroDeleteBtn;
        final ImageButton montajeDeleteBtn;
        final ImageButton transporteDeleteBtn;
        final ImageButton lacaDeleteBtn;
        final ImageButton embalajeDeleteBtn;
        final ImageButton mecanizadoDeleteBtn;
        final ImageButton espejosDeleteBtn;
        final ImageButton cajonesDeleteBtn;
        final ImageButton adminDeleteBtn;
        final ImageButton corteDeleteBtn;
        final ImageButton canteadoDeleteBtn;
        final LinearLayout ordersLayout;
        final CardView cardPertanyaan;
        final ConstraintLayout adminLayout;
        final ConstraintLayout corteLayout;
        final ConstraintLayout canteadoLayout;
        final ConstraintLayout cajonesLayout;
        final ConstraintLayout espejosLayout;
        final ConstraintLayout mecanizadoLayout;
        final ConstraintLayout lacaLayout;
        final ConstraintLayout montajeLayout;
        final ConstraintLayout embalajeLayout;
        final ConstraintLayout transporteLayout;
        final ConstraintLayout uneroLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            order = itemView.findViewById(R.id.orderTv);
            uneroDeleteBtn = itemView.findViewById(R.id.uneroDeleteBtn);
            montajeDeleteBtn = itemView.findViewById(R.id.montajeDeleteBtn);
            transporteDeleteBtn = itemView.findViewById(R.id.transporteDeleteBtn);
            lacaDeleteBtn = itemView.findViewById(R.id.lacaDeleteBtn);
            embalajeDeleteBtn = itemView.findViewById(R.id.embalajeDeleteBtn);
            mecanizadoDeleteBtn = itemView.findViewById(R.id.mecanizadoDeleteBtn);
            espejosDeleteBtn = itemView.findViewById(R.id.espejosDeleteBtn);
            cajonesDeleteBtn = itemView.findViewById(R.id.cajonesDeleteBtn);
            corteDeleteBtn = itemView.findViewById(R.id.corteDeleteBtn);
            canteadoDeleteBtn = itemView.findViewById(R.id.canteadoDeleteBtn);
            adminDeleteBtn = itemView.findViewById(R.id.adminDeleteBtn);
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
