package pkukendoclub.pkukendo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import pkukendoclub.pkukendo.*;

public class mMe extends Fragment {

    private View mName;
    private View mPassword;
    private TableRow mNote;
    private TableRow AboutUs;

    private TextView text_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_m_me, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){

        mName = getActivity().findViewById(R.id.mUser);
        mPassword = getActivity().findViewById(R.id.password);
        mNote = (TableRow) getActivity().findViewById(R.id.mynote);
        AboutUs = (TableRow) getActivity().findViewById(R.id.aboutus);

        text_name = (TextView) getActivity().findViewById(R.id.uname);


        text_name.setText("孙伟");

        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mName.class);
                startActivity(intent);
            }
        });

        mNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mNote.class);
                startActivity(intent);
            }
        });

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mPassword.class);
                startActivity(intent);
            }
        });

        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Aboutus.class);
                startActivity(intent);
            }
        });

    }
}