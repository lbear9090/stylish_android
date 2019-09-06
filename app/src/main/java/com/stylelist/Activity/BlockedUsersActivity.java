package com.stylelist.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.R;

public class BlockedUsersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        CustomFontButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CustomFontTextView txtTitle = findViewById(R.id.title_text);
        txtTitle.setText("Blocked Users");
    }
}
