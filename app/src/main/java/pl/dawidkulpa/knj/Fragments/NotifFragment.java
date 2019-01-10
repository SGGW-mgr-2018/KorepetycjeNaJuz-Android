package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.NotifsListAdapter;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;

public class NotifFragment extends Fragment {

    private NotifsListAdapter notifsListAdapter;
    private User user;


    public NotifFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NotifFragment newInstance() {
        NotifFragment fragment = new NotifFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_notif, container, false);

        user= ((HomeActivity)getContext()).getLogedInUser();

        notifsListAdapter= new NotifsListAdapter(getContext(), user.getLessonsEntries());
        ListView listView= rootView.findViewById(R.id.notifs_list);
        listView.setAdapter(notifsListAdapter);
        notifsListAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //TODO: Download all notifs
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
