package pl.dawidkulpa.knj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class LessonMapMarker {
    private Marker googleMarker;
    private GoogleMap map;
    private Context context;

    private Lesson lesson;

    public static LessonMapMarker create(JSONObject jObj){
        LessonMapMarker lessonMapMarker= new LessonMapMarker();

        lessonMapMarker.lesson= Lesson.create(jObj);
        if(lessonMapMarker.lesson==null)
            return null;

        return lessonMapMarker;
    }

    public boolean equals(JSONObject jObj){
        return lesson.equals(jObj);
    }

    public void register(Context context, GoogleMap map){
        BitmapDrawable bitmapdraw= (BitmapDrawable)context.getResources().getDrawable(R.drawable.pin2);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 200, 200, false);


        googleMarker= map.addMarker(new MarkerOptions()
                .position(new LatLng(lesson.getLat(), lesson.getLng()))
                .anchor(0.5f, 1.0f)
                .icon(BitmapDescriptorFactory.fromBitmap(icon)));


        this.map= map;
        this.context= context;
    }

    public void unregister(){
        googleMarker.remove();
    }

    public void update(JSONObject jObj){

    }
}
