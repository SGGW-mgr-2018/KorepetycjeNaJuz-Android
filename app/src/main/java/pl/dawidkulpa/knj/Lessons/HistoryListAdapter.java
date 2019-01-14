package pl.dawidkulpa.knj.Lessons;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.dawidkulpa.knj.R;

public class HistoryListAdapter extends ArrayAdapter<HistoryLessonEntry> {
    private ArrayList<HistoryLessonEntry> data;
    private Context context;
    StarButtonClickListener starButtonClickListener;

    public interface StarButtonClickListener{
        void onStarButtonClick(int id, int rating);
    }

    public HistoryListAdapter(@NonNull Context context, ArrayList<HistoryLessonEntry> data, StarButtonClickListener starButtonClickListener) {
        super(context, R.layout.list_item_history_lesson);
        this.context= context;
        this.data= data;
        this.starButtonClickListener= starButtonClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row= convertView;
        LessonHolder lessonHolder=null;
        final HistoryLessonEntry obj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_history_lesson, null);

            lessonHolder= new LessonHolder();
            lessonHolder.starButtonsBox= row.findViewById(R.id.star_buttons_box);
            lessonHolder.titleText= row.findViewById(R.id.title_text);
            lessonHolder.coachText= row.findViewById(R.id.coach_text);
            lessonHolder.dateText= row.findViewById(R.id.date_text);
            lessonHolder.lengthText= row.findViewById(R.id.length_text);

            row.setTag(lessonHolder);
        }else {
            lessonHolder= (LessonHolder) row.getTag();
        }

        String title= obj.getSubject();
        lessonHolder.titleText.setText(title);
        lessonHolder.coachText.setText(obj.getCoachName()+" "+obj.getCoachSName());
        lessonHolder.dateText.setText(obj.getDateStartString());

        if(obj.getTime()==1){
            lessonHolder.lengthText.setText(obj.getTime()+" godzina");
        } else if (obj.getTime()<5) {
            lessonHolder.lengthText.setText(obj.getTime()+" godziny");
        } else {
            lessonHolder.lengthText.setText(obj.getTime()+" godzin");
        }

        for(int i=0; i<obj.getCoachRating(); i++){
            lessonHolder.starButtonsBox.getChildAt(i).setBackground(context.getDrawable(R.drawable.ripple_full_star));
        }
        for(int i=obj.getCoachRating(); i<lessonHolder.starButtonsBox.getChildCount(); i++){
            lessonHolder.starButtonsBox.getChildAt(i).setBackground(context.getDrawable(R.drawable.ripple_empty_star));
        }

        lessonHolder.starButtonsBox.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 1);
                starButtonClickListener.onStarButtonClick(obj.getId(), 0);
            }
        });

        lessonHolder.starButtonsBox.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 2);
                starButtonClickListener.onStarButtonClick(obj.getId(), 1);
            }
        });


        lessonHolder.starButtonsBox.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 3);
                starButtonClickListener.onStarButtonClick(obj.getId(), 2);
            }
        });


        lessonHolder.starButtonsBox.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 4);
                starButtonClickListener.onStarButtonClick(obj.getId(), 3);
            }
        });

        lessonHolder.starButtonsBox.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 5);
                starButtonClickListener.onStarButtonClick(obj.getId(), 4);
            }
        });


        return row;
    }

    private void setStartsOn(View v, int no){
        LinearLayout rootView= (LinearLayout)v.getParent();

        for(int i=0; i<no; i++){
            rootView.getChildAt(i).setBackground(context.getDrawable(R.drawable.ripple_full_star));
        }

        for(int i=no; i<rootView.getChildCount(); i++){
            rootView.getChildAt(i).setBackground(context.getDrawable(R.drawable.ripple_empty_star));
        }
    }


    @Nullable
    @Override
    public HistoryLessonEntry getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class LessonHolder{
        TextView titleText;
        TextView coachText;
        TextView dateText;
        TextView lengthText;

        LinearLayout starButtonsBox;
    }
}
