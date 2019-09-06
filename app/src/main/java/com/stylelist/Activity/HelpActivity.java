package com.stylelist.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.R;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        CustomFontButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CustomFontTextView txtTitle = findViewById(R.id.title_text);
        txtTitle.setText("Help");
    }

    public void commonQuestions(View v) {

    }

    public void guide(View v) {

    }

    public void generalFeedback(View v) {

    }

    public void reportProblem(View v) {

    }
}
