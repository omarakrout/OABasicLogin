package com.maro.basiclogin.webservicecalls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.maro.basiclogin.R;
import com.maro.basiclogin.activity.MainActivity;
import com.maro.basiclogin.entity.User;
import com.maro.basiclogin.session.UserSessionManager;
import com.maro.basiclogin.utils.MD5Crypter;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.maro.basiclogin.utils.FakeApi;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

/**
 * Created by Maro on 23/08/2015.
 */
public class LoginRequest extends AsyncTask< String, String, String > {

    private static final String NO_NETWORK = "noNetwork";

    private static final String INTERNAL_ERROR = "internalError";

    private static final String NO_INTERNET = "noInternet";

    private static final String INCORRECT_LOGS = "incorrectLogs";

    private static final String SERVER_DOWN = "serverDown";

    private ProgressDialog progressDialog;
    private Activity mActivity;
    private UserSessionManager session;

    public LoginRequest(ProgressDialog progressDialog, Activity mActivity, UserSessionManager session ){
        this.progressDialog = progressDialog;
        this.mActivity = mActivity;
        this.session = session;
    }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressDialog during execution
            progressDialog = ProgressDialog.show(mActivity, "", mActivity.getString(R.string.pd_please_wait), true);

            // When Logging in, if the user press the return button, the progressDialog
            // wil be dismissed and the AsyncTask cancelled
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // Close request
                    if (this != null)
                        if(getStatus() == AsyncTask.Status.RUNNING)
                            cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Check the network status
                if (!isNetworkConnected()) return NO_NETWORK;

                // Create the fake API
                FakeApi fakeApi = new FakeApi(mActivity);

                // Fake API returns a user
                User user;
                try {
                    user = fakeApi.executeLogin(params[0], MD5Crypter.md5(params[1]), mActivity.getString(R.string.api_key));
                }catch (Exception e){
                    return INTERNAL_ERROR;
                }

                // Check if an internal error occured
                if (user == null) return INCORRECT_LOGS;

                // Check if the user was found
//                if(user.getReason().equals("Not authentified")) return INCORRECT_LOGS;

                // Update the UserSession
                session.createUserLoginSession(user);

                // Starting MainActivity
                Intent i = new Intent(mActivity.getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(i);

                // Close the activity
                mActivity.finish();
                return null;

            }catch (Exception e){
                if (!isOnline()) return NO_INTERNET;
            }
            return SERVER_DOWN;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            String message;
            if (result == null) return;
            switch (result){
                // user with wrong credentials
                case INCORRECT_LOGS: message = mActivity.getString(R.string.alert_incorrect_logs);
                    break;
                case NO_NETWORK: message = mActivity.getString(R.string.alert_no_network);
                    break;
                case NO_INTERNET: message = mActivity.getString(R.string.alert_no_internet);
                    break;
                case SERVER_DOWN: message = mActivity.getString(R.string.alert_server_down);
                    break;
                case INTERNAL_ERROR: message = mActivity.getString(R.string.alert_internal_error);
                    break;
                default : message = "";
            }

            if (!message.equals("")) {

                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(mActivity);

                dialogBuilder
                        .withTitle("Error")
                        .withMessage(message)
                        .withDuration(700)
                        .withEffect(Effectstype.Shake)
                        .show();
            }
        }


    private boolean isNetworkConnected() {
        // Check network status
        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null);
    }


    private boolean isOnline() {
        try{
            // Ping to google to check internet connectivity
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 80);
            socket.connect(socketAddress, 3000);
            socket.close();
            return true;
        } catch (Exception e) {
            // internet not working
            return false;
        }
    }
}
