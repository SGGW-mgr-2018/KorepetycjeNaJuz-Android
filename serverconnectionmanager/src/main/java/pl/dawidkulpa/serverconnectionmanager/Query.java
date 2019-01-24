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

        String val= "{";
        val+= obj.build(BuildType.JSONPatch);
        val+= "}";

        values.add(val);
    }

    public void addPair(String name, ArrayList<String> list){
        names.add(name);

        StringBuilder sb= new StringBuilder();

        sb.append("[");

        for(int i=0; i<list.size(); i++){
            sb.append(list.get(i));

            if(i<list.size()-1)
                sb.append(", ");
        }

        sb.append("]");
        values.add(sb.toString());
    }

    boolean encryptValue(SecretKey key){
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
        StringBuilder sb= new StringBuilder();

        for(int i=0; i<this.size(); i++){

            if(buildType==BuildType.Pairs) {
                if (i > 0) {
                    sb.append("&");
                }
                sb.append(names.get(i));
                sb.append("=");
                sb.append(values.get(i));
            } else if(buildType==BuildType.JSONPatch){

                //If name is not empty write its name
                if(!names.get(i).equals("")){
                    sb.append("\"");
                    sb.append(names.get(i));
                    sb.append("\": ");
                }

                //If value first char is not { and [ start with "
                if(values.get(i).isEmpty() || ( values.get(i).charAt(0)!='{' && values.get(i).charAt(0)!='[')){
                    sb.append("\"");
                }

                //Add value
                sb.append(values.get(i));

                //If value first char is { and [ end with "
                if(values.get(i).isEmpty() || (values.get(i).charAt(0)!='{' && values.get(i).charAt(0)!='[')){
                    sb.append("\"");
                }

                if(i<this.size()-1)
                    sb.append(", ");
            }
        }

        Log.e("Query", sb.toString());
        
        return sb.toString();
    }
}
