package pl.dawidkulpa.knj.Messages;

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

public class ConversationsListAdapter extends ArrayAdapter<Conversation> {

    public interface ItemClickListener{
        void onItemClickListener(int withId, String withName);
    }

    private ArrayList<Conversation> data;
    private Context context;
    private ItemClickListener itemClickListener;

    public ConversationsListAdapter(@NonNull Context context, ArrayList<Conversation> data, ItemClickListener itemClickListener){
        super(context, R.layout.list_item_conversation);
        this.context= context;
        this.data= data;
        this.itemClickListener= itemClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row= convertView;
        ConversationHolder conversationHolder=null;
        final Conversation obj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.list_item_conversation, null);

            conversationHolder= new ConversationHolder();
            conversationHolder.withText= row.findViewById(R.id.with_text);
            conversationHolder.lastMsgDateText= row.findViewById(R.id.last_msg_date_text);
            conversationHolder.lastMsgText= row.findViewById(R.id.last_msg_text);

            row.setTag(conversationHolder);
        }else {
            conversationHolder= (ConversationHolder) row.getTag();
        }

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClickListener(obj.getWithId(), obj.getWithName());
            }
        });

        conversationHolder.withText.setText(obj.getWithName());
        conversationHolder.lastMsgDateText.setText(obj.getLastMsg().getDate());
        conversationHolder.lastMsgText.setText(obj.getLastMsg().getText());

        return row;
    }

    @Nullable
    @Override
    public Conversation getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class ConversationHolder{
        TextView withText;
        TextView lastMsgDateText;
        TextView lastMsgText;
    }
}
