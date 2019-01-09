package pl.dawidkulpa.knj.Messages;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.dawidkulpa.knj.R;

public class MessagesListAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> data;
    private Context context;
    private int withId;

    public MessagesListAdapter(@NonNull Context context, ArrayList<Message> data, int withId){
        super(context, R.layout.list_item_message);
        this.context= context;
        this.data= data;
        this.withId= withId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row= convertView;
        MessageHolder messageHolder=null;
        Message obj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_message, null);

            messageHolder= new MessageHolder();
            messageHolder.text= row.findViewById(R.id.text);

            row.setTag(messageHolder);
        }else {
            messageHolder= (MessageHolder) row.getTag();
        }

        messageHolder.text.setText(obj.getText());

        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(withId!=obj.getRecipient()){
            messageHolder.text.setBackground(context.getDrawable(R.drawable.background_his_message));
            layoutParams.gravity= Gravity.START;
        } else {
            messageHolder.text.setBackground(context.getDrawable(R.drawable.background_my_message));
            layoutParams.gravity=Gravity.END;
        }

        messageHolder.text.setLayoutParams(layoutParams);

        return row;
    }

    @Nullable
    @Override
    public Message getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class MessageHolder{
        TextView text;
    }
}
