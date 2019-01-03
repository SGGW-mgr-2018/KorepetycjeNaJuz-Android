package pl.dawidkulpa.knj.Dialogs.DateTimePicker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import pl.dawidkulpa.knj.R;

public class TimePickerFragment extends Fragment {
    public TimePickerFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TimePickerFragment newInstance() {
        TimePickerFragment fragment = new TimePickerFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_dp_time, container, false);

        ((TimePicker)rootView.findViewById(R.id.time_picker)).setIs24HourView(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dp_time, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof OnFragmentInteractionListener) {
        //   mListener = (OnFragmentInteractionListener) context;
        //} else {
        //throw new RuntimeException(context.toString()
        //          + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
