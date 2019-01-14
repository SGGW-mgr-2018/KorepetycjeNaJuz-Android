package pl.dawidkulpa.knj.Lessons;

import java.util.ArrayList;
import java.util.Date;

public class LessonBuilder extends Lesson {
    public ArrayList<Integer> levelIds;
    public int subjectId;
    public double ratePH;
    public Date dateStart;
    public Date dateEnd;
    public String city;
    public String street;
    public String flat;
    public int time;
    public String description;

    public LessonBuilder(){
        levelIds= new ArrayList<>();
        description="a";
        time=1;
    }

}
