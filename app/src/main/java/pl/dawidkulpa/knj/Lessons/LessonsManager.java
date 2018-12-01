package pl.dawidkulpa.knj.Lessons;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class LessonsManager {
    private Context context;

    private ArrayList<LessonMapMarker> onMapLessons;

    //LessonFilters
    private LessonFilters filters;

    public LessonsManager(Context context) {
        this.context = context;
        this.onMapLessons= new ArrayList<>();
        filters= new LessonFilters();
    }

    private void readMapPosition(GoogleMap map){
        double latLength;
        LatLngBounds latLngBounds;
        filters.mapCenter= map.getCameraPosition().target;
        latLngBounds= map.getProjection().getVisibleRegion().latLngBounds;
        latLength= latLngBounds.northeast.latitude-latLngBounds.southwest.latitude;
        latLength= Math.abs(latLength);
        filters.radius= ((latLength*110.574)/2);
    }

    public void refreshLessonMarkers(final GoogleMap map){
        readMapPosition(map);

        filters.dateFrom= Calendar.getInstance().getTime();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onRefreshMarkersResult(respCode, jObject, map);
            }
        }, Query.BuildType.Pairs);

        //Prawidlowe
        scm.addPOSTPair("Latitude", String.valueOf(filters.mapCenter.latitude));
        scm.addPOSTPair("Longitude", String.valueOf(filters.mapCenter.longitude));
        scm.addPOSTPair("Radius", String.valueOf(filters.radius));


        if(filters.isDateFromDefined())
            scm.addPOSTPair("DateFrom", sdf.format(filters.dateFrom));
        if(filters.isDateToDefined())
            scm.addPOSTPair("DateTo", sdf.format(filters.dateTo));


        if(filters.isSubjectDefined())
            scm.addPOSTPair("SubjectId", String.valueOf(filters.subject));
        if(filters.isLevelDefined())
            scm.addPOSTPair("LevelId", String.valueOf(filters.level));
        if(filters.isCoachDefined())
            scm.addPOSTPair("CoachId", String.valueOf(filters.coachId));

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

            //Log.d("Lesson Manager", "Map refreshed with "+String.valueOf(onMapLessons.size())+" markers");
            //Log.d("Lesson Manager", "LessonFilters:");
            //Log.d("Lesson Manager", "Map center: "+String.valueOf(mapCenter.latitude)+" / "+String.valueOf(mapCenter.longitude));
            //Log.d("Lesson Manager", "Radius: "+String.valueOf(radiusFilter));
        } else {
            //Log.d("Lesson Manager", "No lessons found");
            for(int i=0; i<onMapLessons.size(); i++){
                onMapLessons.get(i).unregister();
                onMapLessons.remove(i);
            }
        }
    }

    public void updateFilters(LessonFilters filters){
        this.filters.level= filters.level;
        this.filters.subject= filters.subject;
        this.filters.dateFrom= filters.dateFrom;
        this.filters.dateTo= filters.dateTo;
        this.filters.coachId= filters.coachId;
    }

    public LessonFilters getFilters(){
        return filters;
    }
}
