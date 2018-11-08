package pl.dawidkulpa.serverconnectionmanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.crypto.SecretKey;

public class Query {
    private ArrayList<String> names;
    private ArrayList<String> values;

    public Query(){
        names= new ArrayList<>();
        values= new ArrayList<>();
    }

    public void addPair(String name, String value){
        names.add(name);
        values.add(value);
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

    public String build(){
        String queryString="";
        boolean first=true;

        for(int i=0; i<names.size(); i++){
            if(!first){
                queryString+="&";
            }

            queryString+=names.get(i)+"="+values.get(i);

            first=false;
        }
        
        return queryString;
    }
}
