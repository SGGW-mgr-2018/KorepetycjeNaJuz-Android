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

public class LessonsListAdapter extends ArrayAdapter<Lesson> {
    private ArrayList<Lesson> data;
    private Context context;

    public LessonsListAdapter(@NonNull Context context, ArrayList<Lesson> data) {
        super(context, R.layout.list_item_day_lesson);
        this.context= context;
        this.data= data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row= convertView;
        LessonHolder lessonHolder=null;

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_day_lesson, null);

            lessonHolder= new LessonHolder();
            lessonHolder.titleText= row.findViewById(R.id.title_text);
            lessonHolder.descrText= row.findViewById(R.id.descr_text);

            row.setTag(lessonHolder);
        }else {
            lessonHolder= (LessonHolder) row.getTag();
        }

        String title= data.get(position).getSubject()+", liceum, ";
        //Time sTime= data.get(position).getStartTime();
        //Time eTime= new Time(sTime.getTime() + data.get(position).getLenMin()*60*1000);
        //title+= sTime.getHours()+":"+sTime.getMinutes()+" - ";
        //title+= eTime.getHours()+":"+eTime.getMinutes();

        lessonHolder.titleText.setText(title);
        lessonHolder.descrText.setText("Description");

        return row;
    }

    public void notifyIsSelected(int pos){

    }

    @Nullable
    @Override
    public Lesson getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class LessonHolder{
        TextView titleText;
        TextView descrText;
    }
}
