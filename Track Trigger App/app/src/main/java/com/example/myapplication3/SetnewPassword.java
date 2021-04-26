package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetnewPassword extends AppCompatActivity {
    com.google.android.material.textfield.TextInputLayout newPassword;
    String email,fullname,profession,phone,newpassword,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setnew_password);
        newPassword =findViewById(R.id.new_password);
    }

    public void setNewPasswordBtn(View view) {


        newpassword = newPassword.getEditText().getText().toString().trim();
        username =getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");

        fullname=getIntent().getStringExtra("fullname");
        profession=getIntent().getStringExtra("profession");
        phone=getIntent().getStringExtra("mobile");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(username).child("password").setValue(newpassword);
        Toast.makeText(this, "Password has been updated", Toast.LENGTH_SHORT).show();
        Intent intent3 =new Intent(getApplicationContext(),DashboardDefault.class);
        intent3.putExtra("fullname", fullname);
        intent3.putExtra("username", username);
        intent3.putExtra("password", newpassword);
        intent3.putExtra("profession", profession);
        intent3.putExtra("phone", phone);
        intent3.putExtra("email", email);
        startActivity(intent3);
        finish();

    }

    public void goToHomeFromSetNewPassword(View view) {
    }
}