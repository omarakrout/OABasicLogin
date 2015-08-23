package com.maro.basiclogin.utils;

import com.maro.basiclogin.entity.User;

import org.json.JSONObject;

/**
 * Created by Maro on 23/08/2015.
 */
public class ParseUser {
    public static User parseUserLogin(String response) throws Exception{
        User user = new User();
        JSONObject rootElement = new JSONObject(response);
        try {
            // Populating the user from the JSON response
            user.setName(rootElement.getString("name"));
            user.setEmail(rootElement.getString("email"));
            user.setToken(rootElement.getString("token"));
        }catch(Exception ex){
            try {
                // User not found
                throw new Exception(rootElement.getString("response"));
            }
                catch(Exception ex2){
                // Internal error
                return null;
            }

        }
        return user;
    }
}
