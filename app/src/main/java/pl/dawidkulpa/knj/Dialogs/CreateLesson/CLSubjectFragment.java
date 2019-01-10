package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.content.Context;
import android.os.Bundle;
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

        ((Spinner)rootView.findViewById(R.id.subjects_spinner)).setAdapter(subjectsAdapter);

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

    @Override
    public void putOnView(CoachLesson coachLesson) {
        if(getView()!=null){
            //((EditText)getView().findViewById(R.id.rate_edit)).setText(String.valueOf(coachLesson.rate));
            Spinner subjectSpinner= getView().findViewById(R.id.subjects_spinner);
            //subjectSpinner.setSelection(coachLesson.subjectId);
            LinearLayout levels_box = getView().findViewById(R.id.levels_box);
            ((CheckBox)levels_box.getChildAt(2)).setChecked(true);

            //for(int i=0; i<coachLesson.levels.size(); i++){
             //   ((CheckBox)levels_box.getChildAt(2)).setChecked(true);
            //}
        }
    }

    public boolean getherData(CoachLesson coachLesson){
        if(getView()!=null) {
            EditText rateEdit = getView().findViewById(R.id.rate_edit);
            Spinner subjectSpinner = getView().findViewById(R.id.subjects_spinner);
            LinearLayout levels_box = getView().findViewById(R.id.levels_box);

            //coachLesson.rate = Integer.valueOf(rateEdit.getText().toString());
            //coachLesson.subjectId = subjectSpinner.getSelectedItemPosition() + 1;
            //coachLesson.levels.clear();

            for (int i = 1; i < levels_box.getChildCount(); i++) {
                if (((CheckBox)levels_box.getChildAt(i)).isChecked()) {
                    //coachLesson.levels.add(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
