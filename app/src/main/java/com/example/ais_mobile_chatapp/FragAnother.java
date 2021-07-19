package com.example.ais_mobile_chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.ais_mobile_chatapp.user.LoginActivity;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragAnother#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragAnother extends Fragment {

    //로그아웃

    //초기화
    //파이어베이스 인증처리
    private FirebaseAuth nFirebaseAuth;
    //실시간 데이터베이스
    private DatabaseReference nDatabaseRef;
    private Button btn_logout;
    private View view;


    public static FragAnother newInstance(){
        FragAnother fragAnother = new FragAnother();
        return fragAnother;
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_frag_another, container, false);

        nFirebaseAuth = FirebaseAuth.getInstance();
        btn_logout = view.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nFirebaseAuth.signOut();
                //프래그먼트는 부모를 불러와야한다.
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);


            }
        });

        return view;
    }


}