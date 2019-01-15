package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pl.dawidkulpa.knj.R;

public class CLLength extends CLFragment {

    public CLLength() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CLLength newInstance() {
        CLLength fragment = new CLLength();
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
        View rootView= inflater.inflate(R.layout.fragment_cl_length, container, false);

        if(lessonBuilder.ratePH>0)
            ((EditText)rootView.findViewById(R.id.rate_edit)).setText(String.valueOf(lessonBuilder.ratePH));

        if(lessonBuilder.time>0){
            ((EditText)rootView.findViewById(R.id.time_edit)).setText(String.valueOf(lessonBuilder.time));
        }

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
            EditText rateEdit= getView().findViewById(R.id.rate_edit);
            EditText timeEdit= getView().findViewById(R.id.time_edit);

            lessonBuilder.time= Integer.valueOf(timeEdit.getText().toString());
            lessonBuilder.ratePH= Double.valueOf(rateEdit.getText().toString());

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String checkProperties() {
        EditText rateEdit= getView().findViewById(R.id.rate_edit);
        EditText timeEdit= getView().findViewById(R.id.time_edit);

        if(timeEdit.getText().toString().isEmpty())
            return getString(R.string.info_no_time);

        int time= Integer.valueOf(timeEdit.getText().toString());
        if(time<30 || time>180){
            return getString(R.string.info_wrong_time);
        }

        if(rateEdit.getText().toString().isEmpty())
            return  getString(R.string.info_no_rate);

        return "OK";
    }
}
