package pl.dawidkulpa.knj.Messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Conversation {
    private int ownerId;
    private int withId;
    private String withName;
    private String withSecondName;
    private Message lastMsg;

    public static Conversation create(JSONObject jObj){
        Conversation conversation= new Conversation();

        try{
            conversation.ownerId= jObj.getInt("userId");
            conversation.withId= jObj.getInt("userId");
            conversation.withName= jObj.getString("firstName");
            conversation.withSecondName= jObj.getString("lastName");

            JSONObject lastMsgObject= jObj.getJSONObject("lastMessage");
            conversation.lastMsg= Message.create(lastMsgObject);

        } catch (JSONException je){
            Log.e("Conversation", je.getMessage());
            return null;
        }

        return conversation;
    }


    public String getWithName(){
        return withName + withSecondName;
    }

    public Message getLastMsg() {
        return lastMsg;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getWithId() {
        return withId;
    }
}
