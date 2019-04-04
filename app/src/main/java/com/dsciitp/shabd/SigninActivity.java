package com.dsciitp.shabd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class SigninActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

    private final int RC_SIGN_IN = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if(getIntent().hasExtra("logout_action") && getIntent().getStringExtra("logout_action").equals("logout")){
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
           logout();
        } else {
            showSplashScreen();
        }

    }


    private void showSplashScreen(){

        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        final SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        final ImageView imageView = findViewById(R.id.splash_icon);
        imageView.animate().scaleX(1.5f).scaleY(1.5f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageView.animate().translationYBy(-500f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (account == null) {
                            signInButton.animate().alpha(1f).setDuration(500);
                            signInButton.setClickable(true);
                        } else {
                            updateUI(account);
                        }
                    }
                });
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(getClass().getName(), "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account){
        if (account == null){
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
            finish();
        }
    }

    private void logout(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showSplashScreen();
                    }
                });
    }

}
