package com.android.vidrebany.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.vidrebany.R;
import com.android.vidrebany.UserOrderDatesActivity;
import com.android.vidrebany.models.ModelUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class AdapterAdmin extends FirebaseRecyclerAdapter<ModelUsers, AdapterAdmin.MyHolder> {

    private Context context;


    public AdapterAdmin(@NonNull FirebaseRecyclerOptions<ModelUsers> options) { super(options);}

    @Override
    protected void onBindViewHolder(@NonNull MyHolder myHolder, int i, @NonNull ModelUsers modelUsers) {

        String process = "Procés: "+modelUsers.getProcess();

        myHolder.name.setText(modelUsers.getName());
        myHolder.process.setText(process);

        if (Objects.equals(modelUsers.getProcess(), "Admin")) {
            myHolder.deleteBtn.setVisibility(View.GONE);
        } else {
            myHolder.deleteBtn.setVisibility(View.VISIBLE);
        }
        myHolder.deleteBtn.setOnClickListener(view -> FirebaseDatabase.getInstance().getReference("users").child(modelUsers.getNumber()).removeValue());

        myHolder.usersLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserOrderDatesActivity.class);
            intent.putExtra("name", modelUsers.getName());
            intent.putExtra("process", modelUsers.getProcess());
            intent.putExtra("number", modelUsers.getNumber());
            context.startActivity(intent);

        });

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users_admin, parent, false);

        context = parent.getContext();

        return new MyHolder(view);
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView code;
        final TextView process;
        final LinearLayout usersLayout;
        final ImageButton deleteBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTv);
            code = itemView.findViewById(R.id.codeTv);
            process = itemView.findViewById(R.id.processTv);
            usersLayout = itemView.findViewById(R.id.usersLayout);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
