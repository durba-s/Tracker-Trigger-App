package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Privacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String username=intent.getStringExtra("username");
        String password=intent.getStringExtra("password");
        String fullname=intent.getStringExtra("fullname");
        String profession=intent.getStringExtra("profession");
        String phone=intent.getStringExtra("phone");
        ImageView back=findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3;
                intent3 = new Intent(Privacy.this,DashboardDefault.class);
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