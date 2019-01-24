package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Lessons.CalendarListAdapter;
import pl.dawidkulpa.knj.Lessons.HistoryLessonEntry;
import pl.dawidkulpa.knj.Lessons.HistoryListAdapter;
import pl.dawidkulpa.knj.Lessons.LessonEntry;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class HistoryFragment extends Fragment {

    private ListView historyListView;
    private HistoryListAdapter historyListAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_history, container, false);

        ((HomeActivity)getContext()).getLogedInUser().refreshHistory(new User.HistoryRefreshListener() {
            @Override
            public void onHistoryRefreshFinished(ArrayList<HistoryLessonEntry> historyEntries) {
                onHistoryRefreshed(historyEntries);
            }
        });

        return rootView;
    }

    public void onHistoryRefreshed(ArrayList<HistoryLessonEntry> historyLessonEntries){
        historyListView= getView().findViewById(R.id.history_list_view);
        historyListAdapter= new HistoryListAdapter(getContext(), historyLessonEntries, new HistoryListAdapter.StarButtonClickListener() {
            @Override
            public void onStarButtonClick(int id, int rating) {
                sendRating(id, rating);
            }
        });
        historyListView.setAdapter(historyListAdapter);

        if(historyLessonEntries.isEmpty()){
            getView().findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.empty_text).setVisibility(View.GONE);
        }

        getView().findViewById(R.id.progressbar).setVisibility(View.GONE);
    }

    public void sendRating(int id, int rating){
        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int rCode, JSONObject jObject) {
                if(rCode==200){
                    refreshHistory();
                }
            }
        }, Query.BuildType.JSONPatch);

        Query lessonRatingDto= new Query();
        lessonRatingDto.addPair("lessonId", String.valueOf(id));
        lessonRatingDto.addPair("rating", String.valueOf(rating+1));
        lessonRatingDto.addPair("opinion", "");

        scm.addPOSTPair("", lessonRatingDto);
        scm.addHeaderEntry("Authorization", "Bearer "+((HomeActivity)getContext()).getLogedInUser().getLoginToken());
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.setMethod(ServerConnectionManager.METHOD_POST);
        scm.start(HomeActivity.SERVER_NAME+"/Lesson/Rating/Post");
    }

    public void refreshHistory(){
        ((HomeActivity)getContext()).getLogedInUser().refreshHistory(new User.HistoryRefreshListener() {
            @Override
            public void onHistoryRefreshFinished(ArrayList<HistoryLessonEntry> historyEntries) {
                onHistoryRefreshed(historyEntries);
            }
        });
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
