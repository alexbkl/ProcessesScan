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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdminOrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private AdapterOrders adapterOrders;
    private List<ModelOrders> ordersList;


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
                    ordersList.add(modelOrders);
                    adapterOrders = new AdapterOrders(getActivity(), ordersList);
                    adapterOrders.setHasStableIds(true);
                    ordersRecyclerView.setAdapter(adapterOrders);

                    if (ds.hasChild("process")) {
                        if (ds.child("process").hasChild("Corte")) {
                            Object ended = ds.child("process").child("Corte").child("ended").getValue();
                            if (ended != null) {
                                System.out.println("ended:" + ended);



                            } else {
                                System.out.println("ended: a un altre dia o sense acabar");
                            }


                            //  hashMap.put("corteDateTv", Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString());
                            System.out.println("started:" + Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()));
                   //         corteDateTv = Objects.requireNonNull(ds.child("process").child("Corte").child("started").getValue()).toString();

                        }


                    }





                }
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
                } else {
                    loadOrders();
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
                ordersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelOrders modelOrders = ds.getValue(ModelOrders.class);
                    assert modelOrders != null;
                    long adminStartedLong = modelOrders.getAdminStarted();
                    long adminEndedLong = modelOrders.getAdminEnded();
                    long corteStartedLong = modelOrders.getCorteStarted();
                    long corteEndedLong = modelOrders.getCorteEnded();
                    long canteadoStartedLong = modelOrders.getCanteadoStarted();
                    long canteadoEndedLong = modelOrders.getCanteadoEnded();
                    long mecanizadoStartedLong = modelOrders.getMecanizadoStarted();
                    long mecanizadoEndedLong = modelOrders.getMecanizadoEnded();
                    long lacaStartedLong = modelOrders.getLacaStarted();
                    long lacaEndedLong = modelOrders.getLacaEnded();
                    long montajeStartedLong = modelOrders.getMontajeStarted();
                    long montajeEndedLong = modelOrders.getMontajeEnded();
                    long embalajeStartedLong = modelOrders.getEmbalajeStarted();
                    long embalajeEndedLong = modelOrders.getEmbalajeEnded();
                    long transporteStartedLong = modelOrders.getTransporteStarted();
                    long transporteEndedLong = modelOrders.getTransporteEnded();
                    long cajonesStartedLong = modelOrders.getCajonesStarted();
                    long cajonesEndedLong = modelOrders.getCajonesEnded();
                    long espejosStartedLong = modelOrders.getEspejosStarted();
                    long espejosEndedLong = modelOrders.getEspejosEnded();
                    long uneroStartedLong = modelOrders.getUneroStarted();
                    long uneroEndedLong = modelOrders.getUneroEnded();

                    Date adminStartedDate = new Date(adminStartedLong);
                    Date adminEndedDate = new Date(adminEndedLong);
                    Date corteStartedDate = new Date(corteStartedLong);
                    Date corteEndedDate = new Date(corteEndedLong);
                    Date canteadoStartedDate = new Date(canteadoStartedLong);
                    Date canteadoEndedDate = new Date(canteadoEndedLong);
                    Date mecanizadoStartedDate = new Date(mecanizadoStartedLong);
                    Date mecanizadoEndedDate = new Date(mecanizadoEndedLong);
                    Date lacaStartedDate = new Date(lacaStartedLong);
                    Date lacaEndedDate = new Date(lacaEndedLong);
                    Date montajeStartedDate = new Date(montajeStartedLong);
                    Date montajeEndedDate = new Date(montajeEndedLong);
                    Date embalajeStartedDate = new Date(embalajeStartedLong);
                    Date embalajeEndedDate = new Date(embalajeEndedLong);
                    Date transporteStartedDate = new Date(transporteStartedLong);
                    Date transporteEndedDate = new Date(transporteEndedLong);
                    Date cajonesStartedDate = new Date(cajonesStartedLong);
                    Date cajonesEndedDate = new Date(cajonesEndedLong);
                    Date espejosStartedDate = new Date(espejosStartedLong);
                    Date espejosEndedDate = new Date(espejosEndedLong);
                    Date uneroStartedDate = new Date(uneroStartedLong);
                    Date uneroEndedDate = new Date(uneroEndedLong);

                    DateFormat df = new SimpleDateFormat("dd/MM/yy-HH:mm");

                    String adminStarted = df.format(adminStartedDate);
                    String adminEnded = df.format(adminEndedDate);
                    String corteStarted = df.format(corteStartedDate);
                    String corteEnded = df.format(corteEndedDate);
                    String canteadoStarted = df.format(canteadoStartedDate);
                    String canteadoEnded = df.format(canteadoEndedDate);
                    String mecanizadoStarted = df.format(mecanizadoStartedDate);
                    String mecanizadoEnded = df.format(mecanizadoEndedDate);
                    String lacaStarted = df.format(lacaStartedDate);
                    String lacaEnded = df.format(lacaEndedDate);
                    String montajeStarted = df.format(montajeStartedDate);
                    String montajeEnded = df.format(montajeEndedDate);
                    String embalajeStarted = df.format(embalajeStartedDate);
                    String embalajeEnded = df.format(embalajeEndedDate);
                    String transporteStarted = df.format(transporteStartedDate);
                    String transporteEnded = df.format(transporteEndedDate);
                    String cajonesStarted = df.format(cajonesStartedDate);
                    String cajonesEnded = df.format(cajonesEndedDate);
                    String espejosStarted = df.format(espejosStartedDate);
                    String espejosEnded = df.format(espejosEndedDate);
                    String uneroStarted = df.format(uneroStartedDate);
                    String uneroEnded = df.format(uneroEndedDate);



                List<String> list = new ArrayList<>();
                List<String> repeating = new ArrayList<>();
                List<String> nonRepeating = new ArrayList<>();
                list.add(modelOrders.getCode());
                list.add(modelOrders.getAdminUser());
                list.add(modelOrders.getCorteUser());
                list.add(modelOrders.getCanteadoUser());
                list.add(modelOrders.getMecanizadoUser());
                list.add(modelOrders.getLacaUser());
                list.add(modelOrders.getMontajeUser());
                list.add(modelOrders.getEmbalajeUser());
                list.add(modelOrders.getTransporteUser());
                list.add(modelOrders.getCajonesUser());
                list.add(modelOrders.getEspejosUser());
                list.add(modelOrders.getUneroUser());
                list.add(adminStarted);
                list.add(adminEnded);
                list.add(corteStarted);
                list.add(corteEnded);
                list.add(canteadoStarted);
                list.add(canteadoEnded);
                list.add(mecanizadoStarted);
                list.add(mecanizadoEnded);
                list.add(lacaStarted);
                list.add(lacaEnded);
                list.add(montajeStarted);
                list.add(montajeEnded);
                list.add(embalajeStarted);
                list.add(embalajeEnded);
                list.add(transporteStarted);
                list.add(transporteEnded);
                list.add(cajonesStarted);
                list.add(cajonesEnded);
                list.add(espejosStarted);
                list.add(espejosEnded);
                list.add(uneroStarted);
                list.add(uneroEnded);

                for (String i: list) {
                    if (!repeating.contains(i)) {
                        if (nonRepeating.contains(i)) {
                            nonRepeating.remove(i);
                            repeating.add(i);
                        } else {
                            nonRepeating.add(i);
                        }
                    }
                }

                    for (String value : nonRepeating) {
                        if (value != null) {
                            if (value.toLowerCase().contains(s.toLowerCase())) {
                                ordersList.add(modelOrders);
                            }
                        }
                    }

                    //adapter
                    adapterOrders = new AdapterOrders(getActivity(), ordersList);
                    //set adapter to recyclerview
                    ordersRecyclerView.setAdapter(adapterOrders);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



}