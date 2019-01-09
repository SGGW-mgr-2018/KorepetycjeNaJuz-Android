package pl.dawidkulpa.knj.Messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
    private int id;
    private String text;
    private int owner;
    private int recipient;

    public static Message create(JSONObject jObj){
        Message message= new Message();

        try{
            message.id= jObj.getInt("id");
            message.recipient= jObj.getInt("recipientId");
            message.owner= jObj.getInt("ownerId");
            message.text= jObj.getString("content");
            //TODO: Get date
        } catch (JSONException je){
            Log.e("Message", je.getMessage());
            return null;
        }

        return message;
    }

    public String getText() {
        return text;
    }

    public int getOwner() {
        return owner;
    }

    public int getRecipient() {
        return recipient;
    }

    public String getDate(){
        //TODO: Return real date
        return "20/01/2019 14:51";
    }
}
