package pl.dawidkulpa.knj.Dialogs.DateTimePicker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.dawidkulpa.knj.R;

public class DatePickerFragment extends Fragment {

    private int y;
    private int m;
    private int d;


    public DatePickerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DatePickerFragment newInstance() {
        DatePickerFragment fragment = new DatePickerFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_dp_date, container, false);

        ((DatePicker)rootView.findViewById(R.id.date_picker)).init(
                y,
                m,
                d,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        y= year;
                        m=monthOfYear;
                        d=dayOfMonth;
                    }
                }
        );

        // Inflate the layout for this fragment
        return rootView;
    }

    public void setYear(int y) {
        this.y = y;
    }

    public void setMonth(int m) {
        this.m = m;
    }

    public void setDay(int d) {
        this.d = d;
    }

    public int getYear(){
        return y;
    }

    public int getMonth(){
        return m;
    }

    public int getDay(){
        return d;
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
