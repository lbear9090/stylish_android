package com.stylelist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.parse.ParseUser;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.Fragments.Intro1Fragment;
import com.stylelist.Fragments.Intro2Fragment;
import com.stylelist.Fragments.Intro3Fragment;
import com.stylelist.Fragments.Intro4Fragment;
import com.stylelist.R;

import me.relex.circleindicator.CircleIndicator;

public class IntroActivity extends FragmentActivity implements View.OnClickListener {

    private CustomFontButton btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Go to OldMainActivity when user is already logged in
        final ParseUser current = ParseUser.getCurrentUser();
        if (current.getObjectId() != null) {
            Log.i("Log", "User:" + current.getUsername());
            goToMainActivity();
        }
        initUI();
    }

    private void initUI() {
        btnLogin = findViewById(R.id.btn_intro_login);
        btnSignUp = findViewById(R.id.btn_intro_signup);
        ViewPager introFragmentPager = findViewById(R.id.intro_fragment_pager);
        CircleIndicator pagerIndicator = findViewById(R.id.intro_pager_indicator);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return Intro1Fragment.newInstance();
                } else if (position == 1) {
                    return Intro2Fragment.newInstance();
                } else if (position == 2) {
                    return Intro3Fragment.newInstance();
                } else if (position == 3) {
                    return Intro4Fragment.newInstance();
                } else {
                    return null;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
        introFragmentPager.setAdapter(fragmentPagerAdapter);
        pagerIndicator.setViewPager(introFragmentPager);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void goToMainActivity() {
        Intent Intent = new Intent(this, MainActivity.class);
        startActivity(Intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (v == btnSignUp) {
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
