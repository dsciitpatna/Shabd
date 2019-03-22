package com.dsciitp.shabd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Arrays;

import com.dsciitp.shabd.Category.CategoryFragment;
import com.dsciitp.shabd.Dictionary.DictionaryActivity;
import com.dsciitp.shabd.Home.HomeFragment;
import com.dsciitp.shabd.Learn.LearnActivity;
import com.dsciitp.shabd.QuickActions.QuickActionFragment;
import com.dsciitp.shabd.Setting.SettingFragment;


    private TextView mTextMessage;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnCategorySelectedListener{
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    updateFragment(new HomeFragment(), 0);
                    return true;
                case R.id.navigation_quick:
                    updateFragment(new QuickActionFragment(), 1);
                    return true;
                case R.id.navigation_dictionary:
                    startActivity(new Intent(MainActivity.this, DictionaryActivity.class));
                    return true;
                case R.id.navigation_settings:
                    updateFragment(new SettingFragment(), 1);
                    return true;
                case R.id.navigation_learn:
                    startActivity(new Intent(MainActivity.this, LearnActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        setBaseFragment(savedInstanceState);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(MainActivity.this, "You're now signed in. Welcome to Shabd App", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    SignIn();
                }
            }
        };
    }

    public void SignIn() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    public void signout() {
        AuthUI.getInstance().signOut(this);
        Toast.makeText(this, "You are signed out", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "You're now signed in. Welcome to Shabd App", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseApp.initializeApp(this);
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStart() {
        FirebaseApp.initializeApp(this);
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void setBaseFragment(Bundle savedInstanceState) {

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            HomeFragment firstFragment = new HomeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) fragment;
            homeFragment.setOnCategorySelectedListener(this);
        }
    }

    public void onCategorySelected(int position) {

        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle args = new Bundle();
        categoryFragment.setArguments(args);
        transactFragment(categoryFragment);
    }

    private void transactFragment(Fragment frag){
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                .replace(R.id.fragment_container, frag, frag.getTag())
                .addToBackStack(frag.getTag())
                .commit();
    }
    private void updateFragment(Fragment fragment, int bStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        manager.popBackStackImmediate(1,1);

        if (bStack == 1) {
            transaction.addToBackStack(fragment.getTag());
        } else if (bStack == 0){
            manager.popBackStackImmediate();
        }
        transaction.commit();
    }
}
