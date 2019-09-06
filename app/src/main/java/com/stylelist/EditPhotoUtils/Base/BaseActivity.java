package com.stylelist.EditPhotoUtils.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by Hoang Anh Tuan on 3/10/2017.
 */

abstract class BaseActivity extends FragmentActivity {

    public boolean runnable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runnable = true;
    }

    @Override
    protected void onDestroy() {
        runnable = false;
        super.onDestroy();
    }

    /**
     * show notification
     *
     * @param message infor show
     */
    protected void showMessage(int message) {
        Toast.makeText(this, getString(message), Toast.LENGTH_LONG).show();
    }
}
