package com.example.myapplication3;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {
    private static int SPlASH_SCREEN=5000;



    Animation topAnim,bottomAnim;
    ImageView image;
    // TextView logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main3);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image=findViewById(R.id.imageView);
        // logo=findViewById(R.id.textView);

        image.setAnimation(topAnim);
        // logo.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity3.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }},SPlASH_SCREEN);
    }


}