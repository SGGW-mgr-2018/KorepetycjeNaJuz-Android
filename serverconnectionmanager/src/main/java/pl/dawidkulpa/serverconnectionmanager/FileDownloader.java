package pl.dawidkulpa.serverconnectionmanager;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader extends AsyncTask<String, Integer, FileDownloader.ConnectionState> {
    public interface OnFinishListener {
        void onFinish(ConnectionState connectionState, JSONObject jObject);
    }

    public enum ConnectionState {
        None,
        InProgress,
        Ok,
        Error
    }

    private JSONObject jObj;
    private ConnectionState resp;
    private OnFinishListener onFinishListener;
    private Query postData;

    public FileDownloader(OnFinishListener onFinishListener){
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

    public void start(String adr, String writeDir){
        this.execute(writeDir, adr, postData.build(Query.BuildType.Pairs));
    }

    @Override
    protected void onPreExecute() {
        jObj=null;
        resp= ConnectionState.InProgress;
    }

    @Override
    protected ConnectionState doInBackground(String... params) {
        String path= params[0];

        try {
            resp= ConnectionState.Ok;
            URL url = new URL(params[1]);

            URLConnection ucon = url.openConnection();
            ucon.setReadTimeout(5000);
            ucon.setConnectTimeout(10000);

            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

            File file = new File(path);
            if (file.exists()){
                file.delete();
            }


            if(file.createNewFile()){
                FileOutputStream outStream = new FileOutputStream(file);
                byte[] buff = new byte[5 * 1024];

                int len;
                while ((len = inStream.read(buff)) != -1)
                {
                    outStream.write(buff, 0, len);
                }

                outStream.flush();
                outStream.close();
            } else {
                resp= ConnectionState.Error;
            }

            inStream.close();
        } catch (IOException e){
            Log.e("FileDownloader", e.getMessage());
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

}
