package dev.afnan.builders_hub.Common;

import android.net.Uri;
import android.util.Log;

import dev.afnan.builders_hub.Models.User;


public class Common {

    public static User CurrentUser;

    public static Uri userProfileImage;

    public static String CURRENT_ORDER_STATUS;


    public static String checkStatus(String status_code) {

        String st = "N/A";

        if (status_code.equals("0")) {
            st = "Pending";
        } else if (status_code.equals("1")) {
            st = "Accepted";
        } else if (status_code.equals("2")) {
            st = "Cancelled";
        } else if (status_code.equals("3")) {
            st = "Order placed";
        }

        Log.d("statusCheck", "conditions not met , status is: " + st);
        CURRENT_ORDER_STATUS = st;

        return CURRENT_ORDER_STATUS;
    }


}
