package com.android.vidrebany.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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

import com.android.vidrebany.DataDetailsActivity;
import com.android.vidrebany.R;
import com.android.vidrebany.models.ModelDates;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterDates extends FirebaseRecyclerAdapter<ModelDates, AdapterDates.MyHolder> {

    private Context context;
    private String name, process, number;

    public AdapterDates(@NonNull FirebaseRecyclerOptions<ModelDates> options) { super(options);}

    public AdapterDates(FirebaseRecyclerOptions<ModelDates> options, String name, String process, String number) {
        super(options);
        this.name = name;
        this.process = process;
        this.number = number;

    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder myHolder, int i, @NonNull ModelDates modelDates) {


        myHolder.date.setText(modelDates.getDate());

        myHolder.datesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DataDetailsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("process", process);
                intent.putExtra("number", number);
                intent.putExtra("date", modelDates.getDate());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_order_dates, parent, false);

        context = parent.getContext();

        return new MyHolder(view);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView date;
        ImageButton moreBtn;
        LinearLayout datesLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTv);
            datesLayout = itemView.findViewById(R.id.datesLayout);
        }
    }
}
