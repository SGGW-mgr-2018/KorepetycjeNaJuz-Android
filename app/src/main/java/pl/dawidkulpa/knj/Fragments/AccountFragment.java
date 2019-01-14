package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.dawidkulpa.knj.HomeActivity;
import pl.dawidkulpa.knj.R;
import pl.dawidkulpa.knj.User;

public class AccountFragment extends Fragment {

    private OnSaveListener saveClickListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
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
        View layout= inflater.inflate(R.layout.fragment_account, container, false);

        layout.findViewById(R.id.change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeClick(view);
            }
        });
        layout.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClick(view);
            }
        });

        User user= ((HomeActivity)getContext()).getLogedInUser();
        ((TextView) (layout.findViewById(R.id.name_value))).setText(user.getName());
        ((TextView) (layout.findViewById(R.id.sname_value))).setText(user.getSname());
        ((TextView) (layout.findViewById(R.id.email_value))).setText(user.getEmail());
        ((TextView) (layout.findViewById(R.id.phone_no_value))).setText(user.getPhoneNo());
        ((TextView) (layout.findViewById(R.id.about_me_value))).setText(user.getAboutMe());
        ((TextView) (layout.findViewById(R.id.phone_no_edit))).setText(user.getPhoneNo());
        ((TextView) (layout.findViewById(R.id.about_me_edit))).setText(user.getAboutMe());


        // Inflate the layout for this fragment
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnLoginListener) {
            saveClickListener = (OnSaveListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void onChangeClick(View view){
        View v= getView().findViewById(R.id.show_data_container);
        v.setVisibility(View.GONE);
        v= getView().findViewById(R.id.edit_data_container);
        v.setVisibility(View.VISIBLE);
    }

    private void onSaveClick(View view){
        String newPhoneNo;
        String newAboutMe;
        String newPass;

        newPhoneNo= ((EditText)getView().findViewById(R.id.phone_no_edit)).getText().toString();
        newAboutMe= ((EditText)getView().findViewById(R.id.about_me_edit)).getText().toString();
        newPass=    ((EditText)getView().findViewById(R.id.password_edit)).getText().toString();

        ((HomeActivity)getContext()).getLogedInUser().updateMyData(newPhoneNo, newAboutMe, newPass, new User.UpdateFinishListener() {
            @Override
            public void onUpdateFinished(int rCode) {
                onUpdateDataFinished(rCode);
            }
        });
    }

    public void onUpdateDataFinished(int rCode){
        if(rCode==200){
            View layout= getView();
            User user= ((HomeActivity)getContext()).getLogedInUser();
            ((TextView) (layout.findViewById(R.id.name_value))).setText(user.getName());
            ((TextView) (layout.findViewById(R.id.sname_value))).setText(user.getSname());
            ((TextView) (layout.findViewById(R.id.email_value))).setText(user.getEmail());
            ((TextView) (layout.findViewById(R.id.phone_no_value))).setText(user.getPhoneNo());
            ((TextView) (layout.findViewById(R.id.about_me_value))).setText(user.getAboutMe());
            ((TextView) (layout.findViewById(R.id.phone_no_edit))).setText(user.getPhoneNo());
            ((TextView) (layout.findViewById(R.id.about_me_edit))).setText(user.getAboutMe());

            View v = getView().findViewById(R.id.show_data_container);
            v.setVisibility(View.VISIBLE);
            v = getView().findViewById(R.id.edit_data_container);
            v.setVisibility(View.GONE);
            saveClickListener.onDataSaveSuccessful(null);
        } else {
            Toast.makeText(getContext(), "Server connection error", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnSaveListener {
        void onDataSaveSuccessful(User user);
    }
}
