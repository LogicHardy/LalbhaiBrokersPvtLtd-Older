package com.lalbhaibrokers.lalbhaibrokerspvtltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class QuarterlyInvoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarterly_invoice);
    }

    public void downloadInvoiceMethod(View view) {

        Toast.makeText(getApplicationContext(), "Downloading invoice...", Toast.LENGTH_SHORT).show();

    }

//    //    when profile button is pressed
//    public void userProfileMethod(View view) {
//        Intent profileIntent = new Intent(getApplicationContext(), ProfileAndSettings.class);
//        startActivity(profileIntent);
//        finish();
//    }
//
//    //    when back button is pressed
//    public void DashboardMethod(View view) {
//        Intent dashboardIntent = new Intent(getApplicationContext(), UserDashboard.class);
//        startActivity(dashboardIntent);
//        finish();
//    }
}