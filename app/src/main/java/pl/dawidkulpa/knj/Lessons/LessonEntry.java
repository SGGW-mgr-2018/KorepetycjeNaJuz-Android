package pl.dawidkulpa.knj.Lessons;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LessonEntry{
    public static final int ROLE_STUDENT=1;
    public static final int ROLE_COACH=2;

    private Lesson lesson;
    private int status;
    private int role;

    private int lessonId;
    private int studentId;
    private String studentName;
    private String studentSName;
    private String studentPhone;
    private int nuberOfHours;

    public static ArrayList<LessonEntry> create(JSONObject jObject){
        ArrayList<LessonEntry> lessonEntries= new ArrayList<>();
        Lesson lesson;

        try{
            lesson= Lesson.create(jObject);

            if(!jObject.isNull("myLesson")){
                lessonEntries.add(new LessonEntry());
                lessonEntries.get(0).lesson= lesson;
                lessonEntries.get(0).role= jObject.getInt("userRole");

                JSONObject jMyLesson= jObject.getJSONObject("myLesson");

                lessonEntries.get(0).nuberOfHours= jMyLesson.optInt("numberOfHours", 1);
                lessonEntries.get(0).status= jMyLesson.getInt("lessonStatusId");
                lessonEntries.get(0).lessonId= jMyLesson.getInt("id");
            }

            if(!jObject.isNull("lessons")){
                JSONArray jArray= jObject.getJSONArray("lessons");
                JSONObject jLesson;
                JSONObject jStudent;

                for(int i=0; i<jArray.length(); i++){
                    jLesson= jArray.getJSONObject(i);
                    jStudent= jLesson.getJSONObject("student");

                    lessonEntries.add(new LessonEntry());
                    lessonEntries.get(lessonEntries.size()-1).lesson= lesson;
                    lessonEntries.get(lessonEntries.size()-1).role= LessonEntry.ROLE_COACH;
                    lessonEntries.get(lessonEntries.size()-1).status= jLesson.getInt("lessonStatusId");

                    lessonEntries.get(lessonEntries.size()-1).studentId= jStudent.getInt("id");
                    lessonEntries.get(lessonEntries.size()-1).studentName= jStudent.getString("firstName");
                    lessonEntries.get(lessonEntries.size()-1).studentSName= jStudent.getString("lastName");
                    lessonEntries.get(lessonEntries.size()-1).studentPhone= jStudent.getString("telephone");
                    lessonEntries.get(lessonEntries.size()-1).nuberOfHours= jLesson.optInt("numberOfHours", 1);
                    lessonEntries.get(lessonEntries.size()-1).lessonId= jLesson.getInt("id");
                }
            }

        } catch (JSONException je){
            Log.e("LessonEntry", je.getLocalizedMessage());
            lessonEntries= null;
        }

        return lessonEntries;
    }

    public int getId(){
        return lessonId;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public int getStatus() {
        return status;
    }

    public int getRole() {
        return role;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentSName() {
        return studentSName;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public int getLessonLength(){
        return nuberOfHours;
    }
}
