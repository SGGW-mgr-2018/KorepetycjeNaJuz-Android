package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.Lessons.LessonBuilder;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class CreateLessonActivity extends AppCompatActivity {

    private static final int STEPS_NO=3;

    private int step;
    private FragmentManager fragmentManager;
    private ArrayList<CLFragment> appFragments;
    private LessonBuilder lessonBuilder;
    private ArrayAdapter<String> subjectsAdapter;

    private String userLoginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);

        fragmentManager= getSupportFragmentManager();

        lessonBuilder= new LessonBuilder();

        Bundle extras= getIntent().getExtras();
        String[] subjectLabels= extras.getStringArray("subjects");
        userLoginToken= extras.getString("userLoginToken");
        subjectsAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        for(int i=0; i<subjectLabels.length; i++){
            subjectsAdapter.add(subjectLabels[i]);
        }

        appFragments= new ArrayList<>();
        appFragments.add(CLSubjectFragment.newInstance());
        ((CLSubjectFragment)appFragments.get(0)).setSubjectsAdapter(subjectsAdapter);
        appFragments.add(CLDateFragment.newInstance());
        appFragments.add(CLAddressFragment.newInstance());

        for(int i=0; i<appFragments.size(); i++){
            appFragments.get(i).setLessonObj(lessonBuilder);
        }

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
        //appFragments.get(id).putOnView();
    }

    public void onPosButtonClick(View v){

        String checkResp= appFragments.get(step).checkProperties();
        if(!checkResp.equals("OK")){
            Toast.makeText(this, checkResp, Toast.LENGTH_SHORT).show();
        } else {
            appFragments.get(step).gatherData();

            if (step < STEPS_NO - 1) {
                step++;

                switchFragment(step, 1);

                if (step == STEPS_NO - 1)
                    ((Button) findViewById(R.id.pos_button)).setText(R.string.button_create);
            } else {
                createLessonStart();
            }
        }
    }

    public void createLessonStart(){
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

        addressDTO.addPair("city", lessonBuilder.city);
        addressDTO.addPair("street", lessonBuilder.street);

        ArrayList<String> levelsStrId= new ArrayList<>();
        for(int i=0; i<lessonBuilder.levelIds.size(); i++){
            levelsStrId.add(String.valueOf(lessonBuilder.levelIds.get(i)));
        }

        List<Address> addresses=null;
        Geocoder geocoder= new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocationName(lessonBuilder.city + ", " + lessonBuilder.street, 2);
        } catch (IOException ioE){
            Log.e("CreateLessonActivity", ioE.getMessage());
        }

        if(addresses!=null && !addresses.isEmpty()){
            addressDTO.addPair("latitude", String.valueOf(addresses.get(0).getLatitude()));
            addressDTO.addPair("longitude", String.valueOf(addresses.get(0).getLongitude()));

            Log.e("CreateLessonActivity", "Found at: "+addresses.get(0).getLatitude()+" / "+addresses.get(0).getLongitude());

            coachLessonDTO.addPair("lessonLevels", levelsStrId);
            coachLessonDTO.addPair("lessonSubjectId", String.valueOf(lessonBuilder.subjectId));
            coachLessonDTO.addPair("ratePerHour", String.valueOf(lessonBuilder.ratePH));
            coachLessonDTO.addPair("description", lessonBuilder.description);

            //"2018-12-06T02:07:33.592Z" <- Date time format
            SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            coachLessonDTO.addPair("dateStart", df.format(lessonBuilder.dateStart));
            coachLessonDTO.addPair("dateEnd", df.format(lessonBuilder.dateEnd));
            coachLessonDTO.addPair("time", String.valueOf(lessonBuilder.time));

            coachLessonDTO.addPair("address", addressDTO);

            scm.addPOSTPair("", coachLessonDTO);
            scm.addHeaderEntry("Authorization", "Bearer "+userLoginToken);
            scm.start(HomeActivity.SERVER_NAME+"/CoachLesson/Create");
        } else {
            Toast.makeText(this, "Wrong address passed. Correct lesson address",Toast.LENGTH_SHORT).show();
        }

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
