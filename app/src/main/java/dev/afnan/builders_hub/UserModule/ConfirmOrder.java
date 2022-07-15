package dev.afnan.builders_hub.UserModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.afnan.builders_hub.Common.Common;
import dev.afnan.builders_hub.Models.Order;
import dev.afnan.builders_hub.Models.User;
import dev.afnan.builders_hub.R;

public class ConfirmOrder extends AppCompatActivity {
    EditText editTotalWorkers;
    EditText editNoDays;
    EditText editLocation;
    Button placeOrderButton;
    Spinner spinner;
    FirebaseDatabase database;
    DatabaseReference orderReference;
    DatabaseReference requestReference;
    public int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        editTotalWorkers = findViewById(R.id.edtnWorkers);
        editNoDays = findViewById(R.id.edtNDays);
        editLocation = findViewById(R.id.edtLocation);
        placeOrderButton = findViewById(R.id.BtnSave);
        spinner = findViewById(R.id.wtype);


        database = FirebaseDatabase.getInstance();
        requestReference = database.getReference().child("Orders");
        orderReference = requestReference.child(FirebaseAuth.getInstance().getUid());

        String[] workerType = getResources().getStringArray(R.array.worker_type);

        Intent mIntent = getIntent();
        int selectedItem = mIntent.getIntExtra("itemSelected", 0);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(this, R.layout.worker_type_spinner_layout, workerType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(selectedItem);


        orderReference.child("orderRequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("ColumnExist", "column found");
                    int count = (int) snapshot.getChildrenCount();
                    if (count == 0) {
                        counter = 1;
                    } else {
                        counter = count + 1;
                    }
                    Log.d("ColumnExist", "count : " + count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean res = checkoutData();
                if (res) {
                    editTotalWorkers.getText().clear();
                    editNoDays.getText().clear();
                    editLocation.getText().clear();
                }

            }
        });

    }


    private Boolean checkoutData() {

        String worker_type = spinner.getSelectedItem().toString();
        String totalWorkers = editTotalWorkers.getText().toString();
        String totalDays = editNoDays.getText().toString();
        String address = editLocation.getText().toString();
        String orderId = String.valueOf(System.currentTimeMillis());
        String date = getDate();


        if (totalWorkers.isEmpty()) {
            editTotalWorkers.setError("Required!");
            editTotalWorkers.requestFocus();
            return false;
        }

        if (Integer.parseInt(totalWorkers) > 20) {
            editTotalWorkers.setError("Not more than 20 workers!");
            editTotalWorkers.requestFocus();
            return false;
        }

        if (totalDays.isEmpty()) {
            editNoDays.setError("Required!");
            editNoDays.requestFocus();
            return false;
        }

        if (Integer.parseInt(totalDays) > 30) {
            editNoDays.setError("Maximum limit is 30 days!");
            editNoDays.requestFocus();
            return false;
        }

        if (address.isEmpty()) {
            editLocation.setError("Required!");
            editLocation.requestFocus();
            return false;
        }


        User request = new User(
                Common.CurrentUser.getName(),
                Common.CurrentUser.getEmail(),
                Common.CurrentUser.getPhone()
        );


        Order workInfo = new Order(
                orderId,
                worker_type,
                totalWorkers,
                totalDays,
                address,
                date
        );

        orderReference.child("userInfo").setValue(request);

        DatabaseReference currentOrder = orderReference.child("orderRequests").child(String.valueOf(counter));
        currentOrder.setValue(workInfo);


        Toast.makeText(this, "Thank you order placed.", Toast.LENGTH_SHORT).show();
        finish();


        return true;
    }

    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        Log.d("currentTime", "Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;
    }

}