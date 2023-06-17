package com.example.fruitfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    Button login;
    FirebaseAuth auth;
    GoogleSignInClient signInClient;
    int RC_SIGN_IN = 40;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set value of login button
        login = findViewById(R.id.login_button);

        //set click listener on login button
        login.setOnClickListener(v -> signIn());

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        signInClient = GoogleSignIn.getClient(this, gso);
    }

    //method to sign usr in with google
    private void signIn() {
        Log.d("Firebase", "signIn()");
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Firebase", "onActivityResult()"+ resultCode);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Firebase", "before firebaseAuth(account.getIdToken());");
                firebaseAuth(account.getIdToken());
            } catch (ApiException e){
                Log.d("Firebase", "There is an error. " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuth(String idToken) {

        Log.d("Firebase", "FirebaseAuth()");

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = auth.getCurrentUser();

                    if(user == null){
                        Log.d("Firebase", "User is null?");
                    }
                    Log.d("Firebase", user.getDisplayName() + "... has been logged in!");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Error Signing In!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}