package com.android.vidrebany.fragments;

import android.os.Bundle;

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
import com.google.firebase.database.FirebaseDatabase;

public class AdminUsersFragment extends Fragment {

    RecyclerView usersRecyclerView;
    AdapterAdmin adapterAdmin;


    public static AdminUsersFragment newInstance() {
        return new AdminUsersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<ModelUsers> options = new FirebaseRecyclerOptions.Builder<ModelUsers>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), ModelUsers.class)
                .build();
        adapterAdmin = new AdapterAdmin(options);
        usersRecyclerView.setAdapter(adapterAdmin);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterAdmin.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapterAdmin.stopListening();
    }
}