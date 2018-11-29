package pl.dawidkulpa.serverconnectionmanager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONParser {
    public static JSONObject getJSONFrmUrl(InputStream is){
        JSONObject jObject=null;
        String json;

        try{
            BufferedReader reader= new BufferedReader(new InputStreamReader(is));
            StringBuilder sb= new StringBuilder();
            String line;

            while((line=reader.readLine())!=null){
                Log.d("JSONParser", "line: "+line);
                sb.append(line+"n");
            }

            json=sb.toString();

            if(!json.isEmpty()){
                if(json.charAt(0)=='[') {
                    jObject= new JSONObject();
                    jObject.put("array", new JSONArray(json));
                } else {
                    jObject = new JSONObject(json);
                }
            }
        } catch (JSONException me){
            if(me.getMessage()!=null)
                Log.e("JSONParser", me.getMessage());
            else
                Log.e("JSONParser", "nom");
        } catch (IOException ioe){
            if(ioe.getMessage()!=null){
                Log.e("IOE", ioe.getLocalizedMessage());
            } else
                Log.e("IO", "nom");
        }

        return jObject;
    }
}
