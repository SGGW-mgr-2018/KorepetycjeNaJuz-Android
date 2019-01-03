package pl.dawidkulpa.knj.Lessons;

import java.util.ArrayList;
import java.util.Date;

public class CoachLesson {
    public int myId;
    public ArrayList<Integer> levels;
    public int subjectId;
    public int rate;
    public Date dateFrom;
    public Date dateTo;
    public String city;
    public String street;
    public double lat;
    public double lng;
    public String time;

    public CoachLesson() {
        levels= new ArrayList<>();
    }
}
