package pl.dawidkulpa.knj;


import java.sql.Time;

public class Lesson {
    User coach;

    String subject;
    int level;
    Time startTime;
    int lenMin;

    public Lesson(){
        startTime= new Time(65700000);
        lenMin=90;
    }

    public Lesson(String subject, int level, Time startTime, int lenMin) {
        this.subject = subject;
        this.level = level;
        this.startTime = startTime;
        this.lenMin = lenMin;
    }

    public String getSubject() {
        return subject;
    }

    public int getLevel() {
        return level;
    }

    public Time getStartTime() {
        return startTime;
    }

    public int getLenMin() {
        return lenMin;
    }
}
