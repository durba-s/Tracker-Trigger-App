package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
public class Dashboard extends AppCompatActivity {
    List<MyList>myLists;
    RecyclerView rv;
    MyAdapter adapter;
    Button calbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        rv=(RecyclerView)findViewById(R.id.rec);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this,2));
        myLists=new ArrayList<>();

        getdata();
    }

    private void getdata() {
        myLists.add(new MyList(R.drawable.homeapp));
        myLists.add(new MyList(R.drawable.groceries));
        myLists.add(new MyList(R.drawable.work));
        myLists.add(new MyList(R.drawable.medicines));
        myLists.add(new MyList(R.drawable.bills));
        myLists.add(new MyList(R.drawable.notes));
        calbtn=findViewById(R.id.myBtn);




        adapter=new MyAdapter(myLists,this);
        rv.setAdapter(adapter);

       calbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent=new Intent(Dashboard.this,Calender.class);
                startActivity(intent);

            }});
    }
}