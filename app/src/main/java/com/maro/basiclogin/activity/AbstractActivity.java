package com.maro.basiclogin.activity;

import android.app.Activity;
import android.os.Bundle;

import com.maro.basiclogin.session.UserSessionManager;

/**
 * Created by Maro on 23/08/2015.
 */
public class AbstractActivity extends Activity{

    Activity mActivity;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = UserSessionManager.getInstance(getApplicationContext());

        mActivity = this;
    }
}