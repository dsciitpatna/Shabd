package com.dsciitp.shabd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.dsciitp.shabd.Category.CategoryFragment;
import com.dsciitp.shabd.Dictionary.DictionaryActivity;
import com.dsciitp.shabd.Home.HomeFragment;
import com.dsciitp.shabd.Learn.LearnActivity;
import com.dsciitp.shabd.QuickActions.QuickActionFragment;
import com.dsciitp.shabd.Setting.SettingFragment;


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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        setBaseFragment(savedInstanceState);
    }

    private void setBaseFragment(Bundle savedInstanceState){

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
        if (fragment instanceof HomeFragment){
            HomeFragment homeFragment = (HomeFragment) fragment;
            homeFragment.setOnCategorySelectedListener(this);
        }
    }

    @Override
    public void onTopicSelected(int categoryId) {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
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
