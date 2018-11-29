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

    public static final String CONTENTTYPE_TEXT="application/text";
    public static final String CONTENTTYPE_JSONPATCH="application/json-patch+json";
    public static final String CONTENTTYPE_JSON="application/json";

    public static final String METHOD_POST="POST";
    public static final String METHOD_GET="GET";
    public static final String METHOD_PUT="PUT";

    public static final String OUTTYPE_TEXT="text";
    public static final String OUTTYPE_JSON="json";

    private JSONObject jObj;
    private OnFinishListener onFinishListener;
    private Query postData;
    private Query.BuildType buildType;
    private String contentType;
    private String method;
    private String outtype;

    public ServerConnectionManager(OnFinishListener onFinishListener, Query.BuildType buildType){
        this.onFinishListener= onFinishListener;
        postData= new Query();
        this.buildType= buildType;
        method=METHOD_POST;
        contentType="";
        outtype=OUTTYPE_JSON;
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


    public void setContentType(String contentType){
        this.contentType= contentType;
    }

    public void setMethod(String method){
        this.method= method;
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
            if(method.equals(METHOD_GET))
                url= new URL(params[0]+"?"+params[1]);
            else
                url = new URL(params[0]);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);

            if(!contentType.isEmpty())
                httpURLConnection.setRequestProperty("Content-type", contentType);

            httpURLConnection.setDoInput(true);

            if(!method.equals(METHOD_GET)) {
                httpURLConnection.setDoOutput(true);
                PrintWriter urlOut = new PrintWriter(httpURLConnection.getOutputStream());
                urlOut.print(params[1]);
                urlOut.close();
            }

            httpURLConnection.connect();
            rCode=httpURLConnection.getResponseCode();

            if(rCode>=300)
                is=httpURLConnection.getErrorStream();
            else
                is=httpURLConnection.getInputStream();

            if(outtype.equals(OUTTYPE_JSON))
                jObj = JSONParser.getJSONFrmUrl(is);
            else {
                jObj= new JSONObject();
                jObj.put("response", getRawText());
            }

            is.close();
            httpURLConnection.disconnect();
        } catch (IOException ioe){
            Log.e("IOE", ioe.getLocalizedMessage());
            rCode=999;
        } catch (JSONException jsonE){
            Log.e("JSONE", jsonE.getMessage());
            rCode=998;
        }

        return rCode;
    }

    @Override
    protected void onPostExecute(Integer rCode) {
        super.onPostExecute(rCode);

        if(onFinishListener !=null){
            onFinishListener.onFinish(rCode, jObj);
        }
    }

    public JSONObject getData(){
        return jObj;
    }

    private String getRawText(){
        return "No msg!";
    }
}
