package pl.dawidkulpa.knj.Lessons;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SubjectDefinition {
    private int id;
    private String name;

    public static SubjectDefinition create(JSONObject jObj){
        SubjectDefinition lessonDefinition= new SubjectDefinition();

        try{
            lessonDefinition.id= jObj.getInt("id");
            lessonDefinition.name= jObj.getString("name");
        } catch (JSONException je){
            lessonDefinition= null;
            Log.e("Lesson Definition", je.getMessage());
        }

        return lessonDefinition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
