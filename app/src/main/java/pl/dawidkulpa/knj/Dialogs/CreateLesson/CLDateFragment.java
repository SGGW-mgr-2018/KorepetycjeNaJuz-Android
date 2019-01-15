package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.app.Activity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.dawidkulpa.knj.Lessons.CoachLesson;
import pl.dawidkulpa.knj.Dialogs.DateTimePicker.DateTimeActivity;
import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.R;

public class CLDateFragment extends CLFragment {
    public CLDateFragment() {
        // Required empty public constructor
    }

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
                onDateButtonClick(view);
            }
        });

        rootView.findViewById(R.id.date_to_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateButtonClick(view);
            }
        });

        Date from= lessonBuilder.dateStart;
        Date to= lessonBuilder.dateEnd;
        SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date today= Calendar.getInstance().getTime();

        if(from!=null){
            ((Button)rootView.findViewById(R.id.date_from_button)).setText(df.format(from));
        } else {
            ((Button)rootView.findViewById(R.id.date_from_button)).setText(df.format(today));
        }

        if(to!=null) {
            ((Button) rootView.findViewById(R.id.date_to_button)).setText(df.format(to));
        } else {
            ((Button)rootView.findViewById(R.id.date_to_button)).setText(df.format(today));
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onDateButtonClick(View v){
        Intent intent= new Intent(getContext(), DateTimeActivity.class);
        SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            Date date = df.parse(((Button)v).getText().toString());
            Calendar calendar= Calendar.getInstance();
            calendar.setTime(date);

            intent.putExtra("year", calendar.get(Calendar.YEAR));
            intent.putExtra("month", calendar.get(Calendar.MONTH));
            intent.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));

            intent.putExtra("hour", calendar.get(Calendar.HOUR_OF_DAY));
            intent.putExtra("minutes", calendar.get(Calendar.MINUTE));

            if(v.getId()==R.id.date_from_button)
                startActivityForResult(intent, 1);
            else
                startActivityForResult(intent, 2);
        } catch (ParseException pe){
            Log.e("CLDateFragment", pe.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==Activity.RESULT_OK) {

            int y = data.getIntExtra("year", 0);
            int m = data.getIntExtra("month", 0);
            int d = data.getIntExtra("day", 0);
            int hh = data.getIntExtra("hour", 0);
            int mm = data.getIntExtra("minutes", 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, d);
            calendar.set(Calendar.MONTH, m);
            calendar.set(Calendar.YEAR, y);
            calendar.set(Calendar.HOUR_OF_DAY, hh);
            calendar.set(Calendar.MINUTE, mm);

            SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateStr = df.format(calendar.getTime());


            if (requestCode == 1) {
                ((Button) getView().findViewById(R.id.date_from_button)).setText(dateStr);
            } else if (requestCode == 2) {
                ((Button) getView().findViewById(R.id.date_to_button)).setText(dateStr);
            }
        }
    }

    //"2018-12-06T02:07:33Z" <- Date time format

    @Override
    public boolean gatherData() {
        if(getView()!=null){
            Date dateFrom= convertTextToDate((Button)getView().findViewById(R.id.date_from_button));
            Date dateTo= convertTextToDate((Button)getView().findViewById(R.id.date_to_button));

            if(dateFrom!=null && dateTo!=null){
                lessonBuilder.dateStart= dateFrom;
                lessonBuilder.dateEnd= dateTo;
            }

            return true;
        } else {
            return false;
        }
    }

    public Date convertTextToDate(Button button){
        Date date= null;

        String dateStr= button.getText().toString();
        SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try{
            date= df.parse(dateStr);
        } catch (ParseException pe){
            Log.e("CLDateFragment", pe.getMessage());
        }

        return date;
    }

    @Override
    public String checkProperties() {
        Date dateFrom= convertTextToDate((Button)getView().findViewById(R.id.date_from_button));
        Date dateTo= convertTextToDate((Button)getView().findViewById(R.id.date_to_button));

        if(dateFrom!=null && dateTo!=null){
            if(dateFrom.after(dateTo))
                return getString(R.string.info_wrond_date);
        }

        return "OK";
    }
}
