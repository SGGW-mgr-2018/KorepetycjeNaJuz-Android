package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pl.dawidkulpa.knj.Lessons.CoachLesson;
import pl.dawidkulpa.knj.R;

public class CLAddressFragment extends CLFragment {
    public CLAddressFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CLAddressFragment newInstance() {
        CLAddressFragment fragment = new CLAddressFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cl_address, container, false);
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

    @Override
    public void putOnView(CoachLesson coachLesson) {
        if(getView()!=null){
            //((EditText)getView().findViewById(R.id.city_edit)).setText(coachLesson.city);
            //((EditText)getView().findViewById(R.id.street_edit)).setText(coachLesson.street);
        }
    }

    @Override
    public boolean getherData(CoachLesson coachLesson) {
        if(getView()!=null){
            //coachLesson.city= ((EditText)getView().findViewById(R.id.city_edit)).getText().toString();
            //coachLesson.street= ((EditText)getView().findViewById(R.id.street_edit)).getText().toString();

            return true;
        } else {
            return false;
        }
    }
}
