package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Messages.Conversation;
import pl.dawidkulpa.knj.Messages.ConversationsListAdapter;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;

public class MessagesFragment extends Fragment {
    private ConversationsListAdapter conversationsListAdapter;

    public MessagesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_messages, container, false);

        ((HomeActivity)getContext()).getLogedInUser().refreshConversations(new User.ConversationsRefreshListener() {
            @Override
            public void onConversationsRefreshFinished(ArrayList<Conversation> conversations) {
                onConvRefreshFinished(conversations);
            }
        });


        return rootView;
    }

    public void onConvRefreshFinished(ArrayList<Conversation> conversations){
        conversationsListAdapter= new ConversationsListAdapter(getContext(), conversations, new ConversationsListAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int withId) {
                onConversationClick(withId);
            }
        });
        ((ListView)getView().findViewById(R.id.conversation_list_view)).setAdapter(conversationsListAdapter);

    }

    private void onConversationClick(int withId){
        ((HomeActivity)getContext()).showConversation(withId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
