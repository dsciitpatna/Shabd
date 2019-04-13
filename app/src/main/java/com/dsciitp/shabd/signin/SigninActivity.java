package com.dsciitp.shabd.signin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsciitp.shabd.MainActivity;
import com.dsciitp.shabd.R;
import com.dsciitp.shabd.UserConstants;
import com.dsciitp.shabd.database.WordsFromFirebase;
import com.dsciitp.shabd.database.WordsInRealm;
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

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

public class SigninActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

    private final int RC_SIGN_IN = 555;
    SharedPreferences prefs;
    private FirebaseAuth mAuth;
    ProgressDialog signInDialogue;
    ProgressDialog progressDialog;
    Realm realm;

    ImageView splashIcon;

    private static final String INTENT_ACTION = "intent_action";

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

        signInDialogue = new ProgressDialog(this);
        signInDialogue.setIndeterminate(true);
        signInDialogue.setMessage("Logging In...");
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Parsing Data...");

        splashIcon = findViewById(R.id.splash_icon);

        if (getIntent().hasExtra(INTENT_ACTION) && getIntent().getStringExtra(INTENT_ACTION).equals("logout")) {
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
            logout();
        } else if (getIntent().hasExtra(INTENT_ACTION) && getIntent().getStringExtra(INTENT_ACTION).equals("update")) {
            parseData();
            Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                showSplashScreen();
            } else {
                showSecondSplash(currentUser);
            }

        }

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

    }

    private void showSecondSplash(final FirebaseUser user) {
        splashIcon.setScaleX(1.5f);
        splashIcon.setScaleY(1.5f);
        splashIcon.setY(-100f);
//        splashIcon.animate().setDuration(200);
        new CountDownTimer(610, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                updateUI(user);
            }
        }.start();
    }

    private void showSplashScreen() {

        final SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        splashIcon.animate().scaleX(1.5f).scaleY(1.5f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                splashIcon.animate().y(300f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        signInButton.animate().alpha(1f).setDuration(500);
                        signInButton.setClickable(true);
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
            signInDialogue.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account == null) {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            } else {
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
        if (signInDialogue.isShowing()) {
            signInDialogue.dismiss();
        }

        if (user == null) {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        } else {

            UserConstants.email = user.getEmail();
            UserConstants.displayName = user.getDisplayName();
            UserConstants.phone = user.getPhoneNumber();
            UserConstants.photoUri = user.getPhotoUrl();

            if (prefs.getBoolean("firstRun", true)) {
                parseData();
            } else {
                launchApp();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void parseData() {
        progressDialog.show();


        new AsyncTask<String, Void, List<WordsFromFirebase>>() {

            @Override
            protected List<WordsFromFirebase> doInBackground(String... strings) {
                return SigninUtils.extractJson(SigninActivity.this);
            }

            @Override
            protected void onPostExecute(List<WordsFromFirebase> s) {

                realm.beginTransaction();
                for (WordsFromFirebase word : s) {

                    RealmQuery<WordsInRealm> query = realm.where(WordsInRealm.class);
                    query.equalTo("id", word.getId());
                    WordsInRealm result = query.findFirst();
                    if (result == null) {
                        WordsInRealm newWord = realm.createObject(WordsInRealm.class, word.getId());
                        newWord.setDescription(word.getDescription());
                        newWord.setTitle(word.getTitle());
                        newWord.setHindiTitle(word.getHindiTitle());
                        newWord.setImageResource(word.getImageResource());
                        newWord.setParentClass(word.getParentClass());
                        newWord.setIsItTopic(word.getIsItTopic());

                        realm.insert(newWord);
                        Log.e("mylog", word.getTitle());

                    } else {
                        result.setDescription(word.getDescription());
                        result.setTitle(word.getTitle());
                        result.setHindiTitle(word.getHindiTitle());
                        result.setImageResource(word.getImageResource());
                        result.setParentClass(word.getParentClass());
                        result.setIsItTopic(word.getIsItTopic());
                        realm.insertOrUpdate(result);
                    }
                }
                realm.commitTransaction();
                progressDialog.dismiss();
                prefs.edit().putBoolean("firstRun", false).apply();
                launchApp();
            }
        }.execute();

    }

    private void launchApp() {
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
