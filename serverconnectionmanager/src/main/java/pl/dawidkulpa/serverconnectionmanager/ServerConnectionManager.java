package pl.dawidkulpa.serverconnectionmanager;

import android.os.AsyncTask;
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

import javax.crypto.SecretKey;

public class ServerConnectionManager extends AsyncTask<String, Integer, Integer> {
    public interface OnFinishListener {
        void onFinish(int respCode, JSONObject jObject);
    }

    public enum OutputType {
        Json,
        Image,
        TextFile
    }

    private JSONObject jObj;
    private OnFinishListener onFinishListener;
    private Query postData;
    private Query.BuildType buildType;

    public ServerConnectionManager(OnFinishListener onFinishListener, Query.BuildType buildType){
        this.onFinishListener= onFinishListener;
        postData= new Query();
        this.buildType= buildType;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener= onFinishListener;
        postData= new Query();
    }

    public void addPOSTPair(String name, String v){
        postData.addPair(name, v);
    }

    public void addPOSTPair(String name, int v){
        postData.addPair(name, String.valueOf(v));
    }

    public void addPOSTPair(String name, double v){
        postData.addPair(name, String.valueOf(v));
    }

    public void addPOSTPair(String name, Query objDescr){
        postData.addPair(name, objDescr);
    }

    public Query getPOSTQuery(){
        return postData;
    }

    public boolean encryptPOSTQuery(SecretKey key){
        return postData.encryptValue(key);
    }

    public void start(String adr){
        this.execute(adr, postData.build(buildType));
    }

    @Override
    protected void onPreExecute() {
        jObj=null;
    }

    @Override
    protected Integer doInBackground(String... params) {
        int rCode=0;
        InputStream is;
        HttpURLConnection httpURLConnection;
        URL url;

        try {
            url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-type", "application/json-patch+json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            PrintWriter urlOut = new PrintWriter(httpURLConnection.getOutputStream());
            urlOut.print(params[1]);
            urlOut.close();

            httpURLConnection.connect();
            rCode=httpURLConnection.getResponseCode();

            is=httpURLConnection.getInputStream();
            jObj = JSONParser.getJSONFrmUrl(is);

            is.close();
            httpURLConnection.disconnect();
        } catch (IOException ioe){
            if(ioe.getMessage()!=null){
                Log.e("IOE", ioe.getLocalizedMessage());
            } else
                Log.e("IO", "nom");
        }

        return rCode;
    }

    @Override
    protected void onPostExecute(Integer rCode) {
        super.onPostExecute(rCode);

        Log.e("scm rCode", String.valueOf(rCode));

        if(onFinishListener !=null){
            onFinishListener.onFinish(rCode, jObj);
        }
    }

    public JSONObject getData(){
        return jObj;
    }
}
