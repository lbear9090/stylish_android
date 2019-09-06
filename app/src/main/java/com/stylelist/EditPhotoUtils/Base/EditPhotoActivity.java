package com.stylelist.EditPhotoUtils.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.stylelist.R;

import static com.stylelist.Utils.StyleListApp.editPhotoActivity;


/**
 * Created by Hoang Anh Tuan on 12/14/2017.
 */

public class EditPhotoActivity extends BaseActivity {

    public int editActionAcount = 0;

    public static void start(Context context) {
        Intent starter = new Intent(context, EditPhotoActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_photo);
        addFragment(EditPhotoFragment.newInstance());
        editPhotoActivity = this;
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.rootMain, fragment);
        ft.commit();
    }

    public void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.rootMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
