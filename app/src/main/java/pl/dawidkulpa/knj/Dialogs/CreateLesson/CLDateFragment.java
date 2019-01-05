package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import pl.dawidkulpa.knj.Lessons.CoachLesson;
import pl.dawidkulpa.knj.Dialogs.DateTimePicker.DateTimeActivity;
import pl.dawidkulpa.knj.R;

public class CLDateFragment extends CLFragment {
    public CLDateFragment() {
        // Required empty public constructor
    }

    private long dateFrom;
    private long dateTo;

    // TODO: Rename and change types and number of parameters
    public static CLDateFragment newInstance() {
        CLDateFragment fragment = new CLDateFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_cl_date, container, false);

        rootView.findViewById(R.id.date_from_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateFromButtonClick(view);
            }
        });

        rootView.findViewById(R.id.date_to_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateToButtonClick(view);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public void onDateFromButtonClick(View v){
        Intent intent= new Intent(getContext(), DateTimeActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onDateToButtonClick(View v){
        Intent intent= new Intent(getContext(), DateTimeActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Result", String.valueOf(requestCode));

        int y= data.getIntExtra("year", 0);
        int m= data.getIntExtra("month", 0);
        int d= data.getIntExtra("day", 0);
        int hh= data.getIntExtra("hour", 0);
        int mm= data.getIntExtra("minutes",0);

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, d);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        String dateStr= d+"/"+m+"/"+y+" "+hh+":";
        if(mm<10)
            dateStr+="0"+mm;
        else
            dateStr+=mm;


        if(requestCode==1){
            dateFrom= calendar.getTimeInMillis();
            ((Button)getView().findViewById(R.id.date_from_button)).setText(dateStr);
        } else if(requestCode==2){
            dateTo= calendar.getTimeInMillis();
            ((Button)getView().findViewById(R.id.date_to_button)).setText(dateStr);
        }
    }

    private void onDateSelectResult(final View v, final int d, final int m, final int y){
        Calendar calendar= Calendar.getInstance();

        TimePickerDialog timePickerDialog= new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                onTimeSelectResult(v, d, m, y, i, i1);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void onTimeSelectResult(View v, int d, int M, int y, int h, int m){
        String date= d+"/"+M+"/"+y+" - "+h+":";
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, d);
        calendar.set(Calendar.MONTH, M);
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);

        if(v.getId()==R.id.date_from_edit){
            dateFrom= calendar.getTimeInMillis();
        } else {
            dateTo= calendar.getTimeInMillis();
        }

        if(m<10)
            date+="0"+m;
        else
            date+=m;

        ((EditText)v).setText(date);
    }

    //"2018-12-06T02:07:33Z" <- Date time format


    @Override
    public void putOnView(CoachLesson coachLesson) {
        if(getView()!=null) {
            ((Button)getView().findViewById(R.id.date_from_button)).setText("asd");
            ((Button)getView().findViewById(R.id.date_to_button)).setText("asd");
        }
    }

    @Override
    public boolean getherData(CoachLesson coachLesson) {
        if(getView()!=null){

            coachLesson.dateFrom= new Date(dateFrom);
            coachLesson.dateTo= new Date(dateTo);

            return true;
        } else {
            return false;
        }
    }
}
