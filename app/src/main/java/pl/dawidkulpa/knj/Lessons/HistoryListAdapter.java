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

public class HistoryListAdapter extends ArrayAdapter<LessonEntry> {
    private ArrayList<LessonEntry> data;
    private Context context;
    StarButtonClickListener starButtonClickListener;

    public interface StarButtonClickListener{
        void onStarButtonClick(int i);
    }

    public HistoryListAdapter(@NonNull Context context, ArrayList<LessonEntry> data, StarButtonClickListener starButtonClickListener) {
        super(context, R.layout.list_item_history_lesson);
        this.context= context;
        this.data= data;
        this.starButtonClickListener= starButtonClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row= convertView;
        LessonHolder lessonHolder=null;
        LessonEntry obj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_history_lesson, null);

            lessonHolder= new LessonHolder();
            lessonHolder.starButtonsBox= row.findViewById(R.id.star_buttons_box);
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

        lessonHolder.starButtonsBox.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 1);
                starButtonClickListener.onStarButtonClick(0);
            }
        });

        lessonHolder.starButtonsBox.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 2);
                starButtonClickListener.onStarButtonClick(1);
            }
        });


        lessonHolder.starButtonsBox.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 3);
                starButtonClickListener.onStarButtonClick(2);
            }
        });


        lessonHolder.starButtonsBox.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 4);
                starButtonClickListener.onStarButtonClick(3);
            }
        });


        lessonHolder.starButtonsBox.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartsOn(v, 5);
                starButtonClickListener.onStarButtonClick(4);
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

        LinearLayout starButtonsBox;
    }
}
