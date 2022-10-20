package com.android.vidrebany.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.vidrebany.R;
import com.android.vidrebany.adapters.AdapterAdmin;
import com.android.vidrebany.models.ModelUsers;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class AdminUsersFragment extends Fragment {

    RecyclerView usersRecyclerView;
    AdapterAdmin adapterAdmin;
    Button addUserBtn;
    FirebaseDatabase fd;
    DatabaseReference usersRef;


    public static AdminUsersFragment newInstance() {
        return new AdminUsersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fd = FirebaseDatabase.getInstance();
        usersRef = fd.getReference("users");

        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(layoutManager);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseRecyclerOptions<ModelUsers> options = new FirebaseRecyclerOptions.Builder<ModelUsers>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), ModelUsers.class)
                        .build();
                adapterAdmin = new AdapterAdmin(options);
                usersRecyclerView.setAdapter(adapterAdmin);
                adapterAdmin.startListening();
            }

                                           @Override
                                           public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                           }
                                       });





        addUserBtn = view.findViewById(R.id.addUserBtn);



        addUserBtn.setOnClickListener(v -> {
            //get all key values of usersRef and put them in an array
            final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final Integer[] keys = new Integer[(int) dataSnapshot.getChildrenCount()];
                        int i = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            keys[i] = Integer.valueOf(Objects.requireNonNull(ds.getKey()));
                            i++;
                        }
                        //find missing number in keys array

                        int missingNumber = ((keys.length + 1) * (keys.length + 2)) / 2;
                        for (Integer key : keys) {
                            missingNumber -= key;
                        }

                        //create a dialog to add a new user with name field
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Add User")
                                .setMessage("Nom de l'usuari:");

                        final EditText input = new EditText(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        int finalMissingNumber = missingNumber;
                        alertDialog.setView(input)
                                .setPositiveButton("Afegir",
                                        (dialog, which) -> {
                                            //add the user to the database
                                            String name = input.getText().toString();
                                            if (name.isEmpty()) {
                                                dialog.dismiss();
                                            } else {


                                                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        String number;
                                                        if (keys.length == 0) {
                                                            number = "1";
                                                        } else {
                                                            number = String.valueOf(finalMissingNumber);
                                                        }
                                                        snapshot.getRef().child(number).child("code").setValue("sense codi");
                                                        snapshot.getRef().child(number).child("number").setValue(number);
                                                        snapshot.getRef().child(number).child("name").setValue(name);
                                                        snapshot.getRef().child(number).child("process").setValue("sense procés");

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });
                                                dialog.dismiss();
                                            }
                                        })
                                .setNegativeButton("Cancel·lar", (dialog, which) -> dialog.cancel())
                                .show();
                    } else {

                        //create a dialog to add a new user with name field
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Add User")
                                .setMessage("Nom de l'usuari:");

                        final EditText input = new EditText(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        alertDialog.setView(input)
                                .setPositiveButton("Afegir",
                                        (dialog, which) -> {
                                            //add the user to the database
                                            String name = input.getText().toString();
                                            if (name.isEmpty()) {
                                                dialog.dismiss();
                                            } else {


                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                snapshot.getRef().child("1").child("code").setValue("sense codi");
                                snapshot.getRef().child("1").child("number").setValue("1");
                                snapshot.getRef().child("1").child("name").setValue(name);
                                snapshot.getRef().child("1").child("process").setValue("sense procés");

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }

                        });}})

                        .setNegativeButton("Cancel·lar", (dialog, which) -> dialog.cancel())
                                                        .show();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error: "+error, Toast.LENGTH_SHORT).show();
                }
            });



        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapterAdmin != null) {
            adapterAdmin.startListening();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (adapterAdmin != null) {
            adapterAdmin.stopListening();
        }
    }
}