package com.stylelist.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stylelist.Adapters.StyleGridAdapter;
import com.stylelist.Interfaces.StyleAdapterActionCallBack;
import com.stylelist.Models.Style;
import com.stylelist.R;
import com.stylelist.Utils.Constants;
import com.stylelist.Utils.StyleListApp;

import java.util.ArrayList;
import java.util.Collection;

import static com.stylelist.Utils.StyleListApp.mainActivity;
import static com.stylelist.Utils.StyleListApp.manStyles;
import static com.stylelist.Utils.StyleListApp.womanStyles;

public class MyStyleActivity extends Activity implements StyleAdapterActionCallBack, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int EDITOR_OUTPUT = 1;
    private Button btnDone, btnGuide;
    private GridView gridViewStyles;
    private TextView txtDescription;
    private StyleGridAdapter styleGridAdapter;
    private ParseUser currentUser;
    private boolean isMale = true;
    private ArrayList<String> myStyles = new ArrayList<>();
    private ArrayList<Style> styleArrayList = new ArrayList<>();
    private ArrayList<String> selectedStyles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_style);

        btnDone = findViewById(R.id.btn_my_style_done);
        btnGuide = findViewById(R.id.btn_my_style_guide);
        gridViewStyles = findViewById(R.id.gridview_my_styles);
        txtDescription = findViewById(R.id.text_style_description);

        Bundle bundle = getIntent().getExtras();
        int parent = bundle.getInt(Constants.PARENT_ACTIVITY);

        btnGuide.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        String gender = "male";

        currentUser = ParseUser.getCurrentUser();
        try {
            if (currentUser.fetchIfNeeded().get("Style") != null) {
                myStyles.addAll((Collection<? extends String>) currentUser.fetchIfNeeded().get("Style"));
                gender = currentUser.fetchIfNeeded().getString("StyleGender");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (gender == null || gender.equals("male") || gender.equals("Male")) {
            isMale = true;
            styleArrayList = manStyles;
        } else {
            isMale = false;
            styleArrayList = womanStyles;
        }

        gridViewStyles.setOnItemClickListener(this);

        setAction(parent);
    }

    private void setAction(int parent) {
        if (parent == Constants.PARENT_IS_PHOTO) {
            postingAction();
        } else if (parent == Constants.PARENT_IS_PROFILE) {
            profileAction();
        } else if (parent == Constants.PARENT_IS_OTHER_PROFILE) {
//            otherProfileAction();
        }
    }

    private void postingAction() {
        btnDone.setText("Cancel");
        btnGuide.setText("Save");
        btnGuide.setVisibility(View.VISIBLE);
        txtDescription.setText("Select the style that matches your look.");
        styleGridAdapter = new StyleGridAdapter(this, styleArrayList, this, true);
        gridViewStyles.setAdapter(styleGridAdapter);
    }

    private void profileAction() {
        btnDone.setText("Done");
        //btnGuide.setText("Style Guide");
        btnGuide.setVisibility(View.INVISIBLE);
        for (int i = 0; i < styleArrayList.size(); i++) {
            if (myStyles.contains(styleArrayList.get(i).styleName)) {
                styleArrayList.get(i).isSelected = true;
            } else {
                styleArrayList.get(i).isSelected = false;
            }
        }
        styleGridAdapter = new StyleGridAdapter(this, styleArrayList, this, false);
        gridViewStyles.setAdapter(styleGridAdapter);
    }

//    private void otherProfileAction() {
//        btnDone.setText("Done");
//        btnGuide.setText("Style Guide");
//        for (int i = 0; i < styleArrayList.size(); i++) {
//            if (myStyles.contains(styleArrayList.get(i).styleName)) {
//                styleArrayList.get(i).isSelected = true;
//            } else {
//                styleArrayList.get(i).isSelected = false;
//            }
//        }
//        styleGridAdapter = new StyleGridAdapter(this, styleArrayList, this, false);
//        gridViewStyles.setAdapter(styleGridAdapter);
//        gridViewStyles.setEnabled(false);
//    }

    @Override
    public void onDetail(int styleIndex) {
        Intent intent = new Intent(this, StyleDetailActivity.class);
        intent.putExtra("is_man", isMale);
        intent.putExtra("style_index", styleIndex);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == btnGuide) {
            if (btnGuide.getText().toString().equals("Save")) {
                promptToPhotoEditing();
            } else {

            }
        } else if (v == btnDone) {
            if (btnDone.getText().toString().equals("Done")) {
                saveMyStyles();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Style style = (Style) parent.getAdapter().getItem(position);
        if (btnGuide.getText().toString().equals("Save")) {
            if (selectedStyles.size() >= 2 && !style.isSelected) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You can only define a maximum of two styles to your post. Selected styles can be pressed again to unselect them.")
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                style.isSelected = !style.isSelected;
                if (style.isSelected) {
                    selectedStyles.add(style.styleName);
                } else {
                    selectedStyles.remove(style.styleName);
                }
            }
        } else {
            style.isSelected = !style.isSelected;
            if (style.isSelected) {
                myStyles.add(style.styleName);
            } else {
                myStyles.remove(style.styleName);
            }
        }
        styleGridAdapter.notifyDataSetChanged();
    }

    private void saveMyStyles() {
        currentUser.put("Style", myStyles);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    onBackPressed();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void promptToPhotoEditing() {
        StyleListApp.selectedStyles.clear();
        StyleListApp.selectedStyles.addAll(selectedStyles);
        finish();
        mainActivity.promptToEdit();
    }
}
