package pl.dawidkulpa.knj;

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

import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.Lessons.LessonEntry;

public class NotifsListAdapter extends ArrayAdapter<LessonEntry> {
    private ArrayList<LessonEntry> data;
    private Context context;

    public NotifsListAdapter(@NonNull Context context, ArrayList<LessonEntry> data){
        super(context, R.layout.list_item_notif);
        this.context= context;
        this.data= data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row= convertView;
        NotifyHolder notifyHolder=null;
        LessonEntry obj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_notif, null);

            notifyHolder= new NotifyHolder();
            notifyHolder.titleText= row.findViewById(R.id.title_text);
            notifyHolder.descriptionText= row.findViewById(R.id.description_text);
            notifyHolder.sendMessageButton= row.findViewById(R.id.send_message_button);
            notifyHolder.confirmButton= row.findViewById(R.id.confirm_button);
            notifyHolder.declineButton= row.findViewById(R.id.decline_button);

            row.setTag(notifyHolder);
        }else {
            notifyHolder= (NotifyHolder) row.getTag();
        }

        notifyHolder.titleText.setText(obj.getLesson().getSubject()+" - "+obj.getLesson().getStatusName());


        if(obj.getRole()==LessonEntry.ROLE_STUDENT){
            notifyHolder.sendMessageButton.setVisibility(View.GONE);
            notifyHolder.confirmButton.setVisibility(View.GONE);
            notifyHolder.declineButton.setVisibility(View.GONE);
            notifyHolder.descriptionText.setText(obj.getLesson().getDateStartString());
        } else {
            notifyHolder.sendMessageButton.setVisibility(View.VISIBLE);
            notifyHolder.confirmButton.setVisibility(View.VISIBLE);
            notifyHolder.declineButton.setVisibility(View.VISIBLE);
            notifyHolder.descriptionText.setText(obj.getStudentName()+" "+obj.getStudentSName());
        }

        return row;
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

    static class NotifyHolder{
        TextView titleText;
        TextView descriptionText;
        TextView sendMessageButton;
        TextView confirmButton;
        TextView declineButton;
    }
}
