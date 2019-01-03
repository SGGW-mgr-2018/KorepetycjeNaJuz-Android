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

    private Date date;

    private int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        datePickerFragment= DatePickerFragment.newInstance();
        timePickerFragment= TimePickerFragment.newInstance();

        step=0;

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
        finish();
    }

    public void onSelectButtonClick(View v){
        Intent result= new Intent();
        result.putExtra("Date", date.getTime());

        setResult(Activity.RESULT_OK, result);
    }



}