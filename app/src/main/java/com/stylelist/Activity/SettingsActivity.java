package com.stylelist.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.R;

public class SettingsActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private Switch chkLocation, chkReceipt, chkPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chkLocation = findViewById(R.id.switch_settings_location);
        chkReceipt = findViewById(R.id.switch_settings_send_receipt);
        chkPush = findViewById(R.id.switch_settings_push_notification);
        CustomFontButton btnBack = findViewById(R.id.backButton);

        chkPush.setOnCheckedChangeListener(this);
        chkReceipt.setOnCheckedChangeListener(this);
        chkLocation.setOnCheckedChangeListener(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CustomFontTextView txtTitle = findViewById(R.id.title_text);
        txtTitle.setText("Settings");
    }

    public void goToSecurity(View v) {
        startActivity(new Intent(this, SecurityActivity.class));
    }

    public void goToBlocked(View v) {
        startActivity(new Intent(this, BlockedUsersActivity.class));
    }

    public void goToWebSite(View v) {
        startActivity(new Intent(this, WebViewActivity.class));
    }

    public void deleteAccount(View v) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == chkLocation) {

        } else if (buttonView ==  chkReceipt) {

        } else if (buttonView == chkPush) {

        }
    }
}
