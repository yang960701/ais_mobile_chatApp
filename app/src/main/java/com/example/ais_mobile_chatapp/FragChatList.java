package com.example.ais_mobile_chatapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapp.R;

public class FragChatList extends Fragment {
    private View view;
    //어뎁터에 맞춰서 주기적으로 인스턴스가 생성 상태저장
    public static FragChatList newInstance(){
        FragChatList fragChatList = new FragChatList();
        return fragChatList;
    };
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_chatlist, container,false);
        return view;
    }
}
