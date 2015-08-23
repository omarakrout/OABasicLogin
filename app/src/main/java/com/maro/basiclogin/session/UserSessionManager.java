package com.maro.basiclogin.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.maro.basiclogin.activity.LoginActivity;
import com.maro.basiclogin.entity.User;

import java.util.HashMap;

/**
 * Created by Maro on 22/08/2015.
 */
public class UserSessionManager {
    // User name
    public static final String KEY_NAME = "name";
    // Email address
    public static final String KEY_EMAIL = "email";
    // Sharedpref file name
    private static final String PREFER_NAME = "BasicLogin";
    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsLoggedIn";
    public static UserSessionManager sInstance = null;
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static UserSessionManager getInstance(Context context){
        if (sInstance == null)
        return new UserSessionManager(context);
        else return sInstance;
    }

    //Create login session
    public void createUserLoginSession(User user){
    // Storing login value as TRUE
    editor.putBoolean(IS_USER_LOGIN, true);

    // Storing name in pref
    editor.putString(KEY_NAME, user.getName());

        // Storing email in pref
    editor.putString(KEY_EMAIL, user.getEmail());

    // commit changes
    editor.commit();
}

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if (this.isUserLoggedIn())
            return false;

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;


    }



    /**
     * Get stored session data
     * */
    public User getUserDetails(){

        // create new User
        User user = new User();

        // Username
        user.setName(pref.getString(KEY_NAME, null));

        // User email
        user.setEmail(pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
