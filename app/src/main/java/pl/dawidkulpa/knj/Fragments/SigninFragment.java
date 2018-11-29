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

import org.json.JSONObject;

import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class SigninFragment extends Fragment {

    private OnSignInListener onSignInListener;

    public SigninFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SigninFragment newInstance() {
        SigninFragment fragment = new SigninFragment();
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
        View layout= inflater.inflate(R.layout.fragment_signin, container, false);

        layout.findViewById(R.id.signin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInClick(view);
            }
        });

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignInListener) {
           onSignInListener = (OnSignInListener) context;
        } else {
        throw new RuntimeException(context.toString()
                  + " must implement OnRegisterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onSignInClick(View v){
        EditText firstnameEdit= getView().findViewById(R.id.firstname_edit);
        EditText lastnameEdit= getView().findViewById(R.id.lastname_edit);
        EditText emailEdit= getView().findViewById(R.id.email_edit);
        EditText passEdit= getView().findViewById(R.id.password_edit);

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int rCode, JSONObject jObject) {
                onRegisterFinished(rCode, jObject);
            }
        }, Query.BuildType.JSONPatch);

        Query userCreateDTO= new Query();
        userCreateDTO.addPair("firstName", firstnameEdit.getText().toString());
        userCreateDTO.addPair("lastName", lastnameEdit.getText().toString());
        userCreateDTO.addPair("password", passEdit.getText().toString());
        userCreateDTO.addPair("email", emailEdit.getText().toString());
        userCreateDTO.addPair("privacyPolicesConfirmed", "true");
        scm.addPOSTPair("", userCreateDTO);
        scm.setContentType(ServerConnectionManager.CONTENTTYPE_JSONPATCH);
        scm.setMethod(ServerConnectionManager.METHOD_POST);
        scm.start("https://korepetycjenajuzapi.azurewebsites.net/api/Users/Create");
        getView().findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
    }

    public void onRegisterFinished(int rCode, JSONObject jObj){
        getView().findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);

        if(rCode==201){
            onSignInListener.onSignInSuccess();

        } else if(rCode==400){
            Toast.makeText(getContext(),R.string.info_failed_signin,Toast.LENGTH_SHORT).show();
        }
        Log.e("JSON", jObj.toString());

    }

    public interface OnSignInListener {
        void onSignInSuccess();
    }
}
