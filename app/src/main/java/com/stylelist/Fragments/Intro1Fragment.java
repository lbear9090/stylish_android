package com.stylelist.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stylelist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Intro1Fragment extends Fragment {


    public Intro1Fragment() {
        // Required empty public constructor
    }

    public static Intro1Fragment newInstance() {
        Intro1Fragment fragment = new Intro1Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro1, container, false);
    }

}
