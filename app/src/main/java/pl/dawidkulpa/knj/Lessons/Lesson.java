package pl.dawidkulpa.knj.Lessons;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import pl.dawidkulpa.knj.Address;

public class Lesson {
    private int id;
    private int coachId;
    private String coachName;
    private int coachRating;
    private String statusName;
    private String levels[];
    private int subjectId;
    private String subject;
    private double ratePH;
    private Date dateStart;
    private Date dateEnd;
    private Address address;
    private int time;
    private String description;

    public static Lesson create(JSONObject jObj){
        Lesson lesson= new Lesson();
        int role;

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

            lesson.dateStart= parseDate(jObj.getString("dateStart"));
            lesson.dateEnd= parseDate(jObj.getString("dateEnd"));

            lesson.address= Address.create(jObj.getJSONObject("address"));
            lesson.time= jObj.getInt("time");

            Random r= new Random();
            lesson.coachRating= jObj.optInt("", r.nextInt(5)+1);
            lesson.description= jObj.getString("description");
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
        return subject;
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

    public String getAddressString(){
        return address.toString();
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

    public int getCoachRating(){
        return coachRating;
    }

    public String getDescription(){
        return description;
    }

    public String getDateStartString(){
        DateFormat df = new SimpleDateFormat("HH:mm   dd/MM/yyyy");

        return df.format(dateStart);
    }

    public String getDateEndString(){
        DateFormat df = new SimpleDateFormat("HH:mm   dd/MM/yyyy");

        return df.format(dateEnd);
    }

    public boolean isAtDay(int y, int m, int d){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(dateStart);

        return (calendar.get(Calendar.DAY_OF_MONTH)==d) &&
                (calendar.get(Calendar.MONTH)==m) &&
                (calendar.get(Calendar.YEAR)==y);
    }

    public boolean isOlder(Date than){
        return dateStart.before(than);
    }

    public String getTimeStartString(){
        DateFormat df= new SimpleDateFormat("HH:mm");
        return df.format(dateStart);
    }
    public String getTimeEndString(){
        DateFormat df= new SimpleDateFormat("HH:mm");
        return df.format(dateEnd);
    }

    public static Date parseDate(String strDate){
        Calendar calendar= Calendar.getInstance();
        String[] timeDateSplit= strDate.split("T");
        String[] timeSplit= timeDateSplit[1].split(":");
        String[] dateSplit= timeDateSplit[0].split("-");

        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateSplit[2]));
        calendar.set(Calendar.MONTH, Integer.valueOf(dateSplit[1]));
        calendar.set(Calendar.YEAR, Integer.valueOf(dateSplit[0]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeSplit[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(timeSplit[1]));

        return new Date(calendar.getTimeInMillis());
    }
}