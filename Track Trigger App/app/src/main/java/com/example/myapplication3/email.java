package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class email extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String username=intent.getStringExtra("username");
        String password=intent.getStringExtra("password");
        String fullname=intent.getStringExtra("fullname");
        String profession=intent.getStringExtra("profession");
        String codeSend=intent.getStringExtra("codeSend");

        EditText otp;
        Button Nextbtn;

        otp=findViewById(R.id.otppas);
        Nextbtn=findViewById(R.id.nextbtn);


        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpcode=otp.getText().toString().trim();
                if (TextUtils.isEmpty(otpcode)) {
                    otp.setError("This is a required field");
                    return;
                }
                else if(!otpcode.equals(codeSend)){
                    otp.setError("Incorrect otp entered");
                }
                else{
                    Intent intent =new Intent(getApplicationContext(),verification.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("fullname", fullname);
                    intent.putExtra("profession", profession);
                    intent.putExtra("email", email);

                    startActivity(intent);
                }

            }});

    }
}