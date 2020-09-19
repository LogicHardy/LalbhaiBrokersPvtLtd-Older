package com.lalbhaibrokers.lalbhaibrokerspvtltd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

//    Variables
    Context context=this;
    TextInputEditText mobileNumber, otp;
    TextView forgotPassword, errorMessage, login, sendOtp, verifyOtp;
    String verificationCode;
    boolean isVerified;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Hooks
        mobileNumber = (TextInputEditText) findViewById(R.id.mobile_no_editText);
        otp = (TextInputEditText) findViewById(R.id.otp_editText);
        forgotPassword = (TextView) findViewById(R.id.forgot_password_textView);
        errorMessage = (TextView) findViewById(R.id.error_message_textView);
        login = (TextView) findViewById(R.id.login_button);
        sendOtp = (TextView) findViewById(R.id.send_otp_btn);
        verifyOtp = (TextView) findViewById(R.id.verify_otp);

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCode = otp.getText().toString();
                if (!userCode.isEmpty()) {
                    verifyCode(userCode); //verifying the code Entered by user
                }
            }
        });
    }

    //method for login button
    public void loginMethod(View view) {
        if(mobileNumber.getText().toString().equals("") || otp.getText().toString().equals("")) {
            errorMessage.setText(R.string.no_info_provided);
            errorMessage.setVisibility(View.VISIBLE);
        }
        else {
            errorMessage.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, mobileNumber.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
            startActivity(intent);
            finish();
        }
    }

    //method for forget password
    public void forgotPasswordMethod(View view) {
        Toast.makeText(LoginActivity.this, "Forgot password", Toast.LENGTH_SHORT).show();
    }

    //method for start the OTP process
    public void sendOtp(View view){

        database = FirebaseDatabase.getInstance();//connecting with realtime database
        Log.d("myTag", "database "+database.toString());
        reference = database.getReference("Users");//getting reference of User child
        Log.d("myTag", "reference "+reference.toString());

//        making queries
        Query checkPhoneNo= reference.orderByChild("phoneNum").equalTo(mobileNumber.getText().toString());
        Log.d("myTag", "checking for phone number "+mobileNumber.getText().toString());

//        check if phone number exists
        checkPhoneNo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").exists()){ // checking if data exists
                    Log.d("myTag", "phoneNum"+snapshot.getChildrenCount());
                    if(snapshot.getChildrenCount()==1){ //checking if same number is appearing twice in database
                        for (DataSnapshot userId : snapshot.getChildren()) {
                            if(userId.child("phoneNum").getValue(String.class).equals(mobileNumber.getText().toString())){
                                String phoneNumCountryCode = "+91"+mobileNumber.getText().toString(); //we have to add country code in order to receive OTP

                                //method that will send the OTP to given number
                                PhoneAuthProvider.getInstance().verifyPhoneNumber( //sending message
                                        phoneNumCountryCode,        // Phone number to verify
                                        60,                 // Timeout duration
                                        TimeUnit.SECONDS,   // Unit of timeout
                                        TaskExecutors.MAIN_THREAD,    // Activity (for callback binding)
                                        mCallbacks);        // OnVerificationStateChangedCallbacks
                                Toast.makeText(context, "OTP send to "+phoneNumCountryCode, Toast.LENGTH_SHORT).show();
                                sendOtp.setVisibility(View.GONE);
                                verifyOtp.setVisibility(View.VISIBLE);
                            }   else{
                                errorMessage.setText("User not found 1!");
                                errorMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        Log.d("myTag", "more then 1 user with same number, please check");
                    }
                }else{
                    errorMessage.setText("User not found 2!");
                    errorMessage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorMessage.setText("No stable connection! Please try again");
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    //method that verify the OTP received or not
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;   //verification code that should be received by phoneNumber
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode(); //verification code that actually received by phoneNumber
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(context, "Verification Faild: OTP not recived", Toast.LENGTH_SHORT).show();
        }
    };

    //verifying the OTP
    public void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code); //comparing both verification code
        signin(credential);
    }

    //signing in the User to update in database
    private void signin(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Verification Complete", Toast.LENGTH_SHORT).show();
                    isVerified=true;
                    Intent intent = new Intent(context, UserDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    errorMessage.setText("Verification Failed: OTP wrong");
                    errorMessage.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}