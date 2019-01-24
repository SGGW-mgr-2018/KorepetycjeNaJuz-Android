package pl.dawidkulpa.knj.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.Lessons.Lesson;
import pl.dawidkulpa.knj.Lessons.LessonMapMarker;
import pl.dawidkulpa.knj.Lessons.LessonsManager;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class LessonDescriptionDialog {
    private Context context;
    private LessonMapMarker lessonMarker;
    private User user;

    public LessonDescriptionDialog(Context context, LessonMapMarker lessonMarker, User user){
        this.context= context;
        this.lessonMarker= lessonMarker;
        this.user= user;
    }

    public void show(){
        AlertDialog.Builder adBuilder= new AlertDialog.Builder(context, R.style.AppTheme_CustomDialog);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.dialog_lesson_description, null);
        Lesson lesson= lessonMarker.getLesson();

        ((TextView)dialogView.findViewById(R.id.subject_text)).setText(lesson.getSubject());
        ((TextView)dialogView.findViewById(R.id.level_text)).setText(lesson.getLevelsAsOne());
        ((TextView)dialogView.findViewById(R.id.rateph_text)).setText(String.valueOf(lesson.getRatePH())+" zł/h");
        ((TextView)dialogView.findViewById(R.id.time_text)).setText(lesson.getTime()+" min");

        ((TextView)dialogView.findViewById(R.id.start_text)).setText(lesson.getDateStartString());
        ((TextView)dialogView.findViewById(R.id.end_text)).setText(lesson.getDateEndString());

        ((TextView)dialogView.findViewById(R.id.coach_name_text)).setText(lesson.getCoachName());
        ((TextView)dialogView.findViewById(R.id.description_text)).setText(lesson.getDescription());


        int rating= lesson.getCoachRating();
        LinearLayout ratingBox= dialogView.findViewById(R.id.rating_box);
        for(int i=0; i<rating; i++){
            ((ImageView)ratingBox.getChildAt(i)).setImageDrawable(context.getDrawable(R.drawable.star_full));
        }

        adBuilder.setView(dialogView);

        adBuilder.setNegativeButton(R.string.button_send_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSendMessageClick();
            }
        });

        adBuilder.setNeutralButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        adBuilder.setPositiveButton(R.string.button_lesson_signin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveClick();
            }
        });

        AlertDialog dialog= adBuilder.create();
        dialog.show();
    }

    private void onPositiveClick(){
        if(user!=null && user.isLoggedin()){

            if(user.getId()==lessonMarker.getLesson().getCoachId()){
                ((HomeActivity)context).putSnackbar(context.getString(R.string.info_its_your_lesson));
                return;
            }

            ServerConnectionManager scm= new ServerConnectionManager(Query.BuildType.JSONPatch,
                    new ServerConnectionManager.OnFinishListener() {
                        @Override
                        public void onFinish(int respCode, JSONObject jObject) {
                            onSignInFinished(respCode);
                        }
                    });
            scm.setMethod(ServerConnectionManager.METHOD_POST);
            scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
            scm.addHeaderEntry("Authorization", "Bearer "+user.getLoginToken());

            Query lessonCreateDTO= new Query();
            lessonCreateDTO.addPair("coachLessonId", String.valueOf(lessonMarker.getLesson().getId()));

            scm.addPOSTPair("", lessonCreateDTO);
            scm.start(HomeActivity.SERVER_NAME+"/Lesson/Create");
        } else {
            ((HomeActivity)context).putSnackbar(context.getString(R.string.info_first_login));
        }
    }

    private void onSendMessageClick(){
        if(user!=null && user.isLoggedin()) {
            Lesson lesson = lessonMarker.getLesson();
            ((HomeActivity) context).showConversation(lesson.getCoachId(), lesson.getCoachName());
        }else {
            ((HomeActivity)context).putSnackbar(context.getString(R.string.info_first_login));
        }
    }

    private void onSignInFinished(int rCode){
        if(rCode==201){
            Snackbar.make(((Activity)context).findViewById(R.id.fab),R.string.info_lesson_signin_success, Snackbar.LENGTH_SHORT).show();
        } else if(rCode==400) {
            Snackbar.make(((Activity)context).findViewById(R.id.fab),R.string.info_lesson_already_signin, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(((Activity)context).findViewById(R.id.fab),R.string.info_server_error, Snackbar.LENGTH_SHORT).show();
        }
    }
}
