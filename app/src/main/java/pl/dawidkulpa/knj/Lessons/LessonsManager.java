package pl.dawidkulpa.knj.Lessons;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class LessonsManager {
    private Context context;

    private ArrayList<LessonMapMarker> onMapLessons;

    //Filters
    private double radiusFilter;
    private LatLng mapCenter;
    private Date today;
    private int timeFilter;
    private Date dateToFilter;
    private int subjectFilter;
    private int levelFilter;
    private int coachIdFilter;

    public LessonsManager(Context context) {
        this.context = context;
        this.onMapLessons= new ArrayList<>();
        subjectFilter=-1;
        levelFilter=-1;
        coachIdFilter=-1;
    }

    private void readMapPosition(GoogleMap map){
        double latLength;
        LatLngBounds latLngBounds;
        mapCenter= map.getCameraPosition().target;
        latLngBounds= map.getProjection().getVisibleRegion().latLngBounds;
        latLength= latLngBounds.northeast.latitude-latLngBounds.southwest.latitude;
        latLength= Math.abs(latLength);
        radiusFilter= ((latLength*110.574)/2);
    }

    public void refreshLessonMarkers(final GoogleMap map){
        readMapPosition(map);

        today= Calendar.getInstance().getTime();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);

        Calendar c=Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, timeFilter);
        dateToFilter= c.getTime();

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onRefreshMarkersResult(respCode, jObject, map);
            }
        }, Query.BuildType.Pairs);

        //Testowe
        scm.addPOSTPair("DateFrom", "2018-11-22T15:00:00");
        scm.addPOSTPair("DateTo", "2018-11-24T14:00:00");

        //Prawidlowe
        scm.addPOSTPair("Latitude", String.valueOf(mapCenter.latitude));
        scm.addPOSTPair("Longitude", String.valueOf(mapCenter.longitude));
        scm.addPOSTPair("Radius", String.valueOf(radiusFilter));
        //scm.addPOSTPair("DateFrom", sdf.format(today));
        //scm.addPOSTPair("DateTo", sdf.format(dateToFilter));

        if(subjectFilter>=0)
            scm.addPOSTPair("SubjectId", String.valueOf(subjectFilter));
        if(levelFilter>=0)
            scm.addPOSTPair("LevelId", String.valueOf(levelFilter));
        if(coachIdFilter>=0)
            scm.addPOSTPair("CoachId", String.valueOf(coachIdFilter));

        scm.setMethod(ServerConnectionManager.METHOD_GET);

        scm.start("https://korepetycjenajuzapi.azurewebsites.net/api/CoachLesson/CoachLessonsByFilters");

    }

    private void onRefreshMarkersResult(int rCode, JSONObject jObj, GoogleMap map){
        if(rCode==200){
            boolean[] occurMap= new boolean[onMapLessons.size()];

            try {
                JSONArray lessonsArray = jObj.getJSONArray("array");

                //Update or add lesson marker
                for(int i=0; i<lessonsArray.length(); i++){
                    boolean occured=false;
                    JSONObject lessonJObj= lessonsArray.getJSONObject(i);

                    //Search for occurrence
                    for(int j=0; i<onMapLessons.size(); j++){
                        if(onMapLessons.get(j).equals(lessonJObj)){
                            occurMap[j]=true;
                            occured= true;
                            onMapLessons.get(j).update(lessonJObj);
                            break;
                        }
                    }

                    //If absent -> add
                    if(!occured){
                        onMapLessons.add(LessonMapMarker.create(lessonJObj));
                        onMapLessons.get(onMapLessons.size()-1).register(context, map);
                    }
                }
            } catch (JSONException jsonE){
                Log.e("Refresh Map", jsonE.getMessage());
            }

            //Remove absent elements
            for(int i=0; i<occurMap.length; i++){
                if(!occurMap[i]){
                    onMapLessons.get(i).unregister();
                    onMapLessons.remove(i);
                }
            }

            Log.d("Lesson Manager", "Map refreshed with "+String.valueOf(onMapLessons.size())+" markers");
            Log.d("Lesson Manager", "Filters:");
            Log.d("Lesson Manager", "Map center: "+String.valueOf(mapCenter.latitude)+" / "+String.valueOf(mapCenter.longitude));
            Log.d("Lesson Manager", "Radius: "+String.valueOf(radiusFilter));
        } else {
            Log.d("Lesson Manager", "No lessons found");
            for(int i=0; i<onMapLessons.size(); i++){
                onMapLessons.get(i).unregister();
                onMapLessons.remove(i);
            }
        }
    }

    public void updateFilters(View dialogView){
        RadioGroup levelsGroup=dialogView.findViewById(R.id.levels_radio_group);
        RadioGroup subjectsGroup= dialogView.findViewById(R.id.subjects_radio_group);
        RadioButton checkedLevel= levelsGroup.findViewById(levelsGroup.getCheckedRadioButtonId());
        RadioButton checkedSubject= subjectsGroup.findViewById(subjectsGroup.getCheckedRadioButtonId());

        int level_id= levelsGroup.indexOfChild(checkedLevel);
        int subject_id= subjectsGroup.indexOfChild(checkedSubject);


        if(level_id>0)
            levelFilter= level_id-1;
        else
            levelFilter= -1;

        if(subject_id>0)
            subjectFilter=subject_id;
        else
            subjectFilter= -1;
    }

    public int getTimeFilter() {
        return timeFilter;
    }

    public Date getDateToFilter() {
        return dateToFilter;
    }

    public int getSubjectFilter() {
        return subjectFilter;
    }

    public int getLevelFilter() {
        return levelFilter;
    }

    public int getCoachIdFilter() {
        return coachIdFilter;
    }
}
