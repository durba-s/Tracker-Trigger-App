package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_login,container,false);
        Button loginbtn;
        EditText uname,pass;
        TextView forgpswd;
        float v=0;
        uname=root.findViewById(R.id.username);
        pass=root.findViewById(R.id.password);
        loginbtn=root.findViewById(R.id.Loginbtn);
        forgpswd=root.findViewById(R.id.forgPass);

        uname.setTranslationX(800);
        pass.setTranslationX(800);
        loginbtn.setTranslationX(800);
        forgpswd.setTranslationX(800);


        uname.setAlpha(v);
        pass.setAlpha(v);
        loginbtn.setAlpha(v);
        forgpswd.setAlpha(v);


        uname.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgpswd.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginbtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String user=uname.getText().toString().trim();
                String pswd=pass.getText().toString().trim();

                if(TextUtils.isEmpty(user)){
                    uname.setError("This is a required field");
                    return;
                }

                if(TextUtils.isEmpty(pswd)){
                    pass.setError("This is a required field");
                    return;
                }


                Intent intent=new Intent(getActivity(),Dashboard.class);
                startActivity(intent);




            }});





        return root;

    }
}