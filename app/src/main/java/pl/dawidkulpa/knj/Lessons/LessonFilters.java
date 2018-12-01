package pl.dawidkulpa.knj.Lessons;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class LessonFilters {
    public double radius;
    public LatLng mapCenter;
    public Date dateFrom;
    public Date dateTo;
    public int subject;
    public int level;
    public int coachId;

    public LessonFilters() {
        this.subject = -1;
        this.level = -1;
        this.coachId = -1;
    }

    public boolean isSubjectDefined(){
        return subject!=-1;
    }
    public boolean isLevelDefined(){
        return level!=-1;
    }
    public boolean isCoachDefined(){
        return level!=-1;
    }
    public boolean isDateFromDefined(){
        return dateFrom!=null;
    }
    public boolean isDateToDefined(){
        return dateTo!=null;
    }
}
