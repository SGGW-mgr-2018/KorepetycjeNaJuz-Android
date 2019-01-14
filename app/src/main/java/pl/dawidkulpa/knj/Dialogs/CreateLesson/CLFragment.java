package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.support.v4.app.Fragment;

import pl.dawidkulpa.knj.Lessons.CoachLesson;
import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.Lessons.LessonBuilder;

public abstract class CLFragment extends Fragment {
    protected LessonBuilder lessonBuilder;

    public void setLessonObj(LessonBuilder lessonBuilder){
        this.lessonBuilder= lessonBuilder;
    }
    public abstract boolean gatherData();
    public abstract String checkProperties();
}
