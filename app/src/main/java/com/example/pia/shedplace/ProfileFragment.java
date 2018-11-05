package com.example.pia.shedplace;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnprofilesave = (Button) getActivity().findViewById(R.id.profileSave);
        Button btncancel = (Button) getActivity().findViewById(R.id.cancel);
        final EditText user = (EditText) getActivity().findViewById(R.id.username);
        final EditText pass1 = (EditText) getActivity().findViewById(R.id.password1);
        final EditText pass2 = (EditText) getActivity().findViewById(R.id.password2);

        btnprofilesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.isEmpty(user) && !MainActivity.isEmpty(pass1) && !MainActivity.isEmpty(pass2) && (pass1.getText().toString()).equals(pass2.getText().toString())) {
                    MainActivity.username = user.getText().toString();
                    Toast.makeText(getActivity(), "Profile saved!", Toast.LENGTH_SHORT).show();
                    user.setText("");
                    pass1.setText("");
                    pass2.setText("");
                } else {
                    Toast.makeText(getActivity(), "verify username and passwords", Toast.LENGTH_SHORT).show();
                }

            }

        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment frag = new HomeFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.shedPlace,frag);
                                ft.commit();






            }
        });

    }
}


