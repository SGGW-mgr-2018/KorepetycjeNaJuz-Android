package pl.dawidkulpa.serverconnectionmanager;

import android.os.AsyncTask;

import org.json.JSONObject;

import javax.crypto.SecretKey;

public class ServerConnectionManager extends AsyncTask<String, Integer, ServerConnectionManager.ConnectionState> {
    public interface OnFinishListener {
        void onFinish(ConnectionState connectionState, JSONObject jObject);
    }

    public enum ConnectionState {
        None,
        InProgress,
        Ok,
        Error
    }

    public enum OutputType {
        Json,
        Image,
        TextFile
    }

    private JSONObject jObj;
    private ConnectionState resp;
    private OnFinishListener onFinishListener;
    private Query postData;

    public ServerConnectionManager(OnFinishListener onFinishListener){
        resp= ConnectionState.None;
        this.onFinishListener= onFinishListener;
        postData= new Query();
    }

    public void setOnFinishListener(OnFinishListener onFinishListener){
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

    public Query getPOSTQuery(){
        return postData;
    }

    public boolean encryptPOSTQuery(SecretKey key){
        return postData.encryptValue(key);
    }

    public void start(String adr){
        this.execute(adr, postData.build());
    }

    @Override
    protected void onPreExecute() {
        jObj=null;
        resp= ConnectionState.InProgress;
    }

    @Override
    protected ConnectionState doInBackground(String... params) {
        jObj= JSONParser.getJSONFrmUrl(params[0], params[1]);

        if(jObj!=null){
            resp= ConnectionState.Ok;
        } else {
            resp= ConnectionState.Error;
        }

        return resp;
    }

    @Override
    protected void onPostExecute(ConnectionState connectionState) {
        super.onPostExecute(connectionState);
        if(onFinishListener !=null){
            onFinishListener.onFinish(resp, jObj);
        }
    }

    public ConnectionState getConnectionState(){
        return resp;
    }

    public JSONObject getData(){
        return jObj;
    }
}
