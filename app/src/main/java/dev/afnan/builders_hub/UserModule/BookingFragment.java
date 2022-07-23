package dev.afnan.builders_hub.UserModule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dev.afnan.builders_hub.R;
import dev.afnan.builders_hub.Models.Order;
import dev.afnan.builders_hub.ViewHolder.recyclerAdapter;


public class BookingFragment extends Fragment {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference order;
    recyclerAdapter myAdapter;
    ArrayList<Order> orderList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        database = FirebaseDatabase.getInstance();
        order = database.getReference("Orders");

        recyclerView = view.findViewById(R.id.BookingsRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        orderList = new ArrayList<>();
        myAdapter = new recyclerAdapter(getActivity(), orderList);
        recyclerView.setAdapter(myAdapter);


        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading...");
        dialog.show();

        String userID = FirebaseAuth.getInstance().getUid();

        if (userID != null) {

            order.child(userID).child("orderRequests").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Order myOrder = dataSnapshot.getValue(Order.class);
                        orderList.add(myOrder);
                    }

                    sortOrders();
                    myAdapter.notifyDataSetChanged();
                    Log.d("orderData", "data received successfully");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("orderData", "data failed");
                }
            });
        }
        else {
            Log.d("userId", "user ID is null");
        }

        Handler handler = new Handler();
        handler.postDelayed(dialog::dismiss, 300);

        return view;
    }

    private void sortOrders() {
        Collections.sort(orderList, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getOrderId().compareToIgnoreCase(o2.getOrderId());
            }
        });

        Collections.reverse(orderList);
    }


}