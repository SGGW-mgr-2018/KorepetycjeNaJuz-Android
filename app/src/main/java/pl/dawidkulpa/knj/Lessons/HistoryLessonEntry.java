package pl.dawidkulpa.knj.Lessons;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryLessonEntry {
    private int id;
    private String subjectName;
    private String coachName;
    private String coachSName;
    private Date dateStart;
    private int time;
    private int coachRating;

    public static HistoryLessonEntry create(JSONObject jObj){
        HistoryLessonEntry hle= new HistoryLessonEntry();

        try{
            hle.id= jObj.getInt("id");
            hle.subjectName= jObj.getString("subjectName");
            hle.coachName= jObj.getString("coachFirstName");
            hle.coachSName= jObj.getString("coachLastName");
            hle.dateStart= Lesson.parseDate(jObj.getString("dateStart"));
            hle.time= jObj.getInt("time");
            hle.coachRating= jObj.getInt("ratingOfCoach");
        } catch (JSONException je){
            Log.e("HistoryLessonEntry", je.getMessage());
            hle= null;
        }

        return hle;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subjectName;
    }

    public String getCoachName() {
        return coachName;
    }

    public String getCoachSName() {
        return coachSName;
    }

    public int getTime() {
        return time;
    }

    public int getCoachRating() {
        return coachRating;
    }

    public String getDateStartString(){
        DateFormat df = new SimpleDateFormat("HH:mm   dd/MM/yyyy");

        return df.format(dateStart);
    }
}
