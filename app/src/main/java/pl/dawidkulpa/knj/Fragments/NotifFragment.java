package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.Lessons.LessonEntry;
import pl.dawidkulpa.knj.NotifsListAdapter;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class NotifFragment extends Fragment {

    private NotifsListAdapter notifsListAdapter;
    private ArrayList<LessonEntry> lessonEntries;


    public NotifFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NotifFragment newInstance() {
        NotifFragment fragment = new NotifFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_notif, container, false);

        User user= ((HomeActivity)getContext()).getLogedInUser();

        lessonEntries= user.getLessonsEntries();
        notifsListAdapter= new NotifsListAdapter(getContext(), lessonEntries, new NotifsListAdapter.ControlButtonsClickListener() {
            @Override
            public void onSendMessageClick(LessonEntry lessonEntry) {
                sendMessageButtonClick(lessonEntry);
            }

            @Override
            public void onConfirmLessonClick(LessonEntry lessonEntry) {
                confirmLessonButtonClick(lessonEntry);
            }

            @Override
            public void onDeclineLessonClick(LessonEntry lessonEntry) {
                declineLessonButtonClick(lessonEntry);
            }
        });
        ListView listView= rootView.findViewById(R.id.notifs_list);
        listView.setAdapter(notifsListAdapter);
        filterNotifs();

        if(lessonEntries.isEmpty()){
            rootView.findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.empty_text).setVisibility(View.GONE);
        }

        return rootView;
    }

    public void filterNotifs(){
        for(int i=0; i<lessonEntries.size(); i++){
            if(lessonEntries.get(i).getStatusName().equals("Odrzucona")) {
                lessonEntries.remove(i);
                i--;
            }

        }

        notifsListAdapter.notifyDataSetChanged();
    }

    public void sendMessageButtonClick(LessonEntry lessonEntry){
        ((HomeActivity)getContext()).showConversation(lessonEntry.getStudentId(), lessonEntry.getStudentName()+" "+lessonEntry.getStudentSName());

    }

    public void confirmLessonButtonClick(LessonEntry lessonEntry){
        User user= ((HomeActivity)getContext()).getLogedInUser();


        ServerConnectionManager scm= new ServerConnectionManager(Query.BuildType.Pairs, new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                confirmFinished(respCode);
            }
        });
        scm.setMethod(ServerConnectionManager.METHOD_POST);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addPOSTPair("id", lessonEntry.getId());
        scm.addHeaderEntry("Authorization", "Bearer "+user.getLoginToken());

        scm.start(HomeActivity.SERVER_NAME+"/Lesson/Approve");
    }

    public void declineLessonButtonClick(LessonEntry lessonEntry){
        User user= ((HomeActivity)getContext()).getLogedInUser();


        ServerConnectionManager scm= new ServerConnectionManager(Query.BuildType.Pairs, new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                declineFinished(respCode);
            }
        });
        scm.setMethod(ServerConnectionManager.METHOD_POST);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.addPOSTPair("id", lessonEntry.getId());
        scm.addHeaderEntry("Authorization", "Bearer "+user.getLoginToken());

        scm.start(HomeActivity.SERVER_NAME+"/Lesson/Reject");
    }

    public void confirmFinished(int rCode){
        switch (rCode){
            case 200:
                ((HomeActivity)getContext()).putSnackbar(getString(R.string.info_aprove_success));
                refreshLessonEntries();
                break;
            default:
                ((HomeActivity)getContext()).putSnackbar(getString(R.string.info_server_error));
        }


    }

    public void declineFinished(int rCode){
        switch (rCode){
            case 200:
                ((HomeActivity)getContext()).putSnackbar(getString(R.string.info_reject_success));
                refreshLessonEntries();
                break;
            default:
                ((HomeActivity)getContext()).putSnackbar(getString(R.string.info_server_error));
        }
    }

    public void refreshLessonEntries(){
        ((HomeActivity)getContext()).getLogedInUser().refreshLessonEntries(new User.LessonEntriesRefreshListener() {
            @Override
            public void onLessonEntriesRefreshFinished(ArrayList<LessonEntry> lessonEntries) {
                onLessonEntriesChanged(lessonEntries);
            }
        });
    }

    public void onLessonEntriesChanged(ArrayList<LessonEntry> newEntries){
        notifsListAdapter.notifyDataSetChanged();
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
