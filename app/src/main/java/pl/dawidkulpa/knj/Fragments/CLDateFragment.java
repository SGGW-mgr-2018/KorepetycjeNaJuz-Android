package pl.dawidkulpa.knj.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.dawidkulpa.knj.R;

public class CLDateFragment extends Fragment {
    public CLDateFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CLDateFragment newInstance() {
        CLDateFragment fragment = new CLDateFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cl_date, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof OnFragmentInteractionListener) {
        //   mListener = (OnFragmentInteractionListener) context;
        //} else {
        //throw new RuntimeException(context.toString()
        //          + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
