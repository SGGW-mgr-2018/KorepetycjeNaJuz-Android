package pl.dawidkulpa.serverconnectionmanager;

import android.util.Log;

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
    public static JSONObject getJSONFrmUrl(String urlAddress, String query){
        InputStream is;
        JSONObject jObject=null;
        String json;
        URL url;
        HttpURLConnection httpURLConnection;

        try{
            url= new URL(urlAddress);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            PrintWriter urlOut= new PrintWriter(httpURLConnection.getOutputStream());
            urlOut.print(query);
            urlOut.close();

            httpURLConnection.connect();
            is= httpURLConnection.getInputStream();
            BufferedReader reader= new BufferedReader(new InputStreamReader(is));
            StringBuilder sb= new StringBuilder();
            String line;

            while((line=reader.readLine())!=null){
                Log.d("JSONParser", "line: "+line);
                sb.append(line+"n");
            }
            is.close();
            httpURLConnection.disconnect();
            json=sb.toString();

            jObject=new JSONObject(json);

        } catch (JSONException me){
            if(me.getMessage()!=null)
                Log.e("JSONParser", me.getMessage());
            else
                Log.e("JSONParser", "nom");
        } catch (IOException ioe){
            if(ioe.getMessage()!=null){
                Log.e("IO", ioe.getLocalizedMessage());
            } else
                Log.e("IO", "nom");
        }

        return jObject;
    }
}
