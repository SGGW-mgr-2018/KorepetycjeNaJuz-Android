package pl.dawidkulpa.knj;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.Messages.Conversation;
import pl.dawidkulpa.knj.Messages.Message;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class User {
    private String name;
    private String sname;
    private String email;
    private String phoneNo;
    private String aboutMe;
    private String password;
    private String loginToken;
    private int id;

    ArrayList<Lesson> myLessons;
    ArrayList<Lesson> myCoachLessons;

    ArrayList<Conversation> conversations;

    ConversationsRefreshListener conversationsRefreshListener;
    ConversationRefreshListener conversationRefreshListener;

    public interface ConversationsRefreshListener {
        void onConversationsRefreshFinished(ArrayList<Conversation> conversations);
    }

    public interface ConversationRefreshListener{
        void onConversationRefreshFinished(ArrayList<Message> messages);
    }

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

    public void refreshCalendar(){
        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onGetCalendarFinished(respCode, jObject);
            }
        }, Query.BuildType.Pairs);
        scm.setMethod(ServerConnectionManager.METHOD_GET);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addHeaderEntry("Authorization", "Bearer "+loginToken);
        scm.addPOSTPair("DateFrom", "2018-01-05T17:26:05.313Z");
        scm.addPOSTPair("DateTo", "2030-01-05T17:26:05.313Z");

        scm.start(HomeActivity.SERVER_NAME+"/CoachLesson/Calendar");
    }

    private void onGetCalendarFinished(int rCode, JSONObject jObj){
        myLessons= new ArrayList<>();
        myCoachLessons= new ArrayList<>();

        if(rCode==200){
            try{
                Log.e("User", jObj.toString());
                JSONArray jArr= jObj.getJSONArray("array");
                JSONObject jEntry;

                for(int i=0; i<jArr.length(); i++){
                    jEntry= jArr.getJSONObject(i);

                    if(jEntry.getInt("userRole")==Lesson.ROLE_STUDENT){
                        //myLessons.add(Lesson.create(jEntry));
                    } else {
                        myCoachLessons.add(Lesson.create(jEntry));
                    }
                }

            } catch (JSONException je){
                Log.e("User", je.getMessage());
            }
        }
    }

    public void refreshConversations(ConversationsRefreshListener conversationsRefreshListener){
        this.conversationsRefreshListener = conversationsRefreshListener;

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onRefreshConversationsFinished(respCode, jObject);
            }
        }, Query.BuildType.Pairs);
        scm.setMethod(ServerConnectionManager.METHOD_GET);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addHeaderEntry("Authorization", "Bearer "+loginToken);

        scm.start(HomeActivity.SERVER_NAME+"/Messages");
    }

    private void onRefreshConversationsFinished(int rCode, JSONObject jObj){
        if(rCode==200){
            if(conversations==null){
                conversations= new ArrayList<>();
            }
            conversations.clear();

            try {
                JSONArray jArray = jObj.getJSONArray("array");
                JSONObject jEntry;

                for(int i=0; i<jArray.length(); i++){
                    jEntry= jArray.getJSONObject(i);

                    conversations.add(Conversation.create(jEntry));
                }

                if(conversationsRefreshListener !=null)
                    conversationsRefreshListener.onConversationsRefreshFinished(conversations);
            } catch (JSONException je){
                Log.e("User", je.getMessage());
            }
        }
    }

    public void refreshConversation(final int messId, ConversationRefreshListener conversationRefreshListener){
        this.conversationRefreshListener= conversationRefreshListener;

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onRefreshConversationFinished(respCode, jObject, messId);
            }
        }, Query.BuildType.Pairs);
        scm.setMethod(ServerConnectionManager.METHOD_GET);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addHeaderEntry("Authorization", "Bearer "+loginToken);

        scm.start(HomeActivity.SERVER_NAME+"/Messages/"+messId);
    }

    private void onRefreshConversationFinished(int rCode, JSONObject jObj, int messId){
        ArrayList<Message> messages= new ArrayList<>();
        if(rCode==200){

            try{
                JSONArray jArray= jObj.getJSONArray("array");

                for(int i=0; i<jArray.length(); i++){
                    messages.add(Message.create(jArray.getJSONObject(i)));
                }

            } catch (JSONException je){
                messages= null;
                Log.e("User", je.getMessage());
            }

            if(conversationRefreshListener!=null)
                conversationRefreshListener.onConversationRefreshFinished(messages);
        }
    }

    public ArrayList<Lesson> getMyLessons(){
        return myLessons;
    }

    public ArrayList<Lesson> getMyCoachLessons(){
        return myCoachLessons;
    }

    public ArrayList<Conversation> getConversations(){
        return conversations;
    }
}