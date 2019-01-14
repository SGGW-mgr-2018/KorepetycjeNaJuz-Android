package pl.dawidkulpa.knj.Dialogs.DateTimePicker;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import pl.dawidkulpa.knj.R;

public class DateTimeActivity extends AppCompatActivity {

    public static final int DATE_TIME_RESCODE=1;

    private Fragment datePickerFragment;
    private Fragment timePickerFragment;
    private FragmentManager fragmentManager;

    private int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        datePickerFragment= DatePickerFragment.newInstance();
        timePickerFragment= TimePickerFragment.newInstance();

        step=0;

        Intent intent= getIntent();

        int y=intent.getIntExtra("year", 2019);
        int m=intent.getIntExtra("month", 1);
        int d=intent.getIntExtra("day", 1);
        ((DatePickerFragment)datePickerFragment).setYear(y);
        ((DatePickerFragment)datePickerFragment).setMonth(m);
        ((DatePickerFragment)datePickerFragment).setDay(d);

        int h=intent.getIntExtra("hour", 12);
        int min=intent.getIntExtra("minutes", 0);
        ((TimePickerFragment)timePickerFragment).setHours(h);
        ((TimePickerFragment)timePickerFragment).setMinutes(min);

        fragmentManager= getSupportFragmentManager();
        onDateButtonClick(null);
    }

    public void onTimeButtonClick(View v){
        if(step==1){
            FragmentTransaction transaction= fragmentManager.beginTransaction();

            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.fragment_container, timePickerFragment);
            transaction.addToBackStack(null);

            transaction.commit();
            step=0;
        }
    }

    public void onDateButtonClick(View v){
        if(step==0){
            FragmentTransaction transaction= fragmentManager.beginTransaction();

            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.fragment_container, datePickerFragment);
            transaction.addToBackStack(null);

            transaction.commit();
            step=1;
        }
    }

    public void onCancelButtonClick(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onSelectButtonClick(View v){
        Intent result= new Intent();

        result.putExtra("year", ((DatePickerFragment)datePickerFragment).getYear());
        result.putExtra("month", ((DatePickerFragment)datePickerFragment).getMonth());
        result.putExtra("day", ((DatePickerFragment)datePickerFragment).getDay());

        result.putExtra("hour", ((TimePickerFragment)timePickerFragment).getHours());
        result.putExtra("minutes", ((TimePickerFragment)timePickerFragment).getMinutes());

        setResult(Activity.RESULT_OK, result);
        finish();
    }

}
