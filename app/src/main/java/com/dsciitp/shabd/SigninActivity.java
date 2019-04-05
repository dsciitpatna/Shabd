package com.dsciitp.shabd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

    private final int RC_SIGN_IN = 555;
    SharedPreferences prefs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (getIntent().hasExtra("logout_action") && getIntent().getStringExtra("logout_action").equals("logout")) {
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
            logout();
        } else {
            showSplashScreen();
        }

    }

    private void showSplashScreen() {

//        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        final ImageView imageView = findViewById(R.id.splash_icon);
        imageView.animate().scaleX(1.5f).scaleY(1.5f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageView.animate().translationYBy(-500f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (currentUser == null) {
                            signInButton.animate().alpha(1f).setDuration(500);
                            signInButton.setClickable(true);
                        } else {
                            updateUI(currentUser);
                        }
                    }
                });
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account == null) {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }else {
                firebaseAuthWithGoogle(account);
            }
        } catch (ApiException e) {
            Log.w(getClass().getName(), "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(getClass().getName(), "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(getClass().getName(), "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(getClass().getName(), "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.signin_main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        } else {

            if (prefs.getBoolean("firstRun", true)) {
//                downloadData();
            }

            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            intent.putExtra("account", user);
            startActivity(intent);
            finish();
        }
    }

    private void logout() {
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showSplashScreen();
                    }
                });
    }

}
