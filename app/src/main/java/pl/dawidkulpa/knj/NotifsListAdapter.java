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

public class NotifsListAdapter extends ArrayAdapter<NotifyEntry> {
    private ArrayList<NotifyEntry> data;
    private Context context;

    public NotifsListAdapter(@NonNull Context context, ArrayList<NotifyEntry> data){
        super(context, R.layout.list_item_notif);
        this.context= context;
        this.data= data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row= convertView;
        NotifyHolder notifyHolder=null;
        NotifyEntry obj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_notif, null);

            notifyHolder= new NotifyHolder();
            notifyHolder.titleText= row.findViewById(R.id.title_text);
            notifyHolder.contentText= row.findViewById(R.id.content_text);

            row.setTag(notifyHolder);
        }else {
            notifyHolder= (NotifyHolder) row.getTag();
        }

        notifyHolder.titleText.setText(obj.getTitle());
        notifyHolder.contentText.setText(obj.getContent());

        return row;
    }

    @Nullable
    @Override
    public NotifyEntry getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class NotifyHolder{
        TextView titleText;
        TextView contentText;
    }
}
