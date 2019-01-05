package pl.dawidkulpa.knj.Lessons;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pl.dawidkulpa.knj.Address;

public class Lesson {
    private int id;
    private int coachId;
    private String coachName;
    private String statusName;
    private String levels[];
    private int subjectId;
    private String subject;
    private double ratePH;
    private Date dateStart;
    private Date dateEnd;
    private Address address;
    private int time;

    public static Lesson create(JSONObject jObj){
        Lesson lesson= new Lesson();

        try {
            lesson.id= jObj.getInt("id");
            lesson.coachId= jObj.getInt("coachId");
            lesson.coachName= jObj.getString("coachFirstName")+" "+jObj.getString("coachLastName");
            lesson.statusName= jObj.getString("lessonStatusName");

            JSONArray levelArray= jObj.getJSONArray("lessonLevels");
            lesson.levels= new String[levelArray.length()];
            for(int i=0; i<levelArray.length(); i++){
                lesson.levels[i]= levelArray.getJSONObject(i).getString("name");
            }

            lesson.subjectId = jObj.getInt("lessonSubjectId");
            lesson.subject= jObj.getString("lessonSubject");
            lesson.ratePH= jObj.getDouble("ratePerHour");

            //Get date

            lesson.address= Address.create(jObj.getJSONObject("address"));
            lesson.time= jObj.getInt("time");
        } catch (JSONException je){
            Log.e("Lesson parser", je.getMessage());
            lesson= null;
        }

        return lesson;
    }

    public boolean equals(JSONObject jObj){
        boolean r=false;

        try{
            if(this.id==jObj.getInt("id"))
                r=true;
        } catch (JSONException je){
            Log.e("Lesson", je.getMessage());
        }

        return r;
    }



    public String getSubject() {
        return "Matematyka";
    }

    public double getLat(){
        return address.getLat();
    }

    public double getLng(){
        return address.getLng();
    }

    public int getId() {
        return id;
    }

    public int getCoachId() {
        return coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public String getStatusName() {
        return statusName;
    }

    public String[] getLevels() {
        return levels;
    }

    public String getLevelsAsOne(){
        String levelsStr="";

        for(int i=0; i<levels.length; i++){
            levelsStr+= levels[i];
            if(i!=levels.length-1){
                levelsStr+=", ";
            }
        }

        return levelsStr;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public double getRatePH() {
        return ratePH;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public int getTime() {
        return time;
    }
}
