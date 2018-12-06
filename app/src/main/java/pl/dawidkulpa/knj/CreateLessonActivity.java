package pl.dawidkulpa.knj;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Fragments.CLAddressFragment;
import pl.dawidkulpa.knj.Fragments.CLSubjectFragment;
import pl.dawidkulpa.knj.Fragments.CLDateFragment;

public class CreateLessonActivity extends AppCompatActivity {

    private static final int STEPS_NO=3;

    private int step;
    private FragmentManager fragmentManager;

    private ArrayList<Fragment> appFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);

        fragmentManager= getSupportFragmentManager();

        appFragments= new ArrayList<>();
        appFragments.add(CLSubjectFragment.newInstance());
        appFragments.add(CLDateFragment.newInstance());
        appFragments.add(CLAddressFragment.newInstance());

        step=0;
        switchFragment(step);
    }

    public void onNegButtonClick(View v){
        finish();
    }

    private void switchFragment(int id){
        FragmentTransaction transaction= fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, appFragments.get(id));
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void onPosButtonClick(View v){
        if(step<STEPS_NO-1){
            step++;

            switchFragment(step);

            if(step==STEPS_NO-1)
                ((Button)findViewById(R.id.pos_button)).setText("Utwórz");
        } else {
            finish();
        }

    }

    public void onBackButtonClick(View v){
        if(step>0){
            step--;

            switchFragment(step);
        }
    }

}
