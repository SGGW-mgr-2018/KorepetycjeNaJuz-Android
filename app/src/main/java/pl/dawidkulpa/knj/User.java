package pl.dawidkulpa.knj;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Lessons.HistoryLessonEntry;
import pl.dawidkulpa.knj.Lessons.Lesson;
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

    private boolean loggedin;

    private ArrayList<LessonEntry> myLessonsEntries;

    private ArrayList<Conversation> conversations;

    private ConversationsRefreshListener conversationsRefreshListener;
    private ConversationRefreshListener conversationRefreshListener;
    private HistoryRefreshListener historyRefreshListener;
    private UpdateFinishListener updateFinishListener;
    private LessonEntriesRefreshListener lessonEntriesRefreshListener;



    public interface ConversationsRefreshListener {
        void onConversationsRefreshFinished(ArrayList<Conversation> conversations);
    }

    public interface ConversationRefreshListener{
        void onConversationRefreshFinished(ArrayList<Message> messages);
    }
    public interface HistoryRefreshListener{
        void onHistoryRefreshFinished(ArrayList<HistoryLessonEntry> historyEntries);
    }
    public interface UpdateFinishListener{
        void onUpdateFinished(int rCode);
    }
    public interface LessonEntriesRefreshListener{
        void onLessonEntriesRefreshFinished(ArrayList<LessonEntry> lessonEntries);
    }

    public User() {
        myLessonsEntries= new ArrayList<>();
        loggedin=false;
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
        if(phoneNo.equals("null"))
            return "";
        else
            return phoneNo;
    }
    public String getAboutMe() {
        if(aboutMe.equals("null"))
            return "";
        else
            return aboutMe;
    }
    public String getPassword(){return password;}
    public String getLoginToken(){return loginToken;}
    public int getId(){return id;}
    public boolean isLoggedin(){
        return loggedin;
    }

    public void logout(){
        loggedin= false;
    }

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


    // ########## API Setters ##########
    public void updateMyData(String phoneNo, String aboutMe, String pass,
                             UpdateFinishListener updateFinishListener){
        this.updateFinishListener= updateFinishListener;

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onUpdateMyDataFinished(respCode, jObject);
            }
        }, Query.BuildType.JSONPatch);
        scm.setMethod(ServerConnectionManager.METHOD_PUT);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addHeaderEntry("Authorization", "Bearer "+loginToken);

        Query userEditDto= new Query();
        userEditDto.addPair("id", String.valueOf(id));
        userEditDto.addPair("firstName", name);
        userEditDto.addPair("lastName", sname);
        userEditDto.addPair("password", pass);
        userEditDto.addPair("telephone", phoneNo);
        userEditDto.addPair("description", aboutMe);
        userEditDto.addPair("avatar", "none");
        userEditDto.addPair("privacyPolicesConfirmed", "true");

        scm.addPOSTPair("", userEditDto);
        scm.start(HomeActivity.SERVER_NAME+"/Users/Update");
    }


    // ########## Refreshers ##########
    public void refreshLessonEntries(LessonEntriesRefreshListener lessonEntriesRefreshListener){
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

        this.lessonEntriesRefreshListener= lessonEntriesRefreshListener;

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

    public void refreshHistory(HistoryRefreshListener historyRefreshListener){
        this.historyRefreshListener= historyRefreshListener;

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onHistryRefreshFinished(respCode, jObject);
            }
        }, Query.BuildType.Pairs);
        scm.setMethod(ServerConnectionManager.METHOD_GET);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addHeaderEntry("Authorization", "Bearer "+loginToken);

        scm.start(HomeActivity.SERVER_NAME+"/CoachLesson/History");
    }


    // ########## Callbacks ##########
    public void onLoginSuccessful(JSONObject jObj){
        try{
            loginToken= jObj.getString("token");

            String[] split= loginToken.split("\\.");
            byte[] decoded= Base64.decode(split[1], Base64.DEFAULT);
            JSONObject tokenJObj= new JSONObject(new String(decoded));

            id= tokenJObj.getInt("UserId");
            loggedin=true;
        } catch (JSONException je){
            Log.e("User", je.getMessage());
        }
    }

    public void onGetDataSuccessful(JSONObject jObj){
        try{
            name= jObj.getString("firstName");
            sname= jObj.getString("lastName");
            email= jObj.getString("email");
            phoneNo= jObj.getString("telephone");
            aboutMe= jObj.getString("description");
        } catch (JSONException je){
            Log.e("User", je.getMessage());
        }
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
                myLessonsEntries.clear();
                JSONArray jArr= jObj.getJSONArray("array");
                JSONObject jEntry;

                for(int i=0; i<jArr.length(); i++){
                    jEntry= jArr.getJSONObject(i);
                    ArrayList<LessonEntry> entries= LessonEntry.create(jEntry);

                    myLessonsEntries.addAll(entries);
                }

                if(lessonEntriesRefreshListener!=null){
                    lessonEntriesRefreshListener.onLessonEntriesRefreshFinished(myLessonsEntries);
                }

            } catch (JSONException je){
                Log.e("User", je.getMessage());
            }
        }
    }

    private void onHistryRefreshFinished(int rCode, JSONObject jObj){
        if(rCode==200){
            ArrayList<HistoryLessonEntry> historyLessonEntries= new ArrayList<>();

            try {
                JSONArray jArray = jObj.getJSONArray("array");

                for(int i=0; i<jArray.length();i++){
                    historyLessonEntries.add(HistoryLessonEntry.create(jArray.getJSONObject(i)));
                }
            }catch (JSONException je){
                Log.e("User", je.getMessage());
            }

            if(historyRefreshListener!=null){
                historyRefreshListener.onHistoryRefreshFinished(historyLessonEntries);
            }
        }
    }

    private void onUpdateMyDataFinished(int rCode, JSONObject jObj){
        Log.e("Error code", String.valueOf(rCode));

        if(rCode==200){
            onGetDataSuccessful(jObj);
        }

        if(updateFinishListener!=null){
            updateFinishListener.onUpdateFinished(rCode);
        }
    }
}