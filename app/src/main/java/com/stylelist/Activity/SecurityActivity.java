package com.stylelist.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.R;

public class SecurityActivity extends Activity {

    private EditText edtEmail, edtOldPass, edtNewPass;
    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        CustomFontButton btnBack = findViewById(R.id.backButton);
        CustomFontTextView txtTitle = findViewById(R.id.title_text);

        txtTitle.setText("Security");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtEmail = findViewById(R.id.edit_security_email);
        edtOldPass = findViewById(R.id.edit_old_password);
        edtNewPass = findViewById(R.id.edit_new_password);
    }

    public void changePassword(View v) {
        if (isEmailValid() && !edtOldPass.getText().toString().isEmpty() && edtNewPass.getText().toString().isEmpty() && !edtOldPass.getText().toString().equals(edtNewPass.getText().toString())) {
            updatePassword();
        }
    }

    private boolean isEmailValid() {
        return edtEmail.getText().toString().equals(currentUser.getEmail());
    }

    private void updatePassword() {
        currentUser.setPassword(edtNewPass.getText().toString());
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    finish();
                }
            }
        });
    }
}
