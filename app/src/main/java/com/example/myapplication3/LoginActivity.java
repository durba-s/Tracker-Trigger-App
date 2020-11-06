package com.example.myapplication3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import static com.example.myapplication3.R.drawable.google;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    private LoginButton login;
    private TextView userId;
    public  CallbackManager callbackManager;
    private FirebaseAuth mFirebaseauth;
    private FirebaseAuth.AuthStateListener authStateListener;
     private static final String TAG ="FacebookAuthentication";
     private AccessTokenTracker accessTokenTracker;
    //private ImageView mlogo;


    float v=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseauth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());


        userId = findViewById(R.id.iiin);
        login = findViewById(R.id.login_button);
        login.setReadPermissions("email","public_profile");
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        //mlogo =findViewById(R.id.image_logo);
        callbackManager = CallbackManager.Factory.create();


        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "OnSuccess " + loginResult);
                habdle_facebook_token(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "ON CANCEL " );
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "ON ERROR " );


            }
        });
        authStateListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                   updateUI(user);
                }
                else
                {
                    updateUI( null);
                }
            }
        };
        accessTokenTracker =new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken==null)
                {
                    mFirebaseauth.signOut();
                }

            }
        };



        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tabLayout.setTranslationY(300);


        tabLayout.setAlpha(v);


        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();


    }

    private void habdle_facebook_token(AccessToken token)
    {
        Log.d(TAG,"Handle Facebook Token "+ token);
        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseauth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d(TAG,"Sign in with Credential is Successful ");
                    FirebaseUser user =mFirebaseauth.getCurrentUser();
                    updateUI(user);

                }
                else
                {
                    Log.d(TAG," Sign in with Credential has failed ",task.getException() );
                    Toast.makeText(LoginActivity.this, " Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void  updateUI(FirebaseUser user)
    {
        if(user!=null)
        {
             userId.setText(user.getDisplayName());
             if(user.getPhotoUrl()!=null)
             {
                 String photoUrl =user.getPhotoUrl().toString();
                 photoUrl+="?type=large";
                 //Picasso.get().load(photoUrl).into(mLogo);

             }

        }
        else
        {
            userId.setText("");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseauth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null)
        {
            mFirebaseauth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode ,resultCode, data);

    }


}