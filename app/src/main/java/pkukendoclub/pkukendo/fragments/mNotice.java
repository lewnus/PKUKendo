package pkukendoclub.pkukendo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import pkukendoclub.pkukendo.R;

public class mNotice extends Fragment {

    ImageButton b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_m_notice, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

    }


}