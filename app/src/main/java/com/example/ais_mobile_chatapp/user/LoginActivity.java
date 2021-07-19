package com.example.ais_mobile_chatapp.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ais_mobile_chatapp.MainActivity;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    //파이어베이스 인증처리
    private FirebaseAuth nFirebaseAuth;
    //실시간 데이터베이스
    private DatabaseReference nDatabaseRef;
    private EditText et_Email, et_pwd;
    private LinearLayout linearLayout_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nFirebaseAuth = FirebaseAuth.getInstance();
        nDatabaseRef = FirebaseDatabase.getInstance().getReference("ChatAPP");

        et_Email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);

        linearLayout_login = findViewById(R.id.LinerarLayout_login);

        linearLayout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 요청
                String strEmail = et_Email.getText().toString();
                String strPwd = et_pwd.getText().toString();

                if(et_Email.length()>0 && et_pwd.length()>0){
                nFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //로그인 성공
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();//현재 액티비티 파괴
                        }else{
                            //로그인 실패
                            if(task.getException() != null)
                            Toast.makeText(LoginActivity.this, "잘못된 이메일 혹은 비밀번호 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });}else{
                    //이메일 혹은 비밀번호 미입력
                    Toast.makeText(LoginActivity.this, "이메일 혹은 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        TextView tv_register = findViewById(R.id.tv_register);
        TextView tv_psreset = findViewById(R.id.gotopsresetTv);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 버튼을 눌렀을때에 대한 처리
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        tv_psreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });
    }
}