package com.example.myapplication3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Signup extends Fragment implements AdapterView.OnItemSelectedListener {


    String[] userType = {"Select User Type", "Working Professionals",
            "Job Seekers", "Home Makers", "others"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup, container, false);
        EditText mFullName, mUsername, mPassword, mPassword1, mEmail;
        Spinner mProf;
        Button Registerbtn;
        float v = 0;


        mFullName = root.findViewById(R.id.mname);
        mPassword = root.findViewById(R.id.mpassword);
        mPassword1 = root.findViewById(R.id.mpassword1);
        mUsername = root.findViewById(R.id.musername);
        mEmail = root.findViewById(R.id.memail);
        mProf = (Spinner) root.findViewById(R.id.mprof);
        mProf.setOnItemSelectedListener(this);


        ArrayAdapter ad = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_item, userType);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mProf.setAdapter(ad);


        Registerbtn = root.findViewById(R.id.regbtn);

        mFullName.setTranslationX(800);
        mPassword.setTranslationX(800);
        mPassword1.setTranslationX(800);
        mUsername.setTranslationX(800);
        mEmail.setTranslationX(800);
        mProf.setTranslationX(800);


        mFullName.setAlpha(v);
        mPassword.setAlpha(v);
        mPassword1.setAlpha(v);
        mUsername.setAlpha(v);
        mEmail.setAlpha(v);
        mProf.setAlpha(v);


        mFullName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        mPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        mPassword1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        mUsername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        mEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        mProf.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        String semail = "tracktrigger2020@gmail.com";
        String spassword = "oopProject";

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String password1 = mPassword1.getText().toString().trim();
                String fullname = mFullName.getText().toString().trim();
                String profession = mProf.getSelectedItem().toString().trim();
                String email = mEmail.getText().toString().trim();


                if (TextUtils.isEmpty(fullname)) {
                    mFullName.setError("This is a required field");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("This is a required field");
                    return;
                }
                String regex1 = "^[A-Za-z]\\w{5,29}$";
                Pattern p1 = Pattern.compile(regex1);
                Matcher m1 = p1.matcher(username);
                if (!m1.matches()) {
                    mUsername.setError("Invalid Username");
                    return;
                }


                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("This is a required field");
                    return;
                }

                String regex = "^(?=.*[0-9])"
                        + "(?=.*[a-z])(?=.*[A-Z])"
                        + "(?=.*[@#$%^&+=])"
                        + "(?=\\S+$).{8,20}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(password);
                if (!m.matches()) {
                    if (password.length() < 8) mPassword.setError("Password too short");
                    else if (password.length() >= 20) mPassword.setError("Password too long");
                    else mPassword.setError("Password too weak");
                    return;
                }


                if (TextUtils.isEmpty(password1)) {
                    mPassword1.setError("This is a required field");
                    return;
                }
                if (!password.equals(password1)) {
                    mPassword1.setError("Passwords do not match");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("This is a required field");
                    return;
                }

                //String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String Small_chars = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";
                String values = Small_chars +
                        numbers;
                Random rndm_method = new Random();
                char[] code = new char[8];

                for (int i = 0; i < 8; i++) {
                    code[i] = values.charAt(rndm_method.nextInt(values.length()));

                }
                String codeSend = new String(code);

                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(semail, spassword);
                    }
                });
                try {
                    Message message = new MimeMessage(session);

                    message.setFrom(new InternetAddress(semail));
                    message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));

                    message.setSubject("OTP for track trigger email verification");
                    message.setText("Dear User\nPlease enter this otp to verify your email address : " + codeSend+"\n"+"Do not share your otp with anyone\nRegards\nTeam TrackTrigger");

                    new SendMail().execute(message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    ;
                }


                Intent intent = new Intent(getActivity(), email.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("fullname", fullname);
                intent.putExtra("profession", profession);
                intent.putExtra("email", email);
                intent.putExtra("codeSend", codeSend);
                startActivity(intent);

            }
        });


        return root;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class SendMail extends AsyncTask<Message, String, String> {

        @Override

        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "true";

            } catch (MessagingException e) {
                e.printStackTrace();
                return  "false";

            }

        }

    }
}
