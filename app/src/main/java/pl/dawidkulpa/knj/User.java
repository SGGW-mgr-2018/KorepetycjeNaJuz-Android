package pl.dawidkulpa.knj;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class User {
    private String name;
    private String sname;
    private String email;
    private String phoneNo;
    private String aboutMe;
    private String password;
    private String loginToken;
    private int id;


    public String getName() {
        return name;
    }

    public String getSname() {
        return sname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAboutMe() {
        return aboutMe;
    }
    public String getPassword(){return password;}
    public String getLoginToken(){return loginToken;}
    public int getId(){return id;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void onLoginSuccessful(JSONObject jObj){
        try{
            loginToken= jObj.getString("token");

            String[] split= loginToken.split("\\.");
            byte[] decoded= Base64.decode(split[1], Base64.DEFAULT);
            JSONObject tokenJObj= new JSONObject(new String(decoded));

            id= tokenJObj.getInt("UserId");
            Log.e("User", "User id: "+id);
        } catch (JSONException je){
            Log.e("User", je.getMessage());
        }
    }

    public void onGetDataSuccessful(JSONObject jObj){

    }
}
