package com.maro.basiclogin.utils;

import android.app.Activity;

import com.maro.basiclogin.R;
import com.maro.basiclogin.entity.Response;
import com.maro.basiclogin.entity.User;

/**
 * Created by Maro on 22/08/2015.
 */
public class FakeApi {

    Activity mActivity;

    public FakeApi(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public User executeLogin(String email, String password, String API_KEY){
        Response response = new Response();
        if(API_KEY.equals(mActivity.getString(R.string.api_key))) {
            if ((email.equals(mActivity.getString(R.string.email))) && (password.equals(MD5Crypter.md5(mActivity.getString(R.string.password))))) {
                response.setReason("OK");
                response.setResponseCode(200);
                response.setStatus("{\n" +
                        "    \"name\": \""+mActivity.getString(R.string.name)+"\",\n" +
                        "    \"email\": \""+mActivity.getString(R.string.email)+"\",\n" +
                        "    \"token\": \""+mActivity.getString(R.string.token)+"\"\n" +
                        "}");
            } else {
                response.setReason("OK");
                response.setResponseCode(200);
                response.setStatus("{\"response\" : \"Incorrect credentials.\"}");
            }
        }else{
            response.setReason("OK");
            response.setResponseCode(200);
            response.setStatus("{\"response\" : \"Incorrect API Key.\"}");
        }
        try {
            return ParseUser.parseUserLogin(response.getStatus());
        }catch (Exception e){
            return null;
        }
    }
}
