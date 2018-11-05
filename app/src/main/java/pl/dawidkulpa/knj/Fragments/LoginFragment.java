package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;

public class LoginFragment extends Fragment {

    private OnLoginListener onLoginListener;

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
        onLoginListener.onLoginAcquired(new User());
    }


    public interface OnLoginListener {
        void onLoginAcquired(User user);
    }
}
