package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {
    TextView Userid,Name,Emailaddress,Job,Mobile,welmsg;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String username=intent.getStringExtra("username");
        String password=intent.getStringExtra("password");
        String fullname=intent.getStringExtra("fullname");
        String profession=intent.getStringExtra("profession");
        String phone=intent.getStringExtra("phone");
        back=findViewById(R.id.backbtn);
        Userid=findViewById(R.id.UserID);
        Name=findViewById(R.id.name);
        Emailaddress=findViewById(R.id.emailaddress);
        Job=findViewById(R.id.job);
        Mobile=findViewById(R.id.mobile);
        Userid.setText("USERNAME:  "+username);
        Name.setText("FULLNAME:   "+fullname);
        Job.setText("USER CATEGORY:   "+profession);
        Emailaddress.setText("EMAIL:  "+email);
        Mobile.setText("PHONE :    "+phone);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3;
                intent3 = new Intent(profile.this,DashboardDefault.class);
                intent3.putExtra("username", username);
                intent3.putExtra("password", password);
                intent3.putExtra("fullname", fullname);
                intent3.putExtra("profession", profession);
                intent3.putExtra("phone", phone);
                intent3.putExtra("email", email);
                startActivity(intent3);
            }

        });

    }
}