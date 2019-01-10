package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.Lessons.LessonEntry;
import pl.dawidkulpa.knj.Lessons.CalendarListAdapter;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;

public class CalendarFragment extends Fragment {

    private CalendarListAdapter calendarListAdapter;
    private ArrayList<LessonEntry> dayLessons;
    private Context context;
    private User logedInUser;

    public CalendarFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
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
        View view= inflater.inflate(R.layout.fragment_calendar, container, false);

        ((ListView)view.findViewById(R.id.lessons_list)).setAdapter(calendarListAdapter);
        calendarListAdapter.notifyDataSetChanged();

        ((CalendarView)view.findViewById(R.id.calendar)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                onDateChange(i, i1, i2);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dayLessons= new ArrayList<>();
        calendarListAdapter = new CalendarListAdapter(context, dayLessons);
        logedInUser= ((HomeActivity)context).getLogedInUser();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void onDateChange(int y, int m, int d){
        dayLessons.clear();
        ArrayList<LessonEntry> lessonEntries= logedInUser.getLessonsEntries();

        for(int i=0; i<lessonEntries.size(); i++){
            if(lessonEntries.get(i).getLesson().isAtDay(y, m, d)){
                dayLessons.add(lessonEntries.get(i));
            }
        }

        calendarListAdapter.notifyDataSetChanged();
    }
}
