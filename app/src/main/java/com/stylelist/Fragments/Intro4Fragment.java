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
public class Intro4Fragment extends Fragment {


    public Intro4Fragment() {
        // Required empty public constructor
    }

    public static Intro4Fragment newInstance() {
        Intro4Fragment fragment = new Intro4Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro4, container, false);
        return view;
    }

}
