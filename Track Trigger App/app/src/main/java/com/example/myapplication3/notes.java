package com.example.myapplication3;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class notes extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FirebaseRecyclerAdapter<Model,MyViewHolder> adapter;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton, floatingActionButton2,sharebtn;
    private TextView mTextViewShowUploads;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUse;
    String onlineuserId;

    private ProgressDialog loader;
    private String key="",task,description;
    HashMap<String, String> tasks = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recyclerView=findViewById(R.id.recycleview);
        Intent intent = getIntent();
        String username=intent.getStringExtra("username");
        String category=intent.getStringExtra("category");

        toolbar=findViewById(R.id.hometoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(category);
        mAuth =FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader =new ProgressDialog(this);

        mUse =mAuth.getCurrentUser();
        onlineuserId= Objects.requireNonNull(mUse).getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(username).child(category).child("tasks");
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity(username,category);
            }
        });
        floatingActionButton =findViewById(R.id.fab);
        floatingActionButton2 =findViewById(R.id.fab2);
        sharebtn =findViewById(R.id.share);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String s="Here is list of things I have saved on track trigger "+"\n";
                for (String i : tasks.keySet()) {
                    s=s+"\n"+tasks.get(i);
                }
                String shareBody =s;
                String shareSub = "Here is list of things I have saved";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageUpload.class);
                intent.putExtra("username",username);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(category);
            }
        });


    }

    private void addTask(String Category) {
        AlertDialog.Builder my =new AlertDialog.Builder(this);
        LayoutInflater inflater =LayoutInflater.from(this);
        View myview;


            myview = inflater.inflate(R.layout.input, null);






        my.setView(myview);
        AlertDialog dialog =my.create();
        dialog.setCancelable(false);
        final EditText task =myview.findViewById(R.id.task);
        EditText description =myview.findViewById(R.id.description);


        Button save =myview.findViewById(R.id.save);
        Button cancel =myview.findViewById(R.id.cancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setCancelable(true);
                dialog.dismiss();
            }
        });
        //
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTask =task.getText().toString().trim();
                String mDescription =description.getText().toString().trim();
                String id =reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());


                if(TextUtils.isEmpty(mTask)){
                    task.setError("Task REQUIRED");
                    return;
                }
                if(TextUtils.isEmpty(mDescription)){
                    description.setError("Task REQUIRED");
                    return;
                }

                else{
                    loader.setMessage("ADDing your data");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    Model model;

                         model = new Model(mTask, mDescription, id, date);


                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(notes.this, "Task has been inserted succesfully", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                            else
                            {
                                String error =task.getException().toString();
                                Toast.makeText(notes.this, "Failed"+error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });

                }
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model> options =new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference, Model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Model model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDEsc(model.getDescription());

                tasks.put(model.getId(),model.getTask());





                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key=getRef(position).getKey();


                        task=model.getTask();
                        description=model.getDescription();

                        updateTask();

                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                 view =LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved,parent,false);
                return new MyViewHolder(view);
            }
        }  ;
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        // Assumes current activity is the searchable activity
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(this);
        return true;



       /* getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent searchIntent = new Intent(notes.this, searchActivity.class);
                searchIntent.putExtra("search", query);
                startActivity(searchIntent);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //??????
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);  */



       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

             //   Log.e("Main"," data search"+newText);

             //   adapter.getFilter().filter(newText);



                return true;
            }
        });  */


       // return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if(id == R.id.searchView){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mview =itemView;

        }

        public void setTask(String task){
            TextView taskTextVIew =mview.findViewById(R.id.tasktv);
            taskTextVIew.setText(task);

        }

        public void setDEsc(String desc )
        {
            TextView descTextVIew =mview.findViewById(R.id.desctv);
            descTextVIew.setText(desc);
        }
        public void setDate(String date)
        {
            TextView dateTextVIew =mview.findViewById(R.id.dateTv);
            dateTextVIew.setText(date);

        }

        public void setQuantity(String quantity) {
            TextView quantityTextView =mview.findViewById(R.id.quantitytv);
            quantityTextView.setText(quantity);

        }
    }

    private void updateTask()
    {
        AlertDialog.Builder myDialouge =new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view =inflater.inflate(R.layout.update_data,null);
        myDialouge.setView(view);


        AlertDialog dialog =myDialouge.create();
        EditText mtask =view.findViewById(R.id.medittask);
        EditText mdes =view.findViewById(R.id.meditdesc);


        mtask.setText(task);
        mtask.setSelection(task.length());


        mdes.setText(description);
        mdes.setSelection(description.length());

        Button delbutton =view.findViewById(R.id.btndelete);
        Button updbutton =view.findViewById(R.id.btnupdate);

        updbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task =mtask.getText().toString().trim();
                description=mdes.getText().toString().trim();
                String date =DateFormat.getDateInstance().format(new Date());

                Model model =new Model(task,description,key,date);

                tasks.put(model.getId(),model.getTask());

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(notes.this, "Data has been u[dated sucessfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String ERR = task.getException().toString();
                            Toast.makeText(notes.this, "update field "+ERR, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });
        delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(notes.this, "Task deleted succesfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String err =task.getException().toString();
                            Toast.makeText(notes.this, "Failed to delete task"+err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void openImagesActivity(String username,String category) {
        Intent intent = new Intent(this, ImagesActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("category",category);
        startActivity(intent);
    }

}