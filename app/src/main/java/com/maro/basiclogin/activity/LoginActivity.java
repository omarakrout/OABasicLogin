package com.maro.basiclogin.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.maro.basiclogin.R;
import com.maro.basiclogin.webservicecalls.LoginRequest;

public class LoginActivity extends AbstractActivity {


    Button btnLogin;

    EditText etEmail, etPassword;

    TextView tvEmail, tvPassword;

    ProgressDialog progressDialog;

    LoginRequest loginTask;

    FormEditText etEmailValidator, etPasswordValidator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get Email, Password edit text
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword= (TextView) findViewById(R.id.tvPassword);

        etEmailValidator = (FormEditText) etEmail;
        etPasswordValidator = (FormEditText) etPassword;

        // Changing the email and password TextView depending on the
        // ones in the settings file used to grant the access to the
        // MainActivity
        String email = mActivity.getString(R.string.email);
        tvEmail.setText(mActivity.getString(R.string.tv_email, email));
        String pass = mActivity.getString(R.string.password);
        tvPassword.setText(mActivity.getString(R.string.tv_password, pass));


        // When the user press the done button, calls the doLogin()
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean isValidKey = event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                boolean isValidAction = actionId == EditorInfo.IME_ACTION_DONE;

                if (isValidKey || isValidAction) {
                    // validate then do login request
                    doLogin();
                }
                return false;
            }
        });

        // User Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);

        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // validate then do login request
                doLogin();
            }
        });

    }

    private void doLogin(){

        // Check if password and email are both valid
        if(etEmailValidator.testValidity() && etPasswordValidator.testValidity()){

        // Get username, password from EditText
        final String username = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

            loginTask = new LoginRequest(progressDialog, this, session);
            loginTask.execute(new String[]{username, password});
        }
    }
}
