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

import pl.dawidkulpa.knj.Lessons.LessonEntry;

public class NotifsListAdapter extends ArrayAdapter<LessonEntry> {
    private ArrayList<LessonEntry> data;
    private Context context;
    private ControlButtonsClickListener cbcl;

    public interface ControlButtonsClickListener{
        void onSendMessageClick(LessonEntry lessonEntry);
        void onConfirmLessonClick(LessonEntry lessonEntry);
        void onDeclineLessonClick(LessonEntry lessonEntry);
    }

    public NotifsListAdapter(@NonNull Context context, ArrayList<LessonEntry> data,
                             ControlButtonsClickListener cbcl){
        super(context, R.layout.list_item_notif);
        this.context= context;
        this.data= data;
        this.cbcl= cbcl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row= convertView;
        NotifyHolder notifyHolder=null;
        final LessonEntry obj= data.get(position);

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

        notifyHolder.titleText.setText(obj.getLesson().getSubject()+" - "+obj.getStatusName());

        if(obj.getRole()==LessonEntry.ROLE_STUDENT){

        } else {
            notifyHolder.sendMessageButton.setVisibility(View.VISIBLE);
            notifyHolder.confirmButton.setVisibility(View.GONE);
            notifyHolder.declineButton.setVisibility(View.GONE);

            notifyHolder.sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbcl.onSendMessageClick(obj);
                }
            });


            if(obj.getStatusName().equals("Czeka na potwierdzenie")){
                notifyHolder.confirmButton.setVisibility(View.VISIBLE);
                notifyHolder.declineButton.setVisibility(View.VISIBLE);

                notifyHolder.confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cbcl.onConfirmLessonClick(obj);
                    }
                });
                notifyHolder.declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cbcl.onDeclineLessonClick(obj);
                    }
                });
            }
        }

        if(obj.getRole()==LessonEntry.ROLE_STUDENT){
            notifyHolder.sendMessageButton.setVisibility(View.GONE);
            notifyHolder.confirmButton.setVisibility(View.GONE);
            notifyHolder.declineButton.setVisibility(View.GONE);

            notifyHolder.descriptionText.setText(obj.getLesson().getDateStartString());
        } else if(obj.getLesson().getStatusName().equals("Reserved")) {


            notifyHolder.descriptionText.setText(obj.getStudentName()+" "+obj.getStudentSName());
        } else {
            notifyHolder.sendMessageButton.setVisibility(View.VISIBLE);
            notifyHolder.confirmButton.setVisibility(View.GONE);
            notifyHolder.declineButton.setVisibility(View.GONE);

            notifyHolder.sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbcl.onSendMessageClick(obj);
                }
            });
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
