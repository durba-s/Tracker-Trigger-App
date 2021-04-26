package com.example.myapplication3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
    Button next;
    //private ImageView mlogo;


    // google
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private  String TAGLE = "MainActivity";
    private Button btnSignOut;
    private int RC_SIGN_IN = 1;

    float v=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseauth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.sign_in_button);
        btnSignOut = findViewById(R.id.sign_out_button);
        next=findViewById(R.id.button3);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(LoginActivity.this,"You are Logged Out",Toast.LENGTH_SHORT).show();
                btnSignOut.setVisibility(View.INVISIBLE);
            }
        });

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

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

       startActivityForResult(signInIntent, RC_SIGN_IN);

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

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{

            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mFirebaseauth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mFirebaseauth.getCurrentUser();
                        updateUI_G(user);
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUI_G(null);
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void updateUI_G(FirebaseUser fUser){
        btnSignOut.setVisibility(View.VISIBLE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            next.setVisibility(View.VISIBLE);


            Toast.makeText(LoginActivity.this,personName + personEmail ,Toast.LENGTH_SHORT).show();
            next.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent3=new Intent(LoginActivity.this,DashboardDefault.class);
                    intent3.putExtra("fullname",personName);
                    intent3.putExtra("username",personName);
                    intent3.putExtra("password", "null");
                    intent3.putExtra("profession", "others");
                    intent3.putExtra("phone","null");
                    intent3.putExtra("email",personEmail);
                    startActivity(intent3);
                }});
        }

    }


}