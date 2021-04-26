package com.example.myapplication3;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener{
    private static final String TAG = "AddTask";

    Button dateButton, timeButton;
    TextView dateTextView, timeTextView;
    EditText mtask;
    EditText mdescription;
    Button savebtn;
    String username,email,password,fullname,profession,phone,category;
    ImageView backbtn;
    Spinner prio;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUse;
    String onlineuserId;
    String duedisp;
    private ProgressDialog loader;
    private String key="",task,description,mdateset,mtimeset;
    public static int i=1;
    String[] taskpriority = {"Low Priority", "High Priority" };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        email = intent.getStringExtra("email");

        password=intent.getStringExtra("password");
        fullname=intent.getStringExtra("fullname");
        profession=intent.getStringExtra("profession");
        phone=intent.getStringExtra("phone");
        category=intent.getStringExtra("category");

        setContentView(R.layout.activity_add_task);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        dateTextView = findViewById(R.id.date);
        timeTextView = findViewById(R.id.time);
        backbtn=findViewById(R.id.backbtn);
        prio=findViewById(R.id.mprior);
        prio.setOnItemSelectedListener(this);
        mtask =findViewById(R.id.task);

        mdescription =findViewById(R.id.description);
        savebtn=findViewById(R.id.save);
        loader =new ProgressDialog(this);

        ArrayAdapter ad = new ArrayAdapter(AddTask.this, android.R.layout.simple_spinner_item, taskpriority);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prio.setAdapter(ad);

        mAuth = FirebaseAuth.getInstance();

        mUse =mAuth.getCurrentUser();
        onlineuserId= Objects.requireNonNull(mUse).getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(username).child(category);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5=new Intent(AddTask.this,ToDoList.class);
                intent5.putExtra("username", username);
                intent5.putExtra("category", "To Do List");
                startActivity(intent5);
            }
        });

        //Calendar calendar = Calendar.getInstance();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();

            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String priority = prio.getSelectedItem().toString().trim();
                String mTask =mtask.getText().toString().trim();
                String mDescription =mdescription.getText().toString().trim();
                String mdate=dateTextView.getText().toString().trim();
                SimpleDateFormat myFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
                int daysBetween;

                Date dateBefore;
                try {
                    dateBefore = myFormat.parse(mdate);
                    Date dateAfter=java.util.Calendar.getInstance().getTime();

                    if (dateAfter.compareTo(dateBefore) < 0) {
                        long difference = dateBefore.getTime()-dateAfter.getTime();
                        daysBetween = (int) (difference / (1000*60*60*24));
                        daysBetween++;
                        if(daysBetween>2)
                            duedisp="due in "+daysBetween+" days";
                        else if(daysBetween==1)
                            duedisp="due tomorrow";

                    }

                    else if (dateAfter.compareTo(dateBefore) > 0) {

                        long difference =dateAfter.getTime() - dateBefore.getTime();
                        daysBetween = (int) (difference / (1000*60*60*24));
                        if(daysBetween==0)
                            duedisp="due today";
                        else
                        duedisp="overdue by "+daysBetween+" days";
                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String mtime=timeTextView.getText().toString().trim();
                String date = java.text.DateFormat.getDateInstance().format(new Date());


                if(TextUtils.isEmpty(mTask)){
                    mtask.setError("Task REQUIRED");
                    return;
                }
                else if(TextUtils.isEmpty(mDescription)){
                    mdescription.setError("Task REQUIRED");
                    return;
                }
                else if(TextUtils.isEmpty(mdate)) {
                    dateTextView.setError("date REQUIRED");
                    return;
                }
                else if(TextUtils.isEmpty(mtime)){
                    timeTextView.setError("time REQUIRED");
                    return;}
                else{

                    //add to model class
                    String id =reference.push().getKey();
                    Model model;
                    model = new Model(mTask, mDescription, id, duedisp, mdate, mtime,priority);
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddTask.this, "Task has been inserted succesfully", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                            else
                            {
                                String error =task.getException().toString();
                                Toast.makeText(AddTask.this, "Failed"+error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });


                    Intent intent5=new Intent(AddTask.this,ToDoList.class);
                    intent5.putExtra("password", password);
                    intent5.putExtra("fullname", fullname);
                    intent5.putExtra("profession", profession);
                    intent5.putExtra("phone", phone);
                    intent5.putExtra("email", email);
                    intent5.putExtra("username", username);
                    intent5.putExtra("category", "To Do List");

                    startActivity(intent5);

                }

            }
        });





    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        //int HOUR = calendar.get(Calendar.HOUR);
        //int MINUTE = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);

                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                dateTextView.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();




    }

    //private void handleTimeButton() {



      /*  Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                timeTextView.setText(dateText);
                startAlarm(calendar1);

            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/




    /* @Override
     public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
         Calendar c = Calendar.getInstance();
         c.set(Calendar.HOUR_OF_DAY, hourOfDay);
         c.set(Calendar.MINUTE, minute);
         c.set(Calendar.SECOND, 0);
        // updateTimeText(c);
         startAlarm(c);
     }*/

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c,i++);
    }
    private void updateTimeText(Calendar c) {
        //String timeText = "Alarm set for: ";
        // timeText += java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());
        String dateText = DateFormat.format("h:mm a", c).toString();
        timeTextView.setText(dateText);
        //mTextView.setText(timeText);
    }
    private void startAlarm(Calendar c,int i) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        String mTask =mtask.getText().toString().trim();
        String mDescription=mdescription.getText().toString().trim();
        intent.putExtra("username", username);
        intent.putExtra("title", mTask);
        intent.putExtra("content", mDescription);
        intent.putExtra("email",email);
        intent.putExtra("fullname", fullname);
        intent.putExtra("profession", profession);
        intent.putExtra("phone", phone);
        intent.putExtra("email", email);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}