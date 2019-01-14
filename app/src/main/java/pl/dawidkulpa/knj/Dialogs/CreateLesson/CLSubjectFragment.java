package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Lessons.CoachLesson;
import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.R;

public class CLSubjectFragment extends CLFragment {

    private ArrayAdapter<String> subjectsAdapter;
    private ArrayList<String> levelsList;

    public CLSubjectFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CLSubjectFragment newInstance() {
        CLSubjectFragment fragment = new CLSubjectFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_cl_subject, container, false);

        Spinner subjectSpinner= rootView.findViewById(R.id.subjects_spinner);
        subjectSpinner.setAdapter(subjectsAdapter);

        if(lessonBuilder.ratePH>0)
            ((EditText)rootView.findViewById(R.id.rate_edit)).setText(String.valueOf(lessonBuilder.ratePH));

        subjectSpinner.setSelection(lessonBuilder.subjectId);
        LinearLayout levels_box = rootView.findViewById(R.id.levels_box);

        for(int i=0; i<lessonBuilder.levelIds.size(); i++){
           ((CheckBox)levels_box.getChildAt(lessonBuilder.levelIds.get(i))).setChecked(true);
        }
        Log.e("CLSubjectFragment", "onCreateView");

        // Inflate the layout for this fragment
        return rootView;
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

    public void setSubjectsAdapter(ArrayAdapter<String> subjectsAdapter){
        this.subjectsAdapter= subjectsAdapter;
    }

    public void setLevelsList(ArrayList<String> levelsList){
        this.levelsList= levelsList;
    }

    public boolean gatherData(){
        if(getView()!=null) {
            EditText rateEdit = getView().findViewById(R.id.rate_edit);
            Spinner subjectSpinner = getView().findViewById(R.id.subjects_spinner);
            LinearLayout levels_box = getView().findViewById(R.id.levels_box);

            lessonBuilder.ratePH= Double.valueOf(rateEdit.getText().toString());
            lessonBuilder.subjectId= subjectSpinner.getSelectedItemPosition() + 1;

            lessonBuilder.levelIds.clear();
            for (int i = 1; i < levels_box.getChildCount(); i++) {
                if (((CheckBox)levels_box.getChildAt(i)).isChecked()) {
                    lessonBuilder.levelIds.add(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String checkProperties() {
        EditText rateEdit = getView().findViewById(R.id.rate_edit);
        Spinner subjectSpinner = getView().findViewById(R.id.subjects_spinner);
        LinearLayout levels_box = getView().findViewById(R.id.levels_box);

        //Check levels
        int checkedNo=0;
        for (int i = 1; i < levels_box.getChildCount(); i++) {
            if (((CheckBox)levels_box.getChildAt(i)).isChecked()) {
                checkedNo++;
            }
        }
        if(checkedNo==0){
            return "Select at least on lesson level";
        }

        //Check rate
        if(rateEdit.getText().toString().isEmpty()){
            return "Select rate per hour";
        }


        return "OK";
    }
}
