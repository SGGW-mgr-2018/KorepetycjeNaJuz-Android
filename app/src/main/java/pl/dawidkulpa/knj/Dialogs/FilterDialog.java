package pl.dawidkulpa.knj.Dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.dawidkulpa.knj.Lessons.LessonFilters;
import pl.dawidkulpa.knj.R;

public class FilterDialog {
    private Context context;
    private OnClickListener onClickListener;
    private View dialogView;

    public interface OnClickListener{
        void onPositiveClick(LessonFilters filters);
        void onNegativeClick();
    }

    public FilterDialog(Context context, OnClickListener onClickListener){
        this.context= context;
        this.onClickListener= onClickListener;
    }

    public void show(ArrayAdapter<CharSequence> levelsAdapter,
                     ArrayAdapter<CharSequence> subjectsAdapter,
                     LessonFilters filters){

        AlertDialog.Builder adbuilder= new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        adbuilder.setTitle("Filtry");
        adbuilder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onPositiveClick(getherFilters());
            }
        });
        adbuilder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onNegativeClick();
            }
        });

        dialogView= inflater.inflate(R.layout.dialog_filters, null);
        EditText dateFromEdit= dialogView.findViewById(R.id.date_from_edit);
        EditText dateToEdit= dialogView.findViewById(R.id.date_to_edit);

        dateFromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateSelectClick(view);
            }
        });
        dateToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateSelectClick(view);
            }
        });

        //Create subject radio group
        Spinner subjectsSpinner= dialogView.findViewById(R.id.subjects_spinner);
        subjectsSpinner.setAdapter(subjectsAdapter);
        Spinner levelsSpinner= dialogView.findViewById(R.id.levels_spinner);
        levelsSpinner.setAdapter(levelsAdapter);

        //Set last selected filer in radio group (Level)
        if(filters.isLevelDefined()){
            levelsSpinner.setSelection(filters.level+1);
        } else {
            levelsSpinner.setSelection(0);
        }

        //Set last selected filer in radio group (Subject)
        if(filters.isSubjectDefined()){
            subjectsSpinner.setSelection(filters.subject);
        } else {
            subjectsSpinner.setSelection(0);
        }

        adbuilder.setView(dialogView);

        adbuilder.create().show();


    }

    private void onDateSelectClick(final View v){
        DatePickerDialog datePickerDialog= new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                onDateSelectResult(v, i2, i1, i);
            }
        });

        datePickerDialog.show();
    }

    private LessonFilters getherFilters(){
        LessonFilters filters= new LessonFilters();

        Spinner levelsSpinner= dialogView.findViewById(R.id.levels_spinner);
        Spinner subjectsSpinner= dialogView.findViewById(R.id.subjects_spinner);

        int level_id= levelsSpinner.getSelectedItemPosition();
        int subject_id= subjectsSpinner.getSelectedItemPosition();

        if(level_id>0)
            filters.level= level_id-1;
        else
            filters.level= -1;

        if(subject_id>0)
            filters.subject=subject_id;
        else
            filters.subject= -1;

        return filters;
    }

    private void onDateSelectResult(final View v, final int d, final int m, final int y){
        Calendar calendar= Calendar.getInstance();

        TimePickerDialog timePickerDialog= new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                onTimeSelectResult(v, d, m, y, i, i1);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void onTimeSelectResult(View v, int d, int M, int y, int h, int m){
        String date= d+"/"+M+"/"+y+" - "+h+":"+m;
        ((EditText)v).setText(date);
    }
}
