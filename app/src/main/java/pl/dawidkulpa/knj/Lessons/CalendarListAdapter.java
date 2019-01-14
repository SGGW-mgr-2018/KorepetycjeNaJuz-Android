package pl.dawidkulpa.knj.Lessons;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.dawidkulpa.knj.R;

public class CalendarListAdapter extends ArrayAdapter<LessonEntry> {
    private ArrayList<LessonEntry> data;
    private Context context;

    public CalendarListAdapter(@NonNull Context context, ArrayList<LessonEntry> data) {
        super(context, R.layout.list_item_day_lesson);
        this.context= context;
        this.data= data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row= convertView;
        LessonHolder lessonHolder=null;
        LessonEntry obj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_day_lesson, null);

            lessonHolder= new LessonHolder();
            lessonHolder.titleText= row.findViewById(R.id.title_text);
            lessonHolder.addressText= row.findViewById(R.id.address_text);
            lessonHolder.dateText= row.findViewById(R.id.date_text);
            lessonHolder.lengthText= row.findViewById(R.id.length_text);

            row.setTag(lessonHolder);
        }else {
            lessonHolder= (LessonHolder) row.getTag();
        }

        String title= obj.getLesson().getSubject()+", "+obj.getLesson().getLevelsAsOne();
        lessonHolder.titleText.setText(title);
        lessonHolder.addressText.setText(obj.getLesson().getAddressString());
        lessonHolder.dateText.setText(obj.getLesson().getTimeStartString());

        if(obj.getLessonLength()==1){
            lessonHolder.lengthText.setText(obj.getLessonLength()+" godzina");
        } else if (obj.getLessonLength()<5) {
            lessonHolder.lengthText.setText(obj.getLessonLength()+" godziny");
        } else {
            lessonHolder.lengthText.setText(obj.getLessonLength()+" godzin");
        }


        return row;
    }

    public void notifyIsSelected(int pos){

    }

    @Nullable
    @Override
    public LessonEntry getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class LessonHolder{
        TextView titleText;
        TextView addressText;
        TextView dateText;
        TextView lengthText;
    }
}
