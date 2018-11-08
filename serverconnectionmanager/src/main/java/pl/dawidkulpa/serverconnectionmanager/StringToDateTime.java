package pl.dawidkulpa.serverconnectionmanager;

import java.sql.Timestamp;

public class StringToDateTime {
    public static Timestamp convert(String dt){
        String[] dtSplit= dt.split(" ");
        String[] dSplit= dtSplit[0].split("-");
        String[] tSplit= dtSplit[1].split(":");

        @SuppressWarnings("deprecation")
        Timestamp ts= new Timestamp(Integer.valueOf(dSplit[0]), Integer.valueOf(dSplit[1]),
                Integer.valueOf(dSplit[2]), Integer.valueOf(tSplit[0]), Integer.valueOf(tSplit[1]),
                Integer.valueOf(tSplit[2]),0);

        return ts;
    }
}
