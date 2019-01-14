package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pl.dawidkulpa.knj.Lessons.CoachLesson;
import pl.dawidkulpa.knj.Lessons.Lesson;
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
        View rootView= inflater.inflate(R.layout.fragment_cl_address, container, false);

        ((EditText)rootView.findViewById(R.id.city_edit)).setText(lessonBuilder.city);
        ((EditText)rootView.findViewById(R.id.street_edit)).setText(lessonBuilder.street);
        ((EditText)rootView.findViewById(R.id.flat_edit)).setText(lessonBuilder.flat);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public boolean gatherData() {
        if(getView()!=null){
            lessonBuilder.city= ((EditText)getView().findViewById(R.id.city_edit)).getText().toString();
            lessonBuilder.street= ((EditText)getView().findViewById(R.id.street_edit)).getText().toString();
            lessonBuilder.flat= ((EditText)getView().findViewById(R.id.flat_edit)).getText().toString();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String checkProperties() {

        if(((EditText)getView().findViewById(R.id.city_edit)).getText().toString().isEmpty()){
            return "Pass city";
        }

        if(((EditText)getView().findViewById(R.id.street_edit)).getText().toString().isEmpty()){
            return "Pass street";
        }

        return "OK";
    }
}
