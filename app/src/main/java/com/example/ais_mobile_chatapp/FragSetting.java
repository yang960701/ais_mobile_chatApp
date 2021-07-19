package com.example.ais_mobile_chatapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.chatapp.R;

public class FragSetting extends Fragment {
    private View view;
    public static FragSetting newInstance(){
        FragSetting fragSetting = new FragSetting();
        return fragSetting;
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(
                R.layout.frag_setting, container, false);
    }
}
