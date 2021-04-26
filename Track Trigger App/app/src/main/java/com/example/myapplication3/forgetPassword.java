package com.example.myapplication3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class forgetPassword extends AppCompatActivity {
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        username =findViewById(R.id.phone);
        Button button =findViewById(R.id.forget_password_next_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user =username.getText().toString().trim();
                Query checkuser = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(user);
                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            username.setError(null);
                            String phoneFromDB=snapshot.child(user).child("phone").getValue(String.class);
                            String nameFromDB = snapshot.child(user).child("name").getValue(String.class);
                            String profFromDB= snapshot.child(user).child("profession").getValue(String.class);
                            String emailFromDB= snapshot.child(user).child("email").getValue(String.class);
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" +phoneFromDB,
                                    60,
                                    TimeUnit.SECONDS,
                                    forgetPassword.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential){


                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                            Toast.makeText(forgetPassword.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                            Intent intent =new Intent(getApplicationContext(),OTP_verify.class);
                                            intent.putExtra("mobile",phoneFromDB);
                                            intent.putExtra("verificationId",verificationId);
                                            intent.putExtra("username", user);
                                            intent.putExtra("fullname", nameFromDB);                                        ;

                                            intent.putExtra("profession", profFromDB);
                                            intent.putExtra("phone", phoneFromDB);
                                            intent.putExtra("email", emailFromDB);
                                            intent.putExtra("message", "whatdo?");
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                            );


                        }
                        else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void callBackScreenFromForgetPassword(View view) {
    }


}