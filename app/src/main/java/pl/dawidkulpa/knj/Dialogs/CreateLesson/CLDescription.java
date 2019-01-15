package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pl.dawidkulpa.knj.R;

public class CLDescription extends CLFragment {
    public CLDescription() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CLDescription newInstance() {
        CLDescription fragment = new CLDescription();
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
        View rootView= inflater.inflate(R.layout.fragment_cl_description, container, false);

        ((EditText)rootView.findViewById(R.id.description_edit)).setText(lessonBuilder.getDescription());

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


    public boolean gatherData(){
        if(getView()!=null) {

            lessonBuilder.description=
                    ((EditText)getView().findViewById(R.id.description_edit)).getText().toString();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String checkProperties() {

        return "OK";
    }
}
