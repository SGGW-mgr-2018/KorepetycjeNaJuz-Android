package pl.dawidkulpa.knj.Dialogs.CreateLesson;

import android.support.v4.app.Fragment;

import pl.dawidkulpa.knj.Lessons.CoachLesson;

public abstract class CLFragment extends Fragment {
    public abstract void putOnView(CoachLesson coachLesson);
    public abstract boolean getherData(CoachLesson coachLesson);
}
