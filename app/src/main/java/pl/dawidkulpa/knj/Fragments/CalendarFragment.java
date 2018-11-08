package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;

import pl.dawidkulpa.knj.Lesson;
import pl.dawidkulpa.knj.LessonsListAdapter;
import pl.dawidkulpa.knj.R;

public class CalendarFragment extends Fragment {

    private LessonsListAdapter lessonsListAdapter;
    private ArrayList<Lesson> dayLessons;
    private Context context;

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

        ((ListView)view.findViewById(R.id.lessons_list)).setAdapter(lessonsListAdapter);
        lessonsListAdapter.notifyDataSetChanged();

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
        lessonsListAdapter= new LessonsListAdapter(context, dayLessons);
        //if (context instanceof OnFragmentInteractionListener) {
        //   mListener = (OnFragmentInteractionListener) context;
        //} else {
        //throw new RuntimeException(context.toString()
        //          + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void onDateChange(int y, int m, int d){
        dayLessons.clear();

        for(int i=0; i<d; i++){
            dayLessons.add(new Lesson("Polski", 1, new Time(65700000), 90));
        }

        lessonsListAdapter.notifyDataSetChanged();
    }
}
