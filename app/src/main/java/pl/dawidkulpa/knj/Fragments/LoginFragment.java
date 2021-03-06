package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;


public class LoginFragment extends Fragment {

    private OnLoginListener onLoginListener;
    private User user;

    public LoginFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout= inflater.inflate(R.layout.fragment_login, container, false);

        layout.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick(view);
            }
        });

        // Inflate the layout for this fragment
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginListener) {
           onLoginListener = (OnLoginListener) context;
        } else {
        throw new RuntimeException(context.toString()
                  + " must implement OnLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onLoginClick(View v){
        EditText emailEdit= getView().findViewById(R.id.email_edit);
        EditText passEdit= getView().findViewById(R.id.password_edit);

        if(emailEdit.getText().toString().isEmpty() || passEdit.getText().toString().isEmpty()){
            ((HomeActivity)getContext()).putSnackbar(getString(R.string.info_enter_email_pass));

            return;
        }

        user= new User();
        user.setEmail(emailEdit.getText().toString());
        user.setPassword(passEdit.getText().toString());

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int rCode, JSONObject jObject) {
                onLoginFinished(rCode, jObject);
            }
        }, Query.BuildType.JSONPatch);

        Query userLoginDto= new Query();
        userLoginDto.addPair("username", user.getEmail());
        userLoginDto.addPair("password", user.getPassword());

        scm.addPOSTPair("", userLoginDto);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.setMethod(ServerConnectionManager.METHOD_POST);
        scm.start(HomeActivity.SERVER_NAME+"/Authorization/Login");
        getView().findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
    }

    private void onLoginFinished(int rCode, JSONObject jObj){
        if(rCode==200){
            user.onLoginSuccessful(jObj);
            ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
                @Override
                public void onFinish(int respCode, JSONObject jObject) {
                    onGetDataFinished(respCode, jObject);
                }
            }, Query.BuildType.Pairs);
            scm.addHeaderEntry("Authorization", "Bearer "+user.getLoginToken());
            scm.setMethod(ServerConnectionManager.METHOD_GET);
            scm.start(HomeActivity.SERVER_NAME+"/Users/Get/"+user.getId());
        } else if(rCode==400){
            getView().findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(),R.string.info_failed_login,Toast.LENGTH_SHORT).show();
        } else if(rCode==403){
            getView().findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(),R.string.info_failed_login,Toast.LENGTH_SHORT).show();
        }
    }

    private void onGetDataFinished(int rCode, JSONObject jObj){
        getView().findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
        if(rCode==200){
            user.onGetDataSuccessful(jObj);
            onLoginListener.onLoginAcquired(user);
        } else {
            Toast.makeText(getContext(), R.string.info_server_error, Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnLoginListener {
        void onLoginAcquired(User user);
    }
}
