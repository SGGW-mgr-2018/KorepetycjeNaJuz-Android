package pl.dawidkulpa.knj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LessonMapMarker {
    private Marker googleMarker;
    private GoogleMap map;
    private Context context;

    private double lat;
    private double lng;

    private Lesson lesson;

    public LessonMapMarker(double lat, double lng){
        this.lat= lat;
        this.lng= lng;
    }

    public void register(Context context, GoogleMap map){
        BitmapDrawable bitmapdraw= (BitmapDrawable)context.getResources().getDrawable(R.drawable.pin2);
        Bitmap icon = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), 200, 200, false);


        googleMarker= map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .anchor(0.5f, 1.0f)
                .icon(BitmapDescriptorFactory.fromBitmap(icon)));


        this.map= map;
        this.context= context;
    }
}
