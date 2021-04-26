package com.example.myapplication3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class DashboardDefault extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FeaturedAdapter.OnItemClicked {
    static final float END_SCALE = 0.7f;
    EditText editText;
    Button btnSearch;
    TextView welmsg;
    RecyclerView featuredRecycler,defaultRecycler;
    ImageView calbtn,paybtn,notesbtn;
    LinearLayout l1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon,add,todo;
    String username,email,fullname,password,profession,phone;
    private DatabaseReference reference;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private FirebaseUser mUse;
    ArrayList<FeaturedHelperClass> featuredLocations1 = new ArrayList<>();
    //ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();
    int i=1;
    String mTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_default);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        username=intent.getStringExtra("username");
        password=intent.getStringExtra("password");
        fullname=intent.getStringExtra("fullname");
        profession=intent.getStringExtra("profession");
        phone=intent.getStringExtra("phone");
        add=findViewById(R.id.addCategory);
        todo=findViewById(R.id.todoBtn);
        mAuth = FirebaseAuth.getInstance();

        mUse =mAuth.getCurrentUser();
        mUse =mAuth.getCurrentUser();
        String onlineuserId= Objects.requireNonNull(mUse).getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(username).child("MyCat");



        l1=findViewById(R.id.setbg);
        welmsg=findViewById(R.id.welcome);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menubtn);

        naviagtionDrawer();


        if(profession.equals("Home Makers")) {
            l1.setBackgroundResource(R.drawable.homemaker2);
        }
        else if(profession.equals("Job Seekers")) {
            l1.setBackgroundResource(R.drawable.jobseekerstwo);
        }
        else if(profession.equals("Working Professionals")) {
            l1.setBackgroundResource(R.drawable.bbg4);

        }
        else {
            // col changee
            l1.setBackgroundResource(R.drawable.blackk);
        }


        defaultRecycler=findViewById(R.id.defaultCategories);
        featuredRecycler=findViewById(R.id.featured);
        defaultRecycler(profession);
        welmsg.setText("Welcome "+fullname);
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(DashboardDefault.this, LinearLayoutManager.HORIZONTAL, false));


        editText = findViewById(R.id.editText);
        btnSearch = findViewById(R.id.btnSearch);

        notesbtn = findViewById(R.id.notesbtn);

        paybtn=findViewById(R.id.Paybtn);

        calbtn=findViewById(R.id.mybtn);

        paybtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

               Intent intent=new Intent(DashboardDefault.this,MainActivity.class);
                startActivity(intent);

            }});

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 2);
        c.set(Calendar.MINUTE,58);
        c.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentp = new Intent(this, AlertReceiver.class);
        String mTask ="Reminder";
        String mDescription="Update your inventory Stock";
        intentp.putExtra("username", username);
        intentp.putExtra("title", mTask);
        intentp.putExtra("content", mDescription);
        intentp.putExtra("email",email);
        intentp.putExtra("fullname", fullname);
        intentp.putExtra("profession", profession);
        intentp.putExtra("phone", phone);
        intentp.putExtra("email", email);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentp, 0);
            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DATE, 1);
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),
                1000 * 60 * 1, pendingIntent);



        calbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent=new Intent(DashboardDefault.this,Calender.class);
                startActivity(intent);

            }});
        todo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent=new Intent(DashboardDefault.this,ToDoList.class);

                intent.putExtra("username", username);
                intent.putExtra("category", "To Do List");
                startActivity(intent);

            }});


        notesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),notes.class);
                intent.putExtra("username", username);
                intent.putExtra("category", "notes");
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addCategory();

            }
        });




        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                String term = editText.getText().toString();
                intent.putExtra(SearchManager.QUERY, term);
                startActivity(intent);
            }
        });


    }

    private void addCategory() {

        AlertDialog.Builder my =new AlertDialog.Builder(this);
        LayoutInflater inflater =LayoutInflater.from(this);
        View myview;
        myview = inflater.inflate(R.layout.catadd, null);
        my.setView(myview);
        AlertDialog dialog =my.create();
        dialog.setCancelable(false);
        final EditText task =myview.findViewById(R.id.task);
        Button save =myview.findViewById(R.id.save);
        Button cancel =myview.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setCancelable(true);
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTask =task.getText().toString().trim();

                String id =reference.push().getKey();


                if(TextUtils.isEmpty(mTask)){
                    task.setError("Category name REQUIRED");
                    return;
                }
                else{
                    Model model = new Model(mTask,id);
                    loader =new ProgressDialog(DashboardDefault.this);
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DashboardDefault.this, "Task has been inserted succesfully", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                            else
                            {
                                String error =task.getException().toString();
                                Toast.makeText(DashboardDefault.this, "Failed"+error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });

                }
                dialog.dismiss();

            }
        });
        dialog.show();
        //FeaturedAdapter adapter = new FeaturedAdapter(featuredLocations);
        //featuredRecycler.setAdapter(adapter);
        //adapter.setOnClick(DashboardDefault.this);

        // public void onItemClick(String mTask) {
        // Intent intent=new Intent(getApplicationContext(),CardClick.class);
        //intent.putExtra("username", username);
        //intent.putExtra("category", mTask);
        //startActivity(intent);
        // }

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model> options =new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference, Model.class)
                .build();

        FirebaseRecyclerAdapter<Model, MyViewHolder1> adapter = new FirebaseRecyclerAdapter<Model, DashboardDefault.MyViewHolder1>(options) {
            @NonNull
            @Override
            public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                view =LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
                return new DashboardDefault.MyViewHolder1(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DashboardDefault.MyViewHolder1 holder, int position, @NonNull Model model) {

                holder.setNAme(model.getTask());
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),CardClick1.class);
                        intent.putExtra("username", username);
                        intent.putExtra("id",model.getId());
                        intent.putExtra("category",model.getTask());
                        startActivity(intent);
                    }
                });

            }


        } ;
        featuredRecycler.setAdapter(adapter);
        adapter.startListening();
    }
    public static class MyViewHolder1 extends RecyclerView.ViewHolder{
        View mview;
        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            mview =itemView;

        }

        public void setNAme(String name){
            TextView taskTextVIew =mview.findViewById(R.id.featured_title);
            taskTextVIew.setText(name);

        }}

    private void naviagtionDrawer() {
        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }


    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                l1.setScaleX(offsetScale);
                l1.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = l1.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                l1.setTranslationX(xTranslation);
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    private void defaultRecycler(String prof) {
        defaultRecycler.setHasFixedSize(true);
        defaultRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        featuredLocations1.add(new FeaturedHelperClass(R.drawable.groceries1, "Groceries"));
        featuredLocations1.add(new FeaturedHelperClass(R.drawable.cloth, "Clothings"));
        featuredLocations1.add(new FeaturedHelperClass(R.drawable.durba, "Grooming"));

        featuredLocations1.add(new FeaturedHelperClass(R.drawable.bills1, "Bills"));

        featuredLocations1.add(new FeaturedHelperClass(R.drawable.travel, "Travel"));
        featuredLocations1.add(new FeaturedHelperClass(R.drawable.medicines1, "Medicines"));

        if(prof.equals("Home Makers")) {
            featuredLocations1.add(new FeaturedHelperClass(R.drawable.homeapp, "Home Appliances"));
            featuredLocations1.add(new FeaturedHelperClass(R.drawable.hyg, "Hygiene"));

        }
        else if(prof.equals("Job Seekers")) {
            featuredLocations1.add(new FeaturedHelperClass(R.drawable.resume, "Resume"));

            featuredLocations1.add(new FeaturedHelperClass(R.drawable.books, "Books"));


        }
        else if(prof.equals("Working Professionals")) {


            featuredLocations1.add(new FeaturedHelperClass(R.drawable.books, "Books"));

        }
        else {
            featuredLocations1.add(new FeaturedHelperClass(R.drawable.homeapp, "Home Appliances"));

            featuredLocations1.add(new FeaturedHelperClass(R.drawable.books, "Books"));



        }




        FeaturedAdapter adapter1 = new FeaturedAdapter(featuredLocations1);
        defaultRecycler.setAdapter(adapter1);
        adapter1.setOnClick(DashboardDefault.this);

    }

    @Override
    public void onItemClick(int position) {

        Intent intent=new Intent(getApplicationContext(),CardClick.class);
        intent.putExtra("username", username);

        intent.putExtra("password", password);
        intent.putExtra("fullname", fullname);
        intent.putExtra("profession", profession);
        intent.putExtra("phone", phone);
        intent.putExtra("email", email);
        intent.putExtra("category", featuredLocations1.get(position).getTitle());
        startActivity(intent);





    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_logout:

                Intent intent = new Intent(DashboardDefault.this, LoginActivity.class);
                startActivity(intent);
                return true;

            case  R.id.nav_profile:
                Intent intent1 = new Intent(DashboardDefault.this, profile.class);
                intent1.putExtra("username", username);
                intent1.putExtra("password", password);
                intent1.putExtra("fullname", fullname);
                intent1.putExtra("profession", profession);
                intent1.putExtra("phone", phone);
                intent1.putExtra("email", email);
                startActivity(intent1);
                return true;

            case R.id.nav_priv:
                Intent intent12 = new Intent(DashboardDefault.this, Privacy.class);
                intent12.putExtra("username", username);
                intent12.putExtra("password", password);
                intent12.putExtra("fullname", fullname);
                intent12.putExtra("profession", profession);
                intent12.putExtra("phone", phone);
                intent12.putExtra("email", email);
                startActivity(intent12);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


}