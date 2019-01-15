package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Lessons.LessonEntry;
import pl.dawidkulpa.knj.Lessons.CalendarListAdapter;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;

public class CalendarFragment extends Fragment {

    private static final String[] MONTHS={
            "Styczeń",
            "Luty",
            "Marzec",
            "Kwiecień",
            "Maj",
            "Czerwiec",
            "Lipiec",
            "Sierpień",
            "Wrzesień",
            "Październik",
            "Listopad",
            "Grudzień"
    };

    private CompactCalendarView calendarView;
    private CalendarListAdapter calendarListAdapter;
    private ArrayList<LessonEntry> dayLessons;
    private User logedInUser;

    public CalendarFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_calendar, container, false);

        ((ListView)view.findViewById(R.id.lessons_list)).setAdapter(calendarListAdapter);
        calendarListAdapter.notifyDataSetChanged();

        calendarView= view.findViewById(R.id.calendar);
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Calendar calendar= Calendar.getInstance();
                calendar.setTime(dateClicked);
                onDateChange(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Calendar calendar= Calendar.getInstance();
                calendar.setTime(firstDayOfNewMonth);

                onMonthChanged(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            }
        });



        Calendar calendar= Calendar.getInstance();
        ((TextView)view.findViewById(R.id.month_year_text)).setText(
                MONTHS[calendar.get(Calendar.MONTH)]+" "+String.valueOf(calendar.get(Calendar.YEAR)));

        addMarkers(((HomeActivity)getContext()).getLogedInUser().getLessonsEntries());

        return view;
    }

    public void addMarkers(ArrayList<LessonEntry> lessonEntries){
        Calendar time= Calendar.getInstance();


        for(int i=0; i<lessonEntries.size(); i++){
            time.setTime(lessonEntries.get(i).getLesson().getDateStart());

            calendarView.addEvent(new Event(getContext().getResources().getColor(R.color.colorAccent), time.getTimeInMillis()));
        }
    }

    public void onMonthChanged(int m, int y){
        ((TextView)getView().findViewById(R.id.month_year_text)).setText(MONTHS[m]+" "+String.valueOf(y));
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
