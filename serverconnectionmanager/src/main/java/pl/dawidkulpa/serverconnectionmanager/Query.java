package pl.dawidkulpa.serverconnectionmanager;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.crypto.SecretKey;

public class Query {
    public enum  BuildType{
        Pairs,
        JSONPatch
    }

    private ArrayList<String> names;
    private ArrayList<String> values;

    public Query(){
        names= new ArrayList<>();
        values= new ArrayList<>();
    }

    public int size(){
        return names.size();
    }

    public void addPair(String name, String value){
        names.add(name);
        values.add(value);
    }

    public void addPair(String name, Query obj){
        names.add(name);

        String valString="{";

        for(int i=0; i<obj.size(); i++){
            valString+= "\""+obj.names.get(i)+"\": \""+obj.values.get(i)+"\"";
            if(i<obj.size()-1)
                valString+=", ";
        }

        valString+="}";
        values.add(valString);
    }

    public boolean encryptValue(SecretKey key){
        byte[] rawSecret;

        for(int i=0; i<values.size(); i++){
            rawSecret= AESCipher.encrypt(key, values.get(i));
            if(rawSecret==null)
                return false;

            try {
                values.set(i, new String(rawSecret, "UTF-8"));
            } catch (UnsupportedEncodingException uee){
                return false;
            }
        }

        return true;
    }

    public String build(BuildType buildType){
        String queryString="";


        for(int i=0; i<this.size(); i++){

            if(buildType==BuildType.Pairs) {
                if (i > 0) {
                    queryString += "&";
                }
                queryString += names.get(i) + "=" + values.get(i);
            } else if(buildType==BuildType.JSONPatch){

                if(!names.get(i).equals("")){
                    queryString+="\""+names.get(i)+"\":";
                }

                if(values.get(i).charAt(0)!='{'){
                    queryString+="\"";
                }
                queryString += values.get(i);
                if(values.get(i).charAt(0)!='{'){
                    queryString+="\"";
                }

                if(i<this.size()-1)
                    queryString+=", ";
            }
        }
        
        return queryString;
    }
}
