package com.lalbhaibrokers.lalbhaibrokerspvtltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DailyBill extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_bill);
    }

    public void downloadBillMethod(View view) {

        Toast.makeText(getApplicationContext(), "Downloading bill...", Toast.LENGTH_SHORT).show();

    }

////    when profile button is pressed
//    public void userProfileMethod(View view) {
//        Intent profileIntent = new Intent(getApplicationContext(), ProfileAndSettings.class);
//        startActivity(profileIntent);
//        finish();
//    }
//
////    when back button is pressed
//    public void DashboardMethod(View view) {
//        Intent dashboardIntent = new Intent(getApplicationContext(), UserDashboard.class);
//        finish();
//        startActivity(dashboardIntent);
//    }
}