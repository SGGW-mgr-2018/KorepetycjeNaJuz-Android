package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Messages.Message;
import pl.dawidkulpa.knj.Messages.MessagesListAdapter;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class ConversationFragment extends Fragment {

    private int withId;
    private String withName;
    private ArrayList<Message> messages;
    private MessagesListAdapter messagesListAdapter;
    private TimerTask refreshTask;
    private Timer refreshTimer;
    private User user;
    private ListView listView;

    public ConversationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ConversationFragment newInstance() {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_conversation, container, false);

        messages= new ArrayList<>();

        user= ((HomeActivity)getContext()).getLogedInUser();

        rootView.findViewById(R.id.send_message_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMessageButtonClick();
            }
        });
        listView= rootView.findViewById(R.id.messages_list_view);

        ((TextView)rootView.findViewById(R.id.with_text)).setText(withName);

        refreshConversation();
        refreshTask= new TimerTask() {
            @Override
            public void run() {
                refreshConversation();
            }
        };
        refreshTimer= new Timer();
        refreshTimer.schedule(refreshTask, 10000, 10000);

        return rootView;
    }

    private void refreshConversation(){
        user.refreshConversation(withId, new User.ConversationRefreshListener() {
            @Override
            public void onConversationRefreshFinished(ArrayList<Message> messages) {
                onsConversationRefreshFinished(messages);
            }
        });
    }

    public void onsConversationRefreshFinished(ArrayList<Message> messages){
        boolean scroll= messages.size()!=this.messages.size();

        if(listView==null)
            listView= getView().findViewById(R.id.messages_list_view);

        if(listView!=null) {
            if (messages.size() > this.messages.size()) {
                Log.e("Cf", "this.ms: "+this.messages.size()+", ms: "+messages.size());
                Log.e("CF", "Add just new");
                for (int i = this.messages.size(); i < messages.size(); i++) {
                    this.messages.add(messages.get(i));
                }
            } else {
                Log.e("Cf", "this.ms: "+this.messages.size()+", ms: "+messages.size());
                Log.e("CF", "Add all");
                this.messages.clear();
                this.messages.addAll(messages);
            }

            if (messagesListAdapter == null)
                messagesListAdapter = new MessagesListAdapter(getContext(), this.messages, withId);

            if (listView.getAdapter() == null)
                listView.setAdapter(messagesListAdapter);

            messagesListAdapter.notifyDataSetChanged();

            if (scroll)
                listView.setSelection(messagesListAdapter.getCount() - 1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        refreshTimer.cancel();
        super.onDetach();
    }

    public void setWithId(int withId){
        this.withId= withId;
        if(messages!=null)
            messages.clear();
        messagesListAdapter=null;
    }

    public void setWithName(String withName){
        this.withName= withName;
    }

    public void onSendMessageButtonClick(){
        TextView textView= getView().findViewById(R.id.send_message_text);
        String text= textView.getText().toString();

        if(!text.isEmpty()){
            ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
                @Override
                public void onFinish(int respCode, JSONObject jObject) {
                    onSendMessageFinished(respCode);
                }
            }, Query.BuildType.JSONPatch);

            Query messageDto= new Query();
            messageDto.addPair("recipientId", String.valueOf(withId));
            messageDto.addPair("content", text);
            scm.addPOSTPair("", messageDto);
            scm.addHeaderEntry("Authorization", "Bearer "+user.getLoginToken());
            scm.setMethod(ServerConnectionManager.METHOD_POST);
            scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);

            scm.start(HomeActivity.SERVER_NAME+"/Messages");

            textView.setText("");
        }
    }

    public void onSendMessageFinished(int rCode){
        if(rCode==201){
            refreshConversation();
        }
    }
}
