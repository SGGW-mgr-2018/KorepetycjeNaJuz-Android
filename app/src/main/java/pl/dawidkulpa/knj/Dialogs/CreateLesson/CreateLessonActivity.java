package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import org.json.JSONObject;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Lessons.CoachLesson;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class CreateLessonActivity extends AppCompatActivity {

    private static final int STEPS_NO=3;

    private int step;
    private FragmentManager fragmentManager;

    private ArrayList<CLFragment> appFragments;

    private CoachLesson coachLesson;

    private ArrayAdapter<String> subjectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);

        fragmentManager= getSupportFragmentManager();

        coachLesson= new CoachLesson();

        Bundle extras= getIntent().getExtras();
        String[] subjectLabels= extras.getStringArray("subjects");
        subjectsAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        for(int i=0; i<subjectLabels.length; i++){
            subjectsAdapter.add(subjectLabels[i]);
        }

        appFragments= new ArrayList<>();
        appFragments.add(CLSubjectFragment.newInstance());
        ((CLSubjectFragment)appFragments.get(0)).setSubjectsAdapter(subjectsAdapter);
        appFragments.add(CLDateFragment.newInstance());
        appFragments.add(CLAddressFragment.newInstance());

        step=0;
        switchFragment(step, 0);
    }

    public void onNegButtonClick(View v){
        finish();
    }

    private void switchFragment(int id, int dir){
        FragmentTransaction transaction= fragmentManager.beginTransaction();

        if(dir==-1)
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        else if(dir==1)
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);


        transaction.replace(R.id.fragment_container, appFragments.get(id));
        transaction.addToBackStack(null);

        transaction.commit();
        appFragments.get(id).putOnView(coachLesson);
    }

    public void onPosButtonClick(View v){

        appFragments.get(step).getherData(coachLesson);

        if(step<STEPS_NO-1){
            step++;

            switchFragment(step, 1);

            if(step==STEPS_NO-1)
                ((Button)findViewById(R.id.pos_button)).setText(R.string.button_create);
        } else {
            onCreateLessonStart();
        }
    }

    public void onCreateLessonStart(){
        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onCreateLessonFinished(respCode, jObject);
            }
        }, Query.BuildType.JSONPatch);

        scm.setMethod(ServerConnectionManager.METHOD_POST);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);

        Query coachLessonDTO= new Query();
        Query addressDTO= new Query();

        ArrayList<String> levels= new ArrayList<>();
        for(int i=0; i<coachLesson.levels.size(); i++){
            levels.add(String.valueOf(coachLesson.levels.get(i)));
        }

        addressDTO.addPair("latitude", "0");
        addressDTO.addPair("longitude", "0");
        addressDTO.addPair("city", coachLesson.city);
        addressDTO.addPair("street", coachLesson.street);

        coachLessonDTO.addPair("coachId", String.valueOf(coachLesson.myId));
        coachLessonDTO.addPair("lessonLevels", levels);
        coachLessonDTO.addPair("lessonSubjectId", String.valueOf(coachLesson.subjectId));
        coachLessonDTO.addPair("ratePerHour", String.valueOf(coachLesson.rate));
        //"2018-12-06T02:07:33.592Z" <- Date time format
        //coachLessonDTO.addPair("dateStart", coachLesson.dateFrom);
        //coachLessonDTO.addPair("dateEnd", coachLesson.dateTo);
        coachLessonDTO.addPair("time", coachLesson.time);
        coachLessonDTO.addPair("address", addressDTO);

        scm.addPOSTPair("", coachLessonDTO);
        scm.start("https://korepetycjenajuzapi.azurewebsites.net/api/coachLesson/Create");
    }

    public void onCreateLessonFinished(int rCode, JSONObject jObj){
        Log.e("CreateLessonActivity", String.valueOf(rCode));

        if(rCode==201){
            finish();
        }
    }

    public void onBackButtonClick(View v){
        if(step>0){
            step--;

            ((Button)findViewById(R.id.pos_button)).setText(R.string.button_next);
            switchFragment(step, -1);
        }
    }

}
