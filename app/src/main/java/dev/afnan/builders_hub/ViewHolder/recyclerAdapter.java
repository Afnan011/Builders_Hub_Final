package dev.afnan.builders_hub.ViewHolder;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import dev.afnan.builders_hub.Models.Order;
import dev.afnan.builders_hub.R;


public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.OrderViewHolder> {

    Context context;
    ArrayList<Order> orderList;

    public recyclerAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.my_bookings_list_item_layout, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Order order = orderList.get(position);
        holder.txtOderID.setText(order.getOrderId());
        holder.txtWorkerType.setText(order.getWorkerType());
        holder.txtDate.setText(order.getDate());
        holder.txtLocation.setText(order.getAddress());

        String status_code = order.getStatus();
        String status = checkStatus(status_code);
        holder.txtStatus.setText(status);

    }


    private String checkStatus(String status_code) {

        String st = "N/A";

        if (status_code.equals("0")) {
            st = "Order Placed";
        } else if (status_code.equals("1")) {
            st = "Pending";
        } else if (status_code.equals("2")) {
            st = "Accepted";
        } else if (status_code.equals("3")) {
            st = "cancelled";
        }

        Log.d("statusCheck", "conditions not met , status is: " + st);
        return st;

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView txtOderID, txtWorkerType, txtDate, txtStatus, txtLocation;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOderID = itemView.findViewById(R.id.order_id);
            txtWorkerType = itemView.findViewById(R.id.oder_type);
            txtDate = itemView.findViewById(R.id.order_date);
            txtStatus = itemView.findViewById(R.id.order_status);
            txtLocation = itemView.findViewById(R.id.order_place);

        }
    }

}
