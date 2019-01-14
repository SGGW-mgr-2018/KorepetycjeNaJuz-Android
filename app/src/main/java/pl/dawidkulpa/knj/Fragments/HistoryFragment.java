package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Lessons.CalendarListAdapter;
import pl.dawidkulpa.knj.Lessons.HistoryListAdapter;
import pl.dawidkulpa.knj.Lessons.LessonEntry;
import pl.dawidkulpa.knj.R;

public class HistoryFragment extends Fragment {

    private ListView historyListView;
    private HistoryListAdapter historyListAdapter;
    private ArrayList<LessonEntry> historyLessonEntries;

    public HistoryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
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

        historyLessonEntries= new ArrayList<>();
        historyListView= rootView.findViewById(R.id.history_list_view);
        historyListAdapter= new HistoryListAdapter(getContext(), historyLessonEntries, new HistoryListAdapter.StarButtonClickListener() {
            @Override
            public void onStarButtonClick(int i) {

            }
        });
        historyListView.setAdapter(historyListAdapter);

        filterLessonEntries();

        return rootView;
    }

    public void filterLessonEntries(){
        ArrayList<LessonEntry> userLessonEntries= ((HomeActivity)getContext()).getLogedInUser().getLessonsEntries();
        Date today= Calendar.getInstance().getTime();


        for(int i=0; i<userLessonEntries.size(); i++){
                historyLessonEntries.add(userLessonEntries.get(i));
        }

        historyListAdapter.notifyDataSetChanged();
        historyListAdapter.notifyDataSetInvalidated();
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
