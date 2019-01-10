package pl.dawidkulpa.knj;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Lessons.LessonEntry;
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

    private ArrayList<LessonEntry> myLessonsEntries;

    private ArrayList<Conversation> conversations;

    private ConversationsRefreshListener conversationsRefreshListener;
    private ConversationRefreshListener conversationRefreshListener;


    public interface ConversationsRefreshListener {
        void onConversationsRefreshFinished(ArrayList<Conversation> conversations);
    }

    public interface ConversationRefreshListener{
        void onConversationRefreshFinished(ArrayList<Message> messages);
    }

    public User() {
        myLessonsEntries= new ArrayList<>();
    }

    // ########## Getters ##########
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

    public ArrayList<LessonEntry> getLessonsEntries() {
        return myLessonsEntries;
    }

    // ########## Setters ##########
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    // ########## Refreshers ##########
    public void refreshLessonEntries(){
        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onGetLessonEntriesFinished(respCode, jObject);
            }
        }, Query.BuildType.Pairs);
        scm.setMethod(ServerConnectionManager.METHOD_GET);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addHeaderEntry("Authorization", "Bearer "+loginToken);
        scm.addPOSTPair("DateFrom", "2018-01-05T17:26:05.313Z");
        scm.addPOSTPair("DateTo", "2030-01-05T17:26:05.313Z");

        scm.start(HomeActivity.SERVER_NAME+"/CoachLesson/Calendar");
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

    public void refreshConversation(int messId, ConversationRefreshListener conversationRefreshListener){
        this.conversationRefreshListener= conversationRefreshListener;

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onRefreshConversationFinished(respCode, jObject);
            }
        }, Query.BuildType.Pairs);
        scm.setMethod(ServerConnectionManager.METHOD_GET);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addHeaderEntry("Authorization", "Bearer "+loginToken);

        scm.start(HomeActivity.SERVER_NAME+"/Messages/"+messId);
    }


    // ########## Callbacks ##########
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

    private void onRefreshConversationFinished(int rCode, JSONObject jObj){
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

    private void onGetLessonEntriesFinished(int rCode, JSONObject jObj){
        if(rCode==200){
            try{
                Log.e("User", jObj.toString());
                JSONArray jArr= jObj.getJSONArray("array");
                JSONObject jEntry;

                for(int i=0; i<jArr.length(); i++){
                    jEntry= jArr.getJSONObject(i);

                    myLessonsEntries.addAll(LessonEntry.create(jEntry));
                }

            } catch (JSONException je){
                Log.e("User", je.getMessage());
            }
        }
    }
}