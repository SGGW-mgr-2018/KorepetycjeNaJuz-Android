package pl.dawidkulpa.knj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class NotifsListAdapter extends BaseAdapter {
    public static final int TYPE_ITEM=0;
    public static final int TYPE_SEPARATOR=1;

    private ArrayList<Integer> itemTypes;
    private ArrayList<NotifEntry> data;
    private LayoutInflater inflater;

    public NotifsListAdapter(Context context){
        inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        return itemTypes.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public NotifEntry getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView sepText=null;
        CheckBox notifCheck=null;
        int type = getItemViewType(position);

        if (convertView == null) {
            switch (type) {
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.list_item_notif, null);
                    notifCheck= convertView.findViewById(R.id.notif_checkbox);
                    convertView.setTag(notifCheck);
                    break;
                case TYPE_SEPARATOR:
                    convertView = inflater.inflate(R.layout.list_item_day_sep, null);
                    sepText= convertView.findViewById(R.id.day_separator_text);
                    convertView.setTag(sepText);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_ITEM:
                    notifCheck= (CheckBox)convertView.getTag();
                    break;
                case TYPE_SEPARATOR:
                    sepText= (TextView)convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case TYPE_ITEM:
                notifCheck.setText("");
                break;
            case TYPE_SEPARATOR:
                sepText.setText("");
                break;
        }

        return convertView;
    }

    static class ItemViewHolder {
        public CheckBox checkBox;
    }

    static class SeparatorViewHolder{
        public TextView textView;
    }

}
