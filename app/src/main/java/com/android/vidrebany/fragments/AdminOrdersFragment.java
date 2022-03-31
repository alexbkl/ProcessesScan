package com.android.vidrebany.fragments;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.vidrebany.R;
import com.android.vidrebany.adapters.AdapterOrders;
import com.android.vidrebany.models.ModelOrders;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AdminOrdersFragment extends Fragment {

    RecyclerView ordersRecyclerView;
    AdapterOrders adapterOrders;
    ModelOrders modelo = new ModelOrders();
    List<ModelOrders> ordersList;


    public static AdminOrdersFragment newInstance() {
        return new AdminOrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //set layout to recyclerview
        ordersRecyclerView.setLayoutManager(layoutManager);
        ordersRecyclerView.setNestedScrollingEnabled(true);

        ordersList = new ArrayList<>();


        loadOrders();


        return view;
    }

    private void loadOrders() {

        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("codes");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    ModelOrders modelOrders = ds.getValue(ModelOrders.class);
                    assert modelOrders != null;






                    final String[] inded = {""};

                    //path of all posts
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("codes");
                    //get all data from this ref
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int n = 0;

                            for (DataSnapshot ds: snapshot.getChildren()) {

                                if (ds.hasChild("process")) {
                                    if (ds.child("process").hasChild("Corte")) {
                                        if (ds.child("process").child("Corte").child("ended").getValue() != null) {
                                            String ended = Objects.requireNonNull(ds.child("process").child("Corte").child("ended").getValue()).toString();
                                            if (!isEmpty(ended)) {
                                                System.out.println("ended:" +ended);
                                                System.out.println("indid:"+inded[n]);
                                                n++;


                                            } else {
                                                //          System.out.println("ended: a un altre dia o sense acabar");
                                            }
                                        }



                                        //  hashMap.put("corteDateTv", Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString());
                                        //  System.out.println("started:" + Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString());
                                      //  corteDateTv = Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString();

                                    }

                                    //     String started = Objects.requireNonNull(snapshot.child("process").child("Corte").child("started").getValue()).toString();
                                    //     String ended = Objects.requireNonNull(dsa.child("Corte").child("ended").getValue()).toString();
                                    //    System.out.println("Started: "+started+"\nEnded: "+ended);


                                }





                            }
                            //   adapterOrders.setProcessMap(hashMap);
                            //  adapterOrders.notifyDataSetChanged();
                            System.out.println("inded:" + Arrays.toString(inded));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //in case of error

                        }
                    });





                    ordersList.add(modelOrders);
                    adapterOrders = new AdapterOrders(getActivity(), ordersList, ordersRecyclerView);
                    adapterOrders.setHasStableIds(true);
                    ordersRecyclerView.setAdapter(adapterOrders);

                    if (ds.hasChild("process")) {
                        if (ds.child("process").hasChild("Corte")) {
                            Object ended = ds.child("process").child("Corte").child("ended").getValue();
                            if (ended != null) {
                                System.out.println("ended:" +ended.toString());



                            } else {
                                System.out.println("ended: a un altre dia o sense acabar");
                            }


                            //  hashMap.put("corteDateTv", Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString());
                            System.out.println("started:" + Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString());
                   //         corteDateTv = Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString();

                        }

                        //     String started = Objects.requireNonNull(snapshot.child("process").child("Corte").child("started").getValue()).toString();
                        //     String ended = Objects.requireNonNull(dsa.child("Corte").child("ended").getValue()).toString();
                        //    System.out.println("Started: "+started+"\nEnded: "+ended);


                    }





                }
                //   adapterOrders.setProcessMap(hashMap);
                //  adapterOrders.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error

            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!isEmpty(s)) {
                    searchOrders(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchOrders(String s) {

        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("codes");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (ds.hasChild("process")) {
                        if (ds.child("process").hasChild("Corte")) {
                            Object ended = ds.child("process").child("Corte").child("ended").getValue();
                            if (ended != null) {
                                System.out.println("ended:" +ended.toString());


                            } else {
                                System.out.println("ended: a un altre dia o sense acabar");
                            }
                            System.out.println("started:" + Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString());

                        }

                            //     String started = Objects.requireNonNull(snapshot.child("process").child("Corte").child("started").getValue()).toString();
                            //     String ended = Objects.requireNonNull(dsa.child("Corte").child("ended").getValue()).toString();
                            //    System.out.println("Started: "+started+"\nEnded: "+ended);


                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error

            }
        });

        /*
        //path of all orders
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("codes");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelOrders modelOrders = ds.getValue(ModelOrders.class);

                    assert modelOrders != null;
                    if(modelOrders.getCode().toLowerCase().contains(s.toLowerCase())
                       //     || modelOrders.getpDescr().toLowerCase().contains(s.toLowerCase().toLowerCase()
                    ) {
                        ordersList.add(modelOrders);

                    }
                    //adapter
                    adapterOrders = new AdapterOrders(options);
                    //set adapter to recyclerview
                    ordersRecyclerView.setAdapter(adapterOrders);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }



}