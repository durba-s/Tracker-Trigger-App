package com.example.myapplication3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OTP_verify extends AppCompatActivity {
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String email,fullname,profession,phone;


    private EditText code1,code2,code3,code4,code5,code6;
    private String verificationId;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verify);

        TextView textMobile = findViewById(R.id.textmobile);
        textMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")));
        code1 = findViewById(R.id.inputcode);
        code2 = findViewById(R.id.inputcode2);
        code3 = findViewById(R.id.inputcode3);
        code4 = findViewById(R.id.inputcode4);
        code5 = findViewById(R.id.inputcode5);
        code6 = findViewById(R.id.inputcode6);
        setupOTPinputs();

        final ProgressBar progressBar = findViewById(R.id.progress);
        final Button buttonverify = findViewById(R.id.buttonverify);
        verificationId = getIntent().getStringExtra("verificationId");
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        username=intent.getStringExtra("username");
        String password=intent.getStringExtra("password");
        fullname=intent.getStringExtra("fullname");
        profession=intent.getStringExtra("profession");
        phone=intent.getStringExtra("mobile");
        String message=intent.getStringExtra("message");


        buttonverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                if (code1.getText().toString().trim().isEmpty() || code2.getText().toString().trim().isEmpty() || code3.getText().toString().trim().isEmpty() || code4.getText().toString().trim().isEmpty() ||
                        code5.getText().toString().trim().isEmpty() || code6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OTP_verify.this, "please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() +
                        code5.getText().toString() + code6.getText().toString();
                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonverify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,
                            code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonverify.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        if(message.equals("whatdo?"))
                                        {
                                            updateOldUserData();
                                        }
                                        else {
                                            UserHelperClass helperClass = new UserHelperClass(fullname, username, password, email, profession, phone);
                                            reference.child(username).setValue(helperClass);
                                            Intent intent3 = new Intent(OTP_verify.this, DashboardDefault.class);
                                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent3.putExtra("username", username);
                                            intent3.putExtra("password", password);
                                            intent3.putExtra("fullname", fullname);
                                            intent3.putExtra("profession", profession);
                                            intent3.putExtra("phone", phone);
                                            intent3.putExtra("email", email);
                                            startActivity(intent3);
                                        }


                                    } else {
                                        Toast.makeText(OTP_verify.this, "the verification code entered is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        findViewById(R.id.textresend_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        OTP_verify.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(OTP_verify.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newverificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newverificationId;
                                Toast.makeText(OTP_verify.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            }
                        }

                );

            }
        });
    }

    private void updateOldUserData() {
        Intent intent =new Intent(getApplicationContext(),SetnewPassword.class);
        intent.putExtra("username",username);

        intent.putExtra("fullname", fullname);
        intent.putExtra("profession", profession);
        intent.putExtra("phone", phone);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }


    private void setupOTPinputs()
    {
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    code6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}